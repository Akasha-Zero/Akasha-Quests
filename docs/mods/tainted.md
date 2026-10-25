# Tainted Magic 7.7.8-GTNH — estudo do capítulo `tainted`

## Visão geral

* `Tainted-Magic-7.7.8-GTNH.jar` (modid `TaintedMagic`). Capítulo `quests/chapters/magia/tainted.yaml`,
  `order: 507`, 53 quests. Raiz `pagina_rasgada` (`deps_hidden: [thaumcraft:crisol]`); dependências
  cruzadas com `thaumcraft:*` e `thaumcraft2:*` (infusão, vazio, pérola, Fortaleza, epifania).
* Fontes: registro, NEI (saídas), decompilação CFR (`RecipeRegistry`, `ResearchRegistry`), lang
  (`tm.text.*`), `config/TaintedMagic.cfg`. Aba própria "TM"; a raiz "A Disturbing Find" é auto-desbloqueada.
* Papel: Ato III/IV, magia de Warp; contribui para o Selo Arcano.

## Progressão (ids reais)

* **Metal das Sombras:** Minério `BlockShadowOre` = pedra no Crisol (2 Vacuos, 4 Tenebrae, 6 Metallum,
  8 Praecantatio) — não gera no mundo; fornalha → `ItemMaterial:0` (ingotShadow); bloco; ferramentas;
  Tecido das Sombras `ItemMaterial:1`; Óculos Deformados / do Vazio; Foco Maça do Mago.
* Pontas: Pano `ItemWandCap:1` (5%), Sombra-pano `:3` (10%), Carmesim `:2` (15%), Metal das Sombras `:0` (30%).
* Estilhaços Deformado `ItemMaterial:3` (+4 Alienis) e Fluxado `:4` (+4 Vitium) sobre Estilhaço Equilibrado;
  focos Estilhaço de Vis, Tempestade Maculada, Onda de Choque Maculada; Madeira Deformada (muda, tora,
  tábuas), Haste `ItemWandRod:0` (250, recarrega com Warp) e cajado `:1` (500; arcano 120 de cada primal).
* **Culto:** Adaga de Cristal → Sangue Cristalino `ItemMaterial:7` → Tecido Carmesim `:2` → vestes cultistas
  do Thaumcraft (arcano); Chapas `:8` (exige Epifania) → armadura de Cavaleiro → Pretor (exige Revelação);
  Sangue do Vazio `ItemVoidsentBlood` (auto-reparo); Lâmina Carmesim `Thaumcraft:ItemSwordCrimson`.
* Lâminas da Fortaleza `ItemKatana:0/1/2`; inscrições por NBT `{inscription:0|1|2}`.
* Fortaleza do Vazio, Fortaleza Sombria, Botas e Faixa do Andarilho, Liga Táumica `:6` → Desmontador.
* Criação: Estilhaço da Criação `ItemMaterial:5` (pérola + estrela + 6 cachos), focos Matéria Escura,
  Tempo e Meteorologia, Gume dos Primordiais `ItemPrimordialEdge`.

## Armadilhas e dicas

Warp por pesquisa (código): Shadowmetal 2, CAP_shadowmetal 5, Warpwood 3, Taint Focus 4, Creation Shard 5,
Creation 5, Crystal Dagger 2, Crimson Blade 3, Shadow Fortress 3, Primal Blade 4, Shadow katana 6, Inscrição
morto-vivo 5 etc. (algumas chaves no código não batem com as chaves de pesquisa, p.ex. "CAPSHADOWMETAL" vs
`CAP_shadowmetal` — o Warp real dessas pode não ser aplicado; as quests avisam pelos valores do código).

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `tainted:culto_reconstruido` | milestone | Armaduras e Lâmina Carmesim forjadas. |
| `tainted:mestre_maculado` | milestone | **Marco final** (Gume, pontas e haste de sombras) → Selo Arcano. |
