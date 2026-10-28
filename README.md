# Akasha Quests — *Crônicas Akáshicas*

Livro de quests (BetterQuesting **3.8.79-GTNH**) do modpack **Technic Forbidden (TEOF 4.0)**,
Minecraft 1.7.10, da Rede Akashic.

Uma campanha narrada por **Akasha**, a Guardiã do Registro Akáshico, que atravessa mais de
200 mods de tecnologia, magia e aventura — dos primeiros gravetos até a **Estrela ATM**
(*pseudo-final*) e, depois dela, o **Infinito** do Avaritia.

**8.939 quests em 102 capítulos** (lista completa em [docs/CAPITULOS.md](docs/CAPITULOS.md)),
validadas no BetterQuesting real: nenhum item, entidade, fluido ou dimensão inexistente no pack.

## A campanha

1. **Prólogo — O Despertar** e os **Atos I–IV** (*Crônicas Akáshicas*): a história que liga os
   Volumes de mod. O Ato III reúne as Chaves dos **Três Reinos** (Twilight Forest, Aether,
   Atum); o Ato IV exige os **Oito Selos** (Crepúsculo, Céus, Areias, Abismo, Estrelas,
   Arcano, Engrenagem, Vida), cada um ligado aos marcos finais reais dos capítulos.
2. **Volumes de mod** (Base, Aventura, Tecnologia, Magia, Vida): livres desde o início.
3. Série **AEDF3:** — 17 capítulos com a progressão da lendária *A Era do Futuro*.
4. Série **ATM:** — Allthemodium, Compressed, Tweaks e a **Estrela ATM** (pseudo-final),
   liberada pelos Oito Selos.
5. **Epílogo — Além do Infinito** (Avaritia, Singularidades) e **A Última Página**.

## Patches recomendados para o pack

Problemas reais do modpack encontrados durante o estudo (detalhes em `patches/`):

| Patch | Efeito | Seguro em mundo existente? |
| --- | --- | --- |
| 01 — IDs de bioma ChromatiCraft × Twilight (47–51) | Sem ele, a Floresta de Neve e a Geleira do Crepúsculo não geram (Rainha da Neve impossível) | Não: mundo novo ou com backup |
| 02 — Quartzo Queimado (script CraftTweaker) | Restaura QED, Pedreira do Fim e Último Milênio do Extra Utilities | Sim |
| 03 — Circuito de Controle do MPS (script CraftTweaker) | Libera módulos do Modular Powersuits | Sim |

## Interface estilo FTB Quests (opcional)

O mod **Akasha Quests UI** (`mod/`) troca as telas do BetterQuesting por uma interface no estilo
do FTB Quests: barra de capítulos com grupos, mapa com formas e linhas animadas, janela da quest
sobre o mapa, imagens nos capítulos e um modo de edição para posicionar quests e imagens. É só
cliente (o servidor não precisa dele) e usa os próprios painéis e pacotes do BQ.

Instale `dist/mods/akashaquestui-1.0.2.jar` na pasta `mods/` do cliente. Detalhes em
[mod/README.md](mod/README.md); a especificação de design em [mod/docs/DESIGN.md](mod/docs/DESIGN.md).

O GitHub Actions (`.github/workflows/build.yml`) compila e testa o mod e valida o livro a cada push
na `main` e em pull requests. O jar sai como artefato `.jar` (sem zip) na página da execução; uma tag
`v*` publica o jar numa release.

## Instalação

Copie o conteúdo de `dist/config/betterquesting/` para `config/betterquesting/` do servidor
(e do cliente, se jogar em singleplayer). Com `Load the default quest DB on world startup`
ativo (padrão do pack), o livro é carregado automaticamente; em um mundo já existente, um
operador pode rodar `/bq_admin default load`.

* `DefaultQuests/` — o banco de quests (formato multi-arquivo do BQ GTNH).
* `DefaultLoot.json` — os grupos dos Baús de Saque (Comum → Lendário). É lido apenas na
  primeira carga de um mundo; mundos existentes mantêm o `QuestLoot.json` deles.
* `saved_quests/AkashaQuests.json` — o mesmo livro em arquivo único (22 MB), para carregar por comando.

### Carregar por comando (`saved_quests`)

1. Copie `dist/config/betterquesting/saved_quests/AkashaQuests.json` para
   `config/betterquesting/saved_quests/` do servidor.
2. Como operador, rode `/bq_admin default load AkashaQuests` — substitui o banco de quests do
   mundo atual (o progresso é mantido, pois os UUIDs são fixos).
3. Opcional: `/bq_admin default set AkashaQuests` o grava como `DefaultQuests.json` dos mundos
   novos. O BQ só usa esse arquivo se a pasta `DefaultQuests/` não existir.

Regenere com `python3 tools/bqgen/export_saved.py` após um `build` (`--folder` gera também a
versão multi-arquivo `saved_quests/AkashaQuests/`, que o BQ prefere quando existe).

## Estrutura do repositório

| Caminho | Conteúdo |
| --- | --- |
| `quests/chapters/` | **Fonte** das quests, em YAML legível (um arquivo por capítulo). |
| `quests/loot/loot.yaml` | Conteúdo dos Baús de Saque. |
| `docs/AUTHORING.md` | Guia do formato YAML (tarefas, recompensas, tipos, layout). |
| `docs/design/` | Bíblia narrativa, mapa de capítulos, economia de recompensas. |
| `docs/mods/` | Estudo técnico de cada mod (progressão, materiais, estruturas, chefes). |
| `docs/research/` | Pesquisa de referência: ATM10 e A Era do Futuro 3. |
| `registry/` | Dump real do registro do modpack (itens, metas, nomes pt_BR, oredict, entidades, fluidos, dimensões, receitas). |
| `tools/bqgen/` | Gerador, validador e consulta ao registro. |
| `tools/dumpmod/` | Fonte do mod que extrai o registro de dentro do jogo. |
| `mod/` | Akasha Quests UI: interface estilo FTB Quests para o BQ (Forge 1.7.10, só cliente). |
| `dist/` | Saída gerada — não editar à mão. |

## Gerar

```bash
pip install pyyaml
python3 tools/bqgen/bqgen.py lint     # valida todos os IDs contra o registro real
python3 tools/bqgen/bqgen.py build    # gera dist/config/betterquesting
python3 tools/bqgen/bqgen.py stats    # quests por capítulo
```

Ids de capítulo e de quest são **permanentes**: o UUID de cada quest é derivado deles, então
o progresso dos jogadores sobrevive a qualquer edição de texto, recompensa ou layout.
