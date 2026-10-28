# Akasha Quests UI — Especificação de design

Mod companheiro (Forge 1.7.10, **só cliente**) que dá ao BetterQuesting 3.8.79-GTNH a interface
do FTB Quests, mantendo a UX e os dados do BQ. Este documento é a fonte da verdade de toda a
geometria, layout e cores. O código cita as seções (§) daqui.

## 0. Princípios

1. **O BQ continua dono dos dados.** Banco de quests, progresso, rede, editores, painéis de
   tarefa e recompensa: tudo é do BQ. O mod só troca a *apresentação*.
2. **Nenhum mod no servidor.** Os dados extras (forma, grupo, imagens) vivem como propriedades
   no domínio `akashaui` dentro do próprio NBT do BQ. O `PropertyContainer` do BQ preserva
   domínios desconhecidos na leitura, na escrita, no salvamento e na sincronização
   servidor → cliente. Por isso a edição usa os pacotes nativos `NetQuestEdit`/`NetChapterEdit`.
3. **Geometria exata.** Formas, hit-test e texturas saem do mesmo polígono (gerado por
   `tools/gen_textures.py` e gravado em `shapes/shapes.json`).
4. **Reversível.** `config/akashaquestui.cfg → enabled=false` devolve as telas originais do BQ.

## 1. Sistemas de coordenadas

| Espaço | Origem | Unidade |
| --- | --- | --- |
| **S** (tela GUI) | canto sup. esq. da janela | pixel GUI escalado (`ScaledResolution`) |
| **D** (dispositivo) | idem | pixel físico; `D = S·f`, `f` = fator de escala da GUI |
| **W** (mundo) | origem do capítulo no BQ | pixel do BQ (`posX/posY/sizeX/sizeY` do `QuestLineEntry`) |

A câmera tem centro `c ∈ W` e zoom `z` (pixels S por unidade W). Com `v₀` = centro do viewport:

```
s = v₀ + z·(w − c)          w = c + (s − v₀)/z
```

**Zoom ancorado no cursor.** O ponto do mundo sob o cursor `s*` fica parado:
`w* = c + (s* − v₀)/z` e, depois do zoom, `c' = w* − (s* − v₀)/z'`.

**Escala quantizada.** `z = 2^(k/4)`, `k ∈ [−8, 8]` (4 passos por oitava, `z ∈ [¼, 4]`).
Cada passo multiplica a escala por `2^¼ ≈ 1,189`, de modo que dois passos de roda sempre
compõem exatamente uma razão `√2`.

**Suavização.** O zoom exibido segue o alvo com decaimento exponencial
`z ← z + (z_alvo − z)·(1 − e^(−Δt/τ))`, `τ = 45 ms` (99% em ≈ 207 ms). A âncora `(s*, w*)` é
reaplicada a cada quadro, então o cursor não "escorrega" durante a animação.

**Enquadrar ("ajustar à tela").** Com `B` = caixa das quests visíveis (e imagens) e margem
`m = 24`: `z_fit = min(w_v/(B_w + 2m), h_v/(B_h + 2m))`. O nível é arredondado *para baixo*:
`k = ⌊4·log₂ z_fit⌋`, garantindo que tudo cabe, e limitado a `k ≤ 2` (`z ≤ √2`) para que um
capítulo com duas quests não ocupe a tela inteira. `c` = centro de `B`.

**Limites da câmera.** Pelo menos `m_s = 32 px` S do conteúdo continuam visíveis:
`c_x ∈ [B_x0 − (w_v/2 − m_s)/z, B_x1 + (w_v/2 − m_s)/z]` (idem em `y`).

**Encaixe no pixel.** Retângulos de quest são convertidos de borda a borda
(`x₀ = ⌊D(x)⌉, x₁ = ⌊D(x + w)⌉`, largura `x₁ − x₀`) para que quests vizinhas não tremam
umas em relação às outras durante o pan.

## 2. Formas das quests

