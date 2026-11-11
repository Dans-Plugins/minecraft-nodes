package phonon.nodes

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Tests for how territory income is split between the owning town and an
 * occupier. Strings stand in for towns and items, since the split only
 * uses them as map keys.
 */
class TerritoryIncomeTest {
    private val owner = "owner"
    private val occupier = "occupier"

    private fun pay(
        territories: List<Pair<String?, Map<String, Double>>>,
        taxRate: Double,
        incomeModifier: (String) -> Double = { 1.0 },
    ): Map<Pair<String, String>, Int> {
        val paid = HashMap<Pair<String, String>, Int>()
        payTerritoryIncome(owner, territories, taxRate, incomeModifier, { rate -> Math.round(rate).toInt() }) { town, item, amount ->
            paid[Pair(town, item)] = (paid[Pair(town, item)] ?: 0) + amount
        }
        return paid
    }

    @Test
    fun `unoccupied territory pays all its income to the owner`() {
        val paid = pay(listOf(Pair(null, mapOf("wheat" to 10.0))), taxRate = 0.2)

        assertEquals(mapOf(Pair(owner, "wheat") to 10), paid)
    }

    @Test
    fun `occupied territory pays the tax share to the occupier and the rest to the owner`() {
        val paid = pay(listOf(Pair(occupier, mapOf("wheat" to 10.0))), taxRate = 0.2)

        assertEquals(mapOf(Pair(occupier, "wheat") to 2, Pair(owner, "wheat") to 8), paid)
    }

    @Test
    fun `occupier share is scaled by the occupier's modifier and the owner share by the owner's`() {
        val modifiers = mapOf(owner to 0.5, occupier to 2.0)
        val paid = pay(listOf(Pair(occupier, mapOf("wheat" to 10.0))), taxRate = 0.2) { town -> modifiers.getValue(town) }

        assertEquals(mapOf(Pair(occupier, "wheat") to 4, Pair(owner, "wheat") to 4), paid)
    }

    @Test
    fun `occupied and unoccupied territories add up per town`() {
        val territories = listOf(
            Pair(occupier, mapOf("wheat" to 10.0)),
            Pair(null, mapOf("wheat" to 5.0, "iron" to 3.0)),
        )
        val paid = pay(territories, taxRate = 0.5)

        assertEquals(mapOf(Pair(occupier, "wheat") to 5, Pair(owner, "wheat") to 10, Pair(owner, "iron") to 3), paid)
    }

    @Test
    fun `a zero amount is not paid`() {
        val paid = pay(listOf(Pair(occupier, mapOf("wheat" to 10.0))), taxRate = 0.0)

        assertEquals(mapOf(Pair(owner, "wheat") to 10), paid)
    }
}
