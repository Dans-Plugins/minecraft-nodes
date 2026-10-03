# User Guide

## Prerequisites

Before using Nodes, ensure the following are installed on your server:

- A Paper or Spigot server (1.21+)
- The [Kotlin runtime plugin](https://github.com/d-z4/minecraft-kotlin)
- (Optional) [Dynmap](https://www.spigotmc.org/resources/dynmap.274/) for live map support

## First Steps

The plugin does not generate a world map. Before Nodes can run, you must supply a `world.json` that defines the resource nodes and territories:

1. Start the server once with the plugin installed. The plugin writes a default `plugins/nodes/config.yml` if none exists.
2. Review `plugins/nodes/config.yml` and adjust settings to suit your server (see [CONFIG.md](CONFIG.md)).
3. Create the map with the [Dynmap Editor](https://editor.nodes.soy/earth.html) (source in [`dynmap/`](dynmap/README.md)), which saves it as `world.json`, and place the file at `plugins/nodes/world.json`.
4. Restart the server. Nodes reads `world.json` but never writes it. It creates and saves `towns.json`, `war.json`, `truce.json`, `ports.json`, and its backups in `plugins/nodes/` itself.

If `plugins/nodes/world.json` is missing or cannot be parsed and `disableWorldWhenLoadFails` is `true` (the default), Nodes stops loading partway. Its commands only reply with their usage message, and players cannot break or place blocks or interact with anything on the server. The server log shows `Error loading world`.

## Common Scenarios

### Creating a Town

1. Stand in the territory you want as your town's home.
2. Run `/town create <name>` to create a town there.
3. Use `/town setspawn` to set the town's spawn point (players teleport to it with `/town spawn`).
4. Invite players with `/town invite <player>`; they join with `/town accept`.

### Claiming Territory

1. Stand in the territory you want to claim. It must neighbor one of your existing claims.
2. Run `/town claim` to claim the territory for your town.
3. Check your town's remaining claim power with `/town info`.

### Forming a Nation

1. As a town leader, run `/nation create <name>` to create a nation with your town as its capital.
2. Invite other towns with `/nation invite <town>`; the invited town's leader accepts with `/nation accept`.

### Declaring War

1. Run `/war <town|nation>` to declare war on another town or nation.
2. During war, plant a flag in an enemy chunk to begin capturing it.
3. Defend your own chunks by breaking enemy flags.
4. End the conflict with `/peace <town|nation>`, which opens a peace treaty GUI. The other side opens the same treaty with `/peace <your town|nation>`, and both parties negotiate and confirm the terms there.

### Using Chat Channels

| Command | Description |
|---------|-------------|
| `/globalchat` or `/gc` | Switch to global chat |
| `/townchat` or `/tc` | Switch to town-only chat |
| `/nationchat` or `/nc` | Switch to nation-only chat |
| `/allychat` or `/ac` | Switch to ally-only chat |

### Port Warps

Ports allow quick travel between coastal territories. Run `/port list` to see available ports, then `/port warp <name>` to warp.

## Permissions

Only `nodes.command.town.fly` is declared in the plugin's `plugin.yml`. Bukkit gives every undeclared permission node a default of `op`, so out of the box only operators can run any Nodes command. To let ordinary players use the plugin, grant the player-facing nodes below with a permissions plugin (for example, LuckPerms).

| Permission | Default | Description |
|------------|---------|-------------|
| `nodes.admin` | op | Access to all `/nodesadmin` commands |
| `nodes.command.town` | op | Use `/town` commands |
| `nodes.command.nation` | op | Use `/nation` commands |
| `nodes.command.nodes` | op | Use `/nodes` info commands |
| `nodes.command.ally` | op | Use `/ally` commands |
| `nodes.command.unally` | op | Use `/unally` commands |
| `nodes.command.war` | op | Use `/war` commands |
| `nodes.command.peace` | op | Use `/peace` commands |
| `nodes.command.truce` | op | Use `/truce` commands |
| `nodes.command.chat.global` | op | Use `/globalchat` |
| `nodes.command.chat.town` | op | Use `/townchat` |
| `nodes.command.chat.nation` | op | Use `/nationchat` |
| `nodes.command.chat.ally` | op | Use `/allychat` |
| `nodes.command.player` | op | Use `/player` info command |
| `nodes.command.territory` | op | Use `/territory` info command |
| `nodes.command.port` | op | Use `/port` commands |
| `nodes.command.town.fly` | op | Fly within your town |
