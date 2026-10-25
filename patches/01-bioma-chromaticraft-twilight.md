# Patch 01 — Conflito de IDs de bioma: ChromatiCraft × Twilight Forest

**Gravidade:** crítica para a campanha (bloqueia a Chave do Crepúsculo).

## Problema

`config/Reika/ChromatiCraft.cfg` registra cinco biomas nos IDs **47–51**, os mesmos IDs que
`config/TwilightForest.cfg` usa:

| ID | Twilight Forest | ChromatiCraft (vence no registro) |
| --- | --- | --- |
| 47 | Stream | Ender Forest |
| 48 | **Snowfield** (Floresta de Neve) | Rainbow Forest |
| 49 | **Glacier** (Geleira) | Luminous Cliffs |
| 50 | Clearing | Luminous Cliffs Shores |
| 51 | Oak Savanna | Rainbow Stream |

O dump do registro (feito com o pack em execução) mostra as classes do ChromatiCraft nesses
IDs. O Twilight Forest procura seus biomas por ID; sem a Floresta de Neve e a Geleira, o covil
do **Yeti Alfa** e o **Palácio da Aurora (Rainha da Neve)** não têm onde gerar — e com eles
ficam impossíveis as Terras Altas e a Chave do Crepúsculo.

## Correção proposta

Mover os biomas do ChromatiCraft para IDs livres (61–65 estão livres no registro):

```
# config/Reika/ChromatiCraft.cfg
S:"Ender Forest Biome ID"=61
S:"Rainbow Forest Biome ID"=62
S:"Luminous Cliffs Biome ID"=63
S:"Luminous Cliffs Edge Biome ID"=64
S:"Rainbow Stream Biome ID"=65
```

## Atenção antes de aplicar

Chunks guardam o **número** do bioma. Num mundo já existente, chunks do Overworld gerados como
biomas do ChromatiCraft passariam a ser lidos como biomas do Twilight (e vice-versa na
dimensão 7). Recomendações:

* **Mundo novo / reset de temporada:** aplique sem medo.
* **Mundo existente:** o Twilight Forest (dimensão 7) pode ser regenerado (apague
  `DIM7/` com o servidor desligado — jogadores perdem o que estiver lá) e as regiões do
  Overworld com biomas do ChromatiCraft ficarão com aparência de bioma do Twilight até serem
  regeneradas. Faça backup.
