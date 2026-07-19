# Plague & Pain

Disease and injury addon for **Nova** with progressive conditions, treatment items, guide/status GUIs, and transmissible blood samples.

## Features

- Diseases: `cough`, `fever`, `plague`, `pneumonia`, `rabies`
- Injuries: `broken_leg`, `open_wound`
- Guide + status GUIs (`/pp`, `/pp guide`, `/pp status`)
- Progress-based severity and effects
- Treatment with bandage/medicine
- Disease immunity progression (configurable reduction after successful cures)
- Disease + injury reset on death
- Plague death behavior: dying from plague damage spawns a zombie wearing the player head
- Blood syringe loop:
  - Sneak + right click: self use
  - Normal right click player: target use
  - Syringe samples blood (always creates blood syringe)
  - Infected blood syringe can infect self/others

## Commands

Base command: `/plagueandpain`  
Aliases: `/ppguide`, `/pp`, `/pap`, `/plague`, `/pain`

### Player commands

- `/pp`
- `/pp guide`
- `/pp conditions`
- `/pp status`

Permission required: `plagueandpain.use`

### Admin commands

- `/pp admin give <player> disease <disease> [progress]`
- `/pp admin give <player> injury <injury> [progress]`
- `/pp admin clear <player> disease`
- `/pp admin clear <player> injury`
- `/pp admin clear <player> all`

`progress` is optional (`0..100`).

Permission required: `plagueandpain.admin`

## Blood syringe usage

### Sampling (`syringe`)

1. Sneak + right click (air/block): sample yourself
2. Right click another player (not sneaking): sample that player

Result:
- You always get a `blood_syringe`
- If sampled player has a disease, the disease is stored in that blood syringe
- If sampled player has no disease, the blood syringe is empty (not injectable)

### Injecting (`blood_syringe`)

1. Sneak + right click (air/block): inject yourself
2. Right click another player (not sneaking): inject target player

If the blood syringe contains disease data, it attempts to infect the target.

## Configuration

Config files are under `src/main/resources/configs/`.

Global:
- `config.yml`
  - `injury.bandage_relief`
  - `disease.medicine_relief`
  - `disease.immunity_chance_reduction_per_heal` (default `5.0`)
  - `disease.armor_pieces_for_immunity`

Per-condition:
- `cough.yml`
- `fever.yml`
- `plague.yml`
- `pneumonia.yml`
- `rabies.yml`
- `broken_leg.yml`
- `open_wound.yml`

Progression defaults are tuned to be slower than before.

## Build

From repository root:

```bash
./gradlew build
```
