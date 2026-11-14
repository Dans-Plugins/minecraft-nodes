package phonon.nodes.objects

import org.bukkit.Material
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Tests for OreDeposit.merge and the OreSampler height mapping.
 *
 * Creating an ItemStack needs a running server, so these tests only cover
 * cases where the sampler should drop nothing. A height that is wrongly
 * mapped to an ore would try to create an ItemStack and fail the test.
 */
class OreSamplerTest {
    private fun certainOre(
        ymin: Int = 0,
        ymax: Int = 255,
    ) = OreDeposit(Material.IRON_ORE, 1.0, 1, 1, ymin, ymax)

    @Test
    fun `merge sums drop chances and keeps the larger amounts`() {
        val first = OreDeposit(Material.COAL, 0.25, 1, 4, 10, 40)
        val second = OreDeposit(Material.IRON_ORE, 0.5, 2, 3, 0, 255)

        val merged = first.merge(second)

        assertEquals(OreDeposit(Material.COAL, 0.75, 2, 4, 10, 40), merged)
    }

    @Test
    fun `merge allows the drop chance to go above one`() {
        val ore = OreDeposit(Material.COAL, 0.75, 1, 1)

        assertEquals(1.5, ore.merge(ore).dropChance)
    }

    @Test
    fun `sampler without ores reports no ore`() {
        assertFalse(OreSampler(arrayListOf()).containsOre)
    }

    @Test
    fun `sampler with an ore reports ore`() {
        assertTrue(OreSampler(arrayListOf(certainOre())).containsOre)
    }

    @Test
    fun `sampler without ores drops nothing at any height`() {
        val sampler = OreSampler(arrayListOf())

        for (y in Y_WORLD_MIN..Y_WORLD_MAX) {
            assertEquals(emptyList<Any>(), sampler.sample(y), "sample at y=$y")
            assertEquals(emptyList<Any>(), sampler.sampleAll(y), "sampleAll at y=$y")
        }
    }

    @Test
    fun `heights outside the world drop nothing`() {
        val sampler = OreSampler(arrayListOf(certainOre()))

        for (y in listOf(Y_WORLD_MIN - 1, Y_WORLD_MAX + 1, -64, 320)) {
            assertEquals(emptyList<Any>(), sampler.sample(y), "sample at y=$y")
            assertEquals(emptyList<Any>(), sampler.sampleAll(y), "sampleAll at y=$y")
        }
    }

    @Test
    fun `heights outside an ore's range drop nothing`() {
        val sampler = OreSampler(arrayListOf(certainOre(ymin = 10, ymax = 20)))

        for (y in listOf(0, 9, 21, 100, 255)) {
            assertEquals(emptyList<Any>(), sampler.sample(y), "sample at y=$y")
            assertEquals(emptyList<Any>(), sampler.sampleAll(y), "sampleAll at y=$y")
        }
    }

    @Test
    fun `an ore ending below the top of the world drops nothing at the top`() {
        val sampler = OreSampler(arrayListOf(certainOre(ymax = 254)))

        assertEquals(emptyList<Any>(), sampler.sample(255))
        assertEquals(emptyList<Any>(), sampler.sampleAll(255))
    }

    @Test
    fun `sampleAll never drops an ore with zero drop chance`() {
        val sampler = OreSampler(arrayListOf(OreDeposit(Material.IRON_ORE, 0.0, 1, 1)))

        repeat(1000) {
            assertEquals(emptyList<Any>(), sampler.sampleAll(64))
        }
    }
}
