#!/usr/bin/env python3
"""Export the built DefaultQuests into BetterQuesting `saved_quests` form, for
`/bq_admin default load <name>`.

  export_saved.py [name] [--folder]   (default name: AkashaQuests)

Writes, under dist/config/betterquesting/saved_quests/:
  <name>.json    legacy single-file database (questSettings/questDatabase/questLines)
  <name>/        with --folder: multi-file copy of DefaultQuests (not tracked in git)
BetterQuesting loads the folder when it exists, otherwise the .json.
"""

import glob
import json
import os
import shutil
import sys

ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..', '..'))
BQ = os.path.join(ROOT, 'dist', 'config', 'betterquesting')
SRC = os.path.join(BQ, 'DefaultQuests')


def load(p):
    with open(p, encoding='utf-8') as f:
        return json.load(f)


def main(name, folder_copy):
    if not os.path.exists(os.path.join(SRC, 'QuestSettings.json')):
        sys.exit('run bqgen.py build first')
    out_dir = os.path.join(BQ, 'saved_quests')
    os.makedirs(out_dir, exist_ok=True)

    folder = os.path.join(out_dir, name)
    if os.path.exists(folder):
        shutil.rmtree(folder)
    if folder_copy:
        shutil.copytree(SRC, folder)

    settings = load(os.path.join(SRC, 'QuestSettings.json'))
    quests = {}
    for i, p in enumerate(sorted(glob.glob(os.path.join(SRC, 'Quests', '*', '*.json')))):
        quests['%d:10' % i] = load(p)

    order = [l.split(':', 1)[0].strip() for l in open(os.path.join(SRC, 'QuestLinesOrder.txt'), encoding='utf-8') if l.strip()]
    by_stub = {}
    for d in glob.glob(os.path.join(SRC, 'QuestLines', '*')):
        by_stub[os.path.basename(d).split('-', 1)[1]] = d
    lines = {}
    for idx, enc in enumerate(order):
        d = by_stub[enc]
        ql = load(os.path.join(d, 'QuestLine.json'))
        entries = {}
        for p in sorted(glob.glob(os.path.join(d, '*.json'))):
            if os.path.basename(p) != 'QuestLine.json':
                entries['%d:10' % len(entries)] = load(p)
        ql['quests:9'] = entries
        ql['order:3'] = idx
        lines['%d:10' % idx] = ql

    legacy = {
        'format:8': settings.get('format:8', '3.1.0'),
        'questSettings:10': settings['betterquesting:10'],
        'questDatabase:9': quests,
        'questLines:9': lines,
    }
    with open(os.path.join(out_dir, name + '.json'), 'w', encoding='utf-8') as f:
        json.dump(legacy, f, ensure_ascii=False, separators=(',', ':'))
    print('%d quests, %d quest lines -> saved_quests/%s.json%s'
          % (len(quests), len(lines), name, ' and saved_quests/%s/' % name if folder_copy else ''))


if __name__ == '__main__':
    a = [x for x in sys.argv[1:] if x != '--folder']
    main(a[0] if a else 'AkashaQuests', '--folder' in sys.argv)
