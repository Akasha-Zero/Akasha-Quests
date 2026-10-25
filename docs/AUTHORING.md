# Guia de Autoria de Quests (formato YAML → BetterQuesting)

Toda quest do Akasha Quests é escrita em YAML em `quests/chapters/**.yaml` e compilada por
`tools/bqgen/bqgen.py` para o formato nativo do **BetterQuesting 3.8.79-GTNH**
(`config/betterquesting/DefaultQuests/`). Nunca edite `dist/` à mão: ela é gerada.

```bash
python3 tools/bqgen/bqgen.py lint quests/chapters/tech/ic2.yaml   # valida um capítulo
python3 tools/bqgen/bqgen.py lint                                 # valida tudo (modo estrito)
python3 tools/bqgen/bqgen.py build                                # gera dist/config/betterquesting
python3 tools/bqgen/bqgen.py stats                                # contagem por capítulo
python3 tools/bqgen/reg.py items "cobre|copper" --mod IC2          # procura IDs reais no registro
```

O lint valida **cada item, meta, oredict, entidade, fluido, dimensão e bioma** contra o dump
do registro feito com o próprio modpack em execução (`registry/`). Se o lint passa, o ID existe
no jogo. Se não existe, a quest não compila — não "chute" IDs: pesquise com `reg.py`.

## 1. Estrutura de um capítulo

```yaml
chapter:
  id: ic2                       # slug único e PERMANENTE (gera o UUID; nunca renomeie)
  title: "IndustrialCraft 2"    # nome exibido (sem prefixo)
  prefix: ""                    # "" (nosso), "ATM" -> "ATM: ...", "AEDF3" -> "AEDF3: ..."
  order: 310                    # posição na lista de capítulos (ver docs/design/MAPA.md)
  icon: IC2:itemToolWrench
  desc: |
    Texto curto que aparece ao passar o mouse no capítulo.
quests:
  - id: macerador               # slug único dentro do capítulo e PERMANENTE
    title: "Macerador"
    type: main                  # ver tabela de tipos
    icon: IC2:blockMachine:3    # opcional; padrão = primeiro item das tarefas
    deps: [gerador]             # pré-requisitos; outro capítulo: "ic2:gerador"
    desc: |
      Texto da quest (ver docs/design/NARRATIVA.md para o tom).
    tasks:
      - get: IC2:blockMachine:3
    rewards:
      - item: IC2:itemDust:4 x8
      - loot: incomum
```

Uma entrada `- link: outrocapitulo:quest` (com `at: [col, lin]` opcional) mostra uma quest
de outro capítulo neste mapa (como ponte visual), sem duplicá-la.

## 2. Campos da quest

| Campo | Significado |
| --- | --- |
| `id` | slug `[a-z0-9_]`, único no capítulo. Mudar o id = quest nova (perde progresso). |
| `title` | Nome exibido. Cor automática por tipo; `color: "§a"` sobrescreve. |
| `type` | `normal`, `main`, `optional`, `info`, `milestone`, `boss`, `portal`, `finale`, `secret`. |
| `deps` | Lista de pré-requisitos (desenha linha). |
| `deps_any: true` | Basta concluir **um** dos `deps` (lógica OR). |
| `deps_hidden` | Pré-requisitos sem linha desenhada (para pontes entre capítulos). |
| `visibility` | `normal` (padrão), `unlocked`, `hidden`, `secret`, `chain`, `always`. |
| `task_logic` | `AND` (padrão) ou `OR` entre as tarefas. |
| `at: [col, lin]` | Posição manual na grade (coluna 44px, linha 40px). Opcional: há layout automático em camadas. |
| `size` | Tamanho do ícone (padrão por tipo). |
| `banner` / `subtitle` | Título/subtítulo grande da notificação ao concluir (use em chefes e marcos). |
| `effect` | Efeito cinematográfico: 3 hiperespaço, 4 singularidade, 5 warp, 6 velocidade da luz. |
| `repeat` | Segundos para repetir (quests diárias); padrão não repete. |
| `auto_claim: true` | Recompensa entregue automaticamente. |

