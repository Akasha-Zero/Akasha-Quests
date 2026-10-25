#!/usr/bin/env python3
"""Akasha Quests generator: YAML chapters -> BetterQuesting 3.8.79-GTNH DefaultQuests.

Usage:
    python3 tools/bqgen/bqgen.py build   [--strict]
    python3 tools/bqgen/bqgen.py lint    [chapter.yaml ...]
    python3 tools/bqgen/bqgen.py stats

The authoring format is documented in docs/AUTHORING.md.
"""

import argparse
import base64
import glob
import gzip
import json
import os
import re
import shutil
import struct
import sys
import uuid
from collections import OrderedDict, defaultdict

import yaml

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import snbt  # noqa: E402

ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..', '..'))
CHAPTER_DIR = os.path.join(ROOT, 'quests', 'chapters')
LOOT_FILE = os.path.join(ROOT, 'quests', 'loot', 'loot.yaml')
SETTINGS_FILE = os.path.join(ROOT, 'quests', 'settings.yaml')
REGISTRY_DIR = os.path.join(ROOT, 'registry')
OUT_DIR = os.path.join(ROOT, 'dist', 'config', 'betterquesting')

NAMESPACE = uuid.UUID('6f1c2a7e-4b8d-5e3a-9c0f-a1ca5a000000')
WILDCARD = 32767

PREFIXES = {'': '', None: '', 'ATM': 'ATM: ', 'AEDF3': 'AEDF3: '}

LOOT_TIERS = OrderedDict([
    ('comum', 0), ('incomum', 25), ('raro', 50), ('epico', 75), ('lendario', 100),
])

LOOT_NAMES = {'comum': '§fBaú de Saque Comum', 'incomum': '§aBaú de Saque Incomum',
              'raro': '§9Baú de Saque Raro', 'epico': '§5Baú de Saque Épico',
              'lendario': '§6Baú de Saque Lendário'}

# Quest "type" presets: icon size on the map, main flag, notification extras.
TYPES = {
    'normal':    dict(size=24, main=False),
    'main':      dict(size=32, main=True),
    'optional':  dict(size=24, main=False),
    'info':      dict(size=24, main=False),
    'milestone': dict(size=40, main=True, effect=3, particle='firework', anim='slam'),
    'boss':      dict(size=40, main=True, effect=3, particle='firework', anim='slam'),
    'portal':    dict(size=32, main=True, effect=5, particle='sparkle', anim='zoom'),
    'finale':    dict(size=48, main=True, effect=6, particle='item_confetti', anim='tada'),
    'secret':    dict(size=24, main=False, visibility='SECRET'),
}

VISIBILITIES = {'normal': 'NORMAL', 'unlocked': 'UNLOCKED', 'hidden': 'HIDDEN',
                'secret': 'SECRET', 'chain': 'CHAIN', 'always': 'ALWAYS', 'completed': 'COMPLETED'}


class GenError(Exception):
    pass


# --------------------------------------------------------------------------- utils

def encode_uuid(u):
    hi, lo = struct.unpack('>qq', u.bytes)
    return base64.urlsafe_b64encode(struct.pack('>qq', hi, lo)).decode()


def uuid_parts(u):
    return struct.unpack('>qq', u.bytes)


def quest_uuid(chapter_id, quest_id):
    return uuid.uuid5(NAMESPACE, 'akasha:quest:%s:%s' % (chapter_id, quest_id))


def line_uuid(chapter_id):
    return uuid.uuid5(NAMESPACE, 'akasha:line:%s' % chapter_id)


def strip_fmt(s):
    return re.sub('§[0-9a-fk-orxgqzv]', '', s)


def file_stub(name, u):
    n = re.sub('[^a-zA-Z0-9]', '', strip_fmt(name))[:16]
    return '%s-%s' % (n, encode_uuid(u))


def dump_json(path, obj):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, 'w', encoding='utf-8') as f:
        json.dump(obj, f, indent=2, ensure_ascii=False, sort_keys=True)
        f.write('\n')


# --------------------------------------------------------------------------- registry