Todas definidas no quadro unitário `[−1, 1]²` (y para baixo) e ajustadas por `fit_to_box`:
a caixa envolvente é centrada e escalada uniformemente até tocar `[−F, F]`, com
`F = 1 − 1,5/64` (folga de 1,5 texel para a rampa de anti-aliasing).

| id | Definição |
| --- | --- |
| `circle` | 256-gono (erro de sagita `1 − cos(π/256) ≈ 7,5·10⁻⁵`) |
| `square` | quadrado |
| `rsquare` | quadrado com cantos de raio `ρ = 0,32` |
| `diamond` | `|x| + |y| ≤ 1` |
| `pentagon` | regular, vértice para cima (`φ₀ = −π/2`). Limitado pela largura `2 sen 72°`, centralizado pela caixa (desloca `≈ 0,10` para baixo) |
| `hexagon` | regular, vértice para cima |
| `octagon` | regular, lado para cima (`φ₀ = π/8`) |
| `heart` | `x = 16 sen³t`, `y = −(13 cos t − 5 cos 2t − 2 cos 3t − cos 4t)`, 720 amostras |
| `gear` | `r(θ) = 0,78 + 0,22·T(θ)`, 8 dentes; `T` trapézio de período 1 (flanco 0,14, topo 0,36) |
| `star` | 5 pontas, raio interno 0,5 (extra do Akasha) |
| `none` | só o ícone, sem fundo |

**Campo de distância exato.** Para o polígono `P`,
`d(p) = ±min_arestas dist(p, segmento)`, com sinal pela regra par-ímpar. Daí:

```
cobertura(p) = clamp(½ − d/px, 0, 1)                  (AA analítico de 1 texel)
contorno(p)  = cobertura · clamp(½ + (d + t)/px, 0, 1),  t = 0,11
brilho(p)    = exp(−max(d,0)² / 2σ²),                  σ = 0,24 (textura cobre [−2, 2])
preench.(p)  = L(y) + 0,16·(−n·ℓ)·clamp(1 + d/b, 0, 1)²,  b = 0,18
```

`L(y) = 0,92 − 0,06·(y + 1)` é a rampa vertical, `n = ∇d/|∇d|` a normal externa e
`ℓ = (−0,55; −0,835)` a luz vinda de cima à esquerda. O resultado é um chanfro interno sutil.

Texturas: 64 texels por meia-unidade (128² para corpo, 256² para brilho). O mod gera a cadeia
de **mipmaps** ao carregar e amostra com `GL_LINEAR_MIPMAP_LINEAR`:

* cada nível tem `⌊n/2⌋` texels por eixo (regra do GL) e é a **média por área** do nível anterior em
  alfa pré-multiplicado: o texel `i` cobre `[i·r, (i+1)·r)` com `r = n/⌊n/2⌋`, pesos `|cobertura|/r`
  que somam 1. Para tamanhos pares é a caixa 2×2; para ímpares (`2 < r < 3`) nenhuma linha ou coluna é
  descartada e a média se conserva (teste `MipResampleTest`);
* texels com `α = 0` recebem a cor média dos vizinhos visíveis (8-vizinhança, ponderada por α), que
  desce a cadeia junto. O filtro bilinear mistura o RGB desses texels na borda, e preto ali daria
  franja escura no contorno e nos selos com zoom alto;
* imagens maiores que `min(2048, GL_MAX_TEXTURE_SIZE)` são reduzidas ao carregar (memória e limite
  da GPU). Com zoom ¼ uma quest de 24 px ocupa 12 px de dispositivo a partir
de 128 texels: sem mipmap haveria serrilhado e cintilação.

**Hit-test.** Ponto `p` em S → coordenada local `u = (p − centro)/(meio-tamanho)` → teste
par-ímpar no polígono simplificado (≤ 96 vértices) de `shapes.json`. Só testa quests cuja
caixa contém `p`. Quests `none` usam o quadrado do ícone.

## 3. Composição de uma quest

Ordem de desenho (meio-tamanho `h = z·size/2`):

