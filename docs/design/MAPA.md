# Mapa de Capítulos

Ordem da lista de capítulos no livro (campo `order`), arquivo, id permanente e meta de
quests. Os ids **nunca** mudam depois de publicados (geram os UUIDs das quests).

Faixas de `order`:

| Faixa | Seção | Cor do título |
| --- | --- | --- |
| 0–9 | Crônicas Akáshicas (história) | §6 |
| 100–199 | Sobrevivência e Base | §a |
| 200–299 | Aventura e Dimensões | §2 |
| 300–399 | Tecnologia | §b |
| 500–599 | Magia | §d |
| 600–699 | Agricultura e Vida | §e |
| 800–899 | Série **AEDF3:** | §3 |
| 900–949 | Série **ATM:** | §6 |
| 990–999 | Epílogo (Avaritia) | §5 |

## Como os capítulos se ligam

* Capítulos de mod são **autossuficientes**: a raiz de cada um fica disponível desde o
  início (padrão ATM10 — as quests guiam, não trancam mods).
* Os capítulos **Crônicas** são a espinha da história. Suas quests dependem de **marcos**
  (`type: milestone/boss/portal`) dos capítulos de mod, por dependência cruzada
  (`deps: [ic2:mfsu]`), e mostram esses marcos no próprio mapa com `- link:`.
* Os oito **Selos** (Ato IV) exigem os marcos finais dos capítulos correspondentes.
* `atm_allthemodium` exige os oito Selos; `atm_star` é o pseudo-final; `avaritia` é o epílogo.

## Lista

