# Cozinha e Banquetes (HarvestCraft, Cooking for Blockheads, The Kitchen Mod, AppleCore)

Capítulo `cozinha` — "Cozinha e Banquetes" — `quests/chapters/base/cozinha.yaml` (order 115, Ato I).

## Visão geral

| Mod | Versão no pack | Namespace | Papel |
| --- | --- | --- | --- |
| Pam's HarvestCraft | 1.7.10Lb (mcmod diz "1.7.10i"; registro "1.7.10j") | `harvestcraft` | ~600 comidas, 70+ cultivos, 12 jardins, 40+ árvores frutíferas, abelhas, Mercado, Pressionador, armadilhas |
| Cooking for Blockheads | 1.4.13-GTNH | `cookingforblockheads` | Livros de receitas e a cozinha multibloco (Mesa de Cozinha, Geladeira, Pia, Forno…) |
| The Kitchen Mod | 1.3.23 (MrSpring) | `kitchen` | Sanduíches modulares na Tábua de Cortar, faca, forno a carvão, frigideira e geleias |
| AppleCore | 3.3.11 | `AppleCore` | Mostra fome/saturação no tooltip (Shift) e no HUD; base das mecânicas de comida |

Papel na campanha: Ato I (Raízes) — "comida" do tripé sobrevivência/ferramentas/comida. O
capítulo não depende de outros; a comida boa ajuda todos os capítulos de exploração.

**Não existem no pack:** Shipping Bin, Water Filter, Grinder e Ground/Water Trap (são do
HarvestCraft 1.12). No lugar: Fish Trap e Animal Trap (1.7.10i), Quern (moinho), Churn
(batedeira), Oven (forno a óleo) e as Pias/Poços. Também **não há Spice of Life** (sem
penalidade por repetir comida) nem mod de sede.

## Energia / recursos-base

Sem energia. Recursos-chave: Sal (Panela + qualquer água `listAllwater`), Água Fresca
(balde de água → 1), Leite Fresco (balde de leite → 4), Óleo de Cozinha
(`oliveoilItem`: Espremedor + azeitona/girassol, ou Pressionador), Massa, Caldo, esmeraldas (Mercado).

## AppleCore e a fome (config `AppleCore.cfg`)

* Tooltip de comida com **Shift** mostra fome e saturação (`show.food.values.in.tooltip=true`).
* HUD: mostra quanto a comida na mão vai restaurar e a saturação atual sobre a barra de fome;
  exaustão como barra por trás (`show.food.exhaustion.hud.underlay=true`).
* Regra vanilla: saturação restaurada = fome × modificador × 2, limitada pela fome atual.

## Valores de comida do HarvestCraft (lidos do bytecode `ItemRegistry`)

Config `harvestcraft.cfg`: `snacksaturation=0.6`, `mealsaturation=1.2`,
`meatymealsaturation=1.6`, cultivos crus `cropfoodRestore=1` / `cropsaturationRestore=0.6`.
Fome em meias-coxinhas (2 = 1 coxinha). Exemplos usados no catálogo:

| Prato | Fome | Tipo | Receita (registro) |
| --- | --- | --- | --- |
| Pipoca / Uvas Passas / Castanha Torrada | 4 | lanche | fornalha (milho / uva / castanha) |
| Batata Frita | 4 | lanche | Assadeira + batata + sal |
| Barra de Chocolate | 5 | lanche | Caçarola + chocolate em pó + manteiga + leite |
| Rosquinha | 6 | lanche | Panela + massa + óleo |
| Chá / Café | 4 | lanche (poção) | fornalha (folha de chá / grão de café) |
| Suco de Maçã | 5 | refeição | Espremedor + maçã |
| Limonada | 6 | refeição | Espremedor + limão-siciliano |
| Panquecas | 6 | refeição | Frigideira + farinha + ovo + leite |
| Torrada | 7 | refeição | fornalha (pão) |
| Hambúrguer | 8 | refeição | Frigideira + carne crua + torrada |
| Espaguete | 8 | refeição | Caçarola + tomate + macarrão + louro |
| Cheeseburger / Queijo Grelhado / Waffles / Purê | 9 | refeição | (ver YAML) |
| Sopa de Vegetais / Torta de Maçã / Cachorro-Quente / Caldo Carnoso | 8 | farta | Panela/Assadeira |
| Peixe com Batatas / Frango Assado / Sopa de Macarrão de Frango | 9 | farta | — |
| Pizza / Carne de Panela / Feijão Assado / Torta de Frango | 10 | farta | — |
| Taco / Espaguete com Almôndegas | 11 | farta | — |
| BAT (BLT) / Shepherd's Pie / Cheeseburger Deluxe / Bife com Fritas | 12 | farta | — |
| Chili / Pizza Suprema | 13 | farta | — |
| Cheesebacon / Sunday Roast / Torta de Carne / Frango com Curry / Ploughman's Lunch / Sandubão | 14 | farta | — |
| Quiabo com Frango (Gumbo) / McPam | 16 | farta | — |
| Refeição Deleitosa / Café da Manhã Amistoso / Bacon Épico / Bife Wellington / Pizza de Banquete de Carne / Linguiça com Pão / Deluxe Chicken Curry / Hambúrguer de Beterraba | 18 | farta | — |

