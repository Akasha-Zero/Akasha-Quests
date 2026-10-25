# Economia de Recompensas

Objetivo: recompensas **interessantes e estimulantes**, que empurrem o jogador para o próximo
passo sem trivializar a progressão (o padrão "reembolso de mesmo tier" do ATM10).

## 1. Princípios

1. **Útil agora, não daqui a 10 horas.** A recompensa ajuda a fazer a *próxima* quest do
   capítulo (materiais do próximo craft, combustível, uma ferramenta que poupa tempo).
2. **Nunca dê o item que a próxima quest pede.** Dê os *insumos* parciais ou um atalho.
3. **Escolha > aleatório > fixo.** Sempre que houver variantes igualmente úteis (cobre/estanho
   /bronze; três sementes; três ferramentas), use `choice`.
4. **Surpresa com moderação.** Baús de Saque (`loot`) são a "caixa misteriosa": ótimos em
   marcos e chefes, não em toda quest.
5. **Kits nomeados** (`kit`) em marcos de capítulo: um baú com nome temático ("Kit do
   Engenheiro Iniciante") é memorável e organiza o inventário.
6. **XP** em toda quest que não tem outra recompensa forte (níveis, não pontos).

## 2. Tabela por tipo de quest e Ato

| Tipo | Ato I | Ato II | Ato III | Ato IV | Ato V / Epílogo |
| --- | --- | --- | --- | --- | --- |
| `info` (checkbox) | 1 nv | 1 nv | 2 nv | 2 nv | 3 nv |
| `normal` | 1–2 nv + insumos baratos | 2–3 nv + insumos | 3 nv + insumos | 4 nv + insumos | 5 nv + insumos |
| `main` | 3 nv + `choice` | 4 nv + `choice` | 5 nv + `choice` | 6 nv + `choice` | 8 nv + `choice` |
| `optional` | `loot: comum` ou insumo | `loot: comum` | `loot: incomum` | `loot: incomum` | `loot: raro` |
| `milestone` | 5 nv + `kit` + `loot: incomum` | 8 nv + `kit` + `loot: raro` | 10 nv + `kit` + `loot: raro` | 12 nv + `kit` + `loot: epico` | 15 nv + `loot: lendario` |
| `boss` | — | 8 nv + `loot: raro` | 10 nv + `loot: epico` | 15 nv + `loot: epico x2` | 20 nv + `loot: lendario` |
| `portal` | 5 nv + `loot: incomum` | 5 nv + `loot: raro` | 8 nv + `loot: raro` | 10 nv + `loot: epico` | — |
| `finale` | — | — | — | 30 nv + `lendario x2` | 50–100 nv + `lendario x3` |

Ato I = início (ferro, primeiras máquinas); Ato II = aço/eletricidade/infusão; Ato III =
dimensões/chefes médios; Ato IV = endgame de mod; Ato V = ATM/Avaritia.

## 3. Baús de Saque (`quests/loot/loot.yaml`)

Cinco grupos (Comum → Lendário). O BetterQuesting sorteia o grupo pela raridade do baú
(~80% de chance de cair no próprio tier, o resto nos vizinhos) e depois uma entrada do grupo.
Cada entrada é um "pacote" coerente (ex.: 16 lingotes de cobre + 8 de estanho), nunca um
item solto sem uso.

* **Comum:** comida boa, tochas, lingotes básicos, sementes, pérolas, couro, carvão.
* **Incomum:** lingotes de liga (bronze, aço, invar), diamantes, ender pearls, placas de
  circuito, sementes mágicas tier 1–2, vis/essentia básica (cristais), mel/própolis.
* **Raro:** circuitos avançados, blocos de diamante, lingotes thaumium/manasteel, upgrades
  de máquina, livros encantados bons, cápsulas de energia, fragmentos de draconium.
* **Épico:** blocos de metais de endgame, Ultimate solar/painéis, núcleos dracônicos,
  ingredientes de infusão raros, estrelas do Nether, gaia spirit, módulos AE2 avançados.
* **Lendário:** Allthemodium/Vibranium/Unobtainium, Neutrônio (Avaritia), núcleos
  despertos, células de energia enormes, itens de colecionador.

## 4. Comandos

`command` é poderoso: use para **efeitos de celebração** (fogos, som, partículas) e
**efeitos temporários** úteis (ex.: `/effect @p 3 600 1` — Pressa II por 10 minutos após o
primeiro grande processamento de minério). Não use comando para dar itens (use `item`).

## 5. Proibido

* Itens criativos antes do Epílogo.
* Recompensas que pulam um tier inteiro (ex.: painel solar de endgame na quest de cobre).
* Mais de 1 baú `lendario` fora de chefes/finais.
* Recompensas de mods que o jogador não tem motivo para conhecer ainda (sem contexto).
