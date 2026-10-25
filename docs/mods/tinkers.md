# Tinkers' Construct (com Iguana Tweaks, SuperTic e TiC Tooltips)

Capítulo `tinkers` — "Tinkers' Construct" — `quests/chapters/base/tinkers.yaml` (order 120, Ato I).

## Visão geral

| Mod | Versão no pack | Namespace | Papel |
| --- | --- | --- | --- |
| Tinkers' Construct (fork GTNH) | 1.14.102-GTNH | `TConstruct` | Ferramentas modulares, Fundição, armaduras do Viajante, ilhas de slime |
| Iguana Tinker Tweaks | 2.7.12 | `IguanaTweaksTConstruct` | Níveis de ferramenta, XP de mineração, restrições de peças, cabeças de mob |
| SuperTic (GTNH) | 1.5.6 | `SuperTic` | Pesquisa de Thaumcraft e ritual de Blood Magic que dão +1 modificador |
| TiC Tooltips | 1.4.1 | — | Tooltips de materiais/peças (estatísticas ao passar o mouse) |
| Mantle | 0.5.4 | — | Biblioteca (livros in-game) |

É o "Volume das Ferramentas Vivas" do Ato I (Raízes): primeira metalurgia de verdade, primeira
estrutura multibloco e a base de equipamento até o meio do jogo. Não depende de nenhum outro
capítulo (o Nether aparece só como destino de mineração).

**Atenção — SuperTic NÃO adiciona materiais neste pack.** A versão GTNH (Zokonius/bartimaeusnek)
só traz: efeitos de poção por material (todos desativados — `Effect=0` em
`config/SuperTiC/effects.cfg`), a pesquisa de Thaumcraft **Thaumic Augmentation**
(`TINKERSAUGMENTATION`, aba Artifice: a Matriz de Infusão adiciona 1 modificador, uma vez por
ferramenta) e o ritual de Blood Magic **Spell of the diligent Tinkerer** (`RitualLP=100000`).
Os materiais "extras" (Táumio, metais do Thermal, do Crepúsculo, ATM etc.) vêm das integrações
dos próprios mods com o TiC.

## Energia / recursos-base

Sem energia elétrica. O "combustível" é **lava** dentro de Tanques da Fundição. Materiais-base:
pedregulho/cascalho/argila/areia (Argamassa), madeira, pederneira, osso, cacto, papel, slime,
rocha do Nether, obsidiana e metais fundidos.

## Progressão REAL do pack (configs conferidas)

### Regras do Iguana Tweaks (`config/IguanaTinkerTweaks/`)

`Modules.cfg`: `HarvestLevelTweaks=false` (os níveis de mineração continuam os do TiC/vanilla),
`ToolLeveling=true`, `Restrictions=true`, `Tweaks=true`, `ToolPartReplacing=true`,
`MobHeads=true`, `Items=true` (baldes de argila), `MultipartCompat=true` (serras do FMP),
`Override=false`.

`main.cfg` — o que muda na prática:

* **Ferramentas novas nascem com 0 modificadores.** `ExtraModifiers=0`; o código
  (`LevelingEventHandler.onCraftTool`) subtrai 3 dos modificadores-base do TiC. Bônus de
  material (Papel "Writable", Táumio etc.) continuam valendo.
* **Níveis de ferramenta:** máximo 6 (`maxToolLevel=6`). Ganha **+1 slot de modificador** nos
  níveis **2, 4 e 6** (`ModifiersAtLevels`) e um **bônus aleatório útil** a cada nível de 2 a 6
  (`RandomBonuses=true`, `UsefulBonuses=true`: Pressa, Sorte, Auto-Fundição, Toque de Seda,
  Diamante, Esmeralda, Auto-Reparo, Reforçado, Ataque, Flamejante, Julgamento, Decapitação,
  Rouba Vida, Repulsão, Dentado, Pedregoso, Chance Crítica…). XP vem do uso
  (`xpPerLevelMultiplier=1.15`, 100% para ferramentas e armas).