class Registry:
    """Item/entity/fluid/dimension index built from the in-game dump."""

    def __init__(self, path=REGISTRY_DIR):
        self.items = {}          # (id, meta) -> record
        self.by_id = defaultdict(list)
        self.ores = {}
        self.entities = set()
        self.fluids = set()
        self.dims = {}
        self.biomes = {}
        self.loaded = False
        items = os.path.join(path, 'items.jsonl.gz')
        if not os.path.exists(items):
            return
        with gzip.open(items, 'rt', encoding='utf-8') as f:
            for line in f:
                r = json.loads(line)
                key = (r['id'], r['meta'])
                if key not in self.items:
                    self.items[key] = r
                self.by_id[r['id']].append(r)
        self.ores = self._load_map('oredict.jsonl.gz', 'ore')
        for r in self._iter('entities.jsonl.gz'):
            self.entities.add(r['id'])
        for r in self._iter('fluids.jsonl.gz'):
            if 'id' in r:
                self.fluids.add(r['id'])
        for r in self._iter('dimensions.jsonl.gz'):
            self.dims[r['num']] = r.get('name', '')
        for r in self._iter('biomes.jsonl.gz'):
            self.biomes[r['num']] = r['name']
        self.loaded = True

    def _iter(self, name):
        p = os.path.join(REGISTRY_DIR, name)
        if not os.path.exists(p):
            return
        with gzip.open(p, 'rt', encoding='utf-8') as f:
            for line in f:
                yield json.loads(line)

    def _load_map(self, name, key):
        return {r[key]: r for r in self._iter(name)}

    def check_item(self, iid, meta, ore):
        """Return None if valid, else an error message."""
        if not self.loaded:
            return None
        if ore:
            if ore not in self.ores or not self.ores[ore]['items']:
                return 'ore dictionary name %r has no items' % ore
            return None
        if iid not in self.by_id:
            return 'unknown item id %r' % iid
        if meta == WILDCARD or (iid, meta) in self.items:
            return None
        recs = self.by_id[iid]
        if any(r.get('maxDmg', 0) > 0 for r in recs):
            return None  # damageable: any damage value is a valid stack
        metas = sorted({r['meta'] for r in recs})
        return 'item %s has no meta %d (known: %s)' % (iid, meta, metas[:24])

    def damageable(self, iid):
        return any(r.get('maxDmg', 0) > 0 for r in self.by_id.get(iid, ()))

    def name_of(self, iid, meta):
        r = self.items.get((iid, meta)) or (self.by_id.get(iid) or [None])[0]
        return r.get('name_pt') or r.get('name') if r else None

    def ore_first(self, ore):
        r = self.ores.get(ore)
        if r and r['items']:
            it = r['items'][0]
            return it['id'], (it['meta'] if it['meta'] != WILDCARD else 0)
        return None


# --------------------------------------------------------------------------- item specs

_SPEC = re.compile(r'^\s*(?P<id>[^\s{]+?)(?::(?P<meta>\d+|\*))?(?:\s+x\s*(?P<n>\d+))?(?:\s+(?P<nbt>\{.*\}))?\s*$')


def parse_item(spec, ctx, reg, default_count=1):
    """Parse an item spec into BetterQuesting JSON.

    Forms: "mod:name", "mod:name:meta", "mod:name:* x4", "ore:ingotCopper x8",
    "mod:name:2 x1 {display:{Name:\"X\"}}" or a mapping {id, meta, count, nbt, ore}.
    """
    if isinstance(spec, dict):
        iid = spec.get('id')
        meta = spec.get('meta', 0)
        count = spec.get('count', default_count)
        nbt = spec.get('nbt')
        ore = spec.get('ore', '')
        if meta == '*':
            meta = WILDCARD
    else:
        m = _SPEC.match(str(spec))
        if not m:
            raise GenError('%s: bad item spec %r' % (ctx, spec))
        iid = m.group('id')
        meta = m.group('meta')
        meta = WILDCARD if meta == '*' else int(meta or 0)
        count = int(m.group('n') or default_count)
        nbt = m.group('nbt')
        ore = ''
        if iid.startswith('ore:'):
            ore = iid[4:]
            iid = None
    if ore and not iid:
        first = reg.ore_first(ore)
        iid, meta = first if first else ('minecraft:stone', 0)
    if iid and iid.count(':') != 1:
        raise GenError('%s: item id must be modid:name, got %r' % (ctx, iid))
    err = reg.check_item(iid, meta, ore)
    if err:
        raise GenError('%s: %s' % (ctx, err))
    out = OrderedDict()
    out['Count:3'] = int(count)
    out['Damage:2'] = int(meta)
    out['OreDict:8'] = ore or ''
    out['id:8'] = iid
    if nbt:
        try:
            out['tag:10'] = snbt.parse(nbt) if isinstance(nbt, str) else nbt
        except snbt.SnbtError as e:
            raise GenError('%s: %s' % (ctx, e))
    return out


