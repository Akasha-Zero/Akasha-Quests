# ATM10 quest design patterns: analysis for a BetterQuesting 1.7.10 backport

Source: AllTheMods/ATM-10 `config/ftbquests/quests` (commit `51627afa`, Oct 2026). All numbers below were computed by parsing every chapter's SNBT and the `lang/en_us` files (parser: `scripts/snbt.py` and `scripts/dump.py` in the scratchpad). The quotes are short excerpts kept for analysis. The ATM quests are **All Rights Reserved** (see `quests/README.md`), so reuse the structure and write new text.

---

## 1. Book architecture

### 1.1 Groups and ordering
Order comes from `chapter_groups.snbt`, then each chapter's `order_index`:

| # | Group | Chapters | Role |
|---|---|---|---|
| – | *(ungrouped, shown first)* | Welcome, Bounty Board, Building Tips, Tips and Tricks | Orientation and evergreen side content |
| 1 | **Main Questline** | Ch1 The Beginning → Ch2 Allthemodium → Ch3 The ATM Star → Ch4 Creative | The only real "progression spine" |
| 2 | Tools and Gear | Apotheosis Gear, Artifacts, Basic Armor, Basic Tools, Mahou Tsukai, Relics, Silent Gear | Reference ladders and catalogues |
| 3 | Storage | Basic Storage, AE2, Extended & Advanced AE, Refined Storage | Mod guides |
| 4 | Logistics | Basic Logistics, Integrated Dynamics, Modular Routers, Pylons | Mod guides |
| 5 | Resources | Apothic Spawners, Food & Farming, HNN, Mystical Agriculture, Productive Bees, Productive Trees | Resource generation (the biggest chapters) |
| 6 | Tech | Create, Create Addons, Draconic, IE, IF, JDT, Mekanism, Mek: Reactors, Oritech, XyCraft, PneumaticCraft, RailCraft | Mod guides |
| 7 | Modern Industrialization | Steam → Electric → Digital → End Game | One mod split into age-chapters, chained with cross-chapter dependencies |
| 8 | Power | Basic Power, Extreme Reactors, Generators N Furnaces, Powah | Mod guides |
| 9 | Magic | Apothic Enchanting, Ars Nouveau, EvilCraft, Forbidden & Arcanus, Iron's Spells, Nature's Aura, Neo Vitae, Occultism, Theurgy | Mod guides |
| 10 | Exploration | Aether, Bumblezone, Auroral, Cataclysm, Deeper & Darker, Eternal Starlight, Ice & Fire, Twilight Forest, Undergarden | Dimension and boss guides |

**Key philosophy**, from the Welcome "Quests" checkmark: *"In this modpack, quests are optional. Mods are not gated behind completing any quests! A lot of the individual questlines outside of the Main questline are meant to serve as mod guides."*

`data.snbt` sets `progression_mode: "flexible"`, so a player can complete a quest before its dependencies are done; the tasks simply detect items. In ATM, dependencies are guidance, not locks.

### 1.2 Main questline vs. mod chapters
- The main chapters are numbered in the title with color codes: `&aChapter 1&r: &bThe Beginning`, `&aChapter 2&r: &eAllthemodium`, `&aChapter 3&r: &6The ATM Star`, `&aChapter 4&r: &dCreative`.
- Ch1 → Ch2 has no hard link (`quest_links` is empty everywhere and there are no cross-chapter dependencies). Ch2's first quest simply asks for the item that ends Ch1 (a Netherite Ingot). Ch3's first quest asks for an Allthemodium ingot. **Handoff happens by repeating the previous chapter's capstone item as the next chapter's root task.**
- The ATM Star chapter connects to the mod chapters through **"gateway" quests**. Each is an entry node with no dependencies, a rounded square of size 2.0, a simple early item from the mod, and a 100 XP reward. Examples: "Powah" = dielectric paste, "Mekanism" = steel ingot, "Applied Energistics" = certus quartz. The description points to the mod's own chapter: *"There is a Quest page now that you can follow for it!"*. A few descriptions embed a clickable JSON text component that jumps to the mod chapter: `{ "text": "POWAH QUESTLINE", ..., "clickEvent": { "action": "change_page", "value": "2A6EBEEBAB882679" } }`, where the value is the Powah chapter ID.
- Cross-chapter dependencies exist only inside the MI age split (Electric → Steam, Digital → Electric, Endgame → Digital/Electric) and Apothic Enchanting → Apotheosis Gear. Everything else is self-contained per chapter.

