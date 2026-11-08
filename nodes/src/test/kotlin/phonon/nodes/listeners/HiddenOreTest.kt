package phonon.nodes.listeners

import org.bukkit.configuration.file.YamlConfiguration
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import phonon.nodes.Config

/**
 * Tests for the allowOreIn* rule. Plain objects stand in for towns and
 * nations, since the rule only compares them by identity.
 */
class HiddenOreTest {
    private val town = Any()
    private val otherTown = Any()
    private val nation = Any()
    private val noTown: Any? = null
    private val noNation: Any? = null

    private fun load(yaml: String) {
        val config = YamlConfiguration()
        config.loadFromString(yaml)
        Config.load(config)
    }

    /**
     * Config.load keeps the last loaded value for a missing key, so each
     * test starts from the shipped defaults set explicitly.
     */
    @BeforeEach
    fun resetConfig() {
        load(
            """
            allowOreInWilderness: false
            allowOreInCaptured: true
            allowOreInNationTowns: true
            """.trimIndent(),
        )
    }

    @Test
    fun `player without a town gets no ore in an unclaimed territory by default`() {
        assertFalse(allowsHiddenOre(noTown, noNation, noTown, noTown, noNation))
    }

    @Test
    fun `player without a town gets ore in an unclaimed territory when allowOreInWilderness is on`() {
        load("allowOreInWilderness: true")

        assertTrue(allowsHiddenOre(noTown, noNation, noTown, noTown, noNation))
    }

    @Test
    fun `player without a town gets no ore in an unoccupied town territory`() {
        assertFalse(allowsHiddenOre(otherTown, noNation, noTown, noTown, noNation))
    }

    @Test
    fun `player gets ore in their own town's territory`() {
        assertTrue(allowsHiddenOre(town, noNation, noTown, town, noNation))
    }

    @Test
    fun `player gets ore in a territory their town occupies`() {
        assertTrue(allowsHiddenOre(otherTown, noNation, town, town, noNation))
    }

    @Test
    fun `player gets no ore in an occupied territory when allowOreInCaptured is off`() {
        load("allowOreInCaptured: false")

        assertFalse(allowsHiddenOre(otherTown, noNation, town, town, noNation))
    }

    @Test
    fun `player gets ore in another town of their nation`() {
        assertTrue(allowsHiddenOre(otherTown, nation, noTown, town, nation))
    }

    @Test
    fun `player gets no ore in another town's unoccupied territory`() {
        assertFalse(allowsHiddenOre(otherTown, noNation, noTown, town, noNation))
    }
}
