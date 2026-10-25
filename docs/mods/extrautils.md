# Extra Utilities — estudo do capítulo `extrautils`

## Visão geral

* **Mod:** Extra Utilities `extrautilities-1.2.13a.jar` (modid `ExtraUtilities`, versão reportada
  `1.2.12` em `reg.py mods`).
* **Capítulo:** `quests/chapters/tech/extrautils.yaml`, `order: 355`, 105 quests.
* **Papel:** utilidades de Ato I–II (Bloco Anjo, compressão, geradores, nós de transferência), com
  ramos de Ato III (Runa de Divisão, Lingote Instável, Escuro Profundo).

Fontes: registro, receitas do dump (inclui alterações do pack), catálogo NEI (`QED Recipes`, 254
receitas), `config/ExtraUtilities.cfg`, textos `*.documentation` do lang do JAR (manual embutido),
bytecode de `ExtraUtils`/`EnderConstructorRecipesHandler`.

## Energia / recursos-base

* RF. 12 geradores `ExtraUtilities:generator:0–11` (Sobrevivente, Fornalha, Lava, Fim, Redstone
  Aquecida, Culinária, Poções, Solar, TNT, Rosa, Alta Temperatura, Estrela do Nether) e versões
  `generator.8:*` (8 geradores + Nó de Energia) e `generator.64:*` (8 × x8 + Nó Híper).
* Ender-Flux (EF) do QED: Cristais de Ender-Fluxo (`endConstructor:2`) convertem 1 000 RF → 1 EF,
  máx. 0,08 EF/t cada, alcance de cubo 10.
* Pedreira do Fim: 1 800 RF/bloco + 200 RF × dureza (config `ender quarry power`).

## Progressão (ids reais)

1. Bloco Anjo `angelBlock`; Pedregulho Comprimido `cobblestone_compressed:0–7` (terra 8–11,
   cascalho 12–13, areia 14–15); Lata de Lixo `trashcan:0–2`; Regador `watering_can`; Abafa-Som
   `sound_muffler:0/1`; Barril `drum:0/1`; Sacola Dourada `golden_bag`; Laço `golden_lasso`;
   Arquivador `filing:0/1`; Lustre `chandelier`; Esteira `conveyor`; Espinhos `spike_base_wood`,
   `spike_base`, `spike_base_gold`, `spike_base_diamond`.
2. Decorativos-chave `decorativeBlock1`: :1 Obsidiana Fundida em Ender (4 obsidiana + pérola → 4),
   :2 Quartzo Queimado, :8 Madeira Mágica (+5 níveis), :11 Núcleo de Ender (+20 níveis),
   :12 Matriz Computacional (Quartzo Queimado + diamantes + obsidiana Ender), :5 bloco instável.
3. Transferência: Tubos `pipes:0/8–14`, `pipes.1`; Nós `extractor_base:0` (itens), `:6` (líquidos),
   `:12`/`:13` (energia, QED); Recuperação `extractor_base_remote:0/6` (QED); melhorias
   `nodeUpgrade:0–10` (5/6 transmissor/receptor = QED).
4. QED `endConstructor:0` (olhos do Ender, bancada, Matriz, obsidiana Ender). Itens forçados ao QED
   (`qedcrafting.QEDItems`): nós de recuperação, transmissor/receptor, Tocha Magnum, Marcador do Fim,
   nós de energia, Base de Melhoria da Pedreira. O QED também funde minério + carvão → 3 lingotes.
5. Pedreira do Fim `enderQuarry` (obsidiana Ender, muda, Núcleos de Ender, Matriz, 2 Bombas
   Ender-Térmicas, picareta de diamante) + melhorias `enderQuarryUpgrade:0–9`.
6. Runa de Divisão `divisionSigil` (só baús de masmorra; ativada NBT `damage:256`, estável
   `stable:1b`) → Lingote Instável `unstableingot:0` (ferro + runa + diamante; 10 s; explode,
   `unstableIngotsExplode=true`), Pepita Semi-Estável `:1` (9 = lingote estável), ferramentas
   instáveis, Vidro Etéreo, Varinha do Construtor, Anel de Anjo (estrela do Nether).
