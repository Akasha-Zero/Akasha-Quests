# Thaumic Energistics, Boots, Insurgence e WarpTheory — estudo do capítulo `thaumextras`

## Visão geral

* **Mods:** Thaumic Energistics `thaumicenergistics-1.7.58-GTNH` (namespace `thaumicenergistics`),
  Thaumic Boots `thaumicboots-1.5.12` (`thaumicboots`), Thaumic Insurgence `thaumicinsurgence-0.4.1`
  (`thaumicinsurgence`) e WarpTheory `WarpTheory-1.5.7-GTNH` (`WarpTheory`). Addons do Thaumcraft
  4.2.3.5 e, no caso do ThE, do AE2.
* **Capítulo:** `quests/chapters/magia/thaumextras.yaml`, `order: 513`, 52 quests em 4 partes.
* **Papel:** Ato III (rede ME + infusão automatizada, botas combinadas) e o "remédio" do Warp do Ato
  III/IV. Peça do **Selo Arcano**.

Fontes: `reg.py mod/items/research/entity`, NEI (handlers do TC só com saídas), lang en_US dos
quatro JARs, configs `thaumicenergistics.cfg`, `WarpTheory.cfg`, `thaumicboots.cfg`,
`thaumicinsurgence.cfg` e classes descompiladas (`thaumicenergistics.common.features.*`,
`CommonDependantItems`, `AEPartsEnum`; `thaumicboots.main.utils.compat.ThaumcraftHelper`, botas;
`thaumicinsurgence.main.utils.compat.ThaumcraftHelper`, sabões, BIGSHOT, lente;
`shukaro.warptheory.recipe.WarpRecipes`, `research.WarpResearch`, `handlers.WarpHandler`, itens).

## Energia

* ThE não tem energia própria: usa a da rede AE2. A **Câmara de Vibração de Essência** queima
  Ignis/Potentia (o Taumonomicon estima ~350% a mais que queimar carvão).
* O Montador Arcano gasta +15% de energia por ponto de Warp do equipamento colocado nele; cartões
  de aceleração: −20% tempo, +50% energia cada.

## Progressão (ids reais)

### Thaumic Energistics
* Raiz `TERESEARCH` auto-desbloqueada. Pesquisas filhas exigem as do TC (DISTILESSENTIA,
  TUBEFILTER, INFUSION, VISPOWER, SCEPTRE, COREUSE, COREGATHER, ALCHEMICALDUPLICATION, FOCUSFIRE).
* Núcleos: `material` Difusão (3 Gotas de Mercúrio + Estilhaço de Entropia + Núcleo de
  Aniquilação), `material:1` Coalescência (… + Ordem + Núcleo de Formação). Arcana sem formato.
* Células: caixa `storage.casing` (Vidro Guardião/redstone/táumio, bancada); componentes
  `storage.component:0-3,5-8` (Arcana; 1k: Essência Etérea + Certus + Proc. Lógico; 4k: Proc.
  Cálculo + Vidro de Quartzo; 16k/64k: Salis + Proc. Engenharia; 256k: Vidro Guardião; 1024k:
  Sementes do Vazio; 4096k: Metal do Vazio; 16384k: Amuletos Primais + Reservatório);
  células `storage.essentia:0-3,5-8`, quântica `:9` (Anéis Quânticos + Célula Densa), singularidade
  `:10` (8 quânticas + Condensador). `:4` (Thaumometric) sem receita.
* Barramentos `part.base` (import), `:3` (export), `:2` (storage). Terminal Arcano `:5`
  (Terminal ME + Mesa Arcana + Proc. Cálculo), Interface de Vis `:6` (Estilhaço Equilibrado + P2P ME).
* Infusões: Provedor de Essência e de Infusão (sobre Interface ME; inst. 3 e 4), Provedor de
  Infusão Avançado (sobre o Provedor; inst. 10; 64 de cinco aspectos), Montador Arcano (sobre
  Montador Molecular + Cetro de Silverwood carregado + 7 estilhaços; inst. 7).
* Outros: Inscritor + Núcleo de Conhecimento, Codificador de Destilação, Caixa de Engrenagens de
  ferro (Arcana) e de táumio (Crisol), Mochila Sem Fio de Golem, Foco Chave do AE, Microscópio de
  Células (300 de Aer/Terra/Ordo), duplicação de Certus/Quartzo no Crisol.