1. **Brilho** (só "disponível" e "selecionada"): textura `glow` em `[−2h, 2h]`.
   Disponível pulsa `α = 0,10 + 0,20·(½ − ½cos(2πt/2,4 s))`.
2. **Preenchimento** tingido com a cor de fundo do estado.
3. **Ícone** do item (`RenderItem`) de lado `s = 0,62·2h`, centralizado, origem alinhada ao pixel de
   dispositivo e escala `s/16` em x, y e `min(s/16, 8)` em z (a profundidade do bloco 3D encolhe junto;
   o teto mantém z dentro de `[−1000, 1000]`, a faixa da projeção da GUI, para ícones enormes). O `RenderItem` escreve profundidade, então o
   buffer de profundidade é limpo antes de cada ícone e entre camadas da tela; sem isso um ícone de
   bloco recorta os ícones desenhados depois dele. Ícones abaixo de 6 px de dispositivo não são
   desenhados (ilegíveis e o custo maior por quest).
4. **Escurecimento** (bloqueada): máscara tingida `#000000`, `α = 0,55`.
5. **Hover**: máscara branca, `α = 0,12`.
6. **Contorno** tingido com a cor do estado.
7. **Selo** no canto superior direito, lado `0,46·2h`, centro em `(+0,72h, −0,72h)`:
   ✔ concluída, `!` recompensa pendente (pulso de escala `1 ± 0,08`, período 1,2 s),
   ↻ repetível. 📌 favorita no canto superior esquerdo.

### Estados (mapeamento BQ → visual FTB)

| Estado | Condição no BQ | Preench. | Contorno |
| --- | --- | --- | --- |
| Bloqueada | `LOCKED` | `#2B2B2E` | `#76767F` (3,14:1 contra o preenchimento, WCAG 1.4.11) |
| Disponível | `UNLOCKED`, 0 tarefas concluídas | `#3A3A40` | `#E6E6E6` + brilho |
| Em progresso | `UNLOCKED`, ≥ 1 tarefa concluída | `#3A3A40` | `#3FD2E0` |
| Recompensa pendente | `UNCLAIMED` | `#3A3A40` | `#5BD94F` + `!` |
| Concluída | `COMPLETED` | `#2E3A2C` | `#5BD94F` + ✔ |
| Repetível | `REPEATABLE` | `#3A3A40` | `#B584F0` + ↻ |

Forma padrão: propriedade da quest → padrão do capítulo → `hexagon` para quests `MAIN` do BQ
e `circle` para as demais (ambos configuráveis).

## 4. Linhas de dependência

De centro a centro, desenhadas **antes** das quests (que as ocultam por cima). Para
`A → B` em S: `d̂ = (B − A)/|B − A|`, `n̂ = (−d̂_y, d̂_x)`; quad `A ± n̂·w/2`, `B ± n̂·w/2`.

* Quests sem forma (`none`) não têm preenchimento para cobrir a linha: ela termina na borda do
  ícone, `0,31·size·z` antes do centro.
* Antes da faixa, um contorno `#000000` α `0,85·α_linha` de `w + 2` px (1 px de cada lado) mantém
  a linha legível sobre qualquer mundo atrás do fundo translúcido.
* Largura no mundo `w_W = 0,11·min(size_A, size_B)` (2,64 px para quests de 24 px), ×1,25 se a
  dependente for `MAIN`; na tela `w = max(1, z·w_W)`.
* Textura em ladrilho de 2 larguras: faixa com borda escura e uma chevron `>` apontando de A
  para B. `u = s/(2w) − φ(t)`, com `s` a distância ao longo da linha desde A. A fase
  `φ` faz as chevrons correrem no sentido do progresso (`v = 0,5` ladrilho/s; ×3 na
  linha sob o mouse). Cada linha (chave: ids das duas quests, que sobrevivem às reconstruções do modelo) guarda sua fase e a avança por `φ ← (φ + v·Δt) mod 1`; com
  `φ = v·t` a troca de `v` no hover faria as chevrons saltarem. Sem setas (opção do BQ) usa a faixa lisa.
