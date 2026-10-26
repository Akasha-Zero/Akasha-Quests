# ATM: Capítulo 3 — A Estrela ATM (`atm_star`)

## Visão geral
* **Mods:** Akashic: All The Modium / All The Tweaks / All The Compressed (`allthemodiumlegacy`),
  mais os ingredientes de Draconic Evolution, Ender IO, Thermal Foundation, IC2, AE2 e vanilla.
* **Capítulo:** `quests/chapters/atm/atm_star.yaml`, `order: 925`, prefixo `ATM`, 126 quests.
  É o **pseudo-final**: a quest `atm_star` é `finale` (efeito 6, banner "O Registro está inteiro!",
  100 xp + 3 baús lendários) e `deps_hidden` em `atm_allthemodium:allthemodium`.
* Metais, dimensões, Piglich, ligas e equipamento ficam em `atm_allthemodium`; aqui só há
  quests de **quantidade para a Estrela** que dependem delas.

## Árvore real (NEI)
Estrela ← 5 fragmentos distintos (`atm_star_shard_1..5`, sem forma):
1. Alma de Dragão + Semente Dimensional + 3 Blocos Liga U-A 1x + 1 Bloco Liga U-A.
2. Dispositivo de Prob. Improvável + Emissor Nexium + 3 Estrela do Nether 3x + 1 Estrela 4x.
3. Buraco Negro Pulsante + Frag. do Esquecimento + Bloco Liga U-V + Awakened Core + 3 Estrela 3x.
4. Combustível do Filósofo + Bússola do Wither + Bloco Liga U-V + Awakened Core.
5. Patrick Estrela (9 lãs) + AllTheCatalystium.
Componentes pedem: Awakened Core (10 no total), Irídio, Endério, Almário, Liga Vibrante, blocos
compactados (netherrack 6x, end 5x, esmeralda 4x, obsidiana 5x, diamante 4x...) e Corações de Piglich.

## Progressão
Abertura → gateways (outros mods → matérias-primas) → escadas de compactados → componentes →
5 fragmentos (`milestone`) → `atm_star` → `a_pagina_em_branco` (secreta, anuncia o Epílogo).

## Armadilhas
* Fragmento 2 ≈ 79 mil Estrelas do Nether; no total ≈ 99 mil. Exige fazenda de Wither e AE2.
* A liga U-A do Fragmento 1 são 252 lingotes, cada um gasta um Coração de Piglich.
* Bússola do Wither mantém Wither em quem a segura (guardar fora da mão).
* Quantidades de blocos compactados saem da receita (`:n` = (n+1)x); conferir no NEI.

## Marcos exportados
`atm_star:fragmento_1` … `atm_star:fragmento_5`, `atm_star:atm_star`, `atm_star:a_pagina_em_branco`.