### Thaumic Boots (só os módulos TC/Exploration/Tainted; EMT não está no pack)
* Aspectos novos: `tabernus` (Tutamen + Iter) e `caelum` (Vitreus + Metallum).
* Lente de Arcânio `item.ItemArcaniumInterfacer` (Crisol: táumio + 25 Tabernus/Permutatio/Caelum)
  é o centro das infusões combinadas (inst. 0): `ItemCometMeteor`, `ItemMeteoricComet` (Comet +
  Meteor do TX), `ItemVoidMeteor`, `ItemVoidComet` (Voidwalker do Tainted + bota do TX).
* `ItemSlowBoots` (Crisol sobre Botas do Viajante), `ItemSeasonBoots` (infusão inst. 10).
  `ItemChristmasBoots` só tem receita se o servidor iniciar no Natal (`CalendarHelper`).

### Thaumic Insurgence
* `infusionIntercepter` (infusão inst. 10 sobre Construto Alquímico; pesquisa +4 Warp): vai embaixo
  do pedestal central e recebe essência pela face inferior.
* `item.ItemThaumicInterfacer` Lente do Louco (mostra instabilidade da matriz e primais do nodo).
* Sabões `ItemSanitySoapAlpha` (50% de −1 Warp normal; +25% Warp Ward, +25% Fluido Purificador;
  zera o temporário) e `ItemSanitySoapBeta` (10%/+15%/+15%: 1 permanente → 2 normal; falha = +2
  temporário). `ItemBIGSHOT` (Óculos no Crisol; 8% desconto, mostra nodos, 5 Warp).
* Mármore/pilar/matriz/pedestal, Coroa Vermelha e Varinha de Alastor: sem receita.

### WarpTheory (`wussMode=false` → receitas caras)
* Papel de Tornassol (Arcana Aqua 8), Lágrima Impura (Estrela do Nether + 2 Salis/Ghast/diamante/
  quartzo; age só com ≥50 de Warp, −5), Lágrima Pura (Estrela + 2 Impuras…; zera tudo, mas dispara
  vários eventos que ignoram Warp Ward), Talismã de Purificação (Amuleto Mundano + 2 Puras…; inst.
  10), Hunk of Something (Crisol, carne; dá Warp), Poção do Doce Esquecimento (Arcana 25/primal;
  auto-desbloqueada; zera Warp **e todas as pesquisas**).

## Dimensões/chefes

Nenhuma. Entidades do WarpTheory (`WarpTheory.doppelganger`, `phantom`, `creeperFake`,
`creeperPassive`, `taintSheepSafe`) só surgem como eventos; nenhuma quest pede para matá-las.

## Integrações

* AE2 (`ae2`): núcleos, interface, montador molecular, terminais, P2P, anéis quânticos.
* Thaumic Exploration (botas do Meteoro/Cometa), Tainted Magic (Botas do Andarilho do Vazio).
* Thaumic Insurgence + ThE: o Interceptador deixa o Provedor de Essência alimentar a infusão.
* `thaumcraft2:warp`/`sintomas_warp` já apontam para este capítulo.

## Armadilhas

* **Chuveiro Portátil** (`item.warptheory.portableshower`) é incraftável: a receita registrada em
  `WarpRecipes` produz o Talismã por engano. Não foi pedido.
* Terminal de Essência, Emissor de Nível e Monitores do ThE estão marcados DEPRECATED.
* Lágrima Pura dispara eventos inevitáveis; Poção do Esquecimento apaga as pesquisas.
* Botas do Vazio combinadas e BIGSHOT dão 5 de Warp cada enquanto vestidos.
* Eventos desligados no pack: Chest, Deaf, EyeBlink, Mute, WorldHole, ServerError. O pior ligado é
  o Wither de verdade a partir de 200 de Warp.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `thaumextras:provedor_infusao` | milestone | Altar de infusão alimentado pela rede ME. |
| `thaumextras:montador_arcano` | milestone | Fabricação arcana automática. |
| `thaumextras:sapateiro_mestre` | milestone | Botas combinadas do Vazio (Meteoro e Cometa). |
| `thaumextras:talisma_purificacao` | milestone | Talismã de Purificação (remoção contínua de Warp). |
| `thaumextras:mestre_extras` | milestone | **Marco final** — candidato ao Selo Arcano. |
