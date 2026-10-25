# The Twilight Forest — estudo do capítulo `twilight`

## Visão geral

* **Mod:** The Twilight Forest `TwilightForest-2.7.39.jar` (modid/namespace `TwilightForest`,
  versão `2.7.39`, 1.7.10). É o fork mantido pela GTNH (o código tem um flag `Mods.GTNH`), com
  integração com Et Futurum Requiem e um backport da integração com Tinkers' Construct.
  A progressão é a mesma do TF 2.x clássico.
* **Dimensão:** `7` (`config/TwilightForest.cfg`: `dimensionID=7`). Portal habilitado para todos
  (`AdminOnlyPortals=false`, `DisablePortalCreation=false`), item do portal `diamond`, poça de até
  15x15 (`portalMaxSize=15`). Uncrafting Table ativa (`DisableUncrafting=false`).
* **Capítulo:** `quests/chapters/aventura/twilight.yaml`, `order: 200`, **158 quests**
  (32 main, 39 normal, 71 optional, 2 info, 1 portal, 8 boss, 4 milestone, 1 finale).
* **Papel na campanha:** Ato III, primeiro dos **Três Reinos Perdidos** (NARRATIVA §4): o Reino
  do Crepúsculo, dos Lordes Lich (arquivistas corrompidos pelas páginas do tempo). Termina na
  **Chave do Crepúsculo** (`twilight:chave_crepusculo`), que alimenta o Selo do Crepúsculo (Ato IV).
  As descrições citam o acordo Lich × Faraós e a vigilância das Valquírias; o mapa do capítulo
  mostra, por `link:`, as chaves dos outros dois reinos (`aether:chave_ceus`, `atum:chave_areias`).

Fontes: `reg.py mod TwilightForest` (427 itens), `reg.py entity Twilight`, receitas de bancada e
fornalha do dump, `assets/twilightforest/lang/en_US.lang` (conquistas `twilightProgress*` e os
livros `progressionBook.*`, que descrevem cada maldição), bytecode do JAR com `javap`
(`TFTreasure` e o mapeamento estrutura → tabela de saque; drops de cada chefe/mob;
`TFFeature`/`TFBiomeBase` para biomas), `config/TwilightForest.cfg`. NEI ainda não importado.
O mod **não tem pt_BR**: os nomes aparecem em inglês no jogo e foram mantidos assim nas quests.

## Energia / recursos-base

Não há energia. Os "tiers" são materiais:

| Material | Id | Como se obtém |
| --- | --- | --- |
| Liveroot | `TwilightForest:item.liveRoot` | bloco `tile.TFRoots:1` nas raízes subterrâneas |
| Raw Ironwood | `item.ironwoodRaw` | sem forma: Liveroot + Lingote de Ferro + Pepita de Ouro |
| Ironwood Ingot | `item.ironwoodIngot` (ore `ingotIronwood`) | fornalha: 1 Raw → **2** lingotes; ou Raw + `dustPyrotheum` |
| Steeleaf | `item.steeleafIngot` (ore `ingotSteeleaf`) | **só saque** (colinas, labirintos, etc.); não tem receita |
| Naga Scale | `item.nagaScale` | drop da Naga |
| Fiery Ingot | `item.fieryIngot` (ore `ingotFiery`) | sem forma: `fieryEssence` (Fiery Blood **ou** Fiery Tears) + Lingote de Ferro |
| Knightmetal | `item.knightMetal` (ore `ingotKnightmetal`) | 9 `item.armorShards` → `item.shardCluster` → fornalha; ou cluster + Pyrotheum |
| Carminite | `item.carminite` | 4 Borer Essence + 4 Redstone + 1 Lágrima de Ghast (centro) |
| Arctic Fur / Alpha Yeti Fur | `item.arcticFur` / `item.alphaFur` | Yetis e Winter Wolves / Yeti Alfa |

Todos têm bloco compacto (`tile.IronwoodBlock`, `SteeleafBlock`, `KnightmetalBlock`,
`FieryBlock`, `CarminiteBlock`, `ArcticFurBlock`), base de Beacon ("Usable for Beacon bases").

## Progressão

Ordem oficial das travas de bioma (conquistas `twilightProgress*` + livros de anotações):

1. **Portal** — poça de água cercada de grama/terra com plantas em cima; jogar um Diamante na
   água (raio).
2. **Naga** (`TwilightForest.Naga`) no Pátio da Naga → `item.nagaScale` + troféu `item.trophy:1`.
   Escama libera a **Torre do Lich**.
3. **Lich** (`TwilightForest.Twilight Lich`) no topo da torre → **um cetro aleatório** dos três
   (`scepterTwilight`, `scepterLifeDrain`, `scepterZombie`; bytecode `nextInt(3)`), ouro, pérolas,
   ossos, troféu `trophy:2`. Libera o **Pântano** (mosquitos/fome) e o Labirinto.
