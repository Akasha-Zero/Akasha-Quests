# Thaumcraft 4.2.3.5 — estudo dos capítulos `thaumcraft` e `thaumcraft2`

## Visão geral

* **Mod:** `Thaumcraft-1.7.10-4.2.3.5.jar` (modid `Thaumcraft`), com Thaumcraft4Tweaks 1.5.39 (`tc4tweak`),
  Salis Arcana 1.1.67-GTNH, CarefulCastCorrector, thaumcraftneiplugin, TC Node Tracker 1.4.6,
  ThaumcraftMobAspects 1.2.1-GTNH e WarpTheory 1.5.7-GTNH (eventos de Warp extras).
* **Capítulos:**
  * `quests/chapters/magia/thaumcraft.yaml` — "Thaumcraft I — O Aprendiz", `order: 500`, 166 quests.
  * `quests/chapters/magia/thaumcraft2.yaml` — "Thaumcraft II — O Taumaturgo", `order: 501`, 125 quests
    (raiz com `deps_hidden: [thaumcraft:infusao]`).
* **Papel na campanha:** Cap. I = Ato I/II (primeira magia → Infusão). Cap. II = Ato II→IV (infusão
  avançada, Warp, Mácula, Eldritch). O marco final `thaumcraft2:taumaturgo_supremo` alimenta o **Selo Arcano**.

Fontes: registro do pack (`reg.py mod Thaumcraft`, `reg.py research`), catálogo NEI (só confirma
*saídas* das receitas de Arcano/Alquimia/Infusão — as entradas vêm vazias no dump), **decompilação com CFR
do próprio JAR** (`ConfigRecipes`, `ConfigResearch`, `ConfigItems`, `WarpEvents`, `WandManager`,
`TileInfusionMatrix`, `TileEldritchLock`, entidades/chefes, aldeões) para custos, aspectos, Warp e drops,
`assets/thaumcraft/lang/en_US.lang` (páginas do Taumonomicon) e `config/Thaumcraft.cfg`, `config/salisarcana/*`,
`config/tc4tweak.cfg`, `config/WarpTheory.cfg`. O pack não tem scripts CraftTweaker.

## Energia / recursos-base

* **Vis** (na varinha, por primal: Aer, Terra, Ignis, Aqua, Ordo, Perditio), **Essência** (em frascos,
  jarros, tubos) e **CV** (centi-vis) de nós energizados via relés.
* Capacidade das hastes (ConfigItems): madeira 25, greatwood 50, primais (obsidiana/gelo/quartzo/cana/
  blaze/osso) 75, silverwood 100; cajados greatwood 125, primais 175, silverwood 250, primal 250.
* Fator das pontas: ferro 1,1; cobre 1,1 (1,0 em Ordo/Perditio); ouro 1,0; prata 1,0 (0,95 nos
  elementais); táumio 0,9; vazio 0,8.
* Custo de montar varinha = custo da ponta × custo da haste (ferro 1, ouro 3, táumio 6, vazio 9;
  madeira 1, greatwood 3, primais 6, silverwood 9). Cetro = ×1,5 e leva Primal Charm.
* Estruturas por varinha (antes do desconto): Altar de Infusão 25 de cada primal; Fornalha Infernal
  50 Ignis + 50 Terra; Nó na Jarra 70 de cada; Taumatório 15 Ignis/30 Ordo/30 Aqua; Fornalha Alquímica
  Avançada 50 Ignis/Aqua/Ordo; Oculus 100 de cada primal.

## Progressão (ids reais)

1. Pontas de ferro `Thaumcraft:WandCap` → varinha básica `Thaumcraft:WandCasting` (bancada) →
   Taumonomicon (varinha na estante) `ItemThaumonomicon`.
2. Estilhaços `ItemShard:0-5` → Taumômetro (bancada) → Ferramenta de Escrita `ItemInkwell` → Mesa
   `blockTable` (2 lado a lado + escrita = Mesa de Pesquisa) → Notas `ItemResearchNotes`.
3. Mesa Arcana `blockTable:15` → Pontas de Ouro `WandCap:1`, Haste de Greatwood `WandRod` → varinha
   NBT `{rod:"greatwood",cap:"gold"}`; focos Fogo/Gelo/Choque/Escavação/Troca.
