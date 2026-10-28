# Changelog — Akasha Quests UI 1.0.3

**Interface**
- Barra lateral de capítulos limitada a 22 % da largura da tela (antes até 30 %, e o mínimo fixo de
  110 px podia passar disso em GUI estreita). Nomes de capítulo e de grupo maiores que a barra são
  cortados com reticências; o nome inteiro continua na dica. Conferido em 1920×1080 e 1366×768
  (roteiro `devtest/sidebar.txt`): a barra fica com 21,8 % nas duas.

# Changelog — Akasha Quests UI 1.0.2

**Render (travadas em capítulos grandes)**
- A 1.0.1 limpava a profundidade antes de cada ícone de quest com o recorte do mapa inteiro: num
  capítulo como RotaryCraft (192 quests) ou ChromatiCraft (201), eram cerca de 200 passadas de
  profundidade de tela cheia por frame, além do mundo, do Distant Horizons e do Angelica — numa GPU
  integrada (Intel UHD) isso derruba o FPS e dá as "congeladas" ao navegar. Agora cada limpeza fica
  no quadrado do ícone (com meia aresta de margem), o que dá o mesmo resultado: o ícone só é testado
  contra a profundidade dentro do próprio quadrado, e o resto só tem profundidade de ícones
  anteriores, que ele não toca. Limpar mais do que o necessário é sempre o comportamento antigo.
- A/B num cliente real headless (Xvfb/llvmpipe, 1920×1080, mesmo mundo e câmera, roteiro
  `devtest/perf.txt` com 200 ícones na tela, 4 rodadas de cada): 1.0.1 ≈ 4–5 FPS, 1.0.2 ≈ 11–13 FPS.
  Capturas das duas versões idênticas pixel a pixel (enquadrado e com zoom alto). O ganho numa Intel
  UHD não foi medido; o llvmpipe não reproduz o custo de limpeza da Intel nem o dos ícones com
  renderizador próprio (RotaryCraft, ChromatiCraft).

# Changelog — Akasha Quests UI 1.0.1

Auditoria de render, interface, UX e lógica. Cada achado foi conferido no código do mod, do
BetterQuesting e do Minecraft antes de corrigir.

**Render**
- Ícones de item escreviam profundidade e recortavam os ícones desenhados depois (buracos com a
  forma de um bloco 3D). Profundidade limpa antes de cada ícone e entre camadas; escala uniforme.
- Franja escura no contorno e nos selos com zoom alto: texels transparentes herdam a cor dos
  vizinhos visíveis; mipmaps por média de área (tamanhos ímpares não perdem linha nem coluna).
- Texto depois de um ícone encantado herdava o blend aditivo do brilho.
- Recorte do mapa deslocado até `f − 1` px em janelas de altura ímpar (cálculo do BQ); recorte próprio.
- Imagens enormes reduzidas ao carregar (limite 2048 e o da GPU); cache de imagens reavalia arquivos
  ausentes e libera texturas sem uso.
- Chevrons não saltam mais ao passar o mouse; linhas não atravessam quests sem forma; brilho e selos
  não somem na borda; imagem de fundo do BQ alinhada ao pixel; grade num só desenho; formas com
  recurso faltando não viram quadrados brancos.
- Item como imagem de capítulo respeita a cor.

**Lógica**
- Edições pelo diálogo de imagem ou pelo menu eram descartadas quando chegava qualquer sincronização
  (o modelo recriava os objetos). As imagens mantêm a identidade; alteração concorrente avisa no chat.
- Mapa desatualizado ao voltar do Designer, do editor de quest ou de um seletor do BQ.
- Links de descrição e cliques em imagem abriam quests e capítulos ocultos ou bloqueados.
- Subir/descer capítulo segue a ordem mostrada no painel (dentro do grupo) e desabilita nas pontas.
- "Resgatar tudo": chave de configuração do BQ correta e Shift com o mesmo sentido do BQ.
- Dados com tamanho 0 ou NaN não geram mais NaN (o BQ deixaria de salvar o capítulo).
- IDs de quest digitados errado não viram outro ID; Ctrl+clique e arrastar não move a seleção errada.
- Diálogo de imagem não reaparece sozinho e não perde o que foi digitado ao redimensionar a janela.
- Opções "animar linhas" e "pulsar disponíveis" e o recolhimento de grupo renomeado são salvos;
  valores fora da faixa no config são limitados.
