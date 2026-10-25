# Witching Gadgets — estudo do capítulo `witchinggadgets`

## Visão geral

* **Mod:** Witching Gadgets `WitchingGadgets-1.8.49-GTNH.jar`, namespace `WitchingGadgets`. Addon
  do Thaumcraft 4.2.3.5 com aba `WITCHGADG` (68 pesquisas, prefixo [WG]).
* **Capítulo:** `quests/chapters/magia/witchinggadgets.yaml`, `order: 512`, 57 quests (meta 65:
  as receitas de Kama e da Capa de Bolsos Volumosos não são fabricáveis neste pack — ver
  Armadilhas — e não foram pedidas).
* **Papel:** Ato II (roca, tecidos, bolsas, capas, adornos, metalurgia) ao Ato IV (Terraformador,
  equipamento Primordial com Pérolas Primordiais). Sem tradução pt_BR no JAR.
* **Sem GregTech (isGT5uLoaded = false):** valem sempre as receitas "não-Greg" do código.

Fontes: `reg.py mod WitchingGadgets`, `reg.py research`, NEI (handlers `wgSpinningWheel` e
`wgInfernalBlastfurnace` com entradas/saídas; os handlers do Thaumcraft só com saídas),
`reg.py ore`, lang en_US, `config/WitchingGadgets.cfg` e classes descompiladas
(`witchinggadgets.common.recipes.*`, `WGResearch`, `WGContent`, `WGWandManager`,
`EventHandler`, `ItemCloak`).

## Progressão (ids reais)

* **Roca de Fiar** `WG_WoodenDevice:0` (Arcana: ferro + tábua + Mesa). NEI: 4 linhas → 2
  `item.WG_Material:0` (Fio); 2 linhas + 2 pepitas de ouro → 2 `:1`; 2 linhas + 2 pepitas de
  táumio → 2 `:2`.
* **Tecidos (Arcana sem formato, 15/primal):** `:3` Cloth of Spacious Folds (2 fios + 4 fios de
  táumio → 2), `:5` Bewitched Fleece (fio + 2 táumio + 1 ouro). Robes `item.WG_AdvancedRobeChest/Legs`.
* **Bolsas** `item.WG_Bag:0` (7 tecidos), `:3` Hungry (Arcana), `:1` Voidlinked (infusão: 2
  Sementes do Vazio + 2 tecidos), `:2` Enderlinked (infusão: Baú do Fim + 2 lãs + Olho do Fim).
* **Capas** (slot de Capa do Baubles Expanded): `item.WG_Cloak:0` (7 Tecidos Encantados), `:3`
  Wolven (3 Wolf Pelt `item.WG_Material:6`, drop de lobos), `:4` Raven (5 penas de corvo do TF +
  Estilhaço de Ar), `:1` Spectral (infusão: invisibilidade longa + 2 lãs + pérola).
* **Adornos** `item.WG_Baubles:0-8` (charm/vambraces de couro, Seraph's Charm, Steadfast,
  Berserker, Steady Progression, Sniper, Covetous Coin) e a Lucky Coin
  (`Thaumcraft:ItemResource:18` encantada).
* **Utilidades:** `item.WG_CrystalFlask` (Crisol: balde + Vacuos 2 + Vitreus 4), Arcane Abacus
  `item.WG_Material:7`, `item.WG_AdvancedScribingTools`, `item.WG_ScanCamera`, Guiding Thread
  `:11`, Aspect Library `WG_WoodenDevice:5`, `item.WG_ThaumiumShears`, `WG_RoseVine` (Crisol:
  Roseira + Herba/Perditio/Praecantatio 4).
