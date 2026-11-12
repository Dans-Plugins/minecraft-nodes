package phonon.nodes.objects

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

/**
 * Tests for converting block coordinates to chunk coordinates and for
 * parsing the "x,z" strings used as chunk keys.
 */
class CoordTest {
    @Test
    fun `toChunk maps the first and last block of a chunk to the same chunk`() {
        assertEquals(0, toChunk(0))
        assertEquals(0, toChunk(15))
        assertEquals(1, toChunk(16))
    }

    @Test
    fun `toChunk rounds negative block coordinates down`() {
        assertEquals(-1, toChunk(-1))
        assertEquals(-1, toChunk(-16))
        assertEquals(-2, toChunk(-17))
    }

    @Test
    fun `fromBlockCoords converts both axes to chunk coordinates`() {
        assertEquals(Coord(2, -1), Coord.fromBlockCoords(40, -3))
    }

    @Test
    fun `equal coords have equal hash codes`() {
        assertEquals(Coord(7, -9), Coord(7, -9))
        assertEquals(Coord(7, -9).hashCode(), Coord(7, -9).hashCode())
    }

    @Test
    fun `fromString parses two comma-separated integers`() {
        assertEquals(Coord(3, -4), Coord.fromString("3,-4"))
    }

    @Test
    fun `fromString returns null for a string without a comma`() {
        assertNull(Coord.fromString("3"))
        assertNull(Coord.fromString(""))
    }

    @Test
    fun `fromString returns null when either side is not an integer`() {
        assertNull(Coord.fromString("a,1"))
        assertNull(Coord.fromString("1,b"))
        assertNull(Coord.fromString(",1"))
        assertNull(Coord.fromString("1,"))
    }

    @Test
    fun `fromString returns null for spaces or a third value`() {
        assertNull(Coord.fromString("1, 2"))
        assertNull(Coord.fromString("1,2,3"))
    }
}
