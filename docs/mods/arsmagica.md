# Ars Magica 2.5 LTS — estudo do capítulo `arsmagica`

## Visão geral

* **Mod:** `AM2.5-LTS-1.6.11-LE.jar` (modid/namespace `arsmagica2`; o `mcmod.info` e o
  `reg.py mods` dizem `1.6.7`, mas o arquivo é o AM2.5 LTS 1.6.11 "LE"). Depende de AnimationAPI.
* **Capítulo:** `quests/chapters/magia/arsmagica.yaml`, `order: 560`, 166 quests.
* **Papel na campanha:** magia "interna" do Ato II (mana, nível de mago, feitiços montados à
  mão). Os Guardiões fortes (Natureza, Fogo, Inverno, Ender) e as Vestes do Arquimago são
  Ato III–IV e alimentam o **Selo Arcano**.
* **Diferenças do AM2 clássico (confirmadas no JAR):** Vestes do Arquimago, Armadura Vinculada,
  Cajados de Feitiço (Novato/Viajante/Mestre), Varas de Pesca Arcana/Infernal, substâncias
  "psicodélicas" (Casparazoid/Astrocalis), Alucinações e o "reino fractal" (Essência do Pesadelo,
  Fragmento Fractal, Fragmento de Alma, Soulspike, Caixa de Ilusões), Replicador de Feitiços,
  Enervador Entrópico, Desconstrutor Arcano, Aura do Outro Mundo, Vórtice Espacial, instabilidade
  no Altar de Criação.
* Não há `pt_BR.lang` no JAR: os itens aparecem em inglês no jogo (o capítulo usa os nomes em
  inglês entre parênteses quando ajuda).

Fontes: registro (`reg.py mod arsmagica2`, receitas de bancada), catálogo NEI (`Essence Refiner`
31 receitas, `Entropic Enervator` 19), o **Arcane Compendium embutido**
(`assets/arsmagica2/docs/ArcaneCompendium_en_US.xml`, todas as entradas), `config/AM2/AM2.cfg`,
e bytecode (`javap`) de `BossSpawnHelper`, `Entity*Guardian.func_70628_a` (drops),
`ItemFrameWatcher`, `TileEntityKeystoneRecepticle`/`BlockKeystoneReceptacle`, `EntityHellCow`,
`RitualShapeHelper` e dos componentes `Light`, `FireDamage`, `LifeTap`, `Rift`, `ManaLink`,
`Transplace`.

## Energia / recursos-base

* **Mana** (barra azul), **Esgotamento/burnout** (barra vermelha), **Nível de mago** (sobe com
  lançamentos válidos; retorno decrescente se repetir). `/amuicfg` configura o HUD.
* **Pontos de habilidade** (Óculo): azuis nos níveis pares 2–20, verdes 22–40, vermelhos 42–50;
  começa com 3 azuis. Extras: **Orbes do Infinito** `arsmagica2:rune:17/18/19` (azul/verde/
  vermelho). Árvores: Ofensa, Defesa, Utilidade, Talentos. `SecondarySkillTreeTierCap=99`
  (padrão) — a "árvore primária" não limita na prática.
* **Etherium** (energia de máquinas): Neutro (Obelisco, queima Pó de Vinteum ou Etherium
  Líquido), Luminoso (Prisma Celestial, luz do sol; tampas de Pedra da Lua → carga noturna),
  Sombrio (Aurem Negra, mata mobs sem drop/XP). Alcance 10 blocos; Conduítes repetem (10 blocos,
  cadeia até 100). Ligação com a Chave de Cristal (fonte → destino; agachado fixa a fonte).
* **Akashic ATM Family 0.1.0-alpha.10** adiciona `allthemodiumlegacy:allthemodium_source_jar`
  ("Reservatório de Etherium de Unobtainium"): 8 lingotes de Unobtainium + Conduíte de Etherium,
  100 000 Etherium de um tipo por vez, conecta à rede nativa do AM2.5 (guia do mod e lang).

## Progressão (ids reais)

1. Vinteum (`vinteumOre` → fornalha → `itemOre:0`, oredict `dustVinteum`).
2. **Arcane Compendium** `arsmagica2:ArcaneCompendium` — **sem receita**: livro comum numa moldura
   de item a ≤2 blocos de Etherium Líquido por 800 ticks (`ItemFrameWatcher`, `AlternativeStart=false`).
3. Óculo `occulus` (tijolo de pedra, vidro, carvão, Topázio Azul) → Pergaminho → Mesa de
   Inscrição `InscriptionTable` (+ Livro e Pena) → Atril `blockLectern` + Altar `CraftingAltar`
   (multibloco; poder por material e 5 tampas, máx. 13 efeitos) → feitiço `arsmagica2:spellBase`.
