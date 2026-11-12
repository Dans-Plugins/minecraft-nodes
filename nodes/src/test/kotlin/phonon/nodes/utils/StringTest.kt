package phonon.nodes.utils.string

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Tests for the list and map string builders used by town serialization,
 * and for the prefix filter used by tab completion.
 */
class StringTest {
    @Test
    fun `stringArrayFromList joins items with commas inside brackets`() {
        assertEquals("[1,2,3]", stringArrayFromList(listOf(1, 2, 3)) { it.toString() })
        assertEquals("[\"a\"]", stringArrayFromList(listOf("a")) { "\"$it\"" })
    }

    @Test
    fun `stringArrayFromList writes an empty list as empty brackets`() {
        assertEquals("[]", stringArrayFromList(listOf<Int>()) { it.toString() })
    }

    @Test
    fun `stringArrayFromSet joins items in iteration order`() {
        assertEquals("[c,a,b]", stringArrayFromSet(linkedSetOf("c", "a", "b")) { it })
        assertEquals("[]", stringArrayFromSet(setOf<String>()) { it })
    }

    @Test
    fun `stringMapFromMap writes key-value pairs inside braces`() {
        val map = linkedMapOf("\"WHEAT\"" to 4, "\"COAL\"" to 10)
        assertEquals("{\"WHEAT\":4,\"COAL\":10}", stringMapFromMap(map, { it }, { it.toString() }))
    }

    @Test
    fun `stringMapFromMap writes an empty map as empty braces`() {
        assertEquals("{}", stringMapFromMap(mapOf<String, Int>(), { it }, { it.toString() }))
    }

    @Test
    fun `filterByStart matches prefixes ignoring case`() {
        val names = listOf("Alpha", "alps", "Beta", "xAl")
        assertEquals(listOf("Alpha", "alps"), filterByStart(names, "AL"))
    }

    @Test
    fun `filterByStart with an empty prefix returns every item`() {
        val names = listOf("Alpha", "Beta")
        assertEquals(names, filterByStart(names, ""))
    }
}
