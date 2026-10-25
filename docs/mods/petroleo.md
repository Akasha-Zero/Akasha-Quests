# Immersive Petroleum e Arcane Engineering — estudo do capítulo `petroleo`

## Visão geral

* **Immersive Petroleum:** `immersivepetroleum-0.1.0-alpha.10-feature-catalyst-refinery-…-dirty.jar`,
  modid `immersivepetroleum`, nome "Immersive Petroleum - Forbidden Backport", versão
  `0.1.0-alpha.10`. É uma **reimplementação/backport feita pela Rede Akashic** para o IE
  "Forbidden" (mcmod.info). Tem lang `pt_BR` própria e uma categoria no Manual do Engenheiro.
  Diferente do IP original: tem **Derrick** (perfuração com plano direcional) antes da Pumpjack,
  **Hidrotratador**, **Unidade de Coqueamento**, Projetor, Queimador, Gerador Portátil, Lancha,
  Molotov/Napalm e uma **Refinaria catalítica** (receitas novas na Refinaria do IE com espaço de
  catalisador).
* **Arcane Engineering:** `arcane_engineering-0.2.7.jar`, modid `arcane_engineering`, aba de
  pesquisa `ARCANEENGINEERING` no Thaumonomicon. Ponte Thaumcraft × IE.
* **Capítulo:** `quests/chapters/tech/petroleo.yaml`, `order: 336`, 63 quests
  (52 do IP, 11 do Arcane Engineering).
* **Papel na campanha:** Ato II avançado (depende do IE pesado). O AE depende também de Thaumcraft
  intermediário (Taumio, Infusão, Destilação de Essência).

Fontes: registro (`reg.py mod immersivepetroleum` / `arcane_engineering`, receitas), NEI
(Distillation/Hydrotreater/Coker/Bottling/Refinery), `assets/immersivepetroleum/lang/pt_BR.lang`
(manual completo), `config/immersivepetroleum.cfg`, bytecode (`DefaultReservoirTypes`,
`DistillationRecipeBook`, `HydrotreaterRecipes`, `CokerRecipeSpec`, `PetroleumCatalystRefinery`,
`PetroleumFuelIntegration`, `ConcreteMixRecipe`, `*Materials`) e `assets/arcane_engineering/lang`.

## Energia / recursos-base

* RF (pelos fios do IE). Gerador Portátil: gasolina 6 mB/s, benzol 6 mB/s, nafta 9 mB/s → 256 RF/t,
  tanque 8000 mB (config). Lancha: gasolina 1 mB/t, nafta/benzol 2 mB/t.
* Combustíveis registrados no Gerador a Diesel do IE: diesel 322, diesel sulfurado 285, querosene
  208 (biodiesel do IE = 125).
* Fluidos: crudeoil, diesel, diesel_sulfur, lubricant, gasoline, naphtha, benzol, petroleum_gas,
  kerosene, napalm, acetaldehyde (+ fluido interno de concreto).

## Progressão (ids reais)

1. **Projetor** `projector` (mostra estrutura e materiais), **Laje Gradeada** `steel_grating_slab`.
2. **Pesquisa sísmica:** `seismic_survey` carregado com Buckshot Cartridge do IE (`bullet:4`),
   3 blocos de altura livre → `survey_result` (mapa de sinal/depósitos).
3. **Reservatórios** (`DefaultReservoirTypes`): petróleo 2,5–32,5 M mB (resíduo 6 mB/t, peso 40),
   aquífero 5–10 M mB de água (peso 30), lava 250 k–1 M mB (peso 30).
4. **Derrick:** concreto seco `concrete_dry_bucket` (4 areias, 2 cascalhos, 2 argilas, balde) →
   Engarrafadora + 1000 mB de água → `concrete_bucket`; canos do IE como tubos; energia; água.
   Componentes: Steel Scaffolding, Steel Fence, Fluid Pipe, Light/Heavy/Redstone Engineering,
   laje gradeada. Plano direcional opcional.
5. **Pumpjack** (Steel Scaffolding, Light/Heavy/Redstone Eng., Treated Fence, bloco de aço, Steel
   Sheet Metal, Fluid Pipe) — extrai do poço pronto quando a pressão acaba.
6. **Tanque de Petróleo** (1 024 000 mB; Sheet Metal de ferro, cercas/andaimes de aço, lajes).
7. **Torre de Destilação** (Steel Scaffolding, Heavy/Redstone Eng., Sheet Metal, lajes, canos):
   60 mB de petróleo → 15 nafta + 20 querosene + 30 diesel sulfurado + 10 lubrificante (+7% betume);
   10 mB de querosene → 5 benzol + 5 diesel sulfurado (+ parafina).
