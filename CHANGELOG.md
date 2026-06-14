# Changelog

## 0.1.2.3 - 2026-06-14

- Restored accented translations for French, German, Spanish and Portuguese.
- Reset Spore Inquisition's `capullo` startup counter before the delayed start hook so the primordial age 10 mound and apostles can spawn after dormant startup.
- Prevented `/inqdel start` from re-running the Inquisition start hook when the infection is already active.

## 0.1.2.2 - 2026-06-14

- Fixed a NeoForge startup crash caused by the message preset config validator.
- Replaced `defineInList` with a null-safe validator so config creation and correction cannot crash when NeoForge tests missing values.

## 0.1.2.1 - 2026-06-14

- Hotfixed config loading so Inquisition Delayed only reacts to its own NeoForge config events.
- This makes startup more defensive in large modpacks where many configs are loaded during the same modloading phase.
- Current available crash reports point to other mods, but this removes the only recent fragile startup path in Inquisition Delayed.

## 0.1.2 - 2026-06-13

- Fixed Inquisition hook execution so built-in functions keep the original command source position and dimension.
- This allows relative-coordinate Inquisition functions such as the primordial mound startup to run from the player, command block or function source that triggered `/inqdel start`.

## 0.1.1 - 2026-06-13

- Bumped the mod version after the post-test polish pass.
- Added configurable start/stop message presets.
- Added English, French, German, Spanish and Portuguese translations.
- Moved public start/stop messages out of mcfunctions so they use the configured preset and client language.
- Included README, changelog and license files in the jar.

## 0.1.0 - 2026-06-12

- Created the initial NeoForge 1.21.1 addon.
- Added persistent world state with `dormant` as the default and `active` after `/inqdel start`.
- Added `/inqdel start`, `/inqdel stop` and `/inqdel status`.
- Added built-in functions `inqdel:start_inquisition`, `inqdel:stop_inquisition` and `inqdel:status`.
- Added server-side spawn gating for Spore entities while dormant.
- Added cleanup of already loaded gated Spore entities when stopping the infection.
- Added the mod logo to the jar metadata.
