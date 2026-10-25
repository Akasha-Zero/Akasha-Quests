# Patch 02 — Quartzo Queimado (Extra Utilities) sem receita

**Gravidade:** alta (bloqueia uma parte inteira do Extra Utilities).

O Quartzo Queimado (`ExtraUtilities:decorativeBlock1:2`) só é obtido no mod original
fundindo um Bloco de Quartzo. Neste pack o **Et Futurum** registra a mesma entrada da
fornalha para o Quartzo Liso, e a receita do Extra Utilities some. Nem a bancada, nem a
fornalha, nem nenhum handler do NEI do pack (68 mil receitas verificadas) produzem o item.

Sem ele ficam impossíveis: Matriz Computacional → QED (Ender Constructor) → Pedreira do
Fim, Base de Melhoria da Pedreira, nós de energia, geradores x8/x64, Tocha Magnum, Marcador
do Fim e o Portal do Último Milênio.

**Correção:** `patches/scripts/akasha_burnt_quartz.zs` (CraftTweaker) adiciona
`Bloco de Quartzo + Pó de Blaze → Quartzo Queimado`. É seguro em mundos existentes.
As quests do capítulo `extrautils` já descrevem o problema; depois de aplicar o patch, a
quest `quartzo_queimado` passa a ser cumprível normalmente.