| order | id | Título exibido | Arquivo | Meta |
| --- | --- | --- | --- | --- |
| 0 | prologo | Crônicas Akáshicas: Prólogo — O Despertar | story/prologo.yaml | 40 |
| 1 | ato1 | Crônicas Akáshicas: Ato I — Raízes | story/ato1.yaml | 60 |
| 2 | ato2 | Crônicas Akáshicas: Ato II — Engrenagens e Encantos | story/ato2.yaml | 70 |
| 3 | ato3 | Crônicas Akáshicas: Ato III — Os Reinos Perdidos | story/ato3.yaml | 60 |
| 4 | ato4 | Crônicas Akáshicas: Ato IV — Os Oito Selos | story/ato4.yaml | 90 |
| 100 | vanilla | Primeiros Passos | base/vanilla.yaml | 120 |
| 105 | etfuturum | Ecos do Futuro (Et Futurum) | base/etfuturum.yaml | 70 |
| 110 | utilidades | Utilidades da Base | base/utilidades.yaml | 100 |
| 115 | cozinha | Cozinha e Banquetes | base/cozinha.yaml | 160 |
| 120 | tinkers | Tinkers' Construct | base/tinkers.yaml | 170 |
| 125 | decoracao | Arquitetura e Decoração | base/decoracao.yaml | 130 |
| 200 | twilight | Floresta do Crepúsculo | aventura/twilight.yaml | 160 |
| 210 | aether | O Aether | aventura/aether.yaml | 120 |
| 220 | atum | Atum: As Areias Eternas | aventura/atum.yaml | 90 |
| 230 | nether | O Nether | aventura/nether.yaml | 80 |
| 235 | fim | O Fim | aventura/fim.yaml | 50 |
| 240 | runic | Runic Dungeons | aventura/runic.yaml | 60 |
| 245 | orespawn | OreSpawn | aventura/orespawn.yaml | 160 |
| 250 | jurassicraft | JurassiCraft | aventura/jurassicraft.yaml | 90 |
| 255 | fossils | Fósseis e Arqueologia | aventura/fossils.yaml | 100 |
| 260 | dimdoors | Portas Dimensionais | aventura/dimdoors.yaml | 35 |
| 265 | bestiario | Bestiário | aventura/bestiario.yaml | 110 |
| 300 | ic2 | IndustrialCraft 2 | tech/ic2.yaml | 220 |
| 302 | iu_base | Industrial Upgrade I — Fundamentos | tech/iu_base.yaml | 160 |
| 303 | iu_avancado | Industrial Upgrade II — Avançado | tech/iu_avancado.yaml | 160 |
| 304 | iu_endgame | Industrial Upgrade III — Endgame | tech/iu_endgame.yaml | 160 |
| 310 | buildcraft | BuildCraft | tech/buildcraft.yaml | 120 |
| 315 | thermal | Thermal Expansion | tech/thermal.yaml | 190 |
| 320 | enderio | Ender IO | tech/enderio.yaml | 190 |
| 325 | mekanism | Mekanism | tech/mekanism.yaml | 180 |
| 326 | mekanism_gen | Mekanism: Geradores e Fusão | tech/mekanism_gen.yaml | 60 |
| 330 | bigreactors | Big Reactors | tech/bigreactors.yaml | 60 |
| 335 | ie | Immersive Engineering | tech/ie.yaml | 160 |
| 336 | petroleo | Immersive Petroleum e Arcane Engineering | tech/petroleo.yaml | 60 |
| 340 | eln | Electrical Age | tech/eln.yaml | 140 |
| 345 | rotarycraft | RotaryCraft | tech/rotarycraft.yaml | 200 |
| 346 | reactorcraft | ReactorCraft | tech/reactorcraft.yaml | 90 |
| 347 | electricraft | ElectriCraft | tech/electricraft.yaml | 45 |
| 350 | mfr | MineFactory Reloaded | tech/mfr.yaml | 130 |
| 355 | extrautils | Extra Utilities | tech/extrautils.yaml | 110 |
| 360 | opencomputers | OpenComputers | tech/opencomputers.yaml | 90 |
| 365 | openblocks | OpenBlocks | tech/openblocks.yaml | 60 |
| 370 | ae2 | Applied Energistics 2 | tech/ae2.yaml | 200 |
| 375 | matteroverdrive | Matter Overdrive | tech/matteroverdrive.yaml | 100 |
| 380 | mps | Modular Powersuits | tech/mps.yaml | 60 |
| 385 | draconic | Draconic Evolution | tech/draconic.yaml | 150 |
| 390 | galacticraft | Galacticraft | tech/galacticraft.yaml | 160 |
| 391 | extraplanets | ExtraPlanets | tech/extraplanets.yaml | 130 |
| 392 | amunra | Amun-Ra | tech/amunra.yaml | 70 |
| 500 | thaumcraft | Thaumcraft I — O Aprendiz | magia/thaumcraft.yaml | 170 |
| 501 | thaumcraft2 | Thaumcraft II — O Taumaturgo | magia/thaumcraft2.yaml | 150 |
| 505 | ttinkerer | Thaumic Tinkerer | magia/ttinkerer.yaml | 90 |
| 506 | forbidden | Forbidden Magic | magia/forbidden.yaml | 60 |
| 507 | tainted | Tainted Magic | magia/tainted.yaml | 55 |
| 508 | thorizons | Thaumic Horizons | magia/thorizons.yaml | 65 |
| 509 | texploration | Thaumic Exploration e Revelations | magia/texploration.yaml | 50 |
| 510 | tbases | Thaumic Bases | magia/tbases.yaml | 55 |
| 511 | gadomancy | Gadomancy | magia/gadomancy.yaml | 55 |
| 512 | witchinggadgets | Witching Gadgets | magia/witchinggadgets.yaml | 65 |
| 513 | thaumextras | Thaumic Energistics, Boots e o Warp | magia/thaumextras.yaml | 50 |
| 530 | botania | Botania | magia/botania.yaml | 260 |
| 531 | advbotany | Advanced Botany | magia/advbotany.yaml | 45 |
| 540 | bloodmagic | Blood Magic | magia/bloodmagic.yaml | 170 |
| 541 | bloodarsenal | Blood Arsenal | magia/bloodarsenal.yaml | 45 |
| 550 | witchery | Witchery | magia/witchery.yaml | 170 |
| 560 | arsmagica | Ars Magica 2 | magia/arsmagica.yaml | 160 |
| 565 | wizardry | Electroblob's Wizardry | magia/wizardry.yaml | 85 |
| 570 | reliquary | Reliquary e Forgotten Relics | magia/reliquary.yaml | 80 |
| 580 | chromaticraft | ChromatiCraft | magia/chromaticraft.yaml | 200 |
| 600 | forestry | Forestry | vida/forestry.yaml | 150 |
| 605 | abelhas | Apicultura (Forestry, MagicBees, Gendustry) | vida/abelhas.yaml | 260 |
| 610 | binnie | Binnie: Árvores, Flores e Genética | vida/binnie.yaml | 120 |
| 615 | agricraft | AgriCraft | vida/agricraft.yaml | 75 |
| 620 | magicalcrops | Magical Crops | vida/magicalcrops.yaml | 85 |
| 800–815 | aedf3_* | AEDF3: (16 capítulos — ver docs/research/aedf3.md §5) | aedf3/*.yaml | 480 |
| 900 | atm_bemvindo | ATM: Bem-vindo | atm/bemvindo.yaml | 8 |
| 905 | atm_comeco | ATM: Capítulo 1 — O Começo | atm/comeco.yaml | 50 |
| 910 | atm_allthemodium | ATM: Capítulo 2 — Allthemodium | atm/allthemodium.yaml | 130 |
| 915 | atm_compressed | ATM: All The Compressed | atm/compressed.yaml | 70 |
| 920 | atm_tweaks | ATM: All The Tweaks | atm/tweaks.yaml | 45 |
| 925 | atm_star | ATM: Capítulo 3 — A Estrela ATM | atm/star.yaml | 160 |
| 930 | atm_bounty | ATM: Quadro de Recompensas | atm/bounty.yaml | 50 |
| 935 | atm_dicas | ATM: Dicas e Truques | atm/dicas.yaml | 30 |
| 990 | avaritia | Epílogo: Além do Infinito (Avaritia) | epilogo/avaritia.yaml | 150 |
| 995 | singularidades | Epílogo: Singularidades Eternas | epilogo/singularidades.yaml | 60 |
| 999 | ultima_pagina | Crônicas Akáshicas: A Última Página | story/ultima_pagina.yaml | 25 |

Meta total: ≈ 8.000 quests (o mínimo pedido é 5.000).
