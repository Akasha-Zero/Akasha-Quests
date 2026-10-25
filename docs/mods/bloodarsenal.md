# Blood Arsenal — estudo do capítulo `bloodarsenal`

## Visão geral

* **Mod:** `BloodArsenal-1.5.12.jar` (fork GTNH; namespace `BloodArsenal`, versão `1.5.12`).
  Addon do Blood Magic.
* **Capítulo:** `quests/chapters/magia/bloodarsenal.yaml`, `order: 541`, 51 quests.
* **Papel:** apêndice do Volume do Sangue (Ato II–IV). Raízes com `deps_hidden` para quests do
  capítulo `bloodmagic` (orbes, tiers do altar, Ritual do Vínculo).
* Sem pt_BR no JAR; nomes em inglês no jogo.

Fontes: registro (`reg.py mod BloodArsenal`), NEI (`Blood Altar`, `Binding Ritual`, Orb Crafting),
código da tag `1.5.12` (`BloodArsenalRecipes.java`, `RitualRegistry.java`, `BlockCompacter.java`,
`ModLivingDropsEvent.java`, `BloodArsenalEventHooks.java`, integração Thaumcraft/TiC),
`config/BloodArsenal.cfg` e o lang do JAR.

## Energia / recursos-base

Usa LP/Essência Vital do Blood Magic. Config: Materializador converte 150 LP → 100 mB; Sigilo do
Ender 400 LP (baú) e ×500 (teleporte); Relâmpago ×1500; Natação 250; Divindade 1 000 000.

## Progressão (ids reais)

1. `glass_shard` (vidro quebrado com sílex na mão; "vidro é perigoso" = Sangramento), `blood_burned_string` (linha + Orbe Fraco).
2. `blood_infused_wood` (tora no altar T2, 3000) → tábuas, gravetos, ferramentas de madeira.
3. `blood_infused_iron` (lingote no altar T3, 6000; bloco 54 000), `amorphic_catalyst` (Lousa em Branco + 8 reagentes).
4. Ferramentas de ferro infundido (Orbe de Mago), `life_imbued_*` (100 000 LP armazenados), `life_infuser`, `lp_materializer`.
5. `blood_diamond` (Orbe Mestre) → `blood_infused_diamond_unactive` → altar T4 80 000 → `_active` → Ritual do Vínculo → `_bound`.
6. Ferramentas de diamante (machado/espada marcados [WIP]), `vampire_ring`, `compacter` (Orbe de Arquimago).
7. Sigilos: Natação (Aprendiz), Ender (Mago), Contenção Aumentada (Mestre), Relâmpago (Arquimago), Divindade (Transcendente).

Outros: `heart` (zumbi 1 % / aldeão 25 % / jogador, com Fraqueza) → `soul_fragment` (T4, 75 000) → `soul_booster`/`soul_nullifier`;
Vinculados (NEI): arco, enxada de diamante → foice, tesoura, isqueiro, couro → capa/grevas/botas de vampiro.
Altar: laranja (corante laranja, 200), biscoito (2000), glowstone (5000), TNT/bolo/papel (10 000).

## Materiais e geração de mundo

Nenhum minério. `wolf_hide` cai de lobos (40 %), sem receita que o use.

## Máquinas e estruturas

* **Soul Compacter**: colocado **abaixo** de uma Pedra Mestra com ritual montado → `compacted_mrs`
  (custo = ativação × 1,05/1,075 por pedra); abaixo de um Altar montado → `portable_altar`
  (orbe ≥ tier do altar, bacia vazia). **Consome o orbe segurado.**
* Rituais do addon: Withering (75 000, nível 1), Midas Touch (5000), Fisherman's Hymn (10 000),
  Enchantress's Spell (50 000, nível 2), Mob Oppression (25 000, nível 2). Nenhum na blacklist.

## Integrações no pack

* Thaumcraft/Forbidden Magic: pesquisas de Capas de Ferro Infundido e Núcleos de Madeira Infundida (com Warp).
* Tinkers' Construct: materiais Blood Infused Wood/Iron (IDs 250/251), modificador Soulbound (Orbe Mestre).
* Baubles: amuletos e anel.
* `blood_infused_glowstone` vale como tampa T3 do Altar de Sangue (config do Blood Magic).

## Armadilhas e dicas

* Quebrar vidro dá Sangramento; Matter Manipulator protege (config).
* O Compactador consome o orbe da mão.
* Itens [WIP]: machado e espada de diamante infundido.
* `portable_altar`, `compacted_mrs` e `bound_diamond_shard` não têm receita direta (os dois primeiros
  vêm do Compactador; os fragmentos não foram usados).

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `bloodarsenal:arsenal_ferro` | milestone | Ferramentas de ferro infundido. |
| `bloodarsenal:sigilo_divindade` | milestone | Sigilo da Divindade (Orbe Transcendente). |
| `bloodarsenal:arsenal_completo` | milestone | **Marco final** do addon (Compactador + diamante + armadura viva). |
