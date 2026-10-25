# Thermal Expansion + Thermal Foundation — estudo do capítulo `thermal`

## Visão geral

* **Mods:** Thermal Expansion `1.7.10R4.1.5` (`ThermalExpansion`), Thermal Foundation `1.7.10R1.2.6`
  (`ThermalFoundation`), CoFH Core `1.7.10R3.1.4` (+ CoFHLib). **Não há Thermal Dynamics no pack**
  (conferido em `mods/` e em `reg.py mods`): não existem condutos/ducts do Thermal.
* **Capítulo:** `quests/chapters/tech/thermal.yaml`, `order: 315`, 167 quests.
* **Papel na campanha:** Ato I (metais, Pulverizador, Fundição, RF) → Ato II (fluidos, elementais,
  níveis Endurecido/Reforçado) → fim de Ato II/início de Ato IV (Endério, nível Ressonante,
  Tesserato). Alimenta o **Selo da Engrenagem** pelo marco final.

Fontes: registro do pack (`reg.py mod ThermalExpansion/ThermalFoundation`), receitas de bancada do
dump, catálogo NEI (Pulverizador, Fundição Induzida, Transpositor, Crisol, Insolador, receitas de
máquina/atualização), `config/cofh/thermalexpansion/common.cfg` e `fuels.cfg`,
`config/cofh/world/ThermalFoundation-Ores.json`, lang do JAR e bytecode (`javap`) de
`TECraftingHandler`, `BlockMachine`, `TEAugments`, `TileMachineBase`, `BlockFrame`,
`SmelterManager`, `CrucibleManager`, `TransposerManager` e das entidades Blizz/Blitz/Basalz.

## Energia / recursos-base

* **RF (Redstone Flux)**, sem tiers de tensão (nada explode por "voltagem").
* Dínamos (base 80 RF/t, `fuels.cfg` do pack):
  * Vapor (`Dynamo:0`, cobre): carvão 48 000, carvão vegetal 32 000, madeira 4 500; precisa de água (ou vapor).
  * Magmático (`Dynamo:1`, Invar): lava 180 000/balde, Pyrotheum 2 000 000/balde.
  * Compressão (`Dynamo:2`, estanho): combustível BC 1 500 000, carvão liquefeito 1 000 000,
    biofuel/bioetanol 500 000, petróleo 150 000; refrigerante água 400 000, Cryotheum 4 000 000, gelo 2 000 000.
  * Reagente (`Dynamo:3`, bronze): fluidos (redstone 600 000, glowstone 750 000, biomassa, creosoto,
    óleo de sementes, essência de mob, esgoto/lodo) + reagente sólido.
  * Enervação (`Dynamo:4`, Electro): redstone 64 000 RF e itens com RF.
* Células (config do pack): Leadstone `Cell:1` 400 k (200 RF/t), Endurecida `Cell:2` 2 M (800),
  Redstone `Cell:3` 20 M (8 000), Ressonante `Cell:4` 80 M (32 000).
* Capacitores: batata 32 k (não recarrega), Leadstone 80 k, Endurecido 400 k, Redstone 4 M, Ressonante 20 M.

## Progressão (ids reais)

1. **Metais TF:** lingotes `ThermalFoundation:material:64–76` (cobre, estanho, prata, chumbo,
   ferroso=níquel, brilhante=platina, mana infundida, Electro, Invar, bronze, Signalo, Lúmio, Endério).
   Pós 32–44 (misturas 39–44), pepitas 96–108, engrenagens 12/13/128–140 (4 lingotes + 1 ferro).
2. **Primeira máquina:** Martelo Tramontina `ThermalExpansion:wrench`, Servo `material:0`, Bobinas
   Receptora/Transmissora/Condutora `material:1/2/3`, Estrutura Básica `Frame:0`
   (ferro + vidro + engrenagem de estanho). Molde de máquina (bytecode `BlockMachine`):
   ` X ` / `YCY` / `IPI` com C = estrutura, I = engrenagem de cobre, P = bobina receptora
   (servo no Extrusor e Acumulador). X/Y por máquina: Fornalha redstone/tijolo; Pulverizador
   pistão/sílex; Serraria machado/tábuas; Fundição balde/Invar; Crisol estrutura de Leadstone/tijolo
   do Nether; Transpositor balde/vidro; Precipitador pistão/Invar; Extrusor pistão/vidro; Acumulador
   balde/vidro; Montador baú/engrenagem de estanho; Infusor estrutura de Leadstone/bobina
   transmissora; Insolador engrenagem de Lúmio/terra.