- O tema só é aplicado sobre o tema padrão do BQ, nunca sobre um escolhido pelo jogador.
- `bqgen`: listas com 11 ou mais itens saíam embaralhadas (`"10:10"` antes de `"2:10"`). Havia 45
  no livro (itens de tarefas e tabela de saque); `dist/` regenerado com a ordem correta. Valida
  `tint` (6 dígitos), `size` positivo e `click: chapter:`.

**Interface e UX**
- Backspace nunca sai do livro; ESC e Backspace fecham a camada mais interna (menu, janela, seleção).
- Clique direito no vazio não volta mais para a tela inicial do BQ.
- Título e progresso do capítulo sobre o mapa, como prometido no DESIGN §6.
- Telas do BQ (tarefas, recompensas, editores) em português: tradução pt_BR do BetterQuesting e do
  BQ Standard incluída no mod.
- Menus longos rolam com a roda e cortam rótulos compridos com reticências.
- Botões desabilitados dizem o motivo ("Concluída", "Resgatado"); "Resgatar" ocupa a largura toda
  quando não há tarefas.
- Mira escondida sob o fundo translúcido; tela inteira escurece com a janela da quest aberta;
  sem hover no mapa sob o painel aberto.
- Contraste: linhas bloqueadas com contorno escuro (≥ 3,45:1), texto desabilitado ≥ 4,1:1,
  contorno de quest bloqueada 3,14:1, dicas sem cinza-escuro ilegível; barra de status compacta.

# Changelog — Akasha Quests UI 1.0.0

Novo mod opcional, só cliente, em `mod/` (jar em `dist/mods/`): interface no estilo FTB Quests
para o BetterQuesting 3.8.79-GTNH.

- Barra de capítulos com grupos (pela pasta do capítulo), nomes curtos, selos de concluído,
  recompensa esperando e bloqueado; fixável ou recolhida.
- Mapa com câmera (zoom no cursor, arrastar, enquadrar), 11 formas de quest com anti-aliasing e
  mipmaps, estados por cor, selos, linhas com chevrons animados e destaque de requisitos/dependentes.
- Janela da quest sobre o mapa com os painéis de tarefa e recompensa do próprio BQ, descrição com
  links e imagens, Detectar/Entregar, Resgatar, navegação por pré-requisitos e dependentes.
- Imagens nos capítulos (resource pack, arquivo ou item) com tamanho, rotação, opacidade, cor,
  camada, dica, clique e quest requerida.
- Modo de edição: mover quests e imagens, redimensionar e girar imagens, forma e tamanho das
  quests, requisitos, grupos e ordem dos capítulos, desfazer.
- `bqgen`: campos `shape`, `group`, `short` e `images`; formas padrão por tipo de quest.
- Fundo translúcido como no FTB Quests: o mundo aparece atrás do mapa (opacidade ajustável em
  Opções de visualização ou `backgroundOpacity` no config).

# Changelog — Akasha Quests 1.0.0

Primeira versão do livro de quests *Crônicas Akáshicas* (BetterQuesting 3.8.79-GTNH) do modpack Technic Forbidden (TEOF 4.0).

**8.939 quests em 102 capítulos** — validado no BetterQuesting real, sem itens inexistentes.

## Crônicas Akáshicas (história) (228 quests)

- **Crônicas Akáshicas: Prólogo — O Despertar** — 29 quests · história (liga os marcos de todos os capítulos)
- **Crônicas Akáshicas: Ato I — Raízes** — 44 quests · história (liga os marcos de todos os capítulos)
- **Crônicas Akáshicas: Ato II — Engrenagens e Encantos** — 56 quests · história (liga os marcos de todos os capítulos)
- **Crônicas Akáshicas: Ato III — Os Reinos Perdidos** — 38 quests · história (liga os marcos de todos os capítulos)
- **Crônicas Akáshicas: Ato IV — Os Oito Selos** — 61 quests · história (liga os marcos de todos os capítulos)

## Base (776 quests)

