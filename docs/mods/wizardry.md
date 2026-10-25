# Electroblob's Wizardry — estudo do capítulo `wizardry`

## Visão geral

* **Mod:** `Electroblob's Wizardry - 1.1.6 - MC 1.7.10.jar` (namespace `wizardry`; o
  `mcmod.info` diz 1.1.5, `reg.py mods` diz 1.1.6). O JAR também carrega uma pasta
  `assets/ebwizardry` (recursos da linha 1.12) que não é usada pelos ids do pack.
* **Capítulo:** `quests/chapters/magia/wizardry.yaml`, `order: 565`, 86 quests.
* **Papel na campanha:** magia "de RPG" leve, do Ato I/II (cristais e varinha de Novato) ao
  Ato III (Mestre). Não há multiblocos nem árvores de habilidade.
* Sem `pt_BR.lang` no JAR: itens aparecem em inglês (os pergaminhos mostram "Pergaminho" porque
  usam um nome genérico).

Fontes: registro (`reg.py mod wizardry`, receitas), `assets/wizardry/texts/handbook_en_US.txt`,
`assets/wizardry/lang/en_US.lang` (descrições dos feitiços), `config/wizardry.cfg`, bytecode de
`WizardryRegistry` (ordem dos 140 feitiços = metas de `spell_book`/`scroll`), dos construtores de
cada `spell/*` (tier, elemento, custo), de `Wizardry` (constantes), `WizardryWorldGenerator` e
`EntityWizard` (loja).

## Energia / recursos-base

* **Mana** guardada na varinha/armadura, vinda de **Cristais Mágicos** (`wizardry:magic_crystal`,
  `MANA_PER_CRYSTAL = 100`). Frasco de Mana = 700 (`MANA_PER_FLASK`).
* Capacidade das varinhas (dano máximo no registro): Novato 700, Aprendiz 1000, Avançado 1500,
  Mestre 2500.
* Tiers de feitiço: Novato (metas 1–12, 111–113), Aprendiz (13–47, 114–123), Avançado (48–90,
  124–135), Mestre (91–110, 136–140). Elementos: Magia (1), Fogo, Gelo, Raio, Necromancia, Terra,
  Feitiçaria, Cura (listas completas no YAML).

## Progressão (ids reais)

1. `crystal_ore` → `magic_crystal` (também `crystal_flower` → 2 cristais; `crystal_block` = 9).
2. `magic_wand` (pepita de ouro, graveto, cristal) → `arcane_workbench` (3 pedras, bloco de lápis,
   2 cristais, 2 pepitas de ouro, tapete roxo).
3. `spell_book:1` (Magic Missile: livro + 4 cristais — único livro com receita).
4. `arcane_tome:1/2/3` (sem receita) sobem a varinha Novato → Aprendiz → Avançado → Mestre
   (varinhas elementais mantêm o elemento).
5. Upgrades sem receita: `storage/siphon/condenser/range/duration/cooldown/blast/attunement_upgrade`
   (até 3 de cada; limite total por tier; não removíveis).
6. `magic_silk` (4 linhas + cristal → 2) → `wizard_hat/robe/leggings/boots` (carregam com cristais
   ou Frasco). Elementais (`wizard_*_fire` etc.) e `armour_upgrade` (Selo Arcano → Lendária) só
   como saque.

## Materiais e geração de mundo

* `oreDimensions/flowerDimensions/towerDimensions = 0` (só Mundo Normal). Minério de cristal:
  veios de até ~5, ~7 tentativas/chunk, Y ~5–30 (bytecode). Flores de cristal na superfície.
  Torres de mago: `towerRarity=8`.
* `generateLoot=true` (livros, tomos, upgrades e vestes em baús); `spellBookDropChance=3`
  (monstros soltam livros); `discoveryMode=true` (feitiços não lançados aparecem como runas).

## Máquinas, estruturas e NPCs

* **Bancada Arcana**: vincular (até 5 feitiços), carregar (slot superior esquerdo), melhorar
  (slot inferior esquerdo); botão Aplicar.
* **Torres de mago**: magos (`wizardry.Wizard`) vendem livros, tomos, pergaminhos de
  identificação e upgrades por ouro, diamante e esmeralda; magos especialistas vendem feitiços de
  Mestre. **Magos hostis** (`wizardry.Evil Wizard`) atacam com magia.
* **Pedras de Transporte** (`transportation_stone`): círculo memorizado com a varinha, usado pelo
  feitiço Transporte.

## Integrações no pack

* A Tocha de Interdição do Reliquary empurra várias entidades do Wizardry (config do Reliquary).
* `friendlyFire=true`: use `/ally` e `/allies`.

## Armadilhas e dicas

* Armadura de mago sem carga não protege.
* Feitiços de fogo pouco afetam criaturas do Nether; raio carrega creepers.
* Feitiços de Mestre (Meteoro, Buraco Negro, Terremoto...) destroem terreno.
* A varinha só aceita cristais inteiros — sobra de mana se perde.

## Marcos exportados

| Marco | Tipo | O que representa |
| --- | --- | --- |
| `wizardry:sete_grimorios` | milestone | Um livro de cada um dos 7 elementos. |
| `wizardry:varinha_aprendiz` | milestone | Primeira varinha de Aprendiz (Ato I/II). |
| `wizardry:varinha_avancada` | milestone | Varinha Avançada. |
| `wizardry:varinha_mestre` | milestone | Varinha de Mestre. |
| `wizardry:traje_feiticeiro` | milestone | Traje de Mago completo. |
| `wizardry:mago_hostil` | boss | Primeiro mago renegado derrotado. |
| `wizardry:maestria_elementos` | milestone | **Marco final** (Mestre + 7 grimórios + traje + mago hostil) — pode compor o **Selo Arcano**. |