3. **Ligas na Fundição Induzida** (`SmelterManager`): Invar = 2 ferro + 1 níquel → 3; Electro =
   ouro + prata → 2; bronze = 3 cobre + estanho → 4; aço = ferro + 2 carvões pulverizados (ou 4 de
   vegetal) → 1; Vidro Endurecido = chumbo + 4 obsidianas pulverizadas → 2 (Lúmio → vidro de Lúmio);
   Endério = 2 misturas + 1 pó de Pyrotheum → 2. Minério + areia → 2 lingotes; + Escória Rica ou
   Cinábrio → 3 (`SpecialMultiplier=3`).
4. **Misturas de bancada:** Invar (2 pó de ferro + pó ferroso → 3), Electro (2), bronze (4), Signalo
   (3 cobre + prata + balde de Redstone Desestabilizada → 4), Lúmio (3 estanho + prata + balde de Pedra
   Luminosa Energizada → 4), Endério (2 estanho + prata + platina + balde de Ender Ressonante → 4).
   As misturas 39–43 fundem na fornalha; a de Endério **só** na Fundição com Pyrotheum.
5. **Fluidos:** Crisol (`CrucibleManager`): redstone → 100 mB redstone desestabilizada (bloco 900),
   pó de pedra luminosa → 250 mB (bloco 1 000), pérola → 250 mB Ender Ressonante, pós elementais →
   250 mB, carvão pulverizado → 100 mB carvão liquefeito, pedregulho/pedra/obsidiana/Netherrack → lava.
   Transpositor enche baldes `ThermalFoundation:bucket:0–8` e faz: arenito + Ender → Pedra do Fim,
   gelo + Cryotheum → gelo compactado, pó de pedra luminosa + redstone fluida → pó de blaze.
6. **Elementais:** Pyrotheum = carvão pulverizado + enxofre + redstone + pó de blaze → 2;
   Cryotheum = bola de neve + salitre + redstone + pó de Blizz → 2; Aerotheum = areia + salitre +
   redstone + pó de Blitz → 2; Petrotheum = argila + obsidiana pulverizada + redstone + pó de Basalz → 2.
7. **Níveis:** estruturas `Frame:1/2/3` e atualização de máquinas com o mesmo padrão `IGI / X / I I`
   (`TECraftingHandler`): Endurecida = Invar + engrenagem de Electro; Reforçada = Vidro Endurecido +
   engrenagem de Signalo; Ressonante = prata + engrenagem de Endério. O nível é NBT `Level` (0–3) no
   item `ThermalExpansion:Machine:N`; o capítulo usa `{get: 'ThermalExpansion:Machine:* {Level:Nb}', nbt: true}`.
8. **Armazenamento:** Tanque `Tank:1–4` (8 k/32 k/128 k/512 k mB), Caixa Forte `Strongbox:1–4`,
   Estoque `Cache:1–4` (10 k/40 k/160 k/640 k itens), Sacola `satchel:1–4`, Bancada do Maquinista
   `Workbench:1–4` (atualizações: Invar → Vidro Endurecido/Signalo → Endério).
9. **Células:** estruturas `Frame:4` (Leadstone: chumbo + vidro + bloco de redstone), `Frame:5`,
   `Frame:6` (Electro + vidro endurecido + diamante) → `Frame:7` (Transpositor, 4 000 mB redstone),
   `Frame:8` → `Frame:9` (idem). Tesserato: `Frame:10` (Endério + vidro endurecido + diamante) →
   `Frame:11` (1 000 mB Ender Ressonante) + 4 bronze + 4 prata → `Tesseract`.

## Aumentos (bytecode `TEAugments` / `TileMachineBase`)

* Slots por nível de máquina: 3 / 4 / 5 / 6 (`AUGMENT_COUNT`).
* Nível do aumento ≤ nível da máquina (I exige Endurecida, II Reforçada, III Ressonante). Dínamos não
  têm nível. Nível II/III exige os anteriores instalados (`hasAugmentChain`); sem duplicatas.