* Requisito `IMPLICIT` do BQ: só aparece com hover ou Shift (UX do BQ), textura tracejada.
* Quest com lógica `OR`/`XOR` nos pré-requisitos: textura tracejada.

| Situação | Cor |
| --- | --- |
| Dependente bloqueada | `#6E6E78` (com o contorno: ≥ 3,45:1 no pior fundo) |
| Dependente disponível/em progresso | `#BDBDC6` |
| Dependente concluída | `#5BD94F` |
| Dependente repetível | `#B584F0` |
| Hover em Q: linhas que entram em Q (requisitos) | `#46E0FF` |
| Hover em Q: linhas que saem de Q (dependentes) | `#FFD34A` |
| Hover em Q: demais linhas | cor × `α 0,35` |

## 5. Fundo do mapa

`#16161A` com opacidade `a` (padrão `0,72`, ajustável de 0,2 a 1 em Opções de visualização →
Opacidade do fundo): abaixo de 1 o mundo aparece por trás, como no FTB Quests. Barra lateral e barra de
ferramentas usam `max(0,88, min(1, a + 0,15))` para o texto continuar legível (com o piso 0,88 o
texto desabilitado `#8A8A94` fica ≥ 4,1:1 mesmo com o mundo branco atrás); a janela da quest, menus e
diálogos continuam sólidos. Por cima, duas grades em espaçamento de mundo `g = 8` (menor) e `4g = 32` (maior).
A opacidade some suavemente quando as linhas ficariam densas demais:
`α_menor = 0,035·clamp((8z − 5)/7, 0, 1)` e `α_maior = 0,07·clamp((32z − 5)/7, 0, 1)`.
A imagem de fundo nativa do BQ (`bg_image`, `bg_size`) é desenhada sob a grade, com os cantos
alinhados ao pixel de dispositivo como as quests (não desliza ao dar zoom). Os recortes (scissor)
do mapa, do painel e dos menus convertem y por `H − y·f` com a altura real `H` da janela; o BQ usa
`⌈H/f⌉·f`, que desloca o recorte até `f − 1` px quando `H` não é múltiplo de `f`.

## 6. Layout da tela

`W × H` = tamanho da tela em S. Sem margens (`useMargins(false)`).

```
┌────────────┬──────────────────────────────────────────────┬──┐
│ Capítulos  │ título · progresso                            │▣ │ ← barra de ferramentas
│ ▼ Grupo    │                                              │📌│   (20 px)
│   ◆ Cap. 1 │          mapa de quests (câmera §1)          │🔍│
│   ◆ Cap. 2 │                                              │ⓘ │
│ ▶ Grupo    │                                              │  │
│            │                                              │⚙ │
│            │                                              │✎ │
└────────────┴──────────────────────────────────────────────┴──┘
```

**Título do capítulo.** No canto superior esquerdo do mapa (6 px de margem, seguindo a borda
direita do painel quando ele desliza por cima): pílula de 16 px na cor do painel com o nome, o
progresso `feitas/total quests (p%)` em cor secundária e uma barra de 1 px de largura
`(L − 2)·feitas/total` em `#3FD2E0`. Só exibição, não recebe cliques. Se o espaço não couber, o nome
ganha reticências e o progresso some antes do nome.

**Camadas e teclado.** ESC e Backspace fecham a camada mais interna (Backspace não fecha diálogos, onde
apaga texto): menu/diálogo, depois a janela
da quest, depois a seleção do modo de edição; ESC sem nada aberto fecha o livro, Backspace nunca
(o BQ voltaria para a tela-mãe). Com a janela da quest aberta a tela inteira escurece (`#000000`
α 0,6) e nada atrás dela reage ao mouse.

