# Ender IO — estudo do capítulo `enderio`

## Visão geral

* **Mod:** `EnderIO-2.10.39.jar` (namespace de registro `EnderIO`) + `endercore-0.5.14.jar`.
  É um fork 1.7.10 estilo GTNH que já traz embutidas as ligas/condutos **"Endergy"** (Clay
  Compound, Liga Cristalina, Melódica, Estelar, Prata Energética, Liga Vívida, Crystalline Pink
  Slime), capacitores extras (Cristalino, Melódico, Estelar, Totêmico, linha de prata) e o
  equipamento de **End Steel** e **Estelar**.
* **Capítulo:** `quests/chapters/tech/enderio.yaml`, `order: 320`, 185 quests.
* **Papel na campanha:** Ato II (Engrenagens e Encantos) — ligas, condutos, energia RF,
  automação e viagem. O trecho Endergy/Estelar (estrela do Nether) é Ato III/IV.

Fontes: registro do pack (`reg.py mod EnderIO`), receitas de bancada do dump, catálogo NEI
(`Alloy Smelter`, `SAG Mill`, `Vat`, `Slice'N'Splice`, `Soul Binder`, `Enchanter`), os XML de
receitas em `config/enderio/*_Core.xml` (idênticos aos do JAR; os `_User.xml` estão vazios — o
pack **não altera** receitas de máquina do Ender IO), `config/enderio/EnderIO.cfg`, lang do JAR
(`en_US`/`pt_BR`) e `javap` das classes de upgrades de Aço Negro, Vinculador de Almas e Telepad.

## Energia / recursos-base

* **RF**, sem tensões (nada explode por sobrecarga). Valores da config do pack:
  * Condutos de energia: básico 640 RF/t, Melhorado 5120, do Fim 20 480.
    `powerConduitCanDifferentTiersConnect=false` → tiers diferentes **não se ligam**.
    Condutos Endergy (12 tiers, de Clay Compound até Estelar ≈ ilimitado).
  * Bancos de Capacitores: Básico 1 M RF / 1000 RF/t; comum 5 M / 5000; Vibrante 25 M / 25 000.
    Multibloco por adjacência do mesmo tipo.
  * Stirling 20 RF/t base (capacitores multiplicam); Combustão: Aguardente 60, Água de Fogo 80,
    Combustível para Foguetes 160 RF/t; Gerador Zumbi 80, Frank'N'Zombie 120, Ender 360;
    Células Fotovoltaicas 10 / 40 / 160 RF/t.
* **Capacitores** no espaço de capacitor de cada máquina: Básico → Dupla Camada → Octádico →
  (Endergy) Cristalino → Melódico → Estelar; Totêmico à parte; linha de prata (Prata →
  Endergetic → Endergised) equivale à básica (oredict `capacitorBasic/Advanced/Ender`).

## Progressão (ids reais)

1. Engrenagem Básica `EnderIO:itemMachinePart:1`, Capacitor Básico `itemBasicCapacitor`
   (ouro, redstone, **cobre**), Chassi de Máquina `itemMachinePart`, Gerador de Stirling
   `blockStirlingGenerator`.
2. Moinho SAG `blockSagMill` (minério ×2, areia → Silício 50%), Fundidora de Ligas
   `blockAlloySmelter`.
3. Ligas `EnderIO:itemAlloy:0..8` (receitas da Fundidora, confirmadas no XML):
   Aço Elétrico (ferro + pó de carvão + silício), Liga Energética (ouro + redstone + pó de
   pedra luminosa), Vibrante (Energética + pérola), Redstone (redstone + silício), Ferro
   Condutivo (ferro + redstone), Ferro Pulsante (ferro + pérola), Aço Negro (ferro + pó de
   carvão + obsidiana), Almário (ouro + areia das almas), End Steel (Aço Negro + pedra do Fim +
   obsidiana).
4. Cristais: Pulsante `itemMaterial:5` (8 pepitas + diamante), Vibrante `:6` (8 pepitas +
   esmeralda). Capacitores Dupla Camada `:1`, Octádico `:2`.
5. Condutos: Composto de Capa → Capa de Conduto (fornalha) → energia, itens (pepitas de Ferro
   Pulsante), fluidos (vidro claro / Quartzo Fundido / Liga Vibrante), redstone, gás, ME, OC.
6. Energia: Bancos de Capacitores, Gerador a Combustão, Tonel (`blockVat`) e fluidos, Células
   Fotovoltaicas, Reservatório (2×2, água infinita).
7. Almas: "The Ender" (cabeças, pérolas), Fatia-e-Emenda (precisa de **machado e tesoura** nos
   espaços de ferramenta — NEI), Frasco de Almas, Vinculador de Almas (4 cabeças).
   Vinculador (NEI + bytecode): Cristal Vibrante + Enderman → Cristal do Fim; Esmeralda +
   Aldeão → Cristal Atraente; Cristal Pulsante + Enderman → Precient Crystal; Z-Lógica +
   Zumbi → Frankenzumbi; Ressoador do Fim + Bruxa → Sentient Ender; trilho detector → Trilho do
   Fim; Invocador Quebrado + alma → novo tipo (15 níveis, 2,5 M RF).
8. Automação: Estação de Cultivo, Fabricador (2500 RF/craft), Baú Aspirador (raio 6),
   Painel de Inventário (usa Destilado de Nutrientes), Encantador, Eletroímã.
