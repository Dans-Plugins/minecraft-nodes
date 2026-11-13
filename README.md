# Minecraft Nodes

![Nodes map screenshot](docs/nodes_map_example.jpg)

## Description

Nodes is a Minecraft plugin that adds a territory-based geopolitics system to your server. Players form towns and nations, claim territories, manage resources, form alliances, and wage war. The plugin also ships a Dynmap viewer/editor extension for visualising the world map.

## Installation

### First Time Installation

1. Download the latest release JAR from the [Releases page](https://github.com/Dans-Plugins/minecraft-nodes/releases).
2. Place `nodes-VERSION.jar` in the `plugins` folder of your Paper/Spigot server.
3. Restart your server.
4. Supply a `world.json` map at `plugins/nodes/world.json` and restart again. The plugin does not generate one; see [First Steps](USER_GUIDE.md#first-steps) in the User Guide.

### Companion Plugins

- **Kotlin runtime** – Nodes requires a Kotlin runtime plugin on the server. See [minecraft-kotlin](https://github.com/d-z4/minecraft-kotlin) for a compatible runtime.
- **Dynmap** (optional) – Install [Dynmap](https://www.spigotmc.org/resources/dynmap.274/) and the included Dynmap extension for an interactive live map. See `dynmap/README.md` for details.

## Usage

### Documentation

- [User Guide](USER_GUIDE.md) – Getting started and common scenarios
- [Commands Reference](COMMANDS.md) – Complete list of all commands
- [Configuration Guide](CONFIG.md) – Detailed configuration options
- [Changelog](CHANGELOG.md) – Release-by-release summary of changes

### Wiki & Additional Resources

- [Wiki Guide](https://github.com/d-z4/minecraft-nodes/wiki)
- [Dynmap Editor](https://editor.nodes.soy/earth.html)

## Support

You can find the support Discord server [here](https://discord.gg/xXtuAQ2).

### Experiencing a bug?

Please fill out a bug report [here](https://github.com/Dans-Plugins/minecraft-nodes/issues/new).

- [Known Bugs](https://github.com/Dans-Plugins/minecraft-nodes/issues?q=is%3Aissue+is%3Aopen+label%3Abug)

## Contributing

- [CONTRIBUTING.md](CONTRIBUTING.md)
- [Notes for Developers](https://github.com/d-z4/minecraft-nodes/wiki)

## Testing

Unit tests live in `nodes/src/test/kotlin/` and run on JUnit 5 without a server. They cover config loading, the territory income split, the hidden ore rule, chunk coordinate parsing, name validation, port save data, the enum-keyed array map, and the string helpers used by town serialization. The build compiles the Kotlin sources, runs the tests, and runs `ktlintCheck`.

Linux:

```
cd nodes
./gradlew clean build
```

Windows:

```
cd nodes
.\gradlew.bat clean build
```

`BUILD SUCCESSFUL` means the plugin compiles, is style-clean, and passes the unit tests. Commands, listeners, and war logic have no tests. To check their behaviour, build the JAR and run it on a local Paper/Spigot server as described in [CONTRIBUTING.md](CONTRIBUTING.md).

## Development

### Repository Structure

```
minecraft-nodes/
 ├─ nodes/      – Main nodes server plugin (Kotlin + Paper)
 └─ dynmap/     – Dynmap viewer/editor (Node.js + Rust/WASM)
```

### Building the Main Plugin

Requirements: Java JDK 21

```
cd nodes
./gradlew build
```

The built JAR appears in `nodes/build/libs/`.

### Building the Dynmap Viewer/Editor

See `dynmap/README.md` for full instructions. Requirements: Node.js and Rust.

## Authors and Acknowledgement

### Developers

| Name | Main Contributions |
|------|--------------------|
| phonon | Original plugin author |
| Jonathan | Coding + map painting |
| Doneions | Coding + testing |
| dmccoystephenson | Maintenance + ongoing development |

## License

This project is licensed under the [GNU General Public License v3.0](LICENSE.md) (GPL-3.0).

You are free to use, modify, and distribute this software, provided that:

- Source code is made available under the same license when distributed.
- Changes are documented and attributed.
- No additional restrictions are applied.

See the [LICENSE.md](LICENSE.md) file for the full text of the GPL-3.0 license.

## Project Status

This project is in active development.

See [TODO.md](TODO.md) for the current high-level task list.
