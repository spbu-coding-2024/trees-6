import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Tag
import trees.avl.AVLTree
import kotlin.math.pow
import kotlin.random.Random

const val MAD_STRING_LENGTH = 20_000
const val MAD_MAX_COUNT_ELEMENTS_IN_TESTS = 20_000 // Adequate value is 20_000, mad value is 100_000+
const val MAD_MIN_COUNT_ELEMENTS_IN_TESTS = 1_000 // Adequate value is 1_000, mad value is 95_000+

// These crazy tests are needed to test the library on large amounts of data.
// They are not basic. They're just for variety.
// To run them, you need to comment the string "@Tag("mad")"
// and run the test with the command "./gradlew test" in the root directory of the project.

@Tag("mad")
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
        val avl = AVLTree<Int, String>()
        val allNumbers = mutableListOf<Int>()
        val cntNumbers = Random.nextInt(MAD_MIN_COUNT_ELEMENTS_IN_TESTS, MAD_MAX_COUNT_ELEMENTS_IN_TESTS)

        for (i in 1..cntNumbers) {
            allNumbers.add(i)
        }
        allNumbers.shuffle()
        for (i in 0..<cntNumbers) {
            val currentInt = allNumbers[i]
            avl.insert(currentInt, randomStringByApacheCommons())
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
        val avl = AVLTree<Int, Int>()
        val cntElements = Random.nextInt(MAD_MIN_COUNT_ELEMENTS_IN_TESTS, MAD_MAX_COUNT_ELEMENTS_IN_TESTS)
        val allInt = mutableListOf<Int>()
        for (i in 1..cntElements) {
            allInt.add(i)
        }
        allInt.shuffle()
        for (i in 0..<cntElements) {
            avl.insert(allInt[i], allInt[i])
        }
        // Checking tree
        for (i in 0..<cntElements) {
            assertEquals(avl.search(allInt[i]), allInt[i])
        }
        // Delete some nodes from tree
        val cntDeleted = Random.nextInt(1, cntElements)
        val allDeleted = mutableListOf<Int>()
        for (i in 0..<cntDeleted) {
            allDeleted.add(allInt[i])
            assertTrue(avl.delete(allInt[i]))
        }
        for (i in 0..<cntDeleted) {
            allInt.remove(allDeleted[i])
        }
        // Checking deleted
        for (i in 0..<cntDeleted) {
            assertNull(avl.search(allDeleted[i]))
        }
        allInt.shuffle()
        // Checking tree
        for (i in 0..<(allInt.size)) {
            assertEquals(avl.search(allInt[i]), allInt[i])
        }
    }

    @RepeatedTest(100)
    fun `Mad AVL random test with min() method`() {
        val avl = AVLTree<Int, Int>()
        val cntElements = Random.nextInt(MAD_MIN_COUNT_ELEMENTS_IN_TESTS, MAD_MAX_COUNT_ELEMENTS_IN_TESTS)
        for (i in 1..cntElements) {
            avl.insert(i, i)
        }
        assertEquals(1, avl.min())
    }

    @RepeatedTest(100)
    fun `Mad AVL random test with max() method`() {
        val avl = AVLTree<Int, Int>()
        val cntElements = Random.nextInt(MAD_MIN_COUNT_ELEMENTS_IN_TESTS, MAD_MAX_COUNT_ELEMENTS_IN_TESTS)
        for (i in 1..cntElements) {
            avl.insert(i, i)
        }
        assertEquals(cntElements, avl.max())
    }

    @RepeatedTest(100)
    fun `Mad AVL random test with checkValue() method`() {
        val avl = AVLTree<Int, Int>()
        val allInt = mutableListOf<Int>()
        val cntElements = Random.nextInt(MAD_MIN_COUNT_ELEMENTS_IN_TESTS, MAD_MAX_COUNT_ELEMENTS_IN_TESTS)
        for (i in 1..cntElements) {
            allInt.add(i)
        }
        allInt.shuffle()
        for (i in 0..<cntElements) {
            avl.insert(allInt[i], allInt[i])
        }
        for (i in allInt.indices) {
            assertTrue(avl.checkValue(allInt[i]))
        }
        for (i in 1..1_000) {
            val currentInt = Random.nextInt(cntElements + 10, cntElements + 10_000_000)
            assertFalse(avl.checkValue(currentInt))
        }
        for (i in 1..1_000) {
            val currentInt = Random.nextInt(-10_000_000, -10)
            assertFalse(avl.checkValue(currentInt))
        }
    }
}
