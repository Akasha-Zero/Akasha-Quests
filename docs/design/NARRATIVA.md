# Bíblia Narrativa — *Crônicas Akáshicas*

> Documento-mestre de tom, enredo e personagens. Toda descrição de quest deve soar como se
> **Akasha** estivesse falando diretamente com o jogador. Leia isto antes de escrever uma linha.

## 1. A premissa

Existe um lugar fora do tempo chamado **Registro Akáshico**: uma biblioteca viva onde cada
segredo do universo — cada engrenagem, cada feitiço, cada estrela — está escrito numa página.

Há muito tempo, uma civilização ambiciosa (os **Antigos**) tentou ler a última página, aquela
que descreve o **Infinito**. O Registro não aguentou: rachou. Esse evento é chamado de
**A Fratura**. As páginas se espalharam por todas as dimensões e, pelas rachaduras, vazou o
**Proibido** — a corrupção que hoje aparece como Taint, Warp, Eldritch e monstros que não
deveriam existir.

O jogador acorda nesse mundo fraturado. Ninguém lembra mais como as coisas funcionam: a
humanidade esqueceu a metalurgia, a magia, a eletricidade. **Cada mod do pack é um "Volume"
perdido do Registro**. Ao completar quests, o jogador *reescreve* essas páginas — e o mundo
volta a fazer sentido.

## 2. A narradora: Akasha

**Akasha** é a Guardiã do Registro Akáshico — uma consciência antiga, curiosa e muito afetuosa
que sobreviveu à Fratura presa dentro do livro de quests. Ela é a voz de todas as descrições.

Personalidade:

* **Calorosa e torcedora.** Comemora cada conquista como se fosse a primeira. "Isso! Eu sabia!"
* **Nerd assumida.** Fica empolgadíssima com máquinas *e* com magia — e às vezes se perde
  explicando detalhes ("…desculpa, me empolguei. Onde eu estava?").
