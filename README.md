# Inquisition Delayed

Small server-side NeoForge 1.21.1 addon for modpacks using **Fungal Infection: Spore** and **Spore Inquisition**.

The mod keeps the Spore infection dormant on new worlds, then lets a pack author or server operator activate it later.

It does not add items, blocks or gameplay content. It only controls when Spore/Inquisition are allowed to start producing active infected entities.

## What Stays Available While Dormant

- Spore blocks
- Spore items
- Recipes
- Structures
- World generation
- Pack progression around Spore content

## What Is Blocked While Dormant

- Targeted Spore infected entity spawns
- Direct entity additions caught by NeoForge entity join events
- Spore entities already loaded when `/inqdel stop` is used

The goal is to avoid an active outbreak before the pack decides to start it.

## Commands

`/inqdel status`

Shows whether the infection state is `dormant` or `active`.

Permission: available to all players.

`/inqdel start`

Sets the persistent world state to `active`, then runs the built-in Inquisition start hook.

Permission: level 2.

`/inqdel stop`

Sets the persistent world state back to `dormant`, removes currently loaded gated Spore entities, then runs the built-in Inquisition stop hook.

Permission: level 2.

## Function Entry Points

These functions are bundled inside the mod jar:

- `function inqdel:start_inquisition`
- `function inqdel:stop_inquisition`
- `function inqdel:status`

Use them from command blocks, FTB Quests, datapacks or other pack logic when a function entry point is easier than calling `/inqdel` directly.

The public start and stop functions call the mod command internally, so the persistent world state remains synchronized.

## Config

The mod creates a common config file:

`config/inq_delayed-common.toml`

Available option (for now):
`messagePreset`

Allowed values:  `sober`,`dark_fantasy`,`fragile_calm`,`technical`
(This controls the public start and stop messages sent by `/inqdel start` and `/inqdel stop`)
Default: `sober`

Bundled languages:
`en_us`, `fr_fr`, `de_de`, `es_es`, `pt_br`, `pt_pt`

## Internal Functions

The command implementation calls these internal hooks:

- `inqdel:internal/start_inquisition`
- `inqdel:internal/stop_inquisition`

They are included so the mod can trigger the Spore Inquisition function chain after its own persistent state has changed.

Pack logic should normally call the public functions or `/inqdel`, not the internal functions.

## Spawn Gating

While dormant, the mod blocks targeted entities through:

- spawn placement checks
- spawn position checks
- finalize-spawn cancellation
- entity join cancellation

Primary target:

`#spore:fungus_entities`

Pack extension tag:

`#inq_delayed:blocked_spore_entities`

Additional namespace fallback:

- `spore`
- `spore_inquisition`
- `sporeinquisition`

The fallback exists so the dormant state still catches Spore entities if a future version changes or omits expected tags.

## Persistence

The infection state is stored in world saved data.

New worlds start as `dormant`.

## Expected Use In A Pack

1. Install Fungal Infection: Spore.
2. Install Spore Inquisition.
3. Install Inquisition Delayed.
4. Start a world normally; the infection should remain dormant.
5. Trigger `/inqdel start` or `function inqdel:start_inquisition` when the pack progression reaches the outbreak point.
6. Use `/inqdel stop` or `function inqdel:stop_inquisition` only for admin recovery, testing or scripted rollback.

## Scope

This mod does not modify or redistribute code from Fungal Infection: Spore or Spore Inquisition.

It only uses NeoForge server events, entity tags, entity namespaces and bundled Minecraft functions.

Report issues here only when they involve:

- `/inqdel` commands
- `inqdel:*` functions
- dormant or active state persistence
- entity gating before activation

Report Spore mechanics, balance, content or infection behavior after activation to the original projects.

## Dependencies

- Minecraft 1.21.1
- NeoForge 21.x
- Fungal Infection: Spore
- Spore Inquisition

## License Notes

Inquisition Delayed is licensed under Apache-2.0.

Fungal Infection: Spore is an external ARR project.

Spore Inquisition is an external MIT project.
