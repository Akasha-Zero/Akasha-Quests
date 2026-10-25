// Akasha Quests — patch 02: Quartzo Queimado (Extra Utilities) sem receita no pack.
// O Et Futurum sobrescreve a receita de fornalha Bloco de Quartzo -> Quartzo Queimado
// (a fornalha passa a devolver Quartzo Liso). Esta receita alternativa restaura o acesso a
// Matriz Computacional, QED, Pedreira do Fim e Portal do Último Milênio.
// Copie para scripts/ do servidor e do cliente.

// 1 Bloco de Quartzo + 1 Pó de Blaze = 1 Quartzo Queimado
recipes.addShapeless(<ExtraUtilities:decorativeBlock1:2>, [<minecraft:quartz_block>, <minecraft:blaze_powder>]);
