import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.RepeatedTest
import trees.avl.AVLNode
import trees.avl.AVLTree
import kotlin.math.pow
import kotlin.random.Random

const val STRING_LENGTH = 20
const val MAX_COUNT_ELEMENTS_IN_TESTS = 3000
const val MIN_COUNT_ELEMENTS_IN_TESTS = 10

class AVLTreeTests {
    private fun randomStringByApacheCommons() = RandomStringUtils.randomAlphanumeric(STRING_LENGTH)

    @Test
    fun `Basic AVL test with simply insert and search and delete`() {
        val avl = AVLTree<Int, String>()
        val firstMSG = "qwerty"
        val secondMSG = "CATS"
        val thirdMSG = "52"

        avl.insert(10, firstMSG)
        avl.insert(15, secondMSG)
        avl.insert(52, thirdMSG)

        assertEquals(thirdMSG, avl.search(52))
        assertEquals(secondMSG, avl.search(15))
        assertEquals(firstMSG, avl.search(10))

        val deleteResult = avl.delete(52)
        assertTrue(deleteResult)
        assertNull(avl.search(52))
    }

    @Test
    fun `Basic AVL test with value redefinition`() {
        val avl = AVLTree<Int, String>()
        val key = 10
        val firstValue = "first_value"
        val secondValue = "second_value"
        avl.insert(key, firstValue)
        assertEquals(firstValue, avl.search(key))
        avl.insert(key, secondValue)
        assertEquals(secondValue, avl.search(key))
    }

    @RepeatedTest(10)
    fun `Complicated AVL random test with varying height`() {
        val avl = AVLTree<Int, String>()
        val minHeight = 1
        val maxExponent = 12.0
        val maxHeight = 2.0.pow(maxExponent)
        val cntElements = Random.nextInt(minHeight, maxHeight.toInt())
        for (i in 1..cntElements) {
            avl.insert(i, i.toString())
        }
        var trueExponent = 0.0
        for (i in 1..maxExponent.toInt()) {
            if (cntElements < 2.0.pow(i)) {
                trueExponent = i.toDouble()
                break
            }
        }

        assertEquals(avl.height(), trueExponent.toInt())

        for (i in 1..cntElements) {
            assertEquals(i.toString(), avl.search(i))
            val deleteResult = avl.delete(i)
            assertTrue(deleteResult)
            assertNull(avl.search(i))
        }
    }

    @Test
    fun `Basic AVL test with clear() method`() {
        val avl = AVLTree<Int, Int>()
        avl.insert(10, 10)
        avl.clear()
        assertEquals(0, avl.height())
    }

    @Test
    fun `Basic AVL test with size() method`() {
        val avl = AVLTree<Int, Int>()
        val cntElements = 5
        for (i in 1..cntElements) {
            avl.insert(i, i)
        }
        assertEquals(cntElements, avl.size())
        avl.clear()
        assertEquals(0, avl.size())
    }

    @Test
    fun `Basic AVL test with empty tree and isEmpty() method`() {
        val avl = AVLTree<Int, String>()
        assertTrue(avl.isEmpty())
    }

    @Test
    fun `Basic AVL test with not empty tree and isEmpty() method`() {
        val avl = AVLTree<Int, Int>()
        avl.insert(10, 10)
        assertFalse(avl.isEmpty())
    }

    @Test
    fun `Basic AVL test with empty tree and height() method`() {
        val avl = AVLTree<Int, String>()
        assertEquals(0, avl.height())
    }

    @Test
    fun `Basic AVL test with empty tree and delete() method`() {
        val avl = AVLTree<Int, String>()
        assertFalse(avl.delete(10))
    }

    @Test
    fun `Basic AVL test with empty tree and 10 non-existent keys`() {
        val avl = AVLTree<Int, String>()
        for (i in 0..10)
            assertNull(avl.search(i))
    }

    @Test
    fun `Basic AVL test with empty tree and min() method`() {
        val avl = AVLTree<Int, Int>()
        assertNull(avl.min())
    }

    @Test
    fun `Basic AVL test with empty tree and max() method`() {
        val avl = AVLTree<Int, Int>()
        assertNull(avl.max())
    }

    @Test
    fun `Complicated AVL test with empty tree`() {
        val avl = AVLTree<Int, String>()
        for (i in 0..10)
            assertNull(avl.search(i))
        assertEquals(0, avl.height())
        assertTrue(avl.isEmpty())
    }

    @Test
    fun `Basic AVL test with first non-existent keys in range() method`() {
        val avl = AVLTree<Int, Int>()
        avl.insert(-52, -52)
        val resultRange = avl.range(-52, 52)
        assertNull(resultRange)
    }

    @Test
    fun `Basic AVL test with second non-existent keys in range() method`() {
        val avl = AVLTree<Int, Int>()
        avl.insert(52, 52)
        val resultRange = avl.range(-52, 52)
        assertNull(resultRange)
    }

