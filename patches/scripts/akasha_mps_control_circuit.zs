// Akasha Quests — patch 03: Circuito de Controle do Modular Powersuits inobtível.
// A receita do MPS usa o oredict componentControlCircuit como saída e entrega o Circuito de
// Controle Avançado do Mekanism; a Mesa do MPS exige o item exato do MPS.
// Conversão 1:1 nos dois sentidos (shapeless). Copie para scripts/ do servidor e do cliente.
recipes.addShapeless(<powersuits:powerArmorComponent:14>, [<Mekanism:ControlCircuit:1>]);
recipes.addShapeless(<Mekanism:ControlCircuit:1>, [<powersuits:powerArmorComponent:14>]);
