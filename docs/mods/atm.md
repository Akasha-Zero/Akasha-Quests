# Akashic ATM Family — capítulos `atm_*` (sem a Estrela)

## Visão geral
* **Mod:** Akashic ATM Family 0.1.0-alpha.10 (`allthemodiumlegacy`; módulos All The Modium, All The
  Tweaks, All The Compressed). Guia in-game: `assets/allthemodiumlegacy/guide.json` (fonte dos fatos).
* **Capítulos** (`quests/chapters/atm/`, prefixo ATM): `atm_bemvindo` 900 (8, info), `atm_comeco` 905
  (44), `atm_allthemodium` 910 (110), `atm_compressed` 915 (59), `atm_tweaks` 920 (40),
  `atm_bounty` 930 (42, repetíveis 86400 s), `atm_dicas` 935 (27, info). `atm_star` é de outro autor.
* Raiz de `atm_allthemodium` sem deps: `quests/chapters/story/ato4.yaml` não existe.

## Progressão
* Cap. 1: vanilla → Nether (Wither, Estrela do Nether) → Fim (Dragão) → EnderIO (Alloy Smelter,
  Soularium) → Enderium (Thermal) → pacote: 8 Enderium + 8 Soularium + 1 Estrela do Nether.
* Cap. 2: Allthemodium bruto (Alloy Smelter, 150 000 RF) → Vibranium bruto (1 bloco Allthemodium +
  placa de irídio IC2 + 8 Enderium, 250 000 RF) → Unobtainium bruto (1 bloco Vibranium + Awakened
  Core + 8 Vibrant Alloy, 500 000 RF). Ferramentas/armaduras: netherita → ATM → Vib → Unob via molde de
  ferraria. Ligas = 2 metais + Coração de Piglich (Alloy Smelter).
* Dimensões pela Teleport Pad (Overworld↔Mineração 240, Nether↔O Outro 241, End↔O Além 242).

## Armadilhas
* Unobtainium NÃO gera na Mineração; geração em Overworld/Nether/End é opcional e vem desligada.
* Miniaturas de portal são só decoração. Armaduras ATM não dão voo. Waystones não cruzam dimensões.
* Peças do Tinkers usam durabilidade do Tinkers; ferramentas prontas do mod não gastam.
* `patrick_star`, `mini_*` e `trophy_atm` aparecem em `atm_tweaks` e também em `atm_star`
  (sobreposição aceita; cuidado ao ajustar recompensas).
* Entidades de caça: Dragão = `etfuturum.ender_dragon`; Piglich = `allthemodiumlegacy.piglich`.

## Marcos exportados
* `atm_comeco:estrela_nether`, `atm_comeco:o_pacote`, `atm_comeco:allthemodium_bruto`
* `atm_allthemodium:atm_lingote`, `atm_allthemodium:vib_lingote`, `atm_allthemodium:unob_lingote`
* `atm_allthemodium:piglich` (boss), `atm_allthemodium:coracao_piglich`
* `atm_allthemodium:liga_paxel`, `atm_allthemodium:allthemodium` (final do capítulo)
* `atm_tweaks:trofeu_atm`, `atm_tweaks:patrick_star`
