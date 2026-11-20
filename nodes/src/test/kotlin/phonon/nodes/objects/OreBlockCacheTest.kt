package phonon.nodes.objects

import org.bukkit.block.Block
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.lang.reflect.Proxy

/**
 * Tests for the hidden ore broken-block cache. Block is an interface, so a
 * dynamic proxy with identity equals and hashCode stands in for a real block.
 */
class OreBlockCacheTest {
    private fun block(): Block {
        lateinit var proxy: Block
        proxy = Proxy.newProxyInstance(
            Block::class.java.classLoader,
            arrayOf(Block::class.java),
        ) { _, method, args ->
            when (method.name) {
                "equals" -> proxy === args?.get(0)
                "hashCode" -> System.identityHashCode(proxy)
                "toString" -> "TestBlock@" + System.identityHashCode(proxy)
                else -> throw UnsupportedOperationException(method.name)
            }
        } as Block
        return proxy
    }

    @Test
    fun `an empty cache contains no blocks`() {
        val cache = OreBlockCache(4)

        assertFalse(cache.contains(block()))
    }

    @Test
    fun `an added block is contained`() {
        val cache = OreBlockCache(4)
        val broken = block()

        cache.add(broken)

        assertTrue(cache.contains(broken))
    }

    @Test
    fun `a block that was never added is not contained`() {
        val cache = OreBlockCache(4)
        cache.add(block())

        assertFalse(cache.contains(block()))
    }

    @Test
    fun `the cache holds up to maxSize blocks`() {
        val cache = OreBlockCache(3)
        val blocks = List(3) { block() }

        blocks.forEach { cache.add(it) }

        blocks.forEach { assertTrue(cache.contains(it)) }
    }

    @Test
    fun `adding past maxSize evicts the oldest block`() {
        val cache = OreBlockCache(3)
        val blocks = List(4) { block() }

        blocks.forEach { cache.add(it) }

        assertFalse(cache.contains(blocks[0]))
        assertTrue(cache.contains(blocks[1]))
        assertTrue(cache.contains(blocks[2]))
        assertTrue(cache.contains(blocks[3]))
    }

    @Test
    fun `re-adding a cached block does not refresh its age`() {
        val cache = OreBlockCache(2)
        val first = block()
        val second = block()
        val third = block()

        cache.add(first)
        cache.add(second)
        cache.add(first)
        cache.add(third)

        assertFalse(cache.contains(first))
        assertTrue(cache.contains(second))
        assertTrue(cache.contains(third))
    }

    @Test
    fun `re-adding a cached block does not evict another block`() {
        val cache = OreBlockCache(2)
        val first = block()
        val second = block()

        cache.add(first)
        cache.add(second)
        cache.add(second)

        assertTrue(cache.contains(first))
        assertTrue(cache.contains(second))
    }
}
