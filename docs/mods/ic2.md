# IndustrialCraft 2 (Experimental) — estudo do capítulo `ic2`

## Visão geral

* **Mod:** IndustrialCraft 2, `industrialcraft-2-2.2.828a-experimental.jar` (modid/namespace de
  registro `IC2`, versão `2.2.828-experimental` segundo `reg.py mods`).
* **Capítulo:** `quests/chapters/tech/ic2.yaml`, `order: 300`, 248 quests (67 main, 102 normal,
  61 optional, 13 milestone, 5 info).
* **Papel na campanha:** é o "primeiro Volume elétrico" do Registro. Começa no Ato I (resina,
  cobre, gerador, triturador) e atravessa o Ato II (aço, ligas, máquinas avançadas, rede de
  tensões, energia renovável). O endgame (reator nuclear, Matéria UU, QuantumSuit) é material do
  Ato IV e alimenta o **Selo da Engrenagem**.
* **Industrial Upgrade** (`industrialupgrade`, 3 capítulos de outro autor: `iu_base`,
  `iu_avancado`, `iu_endgame`) estende o IC2; aqui ele só é citado na quest final como próximo
  passo. Não há itens do IU neste capítulo.

Fontes usadas: registro do pack (`reg.py mod IC2`, receitas de bancada/fornalha do dump),
arquivos de receitas embutidos no JAR (`assets/ic2/config/*.ini`: `shaped_recipes.ini`,
`shapeless_recipes.ini`, `macerator.ini`, `compressor.ini`, `extractor.ini`,
`metal_former_*.ini`, `ore_washer.ini`, `thermal_centrifuge.ini`, `blast_furnace.ini`,
`block_cutter.ini`), o lang do JAR (`ic2/lang/en_US.properties` e `pt_BR.properties`), a config
do pack (`config/IC2.ini`; não existe `config/ic2/` com receitas sobrescritas, então valem as
receitas padrão do JAR) e inspeção do bytecode das máquinas com `javap` para confirmar números
(Alto-Forno, cabos, Fabricador de Massa, reator). O catálogo NEI (`reg.py handlers`) **ainda não
estava importado** quando este capítulo foi escrito.

## Energia / recursos-base

* **EU (Energy Units)**, medida em EU/t. Quatro faixas de tensão (tier):

| Tier | Tensão máx. (EU/t por pacote) | Armazenamento típico | Transformador |
| --- | --- | --- | --- |
| LV (1) | 32 | Caixa de Baterias / BatBox (40 000 EU) | LV-Transformador (LV ↔ MV) |
| MV (2) | 128 | CESU (300 000 EU) | MV-Transformador (MV ↔ HV) |
| HV (3) | 512 | MFE (4 000 000 EU) | HV-Transformador (HV ↔ EV) |
| EV (4) | 2048 | MFSU (40 000 000 EU) | EV-Transformador (EV ↔ 8192) |

  Capacidades conferidas no NBT do registro (`energy:40000`, `300000`, `4000000`, `4.0E7`).
* **Sobretensão:** uma máquina que recebe um pacote acima do seu tier **explode**. As máquinas
  básicas são LV (32 EU/t); o Upgrade de Transformador sobe o tier de entrada. Não há opção em
  `IC2.ini` para desligar explosões de máquinas (só limites de potência para Nuke = 60 e reator
  = 45, com TNT = 4).
* **Cabos** (bytecode de `TileEntityCable`: capacidade em EU/t e perda em EU por bloco):

| Cabo (meta de `IC2:itemCable`) | Capacidade | Perda/bloco |
| --- | --- | --- |
| Estanho (10) / Estanho isolado (13) | 32 | 0,2 / 0,025 |
| Cobre sem isolamento (1) / Cobre isolado (0) | 128 | 0,3 / 0,2 |
| Ouro (2) / Ouro isolado (3) | 512 | 0,5 / 0,45 |
| HV (5) / HV isolado (6) | 2048 | 1,0 / 0,95 |
| Fibra de vidro (9) | 8192 | 0,025 |
| Detector (11) / Separador (12) | 8192 | 0,5 |

  Pacote acima da capacidade derrete o cabo; cabo sem isolamento dá choque.
