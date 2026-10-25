# Bestiário — estudo do capítulo `bestiario`

## Visão geral
* **Capítulo:** `quests/chapters/aventura/bestiario.yaml`, `order: 265`, 95 quests, quase todas `kill`.
* **Mobs:** vanilla, Et Futurum, Netherlicious, Thaumcraft, Witchery, Ars Magica, Blood Magic,
  Botania (Gaia), Tinkers, MFR, Thaumic Horizons, Dim Doors. Fora: Twilight, Aether, Atum,
  OreSpawn, Draconic, Galacticraft (têm capítulos próprios).
* **Contratos diários:** 6 quests `diario_*` com `repeat: 86400`.
* **Infernal Mobs:** não tem entidades próprias (os mesmos mobs com habilidades extras).

## Progressão
`mural` → vanilla (`zumbi`…`dragao_fim`) → Nether → mods mágicos por escola → chefes → marcos
(`cacador_novato`, `cacador_nether`, `cacador_arcano`) → `mestre_cacador` (finale).

## Armadilhas
* Os ids de entidade vêm de `reg.py entity`; alguns bosses (Wither, Dragão) são caros.
* Contratos diários dependem apenas de `mural`; as recompensas são pequenas de propósito.
* Ars Magica: cinco guardiões encadeados (`am_boss_*`) saem do elemental.

## Marcos exportados
* `bestiario:cacador_novato` · `bestiario:cacador_nether` · `bestiario:cacador_arcano` (milestones).
* `bestiario:wither_boss` · `bestiario:dragao_fim` (bosses vanilla).
* `bestiario:mestre_cacador` (finale).
