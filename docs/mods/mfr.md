# MineFactory Reloaded — estudo do capítulo `mfr`

## Visão geral

* **Mod:** MineFactory Reloaded `1.7.10R2.8.1` (`MineFactoryReloaded-[1.7.10]2.8.1-174.jar`),
  namespace de registro `MineFactoryReloaded`, dependência CoFHCore 3.1.4.
* **Capítulo:** `quests/chapters/tech/mfr.yaml`, `order: 350`, 129 quests.
* **Papel na campanha:** Ato II (automação agrícola, animais, mobs, fluidos) com o final no
  Ato III/IV (Broca Laser, DSU, Carregador de Chunks). O marco final alimenta o **Selo da
  Engrenagem**.

Fontes: registro do pack (`reg.py mod MineFactoryReloaded`), dump de receitas de bancada e
fornalha, catálogo NEI (`Broca Laser`, `Caldeira de Lodo`, `Bio-Reactor`, `Empacotador de
Carne`), lang do JAR (`assets/minefactoryreloaded/language/en_US.lang` — o JAR **não tem
pt_BR**; os nomes pt_BR vêm do registro), `config/powercrystals/minefactoryreloaded/common.cfg`
e bytecode (`javap`) de `BlockRubberWood`, `ItemUpgrade`, `BlockConveyor`, `TileEntitySewer`,
`TileEntitySlaughterhouse`, `BlockPinkSlimeFluid`, `TileEntityGrinder`, `Vanilla` (drops da
Caldeira de Lodo).

## Energia / recursos-base

* **RF** (tooltip dos geradores: "Produces RF only"). Custos em `common.cfg` em "DaRF"
  (10 RF). Ex.: Plantador/Colhedor 24 DaRF por ciclo, Fertilizador 96, Acasalador 64,
  Auto-Invocador 60 (×1 comum, ×5 exato).
* Geradores próprios: **Gerador de Bio-Combustível** (`machine.1:11`) e **Turbina a Vapor**
  (`machine.2:9`, vapor da **Caldeira de Vapor** `machine.1:6`).
* **Fluidos:** `sludge` (Colhedor), `sewage` e `mobessence` (Esgoto; o Esgoto também converte
  orbes de XP em Essência), `mobessence` (Triturador), `milk`/`mushroomsoup` (Rancheiro),
  `meat` e `pinkslime` (Matadouro), `biofuel` (Bio-Reator), `steam`, `chocolatemilk`.

## Progressão (ids reais)

1. **Borracha:** troncos naturais `rubberwood.log` (meta 1 no mundo; dropam meta 0 +
   `rubber.raw`, extra com Fortuna — `BlockRubberWood.getDrops`) → fornalha → `rubber.bar`
   (oredict `itemRubber`) → fornalha → `plastic.raw` (também de borracha do IC2 e de itens de
   plástico reciclados) → 4 → `plastic.sheet` ×4 (oredict `sheetPlastic`).
2. **Receitas no modo Thermal Expansion** (`RecipeSets: ThermalExpansion=true`, Vanilla e
   EnderIO desligados): quase toda máquina = plástico + `ThermalExpansion:Frame` (Estrutura
   Básica) + `ThermalExpansion:material:1` (Bobina Receptora) + engrenagens TE
   (`thermalexpansion:machine<Metal>`) e às vezes ferramentas de Invar.
3. **Fazenda:** Plantador `machine.0`, Colhedor `machine.0:2`, Fertilizador `machine.0:4`
   (**só fertilizante MFR**: `EnableBonemeal=false`), Coletor de Itens `machine.0:6` (sem
   energia), Fertilizante Industrial `fertilizer` (×24), Esgoto `machine.0:10` (×4),
   Composteira `machine.0:11`, Caldeira de Lodo `machine.0:9`.
4. **Energia:** Bio-Reator `machine.1:10` (mais variedade de plantas = mais eficiência),
   Gerador de Bio-Combustível, Caldeira de Vapor (precisa de Caldeira de Lodo + 2 Tanques
   Portáteis Reforçados TE), Turbina a Vapor (Dínamo a Vapor TE).
5. **Animais:** Rancheiro `machine.0:3`, Acasalador `machine.0:12` (desliga com >50 entidades),
   Cronotipador `machine.0:15`, seringas `syringe.*`, Estação Veterinária `machine.0:5`.
6. **Mobs:** Rede de Safari `safarinet.singleuse` → `safarinet.reusable` (4 pérolas + lágrima de
   Ghast), Carcereiro/Dourada, Lançador; Triturador `machine.0:13` (ignora filhotes) →
   Essência; Auto-Invocador `machine.1:9` (blacklist: VillagerGolem); Matadouro `machine.1:13`
   (carne + gosma rosa 1/8); Gosma Rosa no mundo gera `mfrEntityPinkSlime` → `pinkslime` →
   bloco → fornalha → `pinkslime:1` (Cristal).
7. **Encantamento:** Auto-Encantador `machine.0:14` (nível-alvo), Auto-Desencantador
   `machine.1:12` (sem Essência: `EnableEssence=false`), Auto-Bigorna `machine.2:2`, Esmagador
   de Blocos `machine.2:3` (Fortuna paga com Essência), Roteador de Encantamentos.