## Progressão

1. **Sobrevivência:** pão, Água Fresca, Leite Fresco, Sal.
2. **Utensílios HarvestCraft** (não se gastam nas receitas): Tábua de Cortar (ferro/cobre/aço +
   graveto + tábua), Panela, Frigideira, Caçarola, Assadeira (8 tijolos ou tijolos do Nether),
   Pilão (pedra + graveto), Tigela de Mistura (tábuas + graveto), Espremedor (pedra + placa de
   pressão de pedra). Oredict `toolPot`, `toolCuttingboard`, etc.
3. **Cultivos:** sementes NÃO caem da grama (todos `*seeddropfromgrass=false`); jardins dropam
   o cultivo (`gardensdropSeeds=false`, `gardendropAmount=3`, espalham-se `gardenSpread=true`);
   cultivo → semente na bancada (`enablecroptoseedRecipe=true`); colheita com botão direito
   (`rightclickharvestCrop=true`, `cropsdropSeeds=false`); arroz/alga/oxicoco/castanha-d'água
   plantados na água (`enablecropspecialplanting=true`).
4. **Pomares:** 40 árvores frutíferas (Twilight desligado); mudas no Mercado.
5. **Abelhas:** Colmeias nas árvores (`beehiveRarity=5`) → Abelha Rainha → Apiária (favos de
   mel/cera, larvas) → Pressionador (mel, cera).
6. **Máquinas:** Pressionador, Forno (óleo), Batedeira (sal), Moinho (placas de pressão de pedra),
   Pias/Poço, Armadilhas de peixe e de animais, Mercado (esmeraldas).
7. **Cozinha CfB** e **Kitchen Mod**.
8. **Catálogo de banquetes** por nível de fome/saturação até os pratos de 18.

## Materiais e geração de mundo (config)

* **Jardins** (`gardenRarity=2`) — conteúdo lido do bytecode (`BlockPamNormalGarden` etc.):

| Jardim | Drops (modo cultivo) | Bioma típico (changelog do mod) |
| --- | --- | --- |
| Bagas | amora, mirtilo, tungue, framboesa, morango, uva | floresta, colinas, frio, pântano |
| Grama | aspargo, cevada, aveia, centeio, milho, broto de bambu | planícies não frias |
| Cabaças | melão, pepino, moranga, abobrinha, abóbora | planícies ou floresta |
| Chão | beterraba, cebola, chirívia, amendoim, rabanete, couve-nabo, batata-doce, nabo, ruibarbo, cenoura, batata | montanha, colinas, mesa, savana |
| Ervas | aipo, alho, gengibre, louro, raiz comestível, chá, café, mostarda | florestas não frias |
| Folhoso | brócolis, couve-flor, alho-poró, alface, cebolinha, alcachofra, couve-de-bruxelas, repolho, espinafre | florestas não frias |
| Caules | feijão, soja, pimentão, pimenta chili, berinjela, quiabo, ervilha, tomate | florestas/planícies não frias, pântano |
| Têxtil | algodão | frio, colinas, pântano |
| Tropical | abacaxi, kiwi, melancia, folha de curry, gergelim | quente/úmido, selva, pântano |
| Cogumelos | cogumelo branco (+ vermelho/marrom) | florestas |
| Deserto | tuna (fruta do cacto) | areia |
| Água | oxicoco, arroz, alga, castanha-d'água, moluscos, caranguejo, lagostim, sapo, vieira, camarão, caracol, tartaruga, Green Heart Fish | oceanos e rios não frios |

* **Árvores frutíferas:** todas habilitadas; raridade temperada 15, tropical 10, conífera 15.
* **Sal:** minério de sal habilitado (`saltRarity=30`) + receita Panela + água.
* **Peixes do HarvestCraft** ativos (`enableharvestcraftfish=true`): pescados com vara comum.
* Ovelhas dropam carneiro cru e lulas dropam calamari.
* The Kitchen Mod: plantas selvagens de tomate, alface, amendoim e morango (`lettuce_spawn_rate=10`).

