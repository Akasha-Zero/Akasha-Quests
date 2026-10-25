# Processo de Produção de um Capítulo

Padrão obrigatório para quem escreve capítulos (pessoas ou agentes). **Qualidade técnica
antes de velocidade — sempre.**

## 0. Leia antes

1. `docs/AUTHORING.md` — formato YAML e validação.
2. `docs/design/NARRATIVA.md` — voz da Akasha, Atos, Reinos, Selos.
3. `docs/design/RECOMPENSAS.md` — economia de recompensas.
4. `docs/design/MAPA.md` — ids, ordens e metas dos capítulos.
5. `docs/research/atm10_design_patterns.md` — padrões de design do ATM10 (estrutura apenas;
   o texto do ATM10 é All Rights Reserved — **escreva sempre texto próprio**).

## 1. Estudar o mod (fontes, em ordem de confiança)

1. **O registro real do pack** (`tools/bqgen/reg.py`): todos os itens/blocos do mod
   (`reg.py mod <namespace>`), nomes pt_BR, oredict, entidades, fluidos, dimensões.
2. **Receitas reais do pack**: `reg.py recipe <id[:meta]>` / `reg.py uses <id>` (bancada e
   fornalha) e `reg.py nei <regex>` (catálogo completo do NEI: máquinas, altares, infusão,
   etc. — inclui alterações de CraftTweaker do pack).
3. **O JAR do mod** em `PACK/mods/` (ver caminho no briefing): `assets/<mod>/lang/*.lang`
   (nomes e textos de manuais/guias internos), `mcmod.info`, arquivos de guia embutidos.
4. **A config do pack** em `PACK/config/` — geração de minérios (Y, raridade, dimensões),
   recursos desativados, multiplicadores. A config manda: se um minério está desativado,
   não peça para minerá-lo.
5. **Documentação externa** (wikis oficiais, FTB Wiki, GitHub do mod) para mecânicas que o
   registro não mostra (multiblocos, rituais, estruturas, chefes). Confira a versão: o pack
   usa versões específicas (veja `reg.py mods`). Prefira fontes da versão 1.7.10.
6. Conhecimento próprio **só** quando confirmado por uma das fontes acima.

## 2. Documentar (`docs/mods/<id>.md`)

Escreva em português, com as seções:

* **Visão geral** — o que o mod é, versão no pack, papel na campanha (Ato).
* **Energia / recursos-base** — tipo de energia (EU, RF, MJ, Essentia, Mana, LP, …) e tiers.
* **Progressão** — tiers em ordem, com itens-chave (ids reais) e o que cada um destrava.
* **Materiais e geração de mundo** — minérios, onde/altura/dimensão (segundo a config).
* **Máquinas, estruturas e multiblocos** — como montar, o que exigem.
* **Dimensões, biomas, estruturas, chefes e mobs** (quando houver).
* **Integrações no pack** — pontes com outros mods (ex.: oredict, energia compatível).
* **Armadilhas e dicas** — o que costuma dar errado (explosões, Warp, Taint, lag, etc.).
* **Marcos exportados** — lista de `capitulo:quest` dos marcos (`milestone/boss/portal/finale`)
  que os capítulos de história podem referenciar, com uma linha explicando cada um.

## 3. Escrever o capítulo (`quests/chapters/<pasta>/<id>.yaml`)

* Siga a meta de quantidade do MAPA, mas **nunca encha linguiça**: cada quest ensina,
  desafia ou recompensa algo real. Catálogos (uma quest por semente, abelha, ferramenta,
  armadura, componente) são legítimos quando o item tem uso/identidade própria.
* Estrutura: espinha principal (`main`) da esquerda para a direita; galhos laterais
  (`normal`/`optional`); marcos a cada fase (`milestone`); chefes (`boss`); novas
  dimensões (`portal`). Use `info` para tutoriais de mecânicas complexas.
* A raiz do capítulo não depende de nada (o jogador pode começar qualquer mod a qualquer
  momento). Dependências entre capítulos só quando um mod realmente exige outro
  (ex.: Thaumic Tinkerer → Thaumcraft); use `deps_hidden` quando a linha não fizer sentido
  visual.
* Tarefas verificáveis: prefira itens (`get`/`craft`), drops/troféus para chefes
  (+ `kill` quando a entidade é estável), `visit` para dimensões.
* Descrições na voz da Akasha (NARRATIVA §2–3): gancho → instrução prática → incentivo.
  Nomes como aparecem em pt_BR. Explique receitas/mecânicas reais (consulte o NEI dump).
* Recompensas conforme RECOMPENSAS.md, com itens que existem (o lint confere).

## 4. Validar

```bash
python3 tools/bqgen/bqgen.py lint quests/chapters/<pasta>/<id>.yaml
```

Repita até `OK`. Depois revise o capítulo inteiro com olhar crítico:

* [ ] Toda quest tem descrição útil, sem informação inventada.
* [ ] A ordem de dependências corresponde à ordem real de fabricação (confira no NEI dump).
* [ ] Nenhum item de tier alto aparece como recompensa cedo demais.
* [ ] Variedade de aberturas e de tipos de tarefa.
* [ ] Marcos exportados documentados.

Não faça commit nem edite arquivos de outros capítulos — o coordenador integra.
