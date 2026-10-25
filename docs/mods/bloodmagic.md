# Blood Magic: Alchemical Wizardry — estudo do capítulo `bloodmagic`

## Visão geral

* **Mod:** `BloodMagic-1.9.12.jar` (fork GTNH; modid/namespace `AWWayofTime`, versão `1.9.12`).
* **Capítulo:** `quests/chapters/magia/bloodmagic.yaml`, `order: 540`, 171 quests.
* **Papel na campanha:** magia do Ato II (Altar T1–T4, rituais, alquimia, Armadura Vinculada) que
  se estende ao Ato IV (T5 com Sinalizadores/Wither, Portal Demoníaco, T6 e Orbe Transcendente).
  O marco final alimenta o **Selo Arcano**.
* Sem tradução pt_BR no JAR: os nomes aparecem em inglês no jogo; o capítulo cita o nome em
  inglês ou uma tradução explicativa.

Fontes: registro do pack (`reg.py mod AWWayofTime`), catálogo NEI (`Blood Altar`, `Binding Ritual`,
`Alchemic Chemistry Set`, `Shaped/Shapeless Orb Crafting`, `Meteor Ritual`), código-fonte do
fork GTNH na tag `1.9.12` (`AlchemicalWizardry.java`: receitas de altar/bancada/alquimia/rituais;
`UpgradedAltars.java`: estruturas; `TEAltar.java`: efeito das runas; `RitualEffectDemonPortal`,
`DaggerOfSacrifice`, eventos de drop), lang do JAR (`nei.infopage.*`, diário do Arquiteto em
`alchemicalwizardrybooks`), `config/AWWayofTime.cfg` e `config/BloodMagic/meteors/`.
Não há scripts de CraftTweaker no pack: valem as receitas padrão.

## Energia / recursos-base

* **Essência Vital (Life Essence)** — fluido do altar (`lifeessence`). Altar base: 10 000 mB.
* **LP (Life Points)** na **Rede de Almas** do jogador, abastecida por um **Orbe de Sangue**
  vinculado colocado no altar. Teto por orbe: Fraco 5 k, Aprendiz 25 k, Mago 150 k, Mestre 1 M,
  Arquimago 10 M, Transcendente 30 M. Sem LP, itens cobram vida / causam náusea.
* Fontes de LP (config): faca 200 LP/coração; adaga 500 LP base (animais 250, valores
  customizados por mob na config); Poço do Sofrimento 10 LP/golpe; Faca Emplumada 100;
  incenso (Cinza 40 % → Purpura 350 %, drena até 10 % da vida, dá Soul Fray).

## Progressão (ids reais)

| Tier | Estrutura (código) | Transmutações-chave (NEI) |
| --- | --- | --- |
| 1 | `AWWayofTime:Altar` | Diamante → `weakBloodOrb` (2000), Pedra → `blankSlate` (1000), balde → `bucketLife` |
| 2 | 8 runas, anel 3×3, y−1 | `reinforcedSlate` (2000), `apprenticeBloodOrb` (esmeralda, 5000), `daggerOfSacrifice`, `alchemyFlask`, `blankSpell` |
| 3 | +20 runas (7×7, y−2), 4 pilares 2 blocos + tampas (pedra luminosa e equivalentes da config) | `imbuedSlate` (5000), `magicianBloodOrb` (bloco de ouro, 25 000), 4 tintas, `activationCrystal`, `bloodSocket` (30 000) |
| 4 | +28 runas (11×11, y−3), pilares 4 + `largeBloodStoneBrick` | `demonicSlate` (15 000), `masterBloodOrb` (fragmento fraco, 40 000), focos de teleposição, tinta Crepúsculo |
| 5 | +52 runas (17×17, y−4), 4 sinalizadores (vanilla/etfuturum/chisel) | `bloodMagicBaseItems:27` Lousa Etérea (30 000), `archmageBloodOrb` (fragmento demoníaco, 75 000) |
| 6 | +76 runas (23×23, y−5), pilares 7 + `blockCrystal` | `transcendentBloodOrb` (aglomerado, 200 000), `dawnScribeTool` (pedra luminosa, 20 000) |

Runas (efeito real deste fork, `TEAltar`/NEI): Velocidade +25 %; Sacrifício/Autossacrifício
+12 %; Capacidade +3500 LP; Capacidade Superior ×1,14; Deslocamento ×1,5 vazão; Orbe +4 %;
Aceleração −1 tick (máx. 19); Rapidez −20 % na pausa entre receitas. A **Efficiency Rune** é
inerte e sem receita.

## Materiais e geração de mundo

