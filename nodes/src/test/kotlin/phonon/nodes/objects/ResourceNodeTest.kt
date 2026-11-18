package phonon.nodes.objects

import com.google.gson.JsonParser
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.EnumMap

/**
 * Tests for how resource node attributes change a territory's resources,
 * and for how DefaultResourceAttributeLoader reads resource nodes from
 * world.json.
 */
class ResourceNodeTest {
    private fun materials(vararg entries: Pair<Material, Double>): EnumMap<Material, Double> {
        val map = EnumMap<Material, Double>(Material::class.java)
        entries.forEach { (k, v) -> map[k] = v }
        return map
    }

    private fun entities(vararg entries: Pair<EntityType, Double>): EnumMap<EntityType, Double> {
        val map = EnumMap<EntityType, Double>(EntityType::class.java)
        entries.forEach { (k, v) -> map[k] = v }
        return map
    }

    private fun ores(vararg deposits: OreDeposit): EnumMap<Material, OreDeposit> {
        val map = EnumMap<Material, OreDeposit>(Material::class.java)
        deposits.forEach { map[it.material] = it }
        return map
    }

    private fun node(vararg attributes: ResourceAttribute) = ResourceNode("test", null, 0, 1.0, 0, attributes.toList())

    private fun load(json: String): HashMap<String, ResourceNode> = DefaultResourceAttributeLoader.load(hashMapOf(), JsonParser.parseString(json).asJsonObject)

    // ========================================================================
    // core attributes
    // ========================================================================

    @Test
    fun `income attribute adds to existing income and spawn egg income`() {
        val start = TerritoryResources(
            income = materials(Material.WHEAT to 4.0),
            incomeSpawnEgg = entities(EntityType.COW to 1.0),
        )
        val attribute = ResourceAttributeIncome(
            materials(Material.WHEAT to 2.0, Material.IRON_INGOT to 3.0),
            entities(EntityType.COW to 0.5),
        )

        val result = attribute.apply(start)

        assertEquals(materials(Material.WHEAT to 6.0, Material.IRON_INGOT to 3.0), result.income)
        assertEquals(entities(EntityType.COW to 1.5), result.incomeSpawnEgg)
    }

    @Test
    fun `applying an attribute leaves the original resources unchanged`() {
        val start = TerritoryResources(income = materials(Material.WHEAT to 4.0))

        ResourceAttributeIncome(materials(Material.WHEAT to 2.0), entities()).apply(start)
        ResourceAttributeTotalIncomeMultiplier(10.0).apply(start)

        assertEquals(materials(Material.WHEAT to 4.0), start.income)
    }

    @Test
    fun `ore attribute merges with an existing deposit and adds new ones`() {
        val start = TerritoryResources(ores = ores(OreDeposit(Material.COAL, 0.25, 1, 2)))
        val attribute = ResourceAttributeOre(
            ores(OreDeposit(Material.COAL, 0.5, 1, 4), OreDeposit(Material.IRON_ORE, 0.1, 1, 1)),
        )

        val result = attribute.apply(start)

        assertEquals(ores(OreDeposit(Material.COAL, 0.75, 1, 4), OreDeposit(Material.IRON_ORE, 0.1, 1, 1)), result.ores)
    }

    @Test
    fun `crops and animals attributes add rates but cap them at one`() {
        val start = TerritoryResources(
            crops = materials(Material.WHEAT to 0.75),
            animals = entities(EntityType.SHEEP to 0.75),
        )

        val crops = ResourceAttributeCrops(materials(Material.WHEAT to 0.5, Material.CARROTS to 0.25)).apply(start)
        val animals = ResourceAttributeAnimals(entities(EntityType.SHEEP to 0.5, EntityType.PIG to 0.25)).apply(start)

        assertEquals(materials(Material.WHEAT to 1.0, Material.CARROTS to 0.25), crops.crops)
        assertEquals(entities(EntityType.SHEEP to 1.0, EntityType.PIG to 0.25), animals.animals)
    }

