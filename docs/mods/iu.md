# Industrial Upgrade — estudo dos capítulos `iu_base`, `iu_avancado`, `iu_endgame`

## Visão geral

* **Mod:** Industrial Upgrade 2.5.21 (`IndustrialUpgrade-2.5.21.jar`, modid/namespace
  `industrialupgrade`, autores Denfop/ijo42). Extensão do IC2 Experimental 2.2.828 (descende do
  "SuperSolarPanels"). ~1100 variantes de item no registro.
* **Sem tradução pt_BR** no JAR (só `en_US.lang` e `ru_RU.lang`): o jogo mostra os nomes em
  inglês. As quests citam o nome em inglês + explicação em português.
* **Capítulos** (`order` 302/303/304):
  * `tech/iu_base.yaml` — **Industrial Upgrade I — Fundamentos** (151 quests): metais, metalurgia,
    ligas, calor, Circuito Nano, máquinas Melhoradas, armazenamento do IU, Sunnarium,
    Enriquecedor, Transformador Molecular, Painel Solar Avançado.
  * `tech/iu_avancado.yaml` — **Industrial Upgrade II — Avançado** (153 quests): Fóton, tier
    Quântico, máquinas Avançadas, petróleo/plástico, módulos, Transformador Molecular Avançado,
    Estação de Modificação, MFSU Melhorado/Avançado, cabos e transformadores, NanoSuit
    Reforçada, ferramentas Quânticas, reatores do IU, painéis do Botania, tier Espectral
    (máquinas Perfeitas, Painel Espectral).
  * `tech/iu_endgame.yaml` — **Industrial Upgrade III — Endgame** (128 quests): Protônico → Quark,
    nêutron, Reator de Fusão e transurânicos, MFSU Perfeito → Quark, arsenal Espectral,
    Pedreira Quântica, Matéria Sólida, módulos avançados, Avaritia.
* **Papel na campanha:** Ato II (I e II) e Ato IV (III, alimenta o **Selo da Engrenagem**).
* **Divisão por tier real do mod:** I = tier Nano (Caixa Nano + Circuito Nano, máquinas
  "Improved", 2 entradas) até o Transformador Molecular; II = tier Quântico (Caixa Quântica +
  Circuito Quântico, máquinas "Advanced", 3 entradas) e tier Espectral (Caixa Quântica Melhorada +
  Circuito Espectral, máquinas "Perfect", 4 entradas) até o Painel Espectral; III = do Núcleo
  Protônico ao Quark.
* **Fontes:** registro do pack (`reg.py mod industrialupgrade`), catálogo NEI do pack (`reg.py
  nei/neiout`: handlers "Shaped IC2 Crafting", "Molecular Transformer", "Circuit Manufacturer",
  "Alloy Manufacturer", "Enrichment", "Nuclear Fusion Reactor", "Mineral Separator", etc.),
  descompilação do JAR com CFR (`BasicRecipe`, `CommonProxy`, inits das tiles, `EnumSolarPanels`,
  `EnumElectricBlock`, `EnumMultiMachine`, `GenOre`, `TileEntityQuarryVein`...), `en_US.lang`
  (achievements e textos de interface) e `config/industrialupgrade.cfg` do pack.

## Energia / recursos-base

* **EU** do IC2 (mesmos tiers: 32·4^(t−1)). Máquinas multi do IU entram em **tier 1 (LV)** por
  padrão (aceitam os upgrades do IC2); a config mantém **explosões ligadas**
  (`Enable explosion from mechanisms = true`). O Transformador Molecular aceita qualquer tensão
  (tier 14). Geradores de Matéria: tier 3.
* **RF:** as máquinas multi só aceitam RF com o **Módulo RF** (`module7:4`) instalado (clique
  direito); armazenamentos/painéis do IU guardam RF com o módulo. `coefficient rf = 4`.
