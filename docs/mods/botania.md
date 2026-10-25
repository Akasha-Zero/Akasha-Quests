# Botania — estudo do capítulo `botania`

## Visão geral

* **Mod:** Botania `r1.8-249` (`Botania+r1.8-249.jar`, namespace de registro `Botania`), com Baubles
  `2.2.22-GTNH`. Sem o Garden of Glass (a receita da Orechid usada é a "normal", com runas e Pó de
  Fada).
* **Capítulo:** `quests/chapters/magia/botania.yaml`, `order: 530`, **311 quests** (meta do MAPA ≈ 260;
  o excesso é catálogo legítimo: 16 runas, 21 lentes, ~40 flores especiais, ~25 acessórios, ~30
  itens místicos — cada um com função própria).
* **Papel na campanha:** é o "Volume das Flores Vivas". Começa no Ato I (flores, Boticário, Margarida
  Pura, Daybloom, Manasteel), atravessa o Ato II (runas, Terrasteel, Portal de Alfheim, Elementium) e
  termina no Ato III/IV (Guardião de Gaia I e II, relíquias). O **Gaia II** é uma das provas do
  **Selo Arcano** (NARRATIVA §4).
* **Addon:** Advanced Botany tem capítulo próprio (`advbotany`, ver `docs/mods/advbotany.md`).
  Outros mods registram flores no Botania (MagicBees: Beegonia/Hiveacynth/Hibeescus; Avaritia:
  Asgardandelion/Soarleander; Forbidden Magic: Tainthistle/Bloodthorn/Whisperweed/Euclidaisy). Elas
  **não** estão neste capítulo (pertencem aos capítulos desses mods).

Fontes: registro do pack (`reg.py mod Botania`, receitas de bancada do dump), **bytecode do JAR**
via `javap` (as receitas de máquinas do Botania são registradas em código: `ModPetalRecipes`,
`ModRuneRecipes`, `ModManaInfusionRecipes`, `ModManaAlchemyRecipes`, `ModManaConjurationRecipes`,
`ModElvenTradeRecipes`, `ModPureDaisyRecipes`, `ModBrewRecipes`; e a lógica de `EntityDoppleganger`,
`TileAlfPortal`, `TileTerraPlate`, `TilePool`, `ItemDice`), `assets/botania/lang/en_US.lang` e
`pt_BR.lang` (textos da Lexica), e `config/Botania.cfg`. O catálogo NEI (`reg.py handlers`) **ainda
não estava importado**; todas as receitas de Boticário/Altar/Poço/Elfos/Fermentador foram tiradas
do bytecode.

## Energia / recursos-base

* **Mana.** Gerada por flores geradoras → guardada no **Dispersor** mais próximo (buffer 1.000) →
  disparada em rajadas → **Poço de Mana** (1.000.000; Diluído 10.000; Fabuloso = normal).
* Acumuladores portáteis: Tablete de Mana 500.000, Bracelete de Mana 500.000, Bracelete Superior
  2.000.000 (constantes do bytecode).
* Fagulhas (`Botania:spark`) transferem Mana poço ↔ bloco sem dispersor; melhorias (Dispersiva,
  Dominante, Recessiva, Isolada) = Pó de Fada + Manasteel + runa elemental (Água/Fogo/Terra/Ar).
* **RF:** Fluxo-campo de Mana (`Botania:rfGenerator`) converte Mana em RF — **ativo**
  (`manaFluxfield.enabled=true`).

## Progressão (ids reais)

1. Lexica (`Botania:lexicon` = muda + livro) → Flores Místicas (`Botania:flower:0-15`, 2 pétalas na
   bancada; altas `doubleFlower1/2` → 4 pétalas) → Pétalas `Botania:petal:*` → Pilão
   `pestleAndMortar` → Pó Floral `Botania:dye:*`.
2. Boticário de Pétalas `Botania:altar` (água + pétalas + **semente** para finalizar).
3. Margarida Pura (`specialFlower {type:"puredaisy"}`, 4 brancas): tronco → Madeira-viva
   `Botania:livingwood`, pedra → Rocha-viva `Botania:livingrock`; também netherrack→pedregulho,
   areia de almas→areia, gelo→gelo compactado, água→neve, Lâmpada Infernal (oredict `blockBlaze`)
   →obsidiana.
