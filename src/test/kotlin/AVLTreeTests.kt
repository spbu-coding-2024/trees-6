import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import trees.avl.AVLTree
import kotlin.math.pow
import kotlin.random.Random

class AVLTreeTests {

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
    fun `Complicated AVL test with varying height`() {
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
    fun `Basic AVL test with empty tree and isEmpty() function`() {
        val avl = AVLTree<Int, String>()
        assertTrue(avl.isEmpty())
    }

    @Test
    fun `Basic AVL test with empty tree and height() method`() {
        val avl = AVLTree<Int, String>()
        assertEquals(0, avl.height())
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
}