4. Crisol `blockMetalDevice` → Estilhaço Equilibrado `ItemShard:6` → Salis Mundus `ItemResource:14`
   (fornalha); Nitor `ItemResource:1` (glowstone + 3 Potentia/Ignis/Lux); Alumentum `ItemResource`
   (carvão + 3 Potentia/Ignis/Perditio); **Táumio** `ItemResource:2` (lingote de ferro + 4 Praecantatio).
5. Destilação: Fornalha Alquímica `blockStoneDevice`, Filtro `ItemResource:8`, Alambique
   `blockMetalDevice:1`, Jarro `blockJar`, tubos `blockTube:*`, Taumatório (Crisol + 2 Construtos
   `blockMetalDevice:9`).
6. Infusão: Pedra Arcana `blockCosmeticSolid:6/7`, Matriz Rúnica `blockStoneDevice:2` (40 Ordo),
   Pedestais `blockStoneDevice:1`. Haste de Silverwood `WandRod:2`, Pontas de Táumio `WandCap:6`→`WandCap:2`
   → varinha `{rod:"silverwood",cap:"thaumium"}` (54 por primal: precisa de desconto ou cajado).
7. Golemancia: Baú Faminto → Golem de Palha (feno + 4 Humanus/Motus/Spiritus) → Sino, núcleos
   (`ItemGolemCore`: 0 Preencher, 1 Esvaziar, 2 Coletar, 3 Colher, 4 Proteger, 5 Decantar, 6 Alquimia,
   7 Lenhador, 8 Usar, 9 Açougueiro, 10 Ordenar, 11 Pescar, 100 em branco), golens `ItemGolemPlacer:0-7`,
   melhorias `ItemGolemUpgrade:0-5`, Golem Avançado (NBT `{advanced:1b}`).
8. Cap. II: Primal Charm `ItemResource:15`, cajados `WandRod:50/52`, Pedestal de Recarga, proteção
   rúnica, Infusão de Encantamento, Fortaleza (`{goggles:1b}`, `{mask:0..2}`), Catalisador
   Taumostático, nós energizados (Transdutor `blockStoneDevice:11`, relés `blockMetalDevice:14/2`,
   Manipulador Focal `blockStoneDevice:13`), Warp, Mácula, Culto Carmesim, Metal do Vazio `ItemResource:16`,
   Olho Eldritch, Terras Exteriores, Pérola Primordial `ItemEldritchObject:3`, Núcleo de Cajado Primal `WandRod:100`.

## Materiais e geração de mundo (config/Thaumcraft.cfg)

Todos ativos: cinábrio (fundo), âmbar (perto da superfície), pedras infundidas, nós (1 a cada ~36
chunks; especiais 1 em 18), estruturas, Greatwood/Silverwood, mácula (`generate_taint=true`).
Biomas: Mácula 192, Floresta Mágica 193, Eerie 194, Eldritch 195. Mácula se espalha (1/200) e pode
nascer de fluxo (`biome_taint_from_flux=true`). `hard_mode_nodes=true`. `wuss_mode=false` (Warp ativo).
`research_difficulty=0` (normal). Campeões ativos.

## Máquinas, estruturas e multiblocos

* **Altar de Infusão:** pedestal central; 4 pilares diagonais (tijolo de pedra arcana embaixo, bloco em
  cima); matriz 2 acima do pedestal; varinha na matriz. Pedestais de ingredientes até 8 blocos; essência
  até 12 blocos. Estabilizadores: crânios, velas e cachos de cristal simétricos.
* **Fornalha Infernal:** 3×3×3 — 12 tijolos do Nether, 12 obsidianas, lava no centro, grade de ferro na
  frente, centro de baixo vazio.
* **Nó na Jarra:** cubo de vidro 3×4×3 com lajes de madeira no topo em volta do nó.
* **Oculus:** altar Eldritch com nó Sinistro intacto + 4 Olhos Eldritch + 100 vis de cada primal.
* **Nó energizado:** Estabilizador embaixo + Transdutor em cima com redstone; relés até 8 blocos.

## Dimensões, estruturas, chefes e mobs

