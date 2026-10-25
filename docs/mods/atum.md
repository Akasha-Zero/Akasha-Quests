# Atum — estudo do capítulo `atum`

## Visão geral

* **Mod:** Atum `Atum-1.7.10-0.6.77.jar` (Team Metallurgy; namespace `atum`, versão `0.6`).
* **Dimensão:** `17` (`config/atum.cfg`: `Atum Dimension ID=17`), bioma único `200` (Atum's
  Desert). Portal por Escaravelho habilitado (`Atum Portal=true`), névoa ativa (`Atum Fog=true`).
* **Capítulo:** `quests/chapters/aventura/atum.yaml`, `order: 220`, **89 quests**
  (15 main, 22 normal, 43 optional, 3 info, 1 portal, 1 boss, 3 milestone, 1 finale).
* **Papel na campanha:** Ato III, terceiro dos **Três Reinos Perdidos**: o Reino das Areias, onde
  os deuses guardam o Livro dos Mortos e o Faraó guarda a **Chave das Areias**
  (`atum:chave_areias`). As descrições citam o acordo Lich × Faraós (tempo × destino) e as
  Valquírias; o mapa mostra por `link:` as chaves do Crepúsculo e dos Céus.

Fontes: `reg.py mod atum` (o mod **tem pt_BR** — nomes das quests seguem `name_pt`, inclusive
os estranhos como "Báu do Atum", "Banddido Chefe", "Sujo Ídolo"), receitas de bancada e
fornalha do dump, `lang/en_US.lang` (efeitos dos artefatos), bytecode com `javap`
(`ItemScarab`/`BlockPortal.tryToCreatePortal`, drops das entidades, `EntityPharaoh`,
`TileEntityPharaohChest`, `ItemLoot` (limpeza na água), `AtumLoot`, `WorldGenOasis`/`Pyramid`,
`BlockSandLayered`), `config/atum.cfg`. NEI ainda não importado.

## Energia / recursos-base

Sem energia. Recursos-base: Calcário (Limestone), Areia Estranha, Palmeira, Linho e ouro.

## Progressão

1. **Escaravelho** (`atum:item.scarab`): 4 ouro em cruz + diamante no centro.
2. **Portal** (`BlockPortal.tryToCreatePortal`): piso 5x5 de Arenito (ou `atum:tile.largeBrick`),
   borda de 1 bloco em volta na camada de cima (vazio 3x3), cantos com +2 blocos de altura
   (49 blocos). Clicar com o Escaravelho na estrutura cria o portal no 3x3. Não precisa água.
3. **Calcário:** `atum:tile.stone` (Limestone) dropa `atum:tile.cobble` (Calcário Rachado) →
   ferramentas de Calcário (nível pedra), `furnaceIdle` (Fornalha de Calcário, 8 rachados),
   tijolos (`smallBrick`, `largeBrick:2`, `carvedBrick`), muros, lajes, escadas.
4. **Areia Estranha** (`atum:tile.sand`) → fornalha → `crystalGlass`; `framedGlass` (+4
   gravetos); vidros tingidos.
5. **Oásis** (`WorldGenOasis`): água, `fertileSoil`, `flax`, `papyrus`. Papiro (7) + 2 gravetos
   = `scroll`.
6. **Linho:** planta `atum:item.flax` (dropa `flaxSeeds`; cresce em Solo Fértil arado) → 3 =
   `atum:item.linen` (tecido); 1 flax = 3 linhas.
7. **Armaduras:** Andarilho (linen) → Deserto (peça do Andarilho + peça de ferro, sem forma);
   Múmia (`clothScrap`).
8. **Mobs e armas** → **Ruínas** (baús com relíquias) → **Pirâmide** → **Faraó** → artefatos.

## Materiais e geração de mundo

* Minérios no Limestone: carvão, ferro, ouro, redstone, lápis, diamante (`atum:tile.*Ore`).
  Ferro e ouro dropam o bloco; carvão/diamante/lápis dropam o item.
* `atum:tile.sandLayer` **não dropa nada** (bytecode), por isso nenhuma quest a pede.
* Palmeiras (`palmLog`, `palmLeaves` → `palmSapling`; `tile.date` nas folhas → `item.date`).
* Arbusto (`shrub`) dá gravetos; com tesoura, o próprio bloco.

## Máquinas, estruturas e multiblocos

* **Pirâmide** (`WorldGenPyramid`): paredes de `largeBrick` inquebrável, labirinto interno,
  armadilhas (`burningTrap`), Baú do Faraó no centro.