9. Viagem: Âncora (alcance 48), Cajado da Viagem (128 / piscar 16, 250 RF por bloco), IO do Fim,
   Telepad (3×3 de `blockTelePad`, **cada bloco leva um Cajado**), Seletor de Coordenadas,
   Transceptor Dimensional (manutenção 10 RF/t, perda 10%).
10. Aço Negro: ferramentas e armadura + upgrades na bigorna. Endergy → Estelar.

## Upgrades de Aço Negro (bytecode + config)

| Upgrade | Peça | Item | Níveis (custo) | Chave NBT / `unlocalized_name` |
| --- | --- | --- | --- | --- |
| Energia I–V | todas | Cristal Vibrante, Cap. Básico, Dupla, Octádico, Cristalino | 10/10/15/20/25 | `energyUpgrade` / `empowered_one..five` (100k/150k/250k/1M/2,5M RF) |
| Planador | peitoral | Asas de Planador | 10 | `glide` / `glider` |
| Velocidade I–III | perneiras | poção de Velocidade | 10/15/20 | `speedBoost` / `speed_one..three` |
| Pulo I–III | botas | pistão | 10/15/20 | `jumpBoost` / `jump_one..three` |
| Nado | botas | vitória-régia | 10 | `swim` |
| Visão Noturna | elmo | poção de Visão Noturna | 10 | `nightVision` |
| Localizador de Sons | elmo | bloco musical | 10 | `soundDetector` / `sound` |
| Solar I–III | elmo | Células Fotovoltaicas | 15/30/… | `speedBoost` / `solar_one..three` |
| Colher | picaretas | pá de diamante | 10 | `spoon` |
| Viagem | picaretas/espadas | Cristal do Fim | 30 | `travel` |

As quests de upgrade verificam a NBT (`nbt: true`, comparação parcial por
`{"enderio.darksteel.upgrade.<chave>":{unlocalized_name:"enderio.darksteel.upgrade.<nome>"}}`,
formato confirmado no NBT do registro).

## Materiais e geração de mundo

O Ender IO não gera minérios. Depende de ferro, ouro, cobre (capacitor), quartzo, areia das
almas (Nether), pedra do Fim (End Steel), pérolas, esmeralda e diamante. A Estrela do Nether
(Liga Estelar) exige o Wither. Quebrar um gerador de monstros **sempre** solta o Invocador
Quebrado (`brokenSpawnerDropChance=1.0`).

## Máquinas, estruturas e multiblocos

* Bancos de Capacitores e Células Fotovoltaicas se juntam por adjacência.
* Reservatório: 2×2×1, dois baldes de água → infinito (1 balde/s).
* Telepad: 3×1×3. `lockCoords=true` e `lockDimension=true`: o GUI **não aceita** digitar
  coordenadas; o destino vem do Seletor de Coordenadas clicado no Telepad (bytecode de
  `ItemCoordSelector.onItemUseFirst`).
* Invocador Elétrico: programado na **bigorna** com um Invocador Quebrado (30 níveis).

## Integrações no pack

* Silício do Ender IO faz Circuitos de Controle do Mekanism (Infusor Metalúrgico).
* Conduto de Gases leva gases do Mekanism; Conduto ME (AE2), Conduto de Rede (OpenComputers).
* Fundidora faz ligas do Thermal (bronze, invar, eletro, enderium, sinálio, lumium).
* Magical Crops tem essências que viram lingotes de liga do Ender IO (receitas no dump).
* Estação de Cultivo aceita enxadas de vários mods (lista `farmHoes` da config).
* Moinho SAG tem receitas para Yellorite/Yellorium (Big Reactors) e ósmio (Mekanism).

## Armadilhas e dicas

* Tiers diferentes de conduto de energia não se conectam (config).
* Fatia-e-Emenda gasta machado e tesoura; Vinculador gasta XP — use o Obelisco de Experiência.
* Frasco de Almas não captura chefes (`soulVesselCapturesBosses=false`).
* Telepad caríssimo (9 Cajados) e exige o Seletor de Coordenadas.
* `Weather Crystal`: o NEI mostra só um diamante como ingrediente; a quest manda conferir no NEI.
* A Cabeça Atormentada pede duas poções (o NEI mostra `minecraft:potion:0`) — a quest manda
  conferir.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `enderio:ligas_completas` | milestone | As oito ligas clássicas (entrada no Ato II). |
| `enderio:rede_condutos` | milestone | Condutos de energia, itens, fluidos e redstone. |
| `enderio:usina_ligas` | milestone | Gerador a Combustão + Combustível para Foguetes + bancos + solar. |
| `enderio:mestre_almas` | milestone | Vinculador de Almas, Cristal do Fim, Frankenzumbi, Sentient Ender. |
| `enderio:fabrica_autonoma` | milestone | Estação de Cultivo, Fabricador, Baú Aspirador. |
| `enderio:senhor_distancias` | milestone | Cajado da Viagem, Telepad e Transceptor. |
| `enderio:armadura_negra` | milestone | Armadura negra completa com upgrades. |
| `enderio:armadura_estelar` | milestone | Armadura Estelar (Liga Estelar, estrelas do Nether) — Ato IV. |
| `enderio:mestre_ligas` | milestone | **Marco final** do capítulo (todos os marcos principais + Banco Vibrante). |
