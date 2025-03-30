import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Tag
import trees.avl.AVLNode
import trees.avl.AVLTree
import kotlin.math.pow
import kotlin.random.Random

const val MAD_STRING_LENGTH = 20000
const val MAD_MAX_COUNT_ELEMENTS_IN_TESTS = 20_000
const val MAD_MIN_COUNT_ELEMENTS_IN_TESTS = 100

//@Tag("mad")
class MadTests {
    private fun randomStringByApacheCommons() = RandomStringUtils.randomAlphanumeric(MAD_STRING_LENGTH)

    @RepeatedTest(100)
    fun `Mad test 1 for AVL`() {
        val avl = AVLTree<Int, String>()
        val minHeight = 1
        val maxExponent = 20.0
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

    @RepeatedTest(100)
    fun `Mad AVL random test inOrder() method`() {
        val avl = AVLTree<Int, Int>()
        val allNumbers = mutableListOf<Int>()
        val cntNumbers = Random.nextInt(MAD_MIN_COUNT_ELEMENTS_IN_TESTS, MAD_MAX_COUNT_ELEMENTS_IN_TESTS)

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

    @RepeatedTest(100)
    fun `Mad AVL random tests`() {
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

    @RepeatedTest(100)
    fun `Mad AVL random test with min() method`() {
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

    @RepeatedTest(100)
    fun `Mad AVL random test with max() method`() {
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

    @RepeatedTest(100)
    fun `Mad AVL random test with contains() method`() {
        val avl = AVLTree<Int, Int>()
        val allInt = mutableListOf<Int>()
        val cntElements = Random.nextInt(MIN_COUNT_ELEMENTS_IN_TESTS, MAX_COUNT_ELEMENTS_IN_TESTS)
        for (i in 1..cntElements) {
            val data = Random.nextInt(-1000000, 1000000)
            allInt.add(data)
            avl.insert(data, data)
        }
        for (i in allInt.indices) {
            assertTrue(avl.contains(allInt[i]))
        }
        for (i in 1..10000) {
            val currentInt = Random.nextInt(1000001, 10000000)
            assertFalse(avl.contains(currentInt))
        }
        for (i in 1..10000) {
            val currentInt = Random.nextInt(-10000000, -1000001)
            assertFalse(avl.contains(currentInt))
        }
    }
}
