#!/usr/bin/env python3
"""Query the registry dump. Used while writing chapters to find exact ids.

  reg.py items <regex> [--mod MOD] [--limit N]   search item names (pt/en), ids, unlocalized names
  reg.py mod MOD [--limit N]                     list every item of a mod (by registry namespace)
  reg.py ore <regex>                             ore dictionary names and members
  reg.py entity <regex>                          entity ids (for kill tasks) with names
  reg.py fluid <regex>                           fluid ids
  reg.py dims | biomes [regex]                   dimensions / biomes
  reg.py recipe <item_id[:meta]>                 crafting-table recipes that output the item
  reg.py uses <item_id[:meta]>                   crafting-table recipes that consume the item
  reg.py research <regex>                        Thaumcraft research keys
  reg.py mods                                    loaded mods (modid, name, version)
"""

import argparse
import gzip
import json
import os
import re
import sys

ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..', '..'))
REG = os.path.join(ROOT, 'registry')


def rows(name):
    with gzip.open(os.path.join(REG, name + '.jsonl.gz'), 'rt', encoding='utf-8') as f:
        for line in f:
            yield json.loads(line)


def fmt_item(r):
    spec = r['id'] if r['meta'] == 0 else '%s:%d' % (r['id'], r['meta'])
    extra = []
    if r.get('ore'):
        extra.append('ore=' + ','.join(r['ore'][:4]))
    if r.get('nbt'):
        extra.append('nbt=' + r['nbt'][:80])
    if r.get('maxDmg'):
        extra.append('dmg=%d' % r['maxDmg'])
    return '%-55s %-38s | %s %s' % (spec, (r.get('name_pt') or '')[:38], r.get('name', '')[:40], ' '.join(extra))


def spec_match(r, spec):
    parts = spec.split(':')
    iid = ':'.join(parts[:2])
    meta = int(parts[2]) if len(parts) > 2 and parts[2] != '*' else None
    if r.get('id') != iid:
        return False
    m = r.get('meta')
    return meta is None or m == meta or m == 32767


def main():
    ap = argparse.ArgumentParser(usage=__doc__)
    ap.add_argument('cmd')
    ap.add_argument('arg', nargs='?', default='')
    ap.add_argument('--mod')
    ap.add_argument('--limit', type=int, default=60)
    a = ap.parse_args()
    rx = re.compile(a.arg, re.I) if a.arg else None
    n = 0

    def out(s):
        nonlocal n
        n += 1
        if n <= a.limit:
            print(s)

    if a.cmd == 'items':
        for r in rows('items'):
            if a.mod and r['mod'].lower() != a.mod.lower():
                continue
            hay = ' '.join(str(r.get(k) or '') for k in ('id', 'name', 'name_pt', 'unloc'))
            if rx.search(hay):
                out(fmt_item(r))
    elif a.cmd == 'mod':
        for r in rows('items'):
            if r['mod'].lower() == a.arg.lower():
                out(fmt_item(r))
    elif a.cmd == 'ore':
        for r in rows('oredict'):
            if rx.search(r['ore']):
                out('%-32s %s' % (r['ore'], ', '.join('%s:%s' % (i['id'], i['meta']) for i in r['items'][:6])))
    elif a.cmd in ('entity', 'fluid'):
        names = {(x['kind'], x['id']): x for x in rows('names')}
        for r in rows('entities' if a.cmd == 'entity' else 'fluids'):
            if 'id' not in r:
                continue
            nm = names.get((a.cmd if a.cmd == 'fluid' else 'entity', r['id']), {})
            hay = r['id'] + ' ' + str(nm.get('name_pt')) + ' ' + str(nm.get('name_en'))
            if not rx or rx.search(hay):
                out('%-40s %-30s | %s' % (r['id'], nm.get('name_pt'), nm.get('name_en')))
    elif a.cmd == 'dims':
        for r in rows('dimensions'):
            out('%5s  %-30s %s' % (r['num'], r.get('name'), r.get('cls')))
    elif a.cmd == 'biomes':
        for r in rows('biomes'):
            if not rx or rx.search(r['name']):
                out('%5s  %s' % (r['num'], r['name']))
    elif a.cmd in ('recipe', 'uses'):
        for r in rows('recipes_crafting'):
            if a.cmd == 'recipe':
                hit = spec_match(r['out'], a.arg)
            else:
                hit = any(spec_match(x, a.arg) for slot in r.get('in', []) for x in slot)
            if hit:
                ins = []
                for slot in r.get('in', []):
                    ins.append('/'.join(x.get('ore') or '%s:%s' % (x['id'], x['meta']) for x in slot[:2]) or '-')
                out('%s x%s  <=  [%s]%s' % (r['out']['id'] + ':' + str(r['out'].get('meta')), r['out'].get('count'),
                                         ', '.join(ins), ' (opaque %s)' % r['cls'] if r.get('opaque') else ''))
        for r in rows('recipes_furnace'):
            if (a.cmd == 'recipe' and spec_match(r['out'], a.arg)) or (a.cmd == 'uses' and spec_match(r['in'], a.arg)):
                out('FURNACE %s:%s -> %s:%s' % (r['in']['id'], r['in']['meta'], r['out']['id'], r['out']['meta']))
    elif a.cmd == 'research':
        for r in rows('thaumcraft_research'):
            if rx.search(r['key'] + ' ' + str(r.get('name')) + ' ' + r['cat']):
                out('%-14s %-28s %-30s parents=%s' % (r['cat'], r['key'], r.get('name'), r.get('parents')))
    elif a.cmd == 'mods':
        for r in rows('mods'):
            out('%-28s %-40s %s' % (r['modid'], r['name'], r['version']))
    else:
        print(__doc__)
        sys.exit(2)
    if n > a.limit:
        print('... %d more (use --limit)' % (n - a.limit))


if __name__ == '__main__':
    main()
