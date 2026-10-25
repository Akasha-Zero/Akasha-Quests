# Gadomancy — estudo do capítulo `gadomancy`

## Visão geral

* **Mod:** Gadomancy `gadomancy-1.5.14.jar`, namespace `gadomancy`. Addon do Thaumcraft 4.2.3.5
  com aba própria (94 pesquisas, incluindo as `GADOMANCY.PSEUDO.*` que espelham pesquisas do
  Thaumcraft e as `GADOMANCY.AURA.<aspecto>` dos efeitos do Pilão).
* **Capítulo:** `quests/chapters/magia/gadomancy.yaml`, `order: 511`, 46 quests (meta do MAPA:
  55 — o mod não tem conteúdo verificável suficiente para mais sem encher linguiça; os itens
  "falsos" `ItemFakeGolemPlacer/Shield/ModIcon` e `ItemCreativeNode` não são pedidos).
* **Papel:** Ato II (jarros, golems) ao Ato IV (Manipulador de Nodo, Portão dos Andarilhos do
  Vazio e o Labirinto Eldritch próprio, dim −73). Peça do **Selo Arcano**.

Fontes: `reg.py mod gadomancy`, `reg.py research`, `reg.py dims`, `reg.py entity`, NEI (saídas),
lang en_US, `config/gadomancy.cfg` e as classes descompiladas
`makeo.gadomancy.common.registration.RegisteredRecipes`, `ItemAuraCore`, `AuraEffects`,
`TCMazeHandler`, `RecipeStickyJar`.

## Config do pack

`dimOuterId = -73`, `maxMazeCount = -1`, `enableAdditionalNodeTypes = true` (Growing Node),
`eldritchdecoblocks = true`, **`ancientStoneRecipes = false`** (Pedra Antiga e Pedestal de Pedra
Antiga só nas Terras Exteriores do Thaumcraft), `auraPylonBlacklist` vazio.

## Progressão (ids reais)

* `BlockStickyJar` (Arcana: jarro com Bola de Slime embaixo), `BlockRemoteJar` (Arcana: Toras de
  Greatwood + Jarro + Espelho de Essência; redes de N jarros com capacidade somada),
  `BlockArcaneDropper` (área 3×3×3).
* Golems: `itemSilverwoodGolemPlacer:8` (infusão inst. 7; carga 8, 3 melhorias, rápido),
  melhoria Golemic Shielding (infusão no golem com Cinto Rúnico + 2 Salis), `ItemGolemCoreBreak`
  (Colher + 3 ferramentas elementais), `ItemGolemCoreBreak:1` Bodyguard (Proteger + Botas do
  Viajante + 2 Pérolas + Espada de Zephyr), `BlockInfusionClaw` (inst. 10; em cima da matriz,
  redstone dispara infusão, adiciona instabilidade).
* Núcleos de Aura `ItemAuraCore` (infusão: Amuleto Primal + 4 Estilhaços Equilibrados + 8
  Táumio). Meta = tipo: 0 branco, 1 Aer, 2 Ignis, 3 Aqua, 4 Terra, 5 Ordo, 6 Perditio.
  Sintonia: núcleo no chão perto de um aglomerado primal + varinha no cristal (84 vis do primal).
  Lavagem no Crisol (Praecantatio 12, Aqua 12, Sano 18).
* `BlockAuraPylon` + `:1` (Topo, com núcleo Ignis); multibloco 1×4×1 (150 de cada primal);
  sintoniza-se jogando Essência Cristalizada perto. Efeitos: tabela `AuraEffects` (poções em área
  e efeitos em blocos/criaturas).
* `BlockStoneMachine:4` Empacotador (núcleo Aer), `BlockKnowledgeBook` (núcleo Ordo; em Pedestal
  Arcano + nota + Cognitio), `ItemElement` (Elemento das Trevas), `BlockEssentiaCompressor`
  (núcleo Aqua; 3 blocos empilhados; 200 → até 3000 por aspecto; explode em Taint),
  `BlockStoneMachine:2` Emissor de Enervação (impede creepers/endermen).
* `ItemEtherealFamiliar` (infusão: Amuleto de Proteção Rúnica + 2 Essências Etéreas + 2 Salis +
  2 Nitor + 10 essência do aspecto) e 7 linhas de melhoria (Shock, Fire, Poison, Weakness,
  Damage, Range, Speed), cada uma com até 3 níveis.
* Encantamento Thaumic Sight (Revealer) e Armor Disguise.
* Nodos: `BlockNodeManipulator:5` (Pedestal de Recarga + ...), `BlockStoneMachine:0` Foco de
  Aleatorização; multibloco 3×3×3 (nodo no topo; foco no meio com 4 Pedras Antigas; manipulador
  embaixo com 4 Pedestais de Pedra Antiga; 100/primal; ≥70/primal na varinha para operar).
* Portão: `BlockStoneMachine:3` Foco de Redirecionamento (2 Olhos Eldritch), `BlockStoneMachine:1`
  Pedestal Antigo ×4 com Olhos Eldritch; forma por 150/primal, abre com 120/primal →
  `BlockAdditionalEldritchPortal` → dim −73.

## Dimensões, estruturas e chefes

* **Outer Lands (Gadomancy), dim −73** (`WorldProviderTCEldrich`): sessão de labirinto
  eldritch individual, gerada pelo Thaumcraft; o salvamento do mundo é desligado e quem está lá
  sem sessão é mandado de volta. Ao vencer o chefe, abrem-se portais de saída e o tempo é
  registrado (`Gadomancy:MazeTime`). O portão de entrada só aguenta uma passagem.
* Chefes possíveis (entidades do Thaumcraft): `Thaumcraft.EldritchWarden`,
  `Thaumcraft.EldritchGolem`, `Thaumcraft.CultistLeader`. A quest de chefe aceita qualquer um
  (`task_logic: OR`).

## Integrações no pack

* Golems do Thaumcraft (núcleos e melhorias); Terras Exteriores do Thaumcraft (dim −42) para
  Pedra Antiga/Pedestal; Olhos Eldritch e Pérola Primordial do lado eldritch do Thaumcraft.

## Armadilhas e dicas

* Garra de Infusão e Manipulador de Nodo têm instabilidade de infusão 10.
* Condensador quebrado com essência = explosão + Taint imediato; Elemento das Trevas cega.
* Growing Node destrói nodos menores e dispara raios.
* Algumas auras do Pilão são nocivas (Perditio, Mortuus, Tenebrae, Venenum, Vinculum, Fames).

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `gadomancy:garra_infusao` | milestone | Infusão automatizada. |
| `gadomancy:condensador` | milestone | Condensador de Essência. |
| `gadomancy:manipulador_montado` | milestone | Manipulador de Nodo operando. |
| `gadomancy:portao_vazio` | portal | Entrada no Labirinto Eldritch do Gadomancy (dim −73). |
| `gadomancy:labirinto_chefe` | boss | Chefe do labirinto derrotado. |
| `gadomancy:mestre_gadomancia` | milestone | **Marco final** — candidato ao Selo Arcano. |
