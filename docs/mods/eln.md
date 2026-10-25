# Electrical Age — estudo do capítulo `eln`

## Visão geral

* **Mod:** `Eln-1.24.6.jar`, modid/namespace `Eln`, nome "Electrical Age - jrddunbr's build",
  versão `1.24.6` (Age Series). Simulador de circuitos: tensão, corrente, resistência, calor.
* **Capítulo:** `quests/chapters/tech/eln.yaml`, `order: 340`, 132 quests.
* **Papel na campanha:** Ato II (eletricidade "realista"); fim de capítulo com geradores de eixo
  3200 V e mineradora 800 V.
* **⚠ Limitação do registro:** o dump do registro só contém ~110 itens do Eln (minérios, materiais
  `Eln.sharedItem`, ferramentas/armaduras de cobre, armadura de grafeno, Exportador e Sonda CC/OC).
  **Nenhum meta de `Eln:Eln.SixNode`, `Eln:Eln.TransparentNode` nem `Eln:Eln.sharedItemStackOne`
  aparece** (cabos, máquinas, baterias, geradores, lâmpadas, sensores, ferramentas elétricas), e
  alguns `sharedItem` também faltam (resistências térmicas 64–79, reguladores 192–200, rastreador
  solar 704, escaneador 1024, tubo de mineração 1088, chave 7728, filtros 7720–7722, booster 7702).
  O lint rejeita esses metas. Por isso **72 quests usam `check`**, e cada uma traz um comentário
  `# real: <id:meta>` com o item verdadeiro (mapeado do bytecode de `SixNodeRegistration`,
  `TransparentNodeRegistration` e `ItemRegistration`, e conferido nas receitas do pack). Quando o
  registro for refeito com esses metas, basta trocar `check` por `get`.

Fontes: registro e receitas do pack (`recipes_crafting` traz as receitas com os metas reais dos
nós), `assets/eln/lang/en_US.lang` e `pt_BR.lang`, `config/Eln.cfg`, bytecode de
`mods/eln/craft/CraftingRecipes` (receitas das máquinas: Macerador, Compressor, Magnetizador,
Máquina de Placa, Forno a Arco, fornalha), `registration/*` (cabos e minérios). O NEI **não tem
handlers** para as máquinas do Eln.

## Energia / recursos-base

* Energia em joules/watts, simulada. Cabos (valores do JAR):

| Cabo | Tensão nominal | Receita |
| --- | --- | --- |
| Sinal (`SixNode:2048`) | 5 V (só sinal) | borracha + fios de ferro |
| Baixa Tensão (`2052`) | 50 V | borracha + fios de cobre |
| Média Tensão (`2056`) | 200 V | borracha + cabo LV |
| Alta Tensão (`2060`) | 800 V | borracha + cabo MV |
| Extrema Alta Tensão (`2064`) | 3200 V | borracha + barras de aço-tungstênio |
| Corrente baixa/média/alta (`2112–2114`) | — | borracha + cobre |
| Térmico de Cobre (`3073`) | calor | pedregulho + fio de cobre |

* Limite térmico dos cabos ~130 °C (`setPhysicalConstantLikeNormalCable`). Watchdogs de tensão,
  térmico e de eixo ligados; **`gameplay.explosion=false`**: aparelhos em falha são destruídos sem
  explosão.
* **Não há dano por "choque" ao tocar cabos neste build** (nenhum código de dano em cabos; os
  únicos danos a entidades são do Volante — arremesso — da Torre de Defesa e do Replicator).
* Exportador de Energia (`eln.EnergyConverter`): 1 J → 1/3 EU (IC2), 4/3 RF (Thermal), 0,133 (OC).

## Progressão (ids reais)

1. Minérios `Eln.Ore:1/4/5/6` (cobre, chumbo, tungstênio, cinábrio); barras `sharedItem:513/516/517`,
   mercúrio `520`; pós `577/578/5/6/7/8/10/579/580`.
2. Resina `4096` (Coletor de Resina na lateral do tronco, ou tora no Compressor) → fornalha →
   2 borrachas `4097`. Fios `4160/4161/4162` (3 lingotes → 12/12/6).
3. Bloco de Máquina `7682`, Motor Elétrico `640`, cabos LV, bateria 50V (`TransparentNode:1024`,
   6 chumbos), lâmpadas (soquetes `SixNode:4096–4114` + lâmpadas `sharedItemStackOne:256–301`).
4. Silício (areia → pó `7696` → barra `7697` → placa `7690`), chips `7680/7681` (oredict
   `circuitBasic`/`circuitAdvanced`), chips de sonda `7683/7684`.
5. Máquinas 50 V: Macerador (`2112`), Máquina de Placa (`2368`), Compressor (`2240`),
   Magnetizador (`2304`), Forno Elétrico (`2048`) com resistência térmica.
6. Liga aço-tungstênio: pó de ferro + pó de carvão + 4 pós de tungstênio → 6 pós `9` → barra `519`
   → placa `7691`; ímãs `7708/7709`; Bloco Avançado `7703`; Motor Avançado `641`; máquinas 200 V
   (`2116/2244/2308/2372`).
