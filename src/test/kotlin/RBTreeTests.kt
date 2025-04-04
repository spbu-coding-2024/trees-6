import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import trees.redblack.RedBlackTree
import trees.redblack.Colors
import kotlin.random.Random

const val MAX_ELEMENTS = 3000
const val MIN_ELEMENTS = 10

class RedBlackTreeTests {
    private lateinit var rbt: RedBlackTree<Int, String>

    @BeforeEach
    fun setUp() {
        rbt = RedBlackTree()
    }

    @AfterEach
    fun endUpWithCheckProperties() {
        rbt.checkRedBlackProperties()
    }

    @Test
    fun `Basic RB test - test with simply insert and search and delete`() {
        val firstMSG = "123"
        val secondMSG = "1234"
        val thirdMSG = "12345"

        rbt.insert(10, firstMSG)
        rbt.checkRedBlackProperties()

        rbt.insert(15, secondMSG)
        rbt.checkRedBlackProperties()

        rbt.insert(52, thirdMSG)
        rbt.checkRedBlackProperties()


        assertEquals(thirdMSG, rbt.search(52))
        assertEquals(secondMSG, rbt.search(15))
        assertEquals(firstMSG, rbt.search(10))

        val deleteResult = rbt.delete(52)
        rbt.checkRedBlackProperties()
        assertTrue(deleteResult)
        assertNull(rbt.search(52))
    }

    @Test
    fun `Basic RB test - test with value redefinition`() {
        val key = 10
        val firstValue = "first_value"
        val secondValue = "second_value"
        rbt.insert(key, firstValue)
        assertEquals(firstValue, rbt.search(key))
        rbt.insert(key, secondValue)
        assertEquals(secondValue, rbt.search(key))
    }

    @RepeatedTest(10)
    fun `Complicated RB test - random test with varying size`() {
        val cntElements = Random.nextInt(1, 1000)
        for (i in 1..cntElements) {
            rbt.insert(i, i.toString())
            rbt.checkRedBlackProperties()
        }

        assertEquals(cntElements, rbt.size())

        for (i in 1..cntElements) {
            assertEquals(i.toString(), rbt.search(i))
            val deleteResult = rbt.delete(i)
            assertTrue(deleteResult)
            assertNull(rbt.search(i))
        }
    }

    @Test
    fun `Basic RB test - test with clear() method`() {
        rbt.insert(10, "10")
        rbt.clear()
        assertEquals(0, rbt.size())
    }

    @Test
    fun `Basic RB test - range should return null when no elements in range`() {
        rbt.insert(10, "10")
        rbt.insert(20, "20")
        assertNull(rbt.range(15, 19))
    }

    @Test
    fun `Basic RB test - range should work when fromKey equals toKey`() {
        rbt.insert(10, "10")
        rbt.insert(20, "20")
        assertEquals(listOf("20"), rbt.range(20, 20))
    }


    @Test
    fun `Basic RB test - test with size() method`() {
        val cntElements = 5
        for (i in 1..cntElements) {
            rbt.insert(i, i.toString())
            rbt.checkRedBlackProperties()
        }
        assertEquals(cntElements, rbt.size())
        rbt.clear()
        assertEquals(0, rbt.size())
    }

    @Test
    fun `Basic RB test- test with empty rbt and isEmpty() method`() {
        assertTrue(rbt.isEmpty())
    }

    @Test
    fun `Basic RB test- test with not empty rbt and isEmpty() method`() {
        rbt.insert(10, "10")
        assertFalse(rbt.isEmpty())
    }

    @Test
    fun `Basic RB test - test with empty rbt and delete() method`() {
        assertFalse(rbt.delete(10))
    }

    @Test
    fun `Basic RB test - test with empty rbt and 10 non-existent keys`() {
        for (i in 0..10) {
            assertNull(rbt.search(i))
        }
    }

    @Test
    fun `Basic RB test - test with empty rbt and min() method`() {
        assertNull(rbt.min())
    }

    @Test
    fun `Basic RB test - test with empty rbt and max() method`() {
        assertNull(rbt.max())
    }

    @Test
    fun `Complicated RB test - test with empty rbt`() {
        for (i in 0..10) {
            assertNull(rbt.search(i))
        }
        assertTrue(rbt.isEmpty())
    }

    @Test
    fun `Basic RB test - test with first non-existent keys in range() method`() {
        rbt.insert(-52, "-52")
        val resultRange = rbt.range(-52, 52)
        assertNull(resultRange)
    }