4. Obelisco `obelisk` → Chave de Cristal `crystal_wrench` → Composto Arcano `itemOre:1` → Cinza
   Arcana `itemOre:2` (fornalha) → Refinador `essenceRefiner` → essências `essence:0..12`.
5. Focos (`lesserFocus` → `standardFocus` → `greaterFocus`, `manaFocus`, `chargeFocus`,
   `mobFocus`, `itemFocus`, `creatureFocus`, `playerFocus`).
6. Rituais: **Purificação** (Luz; reagentes Pedra da Lua + Foco de Mana) → `celestialPrism`;
   **Corrupção** (Dano de Fogo; Foco de Monstro + Pedra do Sol) → `blackAurem`.
7. Inlays (`redstoneInlay`/`ironInlay`/`goldInlay`) → Guardiões → Mago (`mage*`) → Mago de
   Batalha (`battlemage*`, pedem Essências Água/Terra/Fogo/Ar e Inlay de Ouro) → Arquimago
   (`Archmage*`, pedem Antena do Fogo, Armadura da Terra + Proteção da Vida, Orbes da Água, Botas
   do Ender + Trenó Aéreo, Pedras do Sol/Lua, Estrelas do Nether, blocos de diamante, Essência Pura).

Refinador (NEI, Cinza Arcana sempre no centro): Arcana = 5 Cinzas; Terra = terra, pedra×2,
obsidiana; Ar = penas×2 + Raiz de Tarma×2; Fogo = carvão×2 + pó de blaze×2; Água = Wakebloom×2 +
balde/frasco de água×2; Gelo = neve×2 + gelo×2; Raio = redstone×2 + glowstone×2; Natureza = folhas,
vitória-régia, cacto, cipó; Vida = ovo×2 + maçã dourada×2; Ender = pérola×2 + olho×2; Núcleo Básico
= Ar/Água/Fogo/Terra; Núcleo Superior = Raio/Gelo/Natureza/Arcana; Pura = Núcleos + Vida + Ender +
diamante (no centro).

## Materiais e geração de mundo (`config/AM2/AM2.cfg`)

* Vinteum Y 10–45 (veio 4, freq. 6); Quimerita Y 10–80 (6/8); Topázio Azul Y 10–80 (6/8); Pedra do
  Sol Y 5–120 (veio 3, freq. 20, junto a lava — Nether principalmente). Et Futurum adiciona as
  versões em ardósia (`etfuturum:deepslate_am2_ore`).
* **Pedra da Lua** só em meteoros noturnos (`MoonstoneFrequency=48000`, lua cheia aumenta;
  `Meteor_Spawn_Min_Level=10` → só para jogadores de nível de mago ≥ 10; destrói terreno).
* Lagos de Etherium (`EssenceLakeFrequency=25`), Florestas de Witchwood ativas (bioma 100,
  raridade 6). Plantas: Cerublossom, Desert Nova, Raiz de Tarma (cavernas), Wakebloom (água
  quente), Aum (pé das Witchwood).
* Mundo de retroativo desligado; `worldgen_blacklist=-27,-28,-29`.

## Máquinas, estruturas e multiblocos

* **Altar de Criação** (ver Compêndio → Estruturas). Instabilidade: mais componentes/modificadores,
  pessoas por perto, Etherium Sombrio e tempestades pioram; giz + Etherium Líquido em volta, Prisma
  por perto, noite e céu aberto melhoram.
* **Obelisco/Prisma/Aurem**: upgrades opcionais de círculo de giz e pilares (com tampas).
* **Portal (Gateway)**: Receptáculo de Pedra-Chave + anel de tijolos de pedra e escadas de tijolo
  de pedra (definição de multibloco no bytecode), alimentado por Etherium; Pedras-Chave casam os
  destinos.
* Calefator (dobra minérios com os 3 Etherium), Reconstrutor Arcano, Bateria de Mana, Mesa de
  Imbuimento (custos 30/42/54/66 níveis da peça), Flicker Habitat + Focos de Flicker, Invocador,
  Replicador, Enervador, Desconstrutor (Etherium Sombrio), Aura do Outro Mundo.

## Dimensões, bosses e mobs

* **Moo Moo Farm**, dim **−31** (`MMFDimensionID=-31`, bioma 110). Acesso: segurar o
  **Evil-Looking Book** (`arsmagica2:evilBook` = Perna de Pau + Compêndio) e clicar agachado num
  Receptáculo de Portal ativo → portal vermelho (bytecode de `BlockKeystoneReceptacle`/
  `TileEntityKeystoneRecepticle.doTeleport`). Habitantes: `arsmagica2.HellCow` (1% Chifre de Vaca
  Infernal encantado; drop raro diamante/esmeralda).
