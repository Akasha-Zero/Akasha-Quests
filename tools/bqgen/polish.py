#!/usr/bin/env python3
"""Cheap text polishing: export short descriptions, re-import rewritten ones.

  polish.py export <out.json> <chapter.yaml>... [--min 45]
      Writes [{file, id, title, type, tasks, desc}] for quests whose desc has < min words.
  polish.py import <in.json>
      Reads [{file, id, desc}] and replaces each quest's `desc` block in place
      (only that field; the rest of the YAML is untouched). Then run the linter.
"""

import json
import re
import sys

import yaml


def words(s):
    return len(re.sub('§.', '', str(s or '')).split())


def export(out, files, minw):
    rows = []
    for f in files:
        d = yaml.safe_load(open(f, encoding='utf-8'))
        for q in d.get('quests') or []:
            if 'id' not in q or words(q.get('desc')) >= minw:
                continue
            rows.append({'file': f, 'id': q['id'], 'title': q.get('title'), 'type': q.get('type', 'normal'),
                         'tasks': json.dumps(q.get('tasks'), ensure_ascii=False)[:240],
                         'desc': str(q.get('desc', '')).strip()})
    json.dump(rows, open(out, 'w', encoding='utf-8'), ensure_ascii=False, indent=1)
    print('%d quests exported' % len(rows))


ID_RE = re.compile(r'^(\s*)-\s+id:\s*["\']?([^\s"']+)["\']?\s*$')


def replace_desc(lines, qid, text):
    start = None
    for i, l in enumerate(lines):
        m = ID_RE.match(l)
        if m and m.group(2) == qid:
            start, base = i, len(m.group(1))
            break
    if start is None:
        return False
    key_indent = base + 2
    end = len(lines)
    for j in range(start + 1, len(lines)):
        l = lines[j]
        if l.strip() and (len(l) - len(l.lstrip())) <= base and l.lstrip().startswith('-'):
            end = j
            break
    for j in range(start + 1, end):
        l = lines[j]
        if re.match(r'^\s{%d}desc:' % key_indent, l):
            k = j + 1
            if re.match(r'^\s*desc:\s*[|>]', l):
                while k < end and (not lines[k].strip() or len(lines[k]) - len(lines[k].lstrip()) > key_indent):
                    k += 1
                while k > j + 1 and not lines[k - 1].strip():
                    k -= 1
            pad = ' ' * (key_indent + 2)
            body = [pad + t if t else '' for t in text.strip().split('\n')]
            lines[j:k] = [' ' * key_indent + 'desc: |'] + body
            return True
    return False


def do_import(path):
    rows = json.load(open(path, encoding='utf-8'))
    by_file = {}
    for r in rows:
        by_file.setdefault(r['file'], []).append(r)
    ok = miss = 0
    for f, rs in by_file.items():
        lines = open(f, encoding='utf-8').read().split('\n')
        for r in rs:
            if r.get('desc') and replace_desc(lines, r['id'], r['desc']):
                ok += 1
            else:
                miss += 1
                print('MISS', f, r['id'])
        open(f, 'w', encoding='utf-8').write('\n'.join(lines))
        yaml.safe_load(open(f, encoding='utf-8'))  # must still parse
    print('replaced %d, missed %d' % (ok, miss))


if __name__ == '__main__':
    a = sys.argv[1:]
    minw = 45
    if '--min' in a:
        i = a.index('--min'); minw = int(a[i + 1]); del a[i:i + 2]
    if a[0] == 'export':
        export(a[1], a[2:], minw)
    else:
        do_import(a[1])
