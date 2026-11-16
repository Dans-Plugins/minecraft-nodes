package phonon.nodes.objects

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import java.util.UUID

/**
 * Tests for the JSON written for residents when they are saved, and for
 * when the resident save state is rebuilt.
 */
class ResidentTest {
    private val uuid = UUID.fromString("00000000-0000-0000-0000-000000000001")

    @Test
    fun `save state JSON writes null town and nation for a resident without a town`() {
        val resident = Resident(uuid, "alice")

        assertEquals(
            "{\"name\":\"alice\",\"town\":null,\"nation\":null,\"claims\":[0,0]," +
                "\"prefix\":\"\",\"suffix\":\"\",\"trust\":false,\"townCool\":0}",
            resident.getSaveState().createJsonString(),
        )
    }

    @Test
    fun `save state JSON writes claims as a pair of power and elapsed time`() {
        val resident = Resident(uuid, "alice")
        resident.claims = 7
        resident.claimsTime = 12345L
        resident.trusted = true
        resident.townCreateCooldown = 600L
        resident.prefix = "Sir"
        resident.suffix = "the Bold"
        resident.needsUpdate()

        assertEquals(
            "{\"name\":\"alice\",\"town\":null,\"nation\":null,\"claims\":[7,12345]," +
                "\"prefix\":\"Sir\",\"suffix\":\"the Bold\",\"trust\":true,\"townCool\":600}",
            resident.getSaveState().createJsonString(),
        )
    }

    @Test
    fun `save state JSON does not include the uuid`() {
        val json = Resident(uuid, "alice").getSaveState().createJsonString()

        assertEquals(false, json.contains(uuid.toString()))
    }

    @Test
    fun `getSaveState returns the same object until needsUpdate is called`() {
        val resident = Resident(uuid, "alice")
        val first = resident.getSaveState()

        assertSame(first, resident.getSaveState())

        resident.needsUpdate()
        val second = resident.getSaveState()

        assertNotSame(first, second)
        assertSame(second, resident.getSaveState())
    }

    @Test
    fun `save state is a snapshot, so field changes show only after needsUpdate`() {
        val resident = Resident(uuid, "alice")
        val stale = resident.getSaveState()

        resident.claims = 3

        assertEquals(0, stale.claims)
        assertEquals(0, resident.getSaveState().claims)

        resident.needsUpdate()

        assertEquals(3, resident.getSaveState().claims)
    }

    @Test
    fun `toJsonString memoizes the first JSON string it builds`() {
        val saveState = Resident(uuid, "alice").getSaveState()
        val first = saveState.toJsonString()

        assertSame(first, saveState.toJsonString())
    }
}
