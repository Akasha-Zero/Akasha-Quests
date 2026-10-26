#!/usr/bin/env python3
"""Quality audit of the quest sources: a score per quest, per chapter and overall.

  audit.py [--worst N] [--json out.json]

Each quest is checked on objective criteria (text, tasks, rewards, structure, coherence).
The score is the weighted share of criteria passed; see CRITERIA below.
"""

import collections
import glob
import gzip
import json
import os
import re
import sys
import unicodedata

import yaml

ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..', '..'))
REG = os.path.join(ROOT, 'registry')

# key: (weight, description)
CRITERIA = {
    'desc_len':    (2, 'descrição com 45+ palavras (marcos/chefes: 80+)'),
    'desc_unique': (2, 'texto próprio: <25% das frases repetidas em 5+ quests'),
    'desc_names':  (3, 'a descrição cita o item/mob/lugar pedido na tarefa'),
    'verifiable':  (3, 'tarefa verificável (checkbox só em info/história)'),
    'reward_rich': (1, 'recompensa além de só XP'),
    'no_circular': (2, 'a recompensa não entrega o próprio item pedido'),
    'no_skip':     (2, 'a recompensa não entrega item pedido mais adiante no capítulo'),
    'obtainable':  (2, 'item da tarefa tem receita, é bloco ou é drop/saque conhecido'),
    'connected':   (1, 'ligado à árvore do capítulo (deps)'),
    'unique_title':(1, 'título único no capítulo'),
}
BIG = {'milestone', 'boss', 'finale'}
CHECK_OK = {'info'}


def rows(name):
    p = os.path.join(REG, name + '.jsonl.gz')
    with gzip.open(p, 'rt', encoding='utf-8') as f:
        for l in f:
            yield json.loads(l)


def norm(s):
    s = re.sub('§.', '', str(s or '')).lower()
    s = unicodedata.normalize('NFKD', s)
    return ''.join(c for c in s if not unicodedata.combining(c))


STOP = set('de da do das dos e o a os as em no na com para por um uma of the and'.split())


def keywords(name):
    return [w for w in re.findall(r'[a-z0-9]+', norm(name)) if len(w) >= 4 and w not in STOP]


def item_key(spec):
    if isinstance(spec, dict):
        iid, meta = str(spec.get('id')), spec.get('meta', 0)
    else:
        s = str(spec).split(' x')[0].split(' {')[0].strip()
        if s.startswith('ore:'):
            return s, None
        p = s.split(':')
        iid = ':'.join(p[:2])
        meta = p[2] if len(p) > 2 else 0
    try:
        meta = int(meta)
    except (TypeError, ValueError):
        meta = None
    return iid, meta


def specs(v):
    if v is None:
        return []
    if isinstance(v, list):
        out = []
        for x in v:
            out += specs(x)
        return out
    if isinstance(v, dict) and 'items' in v:
        return specs(v['items'])
    return [v]


def task_items(q):
    out = []
    for t in q.get('tasks') or []:
        if isinstance(t, dict):
            for k in ('get', 'consume', 'craft', 'one_of'):
                if k in t:
                    out += [item_key(s) for s in specs(t[k])]
    return out


def reward_items(q):
    out = []
    for r in q.get('rewards') or []:
        if isinstance(r, dict):
            for k in ('item', 'choice', 'kit'):
                if k in r:
                    out += [item_key(s) for s in specs(r[k])]
    return out


def task_kinds(q):
    ks = []
    for t in q.get('tasks') or []:
        ks.append(t if isinstance(t, str) else next(iter(t)))
    return ks


