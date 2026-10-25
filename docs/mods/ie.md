# Immersive Engineering — estudo do capítulo `ie`

## Visão geral

* **Mod:** `ImmersiveEngineering-auto-workbench-test-1-r3.jar`, modid/namespace `ImmersiveEngineering`,
  versão `0.7.9` (mcmod.info: "Forbidden Series Edition", `1.7.10-0.7.9`). É um build customizado
  que traz backports de versões posteriores: **Bancada Automatizada** (Automated Engineer's
  Workbench + Redstone Engineering Block), **Redoma de Jardim** (Garden Cloche), Montador,
  Engarrafadora, Preaquecedor, Estação de Carga, tanques/silos, canos e bombas de fluido.
* **Capítulo:** `quests/chapters/tech/ie.yaml`, `order: 335`, 154 quests
  (39 main, 52 normal, 50 optional, 7 info, 6 milestone).
* **Papel na campanha:** Ato I (martelo, coque, madeira tratada) atravessando o Ato II (aço,
  rede LV/MV/HV, multiblocos pesados, Forno a Arco, Escavadeira). Base obrigatória do capítulo
  `petroleo` (Immersive Petroleum e Arcane Engineering).
* **Não existem nesta versão:** Tesla Coil (há a classe `TileEntityTeslaCoil` no JAR, mas ela não
  é registrada — sem item, lang ou receita), armadura Faraday, Mixer, Crude Oil/Pumpjack (esses
  vêm do Immersive Petroleum). O capítulo não os cita como existentes.
* **Nomes:** o JAR não tem `pt_BR.lang`; o registro mostra os nomes em inglês, então as quests
  usam os nomes em inglês dos itens (como o jogador os vê).

