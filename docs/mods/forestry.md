# Forestry — estudo do capítulo `forestry`

## Visão geral

* **Mod:** Forestry `Forestry-4.11.36.jar` (namespace `Forestry`). Módulos todos ativos
  (`config/forestry/modules.cfg`); modo de jogo `EASY` (`common.cfg` → `gamemodes/EASY.cfg`).
* **Capítulo:** `quests/chapters/vida/forestry.yaml`, `order: 600`, **147 quests**.
* **Papel na campanha:** começa no Ato I (apatita, turfa, bronze, motores), atravessa o Ato II
  (Carpinteiro, biomassa, Fabricador, Fazendas Multibloco, genética de árvores) e o marco final
  alimenta o **Selo da Vida** (Ato IV) junto com o capítulo `abelhas`.
* A apicultura do Forestry fica no capítulo `abelhas` (que liga de volta para
  `forestry:centrifuga`, `forestry:carpinteiro`, `forestry:involucro_impregnado` e
  `forestry:mestre_florestal`).

Fontes: registro do pack (`reg.py`), receitas de bancada/fornalha do dump, catálogo NEI
(Carpinteiro, Hidratador, Fermentador, Centrífuga, Fabricador, Tree Breeding), lang do JAR
(`for.hints.*`), config do pack e o código do JAR descompilado com CFR (receitas de máquina,
combustíveis, multibloco da Fazenda, mutações de árvores, geração de minério).

## Energia / recursos-base

* Motores geram **RF/t** e empurram energia para a face frontal (girar com a Chave Inglesa);
  motores de combustível precisam de sinal de redstone; superaquecimento → resfriamento forçado
  com perda de energia (`for.hints.forcedcooldown`, `cooldownloss`).
* Combustíveis (código `PluginFactory`/`PluginFluids`):
  * Motor de Turfa `Forestry:engine:1`: Turfa 10 RF/t × 5000 ticks; Turfa Betuminosa 20 RF/t × 6000.
    Gera Cinzas.
  * Motor de Biogás `Forestry:engine:2`: biomassa 50, óleo vegetal 30, mel 20, suco/água/leite 10
    RF/t. Água consome lava continuamente (hint do jogo).
  * Motor de Engrenagem `engine:4`: corda manual (clique), gasta fome; corda excessiva causa
    6 de dano.
  * Motor Elétrico `engine:0`: recebe EU (IC2, tier 3) → RF; circuitos: estanho Impulso I, bronze
    Impulso II, cobre Choque, ferro Eficiência.
  * Bio Gerador `engine:3`: etanol 32 EU/t, biomassa 8 EU/t (saída IC2).
* Máquinas com energia: Carpinteiro, Hidratador, Centrífuga, Fermentador, Refinador, Fabricador,
  Engarrafador, Analisador. **Sem energia:** Humidificador (precisa de luz ≤ 11), Fazedor de Chuva,
  Tanque de Chuva (coleta; engarrafar precisa de energia), Mesa de Trabalho.

## Progressão (ids reais)

1. Apatita `Forestry:apatite` / minério `Forestry:resources`; cobre `resources:1`; estanho
   `resources:2`; bronze `Forestry:ingotBronze` (3 Cu + 1 Sn → 4, `crafting.bronze=true`).
2. Solo: Fertilizante `fertilizerCompound` (areia+apatita+areia → 8 no EASY; cinzas → 16),
   Adubo `fertilizerBio`, Húmus `soil:0`, Terra do Pântano `soil:1` → Turfa `peat`.
3. Engrenagem de Pedra (BuildCraft `ore:gearStone`) → engrenagens Cu/Sn/Bronze; Carcaça
   Resistente `sturdyMachine` (8 bronze).
4. Motores; Hidratador `factory:5`, Carpinteiro `factory:1`, Fermentador `factory:3`, Refinador
   `factory:6`, Centrífuga `factory:2`, Humidificador `factory:4`, Engarrafador `factory:0`,
   Fabricador `factory2:0`, Tanque de Chuva `factory2:1`, Mesa de Trabalho `factory2:2`.
5. Carpinteiro: Invólucro Impregnado (8 troncos + 250 mB óleo), Vara Impregnada (2 troncos + 100 mB
   → 2), Polpa (1 tronco + 250 água → 4), Escrivaninha (500 mB óleo), placas de circuito
   (estanho/bronze/ferro/ouro + redstone + 1000 água; 1/2/3/4 soquetes), Ferro de Solda,
   Revestimento Endurecido (4 diamantes + carcaça + 5000 água), Arvoranalizador/Flutterlyzer
   (2000 água), Caixas e caixotes.
6. Fabricador: vidro líquido (vidro 1000 mB, areia 1000, arenito 4000, painel 375); válvulas =
   5 material + 2 redstone + 500 mB → 4 (cobre, estanho, bronze, ferro, ouro, diamante, obsidiana,
   blaze, esmeralda, apatita, lápis, ender; borracha via IC2); vidro colorido com Molde de Cera;
   madeira à prova de fogo (cera refratária).
7. Fazedor de Chuva `factory:7` (Revestimento Endurecido) + cargas: Cápsula de Iodo (chuva),
   Fosfórica (tempestade), Carga de Dissipação (céu limpo).
8. Fazenda Multibloco `Forestry:ffarm` (meta 0 bloco, 2 caixa de engrenagens, 3 hachura, 4 válvula,
   5 controle — NBT `FarmBlock` só muda a aparência; tarefas ignoram NBT).