    @Test
    fun `crops and animals rates are not capped when first added`() {
        val start = TerritoryResources()

        val crops = ResourceAttributeCrops(materials(Material.WHEAT to 1.5)).apply(start)
        val animals = ResourceAttributeAnimals(entities(EntityType.SHEEP to 1.5)).apply(start)

        assertEquals(1.5, crops.crops[Material.WHEAT])
        assertEquals(1.5, animals.animals[EntityType.SHEEP])
    }

    // ========================================================================
    // multipliers
    // ========================================================================

    @Test
    fun `total multipliers scale every entry`() {
        val start = TerritoryResources(
            income = materials(Material.WHEAT to 4.0, Material.IRON_INGOT to 2.0),
            incomeSpawnEgg = entities(EntityType.COW to 1.0),
            ores = ores(OreDeposit(Material.COAL, 0.25, 1, 2)),
            crops = materials(Material.WHEAT to 0.5),
            animals = entities(EntityType.SHEEP to 0.25),
        )

        val income = ResourceAttributeTotalIncomeMultiplier(2.0).apply(start)
        val ore = ResourceAttributeTotalOreMultiplier(2.0).apply(start)
        val crops = ResourceAttributeTotalCropsMultiplier(2.0).apply(start)
        val animals = ResourceAttributeTotalAnimalsMultiplier(2.0).apply(start)

        assertEquals(materials(Material.WHEAT to 8.0, Material.IRON_INGOT to 4.0), income.income)
        assertEquals(entities(EntityType.COW to 2.0), income.incomeSpawnEgg)
        assertEquals(ores(OreDeposit(Material.COAL, 0.5, 1, 2)), ore.ores)
        assertEquals(materials(Material.WHEAT to 1.0), crops.crops)
        assertEquals(entities(EntityType.SHEEP to 0.5), animals.animals)
    }

    @Test
    fun `total crops multiplier does not cap the rate at one`() {
        val start = TerritoryResources(crops = materials(Material.WHEAT to 0.75))

        val result = ResourceAttributeTotalCropsMultiplier(2.0).apply(start)

        assertEquals(1.5, result.crops[Material.WHEAT])
    }

    @Test
    fun `type multipliers scale only matching entries and do not add missing ones`() {
        val start = TerritoryResources(
            income = materials(Material.WHEAT to 4.0, Material.IRON_INGOT to 2.0),
            ores = ores(OreDeposit(Material.COAL, 0.25, 1, 2)),
            crops = materials(Material.WHEAT to 0.5),
            animals = entities(EntityType.SHEEP to 0.25),
        )

        val income = ResourceAttributeIncomeMultiplier(materials(Material.WHEAT to 3.0, Material.GOLD_INGOT to 3.0), entities()).apply(start)
        val ore = ResourceAttributeOreMultiplier(materials(Material.COAL to 2.0, Material.IRON_ORE to 2.0)).apply(start)
        val crops = ResourceAttributeCropsMultiplier(materials(Material.WHEAT to 0.5, Material.CARROTS to 2.0)).apply(start)
        val animals = ResourceAttributeAnimalsMultiplier(entities(EntityType.SHEEP to 4.0, EntityType.PIG to 2.0)).apply(start)

        assertEquals(materials(Material.WHEAT to 12.0, Material.IRON_INGOT to 2.0), income.income)
        assertEquals(ores(OreDeposit(Material.COAL, 0.5, 1, 2)), ore.ores)
        assertEquals(materials(Material.WHEAT to 0.25), crops.crops)
        assertEquals(entities(EntityType.SHEEP to 1.0), animals.animals)
    }

    @Test
    fun `claim time multipliers multiply together`() {
        val start = TerritoryResources(attackerTimeMultiplier = 2.0, defenderTimeMultiplier = 0.5)

        val result = node(
            ResourceAttributeAttackerTimeMultiplier(3.0),
            ResourceAttributeDefenderTimeMultiplier(0.5),
        ).apply(start)

        assertEquals(6.0, result.attackerTimeMultiplier)
        assertEquals(0.25, result.defenderTimeMultiplier)
    }

