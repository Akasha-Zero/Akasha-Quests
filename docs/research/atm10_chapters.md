# ATM10 (All The Mods 10, MC 1.21.1 / NeoForge): FTB Quests chapter inventory

**Source:** <https://github.com/AllTheMods/ATM-10>, shallow sparse clone of `config/ftbquests`, commit `51627afa10897e5f239b403233b3d78283e0f237` (22 Oct 2026).
Local copy: `research/atm10/config/ftbquests/quests/`.

- Chapters: `quests/chapters/*.snbt` (67 files).
- Titles, subtitles and descriptions are **not** in the chapter files. They live in lang files `quests/lang/en_us/chapter.snbt`, `chapter_group.snbt`, `reward_table.snbt` and `lang/en_us/chapters/<file>.snbt`, keyed as `chapter.<ID>.title`, `quest.<ID>.title`, `quest.<ID>.quest_desc`, `task.<ID>.title` and so on. All titles below were resolved from those files.
- Group order comes from `quests/chapter_groups.snbt`. Chapter order inside a group comes from each chapter's `order_index`.
- Global settings (`quests/data.snbt`): `progression_mode: "flexible"`, `default_quest_shape: "circle"`, `default_autoclaim_rewards: "disabled"`, `default_consume_items: false`, `grid_scale: 0.5d`, and the book icon is `allthetweaks:atm_star`.

> **License warning:** `quests/README.md` and a hidden "AllRightsReserved" checkmark quest in every chapter both say the ATM quests are **All Rights Reserved**: *"you cannot redistribute any of the above folders or files in any publicly released packs without permission from Allthemods"*. Use this material as design reference only. Write your own text when you backport, and do not copy the quest descriptions verbatim.

## Totals

| Metric | Value |
|---|---|
| Chapters | 67 (4 ungrouped + 63 across 10 groups) |
| Quests (all objects in the files) | **4,853** |
| Quests excluding the hidden/invisible ones (one ARR notice per chapter plus a few hidden extras) | **4,778** |
| Median visible quests per chapter | 56 |
| Tasks | 5,968 (item 5,432 · kill 213 · checkmark 87 · structure 81 · biome 54 · advancement 53 · dimension 28 · observation 16 · location 2 · forge_energy 1 · xp 1) |
| Rewards | item 3,026 · xp (points) 1,963 · random (reward-table roll) 1,046 · xp_levels 910 · loot (loot crate) 262 · choice 95 · command 7 · toast 1 |
| Quests flagged `optional` | 531 |
| Reward tables | 63 files in `quests/reward_tables/` |

"#" below counts visible quests, with the total including hidden quests in parentheses where they differ.

---

## Ungrouped (shown above all groups)

| File | Title | # | Progression summary |
|---|---|---|---|
| `welcome.snbt` | Welcome | 5 (6) | Hub page with the ATM logo image and a Discord-link image. The center is a pentagon quest of size 3 titled "Welcome to All The Mods 10!", whose task is entering the Overworld (it auto-completes). Around it are 4 checkmark info quests: Quests ("quests are optional, mods are not gated"), Useful Commands (/sethome, /spawn, /rtp), Claiming Chunks, and Creating a Team. Each gives 10 XP. |
| `bounty_board.snbt` | Bounty Board | 49 (50) | Kill-task chapter. It has tiered "Overworld/Nether/End Bounty" quests (zombies, skeletons, creepers, spiders, witches, blazes, wither skeletons, endermen: x5 then up to x100) plus single boss kills (Ender Dragon, Wither, Warden, Elder Guardian, Ravager, Piglin Brute, modded bosses such as Lunar Monstrosity, Starlight Golem, Forgotten Guardian, Wilden Chimera, and a joke "Trader Villager and his annoying Llamas"). Rewards are XP plus random-table rolls. |
| `building_tips.snbt` | Building Tips | 90 (91) | A non-progression catalogue of building and decoration mods: Chipped, Rechiseled, Macaw's, Framed Blocks, Handcrafted, glass/log/stone/light variants, Building Gadgets, Construction Sticks. The root quest is size 5 and uses a smart-filter "any block" task. Rewards are mostly sample blocks, with 9 choice rewards. |
| `tips_and_tricks.snbt` | Tips and Tricks | 22 (23) | Loose QoL tips: Infinite Water sink, Shrinking Device, Tool Belt, Crafting on a Stick, Nature's/Explorer's Compass, Iron Jetpacks, Easy Villagers, Ranged Pump, Functional Storage, disenchanting methods. It ends at a "Tipped Out" checkmark. |

## Group 1: Main Questline (4 chapters)

