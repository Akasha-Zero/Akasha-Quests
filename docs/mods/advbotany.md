# Advanced Botany — estudo do capítulo `advbotany`

## Visão geral

* **Mod:** Advanced Botany `1.3.1.7` (`Advanced Botany 1.3.1.7.jar`, modid/namespace `AdvancedBotany`,
  autores Pulxes e Liapel; depende do Botania). Assets em `assets/ab/`.
* **Capítulo:** `quests/chapters/magia/advbotany.yaml`, `order: 531`, **48 quests** (meta ≈ 45).
* **Papel na campanha:** endgame botânico (Ato IV). Quase tudo exige Espíritos de Gaia, então a
  quest-raiz real (Ancient Alphirine) depende de `botania:gaia1` (link visível no mapa). Uma quest
  `info` sem dependências abre o capítulo; quatro itens avulsos (Terrahoe, Black Hole Box, Sphere of
  Attraction, Playing Board) e o caixote do Thaumcraft só pedem materiais élficos.
* **Sem tradução pt_BR:** o JAR só traz `en_US.lang` e `ru_RU.lang`; o jogador vê os nomes em
  inglês, e as quests usam esses nomes.

Fontes: registro do pack (`reg.py mod AdvancedBotany`, receitas de bancada do dump), bytecode via
`javap` (`ab.common.lib.register.RecipeListAB` — receitas da Alphirine, da Forja e do Boticário;
`SubTileAncientAlphirine`, `SubTileDictarius`, `SubTilePureGladiolus`, `SubTileAspecolus`,
`SubTileArdentAzarcissus`, `TileNidavellirForge`, `TileLebethronCore`), `assets/ab/lang/en_US.lang`
(entradas da Lexica, categoria "Connecting with the past", conhecimento `ab_forgotten`) e
`config/AdvancedBotany.cfg`. NEI do pack não importado ainda (o mod tem handlers próprios
"Ancient Alphirine" e "Forge of Nidavellir").

## Energia / recursos-base

* Mana do Botania. Capacidades pela config: Mana Container 64.000.000, Diluted 8.000.000, Fabulous
  64.000.000; Natural Mana Spreader 128.000 de buffer e rajada de 32.000.
* Ancient Alphirine: até 180.000 de Mana; **4.500 por tentativa** (falha custa 450).

## Progressão (ids reais)

1. **Ancient Alphirine** (`Botania:specialFlower {type:"ancientAlphirine"}`), Boticário: 4 Espíritos de
   Gaia, Runa de Mana, 4 runas de estação, pétala laranja e amarela.
2. Transmutações da Alphirine (item jogado perto; cada tentativa consome 1 unidade; sucesso se
   `nextInt(111) <= chance`):
   * Garrafa de Ar do Fim → Air of Forgotten Lands `itemABResource:3` (chance 75 ≈ 68%)
   * Flor Mística (qualquer) → Nature's Gift `itemABResource:4` (32 ≈ 30%)
   * Funil → Engineer Hopper `blockEngineerHopper` (23 ≈ 22%)
   * Madeira dos Sonhos → Lebethron Wood `blockLebethron` (15 ≈ 14%)
   * Fagulha → Superconductive Spark `itemAdvancedSpark` (11 ≈ 11%)
   * Playing Board → Fate Playing Board `blockBoardFate:1` (60 ≈ 55%)
   * Lexica jogada perto → desbloqueia o conhecimento "Esquecido".
3. **Forge of Nidavellir** `blockABPlate` (2 blocos de manasteel, Placa de Aglomeração, bigorna, 2 Air of
   Forgotten Lands, Runa do Ar). Aceita Mana por rajada e por fagulha. Receitas (3 itens jogados):
   * Mithrill `itemABResource:0` = Espírito de Gaia + Bloco de Manasteel + Pepita de Terrasteel —
     2.500.000 de Mana.
   * Terrasteel = Diamante de Mana + Manasteel + Pérola de Mana — 500.000.
   * Mana Nether Star `itemABResource:2` = Pó de Mana + Espírito de Gaia + Estrela do Nether — 250.000.
   * Nebula Fragment `itemABResource:5` = Pó de Fada + Bloco de Pedra Draconiana + Pepita de Mithrill —
     **25.000.000**. 1 fragmento ↔ 9 Pieces of Nebula `itemABResource:6`.
