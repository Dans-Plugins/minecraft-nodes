# Commands Reference

All commands provided by the Nodes plugin are listed below. Use `/command help` for in-game sub-command help (the diplomacy commands print their help when run with no arguments instead).

## General Commands

### /nodes \[subcommand\]

**Description:** Display general Nodes information. With no sub-command, prints the plugin version and world counts (resource nodes, territories, residents, towns, nations).  
**Aliases:** `/nd`  
**Permission:** `nodes.command.nodes`  
**Usage:** `/nodes help`

Sub-commands:

| Sub-command | Description |
|-------------|-------------|
| `resource [name]` | Get a resource node's properties |
| `territory [id]` | Get territory info |
| `town [name]` | Get town info |
| `nation [name]` | Get nation info |
| `player [name]` | Get player info |
| `war` | Current war status |

### /player \[name\]

**Description:** View info about yourself or another player (town, nation, claim power).  
**Aliases:** `/p`  
**Permission:** `nodes.command.player`  
**Usage:** `/player` or `/player <name>`

### /territory \[id\]

**Description:** View information about the territory you are standing in, or by ID.  
**Permission:** `nodes.command.territory`  
**Usage:** `/territory` or `/territory <id>`

---

## Town Commands

### /town \<subcommand\>

**Description:** Manage your town.  
**Aliases:** `/t`  
**Permission:** `nodes.command.town`  
**Usage:** `/town help`

Sub-commands (aliases in parentheses):

| Sub-command | Description |
|-------------|-------------|
| `create <name>` (`new`) | Create a new town at your location |
| `delete` (`disband`) | Delete your town |
| `promote <player>` / `demote <player>` | Give or remove officer rank (leader only); `officer <player>` toggles it |
| `leader <player>` | Transfer town leadership |
| `apply <town>` (`join`) | Apply to join a town |
| `invite <player>` | Invite a player to your town (leader/officers) |
| `accept [player]` | Accept a town invitation, or (leader/officers) accept an applicant |
| `deny [player]` (`reject`) | Decline a town invitation, or (leader/officers) reject an applicant |
| `leave` | Leave your town |
| `kick <player>` | Kick a player from your town |
| `spawn` | Teleport to your town spawn point |
| `setspawn` | Set a new town spawn point |
| `list` | List all towns |
| `info [name]` | Show town info |
| `online [name]` | Show a town's online players |
| `color <r> <g> <b>` | Set town color on the map |
| `claim` | Claim the territory you are standing in |
| `unclaim` | Unclaim the territory you are standing in |
| `income` | Collect income from territory bonuses |
| `prefix <prefix>` / `suffix <suffix>` | Set your player name prefix/suffix (`remove` to clear; leaders/officers may pass a player name first) |
| `rename <name>` | Rename your town |
| `map` | View the world map |
| `minimap [3\|4\|5]` | Toggle the sidebar minimap |
| `permissions <type> <group> <allow\|deny>` | Set town protection permissions |
| `protect` | Protect town chests |
| `trust <player>` / `untrust <player>` | Mark or unmark a player as trusted |
| `capital` | Move the town's home territory to your location |
| `annex` | Annex the occupied territory you are standing in |
| `outpost list` / `outpost setspawn` | Manage town outposts |
| `fly` | Toggle fly mode inside your town (requires `nodes.command.town.fly`) |

---

## Nation Commands

### /nation \<subcommand\>

**Description:** Manage your nation.  
**Aliases:** `/n`  
**Permission:** `nodes.command.nation`  
**Usage:** `/nation help`

Sub-commands (aliases in parentheses):

| Sub-command | Description |
|-------------|-------------|
| `create <name>` (`new`) | Create a new nation with your town as its capital |
| `delete` (`disband`) | Delete your nation |
| `leave` | Remove your town from its nation (town leader only) |
| `capital <town>` | Make another town in your nation the capital (nation leader only) |
| `invite <town>` | Invite a town to your nation (capital town leader only) |
| `accept` | Accept a pending nation invitation for your town |
| `deny` (`reject`) | Decline a pending nation invitation |
| `list` | List all nations |
| `info [name]` | Show nation info |
| `online [name]` | Show a nation's online players |
| `color <r> <g> <b>` | Set nation color on the map |
| `rename <name>` | Rename your nation |
| `spawn <town>` | Teleport to a town in your nation (if `allowNationTownSpawn` is enabled; may cost items) |

There is no `kick` sub-command; a town leaves a nation with `/nation leave`.

---

## Diplomacy Commands

The diplomacy commands take a town or nation name directly rather than a sub-command. Running any of them with no arguments prints its help text. Only a town's leader and officers may use them, and when the town belongs to a nation only the capital town may act.

### /ally \<town|nation\>

**Description:** Offer an alliance to another town or nation, or accept one that was offered to you.  
**Permission:** `nodes.command.ally`  
**Usage:** `/ally <town>` or `/ally <nation>`

### /unally \<town|nation\>

**Description:** Break an alliance with another town or nation.  
**Permission:** `nodes.command.unally`  
**Usage:** `/unally <town>` or `/unally <nation>`

### /war \<town|nation\>

**Description:** Declare war on another town or nation. Declaring war on a town that belongs to a nation declares war on the whole nation. With no arguments, prints the current war status.  
**Permission:** `nodes.command.war`  
**Usage:** `/war <town>` or `/war <nation>`

### /peace \<town|nation\>

**Description:** Open a peace treaty GUI with another town or nation. Both sides run the command against each other and negotiate and confirm the terms in the GUI.  
**Permission:** `nodes.command.peace`  
**Usage:** `/peace <town>` or `/peace <nation>`

### /truce \[town\]

**Description:** View active truces and the time remaining on each, for your own town or a named town.  
**Permission:** `nodes.command.truce`  
**Usage:** `/truce` or `/truce <town>`

---

## Chat Commands

### /globalchat

**Description:** Switch your chat to global (all players).  
**Aliases:** `/gc`  
**Permission:** `nodes.command.chat.global`

### /townchat

**Description:** Switch your chat to town-only.  
**Aliases:** `/tc`  
**Permission:** `nodes.command.chat.town`

### /nationchat

**Description:** Switch your chat to nation-only.  
**Aliases:** `/nc`  
**Permission:** `nodes.command.chat.nation`

### /allychat

**Description:** Switch your chat to ally-only.  
**Aliases:** `/ac`  
**Permission:** `nodes.command.chat.ally`

---

## Port Commands

### /port \<subcommand\>

**Description:** Use port warps to travel between coastal territories.  
**Permission:** `nodes.command.port`  
**Usage:** `/port help`

Sub-commands:

| Sub-command | Description |
|-------------|-------------|
| `list` | List all available ports |
| `info <name>` | Print info about a port |
| `warp <name>` | Warp to a port |

---

## Admin Commands

### /nodesadmin \<subcommand\>

**Description:** Administrative commands for managing the Nodes plugin.  
**Aliases:** `/nda`  
**Permission:** `nodes.admin`  
**Usage:** `/nodesadmin help`