Tipos: `main` = passo da trilha principal (32px); `optional` = desvio opcional; `info` = só
explicação (normalmente `- check`); `milestone` = marco de capítulo (40px, fogos);
`boss` = chefe (40px, fogos); `portal` = nova dimensão (warp); `finale` = fim de grande arco.

## 3. Especificação de item

```
mod:nome                 meta 0, quantidade 1
mod:nome:5               meta 5
mod:nome:*               qualquer meta (curinga 32767)
mod:nome:5 x16           16 unidades
ore:ingotCopper x8       qualquer item do OreDictionary "ingotCopper"
minecraft:diamond_sword x1 {ench:[{id:16s,lvl:3s}],display:{Name:"Lâmina do Herói"}}
```

Forma longa: `{id: mod:nome, meta: 5, count: 16, nbt: "{...}"}` — **obrigatória quando o id
tem espaço** (ex.: `{id: "BiblioCraft:Armor Stand"}`; o `reg.py` já mostra nesse formato). NBT usa a sintaxe de
comando do Minecraft (sufixos `b s L f d`, listas `[...]`, `[I;1,2]` para int array).
Prefira **oredict** quando qualquer variante servir (cobre de IC2/Thermal/Mekanism etc.).

## 4. Tarefas

| Tarefa | Exemplo | Observação |
| --- | --- | --- |
| `get` | `- get: [IC2:itemCable x8, ore:ingotTin x4]` | Ter no inventário (não consome). |
| `consume` | `- consume: minecraft:diamond x4` | Entregar (consome). Use com moderação. |
| `craft` | `- craft: IC2:blockMachine:3` | Fabricar/fundir o item. |
| `one_of` | `- one_of: [a, b, c]` | Basta ter um dos itens. |
| `optional` | `- optional: x` | Item opcional (não bloqueia). |
| `kill` | `- kill: "TwilightForest.Naga x1"` | Id de entidade de `reg.py entity`. |
| `visit` | `- visit: {dim: 7, name: "Floresta do Crepúsculo"}` | `biome`, `x y z range` opcionais. |
| `fluid` | `- fluid: "water x1000"` | Fluido em tanque/container do inventário. |
| `xp` | `- xp: 30` ou `{levels: 30, consume: true}` | |
| `check` | `- check` | Caixa de seleção (leitura/confirmação). |

Regras de ouro: **toda quest precisa de pelo menos uma tarefa verificável sempre que possível**
(itens > checkbox). Checkbox só para quests `info`, capítulos-tutorial e ações impossíveis de
verificar (ex.: "monte a estrutura multibloco" — então peça um item que a estrutura produz).

## 5. Recompensas

| Recompensa | Exemplo |
| --- | --- |
| `item` | `- item: [minecraft:iron_ingot x16, IC2:itemBatREDischarged]` |
| `choice` | `- choice: [a x4, b x4, c x2]` (jogador escolhe um) |
| `xp` | `- xp: 5` (níveis) |
| `loot` | `- loot: raro` ou `- loot: [comum x2, epico]` — Baús de Saque por raridade |
| `kit` | `- kit: {name: "Kit do Ferreiro", items: [a, b, c]}` — baú com conjunto fixo |
| `command` | `- command: "/effect @p 10 30 1"` (executado pelo servidor; `@p` = jogador) |

Raridades: `comum`, `incomum`, `raro`, `epico`, `lendario` (conteúdo em `quests/loot/loot.yaml`).
Balanceamento em `docs/design/RECOMPENSAS.md`.

## 6. Layout

Sem `at`, o gerador posiciona por camadas da esquerda para a direita (profundidade nos
`deps` do próprio capítulo) e ordena verticalmente pelos pais. Colunas com mais de 14 quests
são quebradas. Use `at` para desenhar formas (estrela, árvore, círculo) em capítulos-vitrine.
