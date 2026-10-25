# Galacticraft — estudo do capítulo `galacticraft`

## Visão geral

* **Mod:** `Galacticraft-4.0-Akashic.jar` — build customizado do fork GTNH do Galacticraft 3
  (1.7.10), com dois modids: `GalacticraftCore` (Core + Lua) e `GalacticraftMars` (Planets: Marte e
  Asteroides). Tem tradução pt_BR completa.
* **Capítulo:** `quests/chapters/tech/galacticraft.yaml`, `order: 390`, **154 quests** (47 main,
  37 normal, 56 optional, 4 info, 5 milestone, 2 boss, 3 portal).
* **Papel na campanha:** metais prensados/circuitos no Ato II; Lua no Ato III; Marte, Asteroides e
  os chefes no Ato IV — **Selo das Estrelas** (junto com ExtraPlanets e Amun-Ra). Base obrigatória
  dos capítulos `extraplanets` e `amunra`.
* O catálogo NEI do pack **não capturou** as receitas das máquinas do GC (handlers com 0 receitas:
  Prensa, Fabricador de Circuitos, Bancada NASA, Refinaria). Receitas de máquina foram conferidas
  no bytecode (`RecipeManagerGC`, `RecipeManagerMars`, `RecipeManagerAsteroids`, `MarsModule`).

## Energia / recursos-base

* **gJ**, com conversão sem perdas para RF (1,6), EU (6,56), Joules Mekanism e MJ (config
  `power-GC3.conf`, entrada/saída de todas ligadas).
* Geradores: Gerador a Carvão, Painel Solar Básico/Avançado (×2 na Estação Espacial —
  `Space Station Solar Energy Multiplier=2.0`). Armazenamento: Bateria, Módulo e Grupo de
  Armazenagem de Energia. Cabos: Alumínio e Alumínio Pesado.

## Progressão (ids reais)

1. Metais: cobre/estanho/alumínio/silício do GC no Overworld (config: todos ligados), aço de
   qualquer mod. **Fabricador de Circuitos** `tile.machine2:4` (diamante + 2 silício + redstone +
   tocha → 3 `basicItem:13` Wafer Básico; repetidor → 1 `basicItem:14` Wafer Avançado; lápis → 9
   `basicItem:12` Wafer Solar). **Prensa** `tile.machine:12` (pede Wafer Básico): 2 lingotes → metal
   prensado; ferro meteórico e desh 1 → 1; Aço Prensado também = Ferro Prensado + 2 carvões
   (modo difícil desligado).
2. **Blindagem Pesada** `item.heavyPlating` (Prensa: aço + alumínio + bronze prensados ×2 → 2).
   Peças: `noseCone`, `rocketFins`, `engine`, Bancada NASA `tile.rocketWorkbench`,
   **Foguete Nível 1** `item.spaceship` (nariz, 8 blindagens, 4 aletas, motor; baús = metas 1–3).
3. Combustível: petróleo (lagos do GC no Overworld, `Oil Generation Factor=1.8`; mesmo fluido do
   BuildCraft) → Refinaria `tile.refinery` → `fuel`. Carregador `tile.fuelLoader`, base 3×3
   `tile.landingPad`.
4. Oxigênio: `oxygenMask`, `oxygenGear`, tanques Leve/Médio/Pesado (900/1800/2700), Coletor,
   Compressor/Descompressor, Distribuidor, Selador, Portas Pressurizadas, Paraquedas, Módulo de
   Frequência. Tochas comuns apagam sem ar → Tocha de Pedra Luminosa.
5. **Lua** (dim −28): Ferro Meteórico (meteoros), queijo, calabouços de Tijolos Lunares,
   `GalacticraftCore.EvolvedSkeletonBoss` → `item.key` → baú → `item.schematic:1` (Foguete Nível 2)
   ou `item.schematic` (Buggy) ou `ExtraPlanets:item.schematicMarsRover`.
