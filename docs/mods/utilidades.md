# Utilidades da Base — estudo do capítulo `utilidades`

## Visão geral

* **Capítulo:** `quests/chapters/base/utilidades.yaml`, `order: 110`, título "Utilidades da
  Base", 103 quests (14 main, 37 normal, 34 optional, 15 info, 3 milestone) + 1 `link` para
  `prologo:tomo_akashico`.
* **Papel na campanha:** Ato I (conforto da base). Não tranca nada; o marco final
  `base_organizada` é o "Volume de Utilidades completo".
* **Mods cobertos (versão / namespace):**
  * Iron Chests 6.1.13 (`IronChest`)
  * Storage Drawers 2.2.31-GTNH (`StorageDrawers`) + pacote Forestry (`StorageDrawersForestry`)
  * EnderStorage 1.8.7 (`EnderStorage`)
  * Waystones-X 1.0.15 (`waystones`)
  * Adventure Backpack 1.4.26-GTNH (`adventurebackpack`)
  * Better Furnaces 4.3 (`FLabsBF`)
  * AkashicFM bffcade (`akashicfm`), Akashic Index (`akashicindex`), AkashicWeb (`akashicweb`)
  * OmniPipes 3.1 (`tuborapido`, JAR `OminiPipes.jar`, modid interno `omnipipes`)
  * Informativos: Morpheus, WAILA (+Harvestability, plugins), JourneyMap, Bogo Sorter,
    Inventory Tweaks, Backhand, Cosmetic Armor, Akashic Voice, Akashic Tome (Prólogo).
  * IronDome: só Discord Rich Presence (cliente) — sem conteúdo de jogo, sem quest.

Fontes: registro, receitas do dump e NEI, configs (`IronChest.cfg`, `StorageDrawers.cfg`,
`EnderStorage.cfg`, `waystones.cfg`, `adventurebackpack.cfg`, `akashicfm.cfg`, `Morpheus.cfg`,
`tuborapido.cfg`, `bogosorter/keybinds.json`), lang e bytecode dos JARs (AkashicFM, Akashic Index,
AkashicWeb, OmniPipes, Adventure Backpack `BackpackTypes`/`BackpackAbilities`/`ModWorldGen`/
`ServerActions`, Better Furnaces `ItemOreUpgrade`).

## Energia / recursos-base

* Transmissor de FM (AkashicFM): `requireEnergy=true`, **8 EU/t** (ou RF, 4 RF = 1 EU),
  8000 EU armazenados, entrada até 128 EU/t sem explodir.
* OmniPipes transporta itens, fluidos, gases (Mekanism), RF e EU (passes configurados em
  `tuborapido.cfg`; fluido praticamente instantâneo).
* Mochila-Cóptero: combustíveis líquidos (lista em `adventurebackpack.cfg`); Jetpack a carvão:
  combustível sólido + água → vapor.

## Progressão (ids principais)

* **Iron Chests:** `IronChest:BlockIronChest` (ferro 54) → `:1` ouro (81) → `:2` diamante (108)
  → `:5` cristal / `:6` obsidiana → `:8` netherita; `:3` cobre (45), `:4` aço (72; nome pt_BR
  "Baú de Prata" mas receita de `ingotSteel`), `:7` terra; upgrades `woodIronUpgrade` etc.
* **Storage Drawers:** gaveta (`ore:drawerBasic`, 32 pilhas na 1x1), 2x2/compactas,
  `compDrawers`, `controller` (alcance 12, até 2048 gavetas), `controllerSlave`, `trim`,
  `framingTable`, `upgradeTemplate`, `upgrade:2..6` (multiplicadores x2, x3, x5, x8, x13; VII e
  VIII sem receita), status, void, redstone, chave/ocultação/quantidade/pessoal, fita.
* **EnderStorage:** `enderChest` (27 slots, 3 cores de lã, diamante = pessoal), `enderChest:4096`
  (tanque, 256 baldes), `enderPouch`.
* **Waystones:** `warpStone` (recarga 300 s), variantes de bloco, custos de XP (base 5, 1 nível
  por 100 blocos, 5 entre dimensões), `warpScroll`.
* **Adventure Backpack:** mochila (meta = tipo), componentes (`backpackComponent:1..7`),
  mangueira, fogueira, machete, besta, traje, botas de pistão (passo automático, salto 3),
  jetpack a carvão, motor + hélices → cóptero, bote inflável, suco de melancia; 20 quests de
  mochilas especiais/estilo.