4. **Minoshroom** (`TwilightForest.Minoshroom`) no fundo do Labirinto → `meefStroganoff`,
   `minotaurAxe`, troféu `trophy:5`. Comer o Stroganoff libera o **Pântano de Fogo**.
5. **Hidra** (`TwilightForest.Hydra`) → `fieryBlood`, `hydraChop`, troféu `trophy:0`. Libera a
   **Floresta Sombria** (cegueira).
6. **Pedestal de Troféu** no Stronghold dos Cavaleiros (colocar um troféu abre o resto).
7. **Knight Phantoms** (`TwilightForest.Knight Phantom`, 6) → armas Knightly, `phantomHelm`,
   `phantomPlate`; o baú de chefe (`stronghold_boss`) tem o troféu `trophy:6`. Libera os
   mecanismos da **Torre Sombria**.
8. **Ur-Ghast** (`TwilightForest.Tower Boss`) → baú `darktower_boss` com `fieryTears`,
   `carminite` e troféu `trophy:3`. Libera a **Floresta de Neve** (nevasca).
9. **Yeti Alfa** (`TwilightForest.Yeti Boss`) → `alphaFur`, `iceBomb`, troféu `trophy:7`. Libera a
   **Geleira**.
10. **Rainha da Neve** (`TwilightForest.Snow Queen`) no Palácio da Aurora → `tripleBow` ou
    `seekerBow`, troféu `trophy:4`. Libera as **Terras Altas** (chuva ácida).
11. **Cavernas dos Trolls** → `lampOfCinders` (baú `troll_vault`) queima os espinhos das
    **Thornlands** → **Castelo Final** no platô (Highlands Center). Nesta versão o castelo
    não tem chefe (o próprio lang diz "What could even be in that castle?!?").

Troféus (`TwilightForest:item.trophy:N`): 0 Hydra, 1 Naga, 2 Lich, 3 Ur-ghast, 4 Snow Queen,
5 Minoshroom, 6 Knight Phantom, 7 Alpha Yeti, 8 Questing Ram.

## Materiais e geração de mundo

* Biomas (ids do registro, `config/TwilightForest.cfg`): 41 Twilight Lake, 42 Twilight Forest,
  43 Dense Twilight Forest, 44 Highlands, 45 Mushroom Forest, 46 Swamp, 52 Firefly Forest,
  53 Deep Mushroom Forest, 54 Dark Forest Center, 55 Highlands Center (Final Plateau),
  56 Dark Forest, 57 Enchanted Forest, 58 Fire Swamp, 59 Thornlands. Usados nas tarefas `visit`.
* **47 Stream, 48 Snowfield (Floresta de Neve), 49 Glacier, 50 Clearing, 51 Oak Savanna:
  CONFLITO DE ID** (ver Armadilhas). Nenhuma tarefa usa esses ids.
* Não há minérios próprios; as Colinas Ocas têm minérios vanilla.

## Máquinas, estruturas e multiblocos

* **Uncrafting Table** (`tile.TFUncraftingTable`): 8 bancadas + Maze Map Focus; desfaz receitas
  cobrando XP.
* **Mapas:** Magic Map Focus (pena de corvo + torchberries + pó de glowstone) → Blank Magic Map
  (8 papéis); Maze Map Focus → Blank Maze Map; Ore Map = mapa do labirinto + blocos de ouro,
  diamante e ferro.
* **Árvores mágicas** (`tile.TFSapling:5..8`, vindas do baú `tree_cache` nas Árvores Ocas
  gigantes): Tree of Time (`TFMagicLogSpecial:0`, acelera ticks; `minecraft:portal` excluído
  na config), Transformation (muda o bioma), Miner's (traz minérios), Sorting (organiza baús).
* **Charms:** Life I/II (4 de I = II), Keeping I/II/III (4 → próximo nível).
* **Cetros:** recarga na bancada (Twilight + Pérola do Fim; Life Drain + Olho de Aranha
  Fermentado; Zombie + Carne Podre + Poção de Força).
* Tabelas de saque por estrutura (bytecode): `hill1..3` Colinas Ocas, `hedgemaze`,
  `labyrinth_room/deadend/vault`, `tower_room/library` (Lich), `darktower_cache/key/boss`,
  `tree_cache` (Árvore Oca), `stronghold_cache/room/boss`, `aurora_cache/room`,
  `troll_garden/vault`, `basement` (poços/fundações).

## Dimensões, biomas, estruturas, chefes e mobs

