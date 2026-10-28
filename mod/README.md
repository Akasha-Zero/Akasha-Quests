# Akasha Quests UI

Interface no estilo **FTB Quests** para o **BetterQuesting 3.8.79-GTNH** (Minecraft 1.7.10).
O BetterQuesting continua dono de tudo: banco de quests, progresso, rede, editores e os painéis
de tarefa e recompensa. O mod troca só a apresentação.

* **Só cliente.** Coloque o jar em `mods/` do cliente. O servidor não precisa do mod.
* **Reversível.** `config/akashaquestui.cfg → enabled=false` volta às telas originais do BQ.
* **Compatível com o livro sem o mod.** Os dados extras (forma, grupo, imagens) ficam no domínio
  `akashaui` do próprio NBT do BQ, que os ignora.

Especificação completa (geometria, layout, cores, dados): [docs/DESIGN.md](docs/DESIGN.md).

## O que muda

| FTB Quests | Aqui |
| --- | --- |
| Barra de capítulos com grupos, fixável | Capítulos do BQ agrupados pelo campo `group`, com ✔ (completo), `!` (recompensa esperando) e cadeado; o 📌 fixa ou recolhe o painel |
| Mapa com formas, zoom e linhas animadas | Câmera com zoom no cursor (roda), arrastar para mover, `Home` enquadra; 11 formas; linhas com chevrons que andam no sentido do progresso; ao passar o mouse, requisitos em ciano e dependentes em amarelo |
| Janela da quest sobre o mapa | Tarefas \| Recompensas com os painéis do BQ, descrição com links e `[img]`, ◀ requisitos / ▶ dependentes, Detectar/Entregar e Resgatar, favoritar, editar |
| Barra de ferramentas | Resgatar tudo, favoritos, busca, enquadrar, opções de visualização (as alternâncias do BQ), party, notificações, modo de edição, ajuda, sair |
| Imagens no capítulo | Imagens de resource pack, de arquivo ou de item, com tamanho, rotação, opacidade, cor, camada, dica, ação de clique e quest requerida |

Todas as entradas do BQ abrem esta tela: tecla de quests, NEI, busca, favoritos, `/bq show`,
links de quest e compartilhamento no chat.

## Modo de edição

Disponível para operadores com o modo de edição do BQ ligado (`/bq_admin edit true`). Ative pelo
botão de mão na barra de ferramentas.

| Ação | Como |
| --- | --- |
| Mover quests | arrastar (encaixe de 4 px; Shift: livre). Várias: Ctrl+clique ou Shift+arrastar no vazio |
| Forma, tamanho, requisito, completar, resetar, remover, excluir | clique direito na quest |
| Adicionar imagem / nova quest | clique direito no mapa vazio |
| Mover, redimensionar, girar imagem | arrastar o corpo, as 8 alças ou a alça de rotação (Shift: proporção / sem encaixe de 15°) |
| Editar imagem | clique direito → Editar imagem (usa o navegador de arquivos, o seletor de itens e o editor de texto do BQ) |
| Atalhos | `Del` remove imagem, `Ctrl+D` duplica, setas movem (Shift: 8), `Ctrl+Z` desfaz, `Ctrl+A` seleciona tudo |
| Grupos e ordem dos capítulos | clique direito no capítulo ou no grupo |

Cada gesto concluído é enviado ao servidor pelos pacotes do próprio BQ e sincronizado para todos.

## Configuração (`config/akashaquestui.cfg`)

| Chave | Padrão | |
| --- | --- | --- |
| `enabled` | `true` | Substituir as telas do BQ |
| `applyTheme` | `true` | Ativar o tema "Akasha FTB" do BQ (painéis do BQ no mesmo visual escuro) |
| `defaultShape` / `mainShape` | `circle` / `hexagon` | Formas padrão |
| `animateLines`, `lineSpeed` | `true`, `0.5` | Chevrons nas linhas |
| `pulseAvailable` | `true` | Brilho nas quests que podem ser feitas |

## Compilar

Requer Java 17+ (o Gradle baixa os toolchains).

```bash
cd mod
./gradlew build          # jar em build/libs/akashaquestui-<versão>.jar
```

Texturas e tema são gerados por `tools/gen_textures.py` (numpy + Pillow) e já estão versionados.

### Teste automatizado da interface

`src/devtest` é um mod só de desenvolvimento (nunca vai no jar) que cria um mundo, abre o livro,
executa um roteiro e salva capturas de tela em `run/client/screenshots/`:

```bash
python3 ../tools/bqgen/bqgen.py build --chapters devtest/quests --out run/client/config/betterquesting
xvfb-run -a -s "-screen 0 1280x720x24" ./gradlew runClient -Pselftest=devtest/smoke.txt
```
