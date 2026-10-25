# Apicultura (Forestry, MagicBees, Gendustry, ExtraBees, ATM) — estudo do capítulo `abelhas`

## Visão geral

* **Mods:** Forestry 4.11.36 (apicultura), MagicBees 2.10.12-GTNH (`MagicBees`), Gendustry
  1.9.12-GTNH (`gendustry`), ExtraBees de binnie-mods 2.6.43 (`ExtraBees`; o resto do Binnie é
  do capítulo `binnie`), BeeBetterAtBees 0.4.7-GTNH (addon só de NEI: árvore de mutações; não
  mostra mutações secretas, `shouldShowSecretMutations=false`) e as abelhas metálicas da Família
  ATM (`allthemodiumlegacy`, `enableForestryBees=true`).
* **Capítulo:** `quests/chapters/vida/abelhas.yaml`, `order: 605`, **263 quests**.
* **Papel:** Ato II (colmeias, Apiário, linhas Nobre/Industrial) → Ato III/IV (Apiaryum, MagicBees,
  ExtraBees, Gendustry). Marco final `abelhas:selo_da_vida` = **Selo da Vida**. As abelhas ATM
  são um ramo opcional do Ato V (não bloqueiam o Selo).
* Pontes com o capítulo `forestry` via `- link:` e dependências cruzadas: `forestry:centrifuga`,
  `forestry:carpinteiro`, `forestry:involucro_impregnado`, `forestry:mestre_florestal`.

Fontes: registro, NEI (Bee Breeding 412 mutações, Bee Produce, Centrífuga, Carpinteiro, Mutatron
etc.), código descompilado (Forestry `BeeDefinition`, `HiveDescription`, `PluginApiculture`,
`VillageHandlerApiculture`, `AlvearyController`; MagicBees `BeeMutation`/`BeeSpecies`/`HiveType`;
ExtraBees `ExtraBeeDefinition`/`ModuleGeneration`; ATM `ForestryBeeIntegration`/`BeeProgression`),
configs do Gendustry embutidas no JAR (`tuning.cfg`, `recipes.cfg`, `upgrades.cfg`,
`bees_color.cfg`; a pasta `config/gendustry/overrides` do pack está vazia) e lang dos JARs.

## Energia / recursos-base

* Forestry: Analisador, Ventilador e Aquecedor do Apiaryum usam RF; analisadores portáteis
  gastam Gota de Mel ou Melaço por análise; o Analisador-bloco gasta 100 mB de mel + energia.
* Gendustry: aceita **RF** e EU (sink tier 2); conta em MJ (10 RF = 1 MJ, 5 EU = 2 MJ). Apiário
  Industrial 2 MJ/t base. Mutatron: 1000 mB mutagênico + vidraria por operação; Replicador:
  1000 mB DNA + 5000 mB proteína por abelha (sai não-imaculada).
* Mutagênico: redstone 100, bloco 900, glowstone 200/800, urânio IC2, yellorium. Proteína: porco
  500, boi 450, frango 350, peixe 250. DNA: princesa 500, rainha 600, zangão 100, muda 100, pólen 400.

## Progressão (ids reais)

1. Puça `Forestry:scoop` → colmeias `Forestry:beehives:1–7` (sem meta 5 no registro; colmeia do
   Fim só gera no Fim) → Colméia `apiculture:2` (**modificador de mutação 0: não muta**) →
   Abelhanalizador `beealyzer` → Apiário `apiculture:0` (3 quadros) → quadros
   `frameUntreated/Impregnated/Proven` (80/240/720 usos).
2. Linha Honey/Noble/Industrious, Agrarian, Boggy, Monastic, Heroic, Infernal, Austere, Tropical,
   Frozen, End, Vengeful, Festive (tabela abaixo).
3. Apiaryum `Forestry:alveary` (Painéis Perfumados `craftingMaterial:6`) + blocos 2–7 e os 7
   blocos do ExtraBees (`ExtraBees:alveary:0–6`).
