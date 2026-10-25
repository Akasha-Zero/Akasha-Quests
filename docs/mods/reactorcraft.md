# ReactorCraft — estudo do capítulo `reactorcraft`

## Visão geral

* **Mod:** ReactorCraft V33a (+ patch cliente "Akashic ReactorCraft PT-BR Format Fix").
  Namespace `ReactorCraft:reactorcraft_*`. Exige RotaryCraft (Mesa de Trabalho, HSLA).
* **Capítulo:** `quests/chapters/tech/reactorcraft.yaml`, `order: 346`, 89 quests. Raiz
  (`fluorita`) sem dependência; `manual_rec` tem `deps_hidden: rotarycraft:aco_hsla`.
* **Campanha:** Ato II/IV — Selo da Engrenagem.
* Fontes: registro, bancada, NEI (`recentri`, `recelect`, `rcammonia`, Blast Furnace do RC,
  compactador), Manual interno (`Reika/ReactorCraft/Resources`), config `ReactorCraft.cfg`,
  bytecode (`ReactorOres`, constantes das TileEntities, `RadiationShield`,
  `TileEntityUProcessor$Processes`). Receitas de máquinas (Mesa de Trabalho) não estão no NEI.

## Energia / recursos-base

Calor → caldeiras (200 mB água por vapor) → dutos de vapor → grade → Turbina (até 65.536 rad/s,
torque cap 32.768) ou Turbina de Alta Pressão (131.072 rad/s, injetores) → eixo do RotaryCraft
ou Gerador de Turbina (RF/EU). CPU: 1.024 W por haste. Pressurizador 1.024 Nm/16 kW; trocador
512 rad/s/8 kW; extrator de fluidos 512 Nm/65 kW; Centrífuga de Isótopos **262.144 rad/s**.

## Progressão

Fluorita + pechblenda (→ Lingote de Urânio Bruto) → Processador de Urânio (sem potência, água;
`ingotUranium` + HF → UF6) → Centrífuga → pó enriquecido `item_raw:1` (4 → 2 pastilhas
`item_fuel`) / empobrecido → núcleos `item_placer`, hastes `:1` (Vara de Absorção: Pr-In-Cad +
grafite), CPU `:3`, caldeiras `:11`, dutos `:6`, grade `:12`, turbina `:4` → condensador/
pressurizador/amônia → HP `:36` → reprodutor (sódio via Eletrolisador, `:18/:19/:20`, plutônio)
→ HTGR (TRISO `item_pellet`, leito `:29`, CO2 `:28/:30`) → tório (`:38`, `:40`) → fusão
(ímãs por compactador, ferromagnéticos, pré-aquecedor `:23` com Raio de Calor, injetor `:22`,
toroides `:15`, solenoide `:27` 32.768 Nm 256–8.192 rad/s, absorvedores `:26`, tubos
magnéticos `:25` + Van de Graaff).

## Materiais e geração de mundo (bytecode `ReactorOres`; config 100%)

Fluorita Y32–60 (8 cores); Pechblenda Y8–24 (Overworld) e Y0–64 (Fim); Cádmio Y12–32; Índio
Y0–16; Prata Y16–40; Calcita Y32–60; Magnetita Y60–128; Cloreto de Amônia e Torita (Y0–32) no
Nether. `Make Fuel Ores Weakly Radioactive=false`; água pesada em qualquer dimensão.

## Riscos (bytecode)

Núcleos: 800 °C revestimento, 1.400 °C hidrogênio, 1.800 °C meltdown (cório). Amônia detona
>650 °C na caldeira. Leito de seixos falha ~4.400 °C. Tório despeja combustível a 1.100 °C.
Blindagem: aço, concreto, água, bloco de lingote de rocha matriz, chumbo, obsidiana, vidro
anti-explosão. Config: nêutrons **não** se movem na vertical; núcleos ativos carregam chunk.

## Integrações

RotaryCraft (tudo), Thermal (Pirotéo para cádmio/índio, prata, transposer), IC2 (urânio
purificado no processador), ElectriCraft (EU Splitter para o gerador).

## Marcos exportados

| Marco | Representa |
| --- | --- |
| `reactorcraft:primeira_fissao` | Primeiro reator de fissão com turbina. |
| `reactorcraft:gigaturbina` | Usina: turbina de alta pressão, ciclo fechado, blindagem. |
| `reactorcraft:plutonio` | Reator reprodutor a sódio. |
| `reactorcraft:htgr` | Reator a gás de alta temperatura. |
| `reactorcraft:fusao` | Tokamak funcionando. |
| `reactorcraft:mestre_nuclear` | **Marco final** — Selo da Engrenagem. |
