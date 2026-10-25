# The Aether (Legacy) — estudo do capítulo `aether`

## Visão geral

* **Mod:** The Aether (Legacy) `aether-1.7.10-v1.1.2.5.jar` — namespace de registro
  `aether_legacy`, pacote `com.gildedgames.the_aether`, versão `v1.1.2.5` (`reg.py mods`).
* **Dimensão:** `4` (`config/aether/AetherI.cfg`: `Aether Dimension ID=4`, bioma `127`).
  Cair de uma ilha leva ao Mundo Normal (`Dimension below aether=0`).
* **Capítulo:** `quests/chapters/aventura/aether.yaml`, `order: 210`, **118 quests**
  (23 main, 40 normal, 41 optional, 5 info, 1 portal, 3 boss, 4 milestone, 1 finale).
* **Papel na campanha:** Ato III, segundo dos **Três Reinos Perdidos**: o Reino dos Céus, onde as
  Valquírias protegiam as páginas do céu e o Espírito do Sol as mantém reféns. Termina na
  **Chave dos Céus** (`aether:chave_ceus`). As descrições citam as Valquírias vigiando o acordo
  Lich × Faraós; o mapa mostra por `link:` as chaves do Crepúsculo e das Areias.

Fontes: `reg.py mod aether_legacy`, receitas de bancada do dump, `lang/en_US.lang` (todos os
textos `lore.aether_legacy.*` do Book of Lore, diálogos da Valquíria/Rainha/Espírito do Sol,
conquistas), bytecode com `javap` (`AetherRegistries`: receitas do Altar e do Freezer e seus
combustíveis; `BronzeDungeon`/`ComponentSilverDungeon`/`ComponentGoldenDungeon`: saque;
`EntitySlider`, `EntitySunSpirit`, `EntityValkyrieQueen`: regras de dano e drops;
`BlockAetherLog`: Golden Amber), `config/aether/AetherI.cfg`. NEI ainda não importado.
O mod **não tem pt_BR**: nomes em inglês. O JAR traz pastas `recipes/`/`advancements/` estilo
1.12 que **não** valem no 1.7.10; as receitas reais são as do registro (código).

## Energia / recursos-base

Sem energia. Combustíveis das máquinas (bytecode):

* **Altar of Light** (`aether_legacy:enchanter`; a config mantém o nome "Altar"): combustível
  `ambrosium_shard` (500 ticks); acelera com Enchanted Gravitite embaixo.
* **Freezer** (`freezer`): combustível `icestone` (500); acelera com Icestone embaixo.
* **Incubator** (`incubator`): combustível `ambrosium_torch`.

## Progressão

1. **Portal:** moldura de Glowstone (formato de portal do Nether) + Balde de Água (comum ou de
   Skyroot; `Activate portal with only Skyroot bucket=false`). Primeira entrada dá
   `lore_book` e `golden_parachute` (config `true`).
2. **Skyroot** (`skyroot_log` → `skyroot_planks` → `skyroot_stick`): ferramentas que dobram drops.
3. **Holystone** (`holystone`): ferramentas que soltam Ambrosium; **Ambrosium**
   (`ambrosium_shard`, tochas `ambrosium_torch`).
4. **Zanite** (`zanite_ore` → `zanite_gemstone`, exige picareta de Holystone): ferramentas e
   armadura que ficam mais fortes conforme gastam.
5. **Máquinas:** Altar (8 Holystone + Zanite), Freezer (Holystone + Icestone + 3 Skyroot Planks),
   Incubator (8 Holystone + Ambrosium Torch).
6. **Gravitite** (`gravitite_ore`, exige Zanite; flutua) → Altar → `enchanted_gravitite` →
   ferramentas/armadura (super pulo com o conjunto).
7. **Masmorra de Bronze** (Slider) → `dungeon_key` (meta 0).
8. **Masmorra de Prata** (10 `victory_medal` das Valquírias → Rainha Valquíria) →
   `dungeon_key:1`.
9. **Masmorra de Ouro** (Espírito do Sol) → `dungeon_key:2` + `sun_altar`.

### Receitas do Altar (enchant) e do Freezer (bytecode de `AetherRegistries`)

* Altar: Holystone → `healing_stone`; `gravitite_ore` → `enchanted_gravitite`; `quicksoil` →
  `quicksoil_glass`; `blueberry` → `enchanted_blueberry`; `dart` → `dart:2`; `dart_shooter` →
  `dart_shooter:2`; `skyroot_bucket:2` (veneno) → `skyroot_bucket:3` (remédio); discos vanilla →
  `aether_tune`/`legacy`; e **reparo** de ferramentas/armaduras do Aether e da Superfície.
* Freezer: `aercloud` → `aercloud:1` (azul); folhas de Skyroot/Golden Oak → `crystal_leaves`;
  `skyroot_bucket:1` → gelo; gelo → gelo compactado; balde de água → gelo; balde de lava →
  obsidiana; `ascending_dawn` → `welcoming_skies`; `iron_ring`/`golden_ring` → `ice_ring`;
  `iron_pendant`/`golden_pendant` → `ice_pendant`.