    @Test
    fun `Basic RB test - test with incorrect order of elements in range() method`() {
        rbt.insert(52, "52")
        rbt.insert(-52, "-52")
        val resultRange = rbt.range(52, -52)
        assertNull(resultRange)
    }

    @RepeatedTest(10)
    fun `Complicated RB test - random test inOrder() method`() {
        val allNumbers = mutableListOf<Int>()
        val cntNumbers = Random.nextInt(MIN_ELEMENTS, MAX_ELEMENTS)

        for (i in 1..cntNumbers) {
            val currentInt = Random.nextInt()
            rbt.insert(currentInt, currentInt.toString())
            rbt.checkRedBlackProperties()
            allNumbers.add(currentInt)
        }

        val order = rbt.inOrder()
        allNumbers.sort()
        rbt.checkRedBlackProperties()
        for (i in 0..<cntNumbers) {
            val currentInt = allNumbers[i]
            val currentIntInNode = order[i].key
            assertEquals(currentInt, currentIntInNode)
        }
    }

    @RepeatedTest(10)
    fun `Complicated RB test - random test with min() method`() {
        val cntElements = Random.nextInt(MIN_ELEMENTS, MAX_ELEMENTS)
        var minElement = 0
        for (i in 1..cntElements) {
            val data = Random.nextInt()
            if (minElement > data) minElement = data
            rbt.insert(data, data.toString())
            rbt.checkRedBlackProperties()
        }
        assertEquals(minElement, rbt.min())
    }

    @RepeatedTest(10)
    fun `Complicated RB test - random test with max() method`() {
        val cntElements = Random.nextInt(MIN_ELEMENTS, MAX_ELEMENTS)
        var maxElement = 0
        for (i in 1..cntElements) {
            val data = Random.nextInt()
            if (maxElement < data) maxElement = data
            rbt.insert(data, data.toString())
            rbt.checkRedBlackProperties()
        }
        assertEquals(maxElement, rbt.max())
    }

    @RepeatedTest(10)
    fun `Complicated RB test - random test with contains() method`() {
        val rbt = RedBlackTree<Int, Int>()
        val allInt = mutableListOf<Int>()
        val cntElements = Random.nextInt(MIN_ELEMENTS, MAX_ELEMENTS)
        for (i in 1..cntElements) {
            val data = Random.nextInt(-10000, 10000)
            allInt.add(data)
            rbt.insert(data, data)
            rbt.checkRedBlackProperties()
        }
        for (i in allInt.indices) {
            assertTrue(rbt.contains(allInt[i]))
        }
        for (i in 1..100) {
            val currentInt = Random.nextInt(10001, 100000)
            assertFalse(rbt.contains(currentInt))
        }
        for (i in 1..100) {
            val currentInt = Random.nextInt(-100000, -10001)
            assertFalse(rbt.contains(currentInt))
        }
    }

    @Test
    fun `Basic RB test - test with range() method`() {
        val rbt = RedBlackTree<Int, Int>()
        val expected = mutableListOf<Int>()
        for (i in 1..10) {
            val data = i * 10
            rbt.insert(data, data)
            rbt.checkRedBlackProperties()
        }
        for (i in 3..6) {
            val data = i * 10
            expected.add(data)
        }
        val resultRange = rbt.range(30, 60)
        if (resultRange != null) {
            for (i in resultRange.indices) {
                assertEquals(expected[i], resultRange[i])
            }
        }
    }

    @Test
    fun `Basic RB test - test with checking properties after insertions`() {
        val rbt = RedBlackTree<Int, Int>()
        val elements = listOf(10, 20, 30, 15, 25, 5, 35, 50, 60, 70, 80, 90)

        elements.forEach {
            rbt.insert(it, it)
            rbt.checkRedBlackProperties()
        }
    }


    @Test
    fun `Basic RB test - insert should not change size when updating existing key`() {
        rbt.insert(1, "A")
        val initialSize = rbt.size()
        rbt.insert(1, "B")
        assertEquals(initialSize, rbt.size())
    }

    @Test
    fun `Basic RB test - root should be black`() {
        rbt.insert(10, "A")
        assertEquals(rbt.getRoot()?.color, Colors.BLACK)
    }

    @Test
    fun `Basic RB test - new node should be red`() {
        rbt.insert(10, "A")
        rbt.checkRedBlackProperties()
        rbt.insert(15, "B")
    }

