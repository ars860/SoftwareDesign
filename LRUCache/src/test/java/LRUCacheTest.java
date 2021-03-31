import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LRUCacheTest {
    @Test
    public void simplePutGet() {
        LRUCache<Integer, Integer> lruCache = new LRUCacheImpl<>(5);
        lruCache.put(1, 2);
        assertEquals(2, (int)lruCache.get(1));
    }

    @Test
    public void noValueTest() {
        LRUCache<Integer, Integer> lruCache = new LRUCacheImpl<>(3);
        lruCache.put(1, 2);
        assertNull(lruCache.get(2));
    }

    @Test
    public void sameKeyTest() {
        LRUCache<Integer, Integer> lruCache = new LRUCacheImpl<>(5);
        lruCache.put(1, 2);
        lruCache.put(1, 3);
        assertEquals(3, (int)lruCache.get(1));
        assertEquals(1, lruCache.size());
    }

    @Test
    public void sizeTest() {
        LRUCache<Integer, Integer> lruCache = new LRUCacheImpl<>(5);
        assertEquals(0, lruCache.size());

        lruCache.put(1, 2);
        lruCache.put(2, 3);
        lruCache.put(3, 4);
        assertEquals(3, lruCache.size());

        lruCache.put(4, 5);
        lruCache.put(5, 6);
        assertEquals(5, lruCache.size());
    }

    @Test
    public void overflowSizeTest() {
        LRUCache<Integer, Integer> lruCache = new LRUCacheImpl<>(5);
        lruCache.put(1, 2);
        lruCache.put(2, 3);
        lruCache.put(3, 4);
        lruCache.put(4, 5);
        lruCache.put(5, 6);
        lruCache.put(6, 7);
        assertEquals(5, lruCache.size());
    }

    @Test
    public void overflowOldestRemovedTest() {
        LRUCache<Integer, Integer> lruCache = new LRUCacheImpl<>(3);
        lruCache.put(1, 2);
        lruCache.put(2, 3);
        lruCache.put(3, 4);
        assertEquals(3, lruCache.size());

        lruCache.put(4, 5);
        assertNull(lruCache.get(1));
        assertEquals(3, lruCache.size());
    }

    @Test
    public void overflowMoveToTopTest() {
        LRUCache<Integer, Integer> lruCache = new LRUCacheImpl<>(3);
        lruCache.put(1, 2);
        lruCache.put(2, 3);
        lruCache.put(3, 4);
        assertEquals(2, (int)lruCache.get(1));
        assertEquals(3, lruCache.size());

        lruCache.put(4, 5);
        assertNull(lruCache.get(2));
        assertEquals(3, lruCache.size());
    }
}