def as_list(v):
    if v is None:
        return []
    return v if isinstance(v, list) else [v]


# --------------------------------------------------------------------------- tasks

def _item_list(v, ctx, reg, task=False):
    out = OrderedDict()
    for i, s in enumerate(as_list(v)):
        it = parse_item(s, '%s[%d]' % (ctx, i), reg)
        # Tools, armour and electric items change damage with use/charge: match any damage.
        if task and not it['OreDict:8'] and reg.damageable(it['id:8']):
            it['Damage:2'] = WILDCARD
        out['%d:10' % i] = it
    return out


def build_task(t, idx, ctx, reg):
    if isinstance(t, str):
        t = {t: True}
    if not isinstance(t, dict) or not t:
        raise GenError('%s: bad task %r' % (ctx, t))
    kind = next(iter(t))
    v = t[kind]
    opts = {k: t[k] for k in t if k != kind}
    c = '%s.task%d(%s)' % (ctx, idx, kind)
    o = OrderedDict()
    if kind in ('get', 'consume', 'optional', 'one_of'):
        o['autoConsume:1'] = 0
        o['consume:1'] = 1 if kind == 'consume' or opts.get('consume') else 0
        o['groupDetect:1'] = 0
        o['ignoreNBT:1'] = 0 if opts.get('nbt') else 1
        o['index:3'] = idx
        o['partialMatch:1'] = 1
        o['requireOnlyOneItem:1'] = 1 if kind == 'one_of' else 0
        o['requiredItems:9'] = _item_list(v, c, reg, task=True)
        o['taskID:8'] = 'bq_standard:optional_retrieval' if kind == 'optional' else 'bq_standard:retrieval'
    elif kind == 'craft':
        o['allowAnvil:1'] = 1 if opts.get('anvil') else 0
        o['allowCraft:1'] = 1
        o['allowCraftedFromStatistics:1'] = 0
        o['allowSmelt:1'] = 1
        o['ignoreNBT:1'] = 1
        o['index:3'] = idx
        o['partialMatch:1'] = 1
        o['requiredItems:9'] = _item_list(v, c, reg, task=True)
        o['taskID:8'] = 'bq_standard:crafting'
    elif kind == 'kill':
        if isinstance(v, str):
            m = re.match(r'^\s*(\S+)(?:\s+x\s*(\d+))?\s*$', v)
            v = {'entity': m.group(1), 'count': int(m.group(2) or 1)}
        ent = v['entity']
        if reg.loaded and ent not in reg.entities:
            raise GenError('%s: unknown entity %r' % (c, ent))
        o['damageType:8'] = ''
        o['ignoreNBT:1'] = 1
        o['index:3'] = idx
        o['required:3'] = int(v.get('count', 1))
        o['subtypes:1'] = 1 if v.get('subtypes', True) else 0
        o['target:8'] = ent
        o['targetNBT:10'] = {}
        o['taskID:8'] = 'bq_standard:hunt'
    elif kind == 'visit':
        dim = int(v.get('dim', 0))
        if reg.loaded and dim not in reg.dims:
            raise GenError('%s: unknown dimension %r' % (c, dim))
        biome = int(v.get('biome', -1))
        if reg.loaded and biome != -1 and biome not in reg.biomes:
            raise GenError('%s: unknown biome %r' % (c, biome))
        o['biome:3'] = biome
        o['dimension:3'] = dim
        o['hideInfo:1'] = 1 if v.get('hide') else 0
        o['index:3'] = idx
        o['invert:1'] = 0
        o['name:8'] = v.get('name', '')
        o['posX:3'] = int(v.get('x', 0))
        o['posY:3'] = int(v.get('y', 0))
        o['posZ:3'] = int(v.get('z', 0))
        o['range:3'] = int(v.get('range', -1))
        o['structure:8'] = v.get('structure', '')
        o['taskID:8'] = 'bq_standard:location'
        o['taxiCabDist:1'] = 0
        o['visible:1'] = 1 if v.get('visible') else 0
    elif kind == 'fluid':
        fl = OrderedDict()
        for i, s in enumerate(as_list(v)):
            m = re.match(r'^\s*(\S+)(?:\s+x\s*(\d+))?\s*$', s)
            name, amt = m.group(1), int(m.group(2) or 1000)
            if reg.loaded and name not in reg.fluids:
                raise GenError('%s: unknown fluid %r' % (c, name))
            fl['%d:10' % i] = OrderedDict([('Amount:3', amt), ('FluidName:8', name)])
        o['autoConsume:1'] = 0
        o['consume:1'] = 1 if opts.get('consume') else 0
        o['groupDetect:1'] = 0
        o['ignoreNBT:1'] = 1
        o['index:3'] = idx
        o['requiredFluids:9'] = fl
        o['taskID:8'] = 'bq_standard:fluid'
    elif kind == 'xp':
        if not isinstance(v, dict):
            v = {'levels': int(v)}
        o['amount:3'] = int(v.get('levels', v.get('points', 1)))
        o['consume:1'] = 1 if v.get('consume') else 0
        o['index:3'] = idx
        o['isLevels:1'] = 0 if 'points' in v else 1
        o['taskID:8'] = 'bq_standard:xp'
    elif kind == 'check':
        o['index:3'] = idx
        o['taskID:8'] = 'bq_standard:checkbox'
    else:
        raise GenError('%s: unknown task kind %r' % (c, kind))
    return o