## Máquinas, estruturas e multiblocos

* **Mercado** (tábuas nos cantos, lã nas bordas, esmeralda no centro): compra sementes (1
  esmeralda), mudas de árvores (3), farinha de osso (3) e ovos de criação (spawn eggs) de
  galinha (3), porco (6), ovelha (6), vaca (9) e cavalo (12) — moeda padrão esmeralda.
* **Pressionador** (Presser; ferro/bronze/aço + 2 pistões): favo de mel → cera + mel; frutas → suco + isca;
  azeitona/girassol/nozes/sementes → óleo + isca; cana → açúcar; tronco → papel; soja → tofu.
* **Forno** HarvestCraft (ferro + barras de ferro + fornalha): só comida, combustível Óleo de
  Cozinha, muito rápido.
* **Batedeira** (Churn): Leite Fresco → Manteiga, combustível Sal.
* **Moinho** (Quern): mói o que o Pilão mói (farinha, chocolate em pó, especiarias);
  combustível placas de pressão de pedra.
* **Pias** (Barril de Água, Bacia de Pedra, Jarro de Argila, Poço de Tijolos): ferro + tronco/
  pedra/argila/tijolos (cobre/estanho/bronze/aço também).
* **Armadilha de Peixe** (precisa de ≥5 blocos de água parada em volta, no mesmo nível) com
  Isca de Peixe (linha + 3 peixes crus → 4). **Armadilha de Animais** (≥5 blocos de terra/grama
  em volta) com iscas de grãos, frutas ou vegetais → peru, coelho, veado e drops de animais.
* **Apiária** (tábuas + 3 molduras): Abelha Rainha → favos de mel, de cera e larvas; rainha
  gasta (dano 18) vira Geleia Real na bancada.
* **Cooking for Blockheads:** livro I = livro na fornalha; livro "#NoFilter" = livro + quadro;
  livro II = livro I + 2 diamantes + 2 bancadas; Mesa de Cozinha (argila endurecida preta +
  troncos + livro II) conecta-se a Geladeira (baú + porta de ferro), Balcões, Armários, Pia,
  Forno de Cozinha, Torradeira e Raque de Utensílios encostados (ou via Piso de Cozinha).
  Config: `sinkRequiresWater=true` (a pia precisa ser abastecida), `ovenRequiresCookingOil=false`.
* **The Kitchen Mod:** Faca (ferro + graveto) fatia na bancada; Tábua de Cortar (lajes + placa
  de pressão) monta o sanduíche camada por camada (pão em cima e embaixo); Forno (ferro, carvão,
  isqueiro) cozinha rosbife, filé de frango e bacon (passou do ponto → Carne Queimada);
  Frigideira sobre o forno faz geleias (fatias de morango açucaradas, fatias de maçã,
  amendoim) e frita bacon/filé; Jarro Vazio recolhe a geleia; combos secretos de sanduíche.

## Integrações no pack

* Oredict compartilhado (`cropTomato`, `listAllwater`, `foodSalt`, `listAllmeatraw`…):
  ingredientes de outros mods (Forestry, Netherlicious, etc.) servem nas receitas.
* CfB lê receitas de HarvestCraft, AppleCore e Storage Drawers (módulos ativos).
* Tinkers: Charque no Cabide de Secagem e Frigideira do TiC (ver capítulo `tinkers`).

## Armadilhas e dicas

* Sem sementes da grama: o começo depende dos **jardins** (ou do Mercado).
* Ferramentas de cozinha voltam para a grade após cada receita — não se gastam.
* Armadilhas sem os 5 blocos ao redor não produzem.
* Fruta colhida com botão direito reinicia o crescimento; não quebre o bloco da fruta.

## Marcos exportados

* `cozinha:cozinha_equipada` — os 8 utensílios do HarvestCraft (base da culinária).
* `cozinha:horta_do_registro` — cultivos de seis jardins diferentes (agricultura do Ato I).
* `cozinha:cozinha_multibloco` — cozinha completa do Cooking for Blockheads.
* `cozinha:mestre_petiscos`, `cozinha:mestre_pratos`, `cozinha:mestre_fartura`,
  `cozinha:mestre_banquetes` — degraus do catálogo por nível de fome.
* `cozinha:grande_banquete` — quatro pratos lendários (18 de fome): fim do capítulo.
