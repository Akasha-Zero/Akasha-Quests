# Fósseis e Arqueologia — estudo do capítulo `fossils`

## Visão geral
* **Mod:** Fossils and Archeology Revival 7.3.2.1 (namespace `fossil`).
* **Dimensões:** `63` Land of Darkness (Anu) e `62` Ancient Treasure Room.
* **Capítulo:** `quests/chapters/aventura/fossils.yaml`, `order: 255`, 88 quests.

## Progressão
1. `bloco_fossil` → `bio_fossil` → `sucata_reliquia` → `dinopedia` → `analisador`.
2. `mesa_arqueologia`, vasos, armas antigas, esqueleto/armadura de ossos.
3. DNA por grupos → `cuba_cultura` → ovos, sacos de ovas, embriões; cuidado (comedouro, brinquedos).
4. `gema_escaravelho` → ferramentas → `maquina_tempo` (milestone).
5. `estatua_anu` → `portal_anu` (dim 63) → `anubita`/`sentinela` → `anu` (boss) → `chave_antiga`
   → `sala_tesouro` (dim 62) → `fim_fossil`.

## Armadilhas
* Não foi confirmado como ativar a Estátua de Anu nem o uso exato de Relógio/Máquina do Tempo:
  as descrições orientam a conferir no NEI/Dinopédia.
* Analisador exige Sucata de Relíquia + Bio-Fóssil na receita (não "artifact" genérico).
* Anu é a entidade `fossil.PigBoss`.

## Marcos exportados
* `fossils:analisador` — laboratório de análise.
* `fossils:cuba_cultura` — Cuba de Cultura.
* `fossils:maquina_tempo` (milestone).
* `fossils:portal_anu` (portal, dim 63) · `fossils:anu` (boss) · `fossils:chave_antiga`.
* `fossils:sala_tesouro` (portal, dim 62) · `fossils:fim_fossil` (milestone).
