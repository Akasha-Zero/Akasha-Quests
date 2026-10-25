# Modular Powersuits — estudo do capítulo `mps`

## Visão geral

* **Mod:** MachineMuse's Modular Powersuits `1.7.10-0.11.1.118-Akashic-QMX.2` (namespace
  `powersuits`) + Numina 0.4.1.105.
* **Capítulo:** `quests/chapters/tech/mps.yaml`, `order: 380`, 57 quests.
* **Papel na campanha:** Ato II/III (traje, mobilidade, ferramentas, armas). Marco final ajuda
  o **Selo da Engrenagem**.

Fontes: registro (`reg.py mod powersuits`), NEI (handler `JSON Shaped`),
`config/machinemuse/recipes/mps-{thermalexpansion,enderio,ic2}.recipes`,
`config/machinemuse/powersuits.cfg`, `custominstallcosts.json`, lang `pt_BR`/`en_US` do JAR e
bytecode dos módulos (`javap` dos construtores: `addInstallCost`) para os custos de instalação.

## Energia / recursos-base

* Energia interna (J); aceita **RF** (`Energy per RF=0.1`) e **EU** (`Energy per IC2 EU=0.4`).
  Peças vazias não guardam energia: precisam de módulo de bateria (Básica = Capacitor LV,
  Avançada = MV, Elite = HV).
* `Weight Limit = 25000 g`, `Maximum Armor per Piece = 6`, `Base Heat Cap = 50`,
  `Salvage Ratio = 0.9`, voo máx. 25 m/s.

## Progressão (ids reais)

* Mesa `powersuits:tile.tinkerTable`; peças `item.powerArmorHelmet/Chestplate/Leggings/Boots`,
  `item.powerFist`.
* Componentes `powersuits:powerArmorComponent:0–21` (Fiação, Solenoide, Servomotor, Asa de
  Planador, Propulsor Iônico, Capacitores LV/MV/HV, Paraquedas, Blindagens, Emissor de Campo,
  Emissor de Holograma, Miofibra, Circuito de Controle (14), Gel, Músculo, Solar Panel, Magnet,
  Computer Chip, Rubber Hose, Liquid Nitrogen).
* Três conjuntos de receitas ativos ao mesmo tempo (Thermal, EnderIO, IC2).
* Custos de instalação (bytecode): Jetpack 4 Propulsores; Bota-Jatos 2; Planador 2 Asas;
  Paraquedas 2; Corrida 4 Servos; Pulo 4 Servos; Escalada 2 Servos; Amortecedor 2 Servos +
  2 lãs (`custominstallcosts.json`); Natação Propulsor + 2 Solenoides; Blink Drive Propulsor +
  2 Emissores; Campo de Energia 2 Emissores; Blindagem Ferro/Diamante 1 placa; Dissipador
  1 Blindagem Básica; Picareta/Pá 3 ferros + Solenoide; Machado/Enxada Solenoide; Tesoura 2
  ferros + Solenoide; Broca de Diamante 3 diamantes + Solenoide; Assistente de Golpes 2 Servos +
  LV; Canhão de Plasma 2 Emissores + 2 HV; Railgun 6 Solenoides + HV; Lança-Lâminas Servo + MV;
  Raios HV + 2 Solenoides + 2 Emissores; Capacitor de Luz glowstone + 2 ferros; Assistência
  Mecânica 4 Músculos; Visão Noturna Holograma + Circuito de Controle.

## Integrações no pack

* Componentes usam materiais de Thermal/EnderIO/IC2; Mekanism compartilha o oredict
  `componentControlCircuit`.

## Armadilhas e dicas

* **Problema encontrado:** a receita JSON do Circuito de Controle tem como resultado o oredict
  `componentControlCircuit`, que no pack resolve para **`Mekanism:ControlCircuit:1`** (o NEI
  mostra as três receitas gerando o circuito avançado do Mekanism). `MuseItemUtils.isSameItem`
  exige item+meta exatos do MPS (`powerArmorComponent:14`), então os módulos que pedem Circuito
  de Controle (Controle de Voo, Visão Noturna, geradores, Ímã, Auto-Alimentador, Relógio,
  Bússola, Escâner de Minérios, etc.) provavelmente **não são instaláveis**. O capítulo evita
  depender dele e avisa o jogador (quest `mps:circuito_controle`).
* Calor excessivo causa fogo; peso acima de 25 kg deixa lento.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `mps:traje_protegido` | milestone | Traje completo com bateria e proteção. |
| `mps:em_movimento` | milestone | Jetpack, corrida, planador e paraquedas. |
| `mps:arsenal_traje` | milestone | Ferramentas + Canhão de Plasma no Power Fist. |
| `mps:mestre_traje` | milestone | **Marco final** — para o **Selo da Engrenagem**. |
