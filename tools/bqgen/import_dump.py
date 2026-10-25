#!/usr/bin/env python3
"""Merge the AkashaDump output (tools/dumpmod) into the compact registry/ index.

Usage: python3 tools/bqgen/import_dump.py <dump_dir>
"""

import gzip
import json
import os
import sys

ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..', '..'))
OUT = os.path.join(ROOT, 'registry')


def read(path):
    with open(path, encoding='utf-8') as f:
        for line in f:
            line = line.strip()
            if line:
                yield json.loads(line)


def write(name, rows):
    with gzip.open(os.path.join(OUT, name), 'wt', encoding='utf-8', compresslevel=9) as f:
        for r in rows:
            f.write(json.dumps(r, ensure_ascii=False, sort_keys=True) + '\n')


def main(dump):
    os.makedirs(OUT, exist_ok=True)
    pt = {}
    for r in read(os.path.join(dump, 'items_pt_BR.jsonl')):
        pt[(r['id'], r['meta'], r.get('nbt'))] = r['name']
    # Cosmetic NBT variants (one per block in the game) that are useless as quest targets.
    collapse = {'ForgeMicroblock:microblock', 'BuildCraft|Transport:pipeFacade',
                'ExtraUtilities:microblocks', 'appliedenergistics2:item.ItemFacade'}
    kept = set()
    items = []
    for r in read(os.path.join(dump, 'items_en_US.jsonl')):
        if r['id'] is None:
            continue
        if r['id'] in collapse and r.get('nbt'):
            if r['id'] in kept:
                continue
            kept.add(r['id'])
        rec = {
            'id': r['id'], 'meta': r['meta'], 'name': r['name'],
            'name_pt': pt.get((r['id'], r['meta'], r.get('nbt')), r['name']),
            'unloc': r.get('unloc'), 'block': r.get('block'), 'creative': r.get('creative'),
            'ore': r.get('ore') or [], 'tab': r.get('tab'), 'maxDmg': r.get('maxDmg', 0),
            'stack': r.get('stack'), 'mod': r['id'].split(':', 1)[0],
        }
        if r.get('nbt'):
            rec['nbt'] = r['nbt']
        items.append(rec)
    write('items.jsonl.gz', items)
    for name in ('oredict', 'entities', 'fluids', 'biomes', 'dimensions', 'mods',
                 'recipes_crafting', 'recipes_furnace', 'thaumcraft_research'):
        p = os.path.join(dump, name + '.jsonl')
        if os.path.exists(p):
            write(name + '.jsonl.gz', read(p))
    names = {}
    for lang in ('en_US', 'pt_BR'):
        p = os.path.join(dump, 'names_%s.jsonl' % lang)
        if os.path.exists(p):
            for r in read(p):
                k = (r['kind'], r['id'])
                names.setdefault(k, {'kind': r['kind'], 'id': r['id']})['name_' + lang[:2]] = r.get('name')
    write('names.jsonl.gz', names.values())
    print('items: %d, mods: %d' % (len(items), len({i['mod'] for i in items})))


if __name__ == '__main__':
    main(sys.argv[1])
