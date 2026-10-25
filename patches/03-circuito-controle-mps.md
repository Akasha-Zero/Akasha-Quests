# Patch 03 — Circuito de Controle do Modular Powersuits inobtível

**Gravidade:** média (bloqueia módulos do MPS: Controle de Voo, Visão Noturna, geradores,
Ímã, Escâner de Minérios...).

O Circuito de Controle do MPS (`powersuits:powerArmorComponent:14`) e o Circuito de Controle
Avançado do Mekanism (`Mekanism:ControlCircuit:1`) compartilham o oredict
`componentControlCircuit`. No pack, a receita resulta no item do Mekanism, e nenhuma receita
(bancada, fornalha ou NEI) produz o item do MPS — mas a Mesa do MPS compara o item exato.

**Correção:** `patches/scripts/akasha_mps_control_circuit.zs` adiciona a conversão 1:1 entre
os dois circuitos. Seguro em mundos existentes. A quest `mps:circuito_controle` explica a
situação ao jogador; após o patch ela continua válida.
