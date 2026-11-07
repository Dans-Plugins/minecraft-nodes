package phonon.nodes

import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.util.UUID
import java.util.logging.Logger

/**
 * Config is a global object, so each test starts from a known state by
 * loading an empty config.
 */
class ConfigTest {
    private val logger = Logger.getAnonymousLogger()

    private fun load(yaml: String) {
        val config = YamlConfiguration()
        config.loadFromString(yaml)
        Config.load(config)
    }

    @BeforeEach
    fun resetConfig() {
        load("")
    }

    @Test
    fun `portWarpTime is converted from seconds to ticks`() {
        load("portWarpTime: 3.0")

        assertEquals(60.0, Config.portWarpTime)
    }

    @Test
    fun `portWarpTime falls back to 5 seconds when the key is missing`() {
        load("portWarpTime: 3.0")
        load("")

        assertEquals(100.0, Config.portWarpTime)
    }

    @Test
    fun `portWarpTime fallback does not grow across reloads`() {
        load("")
        load("")
        load("")

        assertEquals(100.0, Config.portWarpTime)
    }

    @Test
    fun `outpost teleport cost is parsed with its display string`() {
        load(
            """
            outpostTeleportCost:
              diamond: 2
            """.trimIndent(),
        )

        assertEquals(mapOf(Material.DIAMOND to 2), Config.outpostTeleportCost.toMap())
        assertEquals("2 DIAMOND", Config.outpostTeleportCostString)
    }

    @Test
    fun `item removed from outpost teleport cost is dropped on reload`() {
        load(
            """
            outpostTeleportCost:
              diamond: 2
              iron_ingot: 4
            """.trimIndent(),
        )
        load(
            """
            outpostTeleportCost:
              diamond: 2
            """.trimIndent(),
        )

        assertEquals(mapOf(Material.DIAMOND to 2), Config.outpostTeleportCost.toMap())
        assertEquals("2 DIAMOND", Config.outpostTeleportCostString)
    }

    @Test
    fun `removing the outpost teleport cost section clears the cost and its string`() {
        load(
            """
            outpostTeleportCost:
              diamond: 2
            """.trimIndent(),
        )
        load("")

        assertTrue(Config.outpostTeleportCost.isEmpty())
        assertEquals("", Config.outpostTeleportCostString)
    }

    @Test
    fun `item removed from nation town teleport cost is dropped on reload`() {
        load(
            """
            nationTownTeleportCost:
              gold_ingot: 1
              emerald: 3
            """.trimIndent(),
        )
        load(
            """
            nationTownTeleportCost:
              emerald: 3
            """.trimIndent(),
        )

        assertEquals(mapOf(Material.EMERALD to 3), Config.nationTownTeleportCost.toMap())
        assertEquals("3 EMERALD", Config.nationTownTeleportCostString)
    }

    @Test
    fun `removing the nation town teleport cost section clears the cost and its string`() {
        load(
            """
            nationTownTeleportCost:
              emerald: 3
            """.trimIndent(),
        )
        load("")

        assertTrue(Config.nationTownTeleportCost.isEmpty())
        assertEquals("", Config.nationTownTeleportCostString)
    }

    @Test
    fun `loadFile applies a valid file`(@TempDir dir: File) {
        val file = File(dir, "config.yml")
        file.writeText("townSpawnTime: 7")

        assertTrue(Config.loadFile(file, logger))
        assertEquals(7, Config.townSpawnTime)
    }

    @Test
    fun `loadFile keeps the current config when the file is malformed`(@TempDir dir: File) {
        val townUuid = UUID.randomUUID()
        load(
            """
            townSpawnTime: 7
            warWhitelist:
              - $townUuid
            outpostTeleportCost:
              diamond: 2
            portWarpTime: 3.0
            """.trimIndent(),
        )
        val file = File(dir, "config.yml")
        file.writeText("townSpawnTime: [")

        assertFalse(Config.loadFile(file, logger))

        assertEquals(7, Config.townSpawnTime)
        assertEquals(setOf(townUuid), Config.warWhitelist.toSet())
        assertTrue(Config.warUseWhitelist)
        assertEquals(mapOf(Material.DIAMOND to 2), Config.outpostTeleportCost.toMap())
        assertEquals(60.0, Config.portWarpTime)
    }

    @Test
    fun `loadFile keeps the current config when the file is missing`(@TempDir dir: File) {
        load("townSpawnTime: 7")

        assertFalse(Config.loadFile(File(dir, "config.yml"), logger))
        assertEquals(7, Config.townSpawnTime)
    }
}
