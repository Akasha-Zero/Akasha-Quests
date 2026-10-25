# OpenComputers — estudo do capítulo `opencomputers`

## Visão geral

* **Mod:** OpenComputers `1.12.61-GTNH` (`OpenComputers-1.12.61-GTNH.jar`), namespace
  `OpenComputers`. Quase todos os componentes são metas de `OpenComputers:item`.
* **Capítulo:** `quests/chapters/tech/opencomputers.yaml`, `order: 360`, 97 quests.
* **Papel na campanha:** Ato II (primeiro computador, redstone, rede) até Ato III (robôs,
  drones, servidores). O marco final contribui para o **Selo da Engrenagem**.

Fontes: registro (`reg.py mod OpenComputers`), dump de receitas (receitas `default.recipes` —
`config/OpenComputers.cfg` inclui só `default.recipes`; hardmode/gregtech desligados),
**manual interno** do JAR (`assets/opencomputers/doc/en_US/**`: quickstart, computer, openOS,
páginas de blocos e itens), `assets/opencomputers/loot/loot.properties` (disquetes de
masmorra) e `config/OpenComputers.cfg`.

## Energia / recursos-base

* Energia interna própria; aceita RF (`value.RedstoneFlux=100`), EU (`IndustrialCraft2=400`),
  AE2 (200), Galacticraft (48) etc. Vários blocos recebem energia externa direto (tooltip
  mostra a taxa); **Conversor de Energia** é o caminho rápido; **Capacitador** armazena
  (1600 + bônus de vizinhança 800); **Distribuidor de Energia** equilibra sub-redes.
* Buffers (config): computador 500, robô 20 000, drone 5 000, tablet 10 000, microcontrolador
  1 000, botas 15 000, nanomáquinas 100 000. `ignorePower=false`.
* **Carregador** só funciona com sinal de redstone (força 15 = 100%; chave inverte).

## Progressão (ids reais)

1. Placa Crua `item:30` (ouro + argila + corante verde, ×8) → fornalha → PCB `item:32`;
   Transistor `item:23` (×8); Circuitos `item:24/25/26` (×8/×4/×2; ferro/ouro/Chip de Diamante
   `item:111` via Fio de Corte `item:17`); ULA `item:27`; UC `item:28` (relógio); Base de
   Cartão `item:33`; Manual `item:98` (livro + chip 1).
2. Primeiro computador: Gabinete `case1`, CPU `item:29`, 2× RAM `item:1`, EEPROM
   `OpenComputers:eeprom` + Manual → Lua BIOS, Disquete `item:4` + Manual → OpenOS, Unidade
   de Disco `diskDrive`, Placa de Vídeo `item:8`, Monitor `screen1`, Teclado `keyboard`,
   HDD `item:5`. Comando `install` copia o OpenOS para o HDD.
3. Níveis 2/3: `case2/case3` (o 3 tem slot de disquete), CPUs `item:42/43` (8/12/16
   componentes), RAM `item:1,50,2,3,38,39` (192–1024 KB), HDD 1/2/4 MB (`hddSizes`), GPUs
   `item:9/10`, Monitores `screen2` (mouse, 80×25, 4 bits) e `screen3` (160×50, 8 bits),
   APU `item:101/102`, Raid.
4. Mundo: Cartão de Redstone `item:66` / `item:12` (agrupada: RedNet do MFR), E/S de Redstone
   `redstone`, Adaptador `adapter`, Controladores de Inventário/Tanque `item:61/77`,
   Transpositor `transposer` (×4), Geonalizador `geolyzer`, Sensor de Movimento, Projetores
   Holográficos `hologram1/2`, Impressora 3D `printer` + Camaleão `item:96` + Cartucho
   `item:95`, Interweb `item:48`, Cartões de Internet/Dados.
5. Rede: Cartão de Rede `item:11`, Relé `relay`, Wi-Fi `item:113` (16 blocos) e `item:13`
   (400 blocos) — `maxWirelessRange=[16,400]`, Cartões Vinculados `item:51` (interdimensional),
   Divisor de Rede, Distribuidor de Energia.
6. Montador `assembler` → Robô `robot`, Microcontrolador `microcontroller` (carcaça
   `item:82`), Drone `item:84` (carcaça `item:83`, precisa de Barramento Nível 2 e Pedra do
   Fim do OC `endstone`), Tablet `item:68` (carcaça `item:74`), Carregador `charger`,
   Desmontador, aprimoramentos (Inventário `item:53`, Navegação `item:36`, Chunkloader
   `item:62`, Solar `item:34`, etc.), Botas Voadoras `hoverBoots`, Nanomáquinas `item:107`.
7. Servidores: Barramento `item:70`, Servidor `item:45/46/40`, Rack `rack`, Servidor de
   Terminal `item:108` + Terminal Remoto `item:41`, Unidade de Disco Montável `item:109`.

## Materiais e geração de mundo

* Nenhum minério. Disquetes de saque em masmorras (`loot.properties`): OPPM, Network,
  Plan9k, OpenIRC, OpenLoader, Builder, Digger, Mazer, Data Card Software, Generator Upgrade
  Software.

## Máquinas, estruturas e multiblocos

* Monitores do mesmo nível e cor formam telas grandes (resolução não muda).
* Complexidade no Montador: nível do item; contêineres = 2× nível.
* Rack: 4 montáveis; atua como relé + distribuidor de energia.

## Integrações no pack

* Cartão de Redstone Nível 2 / E/S de Redstone ↔ RedNet do MineFactory Reloaded.
* Adaptador lê blocos de muitos mods (IE tem módulo de compatibilidade ativo).
* Cartão de Sensor de Mundo usa Lentes Sensoras do Galacticraft.
* **ME Upgrade** (`item.ae:0-2`) e Beekeeper Upgrade existem no registro, mas o ME Upgrade
  **não tem receita** no dump nem no NEI — não usado no capítulo.

## Armadilhas e dicas

* Lados do Cartão de Redstone são relativos à frente do gabinete.
* Passar do limite de componentes da CPU impede o boot.
* Robô montado não pode ser alterado — só desmontado (chance de perder peças).
* Raid apaga discos ao inserir; remover um disco apaga o Raid.
* Tablets não persistem ao sair do jogo/trocar de dimensão.
* Nanomáquinas: muitas entradas ativas causam efeitos negativos; só Grogue remove.
* pt_BR do jogo: "Geonalizador", "Capacitador", "Analizador" (grafias do próprio mod).

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `opencomputers:primeiro_computador` | milestone | Primeiro computador com OpenOS (Ato II). |
| `opencomputers:estacao_trabalho` | milestone | Computador Nível 3 completo. |
| `opencomputers:rede_computadores` | milestone | Rede com cabo, relé e sem fio. |
| `opencomputers:maquinas_pensantes` | milestone | Robô + Drone + Microcontrolador. |
| `opencomputers:mestre_computacao` | milestone | **Marco final** (Rack + servidor + robô + drone) — para o **Selo da Engrenagem**. |
