# User Guide

## Prerequisites

Before using Nodes, ensure the following are installed on your server:

- A Paper or Spigot server (1.21+)
- The [Kotlin runtime plugin](https://github.com/d-z4/minecraft-kotlin)
- (Optional) [Dynmap](https://www.spigotmc.org/resources/dynmap.274/) for live map support

## First Steps

After installing the plugin and starting your server:

1. The plugin will create its configuration files in `plugins/nodes/`.
2. Review `plugins/nodes/config.yml` and adjust settings to suit your server (see [CONFIG.md](CONFIG.md)).
3. The world map data is stored in `plugins/nodes/world.json` and `plugins/nodes/towns.json`.

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

| Permission | Default | Description |
|------------|---------|-------------|
| `nodes.admin` | op | Access to all `/nodesadmin` commands |
| `nodes.command.town` | true | Use `/town` commands |
| `nodes.command.nation` | true | Use `/nation` commands |
| `nodes.command.nodes` | true | Use `/nodes` info commands |
| `nodes.command.ally` | true | Use `/ally` commands |
| `nodes.command.unally` | true | Use `/unally` commands |
| `nodes.command.war` | true | Use `/war` commands |
| `nodes.command.peace` | true | Use `/peace` commands |
| `nodes.command.truce` | true | Use `/truce` commands |
| `nodes.command.chat.global` | true | Use `/globalchat` |
| `nodes.command.chat.town` | true | Use `/townchat` |
| `nodes.command.chat.nation` | true | Use `/nationchat` |
| `nodes.command.chat.ally` | true | Use `/allychat` |
| `nodes.command.player` | true | Use `/player` info command |
| `nodes.command.territory` | true | Use `/territory` info command |
| `nodes.command.port` | true | Use `/port` commands |
| `nodes.command.town.fly` | op | Fly within your town |