**Barra lateral.** Largura
`W_s = clamp(maxᵢ largura(títuloᵢ) + 8 + 8 + 8 + 16, min(110, ⌊0,22·W⌋), ⌊0,22·W⌋)`
(ícone 8 + recuo do grupo 8 + selo 8 + margens; o nome do pack no cabeçalho é encurtado com
reticências e não alarga o painel; nomes de capítulo maiores que a barra também, com o nome inteiro na dica). Os capítulos mostram o nome curto `akashaui:title` quando existe. Linhas de 12 px (ícone 8 px: a 2× da GUI
isso é 16 px de dispositivo, escala 1:1 do sprite do item), cabeçalho de grupo 13 px,
cabeçalho do painel 18 px. Dois modos:

* **Fixada** (📌 ligado, padrão = `lockTray` do BQ): o mapa começa em `x = W_s`.
* **Recolhida**: tira de 10 px com `▶`. Passar o mouse expande sobre o mapa, com
  `largura(t) = 10 + (W_s − 10)·(1 − (1 − t)³)`, `t = clamp(Δt/140 ms, 0, 1)`.

Cor do nome do capítulo: selecionado `#FFFFFF` sobre `#FFFFFF 20%` com barra de destaque de 2 px
`#3FD2E0`; concluído `#7CD977` com ✔; recompensa pendente com `!`; sem quests visíveis
`#62626C` com cadeado; demais `#D8D8DE`. A dica (tooltip) mostra `concluídas/total (xx,x %)`,
exatamente pela regra do BQ (ignora `HIDDEN`/`SECRET` e `COUNT_AS_QUEST = false`).

**Barra de ferramentas** (direita, 20 px; ícones de 16 px do próprio BQ):
topo → resgatar tudo (pulsa quando há o que resgatar), favoritos, busca, ajuda;
base → configurações de visualização (menu com as alternâncias do BQ), modo de edição (só
operadores), notificações, sair.

## 7. Janela da quest (popup)

Viewport do mapa `V = (w_v, h_v)`. A janela se ajusta ao conteúdo, como no FTB Quests:

```
W_p = clamp(⌊0,78·w_v⌉, 300, 480 com recompensas | 360 sem)   (se w_v < 316: W_p = w_v − 16)
H_max = ⌊0,82·h_v⌉                                               (se h_v < 206: h_v − 16)
H_p = clamp(h_h + h_f + 2p + c + g + d, 150, H_max)
```

onde `c = 12 + 2 + max(altura das tarefas, altura das recompensas)` (medidas montando os próprios
painéis do BQ), `d = altura da descrição quebrada + 4` e `g = 4` quando há colunas e descrição.
A janela é centrada em `V`. A largura é generosa porque os painéis de tarefa do BQ precisam de
≈ 200 px por coluna para não cortar nomes de itens. Fundo do mapa escurecido por `#000000 60%`. Margem interna `p = 6`.

```
┌──────────────────────────────────────────────────────────┐
│ [ícone]            Título da quest           ✎  📌  ✕   │ h_h = 20
├──────────────────────────────────────────────────────────┤
│ ◀        Tarefas         │        Recompensas         ▶ │ 12
│  painéis de tarefa do BQ │  painéis de recompensa     │ h_c
│  (rolagem)               │  (rolagem)                 │
├──────────────────────────────────────────────────────────┤
│  descrição (formatação, links e [img] do BQ, rolagem)    │ h_d
├──────────────────────────────────────────────────────────┤
│ [ Detectar / Entregar ]  │  [ Resgatar ]                 │ h_f = 18
└──────────────────────────────────────────────────────────┘
```

Altura do corpo `H_b = H_p − h_h − h_f − 2p` e `A = H_b − g`. Se colunas e descrição cabem
(`c + d ≤ A`), cada uma recebe o que precisa e a sobra vai para as colunas. Senão a descrição fica
com `h_d = min(d, max(36, ⌊0,42·A⌋, A − c))` e as colunas com `h_c = A − h_d`; as duas rolam.
Sem recompensas, a coluna de tarefas ocupa a largura toda. `◀`/`▶` abrem a lista de
pré-requisitos/dependentes (navegação do BQ GTNH) e levam a câmera até a quest escolhida.

Abertura: escala `0,92 → 1` e `α 0 → 1` em 120 ms com `1 − (1 − t)³`, em torno do centro.
Fecha com ✕, `Esc`, clique fora ou clique direito.