6. **Estação Espacial**: 32 estanho, 16 alumínio, 24 ferro, 1 Wafer Avançado (bytecode).
7. **Marte** (dim −29): Blindagem Nível 2 `GalacticraftMars:item.null:3` (Prensa: Blindagem +
   Ferro Meteórico Prensado), Motor Auxiliar `item.engine:1`, Foguete Nível 2 (nariz, 10 blindagens
   N2, 4 aletas, motor, 2 auxiliares). Desh, Traje Térmico, Creeper-Chefe → `GalacticraftMars:item.key`
   → `GalacticraftMars:item.schematic` (N3) / `:1` (Carga) / `:2` (Astrominerador).
8. **Asteroides** (dim −30): Blindagem Nível 3 `itemBasicAsteroids:0` (Prensa: N2 + Desh Prensado),
   nariz/motor/aletas pesados, Foguete Nível 3 `item.itemTier3Rocket`; ilmenita → titânio.

## Materiais e geração de mundo

* Overworld: cobre, estanho, alumínio, silício (todos ligados), petróleo. Lua: cobre, estanho,
  queijo (ligados), vilas ligadas. Marte: cobre, estanho, ferro, desh. Asteroides: alumínio,
  ferro, ilmenita (todos ligados).
* Térmico (bytecode): Lua 0; Marte −1,0; Asteroides −1,5 → sem as 4 peças de Traje Térmico a
  temperatura muda até causar dano; ar respirável normaliza.

## Dimensões, estruturas e chefes

* Lua −28, Marte −29, Asteroides −30 (`reg.py dims`); Estação Espacial = dimensão dinâmica (quest
  com checkbox). Foguetes não decolam do Nether/Fim (config).
* Chefes: Esqueleto-Chefe Evoluído (Lua), Creeper-Chefe Evoluído (Marte, dispara TNT). Asteroides
  não têm chefe no GC.

## Integrações no pack

* Petróleo/combustível compartilhados com BuildCraft (baldes de ambos aceitos).
* Cabos/canos selados aceitam cabos de IC2, AE2, BuildCraft (receitas `tile.enclosed:*`).
* ExtraPlanets adiciona o esquemático do Mars Rover aos baús lunares; Amun-Ra usa titânio, Núcleo
  de Feixe e Unidade Orion.

## Armadilhas e dicas

* Prensa precisa de Wafer Básico → o Fabricador vem antes.
* Tanques saem vazios da bancada; Coletor fora da Terra só funciona com folhas/plantas por perto.
* Cada baú de calabouço dá um único esquemático — pode ser preciso mais de um calabouço.
* Gravidade (bytecode): quanto maior o valor de `getGravity`, menor a gravidade — Asteroides < Lua < Marte.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `galacticraft:oficina_espacial` | milestone | Prensa, Fabricador de Circuitos e Aço Prensado. |
| `galacticraft:pronto_lancamento` | milestone | Foguete, combustível e equipamento de oxigênio prontos. |
| `galacticraft:lua` | portal | Chegada à Lua. |
| `galacticraft:esqueleto_chefe` | boss | **Esqueleto-Chefe Evoluído — Selo das Estrelas.** |
| `galacticraft:estacao_espacial` | milestone | Estação Espacial criada. |
| `galacticraft:marte` | portal | Chegada a Marte. |
| `galacticraft:creeper_chefe` | boss | **Creeper-Chefe Evoluído — Selo das Estrelas.** |
| `galacticraft:base_marte` | milestone | Desh, traje térmico e chefe de Marte. |
| `galacticraft:asteroides` | portal | Chegada ao Cinturão de Asteroides. |
| `galacticraft:explorador_solar` | milestone | **Marco final** (Foguete N3, titânio, 2 chaves, estação). |

Referências usadas por outros capítulos (deps): `galacticraft:foguete_t2`, `galacticraft:foguete_t3`,
`galacticraft:titanio_prensado`, `galacticraft:nucleo_feixe`, `galacticraft:unidade_orion`,
`galacticraft:motor_pesado`, `galacticraft:condensador`.