9. Arboricultura: Arvoranalizador `treealyzer` (fuel: gota de mel ou melaço; converte mudas
   vanilla), Enxerto `grafter`, 29 espécies de árvore (UIDs `forestry.tree*`), frutas
   `Forestry:fruits:0..6`.
10. Borboletas `butterflyGE` (pegas com a Puça), Flutterlyzer, mochilas, correio.

## Materiais e geração de mundo (código `WorldGenerator`, config `world.generate.ore` todos true)

* **Apatita:** 1 veio de até 36 blocos em 80% dos chunks, Y ≥ 56 (até o teto do mundo − 16).
* **Cobre:** 20 veios de 6 blocos/chunk, Y 32–107.
* **Estanho:** 18 veios de 6 blocos/chunk, Y 16–91.
* Fornalha: turfa → `fossil:volcanicAsh` (alteração de outro mod no pack).

## Máquinas, estruturas e multiblocos

* **Fazenda Multibloco** (`FarmMultiblockSizeLimits`, `FarmController`): base 3×3 a 5×5, altura
  exatamente 4, mínimo 36 blocos; a 3ª camada e o interior só com Blocos da Fazenda comuns;
  exige ≥ 1 Caixa de Engrenagens. Fertilizante obrigatório (valor 2000 no EASY). Circuitos
  soldados (diagramas `farms.managed` / `farms.manual`):
  * Gerenciadas: Arboreto (cobre), Turfeira (estanho), Cereais (bronze), Vegetais (ferro),
    Infernal (blaze), Cogumelos (apatita).
  * Manuais: Pomar (cobre), Turfa (estanho), Cereais (bronze), Vegetais (ferro), Suculentas (ouro),
    Junco (diamante), Morangas (obsidiana), Cogumelos (apatita), Cacau (lápis), Seringueira
    (emborrachada, IC2), Ender (ender, ExtraUtilities).
* **Mutações de árvores** (código `TreeDefinition`, conferidas no NEI Tree Breeding): Lima
  (Vidoeiro×Carvalho 15%), Cereja (Lima×Carvalho/Vidoeiro 10%), Nogueira (Lima×Cereja 10%),
  Castanha (Nogueira×Lima/Cereja 10%), Limão (Lima×Cereja 5%), Ameixa (Limão×Cereja 5%), Lariço
  (Abeto×Vidoeiro/Carvalho 10%), Pinheiro (Abeto×Lariço 10%), Bordo (Abeto×Lariço 5%), Sequóia
  (Lariço×Pinheiro 5%), Sequóia Gigante (Sequóia×Baobá 1%), Teca (Carv. Escuro×Selva 10%), Ipê
  (Teca×Carv. Escuro), Sumaúma (Selva×Teca), Ébano (Carv. Escuro×Sumaúma), Meranti (Sumaúma×Ébano),
  Sipiri (Sumaúma×Meranti, quente/úmido), Balsa (Teca×Acácia), Acácia do Deserto (Teca×Balsa),
  Padauk (Acácia×Selva), Cocobolo (Acácia do Deserto×Carv. Escuro), Wengué (Cocobolo×Balsa),
  Baobá (Balsa×Wengué), Mahoe (Balsa×Acácia do Deserto 5%), Salgueiro (Carvalho/Vidoeiro/Lima
  entre si 10%, quente/úmido), Álamo (Salgueiro×Vid./Carv./Lima 5%), Zebrano (Ébano×Álamo 5%),
  Mamão (Selva×Cereja 5%), Tamareira (Selva×Mamão 5%).
* Humidificador: sementes→micélio, pedregulho→com musgo, tijolos→com musgo; combustível trigo →
  mofado → em decomposição.
* Hidratador (EASY): semente 10 mB óleo, maçã 200 mB suco + bagaço; fósforo + areia/terra →
  lava; neve + 4 cacos de gelo → 4000 mB gelo picado. Fermentador: muda 250, trigo/cana/cacto/
  cogumelo 50, batata 100 mB. Refinador: 10 mB biomassa → 3 mB etanol.

## Integrações no pack

* Oredict: cobre/estanho/bronze/engrenagens de outros mods valem; a Engrenagem de Pedra vem do
  BuildCraft. Motor Elétrico e Bio Gerador conversam com o IC2 (EU).
* Mochilas e correio: blacklist de itens no correio em `common.cfg`.
* Selos 20n/50n/100n desativados (`stamps.disabled`); Stamp Collector (`mail:2`) sem receita.

## Armadilhas e dicas

* Mudas e abelhas exigidas nas quests precisam ser **puras** (UID0 = UID1 do gene de espécie):
  as tarefas usam NBT parcial `{Genome:{Chromosomes:[{Slot:0b,UID0:X,UID1:X}]}}` (usar só UID0
  quebraria a leitura do genoma pelo Forestry ao desenhar o ícone).
* Fazenda não se forma sem a caixa de engrenagens ou com bloco especial na 3ª camada.
* Motores ligados sem consumo superaquecem. Água no Motor de Biogás desperdiça lava.
* Quadro Comprovado não tem receita (aldeão apicultor: 6 por 1 esmeralda).

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `forestry:oficina_forestry` | milestone | Carpinteiro, Hidratador, Fermentador, Refinador, Centrífuga e Fabricador (Ato II). |
| `forestry:fazenda_montada` | milestone | Fazenda Multibloco programada colhendo sozinha. |
| `forestry:arborista_mestre` | milestone | Genética de árvores: frutíferas, gigantes e tropicais. |
| `forestry:mestre_florestal` | milestone | **Marco final** do Forestry — pilar do **Selo da Vida** (usado também por `abelhas:selo_da_vida`). |
