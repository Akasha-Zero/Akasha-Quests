# Matter Overdrive — estudo do capítulo `matteroverdrive`

## Visão geral

* **Mod:** MatterOverdrive: Community Edition (`MatterOverdrive.jar`, versão "@VERSION@"),
  namespace `mo`. O mod **não tem pt_BR**: nomes em inglês no jogo.
* **Capítulo:** `quests/chapters/tech/matteroverdrive.yaml`, `order: 375`, 98 quests.
* **Papel na campanha:** Ato II (tritânio, dilítio, inscritor) → Ato III (replicação,
  transporte, armas) → Ato IV (androides, fusão, galáxia). O marco final ajuda o **Selo da
  Engrenagem**.

Fontes: registro (`reg.py mod mo`), catálogo NEI (crafting + `mo_inscriber` + máquinas de
outros mods), **guias internos** do JAR (`assets/mo/info/en_US/*.xml` — o conteúdo do Data
Pad: decomposer, replicator, analyzer, pattern storage, fusion reactor, anomalia, transporter,
ores, armas, upgrades, androides), `assets/mo/lang/en_US.lang` (habilidades biônicas, diálogos
do Mad Scientist, galáxia) e `config/MatterOverdrive/Matter Overdrive.cfg`.

## Energia / recursos-base

* **RF** em todas as máquinas. Decomposer 8000 RF por item, Replicator 16000 RF por cópia
  (config). Baterias: 524 288 e 1 048 576 RF (NBT). Painel Solar. Reator de Fusão
  (`output.energy=2048`, `storage.energy=1e8`).
* **Matter Plasma** (`matter_plasma`, fluido): sai do Decomposer, alimenta Replicator e
  reator. Valores de matéria calculados automaticamente (receitas/fornalha).

## Progressão (ids reais)

1. `mo:tritanium_ore` (Y < 64) → `mo:tritanium_ingot` (oredict `ingotTritanium`) →
   `mo:tritanium_plate`; `mo:dilithium_ore` (Y < 32, picareta de ferro) →
   `mo:dilithium_crystal` (`gemDilithium`).
2. `mo:isolinear_circuit` (ferro + redstone + vidro) → Inscriber `mo:inscriber`: Mk2 (+ouro),
   Mk3 (+diamante), Mk4 (+esmeralda). `mo:machine_casing`, `mo:machine_hull`, `mo:s_magnet`,
   `mo:battery`, `mo:hc_battery`.
3. Matrizes: `mo:integration_matrix`, `mo:me_conversion_matrix`, `mo:h_compensator`.
4. Matéria: `mo:decomposer`, `mo:matter_pipe`/`heavy_matter_pipe`, `mo:matter_container`,
   `mo:matter_dust` → `mo:matter_recycler` → `mo:matter_dust_refined`; `mo:portable_decomposer`.
5. Padrões e rede: `mo:network_pipe`, `mo:network_flash_drive`, `mo:network_switch`,
   `mo:network_router`, `mo:pattern_drive` (2 padrões), `mo:pattern_storage` (6 drives),
   `mo:matter_scanner` (blocos no mundo), `mo:matter_analyzer` (itens, +20% por análise),
   `mo:replicator` (até 5 placas de tritânio contra radiação), `mo:holo_sign`,
   `mo:pattern_monitor` (fila de replicação). Conexão de rede pela face de trás.
6. Upgrades `mo:upgrade:0-7` (casca, Speed, Power, Fail-Safe, Range, Power Storage, Hyper
   Speed, Matter Storage).
7. `mo:transport_flash_drive`, `mo:transporter`, `mo:security_protocol`, `mo:contract_market`.
8. Armas: `mo:weapon_handle`, `mo:weapon_receiver`, `mo:plasma_core`, `mo:phaser`,
   `mo:energy_pack`, `mo:forcefield_emitter`, `mo:weapon_station`, módulos
   `weapon_module_barrel:0-3`, cores `weapon_module_color:*` (só em baús), `mo:phaser_rifle`,
   `mo:plasma_shotgun`, `mo:ion_sniper` (Mk4), `mo:sniper_scope`, `mo:omni_tool`.
9. Androides: entidades `rogue_android`/`ranged_rogue_android` (dims 0, -1, 1; até 4/chunk);
   partes `mo:rouge_android_part:0-3`; Mad Scientist (vilas) faz a transformação com um
   conjunto; `mo:android_station` (consome torso, cabeça e braço), `mo:charging_station`
   (raio 8); pílulas `mo:android_pill:0-2` (sem receita).
10. Anomalia/fusão: `mo:gravitational_anomaly` (dims -1, 0, 2; Y 4–64; chance 0.005),
    `mo:spacetime_equalizer`, `mo:gravitational_stabilizer`, `mo:fusion_reactor_coil` (11),
    `mo:fusion_reactor_controller` (anomalia a ≤3 blocos), `mo:fusion_reactor_io`.
11. Galáxia: `mo:star_map` (2048–2304 estrelas, 1–4 planetas); construções/naves
    (`ship_factory`, `scout_ship`, `ship_colonizer`, `building_*`) **sem receita de bancada** —
    são geridas na interface do Star Map.

## Materiais e geração de mundo

* `ore_gen_blacklist = [-1, 2]`: tritânio e dilítio não geram no Nether nem na dim 2.
* Construções do mod ativas (`generate buildings=true`).
* Tritânio também sai em 2–3 lingotes por minério em trituradores/Fundição por Indução.

## Integrações no pack

* Thermal/EnderIO/IE/Mekanism processam tritânio e dilítio; Transposer/Fluid Canner enchem
  Matter Containers; Tinkers tem Molten Tritanium (`molten_tritanium`).

## Armadilhas e dicas

* Replicador emite radiação: instale 5 placas de tritânio.
* Speed Upgrade aumenta falhas do Decomposer; use Fail-Safe.
* Anomalias crescem e destroem blocos (`block destruction=true`); Estrela do Nether colapsa
  e explode.
* Transformação em androide pode matar (`transformation_death=true`); Blue Pill desfaz.
* Data Pad = manual interno; vários itens só estão em inglês.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `matteroverdrive:pecas_materia` | milestone | Matrizes + Heisenberg + Mk3 (Ato II). |
| `matteroverdrive:primeira_replicacao` | milestone | Linha de decomposição/replicação completa. |
| `matteroverdrive:utilidades_mo` | milestone | Transportador + upgrades. |
| `matteroverdrive:arsenal_fase` | milestone | Phaser, rifle e Omni Tool. |
| `matteroverdrive:corpo_metal` | milestone | Estação de Androides + Recarga. |
| `matteroverdrive:reator_fusao` | milestone | Reator de Fusão montado (Ato IV). |
| `matteroverdrive:mestre_materia` | milestone | **Marco final** — para o **Selo da Engrenagem**. |