    // ========================================================================
    // neighbor attributes
    // ========================================================================

    @Test
    fun `neighbor attributes fill empty neighbor fields and leave the territory's own resources alone`() {
        val start = TerritoryResources(income = materials(Material.WHEAT to 4.0))

        val result = node(
            ResourceAttributeNeighborIncome(materials(Material.WHEAT to 2.0), entities(EntityType.COW to 1.0)),
            ResourceAttributeNeighborOre(ores(OreDeposit(Material.COAL, 0.25, 1, 1))),
            ResourceAttributeNeighborCrops(materials(Material.WHEAT to 0.5)),
            ResourceAttributeNeighborAnimals(entities(EntityType.SHEEP to 0.5)),
        ).apply(start)

        assertEquals(materials(Material.WHEAT to 4.0), result.income)
        assertEquals(materials(Material.WHEAT to 2.0), result.neighborIncome)
        assertEquals(entities(EntityType.COW to 1.0), result.neighborIncomeSpawnEgg)
        assertEquals(ores(OreDeposit(Material.COAL, 0.25, 1, 1)), result.neighborOres)
        assertEquals(materials(Material.WHEAT to 0.5), result.neighborCrops)
        assertEquals(entities(EntityType.SHEEP to 0.5), result.neighborAnimals)
    }

    @Test
    fun `neighbor crops add up but cap at one`() {
        val attribute = ResourceAttributeNeighborCrops(materials(Material.WHEAT to 0.75))

        val result = attribute.apply(attribute.apply(TerritoryResources()))

        assertEquals(1.0, result.neighborCrops!![Material.WHEAT])
    }

    @Test
    fun `neighbor multipliers add together instead of multiplying`() {
        val start = TerritoryResources(
            neighborTotalIncomeMultiplier = 0.5,
            neighborIncomeMultiplier = materials(Material.WHEAT to 0.5),
        )

        val result = node(
            ResourceAttributeNeighborTotalIncomeMultiplier(0.25),
            ResourceAttributeNeighborTotalOreMultiplier(0.25),
            ResourceAttributeNeighborIncomeMultiplier(materials(Material.WHEAT to 0.25, Material.IRON_INGOT to 2.0), entities()),
            ResourceAttributeNeighborCropsMultiplier(materials(Material.WHEAT to 1.5)),
        ).apply(start)

        assertEquals(0.75, result.neighborTotalIncomeMultiplier)
        assertEquals(0.25, result.neighborTotalOresMultiplier)
        assertNull(result.neighborTotalCropsMultiplier)
        assertEquals(materials(Material.WHEAT to 0.75, Material.IRON_INGOT to 2.0), result.neighborIncomeMultiplier)
        assertEquals(materials(Material.WHEAT to 1.5), result.neighborCropsMultiplier)
    }

    // ========================================================================
    // ResourceNode ordering
    // ========================================================================

    @Test
    fun `resource node applies attributes by priority, not list order`() {
        // income (priority 1) runs before the total multiplier (priority 50),
        // so the added income is doubled too
        val result = node(
            ResourceAttributeTotalIncomeMultiplier(2.0),
            ResourceAttributeIncome(materials(Material.WHEAT to 3.0), entities()),
        ).apply(TerritoryResources(income = materials(Material.WHEAT to 1.0)))

        assertEquals(materials(Material.WHEAT to 8.0), result.income)
    }

    // ========================================================================
    // DefaultResourceAttributeLoader
    // ========================================================================