* **Baú do Faraó** (`pharaohChest`): ao abrir, invoca o **Faraó** (+ múmias); mensagem global
  "foi invocado por"; o baú só abre (`setOpenable`) após o Faraó morrer.
* **Báu do Atum** (`chestSpawner`, baú amaldiçoado): gera inimigos e não abre com inimigos
  por perto ("chat.atum.enemies").
* **Relíquias** (`atum:item.loot:<meta>`): ídolo (base 0), colar (32), anel (64), broche (96),
  cetro (128); +1 Sujo, +2 Prata, +4 Ouro, +6 Safira, +8 Rubi, +10 Esmeralda, +12 Diamante.
  Relíquia **suja jogada na água** vira uma qualidade aleatória (`ItemLoot.onEntityItemUpdate`).

## Dimensões, biomas, estruturas, chefes e mobs

| Entidade (`kill`) | pt_BR | Drop (bytecode) |
| --- | --- | --- |
| `atum.mummy` | Múmia | carne podre, `clothScrap` |
| `atum.desertGhost` | Fantasma do Deserto | `ectoplasm` (4 + poção = Garrafa de Experiência) |
| `atum.dustySkeleton` | Esqueleto Empoeirado | ossos |
| `atum.desertWolf` | Chacal | ossos, `wolfPelt` |
| `atum.stoneSoldier` | Soldado de Pedra | `stoneChunk` (4 = `crackedLargeBrick`), `stoneSoldierSword` |
| `atum.banditWarrior` | Bandido Guerreiro | `scimitar`, pepitas de ouro |
| `atum.banditArcher` | Bandido Arqueiro | `item.bow` (Arco Curto), pepitas, flechas |
| `atum.barbarian` | Bárbaro | `greatsword`, pepitas |
| `atum.banditWarlord` | Banddido Chefe | `scimitar`, armadura do Andarilho, pepitas |
| `atum.pharaoh` | Faraó | ouro + **um artefato aleatório** (`AtumLoot.getRandomArtifact`); empunha `item.scepter` |

**Artefatos** (30; efeitos do lang): armas — Soteks' Rage, Osiris' Will, Monthu's Strike,
Anhur's Might, Hedetet's Sting, Ptah's Destruction, Nu's Flux; arcos — Aten's Fury, Horus'
Soaring, Shu's Breath, Monthu's Blast, Hedetet's Venom, Nut's Call, Neith's Audacity;
ferramentas — Ptah's Decadence, Aker's Toil, Geb's Blessing; amuletos — Mafdet's Quickness,
Isis' Healing, Amunet's Homecoming, Anubis' Mercy; vara — Anuket's Bounty (peixes especiais
`atum:item.fish:0..3`); armaduras — Ra's Glory/Sekhmet's Wrath/Nut's Agility/Horus' Flight e
Mnevis' Horns/Isis' Embrace/Ma'at's Balance/Geb's Solidarity.

## Integrações no pack

* Tábuas de palmeira entram em receitas do AgriCraft (cercas, grades, canais).
* Oredict: Limestone `blockLimestone`, palmeira `logWood`/`plankWood`, vidros `blockGlass*`.
* Witchery assa mudas de palmeira (cinzas de madeira).
* Armas contra mortos-vivos (Castigo, Holy Sword do Aether) são a dica-ponte com o Aether.

## Armadilhas e dicas

* Névoa limita a visão; marque caminhos. Água só nos oásis.
* Armadilhas de fogo nas pirâmides; leve resistência ao fogo.
* Cada Faraó dá **um** artefato aleatório: completar os conjuntos exige várias pirâmides.
* "Linho" no pt_BR nomeia tanto a planta (`item.flax`) quanto o tecido (`item.linen`); as quests
  explicam a diferença.
* Não foi confirmado no código se os artefatos também aparecem em baús; o capítulo só afirma o
  drop do Faraó.

## Marcos exportados

| Quest | Tipo | O que significa |
| --- | --- | --- |
| `atum:portal_atum` | portal | Primeira entrada na dimensão 17. |
| `atum:habitante_areias` | milestone | Calcário, fornalha, tâmaras e linho. |
| `atum:guerreiro_deserto` | milestone | Mobs do deserto vencidos, armadura do Deserto. |
| `atum:farao` | boss | **Faraó derrotado** (marco narrativo do Ato III). |
| `atum:panteao` | milestone | Uma arma, um arco, uma ferramenta e um amuleto divinos. |
| `atum:chave_areias` | finale | **Chave das Areias** (deps: Faraó + Tesouro do Faraó). Usar no Selo das Areias. |