7. Bedrockium `bedrockiumIngot` (no pack: 4 Triplos + 4 Quádruplos comprimidos + bloco de diamante;
   Octuplo na fornalha → `block_bedrockium`).
8. Portais `dark_portal:0` (4 Quádruplos + 4 Lingotes Instáveis + 1 Quíntuplo) e `dark_portal:2`
   (Quartzo Queimado + Pedra Eminência + relógio).

## Dimensões

* **Escuro Profundo** — dim **−100** (`deepDarkDimensionID`, provedor `Underdark`). Mobs nascem com
  qualquer luz; Lustre e Tocha Magnum impedem spawn.
* **Último Milênio** — dim **−112** (`lastMilleniumDimensionID`, `WorldProviderEndOfTime`): vazio.

## Ritual de Ativação (lang `divisionSigil.documentation.*`)

Mesa de encantamento sobre terra/grama com céu aberto e muita terra em volta; 8 redstone em quadrado
ao redor (sobre terra); meia-noite (a runa brilha); área escura; matar um animal/mob sobre ou ao lado
da mesa com a runa no inventário. Gera consequências e Terra Amaldiçoada. Estabilização: agachado +
clique num farol com a runa ativada (ritual complexo, instruções no jogo).

## Integrações no pack

* Energia RF compatível com todos os mods; nós de energia aceitam RF.
* MFR Bio-Reactor/Matter Overdrive aparecem no NEI produzindo sementes de Ender Lily.

## Armadilhas e dicas

* **Quartzo Queimado sem receita no pack (problema!)**: a receita original (fornalha: Bloco de Quartzo
  → Quartzo Queimado) é sobrescrita pelo Et Futurum (Bloco de Quartzo → Quartzo Liso). NEI e o dump de
  fornalha não mostram nenhuma fonte de `ExtraUtilities:decorativeBlock1:2`. Isso bloqueia: Matriz
  Computacional → **QED** e **Pedreira do Fim**; Base de Melhoria da Pedreira; **Portal do Último
  Milênio**; e, via QED, Nós de Energia, geradores x8/x64, Nós de Recuperação, Tocha Magnum, Marcador
  do Fim, Transmissor/Receptor. O capítulo mantém essas quests com aviso honesto na quest
  `quartzo_queimado`. **Sugestão ao coordenador:** adicionar via MineTweaker uma receita alternativa
  (ex.: bancada ou outra máquina) para `<ExtraUtilities:decorativeBlock1:2>`.
* Mesa do Pacífico só funciona em dificuldade Pacífica (`peacefulTableInAllDifficulties=false`).
* Bomba Ender-Térmica: por padrão só funciona no Nether.
* Fragmento de Alma (`mini-soul`, receita "RecipeSoul", handler NEI vazio) e Regador Reforçado não
  entraram no capítulo: mecânica não confirmada (a config `soulDrainResetsOnDeath=false` sugere
  perda permanente de vida).
* Kikoku (`lawSword`) não tem receita (não entrou).

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `extrautils:banco_geradores` | milestone | Cinco geradores do ExU funcionando (Ato I–II). |
| `extrautils:rede_transferencia` | milestone | Nós e tubos de transferência com organização (Ato II). |
| `extrautils:pedreira_fim` | milestone | Pedreira do Fim (depende do Quartzo Queimado — ver problema acima). |
| `extrautils:escuro_profundo` | portal | Visitar o Escuro Profundo (dim −100). |
| `extrautils:ultimo_milenio` | portal | Visitar o Último Milênio (dim −112; depende do Quartzo Queimado). |
| `extrautils:mestre_utilidades` | milestone | **Marco final** (Escuro Profundo, rede, geradores, Varinha, Bedrockium) — sem dependência do Quartzo Queimado. |
