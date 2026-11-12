package phonon.nodes.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Tests for the name validation and sanitizing applied to town and nation
 * names, and for the digit count used to size war save output.
 */
class UtilsTest {
    @Test
    fun `stringInputIsValid accepts names up to the default 32 characters`() {
        assertTrue(stringInputIsValid(""))
        assertTrue(stringInputIsValid("a".repeat(32)))
        assertFalse(stringInputIsValid("a".repeat(33)))
    }

    @Test
    fun `stringInputIsValid uses a custom maximum length`() {
        assertTrue(stringInputIsValid("abcde", maxLength = 5))
        assertFalse(stringInputIsValid("abcdef", maxLength = 5))
    }

    @Test
    fun `stringInputIsValid rejects quotes and braces`() {
        assertFalse(stringInputIsValid("a\"b"))
        assertFalse(stringInputIsValid("a{b"))
        assertFalse(stringInputIsValid("a}b"))
    }

    @Test
    fun `sanitizeString doubles percent signs`() {
        assertEquals("100%% town", sanitizeString("100% town"))
    }

    @Test
    fun `sanitizeString removes newlines, quotes and braces`() {
        assertEquals("abcde", sanitizeString("a\nb\"c{d}e"))
    }

    @Test
    fun `sanitizeString leaves dollar signs unchanged`() {
        // "\$" in Kotlin is the one-character string "$", so the
        // replacement in sanitizeString does not change anything.
        assertEquals("\$5 town", sanitizeString("\$5 town"))
    }

    @Test
    fun `sanitizeString leaves ordinary names unchanged`() {
        assertEquals("New Town_1", sanitizeString("New Town_1"))
    }

    @Test
    fun `estimateNumDigits counts digits at each power of ten boundary`() {
        var power = 1
        for (digits in 1..9) {
            assertEquals(digits, estimateNumDigits(power), "for $power")
            assertEquals(digits, estimateNumDigits(power * 10 - 1), "for ${power * 10 - 1}")
            power *= 10
        }
        assertEquals(10, estimateNumDigits(1_000_000_000))
        assertEquals(10, estimateNumDigits(Int.MAX_VALUE))
    }

    @Test
    fun `estimateNumDigits counts zero as one digit`() {
        assertEquals(1, estimateNumDigits(0))
    }

    @Test
    fun `estimateNumDigits ignores the sign`() {
        assertEquals(3, estimateNumDigits(-123))
    }
}