## Materiais e geração de mundo

* Minérios: `ambrosium_ore` (comum), `zanite_ore` (pouco raro), `gravitite_ore` (raro, flutua),
  `icestone`. Blocos: `aercloud` (0 fria, 1 azul, 2 dourada), `quicksoil`, `holystone`,
  `mossy_holystone` (perto de masmorras). `aerogel` = lava despejada no Aether.
* **Golden Amber:** tronco `golden_oak_log` quebrado por machado do Aether de Zanite/Gravitite/
  Valkyrie ou machado de diamante (`BlockAetherLog`).
* Comida: `blueberry` (dobra em `enchanted_aether_grass`, criada usando Ambrosium Shard na
  grama), `white_apple` (cura veneno), `gummy_swet`.

## Máquinas, estruturas e multiblocos

* **Masmorra de Bronze:** dentro das ilhas; Sentries, `carved_trap`, `chest_mimic`. Chefe
  **Slider** (`aether_legacy.slider`): só leva dano de **picareta** (inclusive do Tinkers).
  Saque do baú (`getBronzeLoot`): flaming_sword, notch_hammer, phoenix_bow, cloud_staff,
  lightning_knife, repulsion_shield, valkyrie_lance, agility_cape, sentry_boots, gummy_swet.
  Baús comuns: zanite tools, iron/golden ring, swet_cape, dardos, discos.
* **Masmorra de Prata:** em leitos de nuvens com pilares; Valquírias (`aether_legacy.valkyrie`)
  dão `victory_medal`; a Rainha (`valkyrie_queen`) exige 10 medalhas. Saque
  (`getSilverLoot`): armadura/ferramentas Valkyrie, holy_sword, lightning_sword, armadura
  Neptune, regeneration_stone, gummy_swet; `Valkyrie Cape` ativa na config, `Golden Feather`
  **desativada** no saque.
* **Masmorra de Ouro:** dentro de ilhas grandes (Hellfire Stone). **Espírito do Sol**
  (`sun_spirit`) só leva dano de **cristal de gelo** (`EntityCrystal` tipo ICE) rebatido; invoca
  Fire Minions. Drop: `dungeon_key:2` + `sun_altar`. Saque: iron_bubble, vampire_blade,
  pig_slayer, armadura Phoenix (vira Obsidian na água), life_shard, armadura Gravitite,
  chain_gloves, obsidian_chestplate.
* Blocos das masmorras (`locked_*`) são inquebráveis até o chefe cair.

## Dimensões, biomas, estruturas, chefes e mobs

Entidades (`kill`): `aether_legacy.aechor_plant`, `swet`, `zephyr`, `cockatrice`, `sentry`,
`mimic`, `valkyrie`, `valkyrie_queen`, `slider`, `sun_spirit`, `fire_minion`. Passivos: moa,
phyg, flying_cow, sheepuff, aerbunny, aerwhale. Moas botam ovos (`moa_egg` com NBT `typeId`:
0 azul, 1 laranja, 2 preta, 3 branca); chocar no Incubator, alimentar com `aechor_petal`,
`nature_staff` faz sentar. As quests de ovos específicos usam `nbt: true`.

## Integrações no pack

* `skyroot_stick` tem oredict `stickWood`.
* Holy Sword é forte contra mortos-vivos (citado no capítulo de Atum como dica).
* Inventário de acessórios próprio do Aether (luvas completam conjuntos de armadura).

## Armadilhas e dicas

* Cair da ilha = cair no Overworld de grande altura; leve paraquedas.
* **Sun Altar exige operador** no multiplayer (config `...operator...=false`).
* `Max Life Shards=10`.
* Anéis (`iron_ring`, `golden_ring`, `zanite_ring`) e o pendente de Zanite **não têm receita**
  nesta versão (só `iron_pendant`, `golden_pendant`, luvas e `white_cape` na bancada); anéis vêm
  do saque das masmorras.
* Ferramentas de Zanite reparadas no Altar voltam a ficar lentas.
* Armadura Phoenix na água vira Obsidian (irreversível).

## Marcos exportados

| Quest | Tipo | O que significa |
| --- | --- | --- |
| `aether:portal_aether` | portal | Primeira entrada na dimensão 4. |
| `aether:primeiros_passos_ceus` | milestone | Skyroot, Holystone e Ambrosium dominados. |
| `aether:oficina_celeste` | milestone | Altar, Freezer e Incubator. |
| `aether:mestre_gravitite` | milestone | Equipamento de Gravitite. |
| `aether:slider` | boss | Masmorra de Bronze. |
| `aether:rainha_valquiria` | boss | **Valquírias libertadas** (marco narrativo do Ato III). |
| `aether:espirito_sol` | boss | Espírito do Sol derrotado. |
| `aether:tres_masmorras` | milestone | As três chaves de masmorra. |
| `aether:chave_ceus` | finale | **Chave dos Céus** (deps: Rainha Valquíria + Espírito do Sol). Usar no Selo dos Céus. |