### 1.3 Sizes
- Median chapter: 56 visible quests. Range: 5 (Welcome) to 251 (Productive Bees).
- Main questline: 35 / 49 / 127 / 37.
- Typical mod guides: about 25–60 for small and medium mods (Pylons 12, XyCraft 26, Powah 108), and 90–150 for big tech mods (Mekanism 89 + Reactors 92, Draconic 139, IE 126, PNC 125). "Catalogue" chapters reach 150–250 when every bee, seed or sapling gets its own quest.

---

## 2. Quest-level conventions

### 2.1 Task types (visible quests)
item 5,432 · kill 213 · checkmark 87 · structure 81 · biome 54 · advancement 53 · dimension 28 · observation 16 · location 2 · forge_energy 1 · xp 1.

- **89% of quests have exactly one task.** About 5% have 2, and the rest 3–9 (used for multiblocks: "Runic Star Altar" lists 20 block stacks).
- **Smart-filter item tasks** (`ftbfiltersystem:smart_filter`) accept "any of" or "any tag", for example any log, any diamond gear piece, or any of three alternative machines ("Power Cables" = Mek universal cable OR Pipez energy pipe OR Powah cable). This is how ATM supports multiple mod paths without branching. → In BQ 1.7.10, use OreDictionary item tasks, or an `retrieval` task with `partialMatch`, or "any of" via `ignoreNBT` plus a quest with `andLogic=false` (task logic OR).
- **Boss progress is tracked by trophy or drop items, not kill tasks.** Twilight Forest uses `naga_trophy`, `lich_trophy`… `snow_queen_trophy`, and Piglich uses `piglich_heart`. Kill tasks appear for vanilla bosses (Wither, Warden) and in the Bounty Board.
- **Dimension tasks are milestones**: entering the Overworld completes the Welcome quest automatically, and the Ch2 dimension quests (Mining, The Other, The Beyond) work the same way.
- **Checkmark tasks** carry pure information: commands, chunk claiming, "Quarried" (a warning that quarries void ATM ore), "Multiblock Levels", "PHOBIA ALERT" in Bumblezone.
- **Observation tasks** (look at a block) are used to confirm multiblocks are built, for example Hephaestus Forge tier 1/2/3.

### 2.2 Shapes and sizes (semantic use)
The chapter `default_quest_shape` varies (circle, hexagon, rsquare, diamond, square, gear, none). Explicit per-quest shapes are used as semantic markers:

| Shape/size | Meaning (observed) |
|---|---|
| **pentagon 3.0–5.0** | The single "goal" of a chapter: Welcome root (3.0), **ATM Star (5.0)**, Fully Automated ATM Star (2.0), Star Shards |
| **octagon 2.5** | The 12 major Star sub-components (Dragon Soul, Black Hole, and so on) |
| **octagon 1.2–1.5** | Key ingots: ATM/Vibranium/Unobtainium ingots (1.4), alloy ingots (1.2), Ch1 "The Metal Age" (1.5) |
| **gear 2.0** | Boss and finale moments: Ch1 The End, Kill The Wither, Kill The Warden, Ch2 root "Allthemodium" |
| **hexagon 1.3** | "Use another mod's machine" prerequisites in Ch2 (Enchanting Apparatus, Energizing Orb, Dissolution Chamber, Hephaestus Forge) and multiblocks in Ch4 |
| **rsquare 2.0** | Mod gateways in Ch3. Also smithing-template finds (1.3) |
| **square** | Dimension visits and structures (Ch2) |
| **heart** | Living things: Piglich Boss, The Sniffer |
| **diamond** | Optional or gear catalogue leaves (Twilight armor/tools, Ch4 prisms) |
| size 1.25 | Standard Star ingredient (the about 80 inputs feeding the octagons) |

Size distribution: 72% use the default size. Common explicit sizes are 1.5 (321 quests), 1.25 (228), 1.2 (177), 2.0 (131) and 0.75 (103, small leaves).

