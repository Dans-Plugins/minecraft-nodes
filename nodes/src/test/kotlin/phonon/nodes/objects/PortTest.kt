package phonon.nodes.objects

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

/**
 * Tests for port chunk coordinates and the JSON written for ports and
 * port groups when they are saved.
 */
class PortTest {
    @Test
    fun `chunk coordinates are the block coordinates divided by 16`() {
        val port = Port("harbor", 40, 17, isPublic = true)

        assertEquals(2, port.chunkX)
        assertEquals(1, port.chunkZ)
    }

    @Test
    fun `negative block coordinates round down to the containing chunk`() {
        val port = Port("harbor", -1, -17, isPublic = true)

        assertEquals(-1, port.chunkX)
        assertEquals(-2, port.chunkZ)
    }

    @Test
    fun `save state JSON contains name, location, groups, and visibility`() {
        val port = Port("harbor", 40, -3, hashSetOf(PortGroup("west")), isPublic = false)

        assertEquals(
            "{\"name\":\"harbor\",\"x\":40,\"z\":-3,\"groups\":[\"west\"],\"isPublic\":false}",
            port.getSaveState().createJsonString(),
        )
    }

    @Test
    fun `save state JSON writes an empty array for a port without groups`() {
        val port = Port("harbor", 0, 0, isPublic = true)

        assertEquals(
            "{\"name\":\"harbor\",\"x\":0,\"z\":0,\"groups\":[],\"isPublic\":true}",
            port.getSaveState().createJsonString(),
        )
    }

    @Test
    fun `getSaveState returns the same object until needsUpdate is called`() {
        val port = Port("harbor", 0, 0, isPublic = true)
        val first = port.getSaveState()

        assertSame(first, port.getSaveState())

        port.needsUpdate()
        val second = port.getSaveState()

        assertNotSame(first, second)
        assertSame(second, port.getSaveState())
    }

    @Test
    fun `save state shares the port's group set, so group changes show without needsUpdate`() {
        val port = Port("harbor", 0, 0, isPublic = true)
        val saveState = port.getSaveState()

        port.groups.add(PortGroup("east"))

        assertEquals(
            "{\"name\":\"harbor\",\"x\":0,\"z\":0,\"groups\":[\"east\"],\"isPublic\":true}",
            saveState.createJsonString(),
        )
    }

    @Test
    fun `port group save state JSON contains only the name`() {
        assertEquals("{\"name\":\"west\"}", PortGroup("west").getSaveState().createJsonString())
    }

    @Test
    fun `port group getSaveState returns the same object until needsUpdate is called`() {
        val group = PortGroup("west")
        val first = group.getSaveState()

        assertSame(first, group.getSaveState())

        group.needsUpdate()

        assertNotSame(first, group.getSaveState())
    }
}