* **Máquinas:** `WG_StoneDevice:0` Ethereal Wall, `:1` Ageing Stone, `WG_WoodenDevice:2` Stone
  Extruder, `:6` Ice Solidifier, `:1` Snowballer, `:4` Sauna Stove, `WG_MetalDevice:0` Essentia
  Pump, `:2` Terraformer + focos `:3-:12` (Plains/Taiga/Desert/Jungle/Hell/Mushroom por Arcana;
  River/Ocean/End/Taint por Infusão; `:13` Magical Forest só existe com GregTech).
* **Metalurgia:** aglomerados nativos do TC e `item.WG_Cluster:20xx` (minério + Metallum + Ordo),
  Aglomerado de Cinábrio, **Alto-Forno Infernal** (NEI: aglomerado → 3 lingotes + pepita; ferro →
  aço do IE).
* **Lapidação:** Gemcutter's Tools `item.WG_Material:8` (quartzo, pederneira, ferro, gravetos) em
  cima de uma Mesa = Cutting Table; `item.WG_InfusedGem` (cortes Point/Oval etc.).
* **Primordial:** Pérola sem Poder `item.WG_Material:12` → Pérola Primordial (infusão com os 6
  estilhaços); `item.WG_PrimordialGlove`, `WG_PrimordialSword/Hammer/Axe`,
  `WG_PrimordialHelm/Chest/Legs/Boots` (com peças do Tainted Magic: Shadow Fortress e Voidwalker
  Boots, pois o Tainted Magic está no pack).

## Estruturas

* **Alto-Forno Infernal** (sem Railcraft, fallback do código): base 8 Tijolos do Nether + Obsidiana
  no centro; meio Obsidiana nos cantos + Areia das Almas nas laterais e no centro; topo 8 Escadas de
  Tijolo do Nether viradas para o centro + Lava no meio. Forma com varinha (Ignis/Terra/Perditio
  50) e dá 3 de Warp "sticky".
* **Cutting Table:** Gemcutter's Tools sobre uma Mesa do Thaumcraft.

## Config do pack

Todos os módulos ligados (bolsas, capas, kamas, gemcutting, primordial, terraformer). Busca no
Taumonomicon ligada. Soul Tether cuida do inventário de Baubles e Galacticraft. Lista "Not
Trippling Cluster" (Iridium, Osmium, Titanium, Platinum, Uranium, Draconium etc.). Blocos do
Alto-Forno configurados para Railcraft, que **não** está no pack → fallback vanilla.

## Armadilhas e dicas

* **Warp das pesquisas (código):** Enderlinked Bag 15, Hungry Bag 2, Voidlinked Bag 4, Witching
  Wearables 4, Stone Extruder 2, Ice Solidifier 3, Ageing Stone 1, Ethereal Wall 3, Metallurgic
  Perfection (Clusters e Transmutation) 10 cada, Infernal Blast Furnace 3, Stealth 2, Backstab 2,
  Terraformer 10, focos Hell 2 / Taint 3 / End 3, Primordial Gauntlet 15, Weaponry 25, Armor 25.
* **Receitas quebradas neste pack:** a Capa de Bolsos Volumosos (`item.WG_Cloak:2`) usa o
  oredict `travelgearCloakBase` e as Kamas (`item.WG_Kama:*`) usam `baubleBeltBase` — **ambos
  vazios** no registro (Traveller's Gear não está no pack). A recarga das Illuminated Scribing
  Tools usa `dustSmallGold`, também vazio. Não pedidas no capítulo; vale avisar o dono do pack.
* Manto Espectral não engana chefes, Eldritch, aranhas, slimes nem quem usa Óculos.
* Alto-Forno: o que não é receita é vaporizado, incluindo jogadores.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `witchinggadgets:terraformador` | milestone | Reformador Terrestre (muda biomas). |
| `witchinggadgets:alto_forno_infernal` | milestone | Alto-Forno Infernal (aglomerado → 3 lingotes). |
| `witchinggadgets:armadura_primordial` | milestone | Armadura Primordial completa. |
| `witchinggadgets:mestre_wg` | milestone | **Marco final** — candidato ao Selo Arcano. |