* **Protetora.** Antes de algo perigoso, avisa com carinho e franqueza ("Isso aqui morde. Leve
  comida, armadura e, por favor, NÃO cave pra baixo.").
* **Bem-humorada, nunca sarcástica com o jogador.** Brinca com a situação, não com quem joga.
* **Honesta sobre mecânicas.** Debaixo da fantasia, a quest sempre explica *o que fazer e como*.

Como ela chama o jogador: **"Herói"** é o título carinhoso preferido (pedido do dono do pack),
alternando com **"Viajante"**, **"parceiro(a) de aventura"** e simplesmente **"você"**.
Ela fala de si no feminino ("eu fiquei tão orgulhosa!") e usa português brasileiro
informal, com "você", exclamações e frases curtas.

## 3. Regras de escrita das descrições

1. **Gancho → explicação → incentivo.** Um parágrafo de fantasia/emoção, um (ou dois) de
   instrução prática, e uma frase final de motivação ou de "próximo passo".
2. **Sempre prático.** O jogador precisa sair da quest sabendo o que fazer: receita-chave,
   máquina necessária, onde encontrar, armadilhas comuns. Cite NEI quando ajudar
   ("abra o NEI com R/U no item").
3. **Tamanho:** quests comuns 50–120 palavras; marcos/chefes/portais 120–220 palavras;
   quests `info` podem ser maiores (tutoriais), mas use quebras de linha.
4. **Formatação BetterQuesting:** `§e` destaque (amarelo), `§a` dica (verde), `§c` perigo
   (vermelho), `§d` magia (rosa), `§b` tecnologia (ciano), `§6` lendário (dourado), `§r` reseta.
   Use com parcimônia: no máximo 2–3 destaques por quest.
5. **Nomes de itens** como o jogador os vê em **pt_BR** (o cliente roda em pt_BR) — `reg.py`
   mostra o nome traduzido. Se o mod não tiver tradução, use o nome em inglês.
6. **Não invente mecânicas.** Se não tiver certeza de como algo funciona, descreva de forma
   geral e mande o jogador conferir no NEI / no livro do mod (Thaumonomicon, Lexica Botania,
   Manual do Engenheiro, etc.).
7. **Variedade.** Não comece toda quest com "Herói!". Alterne aberturas: uma pergunta, uma
   lembrança de Akasha, um aviso, um fato curioso, uma piada.
8. **Continuidade.** Referencie quests anteriores ("lembra daquele Macerador? Pois é…") e
   conecte capítulos ("isso vai ser essencial lá no capítulo de Mekanism").

### Exemplos de tom

> **Primeira Fornalha** — Sabe qual foi a primeira página que eu consegui recuperar depois da
> Fratura? A do fogo controlado. Pode parecer bobo, mas tudo — TUDO — que vem depois começa
> aqui. Junte 8 pedregulhos, monte uma Fornalha e asse alguma coisa.
> §aDica:§r carvão vegetal (madeira na fornalha) funciona tão bem quanto carvão mineral.
> Vamos lá, Herói. O mundo não vai se reconstruir sozinho!

> **Naga (chefe)** — Ouviu esse barulho de escamas? A Naga guarda o pátio de pedra no coração
> da Floresta do Crepúsculo. §cEla dá investidas fortes§r: lute em campo aberto, use escudo de
> blocos se precisar e mire na cabeça — o corpo absorve os golpes. Derrote-a e o Registro libera
> o caminho para a Biblioteca do Lich. Eu confio em você. (E traga a escama, eu quero ver!)

## 4. Estrutura em Atos

O livro de quests é uma grande campanha. Os capítulos de mods são "Volumes"; os capítulos
**Crônicas Akáshicas** formam a espinha dorsal da história e conectam tudo.

| Ato | Nome | O que acontece | Mods centrais |
| --- | --- | --- | --- |
| Prólogo | **O Despertar** | Akasha acorda o Herói, explica o livro, primeiros passos. | Vanilla, utilidades |
| I | **Raízes** | Sobrevivência, ferramentas, comida, primeira metalurgia e primeira magia. | Tinkers, HarvestCraft, Et Futurum, IC2/Thermal básico, Thaumcraft/Botania iniciais |
| II | **Engrenagens e Encantos** | Energia, processamento de minério, automação; magia intermediária; primeiros reinos. | IC2, IU, Thermal, EnderIO, Mekanism, BC, IE, Eln, RotaryCraft; Thaumcraft, Botania, Blood Magic, Witchery, Ars Magica |
| III | **Os Reinos Perdidos** | Três reinos guardam as Chaves do Registro: **Crepúsculo** (Twilight), **Céus** (Aether) e **Areias** (Atum). | Twilight Forest, Aether, Atum, Runic Dungeons, OreSpawn |
| IV | **Os Selos** | Restaurar os oito Selos do Registro (ver abaixo). | Todos os mods de endgame, Galacticraft, Draconic, AE2 |
| V | **A Estrela de Todas as Coisas** | Allthemodium, as três dimensões ATM e os cinco Fragmentos da Estrela ATM → **Estrela ATM** (*pseudo-final*: o Registro é restaurado). | ATM Family |
| Epílogo | **Além do Infinito** | A última página — a mesma que causou a Fratura — ainda tem fome. Avaritia: Neutrônio, Singularidades, Catalisador do Infinito. | Avaritia, Avaritiaddons, Eternal Singularity |

### Os Três Reinos (ligação Aether × Atum × Twilight)

Antes da Fratura, três reinos eram os **arquivos externos** do Registro:

* **O Reino do Crepúsculo** (Twilight Forest) — os **Lordes Lich** eram arquivistas que
  tentaram guardar as páginas do tempo e foram corrompidos por elas. A **Chave do Crepúsculo**
  surge ao vencer o Lich e consolidar-se após a **Rainha da Neve / Ur-Ghast**.
* **O Reino dos Céus** (Aether) — as **Valquírias** juraram proteger as páginas do céu; o
  **Espírito do Sol** as mantém como reféns. A **Chave dos Céus** vem do Sun Spirit.
* **O Reino das Areias** (Atum) — os deuses de Atum guardam o **Livro dos Mortos**, onde está
  escrito o que acontece com tudo que acaba. O **Faraó** guarda a **Chave das Areias**.

Os três reinos se conhecem: os Lich negociavam páginas com os Faraós; as Valquírias vigiavam
ambos lá de cima. Use isso nas descrições ("os Lich e os Faraós tinham um acordo antigo…").

### Os Oito Selos (Ato IV → libera o Ato V)

1. **Selo do Crepúsculo** — Twilight Forest completa.
2. **Selo dos Céus** — Aether completo.
3. **Selo das Areias** — Atum completo.
4. **Selo do Abismo** — Wither, Dragão do Fim, Chaos Guardian (Draconic).
5. **Selo das Estrelas** — chefes espaciais (Galacticraft, ExtraPlanets, Amun-Ra).
6. **Selo Arcano** — maestria mágica (Thaumcraft avançado, Gaia Guardian, Blood Magic, Witchery).
7. **Selo da Engrenagem** — maestria tecnológica (reatores, AE2, Mekanism/IU endgame).
8. **Selo da Vida** — agricultura, apicultura e genética (Forestry/abelhas/MagicBees/AgriCraft).

Quando os oito selos brilham, a Fratura se fecha o bastante para abrir as dimensões ATM
(Mining, Other, Beyond) e começar a jornada até a **Estrela ATM**.

### Pseudo-final e Epílogo

Ao criar a Estrela ATM, Akasha celebra: "O Registro está inteiro de novo!" — banner grande,
efeito *velocidade da luz*. **Mas** logo depois uma quest secreta revela uma página que
continua em branco: a do **Infinito**. O Epílogo (Avaritia) é a busca por escrevê-la com
responsabilidade — sem repetir o erro dos Antigos. O verdadeiro fim é forjar o equipamento
do Infinito e fechar a última página: *"O fim de uma crônica é só o começo de outra."*

## 5. Capítulos com prefixo

* **ATM:** — capítulos inspirados diretamente nas questlines do ATM10 (podem ser muito
  parecidos com o original). Mesma voz de Akasha, que trata o ATM como "o Volume dos
  Metais Impossíveis".
* **AEDF3:** — série que reconstrói a progressão do modpack brasileiro **A Era do Futuro 3**.
  Akasha apresenta como "as memórias de outros heróis que passaram por aqui antes de você".
* **Sem prefixo** — conteúdo original do Akasha Quests.
