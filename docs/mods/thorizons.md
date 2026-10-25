# Thaumic Horizons — estudo do capítulo `thorizons`

## Visão geral

* **Mod:** Thaumic Horizons, `ThaumicHorizons-1.8.22.jar` (modid/namespace `ThaumicHorizons`,
  versão 1.8.22 segundo `reg.py mods`). Addon do Thaumcraft 4.2.3.5 com aba própria no
  Taumonomicon (`ThaumicHorizons`, 74 pesquisas no registro).
* **Capítulo:** `quests/chapters/magia/thorizons.yaml`, `order: 508`, 76 quests.
* **Papel na campanha:** taumaturgia da vida. Começa no Ato II (Crisol e Infusão), sobe para o
  Ato III/IV com o Tonel Curativo, almas e a Pérola Primordial (que só vem das Terras
  Exteriores do Thaumcraft). Alimenta o **Selo Arcano** (Ato IV).
* **Não há tradução pt_BR** no JAR (só en_US, ru_RU, zh_CN): itens do mod aparecem em inglês.

Fontes: registro do pack (`reg.py mod ThaumicHorizons`, `reg.py research`, `reg.py dims`),
lang `assets/thaumichorizons/lang/en_US.lang` (textos das páginas do Taumonomicon), config
`config/ThaumicHorizons.cfg` e a classe principal `com.kentington.thaumichorizons.common.ThaumicHorizons`
descompilada (CFR + nomes MCP stable-12) para conferir **todas** as receitas de Crisol, Arcana e
Infusão, as infusões de criatura (`CreatureInfusionRecipe`) e de si mesmo (`SelfInfusionRecipe`)
e os pontos de Warp. O catálogo NEI ainda não estava importado.

## Energia / recursos-base

* **Essentia e vis** do Thaumcraft. Crisol (aspectos jogados), Mesa Arcana (vis da varinha),
  Altar de Infusão (essência + instabilidade). O Tonel Curativo recebe essência **encanada**.
* Geradores de centivis próprios: **Vis Dynamo** (1 vis/s da varinha → 5 cV) e **Essentia
  Dynamo** (1 essência/s → 12 cV, divididos entre os primais do aspecto).

## Progressão (ids reais)

1. **Alquimia da vida (Crisol):** Alcheponics (sementes + Lux/Terra/Aqua 2 → 2 colheitas;
   melancia/abóbora com 3 → bloco), `ThaumicHorizons:eggIncubated` (Ovo + Ignis 2 + Victus 2),
   Meat Growth (carne crua + Victus 3 → 2), Flesh Hardening (carne + Fabrico + Pannus → couro;
   carne podre + Ordo), Shard Transmutation (Estilhaço Equilibrado + 2 primal + 1 Permutatio).
2. **Dispositivos:** `synthNode` (Ethereal Shard; infusão de Estilhaço Equilibrado + 4 Nitor,
   inst. 7), `alchemite` (TNT + Potentia/Vitreus/Permutatio 4), `visDynamo`, `essentiaDynamo`,
   `nodeMonitor`, focos `focusIllumination` (Crisol: Pedra Luminosa + Lux 12/Potentia 8/Ignis 8),
   `focusLiquefaction` (Diamante + Foco de Fogo + Salis + Foco de Escavação),
   `focusDisintegration`, barcos `boatGreatwood`/`boatThaumium` (este navega em lava).
3. **Lentes** (precisam de Óculos + Infusão; centro = Painel de Vidro): `lensFire` (visão
   noturna), `lensAir` (Kirlian, vê criaturas pela parede), `lensWater` (eco subaquático),
   `lensEarth` (raio-X 3×3), `lensOrderEntropy` (escaneia sozinha), `lensCase`. Tecla V troca,
   Shift+V remove.
4. **Sangue:** `syringeEmpty:2` (Arcana: Frasco de Vidro sobre Pepita de Ferro), `syringeBlood`
   (clique direito tira o próprio sangue), `bloodInfuser` (Seroconverter; infusão no Crisol),
   `syringeInjection`, `syringeBlood:1` (Blast Phial ×4), `injector` (Arco de Osso).
5. **Tonel Curativo** (multibloco, ver abaixo) → **Encarnação** (`nutrients` = Açúcar + Fames 2;
   amostra de tecido + 4 Victus) → `focusContainment` → `modMatrix` (Matriz Rúnica Modificada,
   inst. 10) → infusões de criatura.
