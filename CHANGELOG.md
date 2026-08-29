# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

### Removed

- Deleted `.github/workflows/build.yml`, which triggered only on the nonexistent `main` and `develop` branches and duplicated the `nodes/` build already performed by `.github/workflows/main.yml`.

### Changed

- Replaced the archived `actions-rs/toolchain@v1` action with `dtolnay/rust-toolchain@stable`, and bumped `actions/checkout`, `actions/setup-java`, and `actions/setup-node` to `v5` in `.github/workflows/main.yml` and `.github/workflows/release.yml`, clearing the Node.js 20 runtime and `set-output` deprecation warnings.

### Fixed

- Corrected the `usage` string for `/nodesadmin` in `plugin.yml`, which advertised an unregistered `/na` alias instead of the registered `/nda`.
- Corrected the `usage` string for `/truce` in `plugin.yml`, which had been copied from `/peace` and told players to run `/peace help`.
- Documented the optional town argument accepted by `/truce` in `COMMANDS.md`.
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
