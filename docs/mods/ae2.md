# Applied Energistics 2 — estudo do capítulo `ae2`

## Visão geral

* **Mods:** Applied Energistics 2 `rv3-beta-1035-GTNH` (namespace `appliedenergistics2`),
  AE2 Fluid Crafting `1.5.104-gtnh` (`ae2fc`), NotEnoughEnergistics `1.7.39`
  (`neenergistics`). Thaumic Energistics fica no capítulo `thaumextras` (outro autor).
* **Capítulo:** `quests/chapters/tech/ae2.yaml`, `order: 370`, 188 quests.
* **Papel na campanha:** Ato II (quartzo, meteoritos, primeira rede) → Ato III (autocrafting,
  fluidos, sem fio) → Ato IV (ponte quântica, espacial, células extremas). O marco final é
  peça do **Selo da Engrenagem**.

Fontes: registro (`reg.py mod appliedenergistics2/ae2fc/neenergistics`), **catálogo NEI**
(receitas do AE2 são `opaque` no dump de bancada; todas conferidas por `reg.py neiout` /
handlers `Receita Exata`, `Inscritor`, `Rebolo`), `config/AppliedEnergistics2/*.cfg`,
`MeteoriteLootTable.json`, lang do `ae2fc` (tooltips do Level Maintainer, Dual Interface,
Fluid Packet etc.).

## Energia / recursos-base

* Energia **AE**. Entrada: Aceitador de Energia (`tile.BlockEnergyAcceptor`, RF/EU), Câmara de
  Vibração (combustível), Túneis P2P RF/EU. Reserva: Célula de Energia (200 000 AE, NBT do
  registro) e Célula Densa (1 600 000 AE).
* **Manivela de Madeira** aciona Rebolo e Carregador sem energia.
* **Canais ligados** (`networkfeatures.Channels=true`): cabos comuns 8, densos 32, cada face
  do Controlador 32; rede sem controlador = 8 dispositivos.

## Progressão (ids reais)

1. Certus `item.ItemMultiMaterial` (oredict `crystalCertusQuartz`), Certus Carregado `:1`
   (minério carregado ~8%, Carregador `tile.BlockCharger`), Fluix `:7` (carregado + redstone +
   quartzo do Nether na água → 2), Rebolo `tile.BlockGrinder` (+ engrenagem de madeira BC) +
   Manivela `tile.BlockCrank`, pós `:2/:3/:8`, Vidro de Quartzo `tile.BlockQuartzGlass`.
2. Meteoritos: Bússola `tile.BlockSkyCompass`, Pedra do Céu `tile.BlockSkyStone`, prensas
   `:13` cálculo, `:14` engenharia, `:15` lógica, `:19` silício (uma por baú de meteorito).
   Alternativa do pack: 8 Essências de Certus (Magical Crops) + ferro/diamante/ouro/silício.
3. Sementes `item.ItemCrystalSeed` (areia + pó) crescem na água → Cristais Puros `:10/:11/:12`.
4. Inscritor `tile.BlockInscriber`: circuitos `:16/:17/:18`, Silício Impresso `:20` (silício do
   EnderIO/Galacticraft — oredict `itemSilicon`; o AE2 deste pack não tem silício próprio),
   processadores `:22` lógico, `:23` cálculo, `:24` engenharia. Prensa + bloco de ferro = cópia.
5. Rede: Fibra `ItemMultiPart:140`, Cabos `:16` vidro, `:36` coberto, `:56` inteligente,
   `:76` denso, `:536` coberto denso; Controlador `tile.BlockController` (4 Bloco de Pedra do
   Céu + 4 Fluix Puro + Proc. Engenharia); Acionador `tile.BlockDrive`; Encaixe `:39`;
   componentes `:35/:36/:37/:38` (1k–64k) e avançados `:57–:60` (256k–16384k, encaixe `:61`);
   células `item.ItemBasicStorageCell.*`, `item.ItemAdvancedStorageCell.*`; Terminal
   `ItemMultiPart:380`, Fabricação `:360`, Padrões `:340`, Interface `:480`.
6. Automação: Importação `:240`, Exportação `:260`, Armazenamento `:220`, Interface
   `tile.BlockInterface`/`:440`, Emissor de Nível `:280`, Planos `:300/:301/:320`, Porta E/S
   `tile.BlockIOPort`, Condensador `tile.BlockCondenser` → Bola de Matéria `:6` /
   Singularidade `:47`; cartões `:25–:31,:53–:56,:63–:69`.
