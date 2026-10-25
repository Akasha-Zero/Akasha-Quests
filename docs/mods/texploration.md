# Thaumic Exploration e Thaumic Revelations — estudo do capítulo `texploration`

## Visão geral

* **Mods:** Thaumic Exploration `Thaumic-Exploration-1.5.27-GTNH.jar` (namespace
  `ThaumicExploration`) e Thaumic Revelations `ThaumicRevelations-1.2.3.jar` (namespace
  `thaumrev`). Ambos addons do Thaumcraft 4.2.3.5, cada um com aba própria no Taumonomicon
  (`TX`, 40 pesquisas; `thaumrev`, 24 pesquisas).
* **Capítulo:** `quests/chapters/magia/texploration.yaml`, `order: 509`, 50 quests.
* **Papel na campanha:** Ato II–III. Utilidades de infusão (urnas, selos, botas, núcleos e
  ponteiras de varinha, Braseiro de Almas) e o arsenal dos Guardiões Táumicos (Ato III/IV,
  custo de 125 vis por primal por peça).
* Sem tradução pt_BR nos JARs: nomes dos itens em inglês.

Fontes: registro (`reg.py mod`, `reg.py research`), catálogo NEI (`reg.py nei`; os handlers do
Thaumcraft no dump trazem só as saídas, então as entradas vieram do código), lang en_US dos dois
JARs, configs `ThaumicExploration.cfg`/`thaumrev.cfg`, e as classes descompiladas
`flaxbeard.thaumicexploration.research.ModRecipes` e `thaumrev.config.ConfigRecipes`
(+ `WardenicUpgrade*`, `ThaumRevWorldGenerator`, `BlockExcubitura`).

## Energia / recursos-base

Essentia/vis do Thaumcraft. O Thaumic Revelations adiciona o aspecto **Excubitor**
(Alienis + Mortuus), presente nas pétalas da Rosa Guardiã (4 por pétala).

## Progressão (ids reais)

**Thaumic Exploration**
* Sebo Mágico (`Thaumcraft:ItemResource:4`) → Velas Flutuantes (`floatCandle:*`, 3 velas de
  sebo + Estilhaço de Ar) e Selos em Branco (`blankSeal:*`, 4 sebos + corante, bancada) →
  Crisol: `chestSeal:*` (Alienis 6, Permutatio 4, Vacuos 4, Vinculum 4, Iter 6) e `jarSeal:*`
  (troca Vacuos por Vitreus). Selo em baú/jarro **vazio** cria/entra numa rede compartilhada.
* `pureZombieBrain` (infusão: Cérebro de Zumbi + Poção de Fraqueza qualquer + Maçã Dourada +
  Balde de Água) → `thinkTankJar` (Jarro + 3 cérebros curados + Ferramenta de Escrita +
  Taumonomicon + Taumômetro; em cima de estante, cubo 3×3×3 vazio; livros → Fragmentos).
* `trashJar` (Void na Jarra + Baú Faminto + 4 Estilhaços de Entropia + Âmbar, inst. 7),
  `everfullUrn` (Vaso + 3 baldes de água + 3 tijolos), `everburnUrn` (lava; gasta centivis),
  `crucibleSouls` (Fornalha Alquímica + Crisol, carne podre, osso, lágrima de ghast, areia das
  almas, alambique), `replicator` (Matriz Rúnica + Mesa Arcana + 3 ouro + 3 táumio, inst. 9),
  `talismanFood`, `discountRing:0-5` (4 estilhaços, 15 vis), `charmNoTaint`, `tentacleRing`,
  `stabilizerBelt`, `bootsMeteor` (Botas do Viajante + Cristal de Fogo + 3 Netherrack + Foco de
  Fogo), `bootsComet` (Cristal de Água + 3 Neve + Foco de Gelo), núcleos `amberCore` (10 vis,
  regenera tudo), `transmutationCore` (75), `necroStaffCore` (200, 4× vis de monstros, 75% das
  outras fontes), cajados de âmbar (25) e transmutativo (175), ponteiras `sojournerCap` (drena
  nodos enquanto segura) e `mechanistCap` (drena mais rápido), `soulBrazier` (Estrela do Nether
  + Cérebro de Zumbi + 2 Metal do Vazio, inst. 7; guarda Warp permanente com vis de nodo +
  Mortuus). Encantamentos: Binding, Disarming, Night Vision (todos ativos na config).

