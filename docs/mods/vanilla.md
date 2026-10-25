# Minecraft 1.7.10 (sobrevivência) — estudo do capítulo `vanilla`

## Visão geral

* **Conteúdo:** Minecraft 1.7.10 (Forge 10.13.4.1614) com os ajustes que outros mods fazem no
  jogo base (principalmente **Et Futurum Requiem 2.6.59-GTNH**, **EnderStorage**, **Waystones-X**,
  **Netherlicious**, **Infernal Mobs**).
* **Capítulo:** `quests/chapters/base/vanilla.yaml`, `order: 100`, título "Primeiros Passos",
  121 quests (26 main, 51 normal, 27 optional, 8 info, 8 milestone, 1 portal).
* **Papel na campanha:** Ato I — Raízes. Continua o Prólogo (que já cobre tronco, bancada,
  picareta de pedra, pedregulho, fornalha, carvão, tochas, 16 ferros, cama, baú, comida, Waystone,
  mochila, Bússola da Natureza e Tomo Akáshico — nada disso é repetido aqui). Termina com o marco
  "Prontos para os Volumes" e com o "Limiar do Fim" (12 Olhos do Fim + portal encontrado), que
  aponta para o capítulo `fim` (o Dragão do Fim fica lá).

Fontes: registro do pack (`reg.py`), receitas de bancada/fornalha do dump, catálogo NEI, config do
pack (`config/etfuturum/*.cfg`, `EnderStorage.cfg`, `waystones.cfg`), bytecode do Et Futurum
(`ServerEventHandler`, `EtFuturumWorldGenerator`, `Mending`).

## Energia / recursos-base

Não há energia. Recursos-base: XP (níveis), lápis-lazúli (pagamento da mesa de encantamentos do
Et Futurum), pó de blaze (combustível do suporte de poções do Et Futurum), esmeraldas (comércio).

## Progressão

1. Equipamento de ferro (`minecraft:iron_*`), balde, isqueiro, arco, tesoura.
2. Mineração por camadas → `etfuturum:raw_ore:1` (ferro bruto), ouro, redstone, lápis
   (`minecraft:dye:4`), diamante, obsidiana; milestone `mineracao_profunda`.
3. Encantamentos: livros → `minecraft:enchanting_table` → 15 estantes → nível 30 →
   `minecraft:enchanted_book` → `minecraft:anvil`; milestone `encantador`.
4. Nether: portal (visit dim −1) → quartzo, pedra luminosa, areia das almas, fortaleza
   (`minecraft:nether_brick`), verruga, Blaze (`kill Blaze x5` + 8 varas); milestone
   `nether_basico`.
5. Poções: `minecraft:brewing_stand` + pó de blaze; resistência ao fogo, cura, visão noturna,
   força, regeneração, agilidade, fraqueza, respiração, invisibilidade, arremessáveis; milestone
   `alquimista`.
6. Vilas (esmeraldas, profissões, golens, cura de aldeão zumbi), agricultura e pecuária
   (milestone `fazendeiro`), redstone (milestone `engenheiro_redstone`), pesca, mapas, construção.
7. Final: Endermen → Olho do Fim → Fortaleza → `prontos_volumes` → `limiar_do_fim`.

## Materiais e geração de mundo

* Minérios vanilla (1.7.10): carvão Y 0–127, ferro 0–63, ouro e lápis 0–31, redstone e
  diamante 0–15, esmeralda 4–31 só em Colinas Extremas.
* **Et Futurum:** ardósia profunda substitui toda a pedra abaixo de **Y 22**
  (`deepslateGenerationMode=0`, `deepslateMaxY=22`, também substitui terra e granito/diorito/
  andesito); minérios lá viram versões de ardósia (mesmos drops).
* **Minérios brutos:** `enableRawOres=true` — minério de ferro/ouro/cobre (oredict) solta
  `etfuturum:raw_ore:*` (Fortuna aumenta; Toque Suave pega o bloco). Por isso as quests pedem
  lingotes, nunca o bloco de minério.
* Waystones nascem em vilas, templos do deserto/selva, fortalezas, fortalezas do Nether, pilares
  do Fim e no spawn (`structureWaystoneRules`).

## Máquinas, estruturas e multiblocos

* **Mesa de Encantamentos (Et Futurum):** `enableNewEnchantingTable=true`, tile convertido na
  hora (`tileReplacementMode=0`): prévia do encantamento, paga com lápis, custo de 1–3 níveis.
* **Suporte de Poções (Et Futurum):** slot de combustível; pó de blaze = 30 fermentações.
* **Bigorna (Et Futurum):** renomear mais barato (`enableNewAnvil`).
* **Sinalizador:** feixe colorido por vidro tingido (`enableNewBeacon`).
* Portal do Nether 4×5 de obsidiana; portal do Fim com 12 olhos.

## Dimensões, biomas, estruturas, chefes e mobs

* Nether: provedor do **Netherlicious** (biomas extras); fortalezas vanilla com Waystone.
* Fim: provedor do Et Futurum (ilhas externas) — conteúdo no capítulo `fim`.
* Chefes: nenhum neste capítulo (Wither → `nether`; Dragão → `fim`).
* Infernal Mobs: mobs com modificadores aparecem aleatoriamente (aviso na quest
  `perigos_da_noite`).

## Integrações no pack

* **EnderStorage** (`disable-vanilla=true`): o Baú do Fim vanilla não pode ser colocado — quest
  informativa `bau_ender_aviso` remete ao capítulo `utilidades`.
* Receitas extras do pack: sela (couro + ferro ou linha, do Adventure Backpack), etiqueta de nome.
* NEI: F7 (sobreposição de mobs), F9 (chunks).

## Armadilhas e dicas

* Potions: o registro só lista as metas que o NEI lista (ex.: resistência ao fogo 8227/8259,
  cura 8261/8229, visão noturna 8230/8262, fraqueza 8232/8264). As quests usam `one_of` com todas
  as variantes registradas para não travar o jogador.
* Camas explodem no Nether; lava em Y 11; não cavar reto para baixo.
* Cracked stone bricks (`minecraft:stonebrick:2`) também saem da fornalha — a quest da fortaleza
  pede também um `check`.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `vanilla:mineracao_profunda` | milestone | Minérios de todas as camadas + obsidiana. |
| `vanilla:encantador` | milestone | Mesa de encantamentos com 15 estantes, nível 30 e bigorna. |
| `vanilla:portal_nether` | portal | Primeira entrada no Nether. |
| `vanilla:nether_basico` | milestone | Blazes derrotados, verruga, quartzo e pedra luminosa. |
| `vanilla:alquimista` | milestone | Poções básicas e arremessáveis. |
| `vanilla:fazendeiro` | milestone | Lavouras e criação de animais. |
| `vanilla:engenheiro_redstone` | milestone | Pistões, repetidores, comparadores, funis. |
| `vanilla:prontos_volumes` | milestone | **Marco principal do capítulo** ("Prontos para os Volumes"). |
| `vanilla:limiar_do_fim` | milestone | Portal do Fim encontrado e ativável — ponte para o capítulo `fim`. |
