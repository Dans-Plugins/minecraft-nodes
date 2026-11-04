# Configuration Guide

All configuration options for the Nodes plugin live in `plugins/nodes/config.yml`. Options are listed in the order they appear in the default configuration file.

Each `Default` below is the value in the default `config.yml`. The plugin writes that file only when none exists, so a key deleted from an existing file uses a built-in value instead. The built-in values match the defaults except for these:

| Key | Built-in value |
|---|---|
| `cropsMinYHeight`, `breedingMinYHeight` | `10` |
| `playerClaimsInitial` / `playerClaimsMax` / `playerClaimsIncrease` | `0` / `20` / `1` |
| `chunkAttackTime` | `200` |
| `trucePeriod` | `172800000` (48 hours) |
| `portWarpTime` | 100 seconds |
| `outpostTeleportCost`, `nationTownTeleportCost`, `globalResources` | Empty |

`/nodesadmin reload config` re-reads the file, but `savePeriod`, `mainPeriodicTick`, `overMaxClaimsReminderPeriod`, and `nametagUpdatePeriod` keep their old timing until `/nodesadmin reload managers` or a server restart.

---

## Engine Settings

### disableWorldWhenLoadFails

**Type:** boolean  
**Default:** `true`  
**Description:** Disables world interactions (block placing, breaking, etc.) when `world.json` or `towns.json` fails to load. This prevents corruption from partially loaded data.

**Example:**
```yaml
disableWorldWhenLoadFails: true
```

---

### savePeriod

**Type:** integer (ticks)  
**Default:** `600`  
**Description:** How often plugin data is saved to disk, in server ticks (20 ticks = 1 second).

---

### backupPeriod

**Type:** integer (milliseconds)  
**Default:** `3600000` (1 hour)  
**Description:** How often plugin data is backed up, in milliseconds. A backup is taken during the first save after this period has passed.

---

### mainPeriodicTick

**Type:** integer (ticks)  
**Default:** `1200`  
**Description:** Interval for the main background task, in ticks. That task checks whether income is due and updates town, resident, and truce cooldowns.

---

### overMaxClaimsReminderPeriod

**Type:** integer (ticks)  
**Default:** `24000`  
**Description:** How often players are reminded that their town is over its maximum claims, in ticks.

---

### nametagUpdatePeriod

**Type:** integer (ticks)  
**Default:** `80`  
**Description:** How often player nametags are refreshed, in ticks.

---

### nametagPipelineTicks

**Type:** integer  
**Default:** `16`  
**Description:** Number of ticks over which nametag updates are spread to reduce server load.

---

### dynmapCopyTowns

**Type:** boolean  
**Default:** `false`  
**Description:** Copies the saved world, town, and port data to the Dynmap folder even when the Dynmap plugin is not detected. When Dynmap is detected, the data is copied whatever this is set to.

---

## Nametag Settings

### useNametags

**Type:** boolean  
**Default:** `true`  
**Description:** Enables the internal armorstand-based nametag system for displaying player town/nation info above their heads.

---

## AFK Settings

### afkKickTime

