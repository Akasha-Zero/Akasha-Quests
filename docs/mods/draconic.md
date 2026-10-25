# Draconic Evolution — estudo do capítulo `draconic`

## Visão geral

* **Mod:** Draconic Evolution `1.5.33-GTNH` (fork GTNH do DE 1.0.2 para 1.7.10; namespace
  `DraconicEvolution`). **Sem tradução pt_BR** no JAR (só de/en/fr/ru/zh) — os nomes de itens
  aparecem em inglês no jogo e o capítulo os usa assim.
* **Capítulo:** `quests/chapters/tech/draconic.yaml`, `order: 385`, **145 quests** (21 main,
  61 normal, 41 optional, 11 info, 9 milestone, 2 boss).
* **Papel na campanha:** começa no fim do Ato II (draconium, Draconic Core), sobe pelo Ato III
  (Wyvern, que exige Estrela do Nether) e termina no Ato IV (Awakened/Chaotic, Guardião do Caos,
  Reator). É uma das três provas do **Selo do Abismo** (Wither, Dragão do Fim, Chaos Guardian) e
  fornece o **Awakened Core** usado em muitos componentes da **Estrela ATM**.
* **Não existe Fusion Crafting** nesta versão (é do DE 1.10+). Todo o crafting é de bancada; o
  Draconium Despertado só sai do **Ritual do Despertar** (Dragon Heart) e o Bloco Carregado só do
  **Energy Infuser**. O NEI do pack não tem handler de máquina do DE além do "Reactor".

Fontes: registro do pack (`reg.py mod DraconicEvolution`, receitas de bancada/fornalha, NEI),
`config/DraconicEvolution.cfg`, `config/DraconicEvolution.Balance.cfg`, o manual embutido
(`config/draconicevolution/manual.json`, igual a `assets/draconicevolution/manual/manual-en_US.json`)
e bytecode (`MinecraftForgeEventHandler.getDropChanceFromItem`, `ChaosCrystal`, `ContainerReactor`).

## Energia / recursos-base

* **RF** em tudo. Valores da config Balance: ferramentas/armas Wyvern 1 M RF (+500 k por
  melhoria, 3 melhorias), Dracônicas 10 M RF (+5 M, 6 melhorias); Wyvern Flux Capacitor 80 M RF,
  Draconic Flux Capacitor 250 M RF; Energy Infuser 10 M RF/t; Bloco de Draconium → Carregado
  custa 100 M RF.
* **Energy Core (multibloco)**: 7 tiers — 45,5 M / 273 M / 1,64 G / 9,88 G / 59,3 G / 356 G /
  2,14 T RF. Blocos (manual): T1 0/0; T2 6 Draconium; T3 26 Draconium; T4 26 redstone + 54
  draconium; T5 80 + 98; T6 133 + 150; T7 328 + 218. Config: bloco interno `redstone_block`,
  externo `DraconicEvolution:draconium`. 4 Particle Generators alinhados a ≤10 blocos; Energy
  Pylons a ≤15 blocos com vidro do lado do núcleo; Shift + clique vazio num gerador ativa.
* **Rede sem fio** (Crystal Binder): Relay (alcance 25, 10 conexões), Advanced Relay (50, 20),
  Transceiver (25, 2), Advanced Transceiver (50, 4; 5× mais rápido), Wireless (15 de alcance sem
  fio, 8 máquinas), Advanced Wireless (30, 16).

## Progressão (ids reais)

1. `draconiumOre` → `draconiumDust` (fornalha) → `draconiumIngot` → `draconium` (bloco),
   `nugget`. Atalhos do pack: minério + `ThermalFoundation:material:512` (Pyrotheum) → lingote;
   `ImmersiveEngineering:tool` (martelo) + minério → pó; `magicalcrops:DraconiumEssence` ×9 → lingote.
2. `draconicCore` (4 ouro, 4 draconium, diamante) → dispositivos: `energyInfuser`,
   `upgradeModifier`, `particleGenerator`, `draconiumChest`, `generator:3`, `grinder:3`,
   `weatherController`, `playerDetector(Advanced)`, `dissEnchanter`, `teleporterMKI`,
   `teleporterStand`, `dislocatorReceptacle`, `magnet`, `dislocatorInhibitor`, `wrench`,
   `energyCrystal:*`, `flowGate`/`flowGate:6`, `draconiumEnergyCore` (Wyvern Energy Core).
3. `wyvernCore` (Estrela do Nether + 4 Draconic Cores) → ferramentas/armadura Wyvern (base de
   diamante + Wyvern Core + Wyvern Energy Core), `draconiumFluxCapacitor`, `teleporterMKII`,
   `energyCrystal:1/3/5`, `energyStorageCore`, `energyPylon`, `draconium:1` (Resurrection Stone:
   4 Mob Souls + 4 Wyvern Cores + bloco).
4. `draconium:2` (Charged Draconium Block, no Energy Infuser) + `dragonHeart` → Ritual do
   Despertar → `draconicBlock` (Awakened Draconium) → `draconicIngot`, `nugget:1`.
