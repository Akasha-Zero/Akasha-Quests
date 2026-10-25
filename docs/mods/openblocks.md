# OpenBlocks — estudo do capítulo `openblocks`

## Visão geral

* **Mod:** OpenBlocks `1.12.20-GTNH` (fork GTNH) + OpenModsLib `0.10.14`.
* **Capítulo:** `quests/chapters/tech/openblocks.yaml`, `order: 365`, 59 quests.
* **Papel:** qualidade de vida de Ato I–II (mobilidade, experiência líquida, automação simples,
  pintura). Sem progressão de tiers.

Fontes: registro (`reg.py mod OpenBlocks`), receitas de bancada do dump, `config/OpenBlocks.cfg`,
lang do JAR (`*.description`).

## Energia / recursos-base

* Sem energia própria. Recurso central: **Experiência Líquida** (fluido `xpjuice`),
  `xpToLiquidRatio=20` (20 mB por ponto de XP).

## Progressão (ids reais)

* Livro `OpenBlocks:infoBook` (argila + livro).
* Mobilidade: `elevator:0–15` (8 lã + pérola; cores só conectam iguais; `searchDistance=20`),
  `elevator_rotating`, `hangglider` (2 × `generic:0` + graveto; térmicas ligadas, tecla V),
  `sleepingBag`, `ropeladder`, `ladder` (Escada Cansada), `scaffolding`, `path`, `flag`.
* XP: `tank` (16 baldes, `bucketsPerTank=16`), `xpdrain` (9 barras de ferro), `filledbucket`,
  `xpbottler`, `xpshower`, `vacuumhopper` (funil + obsidiana + olho do Ender), `autoenchantmenttable`,
  `autoanvil`, `sprinkler` (sobre tanque de água, raio 4).
* Companheiros/ferramentas: `luggage`, `goldenEye` (8 pepitas de ouro + olho; recarga com pérola),
  `cursor` (gasta XP por distância, máx. 64), `cannon` + `genericUnstackable` (Ponteiro), `devnull`,
  `slimalyzer`, `pedometer`, `village_highlighter`, `tastyClay`, `epicEraser`, `wrench`.
* Redstone: `blockbreaker`, `blockPlacer`, `itemDropper`, `fan`, `beartrap`, `target`, `bigbutton`,
  `sky`, `builder_guide`.
* Pintura: `imaginary:0/1` (lápis/giz), `pencilGlasses`, `crayonGlasses`, `sonicglasses`,
  `paintmixer` → `paintcan` (sem receita de bancada; feito no misturador), `paintBrush`, `canvas`,
  `canvasglass`, `drawingtable`, `stencil`, `squeegee`.
* Mapas/guindaste: `generic:6/7/9`, `emptyMap`, `cartographer`, `projector`, `craneBackpack`,
  `craneControl`. Troféus `trophy` (NBT `entity`). `donationStation`.

## Materiais e geração de mundo

* Nada gerado. Troféus caem de mobs com `trophyDropChance=0.001` + Pilhagem.

## Integrações no pack

* Tanques/XP aceitam canos de outros mods; Esgoto do MineFactory recolhe orbes; fazendas com Espinhos
  e Esteiras do Extra Utilities.

## Armadilhas e dicas

* **Túmulos desativados** (`blocks.grave=false`; `guide=false` também). Itens caem no chão na morte;
  backup de inventário (`/ob_inventory`) continua.
* Healer (`heal`) é criativo; Technicolor/Badass Glasses só como loot — fora do capítulo.
* Dimensões na `BlacklistedDimensions` do OpenBlocks (vários ids 25–95) — não afetam as quests.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `openblocks:banco_experiencia` | milestone | Experiência líquida automatizada (Dreno, Funil a Vácuo, Mesa e Bigorna automáticas). |
| `openblocks:mestre_curiosidades` | milestone | **Marco final** (Planador, Bagagem, Mesa automática, Guia de Construção). |