4. Graveto `manaResource:3` → Varinha da Floresta `Botania:twigWand`.
5. Daybloom/Nightshade (passivas; decaem após `passiveDecay.time = 72000` ticks) → Dispersor
   `Botania:spreader` → Poço `Botania:pool`.
6. Infusões no poço (custos do bytecode): Ferro→Manasteel 3.000 (bloco 27.000), Pérola do Fim→Pérola
   de Mana 6.000, Diamante→Diamante de Mana 10.000 (bloco 90.000), Pó Floral 400 / Pólvora, Redstone,
   Pedra Luminosa, Açúcar 500 → Pó de Mana `manaResource:23`, Vidro→Vidro de Mana 150, Linha→Linha
   de Mana 5.000, Grama→Sementes de Pasto 2.500, Arbusto Seco→Sementes Boreais 2.500, Cogumelo→Esporos
   6.500, Quartzo→Quartzo de Mana 250, Batata→Batatinha 1.337, Biscoito→Biscoito da Totalidade
   20.000, Frasco→Mana Engarrafada 5.000, Pistão→Transmissor de Mana 15.000, Ferramenta de Escrita
   (Thaumcraft)→Tinteiro do Botanurgista 35.000.
7. **Endoflame** (2 marrons, vermelha, cinza claro + Pó de Mana) — primeira geradora ativa.
8. **Altar Rúnico** `Botania:runeAltar` (5 Rocha-viva + Pérola ou Diamante de Mana). Runas são
   catalisadoras (voltam ao jogador). Receitas e custos:
   * Água/Fogo/Terra/Ar (5.200, rende 2): Pó de Mana + Manasteel + (Farinha de Osso, Cana, Vara de
     Pesca) / (Tijolo do Nether, Pólvora, Fungo do Nether) / (Pedra, Bloco de Carvão, Cogumelo) /
     (Tapete, Pena, Linha).
   * Estações (8.000, rende 1): Primavera = Água+Fogo+3 mudas+trigo; Verão = Terra+Ar+2 areias+slime+
     melancia; Outono = Fogo+Ar+3 folhas+olho de aranha; Inverno = Água+Terra+2 blocos de neve+lã+bolo.
   * Mana (8.000): 5 Manasteel + Pérola de Mana.
   * Pecados (12.000): 2 Diamantes de Mana + Desejo(Verão+Ar), Gula(Inverno+Fogo), Ganância
     (Primavera+Água), Preguiça(Outono+Ar), Ira(Inverno+Terra), Inveja(Inverno+Água), Orgulho
     (Verão+Fogo).
9. **Placa de Aglomeração Terrestre** `Botania:terraPlate` (3 blocos de lápis / runa água, bloco de
   manasteel, runa fogo / runa terra, runa mana, runa ar). Estrutura 3×3 sob a placa: Rocha-viva no
   centro e cantos, blocos de Lápis nos lados (`TileTerraPlate`). **Terrasteel** `manaResource:4` =
   Manasteel + Diamante de Mana + Pérola de Mana, 500.000 de Mana.
10. **Portal de Alfheim:** Núcleo `Botania:alfheimPortal` (6 madeira-viva + 3 pepitas de terrasteel),
    2 Pilões da Natureza `Botania:pylon:1` (pilão de mana + 4 pepitas + olho do fim), 8 Madeira-viva +
    3 Madeira-viva Reluzente `livingwood:5`, 2 Poços. Posições (`TileAlfPortal`): moldura 5×5 sem
    cantos com o núcleo no meio da base; reluzentes no meio das laterais e do topo; poços a ±3 na
    lateral e 3 à frente/atrás, pilões em cima dos poços; miolo 3×3 vazio. Ativa com a Varinha.