* **Temperatura:** Fabricante de Circuitos (4500°) e Separador de Minerais (1500–5000°)
  precisam de um **Aquecedor** encostado (Elétrico `BlockBaseMachine4:5`: 50 EU por +5°;
  de Líquido `:6`: lava, tanque 12 000 mB). Máx. 5000°.
* **Armazenamento (`electricblock`):** BatBox(2) 40k, CESU(5) 300k, MFE(3) 4M, MFSU(4) 40M (tiers
  1–4, como o IC2, com slots de módulo e RF); MFSU Melhorado(0) 100M t5; Avançado(1) 400M t6;
  Perfeito(6) 1,6G t7; Bárion(7) 6,4G t8; Hádron(8) 25,6G t9; Gráviton(9) 102G t10; Quark(10) 409G
  t11. Kits `upgradekitIU` sobem o bloco no lugar sem perder energia (mapa: CESU→MFE→MFSU→…).
* **Cabos (`cable`, perda 0,025):** Espectral 32 768, Protônico 131 072, Singular 524 288, Mítico
  2,1M, Quântico 8,4M, Fotônico 33,5M, Nêutron 134M, Perfeito 537M, Universe 8,6G, Difrativo
  440G, Infinito 1,76T EU/t.
* **Transformadores (`IUTransformer`):** UMV t5, UHV t6, UEV t7, UMH t8, UMEV t9, UHEV t10, HEEV t11.

## Progressão (ids reais)

1. **Metais:** `iuingot:0..18` (Michalov, Alumínio, Vanádio, Tungstênio, Invar, Caravky, Cobalto,
   Magnésio, Níquel, Platina, Titânio, Cromo, Espinélio, Electrum, Prata, Zinco, Manganês, Irídio,
   Germânio). Cada um tem pó, montinho, pepita, placa (`iuplate`), placa densa (`IUdoubleplate`,
   9 placas no Compressor), invólucro (`iucasing`), vareta (`IUstik`) e bloco.
2. **Carbono:** `compresscarbon` (9 Placas de Carbono), `compresscarbonultra` (9 Ligas Avançadas),
   `coal_chunk` (9 fibras). **Caixa Nano** `nanobox` (carbono + Cristal de Energia).
3. **Ligas** (`alloysingot:0..9`): Fabricante de Ligas `machines:4` (Alumel, Latão Vermelho,
   Nicromo, Duralumínio, Ferromanganês, Invar, Electrum, Ferro Refinado) e Fabricante de Ligas
   Melhorado `BlockBaseMachine2:3` (Bronze de Alumínio, Muntz, Alclad, Vanádio-Alumínio, Vitálio).
4. **Circuitos** (Fabricante de Circuitos `machines:6`, 4500°): capacitores/SOIC/QFP
   (`basecircuit:0..8`) → Nano `QuantumItems9`, Quântico `QuantumItems8`, Espectral
   `QuantumItems5`. Capacitor Nano exige cromo e zinco **do IU**; Capacitor Espectral exige
   germânio, Michalov e Caravky **do IU**.
5. **Caixas:** Nano `nanobox` → Nano Avançada `QuantumItems7` (4 Fótons) → Quântica `QuantumItems6`
   → Quântica Melhorada `QuantumItems3`. Placas de irídio comprimidas `QuantumItems2` (4 Placas
   Reforçadas) e `QuantumItems4` (9 das anteriores).
6. **Máquinas multi** (`machines_base`, `machines_base1`, `machines_base2`, `machines_base3`):
   Improved/Advanced/Perfect = 2/3/4 entradas paralelas. Receitas: Melhorada (Alumel/Alumínio
   densos, Circuitos Nano, Caixas Nano), Avançada (Vitálio/Platina densos, Quânticos, Caixas
   Quânticas), Perfeita (Cobalto ou Manganês/Espinélio densos, Espectrais, Caixas Quânticas
   Melhoradas). Kits `upgradekitMachineIU:0..2` convertem no lugar (o Kit Melhorado funciona em
   Triturador, Compressor, Extrator, Fornalha Elétrica, Formador de Metal e Reciclador do IC2).
   Módulos de máquina (um por máquina): RF, Pilha (`module_stack`), Instantâneo
   (`module_quickly`, 100× energia); e Módulo de Painel (`module6:*`) dá geração solar própria.
   Upgrades: Overclocker Melhorado (`upgrademodule:0`, ×0,5 tempo/×1,3 energia), Avançado (`:1`,
   ×0,4/×1,2), Transformador x2/x4 (`:2/:3`, +2/+4 tiers).