Blood Magic não gera minérios. Itens de drop:
* **Fragmento de Sangue Fraco** — mob morto por jogador com Fraqueza III+ (Lâmina Vinculada), 50 %.
* **Fragmento de Sangue Demoníaco** — Elementais (convocados no Plinto Arcano com Orbe Mestre+).
* **Fragmentos da Vida/Alma** — Demon Grunts do Portal Demoníaco. 5 vida + 4 alma = Aglomerado.

## Máquinas, estruturas e rituais

* **Mesa Alquímica** (`blockWritingTable`): 5 ingredientes + orbe; nível mínimo do orbe por receita.
  Reagentes: Incendium, Aether, Sanctus, Crepitous, Crystallos, Terrae, Aquasalus, Tenebrae,
  Magicales (orbe 2); Offensa/Praesidium/Orbis Terrae/Virtus/Reductus/Potentia (orbe 3).
* **Rituais**: Pedra Mestra + Pedras Rituais pintadas; Cristal Fraco (nível 1) ou Desperto (nível
  2: Feathered Earth, Gaia, Condor, Falling Tower, Eternal Soul, Convocation, Omega, Duet, New Moon).
  Custos de ativação vêm da config (ex.: Full Spring 500, Well of Suffering 50 000, Condor 1 M,
  Convocation 15 M). Ritual of Binding: item jogado sobre a pedra (raios).
* **Meteoros** (Mark of the Falling Tower): ativação 100 000 + 1 000 000 por meteoro (padrão da
  config); focos: pedra, bloco de ferro, diamante, Estrela do Nether.
* **Armadura Vinculada**: Soquete Vazio → Preenchido (T3) → Forja da Alma, soquetes no formato da peça.
* **Feitiços complexos**: Paradigma → Efeito → Modificador → Aprimoramentos, ligados por Condutores,
  vinculados ao Cristal de Feitiço Complexo.
* **Plinto + 6 Pedestais**: convocação de Elementais/demônios (orbe nível 4–5 no plinto).

## Dimensões, chefes e mobs

* **Convocação dos Condenados**: exige Altar com "sangue demoníaco" sobre a Pedra Mestra, 8 Redomas
  de Cristal e AR de 8 reagentes. **Config do pack bloqueia a dimensão 0** (não funciona no Mundo
  Normal). Gera aldeia/hordas de Demon Grunts.
* Elementais (Ar, Água, Terra, Fogo, Sombra, Sagrado), Fallen Angel, Bile Demon, Winged Fire Demon,
  Boulder Fist, Ice Demon, Shade, Lower Guardian, Small Earth Golem.

## Integrações no pack

* **Thaumcraft**: Ritual of Binding transforma Óculos da Revelação e armadura de Thaumium em
  Armadura Sanguínea.
* **Blood Arsenal**: novos itens de altar/vínculo e a Pedra Luminosa Infundida vale como tampa T3.
* Tampas T3 aceitas pela config: glowstone, Blood Arsenal glowstone, Botania seaLamp, Chisel,
  Et Futurum sea lantern/shroomlight, ExtraUtilities lightgem. Sinalizadores T5: vanilla, Et Futurum, Chisel.

## Armadilhas e dicas

* Faca com pouca vida mata; incenso drena 90 % da vida de uma vez (Soul Fray).
* Orbe de Mago custa 25 000 LP — mais que a capacidade base do altar; manter o fluxo.
* T5 exige 4 Estrelas do Nether (Withers).
* Portal Demoníaco: perigoso, bloqueado no Overworld pela config.
* Ritual de Vínculo e meteoros soltam raios/explosões.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `bloodmagic:primeiro_sangue` | milestone | Altar + Orbe Fraco + Divinação (entrada no mod). |
| `bloodmagic:lousa_reforcada` | milestone | Altar de Tier 2. |
| `bloodmagic:reagentes_completos` | milestone | Os nove reagentes alquímicos. |
| `bloodmagic:altar_tier3` | milestone | Altar de Tier 3. |
| `bloodmagic:armadura_vinculada` | milestone | Armadura Vinculada completa. |
| `bloodmagic:altar_tier4` | milestone | Altar de Tier 4. |
| `bloodmagic:fragmento_demoniaco` | boss | Elementais derrotados (Fragmentos Demoníacos). |
| `bloodmagic:feitico_completo` | milestone | Sistema de feitiços complexos. |
| `bloodmagic:altar_tier5` | milestone | Altar de Tier 5 (sinalizadores). |
| `bloodmagic:ritual_meteoro` | milestone | Marca da Torre Caída (meteoros). |
| `bloodmagic:portal_demoniaco` | portal | Convocação dos Condenados / Portal Demoníaco. |
| `bloodmagic:orbe_transcendente` | milestone | Altar de Tier 6 + Orbe Transcendente. |
| `bloodmagic:mestre_sangue` | finale | **Marco final** — usado pelo **Selo Arcano**. |