4. Mithrill → Mana Container `blockManaContainer` (4 mithrill, runa orgulho, runa ar, 2 poços, pilão
   de mana) / Diluted `:1` (pepitas, poços diluídos) / Fabulous `:2` (poços fabulosos); Mithrill
   Mana Ring; Material Destroyer; Blade of Sea; Mana Charger (pepitas, rocha-viva, 2 foles, pilão,
   runa de mana).
5. Lebethron → Natural Core `blockLebethron:4` (pilão da natureza + 4 lebethron + 4 runas de
   estação) → Natural Mana Spreader `blockABSpreader` (4 cores, 2 Mana Nether Stars, 2 pilões de mana,
   Dispersor de Gaia); Rod of Sprawl.
6. Nebulosa: Nebula Mana Ring (anel de mithrill + espíritos + pieces), Rod of Nebula, Blade of Space,
   Nebula Blaze, **Armadura de Nebulosa** (peças de Terrasteel + fragmentos + elementium + pó de fada;
   peitoral com Tiara de Flügel; botas com Cinta do Desbravador Planar; calças com Air of Forgotten
   Lands). Conjunto: -30% custo de Mana.
7. Flores: Dictarius (geradora), Ardent Azarcissus (geradora passiva com Playing Board), Pure
   Gladiolus e Aspecolus (funcionais ligadas ao Thaumcraft).
8. Relíquias do Panteão (via Fate Playing Board + Dados do Destino): Freyr Slingshot, Key to Hidden
   Wealth, Nimble Cube, Sphere of Navigation, Horn of Plenty (todas ativas na config).

## Materiais e geração de mundo

Nenhuma geração de mundo. Tudo vem de Botania + Alphirine + Forja.

## Máquinas, estruturas e multiblocos

* **Forge of Nidavellir:** bloco único (sem estrutura conferida no bytecode, ao contrário da Placa de
  Aglomeração do Botania); recebe fagulha (`canAttachSpark`).
* **Árvore de Lebethron:** multibloco da Lexica (`TileLebethronCore.makeMultiblockSet`): coluna de
  Lebethron Wood com o Natural Core no 2º bloco de baixo; recebe folhas por clique e gera folhagem
  infinita.
* **Engineer Hopper:** vínculo origem/destino com agachar + clique; não puxa itens de cima.
* **Mana Charger:** carrega até 4 itens de Mana; vínculo com Varinha a uma fonte. `Enable Mana
  Charger=true`.
* **Thaumim Crafty Crate:** Caixote Inteligente para receitas da Bancada Arcana (precisa
  Thaumonomicon + varinha com Vis). `Enable Thaumim Crafty Crate=true`.

## Mecânicas das flores (bytecode)

* **Dictarius** (geradora, 8.000 de buffer): a cada 200 ticks soma até 16 entidades vivas num 5×3×5:
  jogador +480, aldeão +80 (gera um valor aleatório até o total). Acima de 15 aldeões, cada aldeão
  extra tem 5% de chance de sumir. A cada 1.200 ticks, se houver mais Dictarius que
  `Max Dictarius Count = 64` num 9×9×9, ela vira Arbusto Seco.
* **Ardent Azarcissus** (geradora, 16.000): joga no Playing Board adjacente.
* **Pure Gladiolus** (10.000; 1.000 por uso): solta orbes de aspectos primais do Thaumcraft perto de
  jogadores.
* **Aspecolus** (15.000; 1.250 por uso): adiciona pontos de aspecto de pesquisa do Thaumcraft aos
  jogadores próximos.

## Integrações no pack

* Thaumcraft: Pure Gladiolus, Aspecolus, Thaumim Crafty Crate (+ placeholders/padrões com Pepita de
  Táumio), Nebula Helmet of Revealing.
* Botania: Lexica (categoria nova + entradas nas categorias do Botania), relíquias adicionadas à
  lista do Fate Playing Board.

## Armadilhas e dicas

* A Alphirine consome o item em toda tentativa, inclusive nas falhas — jogue um por vez se o item for
  caro (Fagulhas, Playing Board).
* 25 M de Mana por Nebula Fragment: planeje uma rede de Mana Containers com fagulhas antes.
* Mithrill é indestrutível — não há conserto a pagar.

## Marcos exportados

* `advbotany:mithril` (milestone) — primeiro Mithrill na Forja de Nidavellir.
* `advbotany:fragmento_nebulosa` (milestone) — Nebula Fragment (25 M de Mana).
* `advbotany:armadura_nebulosa` (milestone) — conjunto completo de Nebulosa; fim do capítulo.