7. **Sunnarium:** Gerador de Energia Solar `generator_se` (de dia, 2500 → 1 fragmento;
   Melhorado `advgenerator_se` ×2, Avançado `impgenerator_se` ×4) → `sunnarium:4` → Fabricante
   de Sunnarium (4 fragmentos + glowstone + quartzo + **tungstênio do IU**) → `sunnarium:3` →
   Compressor → `sunnarium:2`. Enriquecedor `BlockBaseMachine2:10`: urânio do IU
   (`ssp_crafting_items:2`, Compressor de urânio) + bloco de glowstone → `ssp:0`; + Vidro
   Reforçado → 2 `ssp:1`; Sunnarium + `ssp:0` → `sunnarium:0`. Fabricante de Placas
   `SunnariumPanelMaker`: `sunnarium:1` e a escada `sunnariumpanel:0..12`
   (Pt, Michalov, Cr, Electrum, Mg, Zn, Mn, Co, Ni, Ag, V, Vitálio, Caravky).
8. **Transformador Molecular** `molecular` (4 Carcaças Avançadas, 2 Transformadores EV, 2
   Circuitos Avançados, núcleo `ssp:3`). Núcleos `ItemCore:0..13` (4→1): 1500, 11 720, 60k,
   300k, 1,5M, 7,5M, 45M, 180M, 900M, 2,7G, 4,5G, 9G, 12G, 21G EU. Transmutações (config):
   ferro → minério de irídio IC2 (10M), → Fóton (1,45M), → Lingote de Fóton (12M); magnésio →
   Caravky (900k) → germânio (600k); espinélio → irídio (2,5M); plutônio → Próton (15,5M);
   crânio → crânio de wither (4M) → estrela do Nether (250M), etc.
9. **Painéis** (`BlockSolarPanel:0..13`): vidro (`IUphotonglass`) + núcleo + placas de irídio +
   **3 painéis do tier anterior** + circuito (do Bárion em diante, 2 núcleos). Geração de dia
   (config): 5, 20, 80, 320, 1280, 5120, 20 480, 81 920, 327 680, 1 325 720, 5 302 880,
   21 211 520, 84 846 080, 339 384 320 EU/t. Kits `upgradekitPanelIU:1..` sobem painel instalado.
   Painéis sujam (−25% por estágio) → Purificador `Purifier` ou Módulo Purificador `module7:9`;
   desmontar com chave inglesa.
10. **Plástico (II):** Sonda `BlockQuarryOil` (analisa o chunk; 90% depósito de petróleo — melhor
    em desertos/oceanos —, 10% veio de minério pesado) → Pumpjack `BlockOilGetter` (10 EU/mB) →
    Refinaria `OilRefiner` (600 gasolina + 400 diesel) / Avançada `OilAdvRefiner` (500
    polietileno + 500 polipropileno) → Fábrica Química `BlockBaseMachine3:11` (+1000 mB água) →
    Plástico; Eletrolisador `:15` (água → 500 H2 + 250 O2) → Máquina de Placas `:13` (+1000 mB
    O2) → `plastic_plate` → Modelo de Módulo `module_schedule` (+ Caravky).
11. **Endgame (III):** Gerador de Partículas de Nêutron `machines:5` (16,25M EU/mB) → `cell:1` →
    `neutronium` (Compressor) → `neutroniumingot` (9). Reator de Fusão `BlockBaseMachine2:11`
    (síntese com chance: Cm→Cf 20%, →Bk 18%, →Es 16%, →Md 14%; Np+Th→Pu 14%; bloco de U+Th→U-233
    16%).

