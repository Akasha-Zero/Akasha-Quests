# Thaumcraft II — O Taumaturgo (`thaumcraft2`)

O estudo completo do Thaumcraft 4.2.3.5 (fontes, mecânicas, ids, Warp, Eldritch, multiblocos) está em
`docs/mods/thaumcraft.md`, compartilhado pelos dois capítulos.

## Visão geral

* Arquivo: `quests/chapters/magia/thaumcraft2.yaml`, `order: 501`, 125 quests.
* Raiz `taumaturgo` com `deps_hidden: [thaumcraft:infusao]` e link visual para `thaumcraft:aprendiz_completo`.
* Fases: Primal Charm e cajados → proteção rúnica → Infusão de Encantamento e Fortaleza → focos
  avançados → nós energizados e vis em rede → golemancia avançada → Warp → Mácula → Culto Carmesim →
  Metal do Vazio → Oculus e Terras Exteriores (4 chefes) → Pérola Primordial e seus usos.
* Extras: núcleos de cajado elementais, aldeões Mago/Banqueiro, campeões, monstros da mácula e do Warp,
  e duas quests secretas do Avaritia (Pérola Extremamente Primordial, Registros Akáshicos).

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `thaumcraft2:vis_em_rede` | milestone | Nó energizado, relés e Manipulador Focal. |
| `thaumcraft2:fabrica_golens` | milestone | Golemancia avançada. |
| `thaumcraft2:terras_exteriores` | portal | Terras Exteriores (dim −42). |
| `thaumcraft2:warden_eldritch` | boss | Warden Eldritch. |
| `thaumcraft2:construto_eldritch` | boss | Construto Eldritch. |
| `thaumcraft2:portal_carmesim` | boss | Portal Carmesim. |
| `thaumcraft2:taintaculo_gigante` | boss | Taintáculos Gigantes. |
| `thaumcraft2:perola_primordial` | milestone | Primeira Pérola Primordial. |
| `thaumcraft2:taumaturgo_supremo` | milestone | **Marco final → Selo Arcano.** |