**Type:** integer (milliseconds)  
**Default:** `900000` (15 minutes)  
**Description:** Claim power progress a player loses when kicked for idling, in milliseconds. It is taken off the timer that counts towards [`playerClaimsIncreasePeriod`](#townclaimspenaltydecayperiod--playerclaimsincreaseperiod), which stops at zero. The plugin does not detect idle players or kick them itself. It reacts to a server kick with the reason `You have been idle for too long!`, so the idle time is set on the server, not here.

---

## General Permissions

### canInteractInEmpty

**Type:** boolean  
**Default:** `false`  
**Description:** Whether players can build, destroy, and interact in areas with no territories defined. When enabled, this also applies in unclaimed territories, whatever `canInteractInUnclaimed` is set to.

---

### canInteractInUnclaimed

**Type:** boolean  
**Default:** `true`  
**Description:** Whether players can build, destroy, and interact in territories that have not been claimed by any town.

---

### requireSheepNodeToShear

**Type:** boolean  
**Default:** `true`  
**Description:** When enabled, players can only shear sheep in territories that have the sheep resource node.

---

## Town Settings

### townCreateCooldown

**Type:** integer (milliseconds)  
**Default:** `172800000` (48 hours)  
**Description:** Cooldown after a player deletes their town with `/town delete` (or `/town disband`) before they can create a new one. Leaving a town does not start this cooldown.

---

### townMoveHomeCooldown

**Type:** integer (milliseconds)  
**Default:** `172800000` (48 hours)  
**Description:** Cooldown between town home relocations.

---

### townCooldownUpdateTick

**Type:** integer (ticks)  
**Default:** `2400`  
**Description:** Present in the default `config.yml` but not read by the plugin, so changing it has no effect. Town, resident, and truce cooldowns are updated on the [`mainPeriodicTick`](#mainperiodictick) interval.

---

### townSpawnTime

**Type:** integer (seconds)  
**Default:** `10`  
**Description:** Warm-up time in seconds before a `/town spawn` teleport (to the town spawn or an outpost) or a `/nation spawn` teleport happens. Moving to another block during the warm-up cancels the teleport.

---

### outpostTeleportCost

**Type:** map (item: quantity)  
**Default:** `diamond: 1`  
**Description:** Items consumed when a player teleports to an outpost.

**Example:**
```yaml
outpostTeleportCost:
  diamond: 1
```

---

## Nation Settings

### allowNationTownSpawn

**Type:** boolean  
**Default:** `false`  
**Description:** Whether players can teleport to other towns within their nation.

---

### nationTownTeleportCost

**Type:** map (item: quantity)  
**Default:** `diamond: 1`  
**Description:** Items consumed when teleporting to another town in the same nation.

---

### allowNationFriendlyFire

**Type:** boolean  
**Default:** `false`  
**Description:** Whether members of the same town or nation can attack each other.

---

## Alliance Settings

### allowAllyFriendlyFire

**Type:** boolean  
**Default:** `true`  
**Description:** Whether members of allied towns/nations can attack each other.

---

## Town Claim Settings

### territoryCostBase

**Type:** integer  
**Default:** `10`  
**Description:** Base claim cost for a territory (`cost = base + scale * chunks`).

---

### territoryCostScale

**Type:** float  
**Default:** `0.25`  
**Description:** Per-chunk scaling factor for territory claim cost.

---

### townInitialClaims

**Type:** integer  
**Default:** `25`  
**Description:** Starting claim power for the leader of a new town. The leader starts with the larger of this and `playerClaimsMax`, minus any penalty from [`initialOverClaimsAmountScale`](#initialoverclaimsamountscale).

---

### initialOverClaimsAmountScale

**Type:** integer  
**Default:** `2`  
**Description:** Penalty scale for a new town whose home territory costs more than `townInitialClaims`. The leader loses `initialOverClaimsAmountScale * (territory cost - townInitialClaims)` starting claim power.

---

### townClaimsBase

**Type:** integer  
**Default:** `20`  
**Description:** Base claim power given to every town.

---

### townClaimsMax

**Type:** integer  
**Default:** `-1` (unlimited)  
**Description:** Maximum total claim power a town can have. Set to `-1`, or any value that is not positive, for no limit.

---

### playerClaimsInitial / playerClaimsMax / playerClaimsIncrease

**Type:** integer  
**Defaults:** `1` / `25` / `2`  
**Description:** Claim power granted on town join (`playerClaimsInitial`), the per-player cap (`playerClaimsMax`), and how much claim power increases per period (`playerClaimsIncrease`).

---

### townPenaltyDecay

**Type:** integer  
**Default:** `2`  
**Description:** Amount by which a town's claims penalty decreases each decay period. Unclaiming a territory that was not annexed adds its cost to this penalty, which lowers the town's maximum claim power until it decays.

---

### townClaimsPenaltyDecayPeriod / playerClaimsIncreasePeriod

**Type:** integer (milliseconds)  
**Default:** `3600000` (1 hour)  
**Description:** Period between town penalty decay ticks and player claim power gain ticks. Players only gain claim power while online.

---

### overClaimsPenalty

**Type:** boolean  
**Default:** `true`  
**Description:** Enables the overclaim penalty system, reducing resource yields when a town exceeds its maximum claims.

---

### overClaimsMaxPenalty

**Type:** float  
**Default:** `0.5`  
**Description:** Share of resources lost while a town is over its maximum claims, so a higher value is a harsher penalty. With `0.5`, income is cut by 50%, and each hidden ore drop, crop harvest, and bred animal has a 50% chance of being lost.

---

### overClaimsAllowClaim

**Type:** boolean  
**Default:** `false`  
**Description:** Whether towns can continue claiming territories even after exceeding their maximum claim power.

---

## Resource Settings

### incomeEnabled

**Type:** boolean  
**Default:** `true`  
**Description:** Enables the periodic territory income system.

---

### incomePeriod

**Type:** integer (milliseconds)  
**Default:** `3600000` (1 hour)  
**Description:** How often territory income is distributed.

---

### incomeScaleByClaimPower

**Type:** boolean  
**Default:** `true`  
**Description:** Scales income by the town's maximum claim power divided by the claim power it has used, limited to the range set by `incomeScaleMin` and `incomeScaleMax`.

---

### incomeScaleMin / incomeScaleMax

**Type:** float  
**Default:** `0.1` / `1.0`  
**Description:** Minimum and maximum income scaling factors.

---

### globalResources

**Type:** map  
**Description:** Default resources present in every territory. Supports `income`, `ore`, `crops`, and `animals` sub-sections.

**Example:**
```yaml
globalResources:
  income:
    gold_ingot: 1
  ore:
    coal: [0.03, 1, 3]
    iron_ore: [0.015, 1, 2]
```

---

### allowOreInWilderness / allowCropsInWilderness / allowBreedingInWilderness

**Type:** boolean  
**Default:** `false`  
**Description:** Whether resources work in wilderness. `allowOreInWilderness` gives hidden ore drops in unclaimed territories. `allowCropsInWilderness` lets crops grow, and `allowBreedingInWilderness` lets animals breed, in unclaimed territories and in areas with no territory. Whether a player can break blocks there is set by `canInteractInUnclaimed` and `canInteractInEmpty`.

---

### allowOreInCaptured

**Type:** boolean  
**Default:** `true`  
**Description:** Whether players get hidden ore drops when mining in a territory their town occupies. Breaking blocks there is not affected by this setting.

---

### allowOreInNationTowns

**Type:** boolean  
**Default:** `true`  
**Description:** Whether players get hidden ore drops when mining in other towns of their nation. Breaking blocks and harvesting crops there are not affected by this setting.

---

### cropsMinSkyLight / breedingMinSkyLight

**Type:** integer  
**Default:** `14`  
**Description:** Minimum sky-light level required for crop growth events and animal breeding. Set to `0` to disable the check.

---

### cropsMinYHeight / cropsMaxYHeight / breedingMinYHeight / breedingMaxYHeight

**Type:** integer  
**Default:** `16` / `255` / `16` / `255`  
**Description:** Y-level range within which crop and breeding resource events can fire.

---

## Tax Settings

### taxIncomeRate

**Type:** float  
**Default:** `0.2`  
**Description:** Fraction of territory income that is redirected to the occupying town.

---

### taxMineRate / taxFarmRate / taxAnimalRate

**Type:** float  
**Default:** `0.2`  
**Description:** Probability that resources from mining, farming, or breeding go to the occupier instead of the territory owner.

---

## War Settings

### restrictExplosions

**Type:** boolean  
**Default:** `true`  
**Description:** Turns on the plugin's explosion rules. While no war is active, explosions are blocked if `onlyAllowExplosionsDuringWar` is enabled. During a war, explosions are blocked in towns that are not on a non-empty `warWhitelist` or that are on `warBlacklist`.

---

### onlyAllowExplosionsDuringWar

**Type:** boolean  
**Default:** `true`  
**Description:** Blocks explosions while no war is active. Has no effect unless `restrictExplosions` is enabled.

---

### flagNoBuildDistance

**Type:** integer  
**Default:** `1`  
**Description:** Square radius around a war flag in which building is prohibited.

---

### flagNoBuildYOffset

**Type:** integer  
**Default:** `-1`  
**Description:** Building is prohibited above `flagBaseY + flagNoBuildYOffset`.

---

### chunkAttackTime

**Type:** integer (ticks)  
**Default:** `1200`  
**Description:** Time in ticks to capture a chunk with a war flag.

---

### chunkAttackFromWastelandMultiplier / chunkAttackHomeMultiplier

**Type:** float  
**Default:** `2.0`  
**Description:** Multipliers applied to `chunkAttackTime`: `chunkAttackFromWastelandMultiplier` when the attacked territory borders wilderness, and `chunkAttackHomeMultiplier` when it is the town's home territory. Both apply to a home territory that borders wilderness.

---

### maxPlayerChunkAttacks

**Type:** integer  
**Default:** `1`  
**Description:** Maximum number of chunks a single player can be attacking simultaneously.

---

### allowBreakingAlliesFlags

**Type:** boolean  
**Default:** `false`  
**Description:** Whether players can break war flags belonging to their allies.

---

### flagBeaconSize / flagBeaconMinSkyLevel / flagBeaconSkyLevel

**Type:** integer  
**Default:** `6` / `100` / `50`  
**Description:** Visual beacon displayed above war flags. `flagBeaconSize` is its size, and values outside `[2, 16]` are limited to that range. The beacon appears `flagBeaconSkyLevel` blocks above the flag, but never below Y-level `flagBeaconMinSkyLevel`.

---

### allowDestructionDuringSkirmish

**Type:** boolean  
**Default:** `false`  
**Description:** Stored when `/nodesadmin war skirmish` starts a skirmish and shown in the war status, but nothing else reads it, so changing it has no effect on what players can build or destroy. War permissions apply during a skirmish as they do during a full war.

---

### warPermissions

**Type:** boolean  
**Default:** `true`  
**Description:** Bypasses normal permissions to allow extended ally interactions in towns during war.

---

### canLeaveTownDuringWar / canCreateTownDuringWar / canDestroyTownDuringWar / canLeaveNationDuringWar

**Type:** boolean  
**Defaults:** `true` / `false` / `false` / `false`  
**Description:** Controls which town/nation management actions are allowed while a war is active.

---

### annexDisabled

**Type:** boolean  
**Default:** `false`  
**Description:** Globally disables the territory annexation mechanic.

---

### canOnlyAnnexDuringWar

**Type:** boolean  
**Default:** `true`  
**Description:** Read by the plugin but not used, so changing it has no effect. `/town annex` always requires a full war started with `/nodesadmin war enable`.

---

### warWhitelist / warBlacklist / annexBlacklist

**Type:** list of town UUIDs  
**Default:** (none)  
**Description:** Lists of town UUIDs, commented out by default. While `warWhitelist` is non-empty, only towns on it can be attacked or annexed. Towns on `warBlacklist` cannot be attacked or annexed. Towns on `annexBlacklist` can be attacked and occupied but not annexed.

---

### onlyWhitelistCanAnnex / onlyWhitelistCanClaim

**Type:** boolean  
**Default:** `true`  
**Description:** When enabled, only towns on the war whitelist can annex territories (`onlyWhitelistCanAnnex`) or capture chunks with war flags (`onlyWhitelistCanClaim`). Both settings only take effect while `warWhitelist` is non-empty. `onlyWhitelistCanClaim` does not affect `/town claim`.

---

### occupiedHomeTeleportMultiplier

**Type:** float  
**Default:** `12.0`  
**Description:** Multiplier applied to the `/town spawn` warm-up time, including teleports to an outpost, while a war is active and the town's home territory is occupied.

---

### allowControlInOccupiedTownList

**Type:** list of town UUIDs  
**Default:** (none)  
**Description:** In an occupied territory of a town on this list, the occupying town's permissions also decide who can build and interact there, even outside war time.

---

## Truce Settings

### trucePeriod

**Type:** integer (milliseconds)  
**Default:** `259200000` (72 hours)  
**Description:** How long a truce lasts. A truce starts when a peace treaty is signed, when an alliance is broken with `/unally`, or when an admin runs `/nodesadmin truce`, and war cannot be declared between the two sides until it expires.

---

## Port Settings

### seaLevel

**Type:** float  
**Default:** `62.0`  
**Description:** Y-level that players and boats are teleported to when a port warp completes. It is not used to decide whether a warp can start.

---

### portWarpTime

**Type:** float (seconds)  
**Default:** `5.0`  
**Description:** Warm-up time in seconds before a port warp completes.

---

### allowPortWarpWithoutBoat

**Type:** boolean  
**Default:** `false`  
**Description:** Whether players can use port warps without being in a boat.
