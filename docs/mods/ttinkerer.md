# Thaumic Tinkerer 2.12.30 — estudo do capítulo `ttinkerer`

## Visão geral

* `ThaumicTinkerer-2.12.30.jar` (modid `ThaumicTinkerer`). Capítulo `quests/chapters/magia/ttinkerer.yaml`,
  `order: 505`, 74 quests. Raiz `inventor` com `deps_hidden: [thaumcraft:taumonomicon]`; quests de infusão
  com `deps_hidden: [thaumcraft:infusao]` e afins.
* Fontes: registro, NEI (saídas), **decompilação CFR** de todas as classes `thaumic/tinkerer/common`
  (cada item define sua pesquisa e receita), `assets/ttinkerer/lang/en_US.lang`, `config/ThaumicTinkerer.cfg`.
* Papel: Ato II (gadgets) → Ato IV (Kami/Ichor), contribui para o Selo Arcano.

## Energia / recursos-base

Vis de varinha, essência (Restaurador usa Instrumentum), XP (Foco de Dreno). Ichor (8 por infusão de
Estrela do Nether + diamante + Ender Shard + olho do Fim + Nether Shard; 32 Humanus, 32 Lux, 64 Spiritus).

## Progressão (ids reais)

1. Quartzo Esfumaçado `darkQuartzItem` → Interface Transvetora `interface`, Aglutinador `connector`,
   Deslocador `dislocator`, Atratores `magnet:0/1`, Molde de Alma `soulMould`, Locomotiva, Tabuleta do
   Dinamismo `animationTablet`.
2. Gases `gaseousLightItem`/`gaseousShadowItem`, Nitor Hiperenergético `brightNitor`, Funil `funnel`,
   **Restaurador Táumico** `repairer`, fogos imbuídos `fire*`, sementes/poções infundidas.
3. Pano de Feitiços `spellCloth` → **Encantador Osmótico** `enchanter` (6 pilares de 2–12 Totens
   Obsidianos `Thaumcraft:blockCosmeticSolid` + Nitor no topo, até 4 blocos) + 14 encantamentos próprios.
4. Talismã de Remédio `cleansingTalisman`, Talismã de Retenção `xpTalisman`, Capacete da Revelação,
   Lâmina do Espírito `bloodSword` → Tabuleta de Necromancia `spawner` + Aspectos de Alma `mobAspect:*`.
5. Focos: Efreet `focusSmelt`, Ascensão `focusFlight`, Distorção `focusDeflect`, Telecinese,
   Deslocamento, Restauração `focusHeal` (precisa do Foco Pech), Fenda do Fim `focusEnderChest`.
6. **Kami** (`kamiResource`): 6 Nether Shard, 7 Ender Shard (seguros por Pigmen/Endermen), 0 Ichor,
   1 Ichorcloth (arcano 125 de cada primal), 2 Ichorium (100), 4 Pontas de Ichorium (30% de desconto),
   5 Haste tecida em Ichor (1000 vis). Ferramentas `ichor*` e despertas `ichor*Gem` (instab. 15),
   vestes `ichorcloth*` e despertas `ichorcloth*Gem` (instab. 13), Pérola Celestial `skyPearl`,
   Portal Celestial `warpGate`, Foco de Retorno `focusRecall`, Bolsa Sem Fundo, Anel do Buraco Negro,
   Protoargila, Espelho do Moldador, Amuleto Felino.

## Dimensões

* **Bedrock** (dim −19): agachado, quebrar rocha-mãe abaixo de Y 5 na Superfície com a Picareta
  Desperta cria o Portal; voltar quebrando rocha-mãe acima de Y 253. Densidade de minério 1 (config).

## Armadilhas e dicas

* **Toda pesquisa Kami** tem como pais ocultos todas as pesquisas não perdidas de Básico,
  Taumaturgia, Alquimia, Artifício, Golemancia e da aba do Tinkerer (inclusive de outros addons
  nessas abas) — código de `KamiResearchItem`. ICHOR dá 5 Warp.
* O Encantador não funciona com pontas de ferro/cobre.
* Lista proibida do Foco de Deslocamento: baús compactados/infinitos do Avaritiaddons.
* Analisador de Aspectos / Conector de Golem (periféricos) não estão no registro — fora do capítulo.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `ttinkerer:inventor_completo` | milestone | Tabuleta, Restaurador, Encantador Osmótico. |
| `ttinkerer:varinha_ichor` | milestone | Haste tecida em Ichor + Pontas de Ichorium. |
| `ttinkerer:dimensao_rochamae` | portal | Dimensão da Rocha-Mãe (−19). |
| `ttinkerer:kami` | milestone | **Marco final** (vestes Kami despertas) → Selo Arcano. |
