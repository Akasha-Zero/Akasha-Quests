# Amun-Ra — estudo do capítulo `amunra`

## Visão geral

* **Mod:** `AmunRa-GC-0.8.14.jar` (fork GTNH; modid `GalacticraftAmunRa`; entidades sem prefixo:
  `osirisBoss`, `sentryRobot`, `alienBug`, `porcodon`, `robotVillager`, `alienVillagerAR`). Sem
  pt_BR — nomes em inglês.
* **Capítulo:** `quests/chapters/tech/amunra.yaml`, `order: 392`, **67 quests** (12 main, 23 normal,
  22 optional, 3 info, 3 milestone, 1 boss, 3 portal). Depende do Galacticraft (link
  `galacticraft:foguete_t3` e `deps_hidden` em titânio/Núcleo de Feixe/Unidade Orion/motor pesado/
  Condensador).
* **Papel:** Ato IV, **Selo das Estrelas** (Faraó Morto-Vivo).
* **Não há motores de antimatéria nesta versão:** só Rocket Engine (combustível `fuel`) e Ion
  Thruster (energia + refrigerante `liquidnitrogen`, config).

## Sistema e mundos (config + bytecode)

* Todos os corpos alcançáveis usam `default_tier=3` → **Foguete Nível 3**: Horus (dim 23, planeta;
  pirâmides com chefe + vulcões; térmico +3), Anubis (22, planeta; vilas de casas-caixa/cúpulas e
  campos solares; térmico −10), Seth (24, lua de Sekhmet; mar, gelo, coral, vulcões), Neper (20, lua
  de Baal; terreno "terrestre", animais, ruínas de Urbolite), Maahes (21; Redgrass, Virilig,
  Lumipod, Pincer Bugs, Porcodons) e Cinturão de Mehen (25).
* **`generateOres=false`**: os decoradores de Anubis/Horus/Maahes/Neper/Seth não geram minérios.
  Mehen (`MehenChunkProvider`) gera mesmo assim: cobre, diamante, esmeralda, chumbo, rubi, urânio
  e Dark Matter. Chionite e Spodumene só aparecem no saque das pirâmides (`Pyramid`: Alien Book,
  Chionite, Spodumene, Nanite Cluster, rubi, ferro, ouro, diamante, pérola, picareta de desh).

## Progressão (ids reais)

* Básicos: `item.baseItem` (Ender Wafer, Fabricador de Circuitos — receita não capturada no NEI),
  `baseItem:17` Portable Thermal Regulator, `item.thermalSuit:0..3`, `baseItem:19/20` Compressed
  Gold/Gold Foil, `baseItem:21` Transformer, baterias `battery-ender/-lithium/-nuclear/-quantum`,
  `tile.machines1` (Radioisotope Generator; rende menos no calor) e `:1` (avançado).
* Armas/ferramentas: `baseItem:4` Laser Diode (rubi) → `item.raygun`; `baseItem:5` Cryo Diode
  (Chionite) → `item.cryogun`; `baseItem:28` Nanite Control Module → `item.nanotool`.
* Chefe: `osirisBoss` (Undead Pharaoh) — dropa Shuttle Schematic, Mummy Dust, ouro.
* Shuttle: `baseItem:14/15/16/23` → `item.itemShuttle`; Shuttle Dock `tile.machines4`.
* Nave-mãe (bytecode `RecipeHelper.mothershipRecipe`): 64 Estanho Prensado, 16 Alumínio Prensado,
  1 Mothership Navigation Console `tile.machines1:2` (pede Unidade Orion), 24 lingotes de ferro.
  Terminal `tile.machines1:3`; motores `tile.machines2` + `tile.msBoosters1` (foguete) e `:1`
  (iônico); Block Scale, Artificial Gravity Generator, Hydroponics Unit, baús de alumínio/aço.
  `numMothershipsPerPlayer=-1`, `maxMothershipTier=10`.

## Armadilhas e dicas

* Cair da nave-mãe ou ficar atrás de um motor mata.
* `isNHCoreLoaded` desliga as receitas do mod se o NH Core estiver presente; neste pack as receitas
  existem no registro.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `amunra:horus` | portal | Chegada a Horus. |
| `amunra:anubis` | portal | Chegada a Anubis. |
| `amunra:mehen` | portal | Chegada ao Cinturão de Mehen. |
| `amunra:farao` | boss | **Undead Pharaoh — Selo das Estrelas.** |
| `amunra:nave_mae` | milestone | Primeira nave-mãe criada. |
| `amunra:primeira_viagem` | milestone | Primeira viagem com motores. |
| `amunra:mestre_amunra` | milestone | **Marco final** (Faraó, nave-mãe em viagem, Multitool, shuttle). |