* **Guardiões** (rituais conferidos em `BossSpawnHelper`; níveis do Compêndio). Todos, mortos por
  jogador, deixam 1 Orbe do Infinito (17 = Água/Terra; 18 = Ar/Arcano/Vida/Raio; 19 = Natureza/
  Fogo/Ender/Inverno), 0–3 essências do elemento e **30%** da lembrança; +2000 de mana máx.:

| Guardião | Entidade | Ritual | Lembrança |
| --- | --- | --- | --- |
| Água | `BossWaterGuardian` | anel de Inlay de Redstone em bioma aquático, na chuva; barco + balde de água | `water_orbs` |
| Terra | `BossEarthGuardian` | 3×3 obsidiana, tijolo cinzelado no centro, anel Inlay de Ferro, 4 Tochas de Vinteum; Topázio + esmeralda + Quimerita | `earth_armor` |
| Arcano | `BossArcaneGuardian` | 2 pilares de estantes (3 alt.), Atril com Compêndio, anel Inlay de Ferro em frente; Essência Arcana | `arcane_spellbook` |
| Ar | `BossAirGuardian` | plataforma Y ≥ 150, anel Inlay de Ouro; Essência do Ar | `air_sled` |
| Inverno | `BossWinterGuardian` | bioma gelado, anel Inlay de Ouro, gelo sobre 2 Blocos de Topázio, 3 golens de neve | `winter_arm` |
| Raio | `BossLightningGuardian` | anel Inlay de Ouro, Bateria de Mana + 2 barras de ferro, tempestade | `lightningCharm` |
| Vida | `BossLifeGuardian` | anel 5×5 de giz + 4 velas, lua cheia, matar aldeão criança | `lifeWard` |
| Natureza | `BossNatureGuardian` | matar 5 Dríades em sequência rápida | `natureScythe` |
| Fogo | `BossFireGuardian` | Nether, anel Inlay de Ouro sobre obsidiana, bloco de carvão; Essência da Água | `fire_ears` |
| Ender | `BossEnderGuardian` | Fim, Aurem Negra sobre blocos de carvão, anel Inlay de Ouro, fogo nos 4 lados; Essência do Ender + Olho do Fim | `enderBoots` |

* Mobs: Dríade, Hecate, Elementais de Mana/Fogo/Água/Terra, Mana Creeper, Darkling, Magos da Luz/
  das Trevas, Flickers (11 tipos), Alucinações (fractais).

## Integrações no pack

* RotaryCraft e Immersive Engineering processam Vinteum (oredict `dustVinteum`, `gemBlueTopaz` etc.
  incluem itens do RotaryCraft).
* Et Futurum: minérios de ardósia do AM2. ATM Family: reservatório de Etherium (Ato V).
* Mesa de Imbuimento tem imbuimento de Visão Taumatúrgica (requer Thaumcraft, presente no pack).
* Reliquary: a Tocha de Interdição empurra várias entidades do AM2 (config).

## Armadilhas e dicas

* Gaste ao menos 1 forma + 1 componente nos 3 pontos iniciais (há `/respec` para OPs).
* Guardiões abrem crateras ao surgir (mobGriefing); invoque longe da base.
* Afinidade a 100% trava; Tomo de Afinidade Geral destrava. Água × Fogo machucam.
* Aurem Negra perto do altar aumenta a instabilidade; Prisma reduz.
* Meteoros de Pedra da Lua exigem nível de mago 10.
* O Evil-Looking Book consome o Compêndio.
* Armaduras do AM2 têm pouca durabilidade e drenam mana para se reparar.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `arsmagica:primeiro_feitico` | milestone | Primeiro feitiço criado no Altar (Ato II). |
| `arsmagica:prisma_celestial` | milestone | Etherium Luminoso (Ritual da Purificação). |
| `arsmagica:aurem_negra` | milestone | Etherium Sombrio (Ritual da Corrupção). |
| `arsmagica:tres_luzes` | milestone | Os três tipos de Etherium na rede. |
| `arsmagica:traje_mago` | milestone | Conjunto de Mago completo. |
| `arsmagica:mago_batalha` | milestone | Conjunto de Mago de Batalha. |
| `arsmagica:guardiao_agua` … `arsmagica:guardiao_ender` | boss | Os dez Guardiões (água, terra, arcano, ar, inverno, raio, vida, natureza, fogo, ender). |
| `arsmagica:dez_guardioes` | milestone | Todos os Guardiões derrotados. |
| `arsmagica:moo_moo_farm` | portal | Entrada na Moo Moo Farm (dim −31). |
| `arsmagica:maestria_arcana` | milestone | **Marco final**: Vestes do Arquimago completas — para o **Selo Arcano**. |