### 2.3 Dependencies
- Dependency counts (visible quests): 0 deps = 499, **1 dep = 3,564 (75%)**, 2 = 595, 3 = 76, and 4+ = 38. The trees are mostly simple; wide AND-joins are reserved for climaxes:
  - Each Star component has **6–9 dependencies** (Dragon Soul 9, Black Hole 9, Wither's Compass 9, Dimensional Seed 9, Philosopher's Fuel 9).
  - The ATM Star itself has **13 dependencies**, and Fully Automated ATM Star has 10.
  - The ATM alloy tools have 4 dependencies, and the Alloy Paxel needs all 4 tools.
- **Either-or choices** use `dependency_requirement: "one_completed"` (60 quests). Example: *"Either or Blastproof Casings and 256M Portable Item Cells"*. Two optional quests, one per mod path (MI vs. AE2 MEGA), feed a smart-filter join quest that accepts either item. The same pattern appears in "Mechanical Arm or Large Advanced Motor" (Create vs. MI) and "Blaze Burner or Uranium Quad Fuel Rods". **This is how ATM made Modern Industrialization optional for the Star.** → BQ: a join quest with `questLogic: OR` over the two prerequisites.
- `hide_until_deps_visible` (198 quests) and `hide_until_deps_complete` (55) keep big chapters tidy, for example Twilight Forest gear is hidden until the portal quest. `hide_dependency_lines` (476) is used on dense hubs.

### 2.4 Optional quests
531 quests are optional. They are concentrated in reference chapters (Basic Armor 108, Basic Tools 70, Storage 44, HNN 34, IE 33, Silent Gear 27, Aether 26). In the main line, optional quests mark alternative paths (Create/MI branches in Ch3, the 256M Portable Cell) and QoL extras (Crafting on a Stick, Wind/Solar in Ch1). Every chapter also has an **invisible optional checkmark** holding the ARR notice, plus a few "parked" invisible quests (ATM Bow, Vibranium Shield, Unobtainium Crossbow, Alloy Trident) kept for later.

### 2.5 Info and checkmark quests
- Welcome: 4 checkmarks (team, commands, chunks, quest philosophy), each with `min_width: 250` for a wide description window.
- Mod chapters use checkmarks as **category headers**. JDT has "Simple Machines", "Advanced Machines", "Tools" and "Utilities" as root checkmarks. Occultism ends with a "What's Next?" checkmark (size 2.5).
- A "disguised info" variant is also used: a quest whose task is a trivial item (stick, chest, redstone) acting as the chapter header ("Basic Tools!" = stick, "Basic Power" = redstone).

---

## 3. Rewards

### 3.1 Overall mix (visible quests)
item 3,026 · xp points 1,963 · `random` (one roll from a reward table) 1,046 · xp_levels 910 · `loot` (loot-crate style roll) 262 · `choice` (pick one from a table) 95 · command 7 · toast 1. 182 quests give no reward at all (mostly parked or invisible ones, and a few Cataclysm and Ch2 nodes). Most quests give 1–2 rewards: 2,373 give one and 1,845 give two.

`default_autoclaim_rewards: "disabled"`, so the player clicks to claim.

### 3.2 XP scale
The most common values are `xp 100` (880 quests; the standard mod-chapter award), `xp 10` (462; trivial/info), `xp_levels 1` (446; standard Star-ingredient award), `xp 50` (316), `xp_levels 5` (137), `xp 250` (37; vanilla bosses), `xp 500` (29; ATM/Vib/Unob ingots), `xp_levels 10` (Star components and Ch4 milestones), 1,000 XP (alloy tools), 5,000 XP (Alloy Paxel), `xp_levels 25` (Ch4 star parts), and `xp_levels 100` (ATM Star and each creative item).