* **Calor (hU)** e **energia cinética (kU)** — sistemas novos da Experimental:
  * Geradores de Calor (sólidos, líquidos, radioisótopos, elétrico) emitem hU pela face
    frontal para quem está encostado (Alto-Forno, Stirling, Gerador de Vapor, Fermentador…).
  * Geradores Cinéticos (vento, água, vapor, Stirling, elétrico, manual) emitem kU para o
    **Gerador Cinético de Energia**, que converte **4 kU → 1 EU** (fator 0,25 no bytecode).
  * **Gerador Stirling**: 2 hU → 1 EU (fator 0,5).
* Geradores elétricos: Gerador 10 EU/t (carvão = 4000 EU), Geotérmico 20 EU/t (lava), Painel
  Solar 1 EU/t de dia, Semifluido (biogás, óleo, combustíveis), RTG (pelotas de plutônio),
  Reator Nuclear. **Moinho de água (`blockGenerator:2`) e Moinho de vento (`blockGenerator:4`)
  existem no registro mas não têm receita** (substituídos pelos cinéticos — a config os marca
  como "Deprecated"); o capítulo não os pede.

## Progressão (ids reais)

1. **Borracha:** Madeira de Borracha `IC2:blockRubWood` → Torneira de Resina `IC2:itemTreetap`
   → Resina `IC2:itemHarz` → Borracha `IC2:itemRubber` (fornalha 1:1, Extrator 1:3).
2. **Metais:** cobre/estanho/chumbo (`IC2:itemIngot:0/1/5`), Martelo de Forja
   `IC2:itemToolForgeHammer` (placas e invólucros à mão), Alicate `IC2:itemToolCutter` (cabos à mão).
3. **Primeira energia:** Bateria Reutilizável `IC2:itemBatREDischarged`, Carcaça da Máquina
   Básica `IC2:blockMachine`, Fornalha de Ferro `IC2:blockMachine:1`, Gerador `IC2:blockGenerator`,
   Circuito Eletrônico `IC2:itemPartCircuit`.
4. **Máquinas básicas:** Triturador `blockMachine:3`, Extrator `:4`, Compressor `:5`, Fornalha
   Elétrica `:2`, BatBox `IC2:blockElectric`. Bronze sai de pó (3 pó de cobre + 1 pó de estanho).
5. **Aço e ligas:** Formador de Metal `blockMachine2:4` (bobina, motor, Caixa de Ferramentas),
   Célula Vazia `IC2:itemCellEmpty` (só pelo Formador de Metal — extrusão de placa de estanho),
   Célula de Ar Comprimido `itemCellEmpty:5` (Compressor), Gerador de Calor por Sólidos
   `blockHeatGenerator`, **Fornalha Industrial (Alto-Forno)** `blockMachine3:1` → Ferro Refinado
   `IC2:itemIngot:3` (oredict `ingotSteel`). Liga de Metais `itemIngot:4` → Liga Avançada
   `itemPartAlloy` (Compressor). Pó de Carvão → Fibra → Malha → Placa de Carbono `itemPartCarbonPlate`.
6. **Avançado:** Circuito Avançado `itemPartCircuitAdv`, Carcaça Avançada `blockMachine:12`
   (Liga Avançada + Placa de Carbono + Placa de Ferro Enriquecido), Torno `blockMachine3:8`,
   Laser de Mineração, Centrifugador Térmico `blockMachine2:3`, Lavadora `blockMachine2:5`.
7. **Rede:** Bateria Avançada → CESU; Pó de Diamante → Pó de Energium → Cristal de Energia
   (Compressor) → MFE; Cristal Lapotron → MFSU; transformadores LV/MV/HV/EV.
