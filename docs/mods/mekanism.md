# Mekanism — estudo do capítulo `mekanism`

## Visão geral

* **Mod:** `Mekanism-1.7.10-9.10.48-AkashicPerf.3-ALL.jar` — fork 9.x "Community Edition /
  GTNH-style" para 1.7.10, com MekanismGenerators e MekanismTools no mesmo JAR (namespaces de
  registro `Mekanism`, `MekanismGenerators`, `MekanismTools`). Config em `config/mekanism/`
  (`Mekanism.cfg`, `MekanismCE.cfg`, `MekanismRecipes.cfg`, `MekanismGenerators.cfg`,
  `MekanismTools.cfg`).
* **Capítulo:** `quests/chapters/tech/mekanism.yaml`, `order: 325`, 170 quests. Os geradores, a
  turbina e a fusão estão no capítulo `mekanism_gen` (ver `docs/mods/mekanism_gen.md`).
* **Papel na campanha:** Ato II (ósmio, aço, ligas, 2×/3×) até Ato IV (5×, Matriz de Indução,
  teletransporte, Minerador Digital). O marco final alimenta o **Selo da Engrenagem**.

Fontes: registro do pack, catálogo NEI (handlers do Mekanism: Câmara de Enriquecimento,
Infusor Metalúrgico, Purificação, Injeção, Dissolução, Lavador, Cristalizador, Oxidador, Infusor
Químico, Separador, Evaporação, CRP, Ativador Neutrônico, "Mekanism Exata"), `javap` de
`Mekanism.addRecipes`, `OreDictManager.init`, `MekanismGenerators.addRecipes/postInit` e
`MekanismTools.addRecipes` (receitas reconstruídas com um interpretador simbólico de bytecode —
as receitas de bancada do Mekanism são "opacas" no dump), `GenHandler` (geração), lang `pt_BR`.

## Máquinas confirmadas no registro

`MachineBlock` 0–15: Câmara de Enriquecimento, Prensa de Ósmio, Combinador, Triturador,
Minerador Digital, Fábricas Básica/Avançada/Elite (8 tipos cada, NBT `recipeType` 0–7:
Fundição, Enriquecimento, Trituração, Prensagem, Combinação, Purificação, Injeção, Infusão),
Infusor Metalúrgico, Câmara de Purificação, Fundição Energizada, Teletransportador, Bomba
Elétrica, Baú Pessoal, Base de Recarga, Classificador Logístico.
`MachineBlock2` 0–15: Condensentrador Rotativo, Oxidador Químico, Infusor Químico, Câmara de
Injeção Química, Separador Eletrolítico, Serraria de Precisão, Câmara de Dissolução, Lavador,
Cristalizador, Vibrador Sísmico, CRP, Tanque Líquido (tiers), Abastecedor Fluídico, Laser,
Amplificador de Laser, Feixe Trator. `MachineBlock3`: Transportador Quântico (0), Ativador
Neutrônico Solar (1), Oredictionificador (3), Aquecedor Resistivo (4), Montador Formular (5),
Aquecedor Sólido (6). **Não existem** fábricas de tier Final (só Básica/Avançada/Elite), embora
exista o Instalador de Tier Final. Não há Isotopic Centrifuge nem Nutritional Liquifier.
Todas as máquinas estão habilitadas em `Mekanism.cfg` (`machines`) e `MekanismRecipes.cfg`.

## Energia / recursos-base

* Joules internos; `EnergyType=RF` (exibe RF). Aceita RF e EU (`IC2SinkTier=4`). Sem explosões.
* Tiers (config): cabos 3 200 / 12 800 / 64 000 / 320 000; cubos 2 M / 8 M / 32 M / 128 M;
  células de indução 1 G / 8 G / 64 G / 512 G; provedores 64 k / 512 k / 4,096 M / 32,768 M.
  Silos 4096 / 8192 / 32 768 / 262 144.

## Progressão (ids reais) — particularidades deste fork

1. Ósmio `Mekanism:OreBlock` (Y 0–59, veio 8, 12/chunk; também Deepslate e Ósmio Bruto
   `RawOsmiumOre` com oredict `oreOsmium`), cobre (16/chunk), estanho (14/chunk), sal (2/chunk,
   no fundo de água). Barra `Mekanism:Ingot:1`.
2. **Infusor Metalúrgico** `MachineBlock:8` (ferro, fornalhas, redstone, ósmio).
   Ferro → Ferro Enriquecido (carbono) → Pó de Aço (carbono) → Aço `Ingot:4`.
3. **Circuito Básico = Silício + infusão de Redstone** (`EnableSiliconCompat=true`); receitas
   aceitam qualquer `circuitBasic` (IC2/Eln também).
4. **Liga Enriquecida = Aço + Redstone** (NEI: entradas `ingotSteel`, inclusive Ferro Refinado do
   IC2) — diferente do Mekanism clássico (ferro). Reforçada = Enriquecida + Diamante; Atômica =
   Reforçada + Obsidiana (Obsidiana Prensada). `EnableBoPProgression` não se aplica (sem BoP).
5. Carcaça de Aço `BasicBlock:8` (aço, vidro, ósmio). Máquinas básicas com `alloyBasic`
   (= redstone) e circuitos.
6. Processamento: 2× Enriquecimento; 3× Purificação (oxigênio, 3 aglomerados); 4× Injeção
   (cloreto de hidrogênio, 4 estilhaços); 5× Dissolução (ácido sulfúrico, 1000 mB de polpa) →
   Lavador (água) → Cristalizador (200 mB → 1 cristal).