- **Primeiros Passos** — 121 quests · mods: Et Futurum Requiem
- **Ecos do Futuro (Et Futurum)** — 79 quests · mods: Et Futurum Requiem
- **Utilidades da Base** — 103 quests · mods: Adventure Backpack, Storage Drawers, Iron Chests, Better Furnaces, AkashicFM, Waystones-X, EnderStorage, OmniPipes
- **Cozinha e Banquetes** — 157 quests · mods: Pam's HarvestCraft, Cooking For Blockheads, The Kitchen Mod
- **Tinkers' Construct** — 181 quests · mods: Tinkers' Construct, Iguana Tinker Tweaks, Thermal Expansion
- **Arquitetura e Decoração** — 135 quests · mods: BiblioCraft, Chisel, Malisis' Doors, Ztones-Unofficial, Carpenter's Blocks, Garden Stuff, Armourer's Workshop, Garden Containers, Flood Lights, LittleTiles

## Aventura (810 quests)

- **Floresta do Crepúsculo** — 158 quests · mods: The Twilight Forest
- **O Aether** — 118 quests · mods: The Aether
- **Atum: As Areias Eternas** — 89 quests · mods: Atum
- **O Nether** — 54 quests · mods: Netherlicious
- **O Fim** — 21 quests · mods: Et Futurum Requiem
- **Runic Dungeons** — 30 quests · mods: Runic Dungeons
- **OreSpawn** — 68 quests · mods: OreSpawn Remastered
- **JurassiCraft** — 69 quests · mods: JurassiCraft
- **Fósseis e Arqueologia** — 88 quests · mods: Fossils and Archeology Revival
- **Portas Dimensionais** — 20 quests · mods: Dimensional Doors
- **Bestiário** — 95 quests · caçadas de mobs vanilla e de mods (6 contratos diários)

## Tecnologia (3313 quests)

- **IndustrialCraft 2** — 248 quests · mods: IndustrialCraft 2
- **Industrial Upgrade I — Fundamentos** — 151 quests · mods: Industrial Upgrade, IndustrialCraft 2
- **Industrial Upgrade II — Avançado** — 153 quests · mods: Industrial Upgrade, IndustrialCraft 2
- **Industrial Upgrade III — Endgame** — 128 quests · mods: Industrial Upgrade, IndustrialCraft 2
- **BuildCraft** — 106 quests · mods: BC Transport, BC Robotics, BC Silicon, BuildCraft, BC Factory, BC Builders, BC Energy
- **Thermal Expansion** — 167 quests · mods: Thermal Expansion, Thermal Foundation
- **Ender IO** — 185 quests · mods: Ender IO
- **Mekanism** — 170 quests · mods: Mekanism, Mekanism Tools
- **Mekanism: Geradores e Fusão** — 53 quests · mods: Mekanism Generators, Mekanism
- **Big Reactors** — 54 quests · mods: Big Reactors, Mekanism, Ender IO, Thermal Foundation
- **Immersive Engineering** — 154 quests · mods: Immersive Engineering
- **Immersive Petroleum e Arcane Engineering** — 63 quests · mods: Immersive Petroleum - Forbidden Backport, Arcane Engineering, Immersive Engineering
- **Electrical Age** — 132 quests · mods: Electrical Age - jrddunbr's build
- **RotaryCraft** — 192 quests · mods: RotaryCraft
- **ReactorCraft** — 89 quests · mods: ReactorCraft, RotaryCraft
- **ElectriCraft** — 45 quests · mods: ElectriCraft
- **MineFactory Reloaded** — 129 quests · mods: MineFactory Reloaded, Thermal Expansion
- **Extra Utilities** — 105 quests · mods: Extra Utilities
- **OpenComputers** — 97 quests · mods: OpenComputers
- **OpenBlocks** — 59 quests · mods: OpenBlocks
- **Applied Energistics 2** — 188 quests · mods: Applied Energistics 2, AE2 Fluid Crafting
- **Matter Overdrive** — 98 quests · mods: MatterOverdrive: Community Edition
- **Modular Powersuits** — 57 quests · mods: MachineMuse's Modular Powersuits
- **Draconic Evolution** — 145 quests · mods: Draconic Evolution
- **Galacticraft** — 154 quests · mods: Galacticraft Core, Galacticraft Planets, BC Energy
- **ExtraPlanets** — 124 quests · mods: Extra Planets
- **Amun-Ra** — 67 quests · mods: Amun-Ra

## Magia (2140 quests)

