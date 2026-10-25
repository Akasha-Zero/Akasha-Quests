# Arquitetura e Decoração — estudo do capítulo `decoracao`

## Visão geral

* **Capítulo:** `quests/chapters/base/decoracao.yaml`, `order: 125`, título "Arquitetura e
  Decoração", 135 quests (18 main, 49 normal, 54 optional, 5 info, 9 milestone — 6 delas são
  desafios de construção com itens verificáveis).
* **Papel na campanha:** Ato I (construção), sem portões; o marco `arquiteto_registro` fecha o
  capítulo.
* **Mods (versão / namespace):** Chisel 2.17.32-GTNH (`chisel`), Carpenter's Blocks 3.7.3-GTNH
  (`CarpentersBlocks`), LittleTiles 1.6.42 (`littletiles`), BiblioCraft 1.11.7 (`BiblioCraft`),
  Ztones-Unofficial 2.3.3-GTNH (`Ztones`), Malisis' Doors 1.19.11-GTNH (`malisisdoors`),
  FloodLights 1.5.6 (`FloodLights`), Garden Stuff 1.7.0 (`GardenCore`, `GardenContainers`,
  `GardenStuff`, `GardenTrees`), Armourer's Workshop 0.48.5 (`armourersWorkshop`).
* **Aspect Recipe Index** (`aspectrecipeindex`): é um índice NEI de aspectos do Thaumcraft
  (itens-fantasma `aspectrecipeindex:aspect`), **não é decorativo** — fica fora do capítulo.