### 3.3 Reward tables (63)
- **Generic rarity ladder:** "Reward: Common" (57 entries: spawn eggs, drawer upgrades, waystones…), "Uncommon" (42: Modular Router modules, Pipez upgrades), "Rare" (15: dragon egg/head, diamond furnaces, enderium), "Epic" (8: spirited thermo generator, elite energy cube, ultimate tier installer), and "Reward: Legendary" (`mythic.snbt`, 10: antimatter pellet, ultimate induction cells, Nitro reactor, ATM/Vibranium blocks). Ch1 uses Common/Uncommon, and Ch4 creative items give 2x Legendary.
- **Per-mod themed tables:** "AE2: Basic/Advanced Rewards", "RS: Base Materials/Storage Parts", "Powah: Starter/Basic/Hardened/Blazing/Niotic/Spirited/Nitro" (one per tier), "DE: Basic/Wyvern/Draconic/Chaotic" (one per tier), "MA: Tier 1–5 Seed Bag", "PNC: Basic/Plastic/PCB/Advanced Tier", "IF: Basic/Advanced", "Immersive: Resources/Alloys and Parts/Building Block", "RailCraft: Rails/Random", "Productive Bees: Genes", "Twilight Forest: Rewards", "Bumblezone: Rewards", "Mahou: Powders", "Theurgy: Salt", plus unnamed MI tables per age. **Pattern: a chapter's random-reward table matches the chapter's current tier, so rewards help with the next step.**
- `choice` tables are used where any one of several variants is equally useful (IE building blocks, RailCraft rails, drawers).
- One table (`amekanismr_dadvanced_loot_boxr.snbt`) defines a physical `loot_crate` ("mekanism_advanced_loot_box"). Global `drop_loot_crates: false`, so crates are not mob drops.
- `command` rewards are used only by Neo Vitae (`/neovitae anima-network @p add N`), to give mod-internal currency.

### 3.4 "Same-tier refund" item rewards
Ch2 consistently rewards a copy of what the quest just consumed or showed: Allthemodium Ingot → +1 ATM ingot, Vibranium Tools → +1 Vibranium ingot, each smithing template → +1 template (the description notes you can duplicate templates), each alloy → 3+3 of its parent ingots. Ch3 ingredients refund a lower compression tier (`diamond_block_4x` → `diamond_block_3x`; `netherrack_6x` → `netherrack_5x`) or a thematically related item (Mini Nether → a Nether Star; Mini Exit → a Dragon Egg). **Rewards nudge the player toward the next craft without trivialising it.**

---

## 4. Descriptions: tone and format