## Materiais e geração de mundo (`GenOre` + config)

* Overworld (`Dimension List` 0 e 6; a dim. 6 não existe no pack): 15 metais básicos + manganês,
  irídio, germânio — Y 0–70, 9 tentativas/chunk, veios de 3–5. Minérios pesados (12) Y 10–70;
  tório Y 10–70 (solta Tório); gemas (rubi/safira/topázio) Y 10–70 (soltam gemas); radioativos
  (amerício, netúnio, cúrio) Y 10–70 (soltam gemas). Todos ligados na config.
* Nether (Y 20–120) e End (Y 0–128): versões de 21 minérios (`NetherOre`, `netherore1`,
  `EndOre`, `endore1`), ligados (`Spawn ores in Nether/End = true`).
* Lagos de petróleo: 30% dos chunks de deserto (bloco `blockOil_IU`).
* **Não há receita de Triturador do IC2 para os minérios do IU** (o código só substitui
  receitas já existentes; o NEI confirma). Logo `IUcrushed`/`IUpurified` não são obtidos.
  Processamento: fornalha (1:1), Pulverizador do Thermal / Triturador do IE (2 pós), etc.

## Máquinas, estruturas e multiblocos

Sem multiblocos. Destaques: Separador de Minerais `BlockBaseMachine2:12`; Fabricante de Wither
`BlockBaseMachine2:13` (3 crânios + 4 areias das almas → estrela); Estação de Modificação
`BlockUpgrade` (módulos `iu_upgrademodule:*` em ferramentas/armaduras; os módulos saem do
Transformador Molecular Avançado `doublemolecular`, 39 receitas); Reatores do IU
`BlockBaseMachine2:7/8/9` (10×6 ×1,2; 11×6 ×1,4; 11×7 ×1,8, com câmaras `AdvChamber`,
`ImpChamber`, `PerChamber`); Pedreira Quântica `machines:8` (+ 13/14/15): 200k/150k/100k/50k EU
por operação, 5% de chance de minério aleatório, precisa do Módulo Analisador; Analisador
`BlockBaseMachine3:2` + Módulo Pedreira minera o chunk real; Geradores de Matéria Sólida
`BlockSolidMatter:0..7` (50M EU/matéria; cada um leva um Transformador Molecular), Combinador,
Transformador de Matéria Sólida; Geradores de Matéria UU `machines:1/2/3` (900k/800k/700k EU/mB);
Caçador Automático `BlockBaseMachine4:4` com módulos de caçador; Combinador de Painéis
`BlockSintezator` (limite 2 por slot na config).

## Integrações no pack

* **Botania:** placas de Manasteel/Elementium/Terrasteel (Compressor), Runas do Sol/Noite/Energia
  (Altar Rúnico, 12 000 de mana), núcleos e painéis `blockBotSolarPanel:0..2` (80/320/1280 EU/t),
  Broca de Terrasteel, barras de Terrasteel.
* **Avaritia:** `neutroncore` (4 Neutrônio Cósmico + Núcleo Protônico), Painel de Neutrônio
  (4 Fotônicos), `infinitycore`, Painel Infinito; singularidades de 17 metais do IU no
  Compressor de Nêutrons.
* **Thaumcraft / Thaumic Tinkerer:** os painéis de Táumio, Vazio e Icório e o Gerador de Aspectos
  estão registrados, mas **não têm receita** neste pack (sem pesquisa, sem crafting) — não
  entram nas quests.
