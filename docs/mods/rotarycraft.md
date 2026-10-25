# RotaryCraft — estudo do capítulo `rotarycraft`

## Visão geral

* **Mod:** RotaryCraft V33a (`RotaryCraft 1.7.10 V33a-ForbiddenFix1.jar`), requer DragonAPI V33b
  (ForbiddenFix1). Namespace de registro `RotaryCraft:rotarycraft_*`.
* **Capítulo:** `quests/chapters/tech/rotarycraft.yaml`, `order: 345`, 192 quests.
* **Papel na campanha:** Ato II (aço, motores, processamento) até Ato IV (turbina de avião,
  rocha matriz). Base técnica do ReactorCraft e do ElectriCraft. Alimenta o **Selo da Engrenagem**.
* Fontes: registro do pack, dump de receitas de bancada, NEI (`rcblastf`, `rcgrinder`,
  `rcferment`, `rccentri`, `rccompactor`, `rcpulsej`, `rcfriction`, `rcextract`…), Manual
  interno (`Reika/RotaryCraft/Resources/*.xml`, en e pt_BR), lang pt_BR do JAR, config
  `config/Reika/RotaryCraft.cfg` e bytecode (`EngineType`, `PowerReceivers`, `MaterialRegistry`,
  `TileEntityBlastFurnace`, `TileEntityHeater`). **O handler NEI "Worktable" tem 0 receitas no
  dump**: receitas de máquinas/motores/eixos/caixas (Mesa de Trabalho) não foram conferidas —
  as quests as descrevem de forma geral e remetem ao NEI/Manual.

## Energia / recursos-base

Potência de eixo: **W = Nm × rad/s**; não se acumula; máquinas direcionais. Motores (bytecode):

| Motor | Item | Torque | Velocidade | Potência |
| --- | --- | --- | --- | --- |
| CD Elétrico | `item_engine` | 4 Nm | 256 | 1.024 W (redstone contínuo) |
| Turbina Eólica | `item_engine:1` | 8 | 1.024 | 8 kW (altura/campo aberto) |
| Motor a Vapor | `item_engine:2` | 32 | 512 | 16 kW (água + calor; 150 °C) |
| Motor a Gasolina | `item_engine:3` | 128 | 512 | 65 kW (cristais de etanol) |
| Motor CA | `item_engine:4` | 512 | 256 | 131 kW (relógio + núcleo magnetizado) |
| Performance | `item_engine:5` | 256 | 1.024 | 262 kW (etanol+água+aditivo; 240 °C) |
| Roda D'água | `item_engine:6` | 16.384 | 32 | 524 kW (queda 64+, lubrificante) |
| Microturbina | `item_engine:7` | 16 | 131.072 | 2,1 MW (jato) |
| Turbina de Avião | `item_engine:8` | 1.024 | 65.536 | 67 MW (jato; ingestão) |

Limites de eixo (calculados do bytecode `MaterialRegistry`): madeira ~278 Nm/3.577 rad/s; pedra
~958/11.561; aço ~6.711/57.005; tungstênio ~9.587/314.244; diamante ~69.508/~4,1 M; rocha matriz
infinito. Mínimos de máquinas (`PowerReceivers`): Triturador 128 Nm/4 kW; Fermentador 32 rad/s/1 kW;
Centrífuga 4.096 rad/s/16 kW; Compactador 4.096 Nm/262 kW; Extrator estágios 512 Nm·65 kW /
2.048 rad/s·16 kW / 8.192 rad/s·32 kW / 256 Nm·65 kW; Fracionador 8.192 rad/s/65 kW; Aquecedor
16 Nm/8 kW; Friccionador 32 Nm/8 kW; Bomba 8 Nm/1 kW; Fornalha de Pulso 131.072 rad/s;
Quebrador de Rocha Matriz 16.384 Nm/4,19 MW; Bate-Estacas torque mínimo 80.000 Nm (não citado
nas quests por cautela).

## Progressão (ids reais)

