# MekanismGenerators — estudo do capítulo `mekanism_gen`

## Visão geral

* **Mod:** módulo MekanismGenerators do JAR `Mekanism-1.7.10-9.10.48-AkashicPerf.3-ALL.jar`
  (namespace `MekanismGenerators`). Config: `config/mekanism/MekanismGenerators.cfg` (geração e
  receitas — todas habilitadas) e a seção `mekce_generation` de `MekanismCE.cfg`.
* **Capítulo:** `quests/chapters/tech/mekanism_gen.yaml`, `order: 326`, 53 quests + 6 links para
  quests do capítulo `mekanism` (hidrogênio, caldeira, Matriz de Indução, água pesada, Ativador
  Neutrônico, Amplificador de Laser). Dependências cruzadas usam `deps_hidden`.
* **Papel na campanha:** Ato II (geradores simples) → Ato III (Turbina Industrial) → Ato IV (Reator
  de Fusão, que alimenta o **Selo da Engrenagem**).

## Conteúdo confirmado no registro

`Generator`: 0 Gerador a Calor, 1 Solar, 3 Queima de Gás, 4 Biogerador, 5 Solar Avançado,
6 Eólico, 7 Rotor de Turbina, 8 Complexo Rotacional, 9 Bobina Eletromagnética, 10 Moldura de
Turbina, 11 Válvula de Turbina, 12 Respiradouro, 13 Saturating Condenser.
`Reactor`: 0 Controlador, 1 Armação, 3 Porta, 4 Adaptador Lógico (o meta 2, Placa de Captura de
Nêutrons, não existe no registro). `ReactorGlass`: 0 Vidro, 1 Matriz Focal do Laser.
Itens: `SolarPanel`, `TurbineBlade`, `Hohlraum`. Laser/Amplificador/Feixe Trator e Ativador
Neutrônico Solar são blocos do Mekanism base.

## Energia e config

* Valores internos (J/t) de `MekanismGenerators.cfg`: Calor 150 (+5 por bloco de lava vizinho,
  +100 no Nether), Solar 50, Solar Avançado 300, Bio 100, Eólico 60–480 entre Y 24 e Y 255.
  O pack exibe RF; as quests usam comparações relativas em vez de números em RF.
* `EnableWindmillWhitelist=true`, `Winddimensionids=[0]` → Eólico **só no Mundo Normal**.
* Turbina: `TurbineBladesPerCoil=4` (1 bobina a cada 4 hélices), vazões de dispersor/respiradouro
  e condensador na config.
* Fusão (CE): `FusionReactorEnergyEfficiency=0.00125` (antigo 0.05) e
  `FusionReactorSteamTransferEfficiency=0.0025` (antigo 0.1) — fusão **bem mais fraca** que no
  Mekanism clássico. Quests avisam isso.

## Receitas (bytecode `MekanismGenerators.addRecipes/postInit` + NEI)

* Gerador a Calor: 3 ferros, 2 tábuas, ósmio, 2 cobres, fornalha.
* Painel Solar: 3 vidraças, 2 redstone, Liga Enriquecida, 3 ósmio. Solar: 3 painéis, 2 Enriquecidas,
  ferro, 2 pós de ósmio, Tablete. Avançado: 4 solares, 2 Enriquecidas, 3 ferros.
* Bio: redstone, Enriquecidas, biocombustível, circuito, ferro. Gás: ósmio, Enriquecidas, Carcaça
  de Aço, Núcleo Eletrolítico. Eólico: 3 ósmio, Enriquecida, 2 Tabletes, circuito.
* Hélice: 4 aços + Enriquecida. Rotor: 6 aços + 3 Enriquecidas. Complexo: aço, Enriquecidas,
  2 Circuitos Avançados. Bobina: 4 aços, 4 ouros, Tablete. Moldura: 4 aços + ósmio → 4. Válvula: 4
  molduras + Circuito Avançado → 2. Respiradouro: 4 molduras + barra de ferro → 2. Condensador: aço,
  estanho, balde.
* Armação do Reator: 4 Carcaças de Aço + Liga Atômica → 4. Porta: 4 armações + Circuito Final → 2.
  Vidro: 4 armações + vidro → 4. Controlador: 2 Circuitos Finais, vidraça, armações, Tanque de Gás
  Básico. Matriz Focal: 4 vidros + bloco de redstone → 2. Adaptador: armação + 4 redstone.
* **Hohlraum:** Infusor Metalúrgico, 4 pós de ouro + Carbono (NEI). Enchido com Combustível D-T
  num tanque de gás (NBT `stored:{gasName:"fusionFuelDT"}`, confirmado no registro).
* Gases combustíveis registrados: etileno e metano; combustíveis do BuildCraft viram gases.

## Multiblocos

* **Turbina Industrial:** base quadrada (5×5 a 17×17), rotores empilhados no centro (2 hélices
  cada), Complexo Rotacional em cima, camada de Dispersores de Pressão, Bobinas acima encostadas
  no complexo, Respiradouros nas paredes do nível do complexo para cima, Válvulas (vapor/energia).
* **Reator de Fusão:** cubo 5×5×5 sem arestas e cantos (quadrado 3×3 em cada face, 54 blocos),
  interior oco; Controlador no centro da face de cima; Portas, Vidros, Matriz Focal e Adaptador
  nas faces. Ignição: Hohlraum D-T no controlador + disparo do Amplificador de Laser na Matriz
  Focal. A quest manda conferir o formato exato no manual/wiki (descrição baseada no Mekanism 9).

## Armadilhas e dicas

* Água → hidrogênio → Gerador a Gás é um ciclo com prejuízo; etileno dá lucro.
* Eólico não funciona fora da dimensão 0.
* Trítio e água pesada são lentos: várias bombas com Upgrade de Filtro e vários Ativadores.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `mekanism_gen:usina_simples` | milestone | Solar Avançado, Eólico, Gás e Bio (Ato II). |
| `mekanism_gen:turbina_industrial` | milestone | Turbina Industrial montada (Ato III). |
| `mekanism_gen:reator_fusao` | milestone | **Marco final** — Reator de Fusão; usado pelo **Selo da Engrenagem**. |