* Metas: 0/1/16/32 integrados (auto-saída, auto-entrada, lados, redstone) — **já vêm de fábrica no
  pack** (`Augments.Default.* = true`); 48 bobina para dutos; 49 limitador (throttle); 64–66
  eficiência de dínamo (+10/+35/+85% acumulado); 80–82 potência de dínamo (×2/×4/×8 RF/t e consumo,
  −15/−25/−30% eficiência); 112–114 saída secundária; 128–130 velocidade (×2/×4/×8; +50/+100/+150% de
  energia por operação); 144 nulificador de secundários; 256 fornalha só comida (−50% energia);
  312–314 Extrusor (lotes até 16/32/64, água 500/250/125).

## Materiais e geração de mundo (`config/cofh/world/ThermalFoundation-Ores.json`)

Todos com `retrogen`, fora do Nether (−1) e do Fim (1):

| Minério | Veio × por chunk | Y |
| --- | --- | --- |
| Cobre | 8 × 10 (Y 40–75) + 8 × 8 (Y 48–96) | 40–96 |
| Estanho | 8 × 8 | 20–55 |
| Prata (80% prata / 20% chumbo) | 8 × 6 | 5–30 |
| Chumbo (90% chumbo / 10% prata) | 8 × 8 | 10–35 |
| Ferroso (níquel) | 4 × 3 | 5–20 |
| Brilhante (80% platina / 20% ferroso), sparse-cluster | 3 × 1 | 5–30 |

**Mana Infundida (mithril) não gera** (não há entrada no JSON).

Elementais (bytecode das entidades; config `Mob`): Blizz em biomas COLD/SNOWY (bastão + bolas de
neve), Blitz em PLAINS/SANDY (bastão + salitre), Basalz em MOUNTAIN/WASTELAND (bastão + obsidiana
pulverizada); luz ≤ 8, grupos 1–4, peso 10. Subprodutos do Pulverizador (NEI): enxofre (carvão,
Netherrack, quartzo, bastão de blaze…), salitre (arenito), cinábrio (minério de redstone e de ouro).

## Integrações no pack

* RF é aceito por EnderIO, Mekanism, AE2, Draconic, BuildCraft 7, Extra Utilities etc.
* Transporte sem Thermal Dynamics: condutos do Ender IO, transmissores do Mekanism, tubos do BuildCraft
  (cinéticos), nós/tubos do Extra Utilities, Omni Pipes (`OminiPipes.jar`, mod do pack).
* Oredict amplo (`ingotCopper`, `gearIron` etc.). EnderIO Alloy Smelter também faz Vidro Endurecido e
  Endério; QED do Extra Utilities funde minério TF com carvão (×3).

## Armadilhas e dicas

* `RequireDiamonds=false` (Pulverizador com sílex), `UseIngots=false` (receitas pedem engrenagens).
* Acumulador sem geração passiva (`PassiveGeneration=false`): precisa de 2 fontes de água adjacentes.
* Extrusor: pedregulho grátis; pedra gasta 1 000 mB de água; obsidiana gasta 1 000 de lava e de água.
* Ender Ressonante teleporta quem entra; Pyrotheum queima; Cryotheum congela.
* Nomes pt_BR do jogo: Electrum = "Electro", Signalum = "Signalo", Lumium = "Lúmio", Enderium =
  "Endério", Crescent Hammer = "Martelo Tramontina (Chave Inglesa)".

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `thermal:primeira_oficina` | milestone | Pulverizador + Fundição + Célula de Leadstone + Invar/Electro (Ato I). |
| `thermal:oficina_endurecida` | milestone | Máquina Endurecida, Célula Endurecida, Dínamo Magmático, Velocidade I (Ato II). |
| `thermal:oficina_reforcada` | milestone | Máquina Reforçada, Célula de Redstone, Velocidade II. |
| `thermal:tesserato` | milestone | Tesserato (transporte entre dimensões). |
| `thermal:mestre_termico` | milestone | **Marco final**: máquina Ressonante + Célula Ressonante + Tesserato + Velocidade III — candidato ao **Selo da Engrenagem**. |