## 8. Imagens no capítulo (como no FTB Quests)

Lista NBT `akashaui:images` no capítulo. Cada imagem:

| Campo | Tipo | Significado |
| --- | --- | --- |
| `image` | string | `dominio:caminho.png` (resource pack), `item:mod:nome[:meta]` ou `file:sub/arquivo.png` (em `config/betterquesting/akasha_images/`) |
| `x`, `y` | float | **centro** em W |
| `w`, `h` | float | tamanho em W |
| `rot` | float | rotação em graus (horária) |
| `alpha` | float | opacidade 0–1 |
| `tint` | int | cor ARGB multiplicada (padrão `0xFFFFFFFF`) |
| `layer` | int | `0` atrás das linhas, `1` entre linhas e quests, `2` na frente das quests |
| `hover` | lista | linhas de dica (aceita chaves de tradução e `§`) |
| `click` | string | `quest:<id>`, `chapter:<id>` ou `url:https://…` |
| `dep` | string | id de quest: a imagem só aparece depois que ela é concluída |
| `edit` | byte | 1 = só aparece no modo de edição (notas de layout) |

**Desenho com rotação.** Cantos `c + R(θ)·(±w/2, ±h/2)` com
`R(θ) = [[cos θ, −sin θ], [sin θ, cos θ]]`. **Hit-test**: `u = R(−θ)·(p − c)`, dentro se
`|u_x| ≤ w/2 ∧ |u_y| ≤ h/2`.

**Edição** (modo de edição do BQ + operador):

* Mover: arrastar; encaixe em 4 px de W (Shift: livre, 1 px).
* Redimensionar pelas 8 alças. Com o canto `(σx, σy) ∈ {±1}²` e o arrasto `Δ` em W, o delta
  local é `δ = R(−θ)·Δ`; os novos meios-tamanhos são `h'_x = max(2, h_x + σx·δ_x/2)`, idem `y`,
  e o centro se desloca `R(θ)·(σx·(h'_x − h_x), σy·(h'_y − h_y))`, mantendo o canto oposto fixo.
  Alças de borda zeram a componente que não controlam. Com Shift (ou "manter proporção") a
  razão `w/h` é preservada: `s = max(h'_x/h_x, h'_y/h_y)`.
* Girar pela alça acima da imagem: `θ = atan2(p − c) + 90°`, encaixe de 15° (Shift: livre).
* `Del` remove, `Ctrl+D` duplica, setas movem 1 px (Shift: 8), `Ctrl+Z` desfaz.
* Cada alteração concluída (soltar o mouse) aplica localmente e envia o capítulo inteiro com
  `NetChapterEdit.requestEdit`; o servidor grava e sincroniza com todos.

## 9. Dados (domínio `akashaui`)

| Onde | Chave | Tipo | Uso |
| --- | --- | --- | --- |
| quest | `shape` | string | forma (§2); vazio = padrão |
| capítulo | `group` | string | nome do grupo na barra lateral |
| capítulo | `shape` | string | forma padrão do capítulo |
| capítulo | `images` | lista | imagens (§8) |

No JSON do BQ (multi-arquivo) aparecem como `"akashaui:10": {"shape:8": "hexagon"}`. O
`bqgen` gera esses campos a partir do YAML (`shape`, `group`, `images`).

## 10. Paleta

| Token | Valor |
| --- | --- |
| fundo do mapa | `#16161A` α `a` (configurável, padrão 0,72) |
| painel (barra lateral, barra de ferramentas) | `#101014` α `max(0,88, min(1, a + 0,15))` |
| borda de painel | `#3A3A44` |
| cabeçalho de painel | `#1C1C22` |
| hover de linha | `#FFFFFF` α 0,12 |
| destaque | `#3FD2E0` |
| texto principal / secundário / desabilitado | `#EDEDED` / `#A6A6B0` / `#8A8A94` |
| rótulo "Tarefas" / "Recompensas" | `#8E8EFF` / `#FFC64A` |
