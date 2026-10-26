# Epílogo: Singularidades Eternas (`singularidades`)

## Visão geral
* **Mods:** Avaritia (Singularity metas 0-11), Industrial Upgrade (`avaritia_iu_singularity` 0-16),
  Eternal Singularity (`eternalsingularity:eternal_singularity`) e parentes (AE2, MagicBees).
* **Capítulo:** `quests/chapters/epilogo/singularidades.yaml`, `order: 995`, 50 quests. A raiz
  depende de `avaritia:compressor_neutronio`; o capítulo `avaritia` depende de volta dos marcos
  `eternal_singularity` e `iu_conjunto` (sem ciclo).

## Progressão
Blocos de metal (vanilla/Thermal/IU) → Compressor de Neutrônio → 12 singularidades do Avaritia
→ **Singularidade Eterna** (11 delas, sem forma, Mesa Extrema; a Argila fica de fora) → 17
singularidades do IU (16 entram no Catalisador) → quests opcionais de AE2/MagicBees → `sing_mestre`.

## Armadilhas
* Cada singularidade custa centenas de blocos; o dump não traz as quantidades do Compressor.
* Blocos de entrada aceitam equivalentes (Thermal/IE/TConstruct/ExtraPlanets).
* A Singularidade de Argila não tem entrada identificada no dump; conferir no NEI.

## Marcos exportados
`singularidades:sing_avaritia_12`, `singularidades:eternal_singularity`,
`singularidades:iu_conjunto`, `singularidades:sing_mestre`.