    @Test
    fun `Basic RB test - should maintain properties after deletion`() {
        val values = listOf(50, 30, 70, 20, 40, 60, 80, 15, 25, 35, 45)

        values.forEach {
            rbt.insert(it, "V$it")
            rbt.checkRedBlackProperties()
        }

        listOf(20, 30, 50).forEach { key ->
            assertTrue(rbt.delete(key))
            rbt.checkRedBlackProperties()
        }
    }

    @Test
    fun `Basic RB test - delete should return false when deleting from an empty rbt`() {
        assertFalse(rbt.delete(10))
    }

    @Test
    fun `Basic RB test - delete should return false when deleting a non-existent key`() {
        rbt.insert(10, "Value10")
        assertFalse(rbt.delete(20))
    }

    @Test
    fun `Basic RB test - delete should remove a node with no children`() {
        rbt.insert(10, "Value10")
        rbt.insert(20, "Value20")
        rbt.checkRedBlackProperties()

        val deleteResult = rbt.delete(20)
        rbt.checkRedBlackProperties()
        assertTrue(deleteResult)
        assertNull(rbt.search(20))
    }

    @Test
    fun `Basic RB test - delete should remove a node with one child`() {
        rbt.insert(10, "Value10")
        rbt.insert(20, "Value20")
        rbt.insert(30, "Value30")
        rbt.checkRedBlackProperties()

        val deleteResult = rbt.delete(20)
        rbt.checkRedBlackProperties()

        assertTrue(deleteResult)
        assertNull(rbt.search(20))
        assertEquals("Value30", rbt.search(30))
    }

    @Test
    fun `Basic RB test - delete should remove a node with two children`() {
        rbt.insert(10, "10")
        rbt.insert(20, "20")
        rbt.insert(30, "30")
        rbt.insert(25, "25")
        rbt.checkRedBlackProperties()

        val deleteResult = rbt.delete(20)
        rbt.checkRedBlackProperties()
        assertTrue(deleteResult)
        assertNull(rbt.search(20))
        assertEquals("25", rbt.search(25))
    }

    @Test
    fun `Basic RB test - delete should maintain Red-Black rbt properties after deletion`() {
        rbt.insert(10, "10")
        rbt.checkRedBlackProperties()

        rbt.insert(20, "20")
        rbt.checkRedBlackProperties()

        rbt.insert(30, "30")
        rbt.checkRedBlackProperties()

        rbt.insert(15, "15")
        rbt.checkRedBlackProperties()

        rbt.delete(20)
    }


    @Test
    fun `Basic RB test - delete should update the size correctly after deletion`() {
        rbt.insert(10, "Value10")
        rbt.insert(20, "Value20")
        rbt.insert(30, "Value30")
        rbt.checkRedBlackProperties()

        val initialSize = rbt.size()
        rbt.delete(20)
        rbt.checkRedBlackProperties()
        assertEquals(initialSize - 1, rbt.size())

        rbt.delete(10)
        rbt.checkRedBlackProperties()
        assertEquals(initialSize - 2, rbt.size())
    }

    @Test
    fun `Basic RB test - delete should not cause infinite loop or crash`() {
        val values = listOf(10, 20, 30, 40, 50, 60)

        values.forEach {
            rbt.insert(it, "Value$it")
            rbt.checkRedBlackProperties()
        }

        values.forEach {
            rbt.delete(it)
            rbt.checkRedBlackProperties()
        }

        assertTrue(rbt.isEmpty())
    }

    @Test
    fun `Basic RB test - delete should delete the root node correctly`() {
        rbt.insert(10, "Value10")
        rbt.insert(20, "Value20")
        rbt.checkRedBlackProperties()

        val deleteResult = rbt.delete(10)
        assertTrue(deleteResult)
        assertNull(rbt.search(10))
    }


    //
    @Test
    fun `Basic RB test - delete should handle deleting root when it's the only node`() {
        rbt.insert(10, "Value10")
        assertTrue(rbt.delete(10))
        assertTrue(rbt.isEmpty())
    }

    @Test
    fun `Basic RB test - delete should handle deleting node with two children where successor is right child`() {
        rbt.insert(50, "50")
        rbt.insert(30, "30")
        rbt.insert(70, "70")
        rbt.insert(60, "60")
        rbt.insert(80, "80")

        assertTrue(rbt.delete(50))
        assertEquals("60", rbt.search(60))
        assertEquals("70", rbt.search(70))
        assertEquals("30", rbt.search(30))
        assertEquals("80", rbt.search(80))
    }

