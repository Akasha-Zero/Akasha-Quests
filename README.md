# Akasha Quests — *Crônicas Akáshicas*

Livro de quests (BetterQuesting **3.8.79-GTNH**) do modpack **Technic Forbidden (TEOF 4.0)**,
Minecraft 1.7.10, da Rede Akashic.

Uma campanha narrada por **Akasha**, a Guardiã do Registro Akáshico, que atravessa mais de
200 mods de tecnologia, magia e aventura — dos primeiros gravetos até a **Estrela ATM**
(*pseudo-final*) e, depois dela, o **Infinito** do Avaritia.

## Instalação

Copie o conteúdo de `dist/config/betterquesting/` para `config/betterquesting/` do servidor
(e do cliente, se jogar em singleplayer). Com `Load the default quest DB on world startup`
ativo (padrão do pack), o livro é carregado automaticamente; em um mundo já existente, um
operador pode rodar `/bq_admin default load`.

* `DefaultQuests/` — o banco de quests (formato multi-arquivo do BQ GTNH).
* `DefaultLoot.json` — os grupos dos Baús de Saque (Comum → Lendário). É lido apenas na
  primeira carga de um mundo; mundos existentes mantêm o `QuestLoot.json` deles.

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