* **Better Furnaces:** `flabs.bf:0..4` (ferro, ouro, diamante, inferno, extrema) e upgrades
  (minério ×2 — `ItemOreUpgrade.getSmeltingResult` dobra —, combustível, líquido, I/O, fábrica,
  pacote, cor, oredict, tubulação).
* **AkashicFM:** rádio (URL/FM/iPod), caixa de som (≤32 blocos da rádio, ≤8 por rádio),
  sintonizador (canais mono/esq./dir./estéreo), alto-falante de teto, transmissor + antenas
  (64 + 32/antena, máx. 512), rádio portátil (16 blocos sem fone) + fone, iPod/iPod Player.
* **Akashic Index:** `akashicindex:speaker` = componente OpenComputers (API de alto-falante estilo
  CC:Tweaked) que monta uma ROM "Akashic Index ROM" com `akashic-music install` / `music`
  (precisa de Placa de Internet). Comando de admin `/akashicindex musictablet`.
* **AkashicWeb:** `akashicweb:screen` — retângulo 2x2+ vira navegador (uma pessoa controla).
* **OmniPipes:** `tuborapido:blocoTuboUniversal` (×8, pede Estrela do Nether) e
  `tuborapido:omniCore`.

## Mochilas especiais (Adventure Backpack, conferido no bytecode)

| Mochila (meta) | Efeito |
| --- | --- |
| Morcego (2) | Visão noturna. Só em baú de masmorra e mina. |
| Lula (62) | Respiração aquática na água (+ visão noturna). |
| Pigman (51) | Resistência ao fogo. Porco (53) atingido por raio vira Pigman. |
| Dragão (22) | Força + regeneração (+ visão noturna). Ovo do dragão não é consumido. |
| Arco-Íris (57) | Velocidade + salto; maçã dourada ativa "NYANCAT". |
| Cacto (17) / Melancia (42) | Enchem os tanques na chuva (água / suco de melancia). |
| Vaca (1) / Coguvaca (45) | Consomem trigo guardado e produzem leite / sopa. |
| Galinha (28) | Bota ovos. |
| Girassol (63) | Alimenta de dia sob o céu. |
| Jaguatirica (29) | Creepers fogem. |
| Lobo (73) | Acalma lobos. |
| Slime (67) | Velocidade + sons. |
| Creeper (64) | Chiado de creeper ao agachar perto de jogadores. |
| Esqueleto (65) | O arco usa flechas guardadas na mochila. |
| Golem de Ferro (11) | Estética; só no baú do ferreiro da vila. |
| Elétrica (24) | Diamante (7) atingido por raio. |

## Integrações no pack

* Waystones → marcadores automáticos no JourneyMap; Waystones em estruturas (ver `vanilla.md`).
* Baús/barris de netherita usam `ingotNetherite` do Et Futurum (dependência cruzada
  `etfuturum:lingote_netherita` na quest `bau_netherita`).
* Ender Chest vanilla não pode ser colocado (`disable-vanilla=true`).

## Armadilhas e dicas

* **StorageDrawersForestry**: nenhuma receita de bancada no dump (nem no NEI) — incerto se as
  gavetas de madeira do Forestry são fabricáveis; a quest é só `info`.
* **iPod** (AkashicFM): o config local tem `ipod.enabled=false`; o do servidor pode diferir — a
  quest é opcional e avisa.
* Bat/Golem de Ferro/Pigman/Elétrica não têm receita (loot / transformação por raio); Deluxe não
  tem fonte — sem quest.
* OmniPipes: interações conferidas no bytecode de `BlocoTuboUniversal.onBlockActivated`
  (corante/lã = canal, redstone/pó de pedra luminosa = prioridade, papel = retenção, bloco de
  esmeralda = modo "Overall", chave = energia, agachado = GUI de filtro/túnel, mão vazia = modo).
* Morpheus: 50% dos jogadores da dimensão, `IncludeMiners=true`.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `utilidades:parede_gavetas` | milestone | Rede de gavetas com controlador e aprimoramentos. |
| `utilidades:mestre_mochileiro` | milestone | Cóptero, jetpack, botas e mochilas com poderes. |
| `utilidades:base_organizada` | milestone | **Marco final** — base organizada e conectada. |