11. Trocas élficas: Madeira-viva→Madeira dos Sonhos; 2 Manasteel→1 Elementium (blocos idem); Pérola de
    Mana→Pó de Fada; Diamante de Mana→Pedra Draconiana (bloco idem); Quartzo→Quartzo Élfico; Vidro de
    Mana→Vidro Élfico. Ferro (barra/bloco), Pérola do Fim e Diamante (item/bloco) são **devolvidos**;
    qualquer outro item sem receita é **consumido** ("presente"). Jogar a Lexica no portal desbloqueia
    o Conhecimento Élfico.
12. **Gaia:** Pilão de Gaia `Botania:pylon:2` (pilão de mana + 2 elementium + 2 pó de fada) ×4 +
    Sinalizador. Gaia I com Barra de Terrasteel; **Barra de Espírito de Gaia** `manaResource:14`
    (terrasteel + 4 espíritos) invoca o Gaia II. Dado do Destino → relíquias; 3 anéis → Anel dos Aesir.

## Materiais e geração de mundo (`config/Botania.cfg`)

* Flores Místicas: `worldgen.flower.density=2`, `patchChance=16`, `patchSize=6`, `quantity=2`,
  `tallChance=0.05` (padrões) — manchas em biomas com grama do Mundo Normal.
* Cogumelos Brilhantes subterrâneos: `worldgen.mushroom.quantity=40`.
* Nenhum minério próprio. Prismarinho vem da alquimia (Quartzo → Fragmento, 200 de Mana).
* Conteúdo ativo: `relics.enabled=true`, `manaEnchanter.enabled=true`, `manaFluxfield.enabled=true`,
  `fallenKanade.enabled=true`, `darkQuartz.enabled=true`, `18stones.enabled=true`,
  `enderStuff.enabled=true`, `ringOfOdin.fireResist=true`, `harvestLevel.boreLens=3`,
  `harvestLevel.weightLens=2`. Nada relevante desativado.

## Máquinas, estruturas e multiblocos

* **Boticário de Pétalas:** água (balde), pétalas/ingredientes jogados, semente finaliza. Lava =
  incinerador. Agachado + mão vazia remove o último item; mão vazia até 20 s depois repõe a receita.
* **Altar Rúnico:** ingredientes + Mana por dispersor; finalizar jogando Rocha-viva e clicando com a
  Varinha. Comparador: 1 = aceitando Mana, 2 = pronto.
* **Placa de Aglomeração:** ver Progressão 9. Pegar os itens no meio aborta e perde Mana.
* **Encantador de Mana:** 17 obsidianas, 10 flores místicas, 6 Pilões de Mana, 1 Bloco de Lápis
  (planta na Lexica); Varinha no lápis forma o bloco. Não consome os livros; só o 1º encanto de cada
  livro conta; item não pode já estar encantado.
* **Catalisador Alquímico** (sob o poço): Carne Podre→Couro 600, Vara de Blaze→Fungo do Nether
  4.000, Lágrima de Ghast→Pérola do Fim 28.000, Quartzo→Prismarinho 200, ciclos de madeira/mudas,
  pedras decorativas (Andesito/Basalto/Diorito/Granito do Botania), encolher flores (Petite) etc.
* **Catalizador de Conjurações:** duplica Redstone (5.000), Pedra Luminosa (5.000), Quartzo (2.500),
  Carvão (2.100), Bola de Neve (200), Netherrack (200), Areia de Almas (1.500), Cascalho (720),
  Folhas (2.000), Grama (800).
* **Fermentador Botânico:** frasco + ingredientes (sempre Fungo do Nether). Frascos Pequenos (Vidro de
  Mana, 4 doses) / Frascos Élficos (6 doses, dobro de Mana). Incenso: ~10× Mana, 60× duração, raio 30.
  Pingente de Sangue: poção permanente como amuleto.
* **Corporea:** Fagulha Corporea (spark + pó de fada + ar do fim) e Mestre (+ pedra draconiana) em
  inventários; Índice (pedidos pelo chat; tecla C do NEI), Exaustor, Interceptador, Retentor, Cubo
  de Cristal.

## Dimensões, estruturas, chefes e mobs