Fontes: registro e receitas do dump (script de checagem de receitas por mod), NEI (abas
"Chisel", "Bloomery Process"), configs (`chisel.cfg` worldgen, `FloodLights.cfg`,
`armourersWorkshop/common.cfg`, `GardenStuff/GardenTrees.cfg`), lang e bytecode
(`ModBlockRecipes` do Armourer's Workshop, lang do LittleTiles).

## Energia / recursos-base

* FloodLights usa **RF**: refletor elétrico 20 RF/t (cone ×2), fluorescente 2 RF/t, UV 80 RF/t
  (4 de dano/s), luz de cultivo 40 RF/t; refletor a carvão 300 s por carvão. Alcance 64 (reto) /
  32 (cone).
* Garden Stuff: ferro forjado no Forno de Florescimento (ferro ou minério de ferro + areia →
  `GardenStuff:wrought_iron_ingot`, subproduto carvão vegetal); cera (`GardenCore:wax`) da
  candelila na fornalha.

## Progressão (resumo)

* **Chisel:** `chisel:chisel` → mármore/calcário (worldgen: `marbleAmount=7`,
  `limestoneAmount=8`) → variações, pilares, cinzéis de diamante/obsidiana/estrela, auto-cinzel,
  vitrais, fábrica/técnico/laboratório/futura, pedra do vazio, lava/água, templo/tyrian, papel de
  parede, neonite, hempcrete → estrada, lanterna, nuvem na garrafa, bola de musgo, tapetes;
  milestone `mestre_cinzel`.
* **Carpenter's Blocks:** bloco → martelo → cinzel, rampas, escadas, porta/cama (itens), alçapão,
  portão, redstone disfarçada, cofre, bloco desmontável, tocha/vaso/ladrilho, porta de garagem.
* **LittleTiles:** martelinho, serra, chave, recipiente, tubo de cor, martelo de borracha;
  milestone conjunto `mestre_marceneiro`.
* **BiblioCraft:** estante, prateleira, porta-ferramentas, estante de poções, estojo, etiqueta,
  escrivaninha, mesa/assentos, suporte de armadura (`{id: "BiblioCraft:Armor Stand"}`), pedestal,
  lampiões/lamparinas, relógio, placa sofisticada, serra/folhas de encaixilhamento, baú
  encaixilhado, paineladora, pinturas, máquina de escrever, imprensa
  (`Typesetting Machine` + `Printing Press`), atlas, bússola de marcador, instrumentos, catálogo,
  miudezas, bancada sofisticada, livros especiais.
* **Ztones:** Ofanix → ladrilho (`Ztones:stoneTile`) → 8 ladrilhos + catalisador = família →
  variações com o Ofanix; aurora, lâmpadas planas, impulsionador, mini carvão, solo de jardim.
* **Malisis' Doors:** portas animadas (itens `item.door_*`), vidro deslizante, cela/laboratório/
  fábrica, shoji/saloon, cortinas, medieval/carruagem, quadro de desaparecimento, misturador de
  blocos, sensor de jogador, fábrica de portas, campo de força, alçapões enferrujados.
* **FloodLights:** filamento → lâmpada → refletor elétrico, fluorescente, carvão, UV, cultivo.
* **Garden Stuff:** composteira → composto → solo/pá/kit de teste; vasos grandes, floreiras,
  vasos de quartzo, mesa de cerâmica; forno de florescimento → ferro forjado → treliças, cercas,
  lanternas, correntes, velas/candelabros; postes e cercas de poste; mudas especiais, hera,
  pasta de musgo.
* **Armourer's Workshop:** cubo de equipamento → guia, modelos, mesa de aplicação de visuais,
  bibliotecas, ferramentas de pintura, manequim, holograma, sabão, guarda-roupa (info).
* **Desafios:** biblioteca, jardim secreto, palácio de mármore, fábrica, casa de chá, galeria de
  troféus → `arquiteto_registro`.

## Integrações no pack

* Blocos do Ztones e de vários mods aparecem no NEI "Chisel" (cinzeláveis).
* Postes de madeira do Garden Trees aceitam troncos de Thaumcraft, Twilight, Witchery, Et
  Futurum (mangue) etc.
* Casa de chá usa lanterna do Et Futurum/Chisel/BiblioCraft (`one_of`).

## Armadilhas e dicas

* **Armourer's Workshop:** o bloco **Armeiro** (`armourersWorkshop:block.armourerBrain`) **não tem
  receita** (nem no dump nem em `ModBlockRecipes`); o Boneco (doll) está oculto. As quests usam
  só o que é fabricável (mesa de aplicação, bibliotecas, manequim, ferramentas) e avisam.
* Pottery Pattern Stamps (`GardenContainers:pottery_pattern:*`) sem receita de bancada — a quest
  da mesa de cerâmica manda conferir no NEI.
* `littletiles:chisel` e `littletiles:recipe` sem receita — não pedidos.
* Carpenter's: rampas com meta ≠ 0 não têm receita (a forma muda ao colocar / com o martelo);
  as quests pedem `blockCarpentersSlope` meta 0.
* Malisis: os blocos `door_*` não têm receita; use os itens `item.door_*`.
* Descrições de mecânicas finas (modos do cinzel de diamante, ferramentas do LittleTiles,
  sobreposições do Carpenter's) foram mantidas genéricas e remetem às dicas dos itens.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `decoracao:mestre_cinzel` | milestone | Domínio do Chisel. |
| `decoracao:mestre_marceneiro` | milestone | Carpenter's Blocks + LittleTiles. |
| `decoracao:desafio_biblioteca` | milestone | Desafio de construção: biblioteca (BiblioCraft). |
| `decoracao:desafio_jardim` | milestone | Desafio: jardim (Garden Stuff). |
| `decoracao:desafio_palacio` | milestone | Desafio: palácio de mármore. |
| `decoracao:desafio_fabrica` | milestone | Desafio: fábrica (Chisel + FloodLights + Malisis). |
| `decoracao:desafio_casa_japonesa` | milestone | Desafio: casa de chá. |
| `decoracao:desafio_galeria` | milestone | Desafio: galeria de troféus. |
| `decoracao:arquiteto_registro` | milestone | **Marco final** do capítulo. |