**Thaumic Revelations**
* Rosa Guardiã `blockExcubitura` (gera madura na grama do Mundo Normal, ~1 tentativa a cada 10
  chunks; planta de cultivo; madura dá 2 drops, sementes ou pétalas `itemResource:0`).
* Crisol: `itemResource:1` Cristal (pétala + Praecantatio 32 + Vitreus 32), `:2` Quartzo
  (quartzo + Praecantatio 4 + Alienis 4), `:3` Wardencloth (Tecido Encantado + Praecantatio 8 +
  Pannus 8), `:4` Crimsoncloth (Telum 8 + Pannus 8), `:5` Voidcloth (Alienis 8 + Pannus 8; sem
  uso nesta versão).
* `itemWandCore:0` (infusão: Haste de Silverwood + pétala, 2 wardencloth, 2 salis, cristal; 20
  Excubitor) → `itemWaslieHammer` (Arcana 50/primal) → `itemWardenWeapon`, `itemWardenBow`,
  `itemWardenHelm/Chest/Legs/Boots` (Arcana 125/primal; 5/7/6/4 quartzos + 1 cristal),
  `itemWardenAmulet` (250/primal), vestes de wardencloth, `itemFocusPurity` (Foco de Proteção +
  4 gosmas + 4 vinhas maculadas), `itemFocusIllumination` (Foco de Fogo + 4 Nitor),
  `itemCrimsonHat` (cartola de golem + 4 crimsoncloth), blocos de quartzo infundido.
* O martelo aplica um aspecto (frasco/jarro de essência na GUI) às armas, armaduras e amuleto:
  Ignis, Aer, Terra, Aqua, Ordo, Perditio, Excubitor (efeitos conferidos nas classes
  `WardenicUpgrade*`; Perditio pode dar Warp).

## Materiais e geração de mundo

Só a Rosa Guardiã (Mundo Normal, sobre grama). Itens sem receita no NEI não foram pedidos:
Knowledge Reprocessor, Wardenic Light, armadura Thauminite Fortress (thaumrev), Ring of Love
(cai 25% das vezes de animais domados que morrem).

## Integrações no pack

* Everfull Urn enche Crisol, Spa Arcano, Apotecário de Pétalas (Botania), caldeirões vanilla e
  do Witchery (todas as opções `true`).
* Replicador aceita tábuas de Greatwood/Silverwood e madeiras/pedras de outros mods (config);
  73 receitas no handler NEI "Thaumic Replication".
* Thaumic Revelations: integração com Thaumic Bases ativa (receita extra de tingimento de
  armadura). Thaumic Dyes não está no pack, então valem as receitas próprias de Crimsoncloth
  e robes do culto.

## Armadilhas e dicas

* Crucible of Souls gera muito fluxo; não ataca jogadores nem golems, mas ataca pets.
* Selos só vinculam baús/jarros **vazios**; jarros com rótulo e Void Jars não aceitam.
* Armas/armaduras dos Guardiões exigem varinha/cajado com capacidade ≥ 125 por primal; o
  amuleto, 250.
* Perditio nas armas dos Guardiões pode adicionar Warp (a você ou ao alvo jogador).

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `texploration:tanque_pensante` | milestone | Pesquisa automatizada (Think Tank). |
| `texploration:replicador` | milestone | Replicador Táumico (essência → blocos). |
| `texploration:braseiro_almas` | milestone | Braseiro de Almas: Warp permanente guardado. |
| `texploration:armadura_guardiao` | milestone | Armadura completa dos Guardiões Táumicos. |
| `texploration:explorador_completo` | milestone | **Marco final** do capítulo. |
