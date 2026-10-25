# ExtraPlanets — estudo do capítulo `extraplanets`

## Visão geral

* **Mod:** `ExtraPlanets-1.7.10-2.1.4.jar` (modid `ExtraPlanets`; entidades com prefixo
  `extraplanets.`). Sem pt_BR — nomes em inglês.
* **Capítulo:** `quests/chapters/tech/extraplanets.yaml`, `order: 391`, **124 quests** (32 main,
  31 normal, 39 optional, 2 info, 3 milestone, 7 boss, 10 portal). Depende do Galacticraft
  (`link: galacticraft:foguete_t3`; `deps_hidden` em `galacticraft:foguete_t2/foguete_t3`).
* **Papel:** Ato IV, **Selo das Estrelas** (7 chefes).

## Progressão (tiers do Mapa da Galáxia — bytecode `ExtraPlanets_Planets/Moons` + config)

| Nível | Corpos (dim) |
| --- | --- |
| 2 | Fobos (−33), Deimos (−35) |
| 3 | Vênus (−14) |
| 4 | Mercúrio (−13), Ceres (−20), Júpiter (−15), Io (−2004), Europa (−2002), Ganimedes (−2003), Calisto (−2001) |
| 5 | Saturno (−16), Titã (−40), Reia (−2006), Oberon (−41) |
| 6 | Urano (−17), Titânia (−2007), Jápeto (−43) |
| 7 | Netuno (−18), Tritão (−36) |
| 8 | Plutão (−2005) · 9 Éris (−21) · 10 Kepler 22b (−22) |

* **Chefes** (`TileEntityDungeonSpawner*`, config `Use default bosses=false`): Vênus
  `EvolvedMagmaCubeBoss` → `T4key`; Júpiter `EvolvedFireBatBoss` → `T5key`; Saturno
  `EvolvedGhastBoss` → `T6key`; Urano `EvolvedIceSlimeBoss` → `T7key`; Netuno `EvolvedSnowmanBoss`
  → `T8key`; Plutão `CreeperBossPluto` → `T9key`; Éris `CreeperBossEris` → `T10key`.
* **Loot de calabouço** (`addDungeonLoot`): nível 1 (baú lunar) + Mars Rover; nível N (4–10) +
  `schematicTierN`; nível 4 também Venus Rover.
* **Foguetes:** Blindagem N = Blindagem N−1 + metal prensado do tier (Prensa; T4 parte da
  Blindagem N3 do GC + Compressed Carbon). Metais: T4 Carbono (Vênus), T5 Paládio (Júpiter), T6
  Magnésio (Saturno), T7 Compressed Reinforced Crystal (Urano), T8 Zinco (Netuno), T9 Tungstênio
  (Plutão), T10 Dark Iron (Éris). Peças: nariz (3 blindagens + tocha), motor (4 blindagens + lata,
  respirador, isqueiro, botão), propulsor (4 metal prensado + lã de cor + lata de combustível + 2
  blindagens + respirador), aletas (6 blindagens; T4 = 2 GC + 4 T4).
* **Plataformas:** Tier 2 Launch Pad (3 Blocks of Titanium + 3 titânio → 5) para T4–8; Tier 3 (3
  Blocks of Tungsten + 3 zinco → 5) para T9–10; ambas 5×5 = 25 blocos (bytecode `*Single`: 25).

## Trajes térmicos (lang 1.7.10 + bytecode)

T2 (força 50) Vênus/Mercúrio; T3 (100) Júpiter/Saturno + luas; T4 (150) Urano/Netuno/Plutão/Éris
+ luas. Tecidos: T2 = 3 Desh Prensado + 2 Tecido Térmico; T3 = 3 Compressed Carbon + 2 T2; T4 = 3
Compressed Magnesium + 2 T3. Fobos/Deimos/Ceres usam o frio do GC (−1/−1,5).

## Materiais

Minérios fundem na fornalha (e com Pyrotheum na bancada): Mercury, Carbon, Uranium (Ceres),
Palladium, Nickel, Red Gem (Júpiter), Magnesium, Slime (Saturno), Crystal, White Gem (Urano), Zinc,
Blue Gem (Netuno), Tungsten (Plutão), Dark Iron (Éris), diamantes coloridos e Platinum (Kepler),
chumbo (Fobos/Deimos). Gemas também com Martelo do IE.

## Máquinas e itens

Advanced/Ultimate Refinery, Fuel Loader, Oxygen Compressor/Decompressor; Hybrid/Ultimate Solar;
baterias Mercury/Nickel/Zinc/Advanced/Ultimate; wafers (Diamond, Carbon, Titanium, Red/Blue/White
Gem — receitas no Fabricador, não capturadas pelo NEI); Mars/Venus Rover + Vehicle Charging Pad
(5×5) + Vehicle Charger; Kits (clique direito para vestir); Nuclear/Fire Bomb; maçãs de ferro e
diamante; estações espaciais em 11 planetas (config).

## Armadilhas e dicas

* As receitas do Compressor/Bancada NASA não estão no dump NEI: as quests indicam o padrão e
  mandam conferir na bancada.
* O lang do JAR mistura textos de versões novas (tier3/4/5 térmico); valem os do bloco 1.7.10.
* Gravidade de Kepler 22b é a mais próxima da terrestre (valor baixo de redução).

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `extraplanets:venus` | portal | Chegada a Vênus. |
| `extraplanets:chefe_venus` | boss | Cubo de Magma Evoluído — Selo das Estrelas. |
| `extraplanets:mercurio`, `extraplanets:ceres` | portal | Mercúrio e Ceres. |
| `extraplanets:jupiter` / `extraplanets:chefe_jupiter` | portal / boss | Júpiter e o Morcego de Fogo Evoluído. |
| `extraplanets:saturno` / `extraplanets:chefe_saturno` | portal / boss | Saturno e o Ghast Evoluído. |
| `extraplanets:urano` / `extraplanets:chefe_urano` | portal / boss | Urano e o Slime de Gelo Evoluído. |
| `extraplanets:netuno` / `extraplanets:chefe_netuno` | portal / boss | Netuno e o Boneco de Neve Evoluído. |
| `extraplanets:plutao` / `extraplanets:chefe_plutao` | portal / boss | Plutão e seu Creeper-Chefe. |
| `extraplanets:eris` / `extraplanets:chefe_eris` | portal / boss | Éris e seu Creeper-Chefe. |
| `extraplanets:kepler22b` | portal | Kepler 22b (Nível 10). |
| `extraplanets:gigantes_gasosos` | milestone | Júpiter e Saturno conquistados. |
| `extraplanets:sete_guardioes` | milestone | **Os 7 chefes — usar no Selo das Estrelas.** |
| `extraplanets:viajante_interestelar` | milestone | **Marco final** (Kepler 22b + 7 chefes). |
