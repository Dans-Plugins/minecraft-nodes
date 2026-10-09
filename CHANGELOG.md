# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

### Changed

- Replaced the archived `actions-rs/toolchain@v1` action with `dtolnay/rust-toolchain@stable`, and bumped `actions/checkout`, `actions/setup-java`, and `actions/setup-node` to `v5` in `.github/workflows/main.yml` and `.github/workflows/release.yml`, clearing the Node.js 20 runtime and `set-output` deprecation warnings.

### Removed

- Deleted `.github/workflows/build.yml`, which triggered only on the nonexistent `main` and `develop` branches and duplicated the `nodes/` build already performed by `.github/workflows/main.yml`.

### Fixed

- Documented the `/nodesadmin` sub-commands in `COMMANDS.md`, including the `town` and `nation` sub-command tables. The entry previously listed only the command's permission and alias.
- Corrected the `/nodesadmin allyremove`, `truce`, and `truceremove` usage messages, which had been copied from `ally`, and the `/nodesadmin town sethomecooldown` usage message, which had been copied from `sethome`.
- `/nodesadmin resident` with no arguments now prints the resident help instead of the town help, and that help is headed "Admin resident management" instead of "Admin town management".
- Corrected the "First Steps" section of `USER_GUIDE.md`, which implied the plugin generates `world.json`. The plugin only reads it, so a server operator must create the map with the Dynmap Editor and place it at `plugins/nodes/world.json`. The section now also describes what happens when that file is missing.
- Corrected the `dynmap/README.md` folder tree, which listed a nonexistent `src/dynmap_dummy/` and omitted `src/territory/`, `src/lib.rs`, and the generated `wasm/` directory. The build section now says commands run in `dynmap/` rather than "the repo root", and notes that `npm run build` starts the wasm and webpack steps concurrently.
- Corrected the `Default` column of the `USER_GUIDE.md` permissions table. It listed `true` for fifteen command nodes, but none of them is declared in `plugin.yml`, so Bukkit's default of `op` applies.
- Corrected four `CONFIG.md` entries to match how `Config.kt` reads them. `townCreateCooldown` is started only by deleting a town, not by leaving one. `townCooldownUpdateTick` is not read by the plugin, and cooldowns tick on `mainPeriodicTick`. `initialOverClaimsAmountScale` is an integer, not a float. `onlyWhitelistCanAnnex` and `onlyWhitelistCanClaim` apply only while `warWhitelist` is non-empty, and `onlyWhitelistCanClaim` governs war-flag captures rather than `/town claim`.
- Corrected the `/town`, `/nation`, and `/port` sub-command tables in `COMMANDS.md`, which listed sub-commands that do not exist (`/town sethome`, `/town home`, `/town setleader`, `/nation join`, `/nation kick`, `/nation setleader`) and omitted many that do (`/town setspawn`, `/town spawn`, `/town leader`, `/town apply`, `/town accept`, `/nation accept`, `/nation capital`, `/port info`, and others). Added the `/nodes` sub-command table.
- Corrected the diplomacy command entries in `COMMANDS.md`: `/ally`, `/unally`, `/war`, and `/peace` take a town or nation name directly rather than a sub-command, and print help when run with no arguments.
- Corrected the `USER_GUIDE.md` walkthroughs, which told players to run the nonexistent `/town sethome`, `/war declare`, and `/peace request`.
- Added `.kotlin` to `nodes/.gitignore` so the compiler-state directory written by the Kotlin 2.x Gradle plugin during a build is not shown as untracked or accidentally staged.
- Listed Java JDK 21 under the requirements in `CONTRIBUTING.md`; the requirement was stated in `README.md` but omitted from the contributor guide, and the build fails dependency resolution on older JDKs.
- Corrected the `usage` string for `/nodesadmin` in `plugin.yml`, which advertised an unregistered `/na` alias instead of the registered `/nda`.
- Corrected the `usage` string for `/truce` in `plugin.yml`, which had been copied from `/peace` and told players to run `/peace help`.
- Documented the optional town argument accepted by `/truce` in `COMMANDS.md`.
- Corrected the `usage` strings for `/ally`, `/unally`, `/war`, and `/peace` in `plugin.yml`, which advertised a `help` sub-command that none of the executors accepts (`/war help` replied `Town or nation "help" does not exist`).
- Corrected the `/town rename` usage message, which had been copied from `/nation rename` and told players to run `/n rename`.
- `nodes/gradlew` is now tracked with the executable bit set, so `./gradlew` runs from a fresh clone without a preparatory `chmod`. The workaround `chmod` steps have been removed from `.github/workflows/main.yml` and `.github/workflows/release.yml`.
- Corrected `README.md` and `CONTRIBUTING.md` links that pointed at `dmccoystephenson/minecraft-nodes` instead of `Dans-Plugins/minecraft-nodes`.
- Removed instructions to run a unit test suite that does not exist; `README.md` and `CONTRIBUTING.md` now describe the build and the manual server test as the actual verification steps.
- Corrected the `CONTRIBUTING.md` branch workflow to use `master`, the repository's only branch, instead of a nonexistent `develop`.

## [0.0.14] – 2025-01-01

### Changed

- Updated to Paper 1.21.10 API.
- Updated Kotlin to 2.3.0-RC.

## [0.0.13] – 2024-01-01

### Changed

- Internal maintenance and dependency updates.