4. ExtraBees (hives Water/Rock/Nether, linha mineral → metais/gemas), MagicBees (6 hives, linhas
   arcanas, taumáticas, metálicas), Gendustry (máquinas), ATM.

### Mutações Forestry (código `BeeDefinition`; NEI confirma)

Comum: duas abelhas de colmeia diferentes 15% · Cultivada: Comum × colmeia 12% · Nobre e Diligente:
Comum × Cultivada 10% · Majestosa: Nobre × Cultivada 8% · Imperial: Nobre × Majestosa 8% ·
Incansável: Diligente × Cultivada 8% · Industrial: Diligente × Incansável 8% · Heróica: Inabalável ×
Valente 6% (Floresta) · Sinistra: Cultivada × Modesta/Tropical 60% (Nether) · Diabólica: Sinistra ×
Cultivada/Modesta/Tropical 40% (Nether) · Demoníaca: Sinistra × Diabólica 25% (Nether) · Frugal:
Modesta × Sinistra 16% / × Diabólica 10% (quente+seco) · Austera: Modesta × Frugal 8% · Exótica:
Austera × Tropical 12% · Edênica: Exótica × Tropical 8% · Espectral: Eremítica × Ender 4% ·
Fantasmagórica: Espectral × Ender 2% · Gelada: Industrial × Invernal 12% (frio) · Glacial: Gelada ×
Invernal 8% · Rural: Prados × Diligente 12% (Planícies) · Fazendeira: Rural × Incansável 10% ·
Agrária: Fazendeira × Industrial 6% · Lamacenta: Pantanal × Nobre 15% (quente+úmido) · Alagadiça:
Pantanal × Lamacenta 9% · Solitária: Monástica × Austera 12% · Eremítica: Monástica × Solitária 8% ·
Retaliadora/Vingativa (secretas), Vingadora · Festivas por data (Leporina, Alegre, Embriagada,
Enganadora). Sem mutação: Inabalável (baús de masmorra; hives MagicBees), Valente (3% das hives),
Monástica (aldeão: princesa + 12 esmeraldas), Ender (hive do Fim; aldeão: princesa + 16 pérolas).

### Produtos-chave (Centrífuga)

Favo de Mel → cera + gota · Gotejante → melaço · Fibroso → própolis (própolis → resina IC2 neste
pack) · Cozido → cera em pó + 2 fósforos · Sedoso → própolis sedoso → fio de seda (60%) → Tecido
(9 fios + 500 água) · Misterioso → própolis pulsante → Malha Pulsante → Pérola do Fim (Carpinteiro)
· Suave → melaço + quartzo · Congelado → neve, pólen cristalino · Em Pó → pólvora · Trigo → trigo.
Imperial: Geléia Real; Industrial: Conjunto de Pólem; Alagadiça: Turfa; Demoníaca: glowstone.

## Materiais e geração de mundo

* Forestry (`HiveDescription`): Floresta (árvores), Prados (chão, terra/grama), Modesta (deserto),
  Tropical (árvores da selva), Fim (pedra do Fim), Invernal (neve), Alagadiço (pântano). Quantidade
  `beehives.amount=1.0`. Aldeias com casa de abelhas e aldeão apicultor (`villagers=true`).
* ExtraBees (`ExtraBees.cfg`): Water (fundo de água, Y 20–70, rate 1), Rock (dentro da pedra, Y
  20–70, rate 2), Nether (rocha do Nether, rate 2); 3% de Valente em cada.
* MagicBees (`HiveDescription`/`HiveType`): Curiosa (árvores; floresta/planície/colina) → Mystical;
  Incomum (chão; planície/montanha/colina/rio) → Unusual; Ressonante (areia; deserto/mesa/quente/
  mágico) → Sorcerous; Profunda (subsolo, com redstone) → Attuned; Infernal (Nether; raro no
  subsolo de biomas mágicos quentes) → Infernal (+3% Inabalável); Esquecimento (Fim, pilar de
  obsidiana) → Oblivion (+9% Inabalável).

## Máquinas, estruturas e multiblocos