6. **Almas:** `soulSieve` → `inspiratron` (papel → Fragmento de Conhecimento) / `soulforge`
   (Cognitio na tampa) → `soulJar` → `corpseEffigy` → reencarnação de aldeão.
7. **Golems:** `golemPowder` (Crisol: Salis + Humanus/Spiritus/Motus 4), `focusAnimation`.
8. **Planar:** `planarConduit` → `transductionAmplifier` + `vortexStabilizer` → fenda
   (4 amplificadores + 6 atenuadores num nodo) → `wandCastingDisposable`, `voidPutty`, wisps,
   Voidling Golems, `recombinator` → **Plano de Bolso** (Pérola Primordial na fenda) →
   `keystoneTH`, `slotTH`, Planar Gateway.
9. **Corpo próprio:** `soulBeacon` (Self-Reincarnation), `amuletMirror`, autoinfusões.

## Materiais e geração de mundo

O mod não gera minérios. Os itens de mundo vêm do Plano de Bolso: nuvens planares
(`cloudTH:*`, `cloudGlowingTH:*`), Fóssil Primordial (`leviathanTH`), cristais, carne/ossos.
Ingredientes externos importantes: Pérola Primordial (`Thaumcraft:ItemEldritchObject:3`, Terras
Exteriores), Metal do Vazio, Pedra Antiga (`Thaumcraft:blockCosmeticSolid:11`, Terras Exteriores).

## Máquinas, estruturas e multiblocos

* **Tonel Curativo** (`ConfigResearch.recipes "CurativeVat"`): 3×3×4. Camadas de baixo e de
  cima: 8 Tábuas de Greatwood + Construto Alquímico no centro; as duas do meio: 8 Vidros + Água
  no centro. Formado com varinha por 50 Aqua/Terra/Ordo. Essência na base: Victus (cura lenta),
  Fames (fome), Sano (cura rápida e remove efeitos negativos). Escotilha da tampa = entrar/sair.
* **Infusão de criaturas:** Matriz Rúnica Modificada em cima do tonel; mesma mecânica do altar,
  essência encanada, instabilidade fere a criatura, não pode ser abortada; centivis de
  Aqua+Ordo+Terra ajuda. Humanos, mortos-vivos e golems não aceitam. Várias infusões gerais,
  **uma** de espécie. Não passa para filhotes (exceto Coguvaca). Clones de criaturas infundidas
  custam a soma dos aspectos e não geram instabilidade.
* **Infusões gerais** (ingredientes conferidos): Quicksilver Limbs (Poção de Velocidade II +
  2 Mercúrio), Thaumium Claws (2 Espadas Táumicas), Awakened Blood (Melancia Reluzente, Maçã
  Dourada, 2 Seringas), Diamondflesh (Couro + 2 Diamantes), Enderheart (Pérola + 2 Ovos), Shock
  Skin (2 Quartzos, Estilhaço de Ar, Redstone), Instilled Loyalty (Sela, Cérebro de Zumbi, Maçã
  Dourada), Runic Hide (Diamante, Âmbar, Tecido Encantado, Nitor, Ferramenta de Escrita),
  Eldritch Fangs (2 Metal do Vazio, 2 Vishroom), Portability (Jarro + Pérola do Fim).
* **Infusões de espécie:** Porco → Ore Boar (Fornalha + Estilhaços de Fogo/Entropia/Terra) ou
  Taintfeeder (Flor de Etérea, 2 Toras de Silverwood, Void na Jarra); Galinha → Scholar's
  Chicken (Bolsa de Tinta + Tesoura; ovo `inkEgg`) ou Golden Chicken (2 Maçãs Douradas +
  Construto + Crisol; ovo `goldEgg` → pepita de ouro); Vaca → Coguvaca (cogumelos) ou Chocolate
  Cow (2 Cacau + 2 Açúcar; `bucketChocolate`); Ovelha → Chromatic (3 corantes), Self-Shearing
  (Tesoura + Comparador), Sheeder (teias + olhos de aranha; ou aranha + cérebro + 3 lãs);
  Gato → Guardian Panther (Força II + Sela), Gravekeeper (Osso, Bloco de Ouro, Âmbar, Tora de
  Silverwood), Familiar (2 Estilhaços Equilibrados, Lã Preta, Pepita de Ouro); Lobo → Seawolf
  (2 Peixes, Estilhaço de Água, Respiração Aquática longa), Lunar Wolf (2 Salis + Vidro
  Espelhado), Nether Hound (Balde de Lava, 2 Varas de Blaze, Estilhaço de Fogo); Cavalo →
  Endersteed (2 Pérolas, Ovo, Salis), Zumbi (4 Carnes Podres), Esqueleto (4 Ossos);
  Endersteed → **Nightmare** (Estrela do Nether, 2 Obsidianas, 2 Estilhaços de Fogo, Levitador
  Arcano; inst. 8). Não há receita de "Thunderhorse" nem da "Ethereal Cow" na lista de
  infusões desta versão, embora existam entidades/texto — o capítulo não as pede.