Mobs usados em `kill` (ids reais com espaço, por isso na forma `{entity, count}`): Redcap,
Redcap Sapper, Twilight Wraith, Hedge Spider, Swarm Spider, Twilight Kobold, Skeleton Druid,
Hostile Wolf, Death Tome, Minotaur, Maze Slime, Mosquito Swarm, Fire/Slime/Pinch Beetle,
Mist Wolf, King Spider, Lower/Upper Goblin Knight, Block&Chain Goblin, Helmet Crab, Tower Golem
(Carminite Golem), Mini Ghast (Carminite Ghastling), Tower Ghast (Ghastguard), Yeti,
WinterWolf, SnowGuardian, Unstable Ice Core, Ice Crystal, Troll, Giant Miner, Armored Giant.
Chefes: Naga, Twilight Lich, Minoshroom, Hydra, Knight Phantom, Tower Boss, Yeti Boss, Snow Queen.
Nas quests de chefe a tarefa é **matar OU ter o troféu** (`task_logic: OR`), para funcionar em
grupo (só um jogador pega o troféu).

## Integrações no pack

* Tinkers' Construct: `EnableTiConstructIntegration=true` — Fiery Metal, Knightmetal, Naga Scale
  e Steeleaf são materiais de ferramenta; fluidos `fierymetal.molten`, `knightmetal.molten`,
  `fiery_essence`; manual `TwilightForest:manualBook` (Padrão em Branco + Papel + Magic Map Focus).
* Thermal: receitas de Ironwood/Knightmetal com `dustPyrotheum`.
* Et Futurum Requiem: Shulkers nas torres do Lich e do Ur-Ghast (config).
* MFR Compat TwilightForest presente; Thaumcraft com aspectos (classe de integração).

## Armadilhas e dicas

* **CRÍTICO — conflito de biome ID com ChromatiCraft.** `config/Reika/ChromatiCraft.cfg` usa
  47 (Ender Forest), 48 (Rainbow Forest), 49 (Luminous Cliffs), 50 (Luminous Cliffs Shores) e
  51 (Rainbow Stream), os mesmos ids do TF para Stream, **Snowfield**, **Glacier**, Clearing e
  Oak Savanna. O dump do registro mostra as classes do ChromatiCraft nesses ids (o TF só avisa
  "Biome ID conflict" e segue). O `TFWorldChunkManager` resolve o bioma por id e o `TFFeature`
  compara o bioma por objeto (`TFBiomeBase.tfSnow` / `glacier`): **com o conflito, a Floresta de
  Neve e a Geleira viram biomas do ChromatiCraft e o covil do Yeti Alfa e o Palácio da Aurora
  não devem gerar**. Isso bloqueia Yeti Alfa, Rainha da Neve, Terras Altas e a Chave do
  Crepúsculo. **Correção sugerida:** mudar os ids de bioma do ChromatiCraft (ou do TF) para ids
  livres antes de gerar o mundo; pelo dump, 61–99 e 113–125 estão livres.
* Chefes dão um item só; em grupo, combinem quem fica com o troféu.
* Cada Lich dá um cetro aleatório: a "Maestria dos Cetros" pede várias torres.
* O pedestal precisa de um troféu de chefe; a quest pede ter um troféu + marcar.
* Hedges machucam ao tocar; Fire Jets explodem em chamas; cofre do labirinto tem armadilhas.
* Steeleaf não tem receita (só saque).

## Marcos exportados

| Quest | Tipo | O que significa |
| --- | --- | --- |
| `twilight:portal_crepusculo` | portal | Primeira entrada na dimensão 7. |
| `twilight:primeiros_passos_crepusculo` | milestone | Mapa mágico + armadura de Ironwood: pronto para a Naga. |
| `twilight:naga` | boss | Naga derrotada (escama libera a Torre do Lich). |
| `twilight:lich` | boss | **Lich derrotado** — o arquivista corrompido (marco narrativo do Ato III). |
| `twilight:minoshroom` | boss | Labirinto concluído. |
| `twilight:hidra` | boss | Hidra derrotada. |
| `twilight:cavaleiros_fantasmas` | boss | Stronghold dos Cavaleiros concluído. |
| `twilight:ur_ghast` | boss | Ur-Ghast derrotado (Torre Sombria). |
| `twilight:fortaleza_crepusculo` | milestone | Domínio dos metais do reino (Ironwood, Fiery, Knightmetal). |
| `twilight:yeti_alfa` | boss | Yeti Alfa derrotado (depende do conflito de bioma estar corrigido). |
| `twilight:rainha_neve` | boss | Rainha da Neve derrotada (idem). |
| `twilight:senhor_terras_altas` | milestone | Reino atravessado até o Castelo Final. |
| `twilight:sala_trofeus` | milestone | Os 8 troféus de chefe. |
| `twilight:chave_crepusculo` | finale | **Chave do Crepúsculo** (deps: Lich + Ur-Ghast + Rainha da Neve). Usar no Selo do Crepúsculo. |