* **Apiaryum** (`AlvearyController`): 3×3×3; camada de cima e interior só blocos comuns; 9 lajes de
  madeira (metade de baixo) por cima; blocos ao redor da camada de cima livres. Território ×2.
  Especiais: Enxame (geléia real → enxames com princesa artificial), Ventilador, Aquecedor,
  Hidroregulador, Estabilizador (mutação 0), Peneira (pólen, Tecido de Seda).
* **Gendustry:** Mutagen Producer, Mutatron (garante a mutação; 10% de morte p/ princesa
  artificial; 10% de escolher mutação secreta), Advanced Mutatron (escolhe a mutação), Industrial
  Apiary (4 upgrades: produção +20%/upgrade até 8, automação, estabilizador, clima ±0,25,
  emulações de bioma, selagem/luz/céu aberto, peneira, filtro de pólen), DNA Extractor, Protein
  Liquifier, Genetic Sampler (gene aleatório), Genetic Template (bancada), Imprinter, Transposer,
  Replicator. Genetics Processor usa Chipset Pulsante do BuildCraft (BC presente).
* **Abelhas ATM** (`BeeProgression`): Allthemodium = Imperial × Industrial puras, 3%; Vibranium =
  Allthemodium × Austera, 2%; Unobtainium = Vibranium × Ender, 1%. Exigem Apiaryum completo (27
  partes) sobre 3×3 do bloco do metal; Allthemodium: The Other (241), bioma The Other, Y ≥ 64, céu
  aberto, WARM/ARID; Vibranium: The Other, Warped Forest, Y 16–64, sem céu, HOT/ARID; Unobtainium:
  The Beyond (242), Y ≥ 64, ICY/ARID. Produção só com espécie e gene de flor puros (flor = bloco do
  metal). Favo → resíduo (Centrífuga, 100%); 9 resíduos → pepita; 81 favos/lingote.

## Integrações no pack

* MagicBees + Thaumcraft (aba Apimancia `MB_*`), Botania (Rooted/Somnolent/Dreaming, Mana
  booster), Thermal (TE*), AE2, Ars Magica (AM*) — todos ativos em `MagicBees.cfg`.
* Metais MagicBees exigem bloco sob a colmeia (ferro, cobre, estanho, chumbo, prata, ouro,
  diamante, esmeralda, apatita).
* Grãos do ExtraBees: 4 grãos → 1 pó (ex.: `ExtraBees:misc:6` → pó de ferro Thermal).

## Armadilhas e dicas

* **Tarefas de espécie pedem abelha pura** (UID0 = UID1). Primeiro filhote mutante costuma ser
  híbrido. Não use NBT só com UID0: o Forestry quebra ao montar o genoma (NPE em `getChromosomes`).
* Colméia (Bee House) **não gera mutação**; use Apiário/Apiaryum.
* Abelhas Ender e derivadas usam **Ovo de Dragão** como flor (`flowersend`).
* Monástica só fica feliz isolada. Sinistra/Austera/Demoníaca têm efeitos hostis → Roupa de Apicultor.
* Favos Irradiados (linha Vingativa) não dão nada na Centrífuga.
* Nomes de espécies do MagicBees, ExtraBees e Gendustry não têm pt_BR (aparecem em inglês).

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `abelhas:primeiro_apiario` | milestone | Apiário, Abelhanalizador e quadros (Ato II). |
| `abelhas:nobreza_industria` | milestone | Imperial + Industrial puras (geléia real e pólen). |
| `abelhas:apiaryum_montado` | milestone | Apiaryum 3×3×3 formado. |
| `abelhas:mineracao_apicola` | milestone | Metais e diamante do ExtraBees. |
| `abelhas:apimancia` | milestone | MagicBees: Ethereal, Arcane, Supernatural e Apiário Mágico. |
| `abelhas:g_replicador` | milestone | Gendustry: Replicador Genético. |
| `abelhas:selo_da_vida` | milestone | **Marco final — Selo da Vida** (depende também de `forestry:mestre_florestal`). |
