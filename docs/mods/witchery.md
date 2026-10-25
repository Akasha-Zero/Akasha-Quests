# Witchery — estudo do capítulo `witchery`

## Visão geral

* **Mod:** `witchery-1.7.10-0.24.1-evernife-V6.jar` (modid `witchery`, versão `0.24.1`).
* **Capítulo:** `quests/chapters/magia/witchery.yaml`, `order: 550`, 185 quests.
* **Papel:** magia "de bruxa" do Ato II (ervas, forno, altar, giz, bonecos) até o Ato IV (Leonard,
  Senhor do Tormento, Reflexo, lobisomem/vampiro). O final alimenta o **Selo Arcano**.
* Sem pt_BR para itens no JAR (só algumas falas do Espelho em português): nomes em inglês no jogo.

Fontes: registro (`reg.py mod witchery`, entidades, dimensões), catálogo NEI (`Witches Oven`,
`Distillery`, `Kettle`, `Witch's Cauldron`, `Spinning Wheel`), lang do JAR (livros Herbology,
Circle Magic, Witches' Brews, Observations of an Immortal, falas do Altar do Lobo e de Lilith),
**descompilação** (CFR + mapeamentos MCP 1.7.10) de `WitcheryRecipes` (todos os ritos com focos,
círculos e poder), `WitcheryBrewRegistry` (receitas do caldeirão), `BlockAltar` (multibloco 6
blocos, fontes de poder e artefatos), `BlockWitchesOven` (fumaças), `ItemGeneral` (Brew do Sono /
Agulha Gelada), `GenericEvents` (drops com Arthana), `EntityLordOfTorment`, `WorldProviderTorment`,
`BlockMirror`, bosses; e `config/witchery.cfg`.

## Energia / recursos-base

* **Poder do Altar**: multibloco de 6 blocos de Altar (retângulo 3×2). Poder máximo = soma das
  fontes naturais num cubo de ±14 blocos (cada tipo com limite: grama, terra, folhas, toras,
  flores, plantações, água, cogumelos, cactos, plantas e árvores de bruxa...). Coração de Demônio e
  Ovo do Dragão valem muito.
* **Artefatos** sobre o altar: crânio (esqueleto/wither/zumbi), Candelabro (ou tocha), Cálice
  (cheio = +2), Arthana (alcance), Ramo Místico (aprimoramento), Pentáculo (recarga ×2).
* **Pedra Sintonizada Carregada**: substitui o poder do altar nas versões portáteis dos ritos.
* **Energia infundida** do jogador (Infusões) para o Ramo Místico.

## Progressão (ids reais)

1. Sementes da grama → `ingredient:21` Beladona, `:22` Mandrágora, `:69` Alcachofra, `:78` Agulha Gelada, `:111` Absinto, `:156` Acônito, `garlic`.
2. `witchesovenidle` + jarros (`ingredient:26/27`): mudas → fumaças (carvalho `:31`, bétula `:32`, pinheiro `:33`, sorveira `:34`, amieiro `:35`, espinheiro `:36`); outros → `:28`; 30 % base, +25 % por funil, +30 % funil filtrado.
3. `ingredient:153` Pasta de Unção (4 sementes) → `cauldron` (caldeirão vanilla + pasta) → Mutandis `:14` ×6 (Mandrágora + Exalação + ovo).
4. Mutandis em mudas → `witchsapling:0/1/2` → `witchlog`; `altar` ×6.
5. `ingredient:10` Pedra Sintonizada (Sopro de Magia + diamante + lava) → `distilleryidle`, `kettle`, `spinningwheel`.
6. Destilaria: Gesso `:17`/Vitríolo `:30`; Lágrima `:37`; Vapor de Diamante `:29`; Mal Refinado `:38`; Orvalho do Ender `:67`; Sangue Demoníaco `:80`.
7. `chalkritual` (Lágrima + Cinza + Gesso) → no caldeirão: `chalkheart`, `chalkotherwhere`, `chalkinfernal`.
8. Ritos (focos/círculos/poder no capítulo), Pedra Carregada `:11`, Waystone `:12`, Taglock, Bonecos `poppet:0–11`.
9. Roda de Fiar, vestes, Vassoura Encantada `:9`, Galho de Ent `:82` → `mysticbranch`.
10. Mundo Espiritual → Espírito Fluido `:96` → Base Infundida `:121` → Brews de Voo/Túmulo.
11. Leonard, Senhor do Tormento, Espelho/Reflexo, Lobisomem, Vampiro, Caçador de Bruxas.

## Materiais e geração de mundo

Não gera minério. Estruturas ativas (config): Apotecários, Livrarias, Covens, cabanas de Hobgoblin,
cabanas, Wicker Men, cabanas de bruxa (inclusive no Twilight). Drops (com Arthana na mão as chances
sobem): Lã de Morcego, Língua de Cão (33 %/75 %), Coração de Creeper (2 %/8 %), Pó Espectral
(esqueletos/zumbis, só com Arthana), Coração de Demônio (Demônio, 33 % com Arthana), Asa de Corujinha,
Dedo de Sapo. Ent desperta ao cortar madeira de bruxa e solta Galho de Ent.

## Máquinas, estruturas e multiblocos

* Círculos: pequeno 7×7, médio 11×11, grande 15×15; Ritual (branco), Além-Mundo (roxo), Infernal
  (vermelho); Glifo do Coração no centro; interior livre.
* Caldeirão da Bruxa: água fervendo; itens em ordem; Gesso limpa, Cal Virgem explode o conteúdo;
  rituais de caldeirão usam taglock/Língua de Cão, chalk circles reduzem custo.
* Chaleira: 6 ingredientes, poder do altar (receitas no NEI).

## Dimensões, chefes e mobs

* **Mundo Espiritual (-37)**: entra bebendo Brew do Sono (ou Maçã do Sono) no Mundo Normal; volta com
  Agulha Gelada. Algodão Etéreo, espíritos, Pesadelo. Brew do Espírito Fluido só é feito lá.
* **Tormento (-38)**: o Senhor do Tormento, abaixo de 50 % de vida fora do Tormento, arrasta os
  jogadores próximos para lá. Drops: livros encantados, Coração de Demônio, Alma do Demônio do
  Tormento. O contrato "Torment" (lido num círculo de pedras) está ligado à sua chegada —
  a origem exata do contrato não foi confirmada.
* **Mundo do Espelho (-39)**: atravessando um Espelho (Rito de Infusão com Demônio vivo).
* Chefes: Leonard (caldeirão com Chapéu de Bruxa, 10 000 de poder; solta Urna de Leonard),
  Reflexo (Espelho, círculo grande infernal), Caçador Chifrudo (Trompa da Caçada, nível 5 de
  lobisomem; solta Lança), Demônio, Lilith (vampirismo), Baba Yaga (Chapéu), Caçador de Bruxas.
* Lobisomem: infecção/maldição (lua cheia, convento completo); Altar do Lobo com 10 níveis
  (ouro → Amuleto da Lua; carne; línguas; Caçador Chifrudo; ...). Vampiro: rito do Cálice de Vidro,
  Lilith no Nether, 10 níveis descritos no livro.

## Integrações no pack

* Thaumcraft, Forestry (árvores no forno), MFR, Ars Magica 2, Mystcraft habilitados na config.
* Prata pelo oredict `ingotSilver`/`dustSilver` (Thermal etc.) para Espada de Prata, virotes,
  armadilha de lobo e armadura prateada.
* Qualquer muda do oredict vira Cinza de Madeira (config).

## Armadilhas e dicas

* Mandrágora colhida na hora errada vira mob.
* Maldições, bonecos vodu e taglocks afetam jogadores: orientar uso responsável (servidor).
* Inferno na Terra é destrutivo; incêndios habilitados.
* Prateleira de Bonecos só funciona no Mundo Normal e no Mundo Espiritual (config).
* Estátua da Deusa e Void Bramble sem receita (config `false`).
* No Mundo Espiritual o corpo fica deitado no Mundo Normal: proteja-o.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `witchery:primeiro_rito` | milestone | Altar + giz + glifo: entrada na magia de círculo. |
| `witchery:colecao_bonecos` | milestone | Bonecos de proteção principais. |
| `witchery:vassoura` | milestone | Vassoura Encantada (voo). |
| `witchery:vinculo_familiar` | milestone | Familiar vinculado. |
| `witchery:mundo_espiritual` | portal | Primeira visita ao Mundo Espiritual (-37). |
| `witchery:invocar_demonio` | boss | Primeiro Demônio invocado e derrotado. |
| `witchery:leonard` | boss | Leonard derrotado. |
| `witchery:senhor_tormento` | boss | Senhor do Tormento derrotado. |
| `witchery:tormento` | portal | Visita ao Tormento (-38). |
| `witchery:mundo_espelho` | portal | Visita ao Mundo do Espelho (-39). |
| `witchery:reflexo` | boss | O Reflexo derrotado. |
| `witchery:cacador_chifrudo` | boss | Caçador Chifrudo (lobisomem nível 5). |
| `witchery:lilith` | boss | Encontro com Lilith (vampirismo). |
| `witchery:grande_bruxa` | finale | **Marco final** — usado pelo **Selo Arcano**. |