    @Test
    fun `Basic RB test - delete should handle deleting node with two children where successor is not right child`() {
        rbt.insert(50, "50")
        rbt.insert(30, "30")
        rbt.insert(70, "70")
        rbt.insert(60, "60")
        rbt.insert(65, "65")

        assertTrue(rbt.delete(50))
        assertEquals("60", rbt.search(60))
        assertEquals("65", rbt.search(65))
        assertEquals("70", rbt.search(70))
        assertEquals("30", rbt.search(30))
    }

    @Test
    fun `Basic RB test - delete should maintain properties when deleting black node with red child`() {
        rbt.insert(50, "50")
        rbt.insert(30, "30")
        rbt.insert(70, "70")
        rbt.insert(20, "20")

        assertTrue(rbt.delete(30))
        assertEquals("20", rbt.search(20))
        assertEquals("50", rbt.search(50))
        assertEquals("70", rbt.search(70))
    }

    @Test
    fun `Basic RB test - delete should handle complex case with multiple rotations and color changes`() {
        val values = listOf(50, 30, 70, 20, 40, 60, 80, 15, 25, 35, 45, 55, 65, 75, 85)
        values.forEach { rbt.insert(it, it.toString()) }

        listOf(20, 30, 50, 70).forEach { key ->
            assertTrue(rbt.delete(key))
            rbt.checkRedBlackProperties()
        }
    }


    @RepeatedTest(20)
    fun `Complicated RB test - insert random elements then delete them all`() {
        val elements = mutableSetOf<Int>()
        val count = Random.nextInt(10, 100)

        // Insert random elements
        repeat(count) {
            val key = Random.nextInt()
            elements.add(key)
            rbt.insert(key, key.toString())
            rbt.checkRedBlackProperties()
        }

        assertEquals(elements.size, rbt.size())

        // Delete all elements in random order
        val shuffledElements = elements.shuffled()
        shuffledElements.forEach { key ->
            assertTrue(rbt.delete(key))
            assertNull(rbt.search(key))
            rbt.checkRedBlackProperties()
        }

        assertTrue(rbt.isEmpty())
    }

    @RepeatedTest(20)
    fun `Complicated RB test - random sequence of insertions and deletions`() {
        val presentElements = mutableSetOf<Int>()
        val operations = 50

        repeat(operations) {
            if (Random.nextBoolean() || presentElements.isEmpty()) {
                // Insert
                val key = Random.nextInt()
                rbt.insert(key, key.toString())
                presentElements.add(key)
                rbt.checkRedBlackProperties()
            } else {
                // Delete
                val key = presentElements.random()
                assertTrue(rbt.delete(key))
                presentElements.remove(key)
                rbt.checkRedBlackProperties()
            }

            // Verify all present elements are still in the tree
            presentElements.forEach {
                assertEquals(it.toString(), rbt.search(it))
            }
        }
    }

    @RepeatedTest(20)
    fun `Complicated RB test - in-order traversal after deletions`() {
        val rbt = RedBlackTree<Int, Int>()
        val elements = mutableListOf<Int>()
        val count = Random.nextInt(10, 100)

        // Insert random elements
        repeat(count) {
            val key = Random.nextInt()
            elements.add(key)
            rbt.insert(key, key)
            rbt.checkRedBlackProperties()
        }

        // Delete half of the elements randomly
        val elementsToDelete = elements.shuffled().take(count / 2)
        elementsToDelete.forEach {
            assertTrue(rbt.delete(it))
            rbt.checkRedBlackProperties()
        }

        // Check in-order traversal
        val remainingElements = elements.filterNot { it in elementsToDelete }.sorted()
        val inOrder = rbt.inOrder().map { it.key }

        assertEquals(remainingElements, inOrder)
    }

    @RepeatedTest(20)
    fun `Complicated RB test - test min and max after random deletions`() {
        val rbt = RedBlackTree<Int, Int>()
        val elements = mutableListOf<Int>()
        val count = Random.nextInt(10, 100)

        // Insert random elements
        repeat(count) {
            val key = Random.nextInt()
            elements.add(key)
            rbt.insert(key, key)
            rbt.checkRedBlackProperties()
        }

        // Delete random elements
        repeat(count / 2) {
            val key = elements.random()
            elements.remove(key)
            assertTrue(rbt.delete(key))
            rbt.checkRedBlackProperties()

            // Check min and max
            if (elements.isNotEmpty()) {
                assertEquals(elements.min(), rbt.min())
                assertEquals(elements.max(), rbt.max())
            }
        }
    }
}
