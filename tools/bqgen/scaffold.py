#!/usr/bin/env python3
"""Draft a chapter from the registry + recipe graph (saves the author most of the research).

  scaffold.py <chapter_id> <namespace>[,<namespace>...] [--title T] [--order N] [--max 200]

Emits YAML to stdout: one quest per craftable item of the namespaces, ordered by recipe
depth, with `deps` inferred from in-chapter ingredients, a `get` task, tiered rewards and a
`desc` stub listing the real ingredients (from NEI/crafting). The author then prunes, merges,
retypes (main/milestone/boss...) and rewrites every `desc` in Akasha's voice.
"""

import argparse
import gzip
import json
import os
import re
from collections import defaultdict

ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..', '..'))
REG = os.path.join(ROOT, 'registry')


def rows(name):
    p = os.path.join(REG, name + '.jsonl.gz')
    if os.path.exists(p):
        with gzip.open(p, 'rt', encoding='utf-8') as f:
            for line in f:
                yield json.loads(line)


def key(x):
    m = x.get('meta', 0)
    return (x['id'], 0 if m == 32767 else m)


def slug(s):
    s = s.lower()
    for a, b in (('áàâã', 'a'), ('éê', 'e'), ('í', 'i'), ('óôõ', 'o'), ('ú', 'u'), ('ç', 'c')):
        for ch in a:
            s = s.replace(ch, b)
    s = re.sub('[^a-z0-9]+', '_', s).strip('_')
    return s[:40] or 'item'


def spec(k):
    iid, meta = k
    if ' ' in iid:
        return '{id: "%s", meta: %d}' % (iid, meta)
    return iid if meta == 0 else '%s:%d' % (iid, meta)


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument('chapter')
    ap.add_argument('namespaces')
    ap.add_argument('--title', default='')
    ap.add_argument('--order', type=int, default=9999)
    ap.add_argument('--max', type=int, default=220)
    a = ap.parse_args()
    ns = set(a.namespaces.split(','))
    items = {}
    for r in rows('items'):
        if r['mod'] in ns and not r.get('nbt'):
            items.setdefault((r['id'], r['meta']), r)
    recipes = defaultdict(list)  # out key -> list of (handler, [input keys])
    for r in rows('recipes_crafting'):
        out = r.get('out') or {}
        if 'id' in out:
            recipes[key(out)].append(('Bancada', [key(s[0]) for s in r.get('in', []) if s and 'id' in s[0]]))
    for r in rows('nei_recipes'):
        outs = r.get('out') or []
        if outs:
            ins = [key(s[0]) for s in r.get('in', []) if s and 'id' in s[0]]
            recipes[key(outs[0])].append((r.get('handler', '?'), ins))
    cand = [k for k in items if k in recipes]
    depth = {}

    def d(k, stack=()):
        if k in depth:
            return depth[k]
        if k in stack:
            return 0
        best = 0
        handler, ins = recipes[k][0]
        for i in ins:
            if i in items and i in recipes and i != k:
                best = max(best, 1 + d(i, stack + (k,)))
        depth[k] = best
        return best
    for k in cand:
        d(k)
    cand.sort(key=lambda k: (depth[k], items[k]['id'], k[1]))
    cand = cand[:a.max]
    ids = {}
    used = set()
    for k in cand:
        s = slug(items[k].get('name_pt') or items[k]['name'])
        base, n = s, 2
        while s in used:
            s = '%s_%d' % (base, n)
            n += 1
        used.add(s)
        ids[k] = s
    print('chapter:')
    print('  id: %s' % a.chapter)
    print('  title: "%s"' % (a.title or a.chapter))
    print('  order: %d' % a.order)
    print('  icon: %s' % spec(cand[-1] if cand else ('minecraft:book', 0)))
    print('  desc: |\n    TODO')
    print('quests:')
    for k in cand:
        r = items[k]
        handler, ins = recipes[k][0]
        deps = sorted({ids[i] for i in ins if i in ids and i != k}, key=lambda x: -depth[[c for c in ids if ids[c] == x][0]])[:3]
        names = []
        for i in ins:
            nm = items.get(i, {}).get('name_pt') if i in items else None
            names.append(nm or spec(i))
        tier = min(depth[k], 5)
        print('  - id: %s' % ids[k])
        print('    title: "%s"' % (r.get('name_pt') or r['name']).replace('"', "'"))
        if deps:
            print('    deps: [%s]' % ', '.join(deps))
        print('    desc: |')
        print('      TODO [%s] %s' % (handler, ', '.join(sorted(set(names)))[:300]))
        print('    tasks:')
        print('      - get: %s' % spec(k))
        print('    rewards:')
        print('      - xp: %d' % (1 + tier))
        if tier >= 3:
            print('      - loot: %s' % ('incomum' if tier < 5 else 'raro'))


if __name__ == '__main__':
    main()
