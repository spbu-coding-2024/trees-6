import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.apache.commons.lang3.RandomStringUtils
import trees.avl.AVLNode
import trees.avl.AVLTree
import kotlin.math.pow
import kotlin.random.Random

class AVLTreeTests {
    private val stringLength = 20
    private fun randomStringByApacheCommons() = RandomStringUtils.randomAlphanumeric(stringLength)

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

    @Test
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
    fun `Complicated AVL test with empty tree`() {
        val avl = AVLTree<Int, String>()
        for (i in 0..10)
            assertNull(avl.search(i))
        assertEquals(0, avl.height())
        assertTrue(avl.isEmpty())
    }

    @Test
    fun `Complicated AVL random test inOrder() method`() {
        val avl = AVLTree<Int, Int>()
        val allNumbers = mutableListOf<Int>()
        val cntNumbers = Random.nextInt(1, 3000)

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

    @Test
    fun `Complicated AVL random test with 3000 nodes and strings as keys and int as values`() {
        val avl = AVLTree<String, Int>()
        val cntElements = 3000
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
}