* **Picaretas (e martelos) nascem com o nível de mineração −1** (`pickaxeBoostRequired=true`).
  Confirmado no registro: Picareta de Ferro `HarvestLevel:1`, Pá de Ferro `HarvestLevel:2`. O
  nível volta ao do material quando a barra **XP de Mineração** enche ("De repente, um clarão
  de luz brilha da ponta…", tooltip "Melhorado"), ou aplicando uma **cabeça de mob** na Estação
  de Ferramentas (`addMobHeadBoost=true`, sem gastar modificador).
* **Cabeças de mob e o limite de cada uma** (tooltip "Aplicável até:"), lido do bytecode
  (`IguanaToolLeveling` + `HarvestLevels.adjustToVanillaLevels`): Cabeça de Zumbi → Ferro (1);
  Crânio de Esqueleto, Cabeça de Creeper e Cabeça de Pigman → Redstone (2); Cabeça de Blaze e de
  Blizz → Obsidiana (3); Cabeça de Enderman e Crânio de Esqueleto Wither → Cobalto (4); Estrela
  do Nether → Manyullyn (5). Drop base de cabeças 5% (+2% por nível de Decapitação).
* `disableStoneTools=true`: **peças de pedra só servem para fazer moldes**.
* `allowStringBinding=true` (linha serve de conector), `allowPartReuse=true` (peças viram
  material no Construtor de Partes), `castingBurnsMaterial=true` (a peça usada para criar um
  molde de metal é destruída), `easyToolRepair=true` (reparo na bancada),
  `addFlintRecipe=true` + `gravelPerFlint=3` + `removeFlintDrop=true` (**3 cascalhos → 1
  pederneira**, cascalho não dropa pederneira), `moreExpensiveSilkyCloth=true` (Pano de Seda
  usa lingote de ouro), `moreModifiersForFlux=true` (Fluxo custa 2 modificadores),
  `removeObsidianAlloy=true` (a Fundição não gera obsidiana a partir de água + lava),
  `durabilityPercentage=80` (todas as ferramentas com 80% da durabilidade).
* **Ferramentas de outros mods continuam funcionando** (`disableRegularTools=false`,
  `disableRegularSwords/Bows/Hoes=false`).

`restrictions.cfg` (lista "allowed" padrão) — materiais não metálicos só fazem estas peças:

| Material | Peças permitidas |
| --- | --- |
| Madeira | cabo, protetor curto, conector, placa larga, limbo de arco, limbo e corpo de besta |
| Pederneira | cabeça de picareta, de pá, de machado, lâmina de faca, ponta de flecha, shuriken |
| Osso | cabo, cabeça de pá, de machado, protetor curto, lâmina de faca, ponta de flecha, limbos/corpo de besta |
| Cacto | cabo, conector, lâmina de faca, shuriken |
| Papel | cabo, conector |
| Slime / Slime-Azul | cabo, conector, (placa larga só verde), limbos/corpo de besta |
| Rocha do Nether / Obsidiana | quase tudo **exceto lâminas de espada, lâmina grande, lâmina de faca, frigideira e cinzel** (rocha do Nether também sem cabeça de martelo) |

Consequência: **não há lâmina de espada sem metal** (nem de pederneira, pedra ou osso). A
primeira arma "barata" é o **Punhal** (Dagger: lâmina de faca de pederneira/osso/cacto).

### TConstruct (`config/TinkersConstruct.cfg`)

* `Craft metals with Wood Patterns=false` + livro "Note: The Smeltery is required to process
  all metals, including iron": **peças de metal só por fundição**.
* `Metal cast FluidType=AluminumBrass` e `Remove Gold Cast Recipes=false`: moldes de metal com
  **Alumínio-Bronze ou Ouro**.
* `Ingots per ore=2` (minério vale 2 lingotes). Retorno de ligas: Bronze 4, Alumínio-Bronze 4,
  Alumite 3, Manyullyn 1, Gusa 1.
* `Always cast TConstruct ingots=true`; `Items can be thrown into smelteries=true`; horses →
  cola e villagers → esmeralda liquefeita permitidos.
* `Keep levels on death=true`, `Keep hunger on death=true`, `Tools lose 10% durability on
  death=true`, `Dropped tools are indestructible=true`, `Drop heart canisters on death=false`.
* `Modifiers increase Mining Level=true`: Diamante → nível 3, Esmeralda → nível 2 (livro).
* `Scythe AOE harvest=true`.

### Níveis de mineração (nomes do tooltip, Iguana com níveis vanilla)

0 Pedra · 1 Ferro · 2 Redstone · 3 Obsidiana · 4 Cobalto · 5 Manyullyn (6+ "Manyullyn+").

Níveis por material **confirmados no registro** (pá = nível do material; picareta nasce com −1):

| Material | Nível | Picareta nova → melhorada |
| --- | --- | --- |
| Madeira, Pederneira, Osso, Cacto, Cobre, Plástico, Slime Rosa, Chumbo, Electrum, Naga Scale, Blood Infused Wood, Magical Wood | 1 | 0 → 1 |
| Slime, Slime-Azul, Papel | 0 | 0 |
| Ferro, Bronze, Rocha do Nether, Aço HSLA, Invar, Ferrous (níquel), Prata, Steeleaf | 2 | 1 → 2 |
| Obsidiana, Gusa, Táumio, Fiery, Tritanium, Blood Infused Iron | 3 | 2 → 3 |
| Cobalto, Ardite, Alumite, Aço, Knightmetal, Brilhante (platina), Unstable Induced | 4 | 3 → 4 |
| Manyullyn, Neutronium, Infinity, Allthemodium | 5 | 4 → 5 |
| Vibranium | 6 | 5 → 6 |
| Bedrockium, Unobtainium | 7 | 6 → 7 |
| Bedrock (RotaryCraft) / Chromastone | 800 / 1200 | — |

Total de **45 materiais** (metas de peça): 0–18 (base), 31 Táumio, 42–45 Crepúsculo, 80/81
RotaryCraft (Bedrock, HSLA), 90 Chromastone, 201 Tritanium (Matter Overdrive), 250/251 Blood
Arsenal, 314–316 Extra Utilities, 500/501 Avaritia, 1000/1001 MFR, 1020–1025 Thermal Foundation
(Invar, Ferrous, Chumbo, Prata, Brilhante, Electrum), 19001–19003 ATM Legacy.

### Ordem de progressão do capítulo

1. **Oficina:** Estêncil em Branco (2 tábuas + 2 gravetos → 4) → Mesa de Estênceis (estêncil +
   tábua), Construtor de Partes (estêncil + tronco), Estação de Ferramentas (estêncil + bancada),
   Baú de Moldes. Livros: estêncil + papel → *Materiais e Você v1* → v2 → *Fundições Poderosas*
   → *Arsenal do Construtor* → *Diário de um Construtor* (cada um craftado do anterior).
2. **Pedra lascada:** pederneira (3 cascalhos), peças de pederneira/madeira/osso/cacto/papel,
   picareta/pá/machadinha/alvião (Mattock)/punhal (Dagger); XP de ferramenta e "Melhorado".
3. **Fundição** (`TConstruct:Smeltery` etc.): Argamassa (`CraftedSoil:1`: 1 argila + 1 areia + 1
   cascalho → 2) → fornalha → Tijolo da Fundição (`materials:2`) → bloco de tijolos (4).
   Controle (8 tijolos), Dreno, Tanque (8 tijolos + vidro), Torneira, Mesa e Bacia de Fundição.
   Estrutura: piso 3×3 de tijolos, paredes 1 nível acima formando casca 5×5 com o Controle,
   ao menos 1 Tanque com lava; mais camadas = mais capacidade.
4. **Moldes:** Molde em Branco (ouro/alumínio-bronze na mesa vazia), moldes de barra/pepita/gema
   e de cada peça (despeje o metal sobre uma peça — de preferência de pedra, que não serve pra
   nada além disso). Moldes de argila existem (`clayPattern`, `blankPattern:3`) — conferir no NEI.
5. **Metais e ligas:** Ferro → Cobre+Estanho = Bronze → Alumínio → Alumínio-Bronze → Obsidiana
   (derreter blocos) → Alumite (Alumínio 5 : Ferro 2 : Obsidiana 2) → Gusa (Ferro + Sangue +
   Esmeralda) → Aço (derreter `ingotSteel` de IE/IC2/Mekanism…).
6. **Nether:** Cobalto e Ardite (`SearedBrick:1/2`), exigem nível **Cobalto (4)**: picareta de
   Alumite/Aço melhorada, ou cabeça de Esqueleto Wither / Enderman. Manyullyn = Cobalto + Ardite.
7. **Modificadores, Forja (Tool Forge), armas, arcos/bestas, Viajante, corações, slime, varal.**

## Materiais e geração de mundo (config)

* **Nether:** Minério de Cobalto e de Ardite, densidade 8 cada (`Generate Cobalt/Ardite=true`).
* **Superfície:** cascalhos de minério (`GravelOre`) de ferro (raridade 400), ouro (900), cobre
  (100), estanho (100), alumínio (50) e cobalto (2000).
* **Subterrâneo:** só **Alumínio** (Y 0–64, densidade 3). Cobre e estanho subterrâneos do TiC
  estão **desligados** (`Generate Copper/Tin=false`) — use os de outros mods (oredict).
* **Arbustos de frutas de minério** (só no Overworld, `OreBushDimWhitelist=0`): ferro, ouro,
  cobre (Y 20–60), estanho (Y 0–40), alumínio (Y 0–60) e essência (XP). Precisam de escuro.
* **Ilhas de slime:** só dimensão 0, raridade 1450. Slime-Azul (`TConstruct.EdibleSlime`) e
  Slime Rei (`TConstruct.KingSlime`).
* Outros mods também registram `oreCobalt` (Industrial Upgrade, inclusive Overworld/End).

## Máquinas, estruturas e multiblocos

* **Fundição** (descrita acima). Versão do Nether: `SmelteryNether`, com Argamassa do Nether
  (`CraftedSoil:6`: fungo do Nether + areia das almas + cascalho) → `materials:37`.
* **Estação de Ferramenta / Forja de Ferramentas:** Forja = 3 blocos de tijolo da fundição em cima, Estação no
  centro, 4 blocos de metal (ferro, ouro, diamante, esmeralda, cobalto, ardite, manyullyn,
  cobre, bronze, estanho, alumínio, alumínio-bronze, alumite ou aço — 14 variantes).
* **Cabide de Secagem** (3 lajes de madeira): carne → charque.
* **Estação de Trabalho:** bancada que mantém itens e acessa baús vizinhos.

## Dimensões, estruturas, chefes e mobs

Ilhas de slime (Overworld), Slime-Azul (dropa Slime Gelatinoso `strangeFood:0`), Slime Rei.
Corações: Mini Corações são drops raros de criaturas/chefes (a config tem a lista de exceção
`YellowHeartDropBlacklist` = NPCs do Custom NPCs; fonte exata não confirmada sem o NEI);
1 Mini Coração Amarelo vira 1 Mini Coração Vermelho na bancada. **Lata Verde não tem receita no pack** (não incluída).

## Integrações no pack

* Osso Necrótico: além do drop do Esqueleto Wither, **3 Wither Essence (Magical Crops)** em X.
* Fluxo: Capacitor de Flux de Leadstone/Endurecido ou Célula de Energia (Thermal Expansion).
* Metais fundidos de Thermal, Twilight, ATM, Extra Utilities, RotaryCraft, Blood Arsenal etc.
  (fluidos `*.molten` existem no registro) → peças por fundição.
* SuperTic: Thaumcraft `TINKERSAUGMENTATION` e ritual de Blood Magic (100 000 LP).
* Iguana: baldes de argila (inclusive para metais fundidos), cabeças extras (Enderman, Pigman,
  Blaze, Blizz), serras de Cobalto/Ardite/Manyullyn (ForgeMultipart).

## Armadilhas e dicas

* Picareta nova "não minera" o minério esperado: é o −1 do Iguana. Minere pedra até melhorar.
* Não use peças boas para fazer moldes: a peça é destruída (`castingBurnsMaterial`).
* Ferramentas novas sem slot: suba o nível antes de gastar diamante/redstone; Papel e Táumio
  dão slots extras.
* Lava do Tanque acaba; mantenha baldes extras. Metais ficam na Fundição se o chunk descarregar.
* Silky (Toque de Seda) é incompatível com Sorte (Lápis) e Auto-Fundição.

## Pendências de verificação (NEI ainda não importado)

* Receita do **Reforço** (`materials:43`) e da **Bola de Cola** (`materials:36`): não estão na
  bancada; as quests mandam conferir no NEI.
* Receitas de varal, mesa/bacia de fundição e Construtor de Partes foram descritas pelo livro
  do mod e pelo comportamento padrão do TiC 1.7.10.

## Marcos exportados

* `tinkers:primeira_picareta` (main) — primeira ferramenta modular (Ato I: "primeira ferramenta").
* `tinkers:fundicao_montada` — Fundição completa (primeira metalurgia do Ato I).
* `tinkers:biblioteca_moldes` — conjunto de moldes de metal essenciais.
* `tinkers:liga_alumite` — primeira liga de nível Cobalto (Alumite).
* `tinkers:manyullyn` — Manyullyn, a liga do Nether (fim da metalurgia do TiC).
* `tinkers:mestre_forja` — Martelo, Machado de lenhador (Lumber Axe) e Escavador (ferramentas grandes).
* `tinkers:arsenal` — Espada (Broadsword), Espada Longa e Florete (Rapier).
* `tinkers:viajante_completo` — conjunto completo do Viajante.

Nota de nomes (pt_BR do jogo): Broadsword = "Espada", Rapier = "Florete", Dagger = "Punhal",
Cutlass = "Cutelo", Cleaver = "Decepador", Mattock = "Alvião", Lumber Axe = "Machado",
Javelin = "Lança", Bolts = "Dardos", Tough Rod = "Cabo" (mesmo nome do cabo comum),
Broad Axe Head = "Cabeça de Machado de Batalha". As quests usam esses nomes.