Fontes: registro do pack (`reg.py mod ImmersiveEngineering`, receitas de bancada/fornalha), catálogo
NEI (handlers Coke Oven, Blast Furnace, Squeezer, Fermenter, Refinery, Bottling, Metal Press,
Crusher, Arc Furnace, Engineer's Workbench), `assets/immersiveengineering/lang/en_US.lang`
(manual completo), `config/ImmersiveEngineering.cfg` e bytecode (`IEContent`, `IERecipes`,
`getTotalMaterials()` de cada `Multiblock*`, `MultiblockAutoWorkbench`).

## Energia / recursos-base

* **RF**, transmitida por fios aéreos entre conectores. Valores da config do pack:

| Fio | Taxa (RF/t) | Vão máx. | Perda a cada 16 blocos | Conector (entrada/saída) |
| --- | --- | --- | --- | --- |
| LV — cobre (`coil:0`) | 2048 | 16 | 5% | LV 256 RF/t |
| MV — electrum (`coil:1`) | 8192 | 16 | 2,5% | MV 1024 RF/t |
| HV — aço+alumínio (`coil:2`) | 163 840 | 32 | 2,5% | HV 163 840 RF/t |
| Corda de cânhamo / cabo de aço | 0 (decorativos) | 32 | — | Conector Estrutural |

* Passar da taxa do **fio** queima o fio. Fios atravessam chunks descarregados. Compatibilidade
  IC2 ligada: os fios aceitam/entregam EU (`EU Conversion = 4`, 4 RF = 1 EU).
* Capacitores: LV 100 000 RF (256 RF/t), MV 1 000 000 (1024), HV 4 000 000 (163 840).
* Geração: Dínamo Cinético (base 3 RF/t × rotação de roda d'água/moinho; moinho aprimorado = 2×),
  Gerador Termoelétrico (diferença de temperatura; gelo 273 K, gelo compactado 200 K, blocos de
  urânio/yellorium 2000 K, plutônio 4000 K), Gerador a Diesel (**327 680 RF/t** na config do pack,
  dividido em até 3 saídas), Para-raios (16 000 000 RF por raio).
* Combustíveis do Gerador a Diesel (valor `registerFuel`): biodiesel 125, "fuel" 375, "diesel"
  175; o Immersive Petroleum acrescenta diesel 322, diesel sulfurado 285, querosene 208.

## Progressão (ids reais)

1. **Manual** `tool:3`, **Martelo** `tool:0` (100 usos), Wire Cutters `tool:1`, Voltímetro `tool:2`.
2. **Metais:** cobre/bauxita/chumbo/prata/níquel (`ore:0..4`, lingotes `metal:0..4`); martelo na
   bancada faz placas (`metal:30/32/33/36/38`) e Grit (`metal:8..14`); Constantan (pó de cobre +
   níquel → `metal:5`) e Electrum (prata + ouro → `metal:6`).
3. **Coque:** 27 Coke Bricks (`stoneDecoration:1`) → Forno de Coque: carvão → Coal Coke
   (`material:6`) + 500 mB de creosoto (1800 t); bloco de carvão → bloco de coque + 5000 mB;
   tora → carvão vegetal + 250 mB. Creosoto → Treated Wood (`treatedWood`, 8 tábuas por balde).
4. **Aço:** 27 Blast Bricks (`stoneDecoration:2`, pede pó de blaze e tijolo do Nether) → Alto-Forno:
   ferro → aço (`metal:7`) + escória em 1200 t; combustíveis coque 1200, bloco de coque 12000,
   carvão vegetal 300, bloco de carvão vegetal 3000. **Alto-Forno Aprimorado:** 27 Reinforced
   Blast Bricks (`stoneDecoration:6` = blast brick + placa de aço) + 1 funil; automatizável;
   até 2 Preaquecedores (`metalDevice2:11`, 32 RF/t).
5. **Rede:** bobinas `coil:0/1/2`, conectores `metalDevice:0/2/6`, relé HV `metalDevice:5` (só na
   face de baixo), capacitores `metalDevice:1/3/7`, transformadores `metalDevice:4/8`, blocos de
   bobina `storage:8/9/10`, dínamo `metalDevice:9`, roda d'água `woodenDevice:1`, moinhos
   `woodenDevice:2/3`, termoelétrico `metalDevice:10`.
6. **Engenharia pesada:** Light/Heavy/Redstone Engineering (`metalDecoration:7/5`,
   `metalDecoration2:3`), Radiator `metalDecoration:4`, Generator Block `metalDecoration:6`.
7. **Multiblocos** (materiais de `getTotalMaterials()`):

| Multibloco | Materiais |
| --- | --- |
| Forno de Coque / Alto-Forno | 27 Coke Bricks / 27 Blast Bricks |
| Alto-Forno Aprimorado | 27 Reinforced Blast Bricks + 1 funil |
| Espremedor / Fermentador | 8 Squeezer (`metalMultiblock:2`) / 8 Fermenter (`metalMultiblock:3`) + 19 Light Engineering |
| Refinaria | 8 Steel Scaffolding, 6 Light Eng., 24 Sheet Metal, 3 Heavy Eng. |
| Gerador a Diesel | 6 Generator Blocks, 9 Radiators, 27 Heavy Eng. |
| Triturador | 10 Steel Scaffolding, 11 Light Eng., 8 Steel Fences, 9 funis |
| Prensa de Metal | 3 Steel Scaffolding, 1 pistão, 2 esteiras, 1 Heavy Eng. |
| Engarrafadora | 5 Steel Scaffolding, 2 Light Eng., 5 esteiras |
| Montador | 9 Steel Scaffolding, 4 Light Eng., 6 Sheet Metal, 6 Structural Arms, 2 esteiras (contagem da tabela de materiais; conferir no manual) |
| Bancada Automatizada | 5 Steel Scaffolding, 2 lajes tratadas, 1 Redstone Eng., 4 Light Eng., 2 Heavy Eng., 4 esteiras |
| Forno a Arco | 1 caldeirão, 14 lajes de aço, 4 blocos de aço, 27 Reinforced Blast Bricks, 14 Light Eng., 7 Heavy Eng., 10 Steel Scaffolding |
| Escavadeira (motor) + Roda | 12 blocos de aço, 9 Light Eng., 13 Heavy Eng., 3 Steel Scaffolding + 9 blocos de aço, 20 Steel Scaffolding |
| Tanque / Silo | 4 cercas tratadas + 34 / 50 Sheet Metal |
| Para-raios | 9 Lightning Rod Base (`metalMultiblock:0`) + cercas de aço empilhadas |

8. **Grafite e Forno a Arco:** Coke Dust (`metal:17`, Triturador) → 8 no Espremedor → HOP Graphite
   Dust (`metal:19`) → fornalha → HOP Graphite Ingot (`metal:20`) → 4 lingotes + Projeto de
   Eletrodos (`blueprint:2`) na Bancada → Graphite Electrode (96 000 ticks de vida). O projeto de
   eletrodo **não é fabricável** (`Arc Furnace: Craftable Blueprint=false`): vem de baús
   (biblioteca de fortaleza, ferreiro de vila, casa do engenheiro) ou do aldeão Engenheiro.
9. **Escavadeira:** Core Sample Drill (`metalDevice:14`, 40 RF/t, 600 t) → Escavadeira
   (4096 RF/t, veio rende até 76 800 minérios, 5% de caçambas vazias).
10. **Arsenal:** Bancada `woodenDevice:5`; projetos `blueprint:0` (fabricável) e `:1` (baús/aldeão);
    revólver `revolver:0` (+ speedloader `:1`), cartuchos `bullet:2..10`; furadeira `drill` com
    cabeças `drillhead:0/1` e melhorias `toolupgrade:0..3`; Lança-Químicos `chemthrower` +
    `toolupgrade:7`; Railgun `railgun` (800 RF/tiro; munição varas de ferro/aço e eletrodos) +
    `toolupgrade:8/9`; Skyhook `skyhook`; Galão `jerrycan`; Shaders/Grabbags.

## Materiais e geração de mundo (`config/ImmersiveEngineering.cfg`)

Todos ativos, apenas Mundo Normal (blacklist Nether -1 e Fim 1):

| Minério | Veio | Y | Veios/chunk | Chance |
| --- | --- | --- | --- | --- |
| Cobre | 8 | 40–72 | 8 | 100% |
| Bauxita | 4 | 40–85 | 8 | 100% |
| Chumbo | 6 | 8–36 | 4 | 100% |
| Prata | 8 | 8–40 | 4 | 80% |
| Níquel | 6 | 8–24 | 2 | 100% |

Cânhamo: sementes ao quebrar grama alta. Veios minerais da Escavadeira (registrados em
`IEContent`): Iron, Magnetite, Pyrite, Bauxite, Copper, Cassiterite (se houver oreTin), Gold,
Nickel, Platinum, Uranium (se houver urânio/yellorium), Quartzite, Galena, Lead, Silver, Lapis,
Coal. Alguns são restritos por dimensão (o manual mostra).

## Máquinas, estruturas e multiblocos

Ver tabela acima. Detalhes de uso: Triturador (minério → 2 pós, até 120 RF/t, **fere criaturas** e
destrói itens sem receita), Prensa (moldes `mold:0/1/2`: placa 1:1, engrenagem 4:1, vara 1:2),
Engarrafadora (8 RF/t), Montador (3 receitas encadeadas, 80 RF por item), Bancada Automatizada
(23 040 RF e 180 t por montagem, configurável), Redoma (8 RF/t), Aquecedor Externo (8 RF/t por
calor, +24 RF/t para acelerar), Bomba (água infinita com >3 fontes; substitui fluidos por
pedregulho — alicate alterna), Forno a Arco (minério → 2 lingotes, aço com aditivo, ligas,
reciclagem ligada).

## Dimensões, estruturas, chefes e mobs

* **Casa do Engenheiro** nas vilas (ativada), com caixotes de saque e o aldeão **Engenheiro**
  (vende projetos, shaders e peças por esmeraldas).
* Shader Grabbags caem de chefes (lista negra: NPCs do Custom NPCs).

## Integrações no pack

* Oredict amplo: cobre, aço, alumínio, electrum etc. de qualquer mod; `circuitBasic` não é usado.
* Fios ↔ EU do IC2; Estação de Carga carrega itens de EU.
* Prensa faz engrenagens no formato do Thermal Foundation; Forno a Arco funde ligas do Ender IO.
* Cartucho Teleguiado usa Pepita de Terrasteel (Botania).
* Base inteira do Immersive Petroleum e do Arcane Engineering (capítulo `petroleo`).

## Armadilhas e dicas

* Alto-Forno: **carvão mineral não serve**; tirar escória ou ele para.
* Conectores LV/MV também são relés; HV não (relé HV só na face de baixo).
* Passar da taxa do fio queima o fio; o voltímetro mede perdas entre dois conectores.
* Triturador mata quem cair; Explosivo destrói terreno.
* Gerador a Diesel bebe rápido — monte a linha de biodiesel antes.
* Projeto de Eletrodos só por exploração/comércio.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `ie:alto_forno_aprimorado` | milestone | Aço automatizado (Alto-Forno Aprimorado). Entrada no Ato II. |
| `ie:rede_eletrica` | milestone | Rede LV/MV/HV com capacitores e transformadores. |
| `ie:gerador_diesel` | milestone | Biodiesel + Gerador a Diesel (primeira energia pesada). |
| `ie:forno_arco` | milestone | Forno a Arco com eletrodos de grafite. |
| `ie:escavadeira` | milestone | Escavadeira e veios minerais. |
| `ie:mestre_engenheiro` | milestone | **Marco final** do capítulo (Forno a Arco + Escavadeira + Diesel + rede + Railgun + Skyhook) — candidato ao Selo da Engrenagem. |
