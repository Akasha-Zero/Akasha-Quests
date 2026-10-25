# ChromatiCraft — estudo do capítulo `chromaticraft`

## Visão geral

* **Mod:** ChromatiCraft V33a + DragonAPI V33b; patches do pack: Akashic ChromatiCraft
  Compatibility Fix 1.3.0 (IDs de encantamento 32001–32013, sons), Chroma Heap/Structure Heap
  Fix, ChromatiFixes 1.14.5. **Sem lang pt_BR** — nomes em inglês. Muitos nomes no dump do
  registro aparecem como `<err>`, mas os ids/metas foram validados pelo lint e mapeados pelo
  bytecode (`ChromaStacks`, `CrystalElement`).
* **Capítulo:** `quests/chapters/magia/chromaticraft.yaml`, `order: 580`, 201 quests.
* **Campanha:** Ato II→IV, alimenta o **Selo Arcano**.
* Fontes: registro, bancada/NEI (`ccfurnace`, `ccfabric`, `ccglowfire`, `ccplants`), Léxico
  interno (`Resources/progression.xml`, `info.xml`, `machines.xml`, `blocks.xml`,
  `resource.xml`, `tools.xml`), lang en_US, config `ChromatiCraft.cfg`, e bytecode de
  `ProgressionManager.load()`, `ResearchLevel.canProgressTo`, `ChromaResearch`, classes de
  receitas de fundição (tier de cada receita) e `PoolRecipes`. **As receitas da Mesa de Fundição
  não estão no NEI** — as quests indicam o tier correto (básica/runas/multibloco/Pylon) sem
  listar ingredientes.

## Energia

Energia lúmen dos 16 elementos (Kuro, Karmir, Kijani, Ruskea, Nila, Zambarau, Vadali, Argia, Ykri,
Ruzova, Asveste, Kitrino, Galazio, Kurauri, Portokali, Tahara), vinda de Pylons (só Overworld pela
config), transmitida por repetidores (feixes bloqueados por blocos opacos), relés, Power Crystals,
Lumen Tree; energia pessoal (drenada com o Manipulador) para habilidades.

## Progressão (gating real do código)

Prereqs de ProgressStage (stage ← prereq): CASTING←CRYSTALS; ALLCOLORS←PYLON; RUNEUSE←ALLCOLORS,
CASTING; MULTIBLOCK←RUNEUSE, **VILLAGECASTING**; USEENERGY←PYLON, RUNEUSE; CHARGE←PYLON,
CRYSTALS; MAKECHROMA←CASTING; SHARDCHARGE←MAKECHROMA, RUNEUSE, DYETREE; ALLOY←SHARDCHARGE,
MULTIBLOCK, CHROMA; INFUSE←ALLOY; TUNECAST←RUNEUSE, CHROMA, MULTIBLOCK; ENERGYIDEA←USEENERGY;
BLOWREPEATER←USEENERGY; REPEATER←BLOWREPEATER, ENERGYIDEA, TUNECAST; LINK←PYLON, REPEATER;
ABILITY←CHARGE, LINK; STORAGE←MULTIBLOCK; POWERCRYSTAL←LINK, STORAGE, CHARGE, INFUSE;
POWERTREE←POWERCRYSTAL; BEDROCK←DEEPCAVE; NETHER←BEDROCK; END←NETHER; NETHERROOF←NETHER,
ANYSTRUCT; NETHERSTRUCT←NETHERROOF; **DIMENSION←ALLCOLORS, END, NETHERSTRUCT, POWERCRYSTAL,
RAINBOWFOREST, GLOWCLIFFS, CAVERN, BURROW, OCEAN, DESERTSTRUCT, SNOWSTRUCT**;
STRUCTCOMPLETE←ABILITY, DIMENSION; ALLCORES←STRUCTCOMPLETE; CTM←ALLCORES;
TURBOCHARGE←DIMENSION, POWERTREE, STRUCTCOMPLETE.

Níveis do Léxico (ordem e condição): Entry; Exploration (CRYSTALS); **Basic Crafting
(ANYSTRUCT — qualquer estrutura gerada)**; Rune Crafting (fundiu receita básica); Energy (CHARGE
+ PYLON); Multiblock (fundiu receita de runas); Networking (fundiu multibloco); Pylon Casting
(REPEATER); Endgame (fundiu receita de Pylon); Elemental Mastery (CTM).

Itens-chave: Léxico `item_help`, Manipulador `item_tool`, Mesa `item_placer:5`, fragmentos
`item_shard:0-15` (carregados 16–31), runas `block_rune`, Pedra Cristalina `block_pylonstruct`,
Chroma Collector `:4`, balde de chroma `item_bucket`, Item Stand `:13`, cores `item_crafting`
(0 void, 5 energy, 7 transform, 8 element unit, 10 iridescent chunk, 11 chroma alloy…), grupos
`item_cluster`, repetidores `:68`/`:11`/`:30`/`:55`, Guardian Stone `:8`, Ritual Table `:16`,
Power Crystal `:21`, Luminescent Battery `:36`, Tile Accelerator `item_adjacency:12`, Portal
`block_portal`, Dimensional Core `:43`, Aura Locus `:42`, Energy Focal Point `:52`.
Liga de chroma: 1 ferro + 16 Pó Crômico (`item_tiered`) na piscina de chroma.

## Dimensão e estruturas

Proxima (dim 60), volátil (não persiste), estruturas-quebra-cabeça (dificuldade 3 na config),
biomas 101–106. Estruturas do Overworld: Crystal Shrine, Crystal Burrow, Ocean Temple, Sandy
Burrow, Frozen Outpost, Data Tower, vilas com tentativas fracassadas dos aldeões. Biomas: Rainbow
Forest 48, Luminous Cliffs 49, Ender Forest 47.

## Integrações

Thaumcraft (integração "mais difícil" ligada; essentia relay, aspect jar), Forestry (abelhas,
frames), AE2 (ME distributor, Void Cell), Botania (Mana Booster), TConstruct (smeltery), RF.

## Armadilhas (config)

Pylons só no Overworld, dão choque; repetidores de madeira explodem (e isso é obrigatório para o
Crystal Repeater); morrer perde conhecimento/energia; upgrades de adjacência consomem energia;
pingentes aprimorados não precisam de carga; varinha de duplicação não copia TileEntities;
`Auxiliary Fragment Acquisition=false`.

## Marcos exportados

| Marco | Representa |
| --- | --- |
| `chromaticraft:todas_as_cores` | As 16 cores (Elemental Awareness). |
| `chromaticraft:ligacao_pylon` | Fundição de Pylon (Top-tier Crafting). |
| `chromaticraft:primeira_habilidade` | Primeira habilidade por ritual. |
| `chromaticraft:pronto_para_proxima` | Todos os requisitos do outro mundo. |
| `chromaticraft:proxima` | (portal) Entrada em Proxima, dim 60. |
| `chromaticraft:nucleos_dimensionais` | 16 Dimensional Cores. |
| `chromaticraft:monumento` | Monumento restaurado (CTM). |
| `chromaticraft:maestria_elemental` | **Marco final** — Selo Arcano. |
