# Reliquary: Forbidden Reincarnation + Forgotten Relics — estudo do capítulo `reliquary`

## Visão geral

* **Reliquary:** `Reliquary-1.7.10-1.2.1-Forbidden-0.1.0-alpha.1.jar`, namespace `xreliquary`,
  nome "Reliquary: Forbidden Reincarnation". Segundo `UPSTREAM.md`, é um **fork não oficial
  (GPLv3) da Rede Akashic** do commit `9354a12` (2016-10-17) do branch 1.7.10 de
  P3pp3rF1y/Reliquary — não é uma recompilação verificada do binário 1.2.1.483. Depende da
  libsandstone embutida. O `pt_BR.lang` do JAR está vazio (itens em inglês). A Elsewhere Flask
  existe no lang mas **não está registrada** — não aparece no capítulo.
* **Forgotten Relics:** `ForgottenRelics-1.7.3-[Final].jar`, namespace `ForgottenRelics`, addon de
  Thaumcraft 4 + Botania (autor Integral). Quase tudo por Infusão Arcana, pesquisa na aba própria
  do Taumonomicon. Sem `pt_BR.lang`.
* **Capítulo:** `quests/chapters/magia/reliquary.yaml`, `order: 570`, 104 quests (69 Reliquary +
  35 Forgotten Relics). Reliquary é Ato I/II (ingredientes de mobs, ferro e ouro); Forgotten Relics
  é Ato IV (Pérola Primordial, Alfheim, Gaia) e se liga ao **Selo Arcano**.

Fontes: registro (`reg.py mod xreliquary`, `reg.py mod ForgottenRelics`, receitas de bancada),
`reg.py research` (árvore de pesquisas FR), NEI (a infusão do FR aparece no catálogo **sem
entradas** — o dump não capturou os ingredientes; as descrições remetem ao Taumonomicon), lang
`en_US` dos dois JARs (tooltips e textos de pesquisa), `config/xreliquary.cfg`,
`config/ForgottenRelics.cfg` e bytecode (`CommonEventHandler` — drops de mobs;
`TileEntityCauldron`, `TileEntityAltar`, `TileEntityMortar`; `XRRecipes`/`Alkahestry` — lista de
alcaestria).

## Energia / recursos-base

* **Reliquary** não tem energia: relíquias guardam "cargas" internas de itens comuns (redstone no
  Tomo, glowstone na Pedra de Midas, pólvora no Catalisador, farinha de osso na Vara da Colheita,
  penas no Vendaval — 100 por pena, limite 30 000 —, pó de blaze e cargas de fogo no Cajado do
  Piromante, bolas de neve nas varas de gelo, pérolas no Cajado do Ender, tochas no Cajado do
  Peregrino). Limites padrão 250 (config).
* **Ingredientes de mobs** (`xreliquary:mob_ingredient:0..15`, só quando o jogador mata; Pilhagem
  aumenta): 0 Costela (esqueleto), 1 Costela Murcha (esqueleto wither), 2 Quelíceras (aranha/
  aranha das cavernas), 3 Glândula Catalisadora (creeper/ghast), 4 Pérola de Gosma (slime), 5 Asa
  de Morcego, 6 Coração de Zumbi (zumbi/homem-porco), 7 Núcleo Derretido (blaze/cubo de magma),
  8 Olho da Tempestade (creeper eletrificado), 10 Núcleo Congelado (golem de neve), 11 Coração
  Nebuloso (enderman), 12 Bico de Lula; fabricados: 9 Essência Fértil, 13 Garra Infernal, 14
  Fragmento de Concha do Kraken, 15 Tecido Carmesim. Chapéu de Bruxa (`witch_hat`) cai de bruxas.
* **Forgotten Relics** consome **Vis** (varinhas/cetros no inventário) para os grimórios.

## Progressão (ids reais)

1. Ingredientes → **Tomo de Alcaestria** `alkahest_tome` (até 250 redstone; duplica itens ao ser
   fabricado com eles: terra, pedregulho, areia, cascalho, arenito, argila, netherrack, carvão,
   lápis, obsidiana, areia das almas, tijolo do Nether, pedra do Fim, pólvora, sílex, ouro, ferro,
   esmeralda e lingotes do oredict) → **Altar da Luz** `altar_idle` (3 redstone, ~20 ± 5 min,
   precisa ver o céu, produz glowstone).