7. Autocrafting: Padrão em Branco `:52`, Padrão Codificado `item.ItemEncodedPattern`, Montador
   Molecular, CPU (Unidade `tile.BlockCraftingUnit`, Co-Proc `:1–:3` e
   `tile.BlockAdvancedCraftingUnit:*`, Armazenamento `tile.BlockCraftingStorage:*` e
   `tile.BlockAdvancedCraftingStorage:*`, Monitor), Pattern Interface (NEE).
8. Fluidos (ae2fc): Dual Interface `ae2fc:fluid_interface`, células `ae2fc:fluid_storage*`,
   `multi_fluid_storage*`, barramentos `part_fluid_*`, Decoder, Level Maintainer, Level
   Terminal, Auto Filler, buffers, Water Infinity cell, Walrus.
9. Sem fio: Receptor `:41`, Amplificador `:42`, Ponto de Acesso `tile.BlockWireless`,
   Terminal de Segurança `tile.BlockSecurity`, Terminal Sem Fio, terminais ae2fc e Universal.
10. P2P `ItemMultiPart:460` (ME) e sintonizados 461 redstone, 462 item, 463 fluido, 465 EU,
    466 RF, 467 luz, 468 OpenComputers, 471 interface, 472 som.
11. Quântico: 8 Anéis `tile.BlockQuantumRing` + Câmara `tile.BlockQuantumLinkChamber` +
    Singularidade Entrelaçada `:48`. Espacial: componentes `:32–:34`, células
    `item.ItemSpatialStorageCell.*`, Pilão, Porta Espacial (dimensão de armazenamento
    `storageProviderID=-11`).

## Materiais e geração de mundo

* Certus: `CertusQuartzWorldGen=true`, `quartzOresClusterAmount=15`, `quartzOresPerCluster=4`,
  `spawnChargedChance≈0.08`.
* Meteoritos: só dimensão 0 (`meteoriteDimensionWhitelist`), `meteoriteSpawnChance 0=0.3`,
  `minMeteoriteDistance 0=707`; loot (MeteoriteLootTable): 1 prensa por baú + pepitas.
  Há tabela para Marte (-29) com diamantes, mas a whitelist só inclui o mundo normal.

## Integrações no pack

* Silício do EnderIO/Galacticraft; Moinho SAG/Triturador do Mekanism fazem silício de areia.
* Magical Crops: essências fazem prensas, cristais puros, fluix e certus carregado.
* MFR: DSU e Tanques de Plástico via Barramentos de Armazenamento; OpenComputers: P2P OC.
* Blocos GTNH sem receita no pack (não usados): Advanced Inscriber, Crystal Growth Chamber,
  Storage Reshuffle, Wireless Connector/Hub, Super ME Replenisher, Pattern Repeater,
  Advanced Level Emitter, Wireless Kit, Network Visualiser, Colorizer.

## Armadilhas e dicas

* Duas estruturas de Controlador na mesma rede = conflito.
* Célula do Vazio sem partição destrói tudo.
* "Fluid Packet" no inventário = Interface comum no lugar da Dual Interface.
* Espacial leva o que estiver dentro da caixa — inclusive o jogador.
* Pt_BR do jogo: "Acionador ME" (Drive), "Compressa Inscitora de Silício", "Tùnel P2P".

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `ae2:quatro_prensas` | milestone | As quatro prensas dos meteoritos. |
| `ae2:tres_processadores` | milestone | Processadores Lógico, Cálculo e Engenharia. |
| `ae2:primeira_rede` | milestone | Controlador + Acionador + célula + Terminal (Ato II). |
| `ae2:armazem_digital` | milestone | Células até 64k e particionamento. |
| `ae2:logistica_digital` | milestone | Importação, exportação, armazenamento, interface. |
| `ae2:primeiro_autocraft` | milestone | Primeira fabricação automática. |
| `ae2:fabrica_digital` | milestone | Autocrafting sob demanda. |
| `ae2:rede_fluidos` | milestone | Fluidos na rede (ae2fc). |
| `ae2:alcance_infinito` | milestone | Terminal sem fio + P2P. |
| `ae2:ponte_quantica` | milestone | Ponte Quântica (Ato IV). |
| `ae2:espaco_guardado` | milestone | Armazenamento espacial. |
| `ae2:mestre_me` | milestone | **Marco final** — para o **Selo da Engrenagem**. |