* **Peneira de Almas:** recebe Areia das Almas, solta areia comum embaixo; precisa de algo em
  cima (Cérebro na Jarra = XP, Inspiratron, Soulforge); Aer centivis acelera.
* **Reencarnação:** efígie no tonel + 8 Sano + 8 Victus + clique com a alma na jarra = aldeão.
* **Fenda planar:** 4 Transduction Amplifiers num nodo energizado + 6 Vortex Attenuators
  apontados. Com a fenda estável, itens jogados "mergulham no vazio"; a varinha os recupera.
* **Plano de Bolso:** Pérola Primordial na fenda; raio 32–128 m conforme a vis do nodo;
  aspectos do nodo definem conteúdo. Dimensão **69** (`pocket_plane_dim=69`,
  `enablePocketPlane=true`). A fenda usada não serve mais para criação nem para outro plano.
* **Planar Gateway** (`"PlanarGateway"`): moldura 5×5 com 4 Construtos Alquímicos Avançados,
  11 Pedras Antigas e o Keystone Receptacle; 250 vis de cada primal para formar; 100 vis de cada
  primal no receptáculo para abrir. Até 4 portais por plano; proibido em planos e nas Terras
  Exteriores. Sem GregTech no pack, valem as receitas "normais" de Keystone e Receptáculo.

## Dimensões, biomas, estruturas, chefes e mobs

* **Pocket Plane (dim 69)** — `WorldProviderPocketPlane`, um por criação.
* Entidades do mod (`reg.py entity ThaumicHorizons`): todas as criaturas infundidas, Lost Soul,
  slimes (Meat/Mercurial/Volt/Med), Voidling Golem (`GolemTH`). Nenhum chefe.

## Integrações no pack

* Depende das pesquisas do Thaumcraft: INFUSION, GOGGLES, JARBRAIN, ENCHFABRIC, VOIDMETAL,
  VISPOWER, PRIMPEARL, FOCUSFIRE/FOCUSEXCAVATION, LIQUIDDEATH, RUNICAUGMENTATION, LEVITATOR,
  GOLEMTHAUMIUM/ADVANCEDGOLEM, BONEBOW.
* `alternateGolemBell=true`: receita alternativa do Sino do Golemante (4 Quartzos + Graveto).

## Armadilhas e dicas

* **Warp das pesquisas:** Blood Alchemy 1, Soul Sieve 3, Inspiratron 2, Soulforge 2,
  Reincarnation 3, Self-Reincarnation 5, New Horizons 3, Voidling Golems 1, Pocket Plane 4,
  Planar Gateways 5.
* Matriz Rúnica Modificada tem instabilidade 10; autoinfusão Warped Tumor 12.
* Quebrar um tonel com criatura dentro mata a criatura e espirra fluxo.
* Alquimita não deve explodir perto de nodos. Mais de 1 Amplificador num nodo comum: 2 = raios,
  3 = fluxo, 4 = fenda que devora tudo (sem os 6 atenuadores).
* Warped Tumor guarda até **50** de Warp (`warpedTumorValue=50`).
* Morphic Fingers = tecla M; Spider Climb = C; Chameleon Skin = X; lentes = V.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `thorizons:tonel_curativo` | milestone | Tonel Curativo montado — início da biotaumaturgia (Ato II/III). |
| `thorizons:pesadelo` | milestone | Nightmare: infusão de criatura máxima, viagem Mundo Normal ↔ Nether. |
| `thorizons:reencarnacao` | milestone | Primeira reencarnação (alma + corpo no tonel). |
| `thorizons:fenda_planar` | milestone | Fenda planar estabilizada (4 amplificadores + 6 atenuadores). |
| `thorizons:plano_bolso` | portal | Entrada no Plano de Bolso (dim 69). |
| `thorizons:portal_planar` | milestone | Planar Gateway permanente. |
| `thorizons:autoinfusao` | milestone | Autoinfusão: o próprio corpo infundido. |
| `thorizons:mestre_horizontes` | milestone | **Marco final** do capítulo — candidato ao **Selo Arcano**. |