# --------------------------------------------------------------------------- rewards

def loot_chest(tier, count, ctx, reg):
    if tier not in LOOT_TIERS:
        raise GenError('%s: unknown loot tier %r (use %s)' % (ctx, tier, ', '.join(LOOT_TIERS)))
    tag = OrderedDict([('display:10', {'Name:8': LOOT_NAMES[tier]}), ('hideLootInfo:1', 1)])
    return OrderedDict([('Count:3', count), ('Damage:2', LOOT_TIERS[tier]), ('OreDict:8', ''),
                        ('id:8', 'bq_standard:loot_chest'), ('tag:10', tag)])


def build_reward(r, idx, ctx, reg):
    if not isinstance(r, dict) or not r:
        raise GenError('%s: bad reward %r' % (ctx, r))
    kind = next(iter(r))
    v = r[kind]
    c = '%s.reward%d(%s)' % (ctx, idx, kind)
    o = OrderedDict()
    o['ignoreDisabled:1'] = 0
    o['index:3'] = idx
    if kind in ('item', 'items'):
        o['rewardID:8'] = 'bq_standard:item'
        o['rewards:9'] = _item_list(v, c, reg)
    elif kind == 'choice':
        o['choices:9'] = _item_list(v, c, reg)
        o['rewardID:8'] = 'bq_standard:choice'
    elif kind == 'xp':
        o['amount:3'] = int(v)
        o['isLevels:1'] = 1
        o['rewardID:8'] = 'bq_standard:xp'
    elif kind == 'loot':
        items = OrderedDict()
        for i, s in enumerate(as_list(v)):
            m = re.match(r'^\s*(\w+)(?:\s+x\s*(\d+))?\s*$', s)
            if not m:
                raise GenError('%s: bad loot spec %r' % (c, s))
            items['%d:10' % i] = loot_chest(m.group(1), int(m.group(2) or 1), c, reg)
        o['rewardID:8'] = 'bq_standard:item'
        o['rewards:9'] = items
    elif kind == 'kit':
        lst = OrderedDict()
        for i, s in enumerate(as_list(v['items'])):
            it = parse_item(s, '%s[%d]' % (c, i), reg)
            lst['%d:10' % i] = it
        tag = OrderedDict([('display:10', {'Name:8': '§e' + v.get('label', v['name'])}),
                           ('fixedLootList:9', lst), ('fixedLootName:8', v['name']), ('hideLootInfo:1', 1)])
        chest = OrderedDict([('Count:3', 1), ('Damage:2', 104), ('OreDict:8', ''),
                             ('id:8', 'bq_standard:loot_chest'), ('tag:10', tag)])
        o['rewardID:8'] = 'bq_standard:item'
        o['rewards:9'] = OrderedDict([('0:10', chest)])
    elif kind == 'command':
        o['command:8'] = v if isinstance(v, str) else v['cmd']
        o['hideCommand:1'] = 1
        o['rewardID:8'] = 'bq_standard:command'
        o['viaPlayer:1'] = 0
        if isinstance(v, dict):
            o['title:8'] = v.get('title', '')
            o['desc:8'] = v.get('desc', '')
    else:
        raise GenError('%s: unknown reward kind %r' % (c, kind))
    return o


# --------------------------------------------------------------------------- model