    @Test
    fun `loader reads icon, cost, and priority, with defaults when missing`() {
        val nodes = load(
            """
            {
                "farm": { "icon": "wheat", "cost": { "constant": 5, "scale": 1.5 }, "priority": 3 },
                "empty": {}
            }
            """,
        )

        val farm = nodes.getValue("farm")
        assertEquals("wheat", farm.icon)
        assertEquals(5, farm.costConstant)
        assertEquals(1.5, farm.costScale)
        assertEquals(3, farm.priority)

        val empty = nodes.getValue("empty")
        assertNull(empty.icon)
        assertEquals(0, empty.costConstant)
        assertEquals(1.0, empty.costScale)
        assertEquals(0, empty.priority)
        assertTrue(empty.attributes.isEmpty())
    }

    @Test
    fun `loader ignores a non-text icon`() {
        val nodes = load("""{ "farm": { "icon": { "name": "wheat" } } }""")

        assertNull(nodes.getValue("farm").icon)
    }

    @Test
    fun `loader ignores the resource nodes passed in`() {
        val existing = hashMapOf("old" to node())

        val nodes = DefaultResourceAttributeLoader.load(existing, JsonParser.parseString("""{ "new": {} }""").asJsonObject)

        assertEquals(setOf("new"), nodes.keys)
    }

    @Test
    fun `loaded income section splits spawn eggs from items, case-insensitively`() {
        val nodes = load("""{ "farm": { "income": { "wheat": 4, "SPAWN_EGG_COW": 1, "spawn_egg_pig": 2 } } }""")

        val result = nodes.getValue("farm").apply(TerritoryResources())

        assertEquals(materials(Material.WHEAT to 4.0), result.income)
        assertEquals(entities(EntityType.COW to 1.0, EntityType.PIG to 2.0), result.incomeSpawnEgg)
    }

    @Test
    fun `loaded ore section accepts a rate or a rate with min and max amounts`() {
        val nodes = load("""{ "mine": { "ore": { "coal": 0.5, "iron_ore": [0.25, 2, 3] } } }""")

        val result = nodes.getValue("mine").apply(TerritoryResources())

        assertEquals(ores(OreDeposit(Material.COAL, 0.5, 1, 1), OreDeposit(Material.IRON_ORE, 0.25, 2, 3)), result.ores)
    }

    @Test
    fun `loaded ore array without exactly three values is skipped`() {
        val nodes = load("""{ "mine": { "ore": { "coal": [0.5, 1], "iron_ore": 0.25 } } }""")

        val result = nodes.getValue("mine").apply(TerritoryResources())

        assertEquals(ores(OreDeposit(Material.IRON_ORE, 0.25, 1, 1)), result.ores)
    }

    @Test
    fun `loader turns each section into its attribute`() {
        val nodes = load(
            """
            {
                "all": {
                    "crops": { "wheat": 0.5 },
                    "animals": { "sheep": 0.25 },
                    "income_total_multiplier": 2,
                    "crops_multiplier": { "wheat": 2 },
                    "neighbor_income": { "wheat": 1 },
                    "neighbor_ore_total_multiplier": 0.5,
                    "attacker_time_multiplier": 1.5,
                    "defender_time_multiplier": 0.5
                }
            }
            """,
        )

        val result = nodes.getValue("all").apply(TerritoryResources(income = materials(Material.WHEAT to 3.0)))

        assertEquals(materials(Material.WHEAT to 6.0), result.income)
        assertEquals(materials(Material.WHEAT to 1.0), result.crops)
        assertEquals(entities(EntityType.SHEEP to 0.25), result.animals)
        assertEquals(materials(Material.WHEAT to 1.0), result.neighborIncome)
        assertEquals(0.5, result.neighborTotalOresMultiplier)
        assertEquals(1.5, result.attackerTimeMultiplier)
        assertEquals(0.5, result.defenderTimeMultiplier)
    }

    @Test
    fun `loader skips empty sections`() {
        val nodes = load("""{ "farm": { "income": {}, "ore": {}, "crops": {}, "neighbor_income": {} } }""")

        assertTrue(nodes.getValue("farm").attributes.isEmpty())
    }
}