1. Forno de Calor `item_machine:45` (bancada: 8 tijolos de pedra + redstone) → Aço HSLA
   `item_shaftcraft:1` (ferro + carvão/coque + pólvora + areia, ≥600 °C; lava encostada soma
   ~600 °C). Coque `item_compacts:8`. Mesa de Trabalho `item_machine:75`.
2. Peças na bancada: Painel Base, Engrenagem `gearcraft:1`, Haste `gearcraft`, Núcleo
   `gearcraft:7`, rolamentos, unidades 2x–16x, impelidor, cilindro, ignição, radiador…
3. Motor CD → Motor a Vapor + Caixa 4:1 → Triturador → canola (grama alta) → lubrificante.
4. Fermentador (fermento → lodo `powders:2`) → Centrífuga → extrato `powders:16` → fornalha →
   Cristais de Etanol → Motor a Gasolina (marco `combustao`).
5. Extrator (flocos; bônus tungstênio do ferro, prata do ouro, alumínio da redstone) →
   Friccionador funde tungstênio `compacts:5`; silício (`powders:14` → `compacts:10`).
6. Motor CA (Magnetizador), Performance, Roda D'água, Compactador (carvão → diamante).
7. TCV, volantes, correias/correntes, molas/ferramentas de mola.
8. Fracionador → combustível de jato `item_bucket:1` → Microturbina → Liga de Tungstênio →
   Turbina de Avião; Fornalha de Pulso → Vidro Anti-Explosão.
9. Quebrador de Rocha Matriz → pó `powders:4` → lingote `compacts:3` (1.450 °C) → ferramentas,
   armaduras, eixos eternos.

## Materiais e geração de mundo

RotaryCraft não gera minérios próprios (metais vêm do Extrator: `item_modextracts/modingots`).
Canola: semente de grama (`MinecraftForge.addGrassSeed`, confirmado no bytecode).

## Máquinas e multiblocos

Torre Solar (`item_machine:71`) + Espelhos (`:70`). Conversores: Dínamo Rotacional (RF),
Motor Magnetostático, Gerador/Motor Elétrico (EU), Caldeira de Fricção/Turbina a Vapor, Motor a
Combustível. Perda de conversão 0% (config).

## Integrações no pack

ReactorCraft e ElectriCraft usam Mesa de Trabalho e HSLA; Thermal (RF, Enderium), IC2 (EU),
Forestry (`rotarycraft=true`), EnderIO reconhece a Picareta de Rocha Matriz para spawners;
Avaritia tem `Rotarycraft=true`.

## Armadilhas e dicas (config)

* `Crafting Table can Make Machines=false`; `Allow Bedrock Breaker to Break Y=0=false`;
  `Allow Bedrock Pickaxe to Harvest Spawners=true`; `Lock enchants on bedrock tools=true`;
  `Spawn with RC Handbook=true`; `GPR Renders Ores=true`; dano a blocos por falhas ligado;
  Chunk Loader raio 8; `Power Converter Loss Percent=0`.
* Aquecedor derrete acima de 2.000 °C. Turbina de Avião suga entidades. Aço não aguenta a
  velocidade de jato. pt_BR do jogo tem erros ("Moto CA", "Minarador Vertical", "Forno de Calor"
  para Blast Furnace).

## Marcos exportados

| Marco | Representa |
| --- | --- |
| `rotarycraft:era_do_aco` | Aço HSLA, Mesa de Trabalho e engrenagens. |
| `rotarycraft:primeira_rotacao` | Motor a Vapor + caixa + Triturador. |
| `rotarycraft:combustao` | Cadeia do etanol e Motor a Gasolina. |
| `rotarycraft:forca_motriz` | Motor CA, Performance e Compactador (centenas de kW). |
| `rotarycraft:turbina_aviao` | Turbina de Avião (67 MW). |
| `rotarycraft:inquebravel` | Ferramentas/armadura/eixos de rocha matriz. |
| `rotarycraft:mestre_rotacional` | **Marco final** — para o Selo da Engrenagem. |
