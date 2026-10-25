# Big Reactors — estudo do capítulo `bigreactors`

## Visão geral

* **Mod:** `BigReactors-0.5.4A.jar` (namespace `BigReactors`). Config:
  `config/BigReactors/BigReactors.cfg`.
* **Capítulo:** `quests/chapters/tech/bigreactors.yaml`, `order: 330`, 54 quests + 1 link
  (`ic2:centrifuga_termica`).
* **Papel na campanha:** Ato II/III (reator passivo, ativo e turbina) → Ato IV (Ludicrite, reatores
  grandes). Marco final exportado para o **Selo da Engrenagem**.
* **Não existe Reprocessor** nesta versão (é do Extreme Reactors). O registro tem: minério,
  blocos de Yellorium/Cyanite/Grafite/Blutonium/Ludicrite, lingotes e pós dos cinco materiais,
  Haste de Combustível, peças de reator (Alojamento, Controlador, Haste de Controle, Porta de
  Energia, Porta de Acesso, Porta de Refrigeração, Porta RedNet, Entrada de Computador, Porta de
  Redstone, Vidro), peças de turbina (Alojamento, Controlador, Porta de Energia, Porta de Fluidos,
  Rolamento, Entrada de Computador, Haste e Lâmina do Rotor, Vidro), fluidos Yellorium/Cyanite,
  peças criativas e a ferramenta de debug.

## Config relevante

* Worldgen: `MaxYelloriteClustersPerChunk=5`, `MaxYelloriteOrePerCluster=10`, `YelloriteMaxY=50`,
  sem lista branca/negra, `enableWorldGenInNegativeDims=false`.
* `registerYelloriteSmeltToUranium=true` e `registerYelloriumAsUranium=true` → o minério na
  fornalha vira o **primeiro `ingotUranium` do oredict, o "Uranium Ingot" do Amun-Ra**
  (`GalacticraftAmunRa:item.baseItem:11`), não Yellorium (confirmado no NEI). O Yellorium de verdade
  vem do pó (SAG, Câmara de Enriquecimento, Pulverizador, Triturador, Rebolo) ou da Fundição
  Induzida. Quest `info_fornalha_yellorite` explica.
* `enableCyaniteFromYelloriumRecipe=true`: `ingotUranium` + areia → Cyanite (bancada).
* `registerCoalForSmelting/CharcoalForSmelting=true`: carvão/carvão vegetal → Grafite.
* `powerProductionMultiplier=4.0`, `reactorPowerProductionMultiplier=5.0`,
  `turbinePowerProductionMultiplier=5.0`, `turbineFluidPerBladeMultiplier=2.0`.
* Tamanhos máximos: reator 64 × 192 (altura); turbina 64 × 128.
* `enableComedy=true` (carne, slime rosa e esgoto como moderadores).
* `autoAddUranium=true`: lingotes `ingotUranium` sem registro viram clones do combustível.

## Progressão (ids reais)

1. `BigReactors:YelloriteOre` → pó `BRIngot:4` → Yellorium `BRIngot:0`; Grafite `BRIngot:2`.
2. Reator: Alojamento `BRReactorPart` (4 ferros, 4 grafites, 1 urânio → 4), Vidro
   `BRMultiblockGlass`, Haste de Combustível `YelloriumFuelRod`, Haste de Controle `BRReactorPart:2`,
   Controlador `:1`, Porta de Energia `:3`, Porta de Acesso `:4`, Refrigeração `:5`, RedNet `:6`,
   Computador `:7`, Porta de Redstone `BRReactorRedstonePort`.
3. Resíduo Cyanite `BRIngot:1` → **Blutonium `BRIngot:3` no Centrifugador Térmico do IC2**
   (receita registrada pelo Big Reactors: 1 Cyanite → 1 Blutonium, `minHeat` 4000 no bytecode;
   confirmada no NEI). Blutonium é combustível (`ReactorConversions` blutonium → cyanite) e é
   `ingotPlutonium`.
4. Ludicrite `BRMetalBlock:4`: 4 `ingotPlutonium` + 2 pérolas + 2 blocos de esmeralda + estrela do
   Nether, **ou** 4 `ingotPlutonium` + 2 varas de blaze + 2 blocos de Enderium.
5. Turbina: Alojamento `BRTurbinePart` (ferro, grafite, quartzo, Cyanite → 4), Controlador `:1`
   (**2 Blutonium** + diamante), Porta de Energia `:2`, Porta de Fluidos `:3`, Rolamento `:4`
   (4 alojamentos, 2 hastes, 3 diamantes), Haste `BRTurbineRotorPart`, Lâmina `:1`.

## Moderadores e bobinas (bytecode `registerGameBalanceData`)

* Interior do reator: blocos de ferro, ouro, diamante, esmeralda, grafite, vidro, gelo, neve,
  cobre, ósmio, latão, bronze, zinco, alumínio, aço, invar, prata, chumbo, eletro, eletro
  fluxado, platina, "shiny", titânio, enderium, metais do Metallurgy; fluidos água, redstone,
  glowstone, cryotheum, ender, pyrotheum, essência de vida; e (comédia) carne, slime rosa, esgoto.
* Bobinas da turbina: ferro, ouro, cobre, ósmio, zinco, chumbo, latão, bronze, alumínio, aço,
  invar, prata, eletro, eletro fluxado, platina, shiny, titânio, enderium, **ludicrite** (melhor) e
  metais do Metallurgy. As quests citam a ordem geral sem números.

## Multiblocos (descrição geral do Big Reactors 0.4/0.5)

* Reator: caixa retangular ≥ 3×3×3; arestas só Alojamento; faces com vidro/peças; colunas de
  hastes de combustível do chão ao teto com Haste de Controle no topo; Controlador único.
* Turbina: caixa de Alojamentos; Rolamento no centro de uma face; haste reta; lâminas numa parte,
  bobina de blocos de metal em volta da haste na outra; Portas de Fluidos (vapor entra, água sai);
  RPM ideal ~900/1800.

## Integrações no pack

* Yellorite moído em SAG (Ender IO), Câmara de Enriquecimento (Mekanism), Rebolo (AE2),
  Triturador (IE), Pulverizador e Fundição Induzida (Thermal).
* Blutonium depende do **Centrifugador Térmico do IC2**.
* Energia RF sai para condutos do Ender IO / cabos do Mekanism; Entrada de Computador para
  OpenComputers; Porta RedNet para MFR.

## Armadilhas e dicas

* Não funda o minério direto na fornalha (sai urânio do Amun-Ra).
* Buffer cheio no reator passivo desperdiça combustível — ligue a um armazenamento grande.
* Controlador da Turbina exige Blutonium → planeje o IC2 (Centrifugador Térmico aquecido).

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `bigreactors:reator_passivo` | milestone | Primeiro reator passivo (Ato II). |
| `bigreactors:reator_ativo` | milestone | Reator com Portas de Refrigeração (vapor). |
| `bigreactors:turbina` | milestone | Turbina a vapor funcionando (Ato III). |
| `bigreactors:mestre_reatores` | milestone | **Marco final** — usado pelo **Selo da Engrenagem**. |
