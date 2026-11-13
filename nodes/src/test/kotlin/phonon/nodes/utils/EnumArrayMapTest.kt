package phonon.nodes.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

/**
 * Tests for the array-backed map keyed by every value of an enum.
 */
class EnumArrayMapTest {
    private enum class Season {
        SPRING,
        SUMMER,
        AUTUMN,
        WINTER,
    }

    @Test
    fun `createEnumArrayMap calls the initializer once for each enum value`() {
        val map = createEnumArrayMap<Season, String> { it.name.lowercase() }

        assertEquals(4, map.size)
        assertEquals("spring", map[Season.SPRING])
        assertEquals("summer", map[Season.SUMMER])
        assertEquals("autumn", map[Season.AUTUMN])
        assertEquals("winter", map[Season.WINTER])
    }

    @Test
    fun `values are stored at the index of the key ordinal`() {
        val map = createEnumArrayMap<Season, Int> { it.ordinal * 10 }

        assertEquals(listOf(0, 10, 20, 30), map.array.toList())
    }

    @Test
    fun `set replaces only the value for the given key`() {
        val map = createEnumArrayMap<Season, Int> { 0 }

        map[Season.AUTUMN] = 7

        assertEquals(7, map[Season.AUTUMN])
        assertEquals(listOf(0, 0, 7, 0), map.array.toList())
    }

    @Test
    fun `copyOf is not changed by later writes to the original`() {
        val original = createEnumArrayMap<Season, Int> { 1 }
        val copy = original.copyOf()

        original[Season.WINTER] = 99

        assertEquals(1, copy[Season.WINTER])
        assertEquals(99, original[Season.WINTER])
        assertNotSame(original.array, copy.array)
    }

    @Test
    fun `copyOf shares the element objects with the original`() {
        val original = createEnumArrayMap<Season, MutableList<String>> { mutableListOf() }
        val copy = original.copyOf()

        original[Season.SPRING].add("seed")

        assertSame(original[Season.SPRING], copy[Season.SPRING])
        assertEquals(listOf("seed"), copy[Season.SPRING])
    }
}
