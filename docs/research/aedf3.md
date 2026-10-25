# "A Era do Futuro" (AEDF) 1, 2 and 3: research dossier

Research date: 25 Oct 2026. Every claim is tagged **[C]** (confirmed from a primary or near-primary source, cited), **[L]** (likely, from multiple indirect sources), or **[I]** (my inference, for example from episode titles or mod lists). The short answer to "who made AEDF3" is surprising, so read §1 first.

---

## 1. Key findings (TL;DR)

1. **"A Era do Futuro" is a Portuguese/Brazilian YouTuber multiplayer series organized by SirKazzio** (Kazzio). It is **not** by Tazercraft. Kazzio is probably Portuguese **[L]**: he founded the "Vila dos Portugueses", announces lives in "PT / BR" times, and his titles use European-Portuguese spelling ("equipa", "fenómeno"). Pac & Mike (TazerCraft) were **participants in season 1 only**. **[C]**
   - **AEDF 1** (Oct–Dec 2013, MC **1.5.2**): about 36 YouTubers on one server, with IC2, BuildCraft, Thaumcraft 3, Twilight Forest, Galacticraft, Atum, Mystcraft, EE3 and more. **[C]**
   - **AEDF 2** (announced Dec 2014, run in 2015, MC **1.6.4**): about 20 YouTubers (Rezende, EduKof, Casal de Nerd, Monark…). It has 68 episodes on Kazzio's channel, ending in a space arc (Moon, Mars) and "O FIM!! EXPLODINDO A VILA". **[C]** for the episodes and the version (version from the modpack file names).
   - **AEDF 3**: SirKazzio only ever published **"A ERA DO FUTURO 3 - Trailer ( 1 de Abril - Dia das Mentiras )"**, an **April Fools' joke trailer**. **There was no official AEDF3 YouTuber series by Kazzio.** **[C]** for the title. That no series followed is **[L]**: Kazzio's re-upload archive of 130 AEDF videos contains AEDF1 and AEDF2 episodes but no AEDF3 episodes.
2. **"A Era do Futuro 3" as people know it today is a community/server modpack for MC 1.7.10.** It is the fan "continuation" of the pack:
   - **"A Era do Futuro 3 (Oficial)"** on Technic, by user **M4uricioGM**. MC 1.7.10, description "Continuação do ModPack A Era do Futuro". Versions 1.5 (21 Aug 2015) through 2.5 (8 Aug 2016). About 57k installs and 220k runs. **[C]** (Technic API)
   - Later it became a *genre* of Brazilian 1.7.10 tech servers branded "Era do Futuro 3": TotalCraft, InfinityCraft ("AEDF3 - IC", still updated in Oct 2026), GalactiCraft Network ("AEDF3 Plus"), and the tags list techcraftbrasil, skytech, finalcraft and agefortech. **[C]** (Technic API)
3. **No quest book existed in the original or official AEDF3 packs.** The 2015–16 official zip contains no BQ/HQM files **[C]**. Later server remakes added BetterQuesting. InfinityCraft's changelog says "Adicionado BetterQuesting…" (Nov 2020) and TotalCraft V3 ships BetterQuesting 3.8.14-GTNH, but their quest databases are server-side and **not in the client packs** **[C]** (zip listings).

---

## 2. Sources

