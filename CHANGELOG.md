# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

### Changed

- Replaced the archived `actions-rs/toolchain@v1` action with `dtolnay/rust-toolchain@stable`, and bumped `actions/checkout`, `actions/setup-java`, and `actions/setup-node` to `v5` in `.github/workflows/main.yml` and `.github/workflows/release.yml`, clearing the Node.js 20 runtime and `set-output` deprecation warnings.

### Removed

- Removed the `lint` and `lint-fmt` scripts from `dynmap/package.json`. Both pointed at an `eslint/` config directory that is not in the repository, so they always failed.
- Deleted `.github/workflows/build.yml`, which triggered only on the nonexistent `main` and `develop` branches and duplicated the `nodes/` build already performed by `.github/workflows/main.yml`.

### Fixed

- `/war` aimed at your own nation, or at another town in it, now prints `You cannot declare war on your own nation` or `You cannot declare war on a town in your own nation` instead of `You cannot declare war on yourself`. The war is still refused.
- `/town` sub-commands other than `help`, `list`, and `outpost` now print `Only players in game can use` when run from the console, instead of doing nothing or writing their usage text to the server log, and `/town` alone from the console prints the pointer to `/town help`. `/town list` now works from the console.
- `/nation` sub-commands other than `help` and `list` now print `Only players in game can use` when run from the console, instead of doing nothing, and `/nation` alone from the console prints the pointer to `/nation help`. `/nation list` from the console now replies to the console instead of writing to the server log. `/nation color` by a player without a nation now prints `You do not belong to a nation`, and `/nation accept` without a pending invitation now prints `You have not been invited to any nation` instead of `Invite invalid`.
- Documented the restrictions on each `/nation` sub-command in `COMMANDS.md`: which ones need the town or nation leader, which are blocked during war, that the capital town cannot leave, that an invitation needs the invited town's leader online and expires after 60 seconds, that `/nation spawn` charges `nationTownTeleportCost` and is cancelled with a refund on movement, and what each one does from the console. Documented how `/player` matches names and what `/territory` prints from the console. Corrected the `townSpawnTime` entry in `CONFIG.md`, which said it applies only to the town home when it also times outpost and `/nation spawn` teleports.
- `/ally`, `/unally`, `/war`, `/peace`, `/globalchat`, `/townchat`, `/nationchat`, and `/allychat` now print `Only players in game can use` when run from the console, instead of doing nothing. `/truce` with no argument from the console now prints `Usage from the console: /truce <town>`.
- `/ally`, `/unally`, and `/peace` now print `You do not belong to a town` when run by a player without a town, matching `/war`, instead of doing nothing. `/ally` and `/unally` aimed at your own town now say `You cannot make an alliance with yourself.` and `You cannot break an alliance with yourself.` instead of the peace-treaty message copied from `/peace`.
- Corrected the diplomacy section of `COMMANDS.md`, which said every diplomacy command prints its help with no arguments and is limited to town leaders and officers. `/truce` is not limited to leaders and officers, lists your own town's truces when run with no argument, and works from the console with a town name. Documented that `/unally` and a signed peace treaty start a truce, that war cannot be declared on an ally or a side in a truce, that an alliance cannot be offered to an enemy, and how target names are matched. Corrected the `trucePeriod` entry in `CONFIG.md`, which said a truce only follows the end of a war.
- `/port info` now prints `Invalid port "<name>"` for a name that matches no port instead of printing nothing.
- Port names loaded from `ports.json` are now lowercased, so a port whose name was written there with capital letters can be found by `/port info` and `/port warp`.
- `/nodesadmin port create` with the z coordinate left off now prints its usage message instead of throwing an `ArrayIndexOutOfBoundsException`.
- Corrected the `/nodes` sub-command table in `COMMANDS.md`, which did not say that `resource`, `town`, and `nation` list everything when run without a name, that `player` requires a name, or that `towns`, `nations`, and `players` are aliases. Documented the `/port` warp conditions (same chunk as a port, shared port group, owner access, boat requirement) and how port names are matched. Corrected the `seaLevel` entry in `CONFIG.md`, which described it as a warp eligibility check when it is the Y-level a warp arrives at, and the `USER_GUIDE.md` port section, which tied ports to coastal territories.
- Documented the `/globalchat` `mute` and `unmute` sub-commands (and their `leave` and `join` aliases) in `COMMANDS.md`, along with how the chat commands toggle back to global chat and drop messages sent to a town, nation, or ally channel the player does not belong to. Corrected the `PlayerCommand` KDoc header, which had been copied from `/truce`, and the `/player` console usage message.
- `npm run build` in `dynmap/` now runs `npm run wasm` and `npm run build-js` one after the other instead of concurrently, so webpack no longer starts before the `wasm/` bindings it imports have been generated.
- `/nation help` now lists the `capital`, `accept`, `deny`, `info`, `online`, `rename`, and `spawn` sub-commands, and its `invite` line says it invites a town rather than a nation. `/town help` now lists `officer`, `leader`, `accept`, `deny`, and `income`. All were routed but missing from the in-game help. `COMMANDS.md` now lists the `perms` alias of `/town permissions`.
- Documented the `/nodesadmin` sub-commands in `COMMANDS.md`, including the `town` and `nation` sub-command tables. The entry previously listed only the command's permission and alias.
- Corrected the `/nodesadmin allyremove`, `truce`, and `truceremove` usage messages, which had been copied from `ally`, and the `/nodesadmin town sethomecooldown` usage message, which had been copied from `sethome`.
- `/nodesadmin help` now lists the `resident`, `treaty`, and `playersonline` sub-commands, and `/nodesadmin town` help now lists `claimspenalty`, `removeleader`, `incomeadd`, `incomeremove`, `sethome`, `sethomecooldown`, and `defaulttownspawns`. All were routed but missing from the in-game help. Also corrected the KDoc headers of the `claimsannex` and `sethomecooldown` handlers.
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
