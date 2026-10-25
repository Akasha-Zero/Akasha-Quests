# JurassiCraft — estudo do capítulo `jurassicraft`

## Visão geral
* **Mod:** JurassiCraft 1.4.0 (namespace `jurassicraft`). Sem dimensão própria.
* **Capítulo:** `quests/chapters/aventura/jurassicraft.yaml`, `order: 250`, 69 quests.
* **Linha:** fóssil/âmbar → Extrator de DNA → Combinador → Cultivador → ovos/seringas → dinossauros.
  Extras: cercas, bifes, couros, crânios, caçadas e chefe T-Rex.

## Progressão
1. `minerio_fossil` → `fossil`; `minerio_ambar` → `ambar`; `dinopad`.
2. `extrator_dna` → `primeiro_dna` → `combinador_dna` → `cultivador` → `soro`.
3. DNA por grupos, depois ovos (`ovo_*`) e seringas (mamíferos: Mamute, Arsinoitherium, Leptictidium).
4. Cercas, culinária (bifes), couros, crânios; caçadas `cacar_*`; `cacar_rex` (boss).

## Armadilhas
* O mod não registra handlers no NEI: o fluxo exato das máquinas (entradas do Extrator/Cultivador)
  foi descrito de forma genérica; as descrições mandam conferir no NEI.
* Entidades dos dinos são `jurassicraft.<Nome>` (ex.: `jurassicraft.Tyrannosaurus`).
* Alguns itens sem Skin/Skull no registro (ex.: Galimimo, Spinosaurus Skin) ficaram de fora.

## Marcos exportados
* `jurassicraft:extrator_dna` — laboratório de DNA iniciado.
* `jurassicraft:ovo_rex` — ovo de Tiranossauro cultivado.
* `jurassicraft:cacar_rex` (boss) — Tiranossauro derrotado.
* `jurassicraft:zoologico_aberto` (milestone) — parque completo.