8. **Hidrotratador** (Scaffolding, Sheet Metal, Light/Heavy/Redstone Eng.): 1000 diesel sulfurado +
   500 água → 1000 diesel + 5 pós de enxofre; 2000 lubrificante + 500 água → 2000 diesel sulfurado
   + parafina; 1500 nafta + 500 água → 2000 gás de petróleo.
9. **Unidade de Coqueamento:** 2 betumes + 125 mB água → 2 coques de petróleo + 27 mB diesel
   sulfurado. Coque → pó (Triturador) → aço no Forno a Arco / 8 pós → grafite no Espremedor.
10. **Refinaria catalítica** (Refinaria do IE): 15 nafta + 10 benzol → 25 gasolina (80 RF/t, sem
    catalisador); 5 benzol + 5 gás de petróleo → 10 creosoto (catalisador pó de redstone,
    240 RF/t); 15 gás de petróleo → 15 acetaldeído (catalisador placa de constantan, 120 RF/t).
11. **Usos:** Gerador Portátil `gas_generator`, Lancha `motorboat` (+5 melhorias), garrafa de
    gasolina/Molotov, Napalm (6 pós de alumínio + balde → Engarrafadora + 1000 mB gasolina),
    Asfalto (+20% de velocidade, config), parafina (velas, bloco escorregadio, couro sintético),
    Lubrificador Automático/Almotolia (Triturador, Escavadeira, Pumpjack), melhoria de lubrificação
    da furadeira (75% de chance de não gastar a cabeça), Queimador de Excedentes.

**Arcane Engineering:** núcleo de varinha de madeira tratada (`wand_upgradeable`, 30 de vis, 2
melhorias na Bancada do Engenheiro), ponteiras `wandcap:0` aço (+2 espaços, sem penalidade) e
`wandcap:1` electrum (95% de custo, +1 espaço), melhorias `wandupgrade:0` Capacitor de Vis
(arcana), `:1` Carregador Cristalino e `:2` Energizador de Foco (infusão), cabeças de furadeira
`drillhead:0` Taumio (arcana) e `:1` Vazio (infusão, imune a dano), Botas de Impulso
`impulse_boots` (bancada; biodiesel), Desestabilizador de Nodos `destabilizer` (RF, deixa o nodo
Faminto; pesquisa dá Warp). O dump do NEI não traz os ingredientes das receitas arcanas/de
infusão (lista vazia), então as quests mandam conferir no Thaumonomicon.

## Materiais e geração de mundo

Nenhum minério novo. Petróleo vem de reservatórios virtuais por região (pesquisa sísmica).

## Máquinas, estruturas e multiblocos

Derrick, Pumpjack, Tanque de Petróleo, Torre de Destilação, Hidrotratador e Unidade de
Coqueamento são formados com o Martelo do Engenheiro a partir de blocos do IE (+ laje gradeada e,
na coqueria, concreto/ladrilho/madeira tratada). As contagens exatas não estão numa tabela simples
no JAR; o capítulo manda usar o **Projetor**, que lista os materiais. Gerador Portátil, Queimador e
Lubrificador são blocos simples.

## Dimensões, estruturas, chefes e mobs

Nenhum.

## Integrações no pack

* Depende do IE (multiblocos, Engarrafadora, Refinaria, Triturador, Espremedor, Bancada).
* Coque de petróleo no Alto-Forno do IE e como pó no Forno a Arco/Espremedor.
* Gasolina alimenta o Gerador a Combustível do Electrical Age.
* Enxofre recuperado entra no oredict `dustSulfur`.
* Arcane Engineering liga Thaumcraft (varinhas, taumio, vazio) à Bancada do Engenheiro e ao
  biodiesel do IE.

## Armadilhas e dicas

* Toda saída da Torre de Destilação precisa de destino; tanque cheio trava o processo.
* Petróleo bruto deixa lento; lubrificante escorrega; napalm propaga fogo; Molotov tem pavio de
  15 s no inventário.
* Estruturas não carregam chunks distantes — mantenha poço, Derrick e Pumpjack carregados.
* Esvazie tanques antes de desmontar. Itens "Registro de Recuperação" são gerados quando o estado
  salvo de uma máquina fica inconsistente (para o administrador recuperar).
* Desestabilizador de Nodos dá Warp e pode destruir nodos vizinhos.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `petroleo:torre_destilacao` | milestone | Primeira destilação de petróleo (Derrick + Pumpjack + Torre). |
| `petroleo:gasolina` | milestone | Gasolina na Refinaria catalítica. |
| `petroleo:industria_petroleo` | milestone | **Marco final do IP**: cadeia completa (gerador a gasolina, coqueria, diesel, asfalto, creosoto catalítico). |
| `petroleo:varinha_engenheiro` | milestone | **Marco do Arcane Engineering**: varinha de madeira tratada com melhorias (ponte Magia × Tecnologia; útil para o Selo Arcano ou da Engrenagem). |