def main():
    args = sys.argv[1:]
    worst = int(args[args.index('--worst') + 1]) if '--worst' in args else 15
    jout = args[args.index('--json') + 1] if '--json' in args else None

    names, blocks, ore_names = {}, set(), collections.defaultdict(list)
    for r in rows('items'):
        k = (r['id'], r['meta'])
        names[k] = (r.get('name_pt') or '') + ' ' + (r.get('name') or '')
        names.setdefault((r['id'], None), names[k])
        if r.get('block'):
            blocks.add(r['id'])
        for o in r.get('ore') or []:
            ore_names['ore:' + o].append(names[k])
    made = set()
    for r in rows('nei_recipes'):
        for o in r.get('out') or []:
            made.add(o['id'])
    for r in rows('recipes_crafting'):
        o = r.get('out') or {}
        made.add(o.get('id'))
    for r in rows('recipes_furnace'):
        o = r.get('out') or {}
        made.add(o.get('id'))
    ents = {}
    for r in rows('entities'):
        ents[r['id']] = ' '.join(str(r.get(k) or '') for k in ('name', 'name_pt', 'display'))
    loot_ids = set()
    loot = yaml.safe_load(open(os.path.join(ROOT, 'quests', 'loot', 'loot.yaml'), encoding='utf-8'))
    for s in re.findall(r'[A-Za-z0-9_.]+:[A-Za-z0-9_.]+', json.dumps(loot)):
        loot_ids.add(s)

    chapters = []
    for f in sorted(glob.glob(os.path.join(ROOT, 'quests', 'chapters', '*', '*.yaml'))):
        d = yaml.safe_load(open(f, encoding='utf-8'))
        chapters.append((f, d))

    sent_count = collections.Counter()
    for _, d in chapters:
        for q in d.get('quests') or []:
            if not isinstance(q, dict):
                continue
            for s in set(x.strip() for x in re.split(r'(?<=[.!?])\s+', norm(q.get('desc'))) if len(x.strip()) > 20):
                sent_count[s] += 1

    results, per_ch = [], {}
    fails = collections.Counter()
    for f, d in chapters:
        ch = d['chapter']
        qs = [q for q in d.get('quests') or [] if isinstance(q, dict) and 'id' in q]
        titles = collections.Counter(norm(q.get('title')) for q in qs)
        later_tasks = {}
        for i, q in enumerate(qs):
            later_tasks[q['id']] = set(k for q2 in qs[i + 1:] for k in task_items(q2) if not str(k[0]).startswith('ore:'))
        ch_scores = []
        for q in qs:
            typ = q.get('type', 'normal')
            desc = norm(q.get('desc'))
            words = len(desc.split())
            sents = [x.strip() for x in re.split(r'(?<=[.!?])\s+', desc) if len(x.strip()) > 20]
            rep = sum(1 for s in sents if sent_count[s] >= 5)
            ti = task_items(q)
            kinds = task_kinds(q)
            ok = {}
            ok['desc_len'] = words >= (80 if typ in BIG else 45)
            ok['desc_unique'] = not sents or rep / len(sents) < 0.25
            # coherence: some keyword of some task target appears in desc (or title)
            text = desc + ' ' + norm(q.get('title'))
            targets = []
            for k in ti:
                if str(k[0]).startswith('ore:'):
                    targets.append(' '.join(ore_names.get(k[0], [])[:3]) + ' ' + re.sub(r'([a-z])([A-Z])', r'\1 \2', k[0][4:]))
                else:
                    targets.append(names.get(k, names.get((k[0], None), k[0].split(':')[-1])))
            for t in q.get('tasks') or []:
                if isinstance(t, dict) and 'kill' in t:
                    e = str(t['kill']).split(' x')[0].strip()
                    targets.append(ents.get(e, '') + ' ' + re.sub(r'([a-z])([A-Z])', r'\1 \2', e.split('.')[-1]))
                if isinstance(t, dict) and 'visit' in t and isinstance(t['visit'], dict):
                    targets.append(str(t['visit'].get('name', '')))
                if isinstance(t, dict) and 'fluid' in t:
                    targets.append(str(t['fluid']).split(' x')[0])
            if targets:
                ok['desc_names'] = any(w in text for tg in targets for w in keywords(tg))
            only_check = kinds and all(k == 'check' for k in kinds)
            ok['verifiable'] = (not only_check) or typ in CHECK_OK or ch['id'].startswith(('prologo', 'ato')) or 'story' in f
            rk = [next(iter(r)) if isinstance(r, dict) else r for r in q.get('rewards') or []]
            ok['reward_rich'] = any(k != 'xp' for k in rk)
            ri = set(k for k in reward_items(q) if not str(k[0]).startswith('ore:'))
            tset = set(k for k in ti if not str(k[0]).startswith('ore:'))
            ok['no_circular'] = not (ri & tset)
            ok['no_skip'] = not (ri & later_tasks[q['id']])
            if tset:
                ok['obtainable'] = all(k[0] in made or k[0] in blocks or k[0] in loot_ids or k[0].startswith('minecraft:') for k in tset)
            deps = (q.get('deps') or []) + (q.get('deps_hidden') or [])
            ok['connected'] = bool(deps) or q is qs[0] or typ in CHECK_OK
            ok['unique_title'] = titles[norm(q.get('title'))] == 1
            w_all = sum(CRITERIA[k][0] for k in ok)
            w_ok = sum(CRITERIA[k][0] for k, v in ok.items() if v)
            sc = 100.0 * w_ok / w_all
            for k, v in ok.items():
                if not v:
                    fails[k] += 1
            ch_scores.append(sc)
            results.append({'chapter': ch['id'], 'file': os.path.relpath(f, ROOT), 'id': q['id'],
                            'title': q.get('title'), 'score': round(sc, 1),
                            'fail': [k for k, v in ok.items() if not v]})
        per_ch[ch['id']] = (ch.get('title'), sum(ch_scores) / max(1, len(ch_scores)), len(qs))

    total = sum(r['score'] for r in results) / len(results)
    print('QUALIDADE GERAL: %.1f%%  (%d quests, %d capítulos)\n' % (total, len(results), len(per_ch)))
    print('Critérios (falhas):')
    for k, (w, desc) in CRITERIA.items():
        n = fails[k]
        print('  %-13s peso %d  %5d falhas (%4.1f%%)  %s' % (k, w, n, 100.0 * n / len(results), desc))
    print('\nPiores capítulos:')
    for cid, (t, s, n) in sorted(per_ch.items(), key=lambda x: x[1][1])[:worst]:
        print('  %5.1f%%  %-28s %4d  %s' % (s, cid, n, t))
    print('\nMelhores capítulos:')
    for cid, (t, s, n) in sorted(per_ch.items(), key=lambda x: -x[1][1])[:5]:
        print('  %5.1f%%  %-28s %4d  %s' % (s, cid, n, t))
    if jout:
        json.dump({'total': total, 'fails': fails, 'chapters': per_ch, 'quests': results},
                  open(jout, 'w', encoding='utf-8'), ensure_ascii=False, indent=1)


if __name__ == '__main__':
    main()