class Quest:
    def __init__(self, chapter, data, order):
        self.chapter = chapter
        self.data = data
        self.order = order
        self.id = str(data['id'])
        self.uuid = quest_uuid(chapter.id, self.id)
        self.type = data.get('type', 'normal')
        if self.type not in TYPES:
            raise GenError('%s:%s: unknown quest type %r' % (chapter.id, self.id, self.type))
        self.deps = [self._qualify(d) for d in as_list(data.get('deps'))]
        self.hidden_deps = [self._qualify(d) for d in as_list(data.get('deps_hidden'))]
        self.pos = None

    def _qualify(self, d):
        d = str(d)
        return d if ':' in d else '%s:%s' % (self.chapter.id, d)

    @property
    def key(self):
        return '%s:%s' % (self.chapter.id, self.id)

    @property
    def size(self):
        return int(self.data.get('size', TYPES[self.type]['size']))


class Chapter:
    def __init__(self, path, data):
        self.path = path
        meta = data.get('chapter') or {}
        if 'id' not in meta:
            raise GenError('%s: missing chapter.id' % path)
        self.id = str(meta['id'])
        self.meta = meta
        self.uuid = line_uuid(self.id)
        self.prefix = meta.get('prefix', '')
        if self.prefix not in PREFIXES:
            raise GenError('%s: prefix must be one of ATM, AEDF3 or empty' % path)
        self.title = PREFIXES[self.prefix] + str(meta['title'])
        self.order = float(meta.get('order', 9999))
        self.quests = []
        self.links = []  # quests from other chapters shown here: (key, at)
        seen = set()
        for i, q in enumerate(data.get('quests') or []):
            if 'link' in q:
                self.links.append((str(q['link']), q.get('at')))
                continue
            quest = Quest(self, q, i)
            if quest.id in seen:
                raise GenError('%s: duplicate quest id %r' % (path, quest.id))
            seen.add(quest.id)
            self.quests.append(quest)


def load_chapters(paths=None, tolerate=None):
    """Load chapter files. `tolerate` is a set of absolute paths that must load; errors in
    any other file are reported as warnings and the file is skipped (used by partial lint)."""
    paths = paths or sorted(glob.glob(os.path.join(CHAPTER_DIR, '**', '*.yaml'), recursive=True))
    chapters = []
    for p in paths:
        try:
            with open(p, encoding='utf-8') as f:
                try:
                    data = yaml.safe_load(f)
                except yaml.YAMLError as e:
                    raise GenError('%s: YAML error: %s' % (p, e))
            if not data:
                continue
            chapters.append(Chapter(p, data))
        except GenError as e:
            if tolerate is None or os.path.abspath(p) in tolerate:
                raise
            print('WARNING (skipped other file):', e)
    ids = defaultdict(list)
    for c in chapters:
        ids[c.id].append(c.path)
    dup = {k: v for k, v in ids.items() if len(v) > 1}
    if dup:
        raise GenError('duplicate chapter ids: %s' % dup)
    chapters.sort(key=lambda c: (c.order, c.id))
    return chapters


# --------------------------------------------------------------------------- layout

COL_W = 44
ROW_H = 40
MAX_COL = 14