* **Draconic Evolution / `Akashic-IU-DE-Compat`:** o JAR do servidor (e o Akashic-ThePatcher no
  cliente) faz um ASM em `com.denfop.*`: troca `Loader.isModLoaded("DraconicEvolution")` por um
  teste que só retorna verdadeiro se existir a API antiga `IConfigurableItem`. O DE do pack é a
  versão GTNH sem essa API, então **a integração Draconic do IU fica desligada** (log: "Disabling
  ONLY Industrial Upgrade's legacy Draconic integration to prevent startup crash"). Painéis
  Draconic/Awakened/Chaotic e armas/armadura do Caos do IU **não existem** no registro, apesar de
  `draconic integration` estar `true` na config.
* MineFactory (receitas da Fazenda Autônoma), oredict amplo (Thermal/IE/TiC/ExtraPlanets
  aparecem como alternativas nas máquinas do IU).
* Receitas alternativas do IU para itens do IC2 (Replicador, Minerador Avançado, Jetpack
  Elétrico, Formador de Metal) — **somam-se** às originais, nenhuma é removida.

## Armadilhas e dicas

* Explosões por sobretensão ligadas; máquinas multi são LV por padrão.
* Reatores do IU: `reactor explode = false` → ao chegar a 100% o reator **e as câmaras somem**
  (com o conteúdo); efeitos de fogo/lava a partir de 85% continuam.
* Capacitores Nano/Espectral e o Sunnarium exigem lingotes **do próprio IU** (não aceitam oredict).
* **Triturador Combinado** (`machines_base1:6..9`): config diz 3 triturados/minério, mas a lista
  de receitas dele é criada vazia no código e o NEI mostra 0 receitas — provavelmente não
  funciona; o capítulo só o menciona num `info`.
* Itens sem receita no pack: `Aminpanel` (admin), painéis Thaum/Void/Icório, `thaummachines`,
  `Helium` (item; o hélio vem do Gerador de Hélio como fluido), `iu_upgrademodule:12` e `:24`.
* Painéis sujam; Purificador. Visão noturna dos capacetes Nano/Quântico do IU abaixo de Y 60 no
  escuro (config).
* Custos: Lingote de Nêutron ≈ 1,46·10¹¹ EU; Núcleo Quark 21G EU numa operação.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `iu_base:circuito_nano` | milestone | Primeiro Circuito Nano (Fabricante de Circuitos aquecido). |
| `iu_base:metalurgia_iu` | milestone | As dez ligas do IU. |
| `iu_base:fabrica_melhorada` | milestone | Máquinas Melhoradas + Overclocker Melhorado. |
| `iu_base:transformador_molecular` | milestone | Transformador Molecular montado. |
| `iu_base:painel_solar_avancado` | milestone | Primeiro painel solar do IU. |
| `iu_base:fundamentos_completos` | milestone | **Fim do Capítulo I** (Ato II). |
| `iu_avancado:painel_hibrido` | milestone | Painel Híbrido. |
| `iu_avancado:circuito_quantico` | milestone | Circuito Quântico. |
| `iu_avancado:fabrica_avancada` | milestone | Máquinas Avançadas. |
| `iu_avancado:painel_quantico` | milestone | Painel Quântico (320 EU/t). |
| `iu_avancado:placa_plastico` | milestone | Cadeia petroquímica completa. |
| `iu_avancado:transformador_molecular_avancado` | milestone | Transformador Molecular Avançado. |
| `iu_avancado:nano_reforcada` | milestone | NanoSuit Reforçada completa. |
| `iu_avancado:circuito_espectral` | milestone | Circuito Espectral. |
| `iu_avancado:painel_espectral` | milestone | Painel Espectral (1280 EU/t). |
| `iu_avancado:avancado_completo` | milestone | **Fim do Capítulo II**. |
| `iu_endgame:painel_protonico` … `iu_endgame:painel_graviton` | milestone | Cada degrau solar (Protônico, Singular, Difrativo, Fotônico, Nêutron, Bárion, Hádron, Gráviton). |
| `iu_endgame:armadura_quantica_espectral` | milestone | Armadura Quântica Espectral completa. |
| `iu_endgame:painel_quark` | milestone | Painel Quark (339 M EU/t). |
| `iu_endgame:mestre_melhorias` | milestone | **Marco final do IU** (Quark + MFSU Quark + armadura + fusão + pedreira + Broca Perfeita) — para o **Selo da Engrenagem**. |