8. **Logística:** Esteira `conveyor:16` (×16; glowstone = rápida, redstone para), Ejetor,
   Roteadores de Itens/Líquidos, Unificador, LiquiFabricador, Tubos/Tanques de Plástico,
   trilhos de carga/passageiros, Estrada (acelera).
9. **RedNet:** `cable.redstone` (16 cores), `cable.redstone:2` (com RF), Medidor/Multimedidor,
   Encaixe `machineblock:1` → CRP `rednet.logic`, cartões LX-100/300/500 `upgrade.logic:0-2`,
   Cartão de Memória, Historiador, RedNote.
10. **Armas:** carregador `needlegun.ammo.empty` → munições → Flechete `needlegun` (Luneta +
    Lançador de Rede) → foguetes `rocket:1`/`rocket` → Lança-SPAM `rocketlauncher`
    (LX-300 + LX-500 + Flechete). `SPAMR.Exploding=true`.
11. **Endgame:** Focos de Laser `laserfocus:0-15`, Broca Laser `machine.2`, Precarregador
    `machine.2:1` (Cristal de Gosma Rosa), DSU `machine.1:3` (4 Caixas Fortes Reforçadas TE +
    Estrutura da Célula Ressonante vazia), Carregador de Chunks `machine.2:10` (Célula
    Ressonante + 2 Tesseratos).

## Materiais e geração de mundo

* Árvores de borracha ativas (`RubberTrees.Enable=true`, retrogen ligado). Muda Sagrada
  encantada no saque das bibliotecas de Fortaleza (`SacredRubberSapling=true`).
* Lagos de **Lodo** e **Esgoto** ativos (`Rarity=32`), fora de dimensões sem respawn.
* Sem minérios próprios.

## Máquinas, estruturas e multiblocos

* **Melhorias de raio** (`upgrade.radius:N`): raio += N+1 (Lápis +1 … Esmeralda +11);
  Pedregulho (`:11`) = −1 (bytecode `ItemUpgrade.getAugmentLevel`). Área padrão 3×3.
* **Caldeira de Lodo** (pesos em `Vanilla.class`): areia 50, argila 30, terra 30, terra grossa
  10, cascalho 10, areia vermelha 5, areia de almas 5, micélio 3, podzol 2, netherrack 1;
  Forestry adiciona composto/turfa/cinzas.
* **Broca Laser:** sobre um poço livre até a rocha-mãe ou o vazio; Precarregadores a um bloco
  de distância (tooltips). Focos por cor (NEI "Broca Laser"): Preto carvão, Marrom ferro,
  Amarelo ouro/glowstone/Yellorite, Vermelho redstone, Azul lápis, Azul Claro diamante/ósmio,
  Lima esmeralda/urânio IC2, Laranja cobre TF, Branco quartzo do Nether, Ciano quartzo
  certus, etc. `LaserDrill.Work=300`.
* **Tanques de Plástico** formam rede (`TankNetwork`).

## Integrações no pack

* Depende de peças do **Thermal Expansion/Foundation** em quase todas as receitas.
* `itemRubber` (Barra de Borracha) vale no IC2; borracha do IC2 derrete em Plástico Cru.
* Bio-Reator aceita plantas de outros mods (148 entradas no NEI).
* Broca Laser rende minérios de vários mods (TF, Mekanism, IC2, IE, Thaumcraft, Forestry,
  ReactorCraft, Industrial Upgrade, Galacticraft...).

## Armadilhas e dicas

* Bio-Combustível explode no Nether (`Biofuel.Exploding=true`).
* Foguetes do SPAMR explodem quando o combustível acaba.
* Carregador de Chunks: custo de energia exponencial com a área; respeita o limite do
  `forgeChunkLoading.cfg` (`IgnoreChunkLimit=false`).
* Fertilizador não usa farinha de osso neste pack.
* Esteiras capturam entidades (`CaptureNonItems=true`), inclusive jogadores.
* O nome "Triturador" no pt_BR do MFR é o Grinder (mobs), não o macerador do IC2.
* O CRP aparece no registro com o mesmo id do "PRC Owner's Manual" (livro com NBT); a tarefa
  usa `rednet.logic` sem NBT.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `mfr:fazenda_automatica` | milestone | Plantador + Colhedor + Fertilizador + Coletor (Ato II). |
| `mfr:energia_fabrica` | milestone | Gerador de Bio-Combustível ou Turbina a Vapor. |
| `mfr:fazenda_mobs` | milestone | Triturador + Auto-Invocador + Essência. |
| `mfr:mestre_rednet` | milestone | CRP programável com expansão. |
| `mfr:arsenal_fabrica` | milestone | Flechete + Lança-SPAM. |
| `mfr:perfuracao_laser` | milestone | Broca Laser com Precarregadores (Ato III/IV). |
| `mfr:mestre_fabrica` | milestone | **Marco final** (Broca Laser + DSU + Auto-Invocador + CRP) — para o **Selo da Engrenagem**. |