* **Alfheim** não é uma dimensão jogável nesta versão: o portal só passa itens.
* **Guardião de Gaia** (`Botania.botania:doppleganger`), regras confirmadas no bytecode de
  `EntityDoppleganger`:
  * Altar: TileEntity de Sinalizador (a Lexica pede ativo; o código só verifica o bloco) + 4 Pilões
    de Gaia em (±4, +1, ±4). Mensagens: `needsCatalysts` (pilões errados), `badArena`, `peacefulNoob`.
  * Arena (`hasProperArena`): círculo de raio 12; cada coluna precisa de chão sólido entre 3 abaixo e
    3 acima do sinalizador com ≥3 blocos sem colisão acima.
  * Vida 800 × nº de jogadores; spawna 3 blocos acima do sinalizador, 8 s invulnerável. Só aceita
    dano tipo `player` (corpo a corpo) de jogador real ou de Fadas; teto 40 (60 crítico); Gaia II
    recebe 60%. Jogadores falsos (`[...]`, ComputerCraft) ignorados. Remove efeitos de sinalizador
    (ambientes). Puxa de volta quem sai do raio. Quebra Beartrap (OpenBlocks) e Magnet (Thaumic
    Tinkerer) na arena.
  * Abaixo de 20% de vida: fase invulnerável com ondas de Zumbis, Esqueletos (arco), Esqueletos
    Wither e Bruxas (mais bruxas no Gaia II); armadilhas roxas (`magicLandmine`), mísseis, fadas.
  * Drops por jogador participante: Gaia I = 8 Espíritos (1º atacante) / 6 (demais), 20% disco
    `recordGaia1`. Gaia II = 16/10 Espíritos + 1 Vontade dos Antigos (aleatória) + Dado do Destino
    (vinculado) + 25% Semente do Super-crescimento, 50% Lótus Negra/Profunda, 90% Manasteel 16–27,
    70% Pérolas de Mana 8–13, 50% Diamantes de Mana 4–6, runas aleatórias, 20% Pinkinator, 30% disco
    vanilla (ou 20% `recordGaia2`).
  * Cabeça do Guardião: golpe final com Machado de Elementium (1/13 + Pilhagem).
* **Dado do Destino:** resultado pela fase da lua; se já tiver aquela relíquia, sorteia entre as que
  faltam; com as 6, o dado some.

## Integrações no pack

* Thaumcraft: Elmos da Revelação (elmo + Óculos da Descoberta), Tinteiro do Botanurgista, poção
  "Mente Sã" (Warp), estabilizadores de infusão (`thaumraftStabilizers.enabled=true`).
* RF (Fluxo-campo). Et Futurum também registra `shardPrismarine` (aceito nas receitas).
* Baubles: inventário compartilhado; o pack tem tipos de slot extras de outros addons.

## Armadilhas e dicas

* Flores memorizam o dispersor/poço mais próximo **no momento em que são plantadas**.
* Passivas murcham; Rafflowsia reaproveita, Solo Encantado impede.
* Entropinnyum só absorve TNT se estiver **vazia**; Thermalily desperdiça lava no resfriamento.
* Portal: itens sem troca são perdidos.
* Gaia: flechas/magia não ferem; não usar no Pacífico; arena plana sem obstruções.
* Índice Corporea compara nomes de item; em servidor pode exigir o nome em inglês (não confirmado
  in-game — por isso a quest sugere a tecla C do NEI).

## Marcos exportados

* `botania:manasteel` (milestone) — primeiro metal mágico; fim da fase inicial.
* `botania:mestre_runas` (milestone) — as 16 runas.
* `botania:terrasteel` (milestone) — **marco exportado**: Terrasteel na Placa de Aglomeração.
* `botania:portal_alfheim` (portal) — **marco exportado**: Portal de Alfheim aberto (tarefa: Madeira dos
  Sonhos).
* `botania:gaia1` (boss) — **marco exportado**: Guardião de Gaia I (kill + 4 Espíritos de Gaia).
* `botania:gaia2` (boss) — **marco exportado**: Guardião de Gaia II (kill + Vontade dos Antigos);
  alimenta o **Selo Arcano**.
* `botania:anel_aesir` (milestone) — capstone opcional: Anel dos Aesir.