5. `awakenedCore` (4 Wyvern Cores + 5 lingotes despertados) → `draconiumEnergyCore:1` →
   ferramentas/armadura Dracônicas (a partir das Wyvern; machado e enxada a partir das de
   diamante), `draconicDistructionStaff`, `draconiumFluxCapacitor:1`, `magnet:1`, `sunDial`.
6. `chaosShard` (Chaos Crystal da Ilha do Caos: 5 shards) → `chaoticCore` (4 Awakened Cores) →
   `reactorCraftingPart:0..4` → `reactorStabilizer` (×4), `reactorEnergyInjector`, `reactorCore`.

## Materiais e geração de mundo (config)

* Draconium Ore: Overworld (peso 2, abaixo de Y 8), Nether (5), Fim (10). Nenhuma dimensão em
  blacklist/whitelist. Dropa 1–3 pó (Fortuna III: 4–12).
* Cometas Ender ativos, 1 em 10 000 chunks. Ilhas do Caos ativas, separação 10 000 blocos (grade:
  x ou z múltiplos de 10 000 no Fim).
* Mob Soul: chance 1/1000 (passivos 1/800), aumentada por Wyvern Sword/Bow (+1), Draconic
  Sword/Bow (+2), Staff (+3) e pelo encantamento Reaper (id 180).

## Máquinas, estruturas e multiblocos

* **Ritual da Ressurreição:** Resurrection Stone + 4 glowstone, 4 obsidiana, 4 blocos de diamante,
  4 Charged Draconium, 12 pilares de quartzo (≤5 blocos), a ≤150 blocos de 0,0 no Fim. Invoca
  `DraconicEvolution.EnderDragon`, que fica mais forte a cada invocação.
* **Ritual do Despertar:** Dragon Heart no chão + explosão; 4–16 Draconic Cores (4 por bloco);
  1–4 Charged Draconium Blocks; o coração destrói itens que não sejam núcleos; 1 ritual por coração.
* **Reator:** núcleo + 4 estabilizadores (virados para o núcleo, ≤8 blocos, ≥4 de distância) +
  injetor + 2 Flux Gates. Combustível até 8 blocos de Awakened Draconium; temperatura ideal
  ~8 000; não deixar a conversão chegar a 100%; explosão de 20 a 200 blocos (config
  `EnableBigExplosion=true`, `enableAutomation=false`). O caos convertido sai como Chaos Fragments.

## Dimensões, estruturas e chefes

* **Dragão do Fim** (dropa Dragon Heart; também o dragão ressuscitado).
* **Chaos Guardian** (`DraconicEvolution.ChaosGuardian`): cristais de cura com 50 de vida e escudo,
  respawn de cristais ativo (config), dano perfurante que só a armadura Dracônica bloqueia, drena
  RF. Quebrar o Chaos Crystal destrói a ilha em segundos.

## Integrações no pack

* **Awakened Core** é ingrediente (NEI) de: Pulsating Black Hole, Nexium Emitter, Dimensional Seed,
  Dragon Soul, Philosopher's Fuel, Oblivion Shard, Improbable Probability Device, ATM Star Shard
  3/5 e 4/5, AllTheCatalystium e Raw Unobtainium (Alloy Smelter). Destacado em `nucleo_despertado`
  e `estrela_atm_info`.
* Draconic Chest pede `IronChest:BlockIronChest:6` (Baú de Obsidiana).
* Draconium via Thermal (Pyrotheum), IE (martelo), RotaryCraft (pó) e Magical Crops (essência).

## Armadilhas e dicas

* Disenchanter danifica (e pode destruir) itens de energia.
* Explosão do arco quebra blocos (config). Pigmen Blood Rage ligada (não comer porco perto deles).
* Mob Grinder sem Looting (config). Orbes de XP somem em 30 s.
* Escudo: "last stand" só funciona com ≥10 M RF na armadura.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `draconic:dispositivos_draconicos` | milestone | Energy Infuser, Upgrade Modifier, Wyvern Energy Core e Draconic Chest. |
| `draconic:armadura_wyvern` | milestone | Armadura Wyvern completa (Ato III). |
| `draconic:ferramentas_wyvern` | milestone | Ferramentas Wyvern. |
| `draconic:nucleo_ativo` | milestone | Núcleo de Energia montado. |
| `draconic:dragao_ressuscitado` | boss | Dragão invocado pelo ritual e derrotado. |
| `draconic:draconio_despertado` | milestone | Primeiro Awakened Draconium (Ritual do Despertar). |
| `draconic:cajado_poder` | milestone | Draconic Staff of Power. |
| `draconic:armadura_draconica` | milestone | Armadura Dracônica (voo). |
| `draconic:guardiao_caos` | boss | **Chaos Guardian — marco do Selo do Abismo.** |
| `draconic:reator_draconico` | milestone | Reator Dracônico completo. |
| `draconic:mestre_draconico` | milestone | **Marco final** do capítulo (Chaotic Core + Staff + armadura + reator + Guardião). |

Referência extra (tipo `main`, não marco): `draconic:nucleo_despertado` — Awakened Core, ponte
para a Estrela ATM.
