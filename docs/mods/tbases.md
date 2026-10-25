# Thaumic Bases — estudo do capítulo `tbases`

## Visão geral

* **Mod:** Thaumic Bases (Thaumic-Based), `Thaumic-Based-1.9.19.jar`, namespace `thaumicbases`.
  Addon do Thaumcraft 4.2.3.5 com aba própria (`THAUMICBASES`, 132 pesquisas; as `TB.*` com nome
  do Thaumcraft são cópias-porta das pesquisas originais).
* **Capítulo:** `quests/chapters/magia/tbases.yaml`, `order: 510`, 55 quests.
* **Papel:** Ato II (alquimia útil, plantas, fogueira) a Ato III/IV (Haste do Vazio, Manipulador
  de Nodo, Revólver). Sementes do Vazio = fonte renovável de Metal do Vazio (Selo Arcano).
* Sem tradução pt_BR no JAR.

Fontes: `reg.py mod thaumicbases`, `reg.py research`, NEI (saídas por handler), lang en_US,
`config/Thaumic Bases.cfg` e as classes descompiladas `tb.init.TBThaumonomicon` e
`tb.init.TBRecipes` (todas as receitas e Warp abaixo vêm de lá).

## Config do pack

`allowTobacco`, `enableBloodyMess`, `enableBracelets`, `enableFlora`, `enableNodeTools`,
`enableRevolver`, `enableTTCompathability` = true; `shardsFromOre = 8`;
`brightFociRequiresPrimordialPearl = true`; Fornalha de Táumio `makeRequireAlumentium = true`,
`speedMultiplierForFurnace = 2`; Incendium dá 5–37 Pós de Blaze. Sem GregTech: vale a receita
de Destruição de Minério com as Pedras Infundidas do Thaumcraft.

## Progressão (ids reais)

* **Crisol útil:** duplicação (flecha/Telum, bola de neve/Gelum, redstone/Machina+Potentia),
  âmbar (Muda de Pinheiro + Vinculum 2), mercúrio (Tora de Silverwood + Ordo + Venenum → 3),
  Salis duplicado (salis + 2 de cada primal + Praecantatio 3 → 2), restauração (cascalho →
  pedregulho, gelo → gelo compactado...), entropia avançada (lã → 3/4 linhas, arenito → 3/4
  areias, vara de blaze → 3/4 pós), backprocessing, `crystalBlock:0-7`, Destruição de Minério
  (Pedra Infundida + Perditio 2 + Praecantatio 1 + primal 3 → 8 estilhaços), `pyroBucket`
  (lava + Ignis 24 + Praecantatio 8), `blockSalisMundus` (9 salis, Arcana).
* **Thauminita:** Barra de Táumio + Metallum 4 + Vitreus 8 + Praecantatio 2 → 8 pepitas
  (`resource:0`); 9 pepitas = `resource:1`. Ferramentas (15/primal) e armadura (25/primal),
  `resource:2` ponteiras, `resource:3` Haste de Táumio (80 vis), `resource:4` Haste do Vazio
  (infusão, inst. 6; acumula vis sozinha).
* **Plantas:** `sweedSeeds`, `plaxSeed` (3 linhas), `briar` → `rosehipSyrup`, `metalleatSeeds`
  (2 pepitas de ferro), `lucriteSeeds` (3 pepitas de ouro; precisa de magia sob o solo),
  `lazulliaSeeds`, `redlonSeeds` (bloco de redstone), `rainbowCactus`, `aurelia` (pétalas na lua
  cheia), `knoseSeed` (Rosa Mysteria sobre Bloco de Cristal → `knoseFragment`), `ashroom`,
  `flaxium`, `glieoniaSeed` (2 pó de glowstone), árvores por infusão (`goldenOakSapling:0-3`) e
  `voidSeed` (infusão da Semente do Vazio do TC + 6 cultivos; a planta dá a Semente do Vazio
  do TC `Thaumcraft:ItemResource:17`).
* **Tabaco:** `tobaccoSeeds`, `mortar`, `tobaccoPowder:0-8`, `greatwoodPipe`, `silverwoodPipe`.
* **Dispositivos:** `campfire`, `brazier` (Ignis dos jarros), `cryingObsidian` (obelisco de
  renascimento), `oldBrick/oldLapis/...` (Arcana com Cristal de Entropia), `advAlchFurnace`,
  `thaumicAnvil` (nível 60, sem penalidade), `voidAnvil` (auto-reparo, 7 níveis fixos),
  `relocator`/`:6`, `spike`/`:2`/`:4`, robes ensanguentados, `overchanter` (+1 nível acima do
  máximo; 32 Praecantatio + 30 níveis), `entityDeconstructor`, focos `fociDrain/Experience/
  Flux/Activation`, braceletes `castingBracelet:0-12`.
* **Nodos e armas:** `nodeManipulator` (em cima de nodo comum, com foco embaixo),
  `nodeFoci:0-9`, `nodeLinker`, `revolver` (Telum interno, 1 ponto = 2 tiros) + melhorias
  `RevolverInfusionRecipe`.

## Materiais e geração de mundo

O mod não gera minérios. Plantas e árvores são feitas no Crisol/Infusão a partir de plantas
vanilla/Thaumcraft.

## Integrações no pack

* Thaumic Revelations: integração com Thaumic Bases ativa (tingimento de armaduras).
* `enableTTCompathability = true` (Thaumic Tinkerer).
* Overchanter aceita XP de jarros do Automagy/obelisco do EnderIO (o pack tem EnderIO).

## Armadilhas e dicas

* **Warp das pesquisas:** Aurelia 2, Fluxium 2, Lucrite 1, Crying Obsidian 1, Entity
  Deconstructor 2, Void Anvil 2, Tainted (encant.) 3, Bloody Robes 2, Bracelet Bone 1/Void 2/
  Primal 4, Node Manipulator 1, Node Foci Destruction 2/Taint 3, Revolver Eldritch 2/Primal 4/
  Tainted 3/Void 2, Tabaco Eldritch 2/Tainted 3. Itens com Warp: tabaco eldritch, Bracelete
  Primal, focos de destruição/taint.
* Sweed se espalha; Peaceful Tree atrai animais sem fim; Nether Tree converte blocos em volta.
* Incendium queima como lava. Ligador de Nodos pode explodir o nodo transmissor.
* Golden Oak (alteração GTNH): tesoura não colhe as folhas.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `tbases:haste_vazio` | milestone | Haste do Vazio (núcleo de varinha que acumula vis). |
| `tbases:semente_vazio` | milestone | Fazenda de Sementes do Vazio (Metal do Vazio renovável). |
| `tbases:sobreencantamento` | milestone | Mesa de Sobre-Encantamento. |
| `tbases:manipulador_nodo` | milestone | Manipulação do tipo de nodos. |
| `tbases:revolver` | milestone | Revólver do Taumaturgo. |
| `tbases:mestre_bases` | milestone | **Marco final** do capítulo. |