def layout(chapter, index):
    """Layered left-to-right layout; honours explicit `at: [col, row]`."""
    local = {q.key: q for q in chapter.quests}
    layer = {}

    def depth(q, stack=()):
        if q.key in layer:
            return layer[q.key]
        if q.key in stack:
            raise GenError('dependency cycle through %s' % q.key)
        ds = [local[d] for d in q.deps if d in local]
        layer[q.key] = 0 if not ds else 1 + max(depth(d, stack + (q.key,)) for d in ds)
        return layer[q.key]

    for q in chapter.quests:
        depth(q)
    cols = defaultdict(list)
    for q in chapter.quests:
        if 'at' not in q.data:
            cols[layer[q.key]].append(q)
    # split very tall columns into sub-columns
    flat = []
    for L in sorted(cols):
        qs = cols[L]
        for k in range(0, len(qs), MAX_COL):
            flat.append(qs[k:k + MAX_COL])
    rows = {}
    x = 0
    for colqs in flat:
        # barycentre ordering by parents' rows
        def bary(q):
            ps = [rows[d] for d in q.deps if d in rows]
            return (sum(ps) / len(ps)) if ps else q.order / 1000.0
        colqs.sort(key=lambda q: (bary(q), q.order))
        n = len(colqs)
        for r, q in enumerate(colqs):
            row = r - (n - 1) / 2.0
            rows[q.key] = row
            q.pos = (x * COL_W, int(row * ROW_H))
        x += 1
    for q in chapter.quests:
        if 'at' in q.data:
            cx, cy = q.data['at']
            q.pos = (int(cx * COL_W), int(cy * ROW_H))
    # center icons of different sizes on the grid point
    for q in chapter.quests:
        px, py = q.pos
        q.pos = (px - q.size // 2, py - q.size // 2)


# --------------------------------------------------------------------------- emit

def quest_props(q, reg, settings):
    d = q.data
    t = TYPES[q.type]
    ctx = q.key
    name = str(d.get('title') or d.get('name') or '')
    if not name:
        raise GenError('%s: missing title' % ctx)
    color = d.get('color')
    if color is None:
        color = {'boss': '§c§l', 'milestone': '§6§l', 'finale': '§d§l', 'portal': '§5§l',
                 'main': '§e', 'optional': '§7', 'info': '§b'}.get(q.type, '')
    desc = str(d.get('desc', '')).rstrip()
    if not desc:
        raise GenError('%s: missing desc' % ctx)
    icon_spec = d.get('icon')
    if icon_spec is None:
        icon_spec = first_task_item(d) or settings.get('default_icon', 'minecraft:book')
    icon = parse_item(icon_spec, ctx + '.icon', reg)
    icon['Count:3'] = 1
    vis = VISIBILITIES.get(str(d.get('visibility', t.get('visibility', 'normal'))).lower())
    if vis is None:
        raise GenError('%s: bad visibility %r' % (ctx, d.get('visibility')))
    p = OrderedDict()
    p['autoClaim:1'] = 1 if d.get('auto_claim') else 0
    p['completion_animation:8'] = d.get('animation', t.get('anim', 'default'))
    p['completion_particle:8'] = d.get('particle', t.get('particle', 'default'))
    p['confetti_icon:10'] = OrderedDict([('Count:3', 0), ('Damage:2', 0), ('OreDict:8', ''), ('id:8', 'minecraft:stick')])
    p['countAsQuest:1'] = 0 if q.type == 'info' and d.get('count') is False else 1
    p['desc:8'] = desc
    p['globalShare:1'] = 1
    p['icon:10'] = icon
    p['isGlobal:1'] = 0
    p['isMain:1'] = 1 if t['main'] else 0
    p['isSilent:1'] = 0
    p['lockedProgress:1'] = 1
    p['name:8'] = color + name
    p['notification_duration:5'] = -1.0
    p['notification_effect:3'] = int(d.get('effect', t.get('effect', -1)))
    p['notification_fade_in:5'] = -1.0
    p['notification_fade_out:5'] = -1.0
    p['notification_icon_offset_y:3'] = -2147483648
    p['notification_icon_scale:5'] = -1.0
    p['notification_pos_x:3'] = -2147483648
    p['notification_pos_y:3'] = -2147483648
    p['notification_show_icon:8'] = 'default'
    p['notification_style:8'] = 'default'
    p['notification_subtitle:8'] = d.get('subtitle', '')
    p['notification_subtitle_scale:5'] = -1.0
    p['notification_title:8'] = d.get('banner', '')
    p['notification_title_scale:5'] = -1.0
    p['particle_count:3'] = -1
    p['partySingleReward:1'] = 0
    p['questLogic:8'] = 'OR' if d.get('deps_any') else 'AND'
    p['repeatTime:3'] = int(d.get('repeat', -1))
    p['repeat_relative:1'] = 1
    p['simultaneous:1'] = 0
    p['snd_complete:8'] = d.get('sound', 'random.levelup')
    p['snd_update:8'] = 'random.levelup'
    p['taskLogic:8'] = str(d.get('task_logic', 'AND')).upper()
    p['visibility:8'] = vis
    return p


def first_task_item(d):
    for t in as_list(d.get('tasks')):
        if isinstance(t, dict):
            k = next(iter(t))
            if k in ('get', 'consume', 'optional', 'one_of', 'craft'):
                v = as_list(t[k])
                if v:
                    return v[0]
    return None


def build_quest_json(q, all_quests, reg, settings):
    ctx = q.key
    tasks = as_list(q.data.get('tasks'))
    if not tasks:
        raise GenError('%s: quest has no tasks (use `- check` for a checkbox)' % ctx)
    o = OrderedDict()
    pre = OrderedDict()
    n = 0
    for d in q.deps + q.hidden_deps:
        if d not in all_quests:
            raise GenError('%s: unknown dependency %r' % (ctx, d))
        hi, lo = uuid_parts(all_quests[d].uuid)
        e = OrderedDict([('questIDHigh:4', hi), ('questIDLow:4', lo)])
        if d in q.hidden_deps:
            e['type:1'] = 1  # IMPLICIT: required but no line drawn
        pre['%d:10' % n] = e
        n += 1
    if pre:
        o['preRequisites:9'] = pre
    o['properties:10'] = {'betterquesting:10': quest_props(q, reg, settings)}
    hi, lo = uuid_parts(q.uuid)
    o['questIDHigh:4'] = hi
    o['questIDLow:4'] = lo
    rw = OrderedDict()
    for i, r in enumerate(as_list(q.data.get('rewards'))):
        rw['%d:10' % i] = build_reward(r, i, ctx, reg)
    o['rewards:9'] = rw
    tk = OrderedDict()
    for i, t in enumerate(tasks):
        tk['%d:10' % i] = build_task(t, i, ctx, reg)
    o['tasks:9'] = tk
    return o


def build(strict=False, out_dir=OUT_DIR, quiet=False):
    reg = Registry()
    if not reg.loaded:
        msg = 'registry dump not found in registry/: item ids will NOT be validated'
        if strict:
            raise GenError(msg)
        print('WARNING:', msg)
    settings = {}
    if os.path.exists(SETTINGS_FILE):
        settings = yaml.safe_load(open(SETTINGS_FILE, encoding='utf-8')) or {}
    chapters = load_chapters()
    all_quests = {}
    for c in chapters:
        for q in c.quests:
            all_quests[q.key] = q
    errors = []
    built = {}
    for c in chapters:
        try:
            layout(c, all_quests)
        except GenError as e:
            errors.append('%s: %s' % (c.path, e))
            continue
        for q in c.quests:
            try:
                built[q.key] = build_quest_json(q, all_quests, reg, settings)
            except GenError as e:
                errors.append(str(e))
    for c in chapters:
        for key, _ in c.links:
            if key not in all_quests:
                errors.append('%s: link to unknown quest %r' % (c.id, key))
    if errors:
        raise GenError('%d error(s):\n  ' % len(errors) + '\n  '.join(errors))

    dq = os.path.join(out_dir, 'DefaultQuests')
    if os.path.exists(dq):
        shutil.rmtree(dq)
    os.makedirs(dq)
    qs = OrderedDict()
    qs['editMode:1'] = 0
    qs['hardcore:1'] = 0
    qs['home_anchor_x:5'] = 0.5
    qs['home_anchor_y:5'] = 0.0
    qs['home_image:8'] = settings.get('home_image', '')
    qs['home_offset_x:3'] = -128
    qs['home_offset_y:3'] = 0
    qs['livesDef:3'] = 3
    qs['livesMax:3'] = 10
    qs['pack_name:8'] = settings.get('pack_name', 'Akasha Quests')
    qs['pack_version:3'] = int(settings.get('pack_version', 1))
    qs['party_enable:1'] = 1
    dump_json(os.path.join(dq, 'QuestSettings.json'), {'betterquesting:10': qs, 'format:8': '3.1.0'})

    membership = defaultdict(list)
    order_lines = []
    for c in chapters:
        stub = file_stub(c.title, c.uuid)
        order_lines.append('%s: %s' % (encode_uuid(c.uuid), strip_fmt(c.title)))
        icon = parse_item(c.meta.get('icon', 'minecraft:book'), c.id + '.icon', reg)
        icon['Count:3'] = 1
        ql = OrderedDict()
        ql['properties:10'] = {'betterquesting:10': OrderedDict([
            ('bg_image:8', c.meta.get('bg_image', '')),
            ('bg_size:3', int(c.meta.get('bg_size', 256))),
            ('desc:8', str(c.meta.get('desc', '')).rstrip()),
            ('icon:10', icon),
            ('name:8', c.meta.get('color', '') + c.title),
            ('visibility:8', VISIBILITIES[str(c.meta.get('visibility', 'normal')).lower()]),
        ])}
        hi, lo = uuid_parts(c.uuid)
        ql['questLineIDHigh:4'] = hi
        ql['questLineIDLow:4'] = lo
        base = os.path.join(dq, 'QuestLines', stub)
        dump_json(os.path.join(base, 'QuestLine.json'), ql)
        entries = [(q, q.pos) for q in c.quests]
        for key, at in c.links:
            lq = all_quests[key]
            pos = (int(at[0] * COL_W) - lq.size // 2, int(at[1] * ROW_H) - lq.size // 2) if at else (0, 0)
            entries.append((lq, pos))
        for q, (x, y) in entries:
            membership[q.key].append(c)
            hi, lo = uuid_parts(q.uuid)
            e = OrderedDict([('questIDHigh:4', hi), ('questIDLow:4', lo), ('sizeX:3', q.size),
                             ('sizeY:3', q.size), ('x:3', x), ('y:3', y)])
            name = strip_fmt(str(q.data.get('title', '')))
            dump_json(os.path.join(base, file_stub(name, q.uuid) + '.json'), e)
    with open(os.path.join(dq, 'QuestLinesOrder.txt'), 'w', encoding='utf-8') as f:
        f.write('\n'.join(order_lines) + '\n')
    for key, data in built.items():
        q = all_quests[key]
        cs = membership[key]
        if len(cs) == 1:
            sub = file_stub(cs[0].title, cs[0].uuid)
        elif not cs:
            sub = 'NoQuestLine'
        else:
            sub = 'MultipleQuestLine'
        dump_json(os.path.join(dq, 'Quests', sub, file_stub(str(q.data.get('title', '')), q.uuid) + '.json'), data)

    build_loot(reg, out_dir)
    total = len(built)
    if not quiet:
        print('built %d quests in %d chapters -> %s' % (total, len(chapters), os.path.relpath(dq, ROOT)))
    return chapters, total


def build_loot(reg, out_dir):
    if not os.path.exists(LOOT_FILE):
        return
    data = yaml.safe_load(open(LOOT_FILE, encoding='utf-8')) or {}
    groups = []
    for gi, (tier, g) in enumerate(data.get('groups', {}).items()):
        if tier not in LOOT_TIERS:
            raise GenError('loot.yaml: unknown tier %r' % tier)
        rewards = OrderedDict()
        for ei, entry in enumerate(g['entries']):
            items = OrderedDict()
            for ii, s in enumerate(as_list(entry['items'])):
                items['%d:10' % ii] = parse_item(s, 'loot.%s[%d][%d]' % (tier, ei, ii), reg)
            rewards['%d:10' % ei] = OrderedDict([('ID:3', ei), ('items:9', items), ('weight:3', int(entry.get('weight', 10)))])
        # Weights 100..104 keep the five tiers strictly ordered (see LootRegistry.getWeightedGroup).
        groups.append(OrderedDict([('ID:3', gi), ('name:8', g['name']), ('rewards:9', rewards),
                                   ('weight:3', 100 + list(LOOT_TIERS).index(tier))]))
    dump_json(os.path.join(out_dir, 'DefaultLoot.json'),
              {'groups:9': OrderedDict(('%d:10' % i, g) for i, g in enumerate(groups))})


def stats():
    chapters = load_chapters()
    total = 0
    by_prefix = defaultdict(int)
    for c in chapters:
        n = len(c.quests)
        total += n
        by_prefix[c.prefix or 'Akasha'] += n
        print('%6.1f  %-40s %4d' % (c.order, c.title[:40], n))
    print('TOTAL %d quests in %d chapters' % (total, len(chapters)))
    for k, v in sorted(by_prefix.items()):
        print('  %-8s %d' % (k, v))


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument('cmd', choices=['build', 'lint', 'stats'])
    ap.add_argument('files', nargs='*')
    ap.add_argument('--strict', action='store_true')
    a = ap.parse_args()
    try:
        if a.cmd == 'build':
            build(strict=a.strict)
        elif a.cmd == 'stats':
            stats()
        else:
            import tempfile
            with tempfile.TemporaryDirectory() as td:
                if a.files:
                    lint_files(a.files)
                else:
                    build(strict=True, out_dir=td)
            print('OK')
    except GenError as e:
        print('ERROR:', e)
        sys.exit(1)


def lint_files(files):
    """Lint a subset of chapters. Cross-chapter deps resolve against all chapters."""
    reg = Registry()
    settings = yaml.safe_load(open(SETTINGS_FILE, encoding='utf-8')) if os.path.exists(SETTINGS_FILE) else {}
    targets = {os.path.abspath(f) for f in files}
    everything = load_chapters(tolerate=targets)
    all_quests = {q.key: q for c in everything for q in c.quests}
    errors = []
    n = 0
    for c in everything:
        if os.path.abspath(c.path) not in targets:
            continue
        try:
            layout(c, all_quests)
        except GenError as e:
            errors.append('%s: %s' % (c.path, e))
            continue
        for q in c.quests:
            n += 1
            try:
                build_quest_json(q, all_quests, reg, settings or {})
            except GenError as e:
                errors.append(str(e))
    if errors:
        raise GenError('%d error(s):\n  ' % len(errors) + '\n  '.join(errors))
    print('linted %d quests' % n)


if __name__ == '__main__':
    main()
