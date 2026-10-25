# BuildCraft — estudo do capítulo `buildcraft`

## Visão geral

* **Mod:** BuildCraft `7.1.63` (`buildcraft-7.1.63.jar`; módulos/namespaces `BuildCraft|Core`,
  `|Energy`, `|Factory`, `|Transport`, `|Silicon`, `|Builders`, `|Robotics`) + BuildCraft Compat
  `7.1.22` (`BuildCraft|Compat`: Tubo do Apicultor do Forestry).
* **Capítulo:** `quests/chapters/tech/buildcraft.yaml`, `order: 310`, 106 quests.
* **Papel:** Ato I–II. Engrenagens → canos → motores → mineração (Pedreira) → petróleo/Refinaria →
  lasers/chipsets → portas lógicas → construtores → robôs.

Fontes: registro (`reg.py mod "BuildCraft|*"`), receitas de bancada do dump, catálogo NEI (Mesa de
Montagem: 61 receitas; Mesa de Integração; Refinaria), `config/buildcraft/{main,objects,compat}.cfg`,
lang do JAR (`assets/buildcraft/lang/en_US.lang`, dicas `tip.*`) e bytecode dos motores.

## Energia / recursos-base

* **BuildCraft 7 usa RF** (MJ foi aposentado). Tubos Cinéticos = tubo de itens + redstone (bancada).
* Motores: Redstone (`engineBlock:0`, só aciona canos — `canConnectEnergy=false`, buffer 1 000),
  Stirling (`:1`, pt_BR "Motor a Vapor", combustível de fornalha, buffer 10 000), Combustão (`:2`,
  buffer 100 000; config: petróleo 30 RF/t, combustível 60 RF/t; precisa de refrigeração).
  `canEnginesExplode=false` no pack (superaquecidos param).
* Tubo Cinético de Madeira/Esmeralda = entrada de energia; Ferro = limitador; Arenito só liga em tubos.

## Progressão (ids reais)

1. Engrenagens `BuildCraft|Core:woodenGearItem → stoneGearItem → ironGearItem → goldGearItem →
   diamondGearItem` (cada uma leva a anterior no centro). Chave Inglesa `wrenchItem`.
2. Canos `BuildCraft|Transport:item.buildcraftPipe.pipeitems<material>` (material + vidro + material
   = 8): wood, cobblestone, stone, quartz, gold, iron, diamond, obsidian, emerald, sandstone, void,
   clay, lapis, daizuli, emzuli, stripes; estrutura `pipestructurecobblestone` (pedregulho + cascalho),
   Tampa `pipePlug`. Fluidos: tubo + Vedação `pipeWaterproof` (corante verde ou cera). Cinéticos:
   tubo + redstone.
3. Fábrica: `BuildCraft|Factory:tankBlock` (16 baldes, empilha), `pumpBlock`, `floodGateBlock`,
   `autoWorkbenchBlock`, `blockHopper`, `miningWellBlock`, `refineryBlock`.
4. Mineração: `BuildCraft|Core:markerBlock` (alcance 64, `markerRange=64`), Pedreira
   `BuildCraft|Builders:machineBlock` (`doChunkLoading=false`, `oneTimeUse=false`).
5. Petróleo: `BuildCraft|Energy:bucketOil` (oredict `container_oil`), Refinaria → `bucketFuel`.
6. Silício: Laser `BuildCraft|Silicon:laserBlock`, mesas `laserTableBlock:0–5` (Montagem, Trabalho
   Avançado, Integração, Carga, Programação, Estampagem), Empacotador `packagerBlock`.
   Mesa de Montagem (NEI): chipsets `redstoneChipset:0–7` (redstone; +ferro; +ouro; +diamante;
   +pérola = 2 pulsantes; +quartzo; +comparador; +esmeralda), Cristal de Redstone (bloco de redstone),
   fios `pipeWire:0–3` (corante + redstone + ferro = 8), lentes/filtros `pipeLens`.
7. Portas `BuildCraft|Transport:pipeGate` (NBT `mat` 0 básica, 1 ferro, 2 ouro, 3 diamante,
   4 esmeralda, 5 quartzo; `logic` 0 E / 1 OU): básica = chipset de redstone + fio vermelho; ferro =
   chipset de ferro + vermelho/azul; ouro e quartzo = + verde; diamante e esmeralda = 4 fios.
   Expansões na Mesa de Integração com chipset pulsante/quartzo/comparador. Copiadora `gateCopier`.
8. Construtores: Modelo `templateItem`, Diagrama `blueprintItem`, Mesa de Arquitetura
   `architectBlock`, Construtor `builderBlock`, Preenchedor `fillerBlock`, Biblioteca `libraryBlock`,
   Marco de Construção `constructionMarkerBlock`, Marcador de Caminhos `pathMarkerBlock`.
9. Robótica: Placa `BuildCraft|Robotics:redstone_board` (8 papéis + redstone; programada na Mesa de
   Programação, NBT `id`), Robô `robot` (ferro + Cristal de Redstone + 2 chipsets de diamante; placa
   combinada na Mesa de Integração, NBT `board.id`), Estação `robotStation` (ferro + chipset de ouro),
   `zonePlan`, `requester`, `BuildCraft|Core:mapLocation`, `list`.

## Materiais e geração de mundo

* **`worldgen.enable=false`**: o BuildCraft **não gera** poços/fontes de petróleo neste pack
  (`spawnOilSprings=false`). O fluido `oil` existe no mundo pelos bolsões subterrâneos do
  **Galacticraft** (`Oil Generation Factor=1.8`, registro como `oil` porque legacy `oilgc=false`) e
  pelo Immersive Petroleum (reservatórios/pumpjack).

## Máquinas, estruturas e multiblocos

* Pedreira: área por Marcadores (ou padrão), ergue Armação `frameBlock`, minera até o fundo.
* Refinaria: petróleo → combustível com RF (handler NEI com 2 receitas de fluido).
* Construtor/Arquitetura: escaneia área marcada para Diagrama/Modelo e reconstrói com materiais.

## Integrações no pack

* RF compartilhado com Thermal/EnderIO/Mekanism; combustível BC vale 1 500 000 RF/balde no Dínamo
  de Compressão do Thermal.
* `BuildCraft|Compat`: Tubo do Apicultor (`pipeitemspropolis`, diamante + própolis).
* Facades de milhares de blocos (`pipeFacade` com NBT).

## Armadilhas e dicas

* Canos de pedregulho/pedra/quartzo não se conectam entre si; itens sem destino caem no chão.
* Pedreira sem chunkloading; muito pedregulho (Diamante + Vazio).
* Motor de Combustão precisa de água; motores não explodem no pack.
* Nome pt_BR enganoso: Stirling Engine aparece como "Motor a Vapor".

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `buildcraft:triagem` | milestone | Primeiros canos, triagem com Diamante/Vazio, Motor Stirling e Mesa Automática (Ato I). |
| `buildcraft:pedreira` | milestone | A Pedreira (mineração automática). |
| `buildcraft:usina_combustao` | milestone | Petróleo → Refinaria → Combustível em Motores de Combustão (Ato II). |
| `buildcraft:construtor` | milestone | Construtor + Mesa de Arquitetura (diagramas). |
| `buildcraft:frota_robos` | milestone | Robôs programados + Estações de Encaixe. |
| `buildcraft:mestre_engrenagens` | milestone | **Marco final** do BuildCraft (Pedreira, Refinaria, Construtor, Robô). |
