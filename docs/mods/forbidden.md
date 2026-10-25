# Forbidden Magic 0.9.17-GTNH — estudo do capítulo `forbidden`

## Visão geral

* `Forbidden.Magic-0.9.17-GTNH.jar` (modid `ForbiddenMagic`). Capítulo `quests/chapters/magia/forbidden.yaml`,
  `order: 506`, 63 quests. Raiz `proibido` (`deps_hidden: [thaumcraft:taumonomicon]`); infusões com
  `deps_hidden: [thaumcraft:infusao]`; itens que pedem pérola/vazio ligam a `thaumcraft2:*`.
* Fontes: registro, NEI (saídas), decompilação CFR (`ForbiddenRecipes`, `compat/BloodMagic`,
  `compat/Compat`, `FMEventHandler`, `VillagerHereticManager`, `ForbiddenItems`), lang, `config/ForbiddenMagic.cfg`
  (todas as integrações ligadas; Gluttony=0 → receitas com diamante).
* Papel: Ato II/III, magia "sombria"; contribui para o Selo Arcano.

## Estilhaços do Nether (`NetherShard:0-6`, `GluttonyShard`) — `FMEventHandler`

Quase todos só no Nether (dim −1): **Ira** (0) mobs hostis mortos por jogador, chance cresce com dano/
encantamentos da arma; **Inveja** (1) Pigman às vezes segura (1/175), cai ao morrer; **Mácula** (2)
cavando mácula com a Pá do Purificador (Toque Suave/Fortuna ajudam, qualquer dimensão); **Orgulho** (3)
chefe morto no Nether; **Luxúria** (4) filhote de animal nascendo no Nether; **Preguiça** (5) mobs do
Nether mortos sem ser por jogador; **Ganância** (6) Pilhagem/foco Tesouro, quartzo com Fortuna;
**Gula** comer no Nether (20%). Encantamento Corruptor converte estilhaços de pedras infundidas.

## Progressão (ids reais)

Rosa Umbral `UmbralBush` (roseira + 8 Tenebrae + 5 Victus), Tinta `FMResource:1`, Escrita Cristalina
`Crystalwell`, fragmentos de esmeralda `FMResource` (traças), Anel da Nutrição, Bolo Táumico,
Machado Arrancador `SkullAxe`, Foco Piscar `BlinkFocus`, Garfo `DiabolistFork`, Cristal `MobCrystal`
(diamante + 10 Cognitio/Potentia), **Gaiola da Ira** `WrathCage` (instab. 10; consome essência),
Coleira da Dor, Chicote, ferramentas Camaleão `Morph*`, Pá do Purificador `TaintShovel`, Picareta da
Distorção, Pedra/Árvore Maculadas, Hexcoal, hastes `WandCores`: 0 Maculada (150), 1 Infernal (150),
6→3 Sangue (altar tier 4, 20.000 LP), 9 cajado de sangue, 4 Witchwood (Ars), 8→7 Livingwood e 12→11
Dreamwood (Piscina de Mana), pontas `WandCaps` 0 Alquímica, 1 Vinteum, 4→3 Manasteel, 6→5 Elementium,
2 Terrasteel (180%). Rapieira da Strix, Orbe de Sangue Eldritch (80M LP), Escrita Primal (pérola),
flores Botania `specialFlower {type:"euclidaisy"|"whisperweed"|"tainthistle"}`.
Varinha Profana `{rod:"profane"}` vendida pelo aldeão **Herege** (esmeraldas + graveto).

## Itens pedidos que **não existem** no pack

"Dark Gold", "Ring of Eternal Life" e uma "Taint wand"/"Blood wand" como itens próprios não existem nesta
versão — as varinhas são núcleos (`WandCores`) montados em varinhas do Thaumcraft. "Morphic Pickaxe" =
Picareta Camaleão.

## Armadilhas e dicas

Pesquisas com Warp: Haste de Sangue 2, Witchwood 2, Rapieira 2 (+2 no item), Orbe Eldritch 3 (+5).
Bolo não pode ser movido depois de colocado. Gaiola precisa de essência por mob.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `forbidden:sete_pecados` | milestone | Os sete estilhaços do Nether. |
| `forbidden:arsenal_proibido` | milestone | Gaiola da Ira, Machado, Camaleão, Piscar. |
| `forbidden:mestre_proibido` | milestone | **Marco final** (hastes Maculada, Infernal, Sangue) → Selo Arcano. |