7. Gases: Separador Eletrolítico (água → H₂ + O₂; salmoura → sódio + cloro; água pesada →
   deutério + O₂), Infusor Químico (H₂+Cl₂ → HCl; O₂+SO₂ → SO₃; SO₃+vapor → H₂SO₄; D+T → D-T),
   Oxidador (enxofre → SO₂; sal → salmoura; lítio; biocombustível → biomassa), Torre de
   Evaporação (água → salmoura → lítio; bioetanol → etileno), CRP (fermento/substrato/PEAD),
   Ativador Neutrônico Solar (lítio → trítio).
8. Fábricas, logística (cabos, tubos mecânicos/pressurizados, transportadores, Classificador),
   armazenamento (silos, tanques, Tanque Dinâmico), Matriz de Indução, Caldeira Termoelétrica.
9. Núcleo de Teletransporte (4 pérolas, 2 Atômicas, 2 ouros, diamante), Teletransportador
   (3×4 de molduras, vão 1×2), Portátil, Robit, Minerador Digital (raio máx. 32 —
   `DigitalMinerMaxRadius=32`; Silk Touch ×6 energia), Transportador Quântico, Lasers.
10. Equipamentos: Jetpack (hidrogênio), Blindado, Máscara + Tanque de Mergulho (oxigênio),
    Corredores, Desintegrador Atômico (20 de dano carregado), Arco Elétrico, Lança-Chamas.
11. MekanismTools: pacachados (madeira, pedra, ferro, ouro, diamante e materiais do mod) e
    ferramentas/armaduras de ósmio, bronze, aço (cabo de ferro), obsidiana refinada, pedra
    luminosa refinada e lápis-lazúli.

## Multiblocos

* **Matriz de Indução:** 3×3×3 a 18×18×18, Caixas de Indução (+ Vidro Estrutural), Portas
  (Configurador alterna entrada/saída), interior só com Células e Provedores.
* **Tanque Dinâmico:** caixa oca de Tanques Dinâmicos/Vidro Estrutural com Válvulas.
* **Torre de Evaporação Térmica:** base 4×4, Controlador, Válvulas, Blocos de Evaporação.
* **Caldeira Termoelétrica:** Carcaças/Válvulas da Caldeira, Elementos de Superaquecimento,
  camada de Dispersores de Pressão.
* **Teletransportador:** moldura 3×4 (9 Molduras + o Teletransportador no meio da base).

## Verificação nas quests

Itens com NBT verificados por `nbt: true` (comparação parcial):
`Mekanism:EnergyCube {tier:"Basic|Advanced|Elite|Ultimate"}`; `Mekanism:GasTank {tier:N}` e
`{stored:{gasName:"oxygen|hydrogen|chlorine|hydrogenChloride|sulfuricAcid|sodium"}}` (formato
`stored`/`gasName` confirmado no NBT do Jetpack do registro e no bytecode de
`ItemBlockGasTank.setGas`); `BasicBlock2:3/4 {tier:N}` (células/provedores);
`BasicBlock:6 {tier:3}` (silo final); fábricas `{recipeType:N}`; Jetpack/Tanque de Mergulho com
gás. Gases em tanques: o jogador quebra o tanque (ele mantém o gás) e o traz.

## Integrações no pack

* Silício do Ender IO/Galacticraft/RotaryCraft → Circuitos; `circuitBasic` do IC2 aceito.
* Aço de qualquer mod (`ingotSteel`) → Liga Enriquecida.
* Combinador/Enriquecimento com quartzo, certus e fluix (AE2); Yellorite → 2 pós (Big Reactors).
* Conduto de Gases do Ender IO transporta gases do Mekanism.
* Combustíveis do BuildCraft viram gases para o Gerador a Queima de Gás.
* Baú Pessoal abre do inventário (`enablePersonalChestPocketAccess=true`); Caixa de Papelão de uso
  único (`EnableSingleUseCardboxes=true`), pode embalar geradores de monstros.

## Armadilhas e dicas

* Liga Enriquecida pede aço (não ferro) e Circuito pede silício — fork diferente do clássico.
* Gases diferentes não dividem o mesmo tubo; o botão de despejar do GUI apaga o gás.
* O Gerador Eólico só gira na dimensão 0 (ver `mekanism_gen`).
* Upgrades de velocidade aumentam muito o consumo de energia.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `mekanism:primeira_fabrica_mek` | milestone | Infusor, Câmara de Enriquecimento, Cubo e cabos (Ato II). |
| `mekanism:tres_ligas` | milestone | Ligas Enriquecida/Reforçada/Atômica e Circuito Final. |
| `mekanism:processamento_5x` | milestone | Linha completa de processamento 5×. |
| `mekanism:logistica_mek` | milestone | Transportadores, Classificador, cabos de elite, fábricas. |
| `mekanism:matriz_inducao` | milestone | Matriz de Indução montada. |
| `mekanism:dobrando_espaco` | milestone | Teletransportador, Minerador Digital, Transportador Quântico. |
| `mekanism:arsenal_mek` | milestone | Armadura/Pacachado de Obsidiana, Desintegrador, Jetpack abastecido. |
| `mekanism:mestre_quimico` | milestone | **Marco final** — usado pelo **Selo da Engrenagem**. |