8. **Renováveis e calor/cinética**, **ferramentas elétricas**, **armaduras** (NanoSuit →
   QuantumSuit), **nuclear** (urânio, MOX, componentes, reator de fluido) e **Matéria UU**.

## Materiais e geração de mundo (`config/IC2.ini` + bytecode de `IC2.generate`)

Todos ativos (`copperOre`, `tinOre`, `uraniumOre`, `leadOre`, `rubberTree = true`,
`oreDensityFactor = 1.0`). Faixas de altura (com nível do mar padrão 64):

* **Cobre** — ~15 veios/chunk de até 10 blocos, Y ≈ 10–68 (mais comum no meio).
* **Estanho** — ~25 veios/chunk de até 6 blocos, Y 0–39.
* **Urânio** — ~20 tentativas/chunk, veios de até 3 blocos, Y 0–63.
* **Chumbo** — ~8 tentativas/chunk, veios de até 4 blocos, Y 0–63.
* **Árvores de Borracha** — geradas por bioma (florestas, pântanos, selvas).
* Irídio **não** gera no mundo pelo IC2: vem do Replicador (Matéria UU); no pack há uma receita
  de RotaryCraft que converte um item dele em `IC2:itemOreIridium`.
* O oredict `ingotUranium` (aceito no lugar do U-238 nas receitas de combustível) inclui o
  lingote de Yellorium do Big Reactors e outros urânios do pack.

## Máquinas, estruturas e multiblocos

* **Processamento de minério (≈2,2×):** Triturador (1 minério → 2 minérios triturados) →
  Máquina de Lavagem de Minérios (1000 mB de água: purificado + 2 pequenos montes + pó de
  pedra) → Centrifugador Térmico (aquece com EU; purificado → pó + subproduto). Ferro rende
  ~2,4 lingotes + ouro em pequenos montes. **O IC2 sozinho não faz 3×**; 3×+ fica para Mekanism /
  Industrial Upgrade.
* **Fornalha Industrial (Alto-Forno):** precisa de calor encostado na face frontal até 50 000 hU,
  6 Células de Ar Comprimido por lingote (devolve as células vazias), 6000 ticks (5 min) por
  lingote; sinal de redstone mantém o calor. Entradas: ferro, minério/pó/triturado de ferro.
  Saída: Ferro Refinado + Escória.
* **Formador de Metal** (3 modos: laminação, corte, extrusão): placas, invólucros, cabos, Células
  Vazias, Barra de Combustível Vazia, eixos de ferro/aço, Latas de Estanho.
* **Torno** + Ferramenta de Torno + Bloco de Torneamento de Ferro: perfis de 5 segmentos
  (lang: Haste/Handle 4-3-2-2-2, Broca/Drill 5-4-3-2-1, Eixo Canelado 4-3-4-3-4, Eixo Canelado
  Fino 3-2-3-2-3). Peças exigidas por: Broca de Diamante (Drill), Laser de Mineração (Handle),
  Gerador de Água Cinético (Eixo Canelado), Gerador Stirling Cinético (Eixo Canelado Fino).
  Há handler NEI do Torno.
* **Fabricador de Massa:** 1 000 000 EU por 1 mB de Matéria UU (`uuEnergyFactor = 1.0`);
  sucata (5000) e caixa de sucata (45000) no amplificador fazem cada EU valer até 6.
* **Examinador + Padrão de Armazenamento + Replicador:** escaneia, guarda e replica itens com UU.
* **Reator Nuclear:** núcleo `blockGenerator:5` + até 6 Câmaras `blockReactorChamber`.
  Calor ≥40% incendeia, ≥50% evapora água, ≥70% fere quem está perto, ≥85% derrete blocos,
  100% explode (potência limitada a 45 pela config). **Reator de Fluido:** cubo 5×5×5 de Cubas
  do Reator `blockreactorvessel` (interior 3×3×3 com o núcleo e as câmaras), com Portas de Fluido,
  Escotilha de Acesso e Porta de Redstone. Em modo EU a saída é reduzida a 50% (lang); em modo
  fluido o reator aquece Fluido Refrigerante → Trocador de Calor Líquido → hU.