    @Test
    fun `Basic AVL test with incorrect order of elements in range() method`() {
        val avl = AVLTree<Int, Int>()
        avl.insert(52, 52)
        avl.insert(-52, -52)
        val resultRange = avl.range(52, -52)
        assertNull(resultRange)
    }

    @RepeatedTest(10)
    fun `Complicated AVL random test inOrder() method`() {
        val avl = AVLTree<Int, Int>()
        val allNumbers = mutableListOf<Int>()
        val cntNumbers = Random.nextInt(MIN_COUNT_ELEMENTS_IN_TESTS, MAX_COUNT_ELEMENTS_IN_TESTS)

        for (i in 1..cntNumbers) {
            val currentInt = Random.nextInt()
            avl.insert(currentInt, currentInt)
            allNumbers.add(currentInt)
        }

        val order = avl.inOrder()
        allNumbers.sort()

        for (i in 0..<cntNumbers) {
            val currentInt = allNumbers[i]
            val currentIntInNode = order[i].key
            assertEquals(currentInt, currentIntInNode)
        }
    }

    @RepeatedTest(10)
    fun `Complicated AVL random tests`() {
        val avl = AVLTree<String, Int>()
        val cntElements = Random.nextInt(MIN_COUNT_ELEMENTS_IN_TESTS, MAX_COUNT_ELEMENTS_IN_TESTS)
        val allNodes = mutableListOf<AVLNode<String, Int>>()

        // Filling in the data
        for (i in 1..cntElements) {
            val key = randomStringByApacheCommons()
            val value = Random.nextInt()
            val newNode = AVLNode<String, Int>(key, value)
            allNodes.add(newNode)
            avl.insert(key, value)
        }

        // Checking tree
        for (i in 0..<cntElements) {
            val currentKey = allNodes[i].key
            val currentValue = allNodes[i].value
            assertEquals(avl.search(currentKey), currentValue)
        }

        // Delete some nodes from tree
        val cntDelete = Random.nextInt(1, cntElements)
        val allDeleted = mutableListOf<AVLNode<String, Int>>()
        for (i in 0..<cntDelete) {
            val deleteNode = AVLNode<String, Int>(allNodes[i].key, allNodes[i].value)
            allDeleted.add(deleteNode)
            val deleteStatus = avl.delete(deleteNode.key)
            assertTrue(deleteStatus)
        }
        for (i in 0..<cntDelete) {
            allNodes.remove(allDeleted[i])
        }

        // Checking tree
        for (i in 0..<cntDelete) {
            assertNull(avl.search(allDeleted[i].key))
        }
    }

    @RepeatedTest(10)
    fun `Complicated AVL random test with min() method`() {
        val avl = AVLTree<Int, Int>()
        val cntElements = Random.nextInt(MIN_COUNT_ELEMENTS_IN_TESTS, MAX_COUNT_ELEMENTS_IN_TESTS)
        var minElement = 0
        for (i in 1..cntElements) {
            val data = Random.nextInt()
            if (minElement > data) minElement = data
            avl.insert(data, data)
        }
        assertEquals(minElement, avl.min())
    }

    @RepeatedTest(10)
    fun `Complicated AVL random test with max() method`() {
        val avl = AVLTree<Int, Int>()
        val cntElements = Random.nextInt(MIN_COUNT_ELEMENTS_IN_TESTS, MAX_COUNT_ELEMENTS_IN_TESTS)
        var maxElement = 0
        for (i in 1..cntElements) {
            val data = Random.nextInt()
            if (maxElement < data) maxElement = data
            avl.insert(data, data)
        }
        assertEquals(maxElement, avl.max())
    }

    @RepeatedTest(10)
    fun `Complicated AVL random test with contains() method`() {
        val avl = AVLTree<Int, Int>()
        val allInt = mutableListOf<Int>()
        val cntElements = Random.nextInt(MIN_COUNT_ELEMENTS_IN_TESTS, MAX_COUNT_ELEMENTS_IN_TESTS)
        for (i in 1..cntElements) {
            val data = Random.nextInt(-10000, 10000)
            allInt.add(data)
            avl.insert(data, data)
        }
        for (i in allInt.indices) {
            assertTrue(avl.contains(allInt[i]))
        }
        for (i in 1..100) {
            val currentInt = Random.nextInt(10001, 100000)
            assertFalse(avl.contains(currentInt))
        }
        for (i in 1..100) {
            val currentInt = Random.nextInt(-100000, -10001)
            assertFalse(avl.contains(currentInt))
        }
    }

    @Test
    fun `Basic AVL test with range() method`() {
        val avl = AVLTree<Int, Int>()
        val expected = mutableListOf<Int>()
        for (i in 1..10) {
            val data = i * 10
            avl.insert(data, data)
        }
        for (i in 3..6) {
            val data = i * 10
            expected.add(data)
        }
        val resultRange = avl.range(30, 60)
        if (resultRange != null) {
            for (i in resultRange.indices) {
                assertEquals(expected[i], resultRange[i])
            }
        }
    }
}