package phonon.nodes.serdes

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import phonon.nodes.objects.Port
import phonon.nodes.objects.PortGroup
import phonon.nodes.objects.Resident
import java.util.UUID

/**
 * Tests for the top-level JSON documents written for the world (residents,
 * towns, nations) and for ports.
 */
class SerializerTest {
    private val uuid1 = UUID.fromString("00000000-0000-0000-0000-000000000001")
    private val uuid2 = UUID.fromString("00000000-0000-0000-0000-000000000002")

    @Test
    fun `worldToJson with no state writes the metadata header and three empty objects`() {
        assertEquals(
            "{\"meta\":{\"type\":\"towns\"},\"residents\":{},\"towns\":{},\"nations\":{}}",
            Serializer.worldToJson(listOf(), listOf(), listOf()),
        )
    }

    @Test
    fun `worldToJson keys each resident by uuid and separates entries with commas`() {
        val alice = Resident(uuid1, "alice").getSaveState()
        val bob = Resident(uuid2, "bob").getSaveState()

        assertEquals(
            "{\"meta\":{\"type\":\"towns\"},\"residents\":{" +
                "\"$uuid1\":${alice.toJsonString()}," +
                "\"$uuid2\":${bob.toJsonString()}" +
                "},\"towns\":{},\"nations\":{}}",
            Serializer.worldToJson(listOf(alice, bob), listOf(), listOf()),
        )
    }

    @Test
    fun `portsToJson with no state writes the metadata header, an empty group array, and an empty port object`() {
        assertEquals(
            "{\"meta\":{\"type\":\"ports\"},\"groups\":[],\"ports\":{}}",
            Serializer.portsToJson(listOf(), listOf()),
        )
    }

    @Test
    fun `portsToJson writes group names as an array and keys each port by name`() {
        val west = PortGroup("west")
        val harbor = Port("harbor", 40, -3, hashSetOf(west), isPublic = false).getSaveState()
        val dock = Port("dock", 0, 0, isPublic = true).getSaveState()

        assertEquals(
            "{\"meta\":{\"type\":\"ports\"},\"groups\":[\"west\",\"east\"],\"ports\":{" +
                "\"harbor\":${harbor.toJsonString()}," +
                "\"dock\":${dock.toJsonString()}" +
                "}}",
            Serializer.portsToJson(
                listOf(west.getSaveState(), PortGroup("east").getSaveState()),
                listOf(harbor, dock),
            ),
        )
    }
}