- **Thaumcraft I — O Aprendiz** — 166 quests · mods: Thaumcraft
- **Thaumcraft II — O Taumaturgo** — 125 quests · mods: Thaumcraft, Avaritia
- **Thaumic Tinkerer** — 74 quests · mods: Thaumic Tinkerer, Thaumcraft
- **Forbidden Magic** — 63 quests · mods: Forbidden Magic, Botania
- **Tainted Magic** — 53 quests · mods: Tainted Magic, Thaumcraft
- **Thaumic Horizons** — 76 quests · mods: ThaumicHorizons, Thaumcraft
- **Thaumic Exploration e Revelations** — 50 quests · mods: Thaumic Exploration, Thaumic Revelations, Thaumcraft
- **Thaumic Bases** — 55 quests · mods: Thaumic Bases, Thaumcraft
- **Gadomancy** — 46 quests · mods: Gadomancy, Thaumcraft
- **Witching Gadgets** — 57 quests · mods: Witching Gadgets, Thaumcraft
- **Thaumic Energistics, Boots e o Warp** — 52 quests · mods: Thaumic Energistics, Thaumic Boots, WarpTheory, Thaumic Insurgence
- **Botania** — 311 quests · mods: Botania
- **Advanced Botany** — 48 quests · mods: Advanced Botany, Botania
- **Blood Magic** — 171 quests · mods: Blood Magic: Alchemical Wizardry
- **Blood Arsenal** — 51 quests · mods: Blood Arsenal
- **Witchery** — 185 quests · mods: Witchery
- **Ars Magica 2** — 166 quests · mods: Ars Magica 2
- **Electroblob's Wizardry** — 86 quests · mods: Electroblob's Wizardry
- **Reliquary e Forgotten Relics** — 104 quests · mods: Reliquary: Forbidden Reincarnation, Forgotten Relics
- **ChromatiCraft** — 201 quests · mods: ChromatiCraft

## Vida (600 quests)

- **Forestry** — 147 quests · mods: Forestry
- **Apicultura (Forestry, MagicBees, Gendustry)** — 263 quests · mods: Forestry, Gendustry, Magic Bees, Extra Bees, Akashic: All The Modium
- **Binnie: Árvores, Flores e Genética** — 62 quests · mods: Extra Trees, Genetics, Botany, Binnie Core
- **AgriCraft** — 58 quests · mods: AgriCraft
- **Magical Crops** — 70 quests · mods: Magical Crops: Core

## Série AEDF3: (441 quests)

- **AEDF3: A Vila** — 29 quests · mods: BiblioCraft, Carpenter's Blocks
- **AEDF3: Idade da Pedra e do Ferro** — 26 quests · mods: Tinkers' Construct
- **AEDF3: Era Industrial** — 27 quests · mods: IndustrialCraft 2
- **AEDF3: Aventura Infernal** — 25 quests · mods: Vanilla (Nether, Wither)
- **AEDF3: Energia Solar** — 23 quests · mods: Industrial Upgrade, IndustrialCraft 2
- **AEDF3: Armaduras do Futuro** — 25 quests · mods: IndustrialCraft 2, Industrial Upgrade
- **AEDF3: Aprendiz de Mago** — 24 quests · mods: Thaumcraft
- **AEDF3: Caçadores de Bosses** — 25 quests · mods: The Twilight Forest
- **AEDF3: Automação e Indústria Pesada** — 27 quests · mods: Thermal Expansion, MineFactory Reloaded, Ender IO, BC Builders
- **AEDF3: Mekanism** — 26 quests · mods: Mekanism
- **AEDF3: A Casa do Futuro** — 25 quests · mods: BiblioCraft, Chisel, Carpenter's Blocks, Garden Containers, Malisis' Doors
- **AEDF3: Armazenamento Digital** — 26 quests · mods: Applied Energistics 2
- **AEDF3: Energia Avançada** — 22 quests · mods: Big Reactors, IndustrialCraft 2, Ender IO
- **AEDF3: Evolução Dracônica** — 31 quests · mods: Draconic Evolution
- **AEDF3: Rumo ao Infinito** — 26 quests · mods: Avaritia
- **AEDF3: Corrida Espacial** — 30 quests · mods: Galacticraft Core, Galacticraft Planets
- **AEDF3: O Fim** — 24 quests · mods: IndustrialCraft 2, The Twilight Forest

## Série ATM: (456 quests)