- 3,512 of 4,778 visible quests have a description. Median length is 247 characters, mean 312, and the longest is 3,611 (the Runic Star Altar build guide).
- **Voice:** first-person narrator ("I"), casual, jokey, often self-deprecating, addressing the player as "you" and "we". Typical structure: (1) what the item or mod is, (2) exactly where to find or how to make it (biome, structure, Y-level, machine), (3) a tip or warning, (4) a joke. Short excerpts:
  - *"I'm going to assume you've been crafting, right? It is MineCRAFT after all."* (Ch1)
  - *"Tip: It explodes. A lot. Don't do this near your base."* (Kill The Wither)
  - *"To teleport you need to shift right click with your main and offhand empty! That is important, read it! BOTH hands need to be EMPTY!"* (Teleport Pad)
  - *"For 17 X3 Netherstar Blocks you'll need 111,537 Netherstars. (Please I am begging you use HNN just don't spawn and kill 111,537, please!)"* (Star's Casing)
  - ATM Star description: *"It does. You have created the ATM Star, that means you have beat the modpack! So what's next?"*, which answers the subtitle "Does this mean you win?".
- **Subtitles** (1,566 quests) are one-line taglines: "Essence of the most powerful beings." (Dragon Soul), "Power over Death itself." (Wither's Compass), "Too bad SpongeBob isn't a Star." (Patrick Star). For compressed blocks the subtitle states the quantity: "6,561 Diamond Blocks", "531,441 Netherrack".
- **Rich formatting:** `&`-color codes, `{image:atm:textures/questpics/... width:200 height:100 align:center}` (478 quests embed screenshots: structures, armor sets, multiblock layers), `{@pagebreak}` for multi-page guides (68 quests, such as layer-by-layer multiblock builds), and JSON `clickEvent` page links (5).
- **Untitled quests:** 1,710 visible quests have no custom title and show the task item's name. These are typical for catalogue leaves.

---

## 5. Layout patterns (x/y coordinates)

- **Spine plus branches** (Ch1): the main line runs left to right (logs → crafting → pick → stone → metal → iron pick → diamonds → Nether → End). Side branches hang underneath (furnaces, power, archaeology) and boss kills sit to the right of their unlock.
- **Tier columns** (Ch2): one column per metal tier. Each column runs dimension → template + ingot (octagon) → tools/mace/armor/arcanist/mage/spellbook as a fan of leaves. The alloy section is a separate cluster on the right: 3 machine hexagons → 3 alloys → 4 tools → Paxel.
- **Hub and radial convergence** (Ch3): the ATM Star (size 5) sits at (0,0). The 12 octagon components ring it at radius 3–4. Ingredients (size 1.25) sit beyond them, and the mod gateways (size 2) are on the outer ring. **Read outward-in: mod → ingredient → component → Star.**
- **Grids** for catalogues (bees, seeds, saplings, glyphs, modules).

---

## 6. The ATM Star (Ch3): full recipe and quest tree

**ATM Star** (`allthetweaks:atm_star`, pentagon, size 5) has 13 dependencies:

| # | Component (quest) | Dependencies (ingredient quests) | Mods touched |
|---|---|---|---|
| 1 | **Runic Star Altar** (the crafting multiblock, 15×8×15, 20-entry block task) + "Upgrades" leaf (Runic Attuned crystal or MI turbo upgrade) | none (root) | Forbidden & Arcanus blocks, Modern Industrialization controller |
| 2 | **Pulsating Black Hole** | Micromissiles (PNC) · ME Quantum Ring (AE2) · Pocket Storage PSU-4 · Paradox Machine (JDT) · Stable Wormhole (Occultism) · Crystal Staff (Roots Classic) · Explosive Mycelial Generator (IF) · Lightning Bomb (EvilCraft) · Manhattan Module/nuke (Oritech) | 9 mods |
| 3 | **Nexium Emitter** | Nitro Player Transmitter (Powah) · Universal Wireless Terminal (AE2WTLib) · Quantum Computer Multi-Threader (Advanced AE) · Gravitational Modulating Unit (Mekanism) · Tesla Coil (IE) · *Mechanical Arm (Create) OR Large Advanced Motor (MI)* · Infinity Booster Card (AE Infinity Booster) | 7 |
| 4 | **Patrick Star** | 7 concrete quests: 13 magenta concrete, 5 pink powder, 8 green powder, 8 green concrete, 11 pink concrete, 8 magenta powder, 2 lime concrete (a vanilla joke recipe) | vanilla |
| 5 | **Dimensional Seed** | Obsidian 5x · End Stone 5x · Netherrack 6x · Diamond Block 4x · Emerald Block 4x (All The Compressed) · Mini Nether · Mini End · Mini Exit (All The Tweaks via Apothic Enchanting shelves and Infused Dragon's Breath) · Teleposer (Neo Vitae) | Compressed, ATT, Apotheosis, Neo Vitae |
| 6 | **Wither's Compass** | Heart of the Deep (Deeper & Darker, from the Warden) · Wither Builder (IF) · Withered Comb Block (Productive Bees) · Netherstar Generator 64x (Generators Galore) · Wither Skull Scroll lvl 10 legendary (Iron's Spells) · Nether Star Crux (MA Additions) · Tipped Arrow of Wither (vanilla brewing) · Witherproof Bricks (MA) · Glyph of Wither (Ars) | 8 mods |
| 7 | **Improbable Probability Device** | 2 Antimatter Pellets (Mekanism) · Aerial Interface (PNC) · Lightning Upgrade Orb (Iron's) · Singularity (AE2) · 2 Rainbow Furnaces (Iron Furnaces) · *2 Blastproof Casings (MI) OR 2 256M Portable Item Cells (MEGA Cells)* | 6 |
| 8 | **Philosopher's Fuel** | Dark Tank of Eclipse Ember Fuel (EvilCraft + JDT goo) · Magmatic Generator 64x (Gen Galore) · Awakened Supremium Ingot Block (MA) · Smelter Prism (F&A) · Insanium Coal Block (MA Additions) · Insanite Blocks (Extreme Reactors) · Ender Generator (Gen Galore) · Rainbow Coal (Iron Furnaces) · *Blaze Burner (Create) OR Uranium Quad Fuel Rods (MI)* | 7 |
| 9 | **Dragon Soul** | Socotra Dragon Sapling (Productive Trees, breeding chain) · Soul Lava Bee spawn egg (Productive Bees + ATM soul lava) · Empty Soul Gem (Occultism) · Dragon Familiar in a Creature Catcher (Occultism + JDT) · Piglich Heart Block (9 hearts, Allthemodium boss) · Ender Dragon Data Model, self-aware (HNN) · Infused Dragon's Breath (Apothic Enchanting) · Chain of Souls (Eternal Starlight) · Abyssal Sacrifice (Cataclysm) | 9 |
| 10 | **Creative Essence** | Master Infusion Crystal · 4 Insanium Gemstone Blocks · 4 Insanium Blocks (MA / MA Additions); the description states 36,864 Inferium Essence for the blocks alone | MA |
| 11 | **Oblivion Shard** | Mace of Destruction (EvilCraft) · Eternal Stella (F&A) · 2 Piercing Vengeance Focuses (EvilCraft) · Snow Queen Trophy (Twilight Forest) · 2 Galgadorian Drills (Steve's Carts) · Void Forge (Cataclysm: Harbinger + Netherite Monstrosity + Ender Golem) · Meat Shredder (Cataclysm Harbinger) | 5 |
| 12 | **The Star's Casing** | none (root). Task: 28 Unobtainium-Allthemodium Alloy Blocks (Ars Enchanting Apparatus) + 15 Nether Star Block 3x (All The Compressed) | ATM, Ars, Compressed |
| 13 | **Awakened Unobtainium-Vibranium Alloy Block** | none (root). Task: 2 Unob-Vib alloy blocks, awakened in the Star Altar (4 Unbreaking I books, 4 Awakened Supremium gemstones and essence, 85M FE) | ATM, MA, Apotheosis |

Each component rewards 1 Allthemodium + 1 Vibranium + 1 Unobtainium block and 10 levels. The Star rewards 1x-compressed ATM/Vib/Unob blocks, the three alloy blocks, and 100 levels.

**Gateway quests in Ch3** (no dependencies, rsquare 2.0, 100 XP): Allthemodium, Twilight Forest, Apothic Mods, Steve's Carts, Applied Energistics, Roots Classic, Ars Nouveau, Powah, Productive Bees, Productive Trees, Cataclysm, Create *(optional)*, Deeper and Darker, Occultism, Hostile Neural Networks, Eternal Starlight, PneumaticCraft, Pocket Storage, Just Dire Things, Industrial Foregoing, EvilCraft, Oritech, Modern Industrialization *(optional)*, Mekanism, Iron's Spells, Iron Furnaces, Generator Galore, Mystical Agriculture, Forbidden & Arcanus, Extreme Reactors, Immersive Engineering. That is about 31 mods.

**Design takeaway:** the Star is a **breadth test**. Each component is themed (Wither, Dragon, Fuel, Dimension, Probability, Remote access, Destruction, Creation) and forces the player through 5–9 different mods, mixing tech, magic, exploration bosses, and grind (compressed 4x–6x blocks). Alternative paths exist so no single big mod (MI or Create) is mandatory. The descriptions double as recipe walkthroughs.

### Ch4 Creative (post-Star loop)
Patrick Bee (fishing in a Warm Ocean, a `biome` task) → Starry Bee (right-click with an ATM Star Block) → Star Shards (OR: crush a Star with the Mekanism crusher; described as "But why?", an invisible joke node). Then three new MI-based multiblocks (Automatic Hephaestus Forge, Runic Enchanter, Runic Crucible), 5 F&A prisms → 5 "ATM Star part" shards (subtitled 1/5…5/5), Infused Patrick Star, Lens of the Killer (Actually Additions), Corrupti Dust and Mundabitur Dust → **Fully Automated ATM Star** → 10 creative items. **Takeaway:** the endgame continues past the "win" by turning the win item into a farmable resource and spending it.

---

## 7. Allthemodium chapter structure (Ch2)

```
Netherite Ingot ("Allthemodium", gear 2.0) ─┬─ Ancient City (structure) ─┬─ ATM Smithing Template (brush suspicious clay)
                                            │                           └─ Allthemodium Ingot (octagon 1.4, Netherite+ pick, ore in Deep Dark)
                                            │                                   └─ Teleport Pad (4 nuggets + pearl; "BOTH hands EMPTY")
                                            │                                         ├─ Overworld → Mining Dimension → "Quarried" checkmark (quarries void ATM ore)
                                            │                                         ├─ The Nether → Vibranium Template (Bastion suspicious soul sand)
                                            │                                         │            → Vibranium Ingot (ATM pick; Nether rare / The Other common)
                                            │                                         │            → The Other (dimension) → Unobtainium Template (Trial Spawners, Dungeon library)
                                            │                                         │                                    → Ancient Pyramid → Piglich Boss (heart)
                                            │                                         └─ The End → Unobtainium Ingot (Vib pick; End Highlands)
                                            │                                                    → The Beyond (void dim; reward Angel Block)
                                            │   per tier (ingot + template): Tools · Mace · Armor · Arcanist Armor · Mage Armor · Spell Book (13/14/15 slots)
                                            ├─ Ars Enchanting Apparatus ─┐
                                            ├─ Powah Energizing Orb ──────┼─(+Piglich Heart)→ Unob-ATM / Vib-ATM / Unob-Vib Alloy Ingots
                                            ├─ IF Dissolution Chamber ────┘
                                            └─ F&A Hephaestus Forge T1 → Eternal Stella
                                                     all 3 alloys + Eternal Stella → Alloy Sword / Axe / Pick / Shovel → Alloy Paxel (5,000 XP)
```

Points to note: tools can't be crafted directly. They are **smithing upgrades** (template + ingot) of the previous tier, which keeps enchantments. Each armor description lists stats and set bonuses (ATM 24 armor/20 toughness, Vib 32/36, Unob 40/60, plus per-piece effects). Each tier is tied to a dimension: Overworld/Deep Dark → Nether/The Other → End/Beyond.

## 8. Welcome / Getting Started

- **Welcome** (ungrouped, 5 quests): a radial hub (pentagon center plus 4 checkmarks). It holds only meta information: philosophy, commands, chunks, teams. It also has images: the ATM logo and a clickable Discord waypoint icon (`click: "https://discord.gg/allthemods"`).
- **Ch1 "The Beginning"** is the true getting-started chapter. Its root description: *"ATM10 is a Kitchensink pack... However, there is an endgame goal: Making the ATM Star! This questline serves as a guide for all players, whether you are brand new to Minecraft or you've played through previous ATM packs."* Vanilla milestones teach the basics, while modded side quests (ore hammers, iron furnaces, generators, cables, power storage) are the first introduction to mods. They use smart filters so any of 2–3 mods' items count.

## 9. "All the Tweaks" / compressed content

There is no standalone chapter (see `atm10_chapters.md`). All The Tweaks items exist only as Star components in Ch3, and All The Compressed items appear as single-item grind quests with quantity subtitles (4x = 6,561 blocks, 5x = 59,049, 6x = 531,441). For a 1.7.10 backport, the closest equivalents are Avaritia (Neutronium compressor, singularities, Infinity Catalyst), ExtraUtilities compressed cobble/blocks, and a custom item mod or ContentTweaker/CustomItems for "ATM Star" components.

---

## 10. Checklist for the 1.7.10 / BetterQuesting adaptation

1. **Main line as numbered chapters** ("ATM: Chapter 1 …") with item-handoff roots instead of cross-chapter dependencies. BQ 1.7.10 does support cross-line prerequisites if you want hard links.
2. **Mod guides as optional lines** in groups. BQ has no chapter groups: emulate them by ordering the quest lines and using name prefixes or separator lines (for example "— Tech —").
3. **Gateway quests** in the Star line, one per required mod, with low-value XP rewards.
4. **Themed components with 6–9 AND prerequisites**, each spanning 5+ mods and mixing tech, magic and bosses, with OR-joins for mod alternatives.
5. **Rewards:** tier-matched per-mod loot bags (BQ `bq_standard:choice` rewards, or loot chests via `bq_standard:item` with BQ loot groups `bq_standard:loot`), a common-to-legendary generic ladder, XP that grows from 10/100 up to 100 levels at the finale, and same-tier refunds.
6. **Shapes:** BQ 1.7.10 has no shapes, but it does have icon size per quest in the line layout (`sizeX/sizeY`). Reuse the size hierarchy: finale 5x, components 2.5x, gateways 2x, standard 1–1.25x.
7. **Info quests** use BQ `bq_standard:checkbox` tasks, and descriptions keep the tone of "what / where / tip / joke".
8. **Tasks:** use trophies or drops for bosses; BQ `hunt` tasks are fine, but item drops are more robust in multiplayer. Use `bq_standard:location` with a dimension for dimension milestones, and OreDict or partial-match retrieval for "any of".
