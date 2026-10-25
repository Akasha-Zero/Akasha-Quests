# Binnie: Árvores, Flores e Genética (Extra Trees, Botany, Genetics, Binnie Core)

Capítulo `binnie` — `quests/chapters/vida/binnie.yaml` (order 610, 62 quests). Extra Bees ficou de fora.

## Visão geral

| Mod | Namespace | Papel |
| --- | --- | --- |
| Extra Trees 2.6.43 | `ExtraTrees` | 35+ madeiras, 60+ frutas, Serraria/Marcenaria/Painelaria/Vidraria/Ladrilharia, bancos de dados |
| Botany 2.6.43 | `Botany` | Flores genéticas, solos com pH (Soil/Loam/Flowerbed), pigmentos, cerâmica |
| Genetics 2.6.43 | `Genetics` | Isolador, Sequenciador, Polimerizador, Inoculador, Emendador, soros, Analista, Registro |
| Binnie Core | `BinnieCore` | Kit de Campo, Compartimentos, cilindros/baldes de fluidos, Genesis |

Depende de `forestry` (Baú do Arborista, Carcaça Resistente, Muda Genética).

## Progressão

1. Extra Trees: toras, Engrenagem Aprovada (4 gravetos de carvalho), Serraria (Carcaça Resistente + machado), Marcenaria etc.
2. Botany: Pá de Jardineiro, Medidor de Solo, solos/pH, pigmentos, argila e cerâmica. Marco `marco_botany`.
3. Genetics: Carcaça Reforçada, Placa Integrada, Bancada, Analisador, Incubadora (Enzima), Isolador, Sequenciador, Polimerizador, Inoculador, Emendador. Marco `marco_binnie`; chefe `genesis`.

## Armadilhas

* Serraria e Marcenaria pedem energia (confira no NEI). A Engrenagem Aprovada só aceita gravetos de carvalho.
* O Registro exige o Dicionário do Extra Bees (mod presente, mas sem capítulo).
* Soros e amostras (centenas de variantes) ficaram só como quests representativas.
* Receitas de Genesis, Banco de Dados Mestre e Banco de Dados do Botânico não foram verificadas; as descrições mandam conferir no NEI.
* Flor do Botany é um item único com NBT (`Botany:itemFlower`), por isso as tarefas não distinguem espécies.

## Marcos exportados

* `binnie:marco_botany`
* `binnie:marco_binnie`