* **Refrigerante:** Máquina de Conservas (modo enriquecer) com água ou água destilada + Pó de
  Lápis-Lazúli.
* Terraformer + TFBPs, Teletransportador, Bobina de Tesla, Minerador/Minerador Avançado,
  Bomba, Magnetizador, plantações (Crop-Matron, Colheitadeira), Cofre Pessoal/Trade-O-Mat/
  Energy-O-Mat.

## Integrações no pack

* Oredict amplo: `ingotSteel` aceita aço do IE/Mekanism/TConstruct; `ingotBronze`, `ingotCopper`,
  `itemRubber` (MFR) e `circuitBasic` (Mekanism, Eln) também valem nas receitas do IC2
  (`requireIc2Circuits = false`).
* `ingotUranium` aceita Yellorium (Big Reactors) nas receitas de combustível.
* Industrial Upgrade usa o IC2 como base e continua a progressão.

## Armadilhas e dicas

* **Chave Inglesa:** clicar numa face a torna a frente (com Shift, o lado oposto); clicar na face
  que já é a frente desmonta a máquina (conferido no bytecode de `ItemToolWrench`).
* Quebre máquinas com a **Chave Inglesa** (`ignoreWrenchRequirement = false`); bancos de
  energia guardam 80% da carga ao serem desmontados (`energyRetainedInStorageBlockDrops = 0.8`).
* Sobretensão explode máquinas; confira tiers antes de ligar MFE/MFSU direto em máquinas LV.
* Itens radioativos (U-238, U-235, combustível enriquecido, MOX, plutônio, pelotas RTG, barras
  esgotadas) causam radiação: use o Traje Hazmat completo e a Caixa de Contenção.
* Reator: planeje o layout antes (simuladores de reator da 1.7.10) e teste com pouca barra.
* O Minerador só aceita baterias de tier 1 (`minerDischargeTier = 1`).
* Botas Estáticas e Capacete Solar carregam o item vestido no **peito** (BatPack, Nano/Quantum).
* Lavadora: subprodutos em pequenos montes são do mesmo metal, exceto urânio (→ chumbo) e
  chumbo (→ 3 de enxofre).
* Erros de tradução pt_BR do jogo: o Bloco de Cobre aparece como "Bloco de bronze" e o Pó de
  Estanho como "Pó de ferro".

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `ic2:primeira_fabrica` | milestone | Gerador + máquinas básicas + BatBox (Ato I). |
| `ic2:ligas_industria` | milestone | Ferro Refinado, Liga Avançada, Placa de Carbono (entrada no Ato II). |
| `ic2:rede_eletrica` | milestone | CESU, MFE e transformadores: domínio das tensões. |
| `ic2:energia_renovavel` | milestone | Solar, geotérmica e cinética funcionando. |
| `ic2:processamento_completo` | milestone | Triturador → Lavadora → Centrifugador Térmico. |
| `ic2:arsenal_eletrico` | milestone | Ferramentas elétricas e scanners. |
| `ic2:mfsu` | milestone | MFSU (40 M EU, EV). |
| `ic2:nano_completo` | milestone | NanoSuit completa. |
| `ic2:reator_nuclear` | milestone | Primeiro reator nuclear funcionando. |
| `ic2:reator_fluido` | milestone | Reator de fluido montado. |
| `ic2:materia_uu` | milestone | Replicador de Matéria UU. |
| `ic2:quantum_completo` | milestone | QuantumSuit completa. |
| `ic2:mestre_industrial` | milestone | **Marco final** (Quantum + Reator + Replicador + MFSU) — usado pelo **Selo da Engrenagem**. |