7. Transformador T1 (`TransparentNode:130`, núcleos `448/449/450`, quadro `7733`): razão de tensão
   = razão entre as pilhas de cabos. Fusíveis `6279–6282`, proteções `320/321`, disjuntor
   (`SixNode:6272`), interruptores, relés.
8. Geração: painéis solares (`3073–3076`, rastreador `704`), eólica `3136`, hidráulica `3152`,
   gerador a combustível `4289/4290`, Câmara de Aquecimento `192/193` + Turbina Térmica `257/264`,
   eixos (`268–277`), Turbina a Vapor `265`, a Combustão `267`, Gerador 3200V `266`, versões grandes
   `278–281` (peças `7740–7743`).
9. Sinais: fontes, sensores ambientais (`6656–6671`), sondas (`6400/6401/6464/6465`),
   Processador de Sinal `6980`, chips lógicos, sem fio (`5888/5896/5904`, antena `7713`).
10. Ferramentas: multímetro/termômetro (`sharedItemStackOne:896–898`), pilhas `7808/7809`,
    lanternas `7744/7745`, brocas `960–964`, Escaneador de Minério (`sharedItem:1024`), Raio-X
    `7840`, Mineradora 800V (`TransparentNode:2688`, alcance 10 na config), Armadura de Grafeno.
11. Forno a Arco 800V (`2176`): minério → 2 lingotes, 2 argilas → Arc Clay Ingot `4422`
    (alumínio no oredict), ferro → Arc Metal Ingot `4423` (aço no oredict); Diamante Sintético
    `4420` (4x barras de grafite no Compressor).

## Materiais e geração de mundo (`config/Eln.cfg` + `ItemRegistration.registerOre`)

Todos ativos (`mapgenerate`): cobre ~30 veios/chunk, 6–10 blocos, Y 0–80; chumbo ~8, 3–9, Y 0–24;
tungstênio ~6, 3–9, Y 0–32; cinábrio ~3, 3–9, Y 0–32. `dictionary.tungsten=false` → o tungstênio
do Eln usa `oreElnTungsten`/`ingotElnTungsten`/`dustElnTungsten`. `dictionary.chips=true`.

## Máquinas, estruturas e multiblocos

Receitas das máquinas (do bytecode):
* **Macerador:** minério de cobre/ferro/ouro/chumbo/tungstênio → 2 pós; carvão → pó; areia → pó de
  silício; cinábrio → pó.
* **Máquina de Placa:** lingote → placa (cobre, chumbo, ferro, ouro, liga; 4 silícios → 1 placa).
* **Compressor:** 4 pós de carvão → placa de carvão; 4 placas → barra de grafite; areia →
  dielétrico; tora → resina; 4x barras de grafite → diamante sintético.
* **Magnetizador:** 2 ferros → ímã; 2 ligas → ímã avançado; pó de cobre → redstone; 3 ímãs → núcleo
  avançado; lata inerte → pérola do Fim.
* **Fornalha (receitas do Eln):** resina → 2 borrachas; pó de liga → barra; pó de silício → barra;
  pó de cinábrio → mercúrio.
* Lâmpadas: vida útil carbono 6 h, incandescente 16 h, econômica 64 h, halógena 128 h, LED 512 h;
  `killMonstersAroundLamps=true` (raio 9).

## Dimensões, estruturas, chefes e mobs

Entidade **Replicator** (`replicatorPop=false` na config — não aparece naturalmente).

## Integrações no pack

* Cobre/chumbo/borracha/placas/chips via oredict (servem em IC2, Mekanism etc.).
* Exportador de Energia para IC2/Thermal/OpenComputers; Sonda CC/OC para computadores.
* Gerador a Combustível queima gasolina (capítulo `petroleo`).
* Martelo do IE tritura minério de cinábrio e tungstênio do Eln (receitas do pack).

## Armadilhas e dicas

* Sobretensão destrói aparelhos; corrente demais queima cabos; curto (fonte direto no terra) idem.
* Sem explosões neste pack, mas a peça se perde.
* Volante em alta rotação arremessa entidades; turbinas acima do rad/s máximo se destroem.
* Baterias envelhecem (`batteryHalfLife=2`); proteja com proteções de sobretensão/sobreaquecimento.
* Cabos de cores diferentes (rolos de pintura) não se conectam no mesmo bloco.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `eln:oficina_50v` | milestone | Oficina 50 V completa (macerar, laminar, comprimir, magnetizar, forno elétrico). |
| `eln:maquinas_200v` | milestone | Passagem para média tensão (blocos e motores avançados, máquinas 200 V). |
| `eln:rede_transformada` | milestone | Transformadores, fusíveis e disjuntores — rede protegida. |
| `eln:gerador_3200` | milestone | Gerador 3200V movido por turbina de eixo. |
| `eln:mineradora` | milestone | Mineradora 800V (Auto Miner). |
| `eln:mestre_eletricista` | milestone | **Marco final** (gerador 3200 V + mineradora + exportador + lógica + fusíveis) — candidato ao Selo da Engrenagem. |