| File | Title | # | Progression summary |
|---|---|---|---|
| `mainquestline_part_1.snbt` | **Chapter 1: The Beginning** (the "Getting Started" chapter) | 35 (36) | Vanilla progression with light modded side branches: logs → crafting table → wooden pick → Stone Age → Metal Age (iron + copper) → iron pick → diamonds → Nether (obsidian x10 plus the advancement) → Eyes of Ender → The End (dragon-egg advancement). It also covers the Wither and the Warden (gear-shaped boss quests of size 2, 250 XP each), netherite gear, archaeology, armor trims, and the sniffer. Side branches cover ore hammers, Iron Furnaces, Generators Galore, lava, wind and solar power, cables, and power storage. Rewards are small items, 10–250 XP, and "Reward: Common" table rolls. |
| `allthemodium.snbt` | **Chapter 2: Allthemodium** | 49 (54) | The post-vanilla metal ladder. Netherite → Ancient City (ATM ore, ATM smithing template from suspicious clay) → ATM ingot → Teleport Pad → dimension quests (Overworld→Mining Dim, Nether→The Other, End→The Beyond) → Vibranium (Nether/The Other, template from Bastion suspicious soul sand) → Unobtainium (End Highlands, template from Trial Spawners in The Other's Dungeon library). Tools, maces, armor, Arcanist/Mage armor and spellbooks branch off each tier. The Piglich boss (Ancient Pyramid in The Other) gives the hearts needed for the three alloys, each made in a different mod's machine: Ars Nouveau Enchanting Apparatus, Powah Energizing Orb, IF Dissolution Chamber. These lead to the Eternal Stella, then 4 alloy tools, then the **Allthemodium Alloy Paxel** (5,000 XP). Rewards are mostly ingots of the same tier plus 500–5,000 XP. |
| `chapter_2_the_star.snbt` | **Chapter 3: The ATM Star** | 127 (128) | The endgame recipe tree. About 30 "mod gateway" quests (rounded square, size 2, 100 XP) feed 12 octagon sub-components of size 2.5: Dragon Soul, Pulsating Black Hole, Improbable Probability Device, Nexium Emitter, Wither's Compass, Philosopher's Fuel, Dimensional Seed, Creative Essence, Oblivion Shard, Patrick Star, plus The Star's Casing and the Awakened Alloy Block. These converge with the Runic Star Altar multiblock on the **ATM Star** (pentagon, size 5). Each component rewards 1 Allthemodium, 1 Vibranium and 1 Unobtainium block plus 10 levels. The Star rewards compressed metal blocks plus 100 levels. See `atm10_atm_chapters_detail.md`. |
| `achapter_2r_6the_atm_star.snbt` | **Chapter 4: Creative** (subtitle "Plus Star Automation!") | 37 (39) | Post-Star content in two parts. (1) Star automation: Patrick Bee (fish it up in a Warm Ocean) → Starry Bee → Star Shards. Then the Automatic Hephaestus Forge, Runic Enchanter and Runic Crucible multiblocks, 5 prisms → 5 "ATM Star part" shards, the Infused Patrick Star and the Lens of the Killer, ending in the "Fully Automated ATM Star". (2) Ten creative items, each costing an ATM Star (Powah/Mek/AE2/ID creative energy, PNC creative compressor/iron block, Creative Jetpack, Locomotive, Blood Drop, Source Jar, Spellbook). Each gives 2 "Reward: Legendary" rolls plus 100 levels. |

## Group 2: Tools and Gear (7)

| File | Title | # | Progression summary |
|---|---|---|---|
| `apotheosis_gear.snbt` | Apotheosis Gear | 29 (30) | Starts with "get any Apotheosis gem". Covers invaders, bosses and world tiers (advancements), the Salvaging Table, reforging/augmenting tables, and the sigils (Socketing, Unnaming, Withdrawal, Rebirth, Malice, Supremacy). Rewards are items plus levels. |
| `artifacts.snbt` | Artifacts | 49 (50) | A flat catalogue. The root is "kill a Mimic" and every artifact (shoes, bracelets, rings and so on) is its own quest, each rewarding XP only. |
| `basic_armor.snbt` | Basic Armor | 127 (128) | The root is a Smithing Table of size 4. Each armor set (leather → netherite → ATM/Vib/Unob, Arcanist and Mage variants, and modded armors) is a 4-item quest. 108 of the quests are optional, so it works as a reference ladder with level rewards. |
| `basic_tools.snbt` | Basic Tools | 76 (77) | "Harvest Tier 1–6" spine (cobble, raw iron, diamond…) with optional pickaxe and sword ladders sorted by tier and damage. 70 optional; XP rewards. |
| `mahou_tsukai.snbt` | Mahou Tsukai | 35 (36) | Starts with taking damage and placing a Mahoujin (an advancement). Branches into exchanges, boundaries, projection spells and the Morgan/Emrys/Clarent swords. Rewards are "Mahou: Powders" table rolls. |
| `relics.snbt` | Relics | 25 (26) | The root is an XP task (level 1). Each relic gets one quest, grouped by the dimension where it is found. |
| `silent_gear.snbt` | Silent Gear (subtitle "And Productive Metalworks") | 68 (69) | Fire clay → Foundry multiblock → casts (one quest per tool cast) → Starlight Charger, plus repair kits. Item rewards. |

## Group 3: Storage (4)

| File | Title | # | Progression summary |
|---|---|---|---|
| `storage.snbt` | Basic Storage | 89 (90) | Covers chests, Sophisticated Storage and Backpacks (each upgrade is a quest), Functional Storage drawers, Dimensional Storage, trash cans, and the Netherite chest. 44 optional; uses 12 choice rewards ("1x1 Wooden Drawers"…). |
| `applied_energistics_2.snbt` | Applied Energistics 2 | 73 (74) | Meteorite hunting → certus/fluix → processors → cabling, terminals and storage cells (1k → 256M) → IO, autocrafting, P2P, Spatial IO. Rewards are "AE2: Basic/Advanced" random rolls. |
| `extended__advanced_ae.snbt` | Extended & Advanced AE | 55 (56) | Fluix → Assembler Matrix → Circuit Slicer → Infinity Cells → Quantum Computer. The capstone needs 200 quantum structures and 100 accelerators. |
| `refined_storage.snbt` | Refined Storage | 71 (72) | Controller → cables/grids → storage parts up to 1048m → autocrafting (Autocrafter, Mega, Ultra) → wireless. Nearly every reward is a random "RS:" table roll. |

## Group 4: Logistics (4)

| File | Title | # | Progression summary |
|---|---|---|---|
| `basic_logistics.snbt` | Basic Logistics (subtitle "And LaserIO") | 39 (40) | Hexagon layout with a "Wrenches" hub that branches to Pipez, Mekanism pipes, Integrated Dynamics and LaserIO. Each pipe family is one smart-filter quest covering all its tiers. Item rewards. |
| `integrated_dynamics.snbt` | Integrated Dynamics | 27 (28) | Menril logs → squeezer/drying basin → networks (checkmark explanations) → variable store and terminals. |
| `modular_router.snbt` | Modular Routers | 54 (55) | Router → use-case branches (mob farms, mining, farming) → every module, upgrade and augment as a leaf. |
| `pylons.snbt` | Pylons | 12 (13) | Polished blackstone → 5 pylons plus their filters. |

## Group 5: Resources (6)

| File | Title | # | Progression summary |
|---|---|---|---|
| `apotheosis_2.snbt` | Apothic Spawners (subtitle "And Other Spawners") | 35 (36) | Silk-touch spawner → each spawner modifier as a leaf, plus Capturing books and a spawn-egg checkmark. |
| `food_and_farming.snbt` | Food and Farming | 34 (35) | Crops → Farming/Cooking for Blockheads → animals, fishing, kitchen. |
| `hostile_neural_networks.snbt` | Hostile Neural Networks | 49 (50) | Redstone → Data Model → Sim Chamber → Loot Fabricator → one quest per prediction type. |
| `elmystical_agriculturerr.snbt` | Mystical Agriculture | 190 (191) | Seeds → Inferium → Infusion Altar (8 pedestals) → tiered essences and seeds (one quest per seed, about 150 of them) → Awakened Supremium. Rewards are 189 XP awards plus 171 "MA: Tier N Seed Bag" rolls. |
| `productive_bees.snbt` | Productive Bees | 251 (252) | Honeycomb → nests → advanced hives → one quest per bee species. Rewards are 133 loot crates ("Productive Bees: Genes"). This is the largest chapter by quest count. |
| `productive_trees.snbt` | Productive Trees | 166 (167) | Vanilla saplings → pollen → one quest per sapling, following the breeding tree. 243 item rewards. |

## Group 6: Tech (12)

| File | Title | # | Progression summary |
|---|---|---|---|
| `create.snbt` | Create | 89 (90) | Uses the `gear` default shape. Andesite → andesite alloy → rotational power → machines, item/fluid logistics, contraptions, trains, and the 1.21 additions (packages, stock tickers). 174 item rewards and no XP. |
| `create_addons.snbt` | Create Addons | 50 (51) | Separate sub-trees for Crafts & Additions (rolling mill → alternator/motor), Dragons Plus, Enchantment Industry, Ars Creo, Aquatic Ambitions, train decoration. |
| `draconic_evolution.snbt` | Draconic Evolution | 139 (140) | Draconium → Fusion Crafting → tiers (Basic → Wyvern → Draconic → Chaotic) → energy core tiers 1–8 → Chaos Guardian (End at 10k coordinates) → reactor → every tool and module per tier. Rewards are "DE: Wyvern/Draconic/Chaotic" table rolls. |
| `immersive_engineering.snbt` | Immersive Engineering | 126 (127) | Engineer's hammer → workbench and blueprints → multiblocks (coke oven, blast furnaces, metal press, crusher, excavator, arc furnace) → power (wires, transformers) → weapons (revolver, railgun). Uses 42 choice rewards from the "Immersive: …" tables. |
| `industrial_foregoing.snbt` | Industrial Foregoing | 77 (78) | Pity Machine Frame → latex/plastic → machines by category (animals, plants, fluids) → frames up to Supreme (Ether Gas) → Laser Drill → Infinity tools. |
| `justdirethings.snbt` | Just Dire Things | 153 (154) | Checkmark category hubs (Simple Machines, Advanced Machines, Tools, Utilities) → Goo tiers 1–4 → Ferricore/Blazegold/Eclipse Alloy/Time Crystal → each machine tier. |
| `mekanism.snbt` | Mekanism | 89 (90) | Iron → osmium/steel → Metallurgic Infuser → machines → 5x ore processing chain → tier installers and upgrades → generators, jetpack, teleporters. |
| `mekanism_reactors.snbt` | Mekanism: Reactors | 92 (94) | Sulfur → fissile fuel → Fission Reactor → turbine/boiler → SPS → antimatter → induction matrix, QIO, MekaSuit, and every module. |
| `oritech.snbt` | Oritech | 56 (57) | Nickel → machine cores → ore processing → pipes/energy → the "nuke" (size 3). |
| `xycraft.snbt` | XyCraft | 26 (27) | Xychorium gems / Kivi → machines (extractor, fabricator, ports). |
| `pneumaticcraft.snbt` | PneumaticCraft | 125 (127) | Compressed iron → air compressor → pressure chamber → plastic/PCB → drones, armor, upgrades. "PNC:" table rolls. |
| `railcraft.snbt` | RailCraft | 50 (52) | Rails → tracks/carts → locomotives (steam/electric) → tunnel bore. Choice rewards ("RailCraft: Rails"). |

## Group 7: Modern Industrialization (4)

| File | Title | # | Progression summary |
|---|---|---|---|
| `mi_steam.snbt` | Steam Age: New Beginnings | 30 (31) | Copper → bronze → bronze/steel steam machines → analog circuit. The first description states its purpose is to guide you to "parts required for the AllTheMods Star and later on Infinity Armor". |
| `mi_electric.snbt` | Electric Age: No more rookie | 50 (51) | Battery alloy → LV/MV machines, cables, transformers, generators → digital circuit. |
| `mi_digital.snbt` | Digital Age | 66 (67) | Digital circuits → processing unit → large motors/pumps → nuclear (fuel rods) → nuke. |
| `mi_endgame.snbt` | End Game | 25 (26) | Cryofluid, He-3 → qubit → quantum circuit → Quantum armor and sword. This is the only group with cross-chapter dependencies (9 to Digital, 3 to Electric). |

## Group 8: Power (4)

| File | Title | # | Progression summary |
|---|---|---|---|
| `basic_power.snbt` | Basic Power | 34 (35) | Redstone → early generation (coal/lava) → transfer → storage, with each mod's options as branches. |
| `extreme_reactors.snbt` | Extreme Reactors | 32 (33) | Uranium → first passive reactor → active cooling → turbine → reprocessor → Insanite block. Includes a single `forge_energy` task. |
| `generators.snbt` | Generators N Furnaces | 46 (47) | Furnace → Iron Furnaces tiers and augments, plus Generators Galore generator types and their 8x/64x upgrades. |
| `powah.snbt` | Powah | 108 (109) | Uraninite → Energizing Orb → seven tiers (Starter → Nitro), each with cells, cables, reactors and generators. "Powah: <tier>" rolls. |

## Group 9: Magic (9)

| File | Title | # | Progression summary |
|---|---|---|---|
| `apothic_enchanting.snbt` | Apothic Enchanting | 58 (59) | Enchanting table → shelf tiers (hell, sea, end, sculk) → infusing → best setup → library. Uses a toast reward and has 4 dependencies into Apotheosis Gear. |
| `ars_nouveau.snbt` | Ars Nouveau | 129 (130) | Book → Scribes Table → source → glyphs (one quest each) → apparatus, rituals, familiars. |
| `evilcraft.snbt` | EvilCraft | 44 (45) | Dark gems → blood infuser → promise tiers → tools, Garmonbozia. |
| `forbidden__arcanus.snbt` | Forbidden & Arcanus | 62 (63) | The first task is a `location` task, "Go to Y level 1". Covers Hephaestus Forge tiers 1–3 (as `observation` tasks), prisms, Eternal Stella, Quantum Injector. |
| `iron_spells_and_spellbooks.snbt` | Iron's Spells 'n Spellbooks | 102 (103) | Arcane essence → inscription table/forge → schools and upgrade orbs → spellbooks → structures and bosses ("Adventure Time!" branch with structure and kill tasks). |
| `natures_aura.snbt` | Nature's Aura | 24 (25) | Brilliant fiber → altar → rituals → aura tools. |
| `neo_vitae.snbt` | Neo Vitae | 48 (49) | Blood-magic clone: altar tiers I–V and orbs. The only chapter that uses `command` rewards (`/neovitae anima-network @p add N`). |
| `occultism.snbt` | Occultism | 63 (64) | Datura → chalks → rituals by spirit tier (Foliot → Marid) → storage → familiars → "What's Next?" checkmark. |
| `theurgy.snbt` | Theurgy | 44 (45) | Sal ammoniac → mercury → sulfurs/salts → step-by-step pedestal quests ("Step 1…4"). |

## Group 10: Exploration (9)

| File | Title | # | Progression summary |
|---|---|---|---|
| `aether.snbt` | The Aether | 72 (73) | Glowstone portal → mob kills (one quest per mob) → dungeons (Bronze/Silver/Gold) → Slider, Valkyrie Queen, Sun Spirit → loot, tools and armor tiers. |
| `bumblezone.snbt` | The Bumblezone | 113 (114) | Beehive → dimension → 32 structure-discovery quests → Queen's advancements. "Bumblezone: Rewards" rolls plus levels. |
| `auroral.snbt` | Auroral | 20 (21) | Aurora event → infusion → Shimmersteel/Shimmerweave. |
| `cataclysm.snbt` | Cataclysm | 109 (110) | Organized by dimension ("Overworld: Land of 5 bosses", "Nether: 2", "End: 1"): structure → boss kill → boss loot. Includes the Eyes for locating structures. |
| `deeper_and_darker.snbt` | Deeper and Darker | 44 (45) | Ancient City → Heart of the Deep → Otherside biomes → mobs → warden gear and the Soul Elytra. |
| `eternal_starlight.snbt` | Eternal Starlight | 192 (193) | Portal ruins and the Gatekeeper duel → the dimension → biomes → bosses → every material set (sword, pick, axe and so on as separate quests). The largest Exploration chapter. |
| `ice__fire.snbt` | Ice and Fire | 86 (87) | Most content has moved to The Other (an Allthemodium dimension). Covers dragons and their stages, taming, and structures with boss kills (Gorgon, Hydra, Cyclops, Siren). |
| `twilight_forest.snbt` | The Twilight Forest | 56 (57) | Portal → strict boss order (Naga → Lich → Minoshroom → Hydra → Knight Phantoms → Ur-Ghast → Alpha Yeti → Snow Queen) with gear per boss → "The Final Boss" → "The Real Final Boss", which needs the magic saplings (size 3). |
| `undergarden.snbt` | The Undergarden | 81 (82) | Catalyst portal → 18 biome visits → mobs → Forgotten Guardian (boss). |

---

### Notes on naming

- There is **no separate "All The Tweaks" chapter**, and no "compressed" chapter. The All The Tweaks items (`allthetweaks:atm_star`, `patrick_star`, `dragon_soul`, `pulsating_black_hole`, `mini_nether/end/exit`, `dimensional_seed`, `nexium_emitter`, `withers_compass`, `philosophers_fuel`, `oblivion_shard`, `improbable_probability_device`, `atm_star_shard`, `atm_star_block`) and the All The Compressed items (`allthecompressed:*_4x/5x/6x`, `nether_star_block_3x`) all appear inside Chapter 3 (The ATM Star) and Chapter 4 (Creative).
- File names are misleading. `chapter_2_the_star.snbt` is titled **Chapter 3**, and `achapter_2r_6the_atm_star.snbt` is titled **Chapter 4: Creative**. Always resolve titles through the lang files.
- `mekanism.snbt` and `mekanism_reactors.snbt` are a deliberate two-page split, as the description explains: "It has so much we even had to make 2 Quest Pages for it!".