2. **Boticário**: Pilão `apothecary_mortar` (moer 2–3 ingredientes → `potion_essence`), Caldeirão
   `apothecary_cauldron` (calor: fogo/lava; água + verruga do Nether + essência; redstone ≤5,
   glowstone, pólvora = arremesso; `cook_time=160`) → frasco `xreliquary:potion` (5 painéis).
3. **Pistola** `handgun` = `gun_part:0/1/2` + ferro + Pérola de Gosma; munição `bullet:1..9`,
   carregadores `magazine:0..9` (8 tiros).
4. Relíquias: Lágrima do Vazio `void_tear_empty` é base de Cálices, Cajados, Pedra de Midas,
   Lágrima Infernal; Moeda da Fortuna + Lágrima Infernal → Medalhão do Herói; Pena Angelical + 3
   Frascos do Coração de Anjo → Pluma da Fênix.
5. **Forgotten Relics** (`reg.py research`): GenericTheory → MiningCharm → AdvancedMiningCharm,
   DeificAmulet → (NebulousCore, DiscordRing, ShinyStone); GenericTheory → SuperpositionRing →
   (OblivionStone → ChaosCore, DarkSunRing, AncientAegis, TheParadox); GenericTheory → WeatherStone
   → XPTome → (OblivionAmulet, TerrorCrown), PechFocus; GenericTheory → DimensionalMirror;
   GenericTheory → SpellbookTheory → (FateTome, Thunderpeal, DiscordTome → ObeliskDrainer →
   (SoulTome, EldritchSpell → ChaosTome → NuclearFury, CrimsonSpell), TelekinesisTome →
   LunarFlares, Apotheosis, Overthrower → VoidGrimoire); FalseJustice (secreta).

## Materiais e geração de mundo

Nenhum minério. Tudo vem de mobs, de itens vanilla e (FR) de Thaumcraft/Botania.

## Máquinas, estruturas e multiblocos

Altar da Luz, Pilão e Caldeirão do Boticário, Nó do Espectro (destino do Cajado do Ender),
Tocha de Interdição (raio 5, empurra a lista de entidades da config — inclui muitas de outros
mods), Vitória-Régia da Fertilidade (raio 4).

## Integrações no pack

* `config/xreliquary.cfg` listas de entidades/projéteis empurráveis incluem AM2, Wizardry,
  Twilight, Witchery, Thaumcraft, Botania etc.
* Forgotten Relics usa Thaumcraft (Infusão, Pérola Primordial, Obeliscos/Guardiões Eldritch) e
  Botania (recursos de Alfheim, Gaia, Vara do Planalto Sombreado/Reservatório Instável, Coroa de
  Louros). O capítulo usa `deps_hidden` para `thaumcraft:infusao`, `thaumcraft2:perola_primordial`,
  `thaumcraft2:terras_exteriores`, `botania:portal_alfheim` e `botania:gaia1`.

## Armadilhas e dicas

* Ingredientes só caem quando o jogador mata (não em farms automáticas sem jogador).
* Olho da Tempestade exige creeper eletrificado — raro.
* Nian Zhu só esconde você do tipo de monstro correspondente.
* Destruction Catalyst só quebra blocos "mundanos" (lista da config) em cubo 3×3×3.
* FR: `falseJusticeEnabled=true`, `voidGrimoireEnabled=true`, `interdimensionalMirror=true`; o
  Tomo dos Destinos tem recarga de 30–90 s; não carregue mais de um. Os Guardiões Eldritch somem
  se houver líquidos por perto (anti-abuso das Terras Exteriores).

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `reliquary:pocao_condensada` | milestone | Boticário funcionando (Ato II). |
| `reliquary:cacador_armado` | milestone | Pistola + munições especiais. |
| `reliquary:pena_fenix` | milestone | Pluma da Fênix (ressurreição). |
| `reliquary:medalhao_heroi` | milestone | Medalhão do Herói. |
| `reliquary:relicario_completo` | milestone | **Marco final do Reliquary.** |
| `reliquary:fr_reliquias_reunidas` | milestone | **Marco final de Forgotten Relics** (Ato IV) — para o **Selo Arcano**. |