* **Terras Exteriores** (dim −42): labirinto; salas-chave com Tábua Rúnica `ItemEldritchObject:2`; a
  fechadura gera um destes: Warden Eldritch (200 HP), Construto Eldritch (250 HP), Portal Carmesim
  (500 HP, despeja cultistas e o Pretor) ou Taintáculos Gigantes (o último deixa a pérola). Warden/
  Construto/Portal deixam Pérola Primordial (+ Tesouro Raro).
* Cultistas: drops de Fragmento de Conhecimento, Semente do Vazio, Moedas de Ouro; drop raro **Ritos
  Carmesins** (`ItemEldritchObject:1`, ensina CRIMSON, 3 Warp). Guardião Eldritch: drop raro Olho Eldritch.
* Aldeões: Mago (torre; vende Anel de Proteção e Pedra de Vis, entre outros) e Banqueiro (moedas).

## Warp (WarpEvents decompilado)

* Pesquisa com Warp dá metade permanente (arredondado para cima) e metade "pegajosa".
* Warp (perm+pegajoso) > 10 → pista dos Sais de Banho; > 25 → Epifania Eldritch (ELDRITCHMINOR);
  > 50 → Revelação Eldritch (ELDRITCHMAJOR). Só acontecem durante um evento de Warp.
* Sais de Banho (Salis + 6 Cognitio/Auram/Ordo/Sano) → Fluido Purificador (Warp Ward). Sabão
  Sanitizante (Bloco de Sebo + 16 Cognitio/Alienis/Ordo/Sano): remove todo temporário e tem chance de
  reduzir o normal. Máscara do Diabo Sorridente reduz a gravidade.

## Integrações no pack

* Salis Arcana: substituição de pontas/núcleos, Focal Disenchanting, varredura de baús pelo
  Taumômetro, fontes de calor por oredict, pedestal aceita CV, aviso de pesquisa faltando.
* TC4 Tweaks: busca/rolagem no livro. Node Tracker: lista de nós examinados.
* Avaritia adiciona na aba Eldritch: Extremely Primordial Pearl (`Avaritia:big_pearl`) e **Akashic
  Records** (`Avaritia:Akashic_Record`) — quests secretas no Cap. II.

## Armadilhas e dicas

* Pontas de ferro +10% de custo: com varinha de madeira o altar (25) não monta — use Greatwood+ouro.
* A varinha Silverwood+Táumio custa 54 por primal: só com desconto (pontas de táumio numa greatwood,
  substituição do Salis Arcana) ou cajado.
* Tradução pt_BR troca nomes de núcleos de golem (Coletar/Preencher) e escreve "Estilhado de Água".
* Gosma/vinha maculada apodrecem no inventário. Morte Líquida mata qualquer coisa.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `thaumcraft:primeira_descoberta` | milestone | Livro, Taumômetro e primeira pesquisa (Ato I). |
| `thaumcraft:laboratorio_alquimico` | milestone | Crisol, Táumio, Nitor, Salis Mundus. |
| `thaumcraft:essencia_destilada` | milestone | Destilação e jarros. |
| `thaumcraft:infusao` | milestone | **Primeira infusão** — pré-requisito de todos os addons. |
| `thaumcraft:operarios` | milestone | Golemancia básica. |
| `thaumcraft:varinha_silverwood` | milestone | Varinha Silverwood+Táumio. |
| `thaumcraft:aprendiz_completo` | milestone | Fim do Cap. I (entrada do Cap. II). |
| `thaumcraft2:vis_em_rede` | milestone | Nó energizado, relés e Manipulador Focal. |
| `thaumcraft2:fabrica_golens` | milestone | Golemancia avançada. |
| `thaumcraft2:terras_exteriores` | portal | Entrada nas Terras Exteriores (dim −42). |
| `thaumcraft2:warden_eldritch` | boss | Warden Eldritch. |
| `thaumcraft2:construto_eldritch` | boss | Construto Eldritch. |
| `thaumcraft2:portal_carmesim` | boss | Portal Carmesim. |
| `thaumcraft2:taintaculo_gigante` | boss | Taintáculos Gigantes. |
| `thaumcraft2:perola_primordial` | milestone | Primeira Pérola Primordial. |
| `thaumcraft2:taumaturgo_supremo` | milestone | **Marco final Eldritch → Selo Arcano** (pérola + Núcleo de Cajado Primal). |