- **ATM: Bem-vindo** — 8 quests · informativo (dicas e orientação)
- **ATM: Capítulo 1 — O Começo** — 44 quests · mods: Ender IO, Thermal Foundation
- **ATM: Capítulo 2 — Allthemodium** — 110 quests · mods: Akashic: All The Modium
- **ATM: All The Compressed** — 59 quests · mods: Akashic: All The Modium
- **ATM: All The Tweaks** — 40 quests · mods: Akashic: All The Modium
- **ATM: Capítulo 3 — A Estrela ATM** — 126 quests · mods: Akashic: All The Modium, Draconic Evolution, IndustrialCraft 2, Ender IO, Magical Crops: Core, Applied Energistics 2
- **ATM: Quadro de Recompensas** — 42 quests · caçadas diárias com recompensas ATM
- **ATM: Dicas e Truques** — 27 quests · informativo (dicas e orientação)

## Epílogo (175 quests)

- **Epílogo: Além do Infinito** — 105 quests · mods: Avaritia, Industrial Upgrade, The Twilight Forest, Avaritiaddons, Tinkers' Construct, MineFactory Reloaded, Ars Magica 2, Pam's HarvestCraft
- **Epílogo: Singularidades Eternas** — 50 quests · mods: Industrial Upgrade, Avaritia, Thermal Foundation, Extra Planets, Applied Energistics 2
- **Crônicas Akáshicas: A Última Página** — 20 quests · história (liga os marcos de todos os capítulos)

## Mods cobertos (109)

Advanced Botany, Adventure Backpack, AE2 Fluid Crafting, AgriCraft, Akashic: All The Modium, AkashicFM, Amun-Ra, Applied Energistics 2, Arcane Engineering, Armourer's Workshop, Ars Magica 2, Atum, Avaritia, Avaritiaddons, BC Builders, BC Energy, BC Factory, BC Robotics, BC Silicon, BC Transport, Better Furnaces, BiblioCraft, Big Reactors, Binnie Core, Blood Arsenal, Blood Magic: Alchemical Wizardry, Botania, Botany, BuildCraft, Carpenter's Blocks, Chisel, ChromatiCraft, Cooking For Blockheads, Dimensional Doors, Draconic Evolution, Electrical Age - jrddunbr's build, ElectriCraft, Electroblob's Wizardry, Ender IO, EnderStorage, Et Futurum Requiem, Eternal Singularity, Extra Bees, Extra Planets, Extra Trees, Extra Utilities, Flood Lights, Forbidden Magic, Forestry, Forgotten Relics, Fossils and Archeology Revival, Gadomancy, Galacticraft Core, Galacticraft Planets, Garden Containers, Garden Core, Garden Stuff, Garden Trees, Gendustry, Genetics, Iguana Tinker Tweaks, Immersive Engineering, Immersive Petroleum - Forbidden Backport, Industrial Upgrade, IndustrialCraft 2, Iron Chests, JurassiCraft, LittleTiles, MachineMuse's Modular Powersuits, Magic Bees, Magical Crops: Core, Malisis' Doors, MatterOverdrive: Community Edition, Mekanism, Mekanism Generators, Mekanism Tools, MineFactory Reloaded, OmniPipes, OpenBlocks, OpenComputers, OreSpawn Remastered, Pam's HarvestCraft, ReactorCraft, Reliquary: Forbidden Reincarnation, RotaryCraft, Runic Dungeons, Storage Drawers, Tainted Magic, Thaumcraft, Thaumic Bases, Thaumic Boots, Thaumic Energistics, Thaumic Exploration, Thaumic Insurgence, Thaumic Revelations, Thaumic Tinkerer, ThaumicHorizons, The Aether, The Kitchen Mod, The Twilight Forest, Thermal Expansion, Thermal Foundation, Tinkers' Construct, WarpTheory, Waystones-X, Witchery, Witching Gadgets, Ztones-Unofficial, Netherlicious

## Também nesta versão

- Baús de Saque em 5 raridades (Comum → Lendário) e kits nomeados.
- Patches de pack em `patches/`: IDs de bioma ChromatiCraft × Twilight, receita do Quartzo Queimado, Circuito de Controle do MPS.
- Ferramentas: gerador/validador YAML → BQ, consulta ao registro real do pack, esqueleto automático de capítulos.
