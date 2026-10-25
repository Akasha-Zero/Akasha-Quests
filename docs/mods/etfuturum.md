# Et Futurum Requiem — estudo do capítulo `etfuturum`

## Visão geral

* **Mod:** `etfuturum-2.6.59-GTNH.jar`, modid/namespace `etfuturum` (Et Futurum Requiem
  2.6.59-GTNH). Backports de 1.8 a 1.21.
* **Capítulo:** `quests/chapters/base/etfuturum.yaml`, `order: 105`, título "Ecos do Futuro
  (Et Futurum)", 79 quests (14 main, 31 normal, 23 optional, 7 info, 4 milestone).
* **Papel na campanha:** Ato I (pedras, cobre, estações de trabalho) com extensão até o início
  do Ato II (netherita, Fim renovado).

Fontes: registro (`reg.py mod etfuturum`), receitas de bancada/fornalha e NEI (abas "Smoking",
"Blasting", "Banners"), toda a pasta `config/etfuturum/` e bytecode do JAR
(`EtFuturumWorldGenerator`, `SmithingTableRecipes`, `ServerEventHandler`, `Mending`, `CommonProxy`,
`EntityShulker`).

## Energia / recursos-base

Sem energia. Recursos novos: cobre (`etfuturum:copper_ingot`, oredict `ingotCopper`), ametista
(`etfuturum:amethyst_shard`, `gemAmethyst`), netherita (`etfuturum:netherite_scrap/ingot`,
`ingotNetherite`), favo/mel (`etfuturum:honeycomb`, `materialHoneycomb`).

## Progressão

1. Pedras: granito/diorito/andesito (`etfuturum:stone:1/3/5`, polidos 2/4/6), ardósia
   (`cobbled_deepslate`, `deepslate_bricks`), tufo, pedra lisa (`smooth_stone`, fornalha).
2. Cobre: minério (Y 4–80, 8 tentativas/chunk) → cobre bruto → lingote → bloco → cortado →
   oxidação/encerado → lâmpada de cobre (`copper_bulb`), grade e porta.
3. Ametista: geodos (abaixo de Y 46, 1/53 chunks) → fragmentos → vidro escurecido, bloco.
4. Estações: barril (+ barris de metal e malho), defumador, alto-forno, mesa de ferraria,
   composteira, suporte de armaduras, estandartes, lanterna/corrente, observador, alvo, slime,
   concreto, terracota vitrificada, madeira descascada, portas/barcos novos.
5. Natureza: frutas doces (taiga), bambu (selva), cerejeira, lama/tijolos de barro (frasco de água
   em terra), musgo (receita), centáurea/lírio-do-vale, beterraba, abelhas/colmeia/mel, coelhos,
   carneiro, Husk/Stray, monumento oceânico.
6. Nether: detritos antigos (Y 8–22 veios de até 3; Y 8–119 veios de até 2; nunca no ar) →
   sucata (fornalha/alto-forno) → lingote (4 sucatas + 4 ouros) → ferramentas/armadura na Mesa de
   Ferraria (diamante + lingote, mantém encantamentos).
7. Fim: tijolos do Fim, cristal do Fim (respawn do dragão), fruta chorus (ilhas externas) →
   purpur, vara do Fim, sopro do dragão (receita do pack) → poção duradoura.

## Materiais e geração de mundo (config)

* `deepslateGenerationMode=0`, `deepslateMaxY=22`; `enableRawOres=true`;
  `copperClusterSize=8`; `amethystMaxY=46`, `amethystRarity=53`, `buddingAmethystMode=0`
  (ametista germinante não pode ser recolhida nem com Toque Suave); `enableFossils=true`;
  `enableOceanMonuments=true`; `bambooWorldgen=true`; `cherryTreeRarity=72`;
  `debrisMax=3`, `smallDebrisMax=2`, `enableAirDebris=false`.
* `endDimensionProvider=true` (ilhas externas; End Gateways **não** geram);
  `netherDimensionProvider=false` (o Nether é do Netherlicious).

## Máquinas, estruturas e multiblocos

* Defumador e Alto-Forno: metade do tempo; buscam receitas automaticamente
  (`enableAutoAddSmoker/BlastFurnace`).
* Mesa de Ferraria: `SmithingTableRecipes` = 9 peças de diamante → netherita (não há aba NEI).
* Mesas **decorativas** (config: "DOES NOT HAVE ANY FUNCTIONALITY"): Stonecutter, Loom,
  Fletching Table, Cartography Table. Para-raios sem receita.
* Mesa de encantamentos/suporte de poções/bigorna/sinalizador novos (ver `vanilla.md`).

## Dimensões, biomas, estruturas, chefes e mobs

* Mobs: abelhas, raposas, coelhos, coguvaca marrom, Husk, Stray, Endermite, golem de neve
  tosquiável, aldeão zumbi novo (`etfuturum.villager_zombie`).
* Fim: ilhas externas com plantas chorus; respawn do dragão com 4 cristais
  (`enableDragonRespawn`).

## Integrações no pack

* `moddedDeepslateOres`/`moddedRawOres`: minérios de outros mods ganham versão de ardósia e
  minério bruto (estanho, prata, chumbo, níquel, urânio, certus, draconium, Thaumcraft, AM2...).
* `barrelIronChest`/`shulkerBoxesIronChest`: barris de metal e upgrades estilo Iron Chests.
* Remendo funciona em slots do Baubles (`baublesMending`).
* Cobre e netherita usados por Iron Chests (`BlockIronChest:8`), barris, ATM etc.

## Armadilhas e dicas

* **Sem fonte natural neste pack:** Élitros, Casca de Shulker (logo Caixa de Shulker), Totem da
  Imortalidade (receita desligada `enableRecipeForTotem=false`), discos novos (exceto "5", por
  fragmentos). Nenhuma quest os pede.
* **Remendo (Mending)**: a mesa só o oferece ao encantar **livros** (`canApplyAtEnchantingTable`
  só para `minecraft:book`).
* **Sopro do Dragão**: receita shapeless `minecraft:potion:8195` + 2 frutas chorus. A meta 8195
  não aparece no registro do pack (as variantes registradas de resistência ao fogo são
  8227/8259) — **incerto** se a poção fermentada encaixa; a quest manda conferir no NEI e é
  opcional (assim como a de poção duradoura).
* Abelhas atacam quem mexe no ninho (não há fogueira do Et Futurum para acalmá-las).
* Cobre encerado: na bancada, cobre + `materialWax`/`materialWaxcomb`/`materialHoneycomb`.
* Blackstone, basalto e obsidiana chorante existem, mas não confirmei geração natural no
  Nether do Netherlicious — não há quests pedindo esses blocos.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `etfuturum:ecos_cobre_ametista` | milestone | Ardósia, tufo, cobre trabalhado e vidro de ametista. |
| `etfuturum:oficina_futuro` | milestone | Barris, defumador, alto-forno, ferraria, suporte, observador. |
| `etfuturum:forja_netherita` | milestone | Ferramenta e armadura completa de netherita. |
| `etfuturum:ecos_completos` | milestone | **Marco final** do capítulo (inclui abelhas, frutas doces e purpur). |
