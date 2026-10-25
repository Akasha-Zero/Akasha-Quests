# Briefing curto (modo econômico)

Repo `/home/user/Akasha-Quests`. Livro BetterQuesting 1.7.10 do pack Technic Forbidden.
**Não leia outros docs** além deste e de UM capítulo pronto como exemplo de tom
(`quests/chapters/tech/ic2.yaml`, só as primeiras ~150 linhas).

## Fluxo (siga nesta ordem, sem pesquisa extra)
1. `python3 tools/bqgen/scaffold.py <id> <namespace,...> --title "..." --order N > quests/chapters/<pasta>/<id>.yaml`
   (namespaces: `python3 tools/bqgen/reg.py mods` ou `reg.py mod <ns> --limit 5`).
2. Edite o YAML: corte itens irrelevantes (blocos decorativos repetidos, variantes), funda
   quests triviais, marque tipos (`main` trilha, `milestone` fim de fase, `boss`, `portal`,
   `optional`), ajuste `deps`, e escreva cada `desc` (substitua os `TODO`).
3. Só pesquise o que faltar: `reg.py neiout/neiin <id>`, `reg.py recipe <id>`,
   `reg.py entity <rx>`, `reg.py dims`, lang do JAR (`unzip -p <jar> assets/<mod>/lang/en_US.lang | grep ...`).
   **Não decompile bytecode, não use web**, salvo se uma quest-chave ficaria errada sem isso.
4. `python3 tools/bqgen/bqgen.py lint quests/chapters/<pasta>/<id>.yaml` até OK.
5. Escreva `docs/mods/<id>.md` CURTO (≤ 40 linhas): visão geral, progressão, armadilhas,
   **Marcos exportados** (`capitulo:quest`).

## Voz (Akasha)
Narradora calorosa, guardiã do Registro Akáshico; fala com o jogador como "Herói"/"você"
(sem gênero para o jogador; Akasha fala de si no feminino). PT-BR informal. Cada desc:
gancho curto → instrução prática (receita/máquina/onde achar) → incentivo. 30–90 palavras;
marcos/chefes até 150. Destaques com `§e` (item), `§a` (dica), `§c` (perigo). Nada inventado:
se não souber, oriente a conferir no NEI.

## Formato (resumo)
Tarefas: `get`, `craft`, `one_of`, `kill: "Entidade xN"`, `visit: {dim: N, name: "..."}`,
`fluid: "agua x1000"`, `xp: N`, `check`. Recompensas: `item`, `choice`, `xp`,
`loot: comum|incomum|raro|epico|lendario`, `kit: {name, items}`. Ids de quest `snake_case`.
Recompensa útil para o PRÓXIMO passo; marcos: kit + loot; chefes: loot raro/épico.

Não faça commit. Relatório final: ≤ 10 linhas (capítulos, nº de quests, marcos, problemas).
