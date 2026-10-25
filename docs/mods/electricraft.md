# ElectriCraft — estudo do capítulo `electricraft`

## Visão geral

* **Mod:** ElectriCraft V33a, namespace `ElectriCraft:electricraft_*`. **Sem lang pt_BR** (nomes
  em inglês no jogo). Exige RotaryCraft.
* **Capítulo:** `quests/chapters/tech/electricraft.yaml`, `order: 347`, 45 quests; raiz sem deps,
  `manual_ec` com `deps_hidden: rotarycraft:aco_hsla`.
* Fontes: registro, bancada/NEI (cristais, placas sem fio, pó de cristal), Manual interno,
  config, bytecode (`ElectriOres`, `WireType`). Fios, baterias, gerador/motor e transformador são
  da Mesa de Trabalho (não conferidos no NEI).

## Energia

Induction Generator (`item_placer:1`): torque → corrente, velocidade → tensão, sem perdas.
Induction Motor (`:2`): inverso; limite de fontes 1 / Indutiva 2 / Tungstênio 4 / Rocha matriz
1000. Não existe motor "DC/AC" separado neste mod (a tarefa pedia; não existe no JAR).

## Fios (bytecode `WireType`, limite de corrente)

Aço 16 A, Estanho 64, Níquel 256, Alumínio 1.024, Cobre 4.096, Prata 32.768, Ouro 65.536,
Platina 131.072, Supercondutor ilimitado/sem perda. Isolados = meta +16.

## Progressão

Minérios (Cobre Y32–64, Estanho 48–72, Níquel 16–48, Prata 0–32, Alumínio 60–128, Platina 0–16;
todos ligados) → Manual → Gerador/Motor → fios → medidor/relé/resistor/fusível/transformador →
Energy Crystal Dust (furnace → cristal de redstone; tiers com glowstone, bloco de lápis, olho do
ender, bloco de esmeralda, estrela do Nether) → baterias Redstone…Auroral → RF crystal/RF Storage
Battery, EU crystal (IC2)/RBSU, EU Splitter → Wireless Energy Transfer Pad (5 tiers, centro
redstone/ouro/diamante/liga indutiva/enderium; usa Silício do RC). "RF bridge" = RF Transfer
Cable + RF Battery + placas sem fio.

## Integrações

RotaryCraft (eixo, HSLA, silício, liga indutiva), IC2 (EU, lapotron), Thermal (Enderium),
ReactorCraft (Gerador de Turbina → EU Splitter).

## Marcos exportados

| Marco | Representa |
| --- | --- |
| `electricraft:eixo_eletrico` | Gerador + fio + motor de indução. |
| `electricraft:bateria_auroral` | Bateria máxima. |
| `electricraft:mestre_eletrico` | **Marco final** — Selo da Engrenagem. |
