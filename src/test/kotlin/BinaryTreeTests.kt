import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import trees.binary.BinaryTree

class BinaryTreeTest {
    private lateinit var tree: BinaryTree<Int, String>

    @BeforeEach
    fun setUp() {
        tree = BinaryTree()
    }

    @Test
    fun `should create empty tree`() {
        assertNull(tree.getRoot())
        assertEquals(0, tree.size())
    }

    @Test
    fun `insert into empty tree`() {
        val node = tree.insert(10, "A")

        assertEquals(1, tree.size())
        assertEquals("A", tree.search(10))
        assertEquals(node, tree.getRoot())
        assertNull(node.left)
        assertNull(node.right)
    }

    @Test
    fun `insert multiple elements in correct order`() {
        tree.insert(10, "Root")
        tree.insert(5, "Left")
        tree.insert(15, "Right")

        assertEquals(3, tree.size())
        val root = tree.getRoot()
        assertNotNull(root)
        assertEquals(10, root?.key)
        assertEquals(5, root?.left?.key)
        assertEquals(15, root?.right?.key)
    }

    @Test
    fun `insert duplicate key should update value`() {
        tree.insert(10, "Initial")
        tree.insert(10, "Updated")

        assertEquals(1, tree.size())
        assertEquals("Updated", tree.search(10))
    }

    @Test
    fun `search in empty tree returns null`() {
        assertNull(tree.search(10))
    }

    @Test
    fun `search for non-existent key returns null`() {
        tree.insert(10, "A")
        tree.insert(5, "B")
        assertNull(tree.search(20))
    }

    @Test
    fun `delete from empty tree returns false`() {
        assertFalse(tree.delete(10))
        assertEquals(0, tree.size())
    }

    @Test
    fun `delete non-existent key returns false`() {
        tree.insert(10, "A")
        assertFalse(tree.delete(20))
        assertEquals(1, tree.size())
    }

    @Test
    fun `delete leaf node`() {
        tree.insert(10, "A")
        tree.insert(5, "B")

        assertTrue(tree.delete(5))
        assertEquals(1, tree.size())
        assertNull(tree.getRoot()?.left)
    }

    @Test
    fun `delete node with only left child`() {
        tree.insert(10, "A")
        tree.insert(5, "B")
        tree.insert(3, "C")

        assertTrue(tree.delete(5))
        assertEquals(2, tree.size())
        assertEquals(3, tree.getRoot()?.left?.key)
    }

    @Test
    fun `delete node with only right child`() {
        tree.insert(10, "A")
        tree.insert(5, "B")
        tree.insert(7, "C")

        assertTrue(tree.delete(5))
        assertEquals(2, tree.size())
        assertEquals(7, tree.getRoot()?.left?.key)
    }

    @Test
    fun `delete node with two children`() {
        tree.insert(10, "A")
        tree.insert(5, "B")
        tree.insert(15, "C")
        tree.insert(12, "D")
        tree.insert(17, "E")

        assertTrue(tree.delete(15))
        assertEquals(4, tree.size())
        assertEquals(17, tree.getRoot()?.right?.key)
        assertEquals(12, tree.getRoot()?.right?.left?.key)
    }

    @Test
    fun `delete root node with two children`() {
        tree.insert(10, "A")
        tree.insert(5, "B")
        tree.insert(15, "C")

        assertTrue(tree.delete(10))
        assertEquals(2, tree.size())
        assertEquals(15, tree.getRoot()?.key)
        assertEquals(5, tree.getRoot()?.left?.key)
    }

    @Test
    fun `in-order traversal returns sorted keys`() {
        tree.insert(10, "A")
        tree.insert(5, "B")
        tree.insert(15, "C")
        tree.insert(3, "D")
        tree.insert(7, "E")
        tree.insert(12, "F")
        tree.insert(17, "G")

        val expected = listOf(3, 5, 7, 10, 12, 15, 17)
        val actual = tree.inOrder().map { it.key }
        assertEquals(expected, actual)
    }

    @Test
    fun `balance tree maintains all elements`() {
        tree.insert(5, "A")
        tree.insert(4, "B")
        tree.insert(3, "C")
        tree.insert(2, "D")
        tree.insert(1, "E")

        tree.getBalance()

        assertEquals(5, tree.size())
        assertEquals("A", tree.search(5))
        assertEquals("B", tree.search(4))
        assertEquals("C", tree.search(3))
        assertEquals("D", tree.search(2))
        assertEquals("E", tree.search(1))

        val expectedInOrder = listOf(1, 2, 3, 4, 5)
        val actualInOrder = tree.inOrder().map { it.key }
        assertEquals(expectedInOrder, actualInOrder)
    }

    @Test
    fun `findMinNode returns leftmost node`() {
        tree.insert(10, "A")
        tree.insert(5, "B")
        tree.insert(15, "C")
        tree.insert(3, "D")
        tree.insert(7, "E")

        val minNode = tree.getRoot()?.let { tree.findMinNode(it) }
        assertEquals(3, minNode?.key)
    }

    @Test
    fun `findMaxNode returns rightmost node`() {
        tree.insert(10, "A")
        tree.insert(5, "B")
        tree.insert(15, "C")
        tree.insert(12, "D")
        tree.insert(20, "E")

        val maxNode = tree.getRoot()?.let { tree.findMaxNode(it) }
        assertEquals(20, maxNode?.key)
    }

    @Test
    fun `tree structure after complex operations`() {
        tree.insert(50, "Root")
        tree.insert(30, "L")
        tree.insert(70, "R")
        tree.insert(20, "LL")
        tree.insert(40, "LR")
        tree.insert(60, "RL")
        tree.insert(80, "RR")

        val root = tree.getRoot()
        assertEquals(50, root?.key)
        assertEquals(30, root?.left?.key)
        assertEquals(70, root?.right?.key)
        assertEquals(20, root?.left?.left?.key)
        assertEquals(40, root?.left?.right?.key)
        assertEquals(60, root?.right?.left?.key)
        assertEquals(80, root?.right?.right?.key)

        tree.delete(30)

        assertEquals(40, root?.left?.key)
        assertEquals(20, root?.left?.left?.key)
        assertNull(root?.left?.right)

        tree.delete(50)
        assertEquals(60, tree.getRoot()?.key)
        assertEquals(40, tree.getRoot()?.left?.key)
        assertEquals(70, tree.getRoot()?.right?.key)
        assertEquals(80, tree.getRoot()?.right?.right?.key)
    }
}