| # | Source | What it gave |
|---|---|---|
| S1 | TazerCraft Fandom wiki, "A Era do Futuro" (read through the MediaWiki API: `tazercraft.fandom.com/api.php?action=parse&page=A_Era_do_Futuro`; the page itself is <https://tazercraft.fandom.com/wiki/A_Era_do_Futuro>) | Creator SirKazzio, dates 20 Oct to 4 Dec 2013, 13–14 TazerCraft episodes, 36 participants, villages, Pac & Mike's Thaumcraft golems, server lag, sequel announced Dec 2014 without Pac & Mike (cites a Mike tweet: <https://twitter.com/MikeLink1/status/544166057255378944>) |
| S2 | Dailymotion API, SirKazzio channel re-uploads (owner `x1b1qjz`, all re-uploaded 9 May 2018): `api.dailymotion.com/user/x1b1qjz/videos?search=ERA DO FUTURO` | 130 AEDF videos: AEDF1 Ep 1–33 plus 5 lives, AEDF2 #1–#68, trailers, the AEDF3 April Fools trailer, and full video descriptions (participant lists, mod highlights, modpack links). Saved at `dl/dm/aedf_kazzio.json` in the scratchpad. |
| S3 | Blog "A Era do Futuro" (fan/companion blog): <https://aeradofuturo.blogspot.com/>, `/p/participantes.html`, `/p/dowloands.html` | AEDF1 participant list, the 45-mod list of the "SUPER PACK DE MODS #5 - A ERA DO FUTURO", and the Nov 2013 posts (Feromonas killed all Twilight bosses, Leon's house hit by a meteor, meteor mod removed) |
| S4 | Blue Craft blog, 29 Nov 2013: <http://bluecraftblogger.blogspot.com/2013/11/a-era-do-futuro.html> | Pack described as partly Tekkit mods, with 30+ players on Kazzio's server |
| S5 | Simples Minecraft blog, Jan 2015: <https://simplesminecraft.blogspot.com/2015/01/modpack-era-do-futuro-2.html> | AEDF2 pack "2.0.0", server file `Servidor_A_Era_do_Futuro_2.0.0_1.6.4.rar` (so MC 1.6.4), Technic slug `a-era-do-futuro-200` |
| S6 | Technic Platform API (`api.technicpack.net/modpack/<slug>?build=999` and `/search?q=era do futuro`) | Metadata for: `a-era-do-futuro-iii` (AEDF3 Oficial, M4uricioGM), `a-era-do-futuro-3-ic` (InfinityCraft), `a-era-do-futuro-3-plus` and `a-era-do-futuro-3-galactcrafts` (GalactiCraft Network), `total-craft-v3`, `a-era-do-futuro-oficial` (AEDF1 1.5.2 "modpack do Sir. Kazzio"), `a-era-do-futuro-20-original` (AEDF2 1.6.4), `a-era-do-futuro-1-fg` and `-2-fg` (FG server packs, about 2M runs each), `total-craft-severs` (AEDF1 TotalCraft), `a-era-do-futuro-cc` (AEDF CCN Classic 1.5.2), `era-do-futuro-1710-2020`. JSON saved in `dl/technic/`. |
| S7 | **AEDF3 (Oficial) zip** from M4uricioGM's Technic pack (`https://www.dropbox.com/s/o9w45gahrzo2q4u/aedf3.zip`, 115 MB, v2.5, files dated Nov 2015 to Aug 2016) | **Exact 1.7.10 mod list** (§4.3), configs, one MineTweaker script. No quest files. Saved at `dl/aedf3_official/aedf3.zip`. |
| S8 | InfinityCraft Solder (`solder.infinitycrafts.com.br/api/modpack/a-era-do-futuro-3-ic/2026.10.22.1`) plus a remote listing of `aedf3-bundle-2026.10.22.1.zip` | Mod list of the modern "AEDF3 - IC" server pack (§4.4). Listing at `dl/ic/listing.txt`. |
| S9 | Remote listings of the TotalCraft V3 (`github.com/SrYthan/A-Era-do-Futuro-V3-TotalCraft` release 12.2), AEDF1 TotalCraft and AEDF CCN Classic zips | AEDF3-style server mod list and AEDF1-era mod lists. Listings at `dl/tc3`, `dl/tc1` and `dl/ccn`. |
| S10 | minecraft-mp listing "A ERA DO FUTURO 3 - IC": <https://minecraft-mp.com/server-s365367> | States the server's progression: **"IC2 → Thermal → Mekanism → Draconic → Avaritia"** |
| S11 | CurseForge remakes (context only): AEDF20 (1.12.2) <https://www.curseforge.com/minecraft/modpacks/era-do-futuro-2020>, AEDF25 (1.20.1), AEDF26 (1.21.1), AEDF Rewritten | These describe AEDF as "a famous modpack in Brasil… in an older version" and are 2020s nostalgia remakes |
| S12 | YouTube search results (titles only): "A ERA DO FUTURO DEPOIS DE 10 ANOS!" <https://www.youtube.com/watch?v=OiwTDckbIjs>, and "EX-MEMBRO DA ADR REAGINDO A ERA DO FUTURO DEPOIS DE 10 ANOS!" <https://www.youtube.com/watch?v=5Fpt23cehws> | Evidence of a 2020s reunion/nostalgia (snippet mentions Rezende, VenomExtreme, SirKazzio). Not viewed. |

Not reachable: technicpack.net web pages (HTTP 403), the Craftlandia forum thread (403), board.aternos.org (DNS), the original Dropbox/MediaFire links for the AEDF1/AEDF2 packs (dead), the FG Solder (502), and galactcraft.com (DNS).

---

## 3. Series history

### 3.1 AEDF 1: "A Era do Futuro" (2013)
- **Organizer:** SirKazzio. Server hosted by brasilhosting.net. Pack on Technic as `a-era-do-futuro.188168` (link in the Ep 1 description). **[C]** (S2)
- **Dates:** trailer posted around 13 Oct 2013 (S3). The series ran 20 Oct to 4 Dec 2013 (S1). Kazzio's channel has 33 episodes, 5 "LIVE DO FUTURO" streams, a map download at Ep 29, and village tours in Ep 30–33. **[C]**
- **MC version:** 1.5.2. **[L]** (the Technic `a-era-do-futuro-oficial` pack "do Sir. Kazzio" is 1.5.2, the fan AEDF1 servers are 1.5.2, and the CCN Classic pack has a 1.5.2 `coremods/` folder)
- **Participants (36):** SirKazzio (organizer), LuginBR, Wolff, Wuant, Kristy, Feromonas, MrNikki, JVNQ, Malena0202, DrM4ster, Ambuplay, TheSkullsCraft, Japa, Remedy, Gamermestre, BaixaMemoria, AuthenticGames, D4rkFrame, Hydro, TazerCraft (Pac & Mike), NioBloGames, HeyKroni, ZoaWar, FeBatista, iDrika, Rezende, Val, VenomExtreme, Vilhena, Leon (Coisa de Nerd), Seymour, Joserobjr, PhoenixBR, Soxzzin, Cellbit, Monark. **[C]** (S1, S3, S2)
- **Stated goal:** "nos divertirmos entre youtubers e explorar os mods" (have fun among YouTubers and explore the mods). There was no win condition. **[C]** (S2 trailer)
- **Ending:** no proper finale, blamed on server lag and Thaumcraft 3 problems. The world was released for download (Ep 29). **[C]** (S1, S2)

### 3.2 AEDF 2 (2015)
- Announced Dec 2014 (S1). Video "A ERA DO FUTURO 2? QUANDO KAZZIO?!" plus an official trailer. Pack "A Era do Futuro 2.0.0" for **MC 1.6.4** (S5). Pac & Mike declined to take part (S1). **[C]**
- **Participants:** SirKazzio (organizer), Rezende, EduKof, CasalDeNerd, Miss (MissPinguina), Wolff, Ravanha, Afreim, Luiz, Maycon, Monark, Lugin, NioBlo, DrM4ster, MrNikki, GameplayRJ, Vilhena, Rayder, Ine, PigManExtreme, Dois Marmotas, Phoenix. **[C]** (S2, AEDF2 #1 description)
- **Episodes:** 68 on Kazzio's channel, from "#1 O MELHOR MINÉRIO!! JÁ?!" to "#68 O FIM!! EXPLODINDO A VILA!!". **[C]**

### 3.3 AEDF 3
- **Kazzio:** only "A ERA DO FUTURO 3 - Trailer ( 1 de Abril - Dia das Mentiras )". Its description lists "IRMÃOS DA #ADR" (Rezende's group: Rezende, Casal De Nerd, SirKazzio, Wolff, Luiz, Miss…) and the series Apocalipse, A Salvação 2, GTA V Online, Dubsmash. **[C]** for the title and description. The year is undated in the re-upload; the Snapchat/Dubsmash/"A Salvação 2" references suggest **1 April 2016** **[I]**.
- **Fan/official modpack:** "A Era do Futuro 3 (Oficial)" by M4uricioGM, Aug 2015 to Aug 2016, MC 1.7.10 **[C]**. Who M4uricioGM is, and whether they were linked to Kazzio, is **unverified**. "Oficial" appears to be the uploader's own label.
- **Server lineage (2016 to 2026):** TotalCraft "A Era do Futuro V3" and "AEDF3 ++", InfinityCraft "A Era Do Futuro 3 - IC" (Solder builds dated Oct 2026, IP aedf3.infinitycrafts.com.br), GalactiCraft Network "AEDF3 Plus" / "AEDF3 (Online)" (2022, "servidor desativado"). **[C]** (S6, S8, S9, S10)
- **Unverified:** whether any YouTuber recorded an "AEDF3" series on one of these servers. I found no episode list.

---

## 4. Mod lists

### 4.1 AEDF1 (1.5.2, 2013)
From the companion blog's pack "SUPER PACK DE MODS #5 - A ERA DO FUTURO" (S3) **[C]**: GalactiCraft, TreeCapitator, Rei's Minimap, IndustrialCraft 2, BuildCraft, Advanced Solar Panels, Atum, Backpacks, BackTools, Battle Towers, Better Furnaces, BiblioCraft, Damage Indicators, Dimensional Doors, Dungeon Pack, Enchanting Plus, Equivalent Exchange 3, ExtraBiomesXL, Flat Bedrock, Gammabright, GraveStone, Gravitation Suite, Inventory Tweaks, Iron Chests, Logistics Pipes, Mo' Zombies, Mob Dismemberment, More Bows, Mutant Creatures, Mystcraft, Power Converters, Primitive Mobs, Thaumcraft (3), Barrels, Twilight Forest, Thermal Expansion, Universal Electricity, Vanilla Plus, Asgard Shield, IC2 Advanced Machines, iChunUtil, Nether Ores, Gravity Gun, Legend Gear, Falling Meteors.

Kazzio's trailers also mention Portal Gun and "quase 60 mods" **[C]** (S2). The episode titles show MineFactory Reloaded and IC2 UU-Matter in use **[I]**.

The AEDF CCN Classic re-creation (1.5.2) adds Applied Energistics (1.5.2), Modular Powersuits, Natura, Nether Star Tools, Custom NPCs, Dungeon Mobs, Wild Caves, VoxelMap and PowerCrystals Core **[C]** (S9). Treat these as a fan approximation.

### 4.2 AEDF2 (1.6.4, 2015)
No original mod list was retrievable, because the packs are dead. Inferred from the episode titles **[I]**: Falling Meteors (#2 "chuva de meteoros"), Thaumcraft 4 (#3 "Aprendiz do Harry Potter"), Tinkers' Construct (#6 "cozinhar armas", #9 "minérios ficaram líquidos", #11–12 tools and swords), Mutant Creatures (#7–8), IC2 with Nano/Quantum armor (#23 "poderes nano", #48 "melhor armadura"), Portal Gun (#27 "portal gun da batata"), Lucky Block (#38), Galacticraft with Mars (#40 "outros planetas", #46 spaceship, #49 rocket, #50 fuel, #52–54 Moon and aliens, #60–61 Mars, #63 space station), a weather/tornado mod (#51 "tornado"), and some ore-multiplication or EE-style mod (#10, #13, #18).

A fan forum list for the "AEDF2 - FG" pack names IC2, BuildCraft, Thermal Expansion, Galacticraft, Thaumcraft, Iron Chests, Better Furnaces and Atum **[L]** (search snippet of the Aternos thread).

### 4.3 AEDF3 (Oficial), 1.7.10, M4uricioGM, v2.5 (Aug 2016): exact list from the zip **[C]** (S7)
- **Tech, IC2 family:** IndustrialCraft 2 2.2.804-experimental, AdvancedSolarPanel 3.5.1, GraviSuite 2.0.3, IC2 Nuclear Control 2.3.3a.
- **Tech, other:** BuildCraft 7.1.13 (+compat), Thermal Expansion 4.0.3B1 / Thermal Foundation / Thermal Dynamics, Redstone Arsenal, Mekanism 9.1.0 (+Generators, Tools), Big Reactors 0.4.3A, Draconic Evolution 1.0.2-Snapshot_8, EnderIO 2.3.0.421, EnderTech, MineFactory Reloaded 2.8.0 + NetherOres, Applied Energistics 2 rv2-stable-10 + ExtraCells 2.3.2, Extra Utilities 1.2.12, OpenBlocks 1.4.4, OpenPeripheral (Core/Addons/Integration; ComputerCraft is **not** included), Wireless Redstone CBE, Ender Storage, ForgeMultipart / ForgeRelocation (+MrTJPCore).
- **Magic:** Thaumcraft 4.2.3.5, Thaumic Tinkerer 2.5.
- **Tools/adventure:** Tinkers' Construct 1.8.8 (+TiC Tooltips, Mantle), Twilight Forest 2.3.7.
- **Building/storage:** Chisel 2.9.3.9, Carpenter's Blocks 3.3.7, BiblioCraft 1.11.4 (+BiblioWoods for BoP/Forestry/Natura, even though those three mods are not in the pack), Iron Chests, JABBA.
- **Utility:** Baubles, NEI (+NEI Addons, NEI Integration, Not Enough Resources), Waila (+Harvestability, Wawla), JourneyMap 5.1.3 unlimited, Inventory Tweaks, denseores, Guide-API, iChunUtil, FastCraft, FastLeafDecay, OptiFine HD C1, LiteLoader + Gammabright, BetterTitleScreen, MineTweaker3 + ModTweaker2 (one script: `scripts/z.zs` recipe change for GraviSuite `itemSimpleItem:2`, the cooling core, using IC2 coolant cells, diamond heat switches, heat plating and iridium). ChickenChunks is present but disabled.
- **Not included** (contrast with AEDF1/2): Galacticraft, Atum, Mystcraft, Portal Gun, Meteors, EE3/ProjectE, Botania, Blood Magic, Avaritia, any quest mod.

### 4.4 Modern "AEDF3" server packs (for comparison) **[C]**
- **InfinityCraft "AEDF3 - IC"** (S8, build 2026.10.22.1, Forge 10.13.4.1614) adds to the core above: **Avaritia 1.13 (+AOBD singularities, Thermal Singularities)**, Botania r1.8, Blood Magic 1.3.3, Ars Magica 2, Forbidden Magic, Thaumic Energistics/Exploration/Horizons, Gadomancy, Forestry + Gendustry, Immersive Engineering 0.7.7, NuclearCraft 1.9g, Magical Crops (+Armoury), Biomes O' Plenty, Compact Machines, Custom NPCs, Customitems, BetterQuesting 3.0.328, Enchanting Plus, InventoryPets, Iguana Tweaks, ExtraTiC, SuperSolarPanels (photonic), Power Converters, UniDict, and Atum in the config. Its stated progression is **IC2 → Thermal → Mekanism → Draconic → Avaritia** (S10).
- **TotalCraft "A Era do Futuro V3"** (S9, v12.2) uses a GTNH-library stack: IC2 + IndustrialUpgrade, GraviSuite, Thermal, MFR, Mekanism CE, Big Reactors, Draconic 1.5.4-GTNH (+DraconicMinus), EnderIO, AE2 (GTNH), Extra Utilities, Thaumcraft (+Tainted Magic, Thaumic Horizons, Thaumic Tinkerer, Forbidden Magic, Gadomancy, Thaumic Energistics), Botania (+Advanced Botany), Blood Magic, ProjectE (+Equivalent Energistics), **Avaritia (+Avaritiaddons, Eternal Singularity, Universal Singularities)**, Twilight Forest, Atum, TConstruct, BiblioCraft, Chisel, Et Futurum, and **BetterQuesting 3.8.14-GTNH** (no default quest DB shipped).

---

## 5. Reconstructed progression timeline: stages that can become quest chapters

Basis: (a) the confirmed AEDF3 1.7.10 mod list (§4.3); (b) the "feel" milestones that recur across AEDF1/AEDF2 episodes (titles in S2); (c) the community server progression (S10). The *order* below is my design synthesis **[I]**, anchored on episode order where possible. AEDF1 episode numbers are given as "E#" and AEDF2 numbers as "#".

| Stage | Goal | Mods | Key items and milestones | Series evidence |
|---|---|---|---|---|
| **0. Chegada / A Vila** (Arrival / The Village) | Spawn, join or found a village, base basics | Vanilla, BiblioCraft, Chisel, Carpenter's, Iron Chests | Claim land, build a house, storage | Villages are the series' signature (Vila do Boi, Vila da Discórdia, Vila dos Portugueses, Vila Flutuante, Vila das Amigas, Vila Kapodi, Vila de Ximbas). E1 "E Começamos!", E30–33 village tours **[C]** |
| **1. Idade da Pedra e do Ferro** (Stone and Iron Age) | Early survival, mining, first dungeon | Vanilla, Tinkers' Construct, JourneyMap | Smeltery ("minérios líquidos"), Tinkers tools, first diamond | E2 "Rei da Montanha", E3 "Mega Dungeon", E6 "Equipa Obsidiana", #1 "O melhor minério", #6/#9/#11/#12 Tinkers **[C] titles / [I] mapping** |
| **2. Era Industrial** (Industrial Age) | First power and ore doubling | IC2 (generator, macerator, electric furnace, BatBox → MFE → MFSU), BuildCraft | Copper cable, macerator, MFE | E4 "Nuvem e Indústria", E5 "Máquinas e Encantamentos" **[C]** |
| **3. Aventura Infernal** (Infernal Adventure) | Nether trip, Wither | Vanilla, NetherOres | Nether star, Wither kill ("Batata e Wither") | E7 "Aventura Infernal", E9 "Batata e Wither", #27 "Wither boss" **[C]** |
| **4. Energia Solar** (Solar Power) | Free power, an iconic AEDF goal | AdvancedSolarPanel | Solar → Advanced → Hybrid → **Ultimate Hybrid Solar Panel**, UU-Matter | E12 "UU-Matter e Advanced Solar Panel", E17 "ULTIMATE Hybrid Solar Panel" **[C]** |
| **5. Armaduras do Futuro** (Armor of the Future) | Power-armor ladder | IC2 + GraviSuite | **Nano Suit → Quantum Suit → GraviChestPlate**, Nano Saber, **Vajra**, GraviTool, Ultimate Solar Helmet | E8 "Nano Kazzio Suit", E14 "Nano Saber", E18 "QUANTUM SUIT", E19 "VAJRA e Ultimate Solar Helmet", E20 "GRAVITOOL", E21 "GraviChestPlate", #23 "poderes nano", #48 **[C]** |
| **6. Magia** (Magic) | The Thaumcraft "wizard" arc, the series' other signature | Thaumcraft 4, Thaumic Tinkerer | Thaumonomicon, wands (iron → gold → thaumium), Alumentum, **golems** (automated wheat farm, "Padaria do Gutin"), infusion | E10 "Aprendiz de Mago", E11 "Alumentum", E13 "Magias e Floresta", E15 "Varinhas", Pac & Mike's golems (S1), #3 "Aprendiz do Harry Potter" **[C]** |
| **7. Caçadores de Bosses** (Boss Hunters) | Twilight Forest boss order plus vanilla bosses | Twilight Forest, vanilla | Naga → Lich → Minoshroom → Hydra → Knight Phantoms → Ur-Ghast → Snow Queen. **Boss-head trophy shelf** in the village. Ender Dragon. | E16 "Caçadores de Bosses", blog: Feromonas "Rei dos bosses" put the heads on a shelf, E27 "O ENDER DRAGON", E28 "O Mundo Escuro" (possibly the Dark Forest **[I]**) **[C]** |
| **8. Automação e Indústria Pesada** (Automation and Heavy Industry) | Ore processing and automation | Thermal Expansion/Dynamics, MFR, BuildCraft quarry, Extra Utilities, EnderIO | Pulverizer/Induction Smelter, ducts, quarry ("escavadora"), MFR farms | #43 "escavadora de diamante", #13/#18 multiply ores **[I]** |
| **9. Mekanism** | x3/x5 ore processing, teleporters, jetpack | Mekanism | Enrichment → Purification → Chemical Injection; Teleporter ("teleportar"); Digital Miner; Jetpack | #16 "teleportar blocos" **[I]** (Mekanism is in AEDF3 and named on the IC server path) |
| **10. A Casa do Futuro** (The House of the Future), build challenge | The futuristic mega-house | Chisel, Carpenter's, BiblioCraft, OpenBlocks, EnderIO | Build a "Casa do Futuro" with a bedroom, library, roof forest and so on | AEDF2 #15, #22, #24–26, #28–30, #32, #36 (a whole arc about Kazzio's house) **[C]** |
| **11. Armazenamento Digital** (Digital Storage) | Storage network | AE2 + ExtraCells, JABBA | ME controller, drives, autocrafting | #17 "A organização" **[I]** |
| **12. Energia Avançada** (Advanced Energy) | Big power | Big Reactors, IC2 nuclear (+Nuclear Control), Draconic energy core | Reactor, turbine; "a melhor energia", "super energia, como guardar?" | #20, #33, #35 **[C] titles / [I] mapping** |
| **13. Evolução Dracônica** (Draconic Evolution), endgame | Draconic tiers | Draconic Evolution | Draconium → Wyvern → Awakened gear, Energy Core, Chaos Guardian | AEDF3 pack and IC server path (S7, S10) **[C]**. Not in the YouTube episodes (they predate DE). |
| **14. (Server-style) Infinity** (optional) | Ultimate endgame | Avaritia (server remakes only) | Neutronium, singularities, Infinity Catalyst, Infinity armor and sword | IC and TotalCraft V3 packs (S8, S9, S10) **[C]**. **Not** in the 2015–16 official pack. |
| **15. Corrida Espacial** (Space Race), optional, AEDF1/2 nostalgia | Moon and Mars | Galacticraft (**not** in AEDF3 Oficial) | Rocket T1 → Moon (dungeons, bosses, aliens) → space station → Mars | AEDF1 E23 "Dungeon na Lua", E24 "Bosses da Lua"; AEDF2 #40–#66 **[C]** |
| **16. O Fim** (The End), finale | A memorable closing event | – | "Explode the village" (AEDF2 #68), the map download, the Mystcraft "Mundo Kazziano" (AEDF1 E25) | **[C]** |

### Signature builds and running gags, useful as flavor quests **[C]** unless marked
- **Vila do Boi** (Pac, Mike, JV, Batista): main base, "Casa de Bruxo" (Thaumcraft wizard house), the first **"Padaria do Gutin"** (wheat auto-harvest by Thaumcraft golems plus a windmill), machine rooms, and Twilight Forest portals. Pac & Mike planned to rebuild the **ChumeCompany** factory from their Tekkit War series (S1).
- **Casamento de Gutin e Carla** (Gutin and Carla's wedding): iron golems were being prepared for it, then it was postponed and became the spin-off "Operação G.U.T.I.N" (S1).
- **Boss-head shelf** (Feromonas, Twilight Forest) and Leon's house destroyed by a lava meteor (S3).
- **Kazzio's "Casa do Futuro"** in AEDF2, later rebuilt in space ("Casa do futuro no espaço", "Levando porcos para o espaço").
- "Equipa Obsidiana" (team), "Vila Flutuante" (floating village), "Fábrica de donuts dos Simpsons" (AEDF2 #21), Lucky Blocks with EduKof, "trolling with TNT".

---

## 6. What I could not verify
- Any **AEDF3 YouTuber series** beyond Kazzio's April Fools trailer, and the exact date of that trailer (2016 is inferred).
- **Who M4uricioGM is**, and whether their "Oficial" AEDF3 pack was endorsed by Kazzio or tied to a specific server.
- The **original AEDF2 mod list**: the packs are dead, and §4.2 is inferred from episode titles.
- The exact MC version of AEDF1 from a primary source: 1.5.2 is strongly implied by every derivative pack, but Kazzio's Technic page `a-era-do-futuro.188168` no longer resolves.
- The YouTube upload dates. The Dailymotion copies are all re-uploads dated 9 May 2018.
- **Quest content of the BetterQuesting-based AEDF3 servers.** The databases are server-side and not shipped in the client zips.
