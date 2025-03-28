package trees.base

/**
 * Abstract base class for a binary search tree implementation
 *
 * @param K Universal comparable type for key storage
 * @param V Universal type for storing values
 * @param N Universal type for storing `Node<K, V, N>`
 */
abstract class AbstractTree<K : Comparable<K>, V, N : Node<K, V, N>> : Tree<K, V, N> {
    private var root: N? = null
    private var countNodes: Int = 0

    protected fun getRoot(): N? {
        return root
    }

    protected fun setRoot(newValue: N?): Boolean {
        root = newValue; return true
    }

    protected fun getCountNodes(): Int {
        return countNodes
    }

    protected fun setCountNodes(newValue: Int): Boolean {
        if (newValue < 0) return false
        countNodes = newValue
        return true
    }

    protected fun addOneToCountNodes(): Boolean {
        setCountNodes(getCountNodes() + 1)
        return true
    }

    protected fun removeOneFromCountNodes(): Boolean {
        if (getCountNodes() == 0) return false
        setCountNodes(getCountNodes() - 1)
        return true
    }

    abstract override fun insert(key: K, value: V): N
    abstract override fun delete(key: K): Boolean

    /**
     * Removes all nodes from the tree
     * `root = null` and `countNodes = 0`
     *
     * @sample samples.avl.sampleClear
     */
    override fun clear() {
        setRoot(null)
        setCountNodes(0)
    }

    /**
     * Returns the number of nodes in the tree
     *
     * @return The total count of nodes in the tree
     * @sample samples.avl.sampleSize
     */
    override fun size(): Int {
        return getCountNodes()
    }

    /**
     * Checks if the tree is empty
     *
     * @return `true` if root is `null`, `false` if root is not `null`
     * @sample samples.avl.sampleIsEmpty
     */
    override fun isEmpty(): Boolean {
        return getRoot() == null
    }

    /**
     * Traverses the tree by keys in ascending order of keys
     *
     * @return `List<Node<K, V, N>>` of all nodes in the tree in ascending order by key
     * @sample samples.avl.sampleInOrder
     */
    override fun inOrder(): List<Node<K, V, N>> {
        val result = mutableListOf<Node<K, V, N>>()
        inOrderRecursively(getRoot(), result)
        return result.toList()
    }

    private fun inOrderRecursively(node: Node<K, V, N>?, result: MutableList<Node<K, V, N>>) {
        if (node != null) {
            inOrderRecursively(node.left, result)
            result.add(node)
            inOrderRecursively(node.right, result)
        }
    }

    /**
     * Searches for a value in the key tree
     *
     * @param key The key that will be used to search for the value
     * @return value obtained by the key, `null` if value is not found
     * @sample samples.avl.sampleSearch
     */
    override fun search(key: K): V? {
        val result = searchRecursively(getRoot(), key)
        return result
    }

    private fun searchRecursively(node: Node<K, V, N>?, key: K): V? {
        if (node == null) return null
        if (node.key == key) return node.value
        if (node.key > key) return searchRecursively(node.left, key)
        return searchRecursively(node.right, key)
    }

    /**
     * Finds the maximum key in the tree
     *
     * @return max key in the tree, `null` if tree is empty
     * @sample samples.avl.sampleMax
     */
    fun max(): K? {
        val root = getRoot() ?: return null
        val maxNode = maxNode(root)
        return maxNode.key
    }

    private fun maxNode(node: Node<K, V, N>): Node<K, V, N> {
        var current = node
        while (current.right != null) {
            current = current.right ?: throw BrakeException()
        }
        return current
    }

    /**
     * Finds the minimum key in the tree
     *
     * @return min key in the tree, `null` if tree is empty
     * @sample samples.avl.sampleMin
     */
    fun min(): K? {
        val root = getRoot() ?: return null
        val minNode = minNode(root)
        return minNode.key
    }

    private fun minNode(node: Node<K, V, N>): Node<K, V, N> {
        var current = node
        while (current.left != null) {
            current = current.left ?: throw BrakeException()
        }
        return current
    }

    /**
     * Returns a list with nodes whose keys are in the specified constraint
     *
     * @param start The starting key of the range
     * @param end The ending key of the range
     * @return list of values in the range, `null` if no keys are found or range is invalid
     * @sample samples.avl.sampleRange
     */
    fun range(start: K, end: K): List<V>? {
        if (!validateKeysForRangeMethod(start, end)) return null
        val order = inOrder()
        val result = mutableListOf<V>()
        for (i in order.indices) {
            if (order[i].key in start..end) result.add(order[i].value)
        }
        return result.toList()
    }

    private fun validateKeysForRangeMethod(first: K, second: K): Boolean {
        if (search(first) == null || search(second) == null) return false
        if (first > second) return false
        return true
    }

    /**
     * Checks if a specific value exists in the tree
     *
     * @param value The value for searching in the tree
     * @return `true` if the value is in the tree, otherwise `false`
     * @sample samples.avl.sampleContains
     */
    fun contains(value: V): Boolean {
        val order = inOrder()
        findNodeInListByValue(order, value) ?: return false
        return true
    }

    private fun findNodeInListByValue(list: List<Node<K, V, N>>, value: V): Node<K, V, N>? {
        for (i in list.indices) {
            if (list[i].value == value) {
                return list[i]
            }
        }
        return null
    }

    // Just a plug for private methods
    protected class BrakeException : Exception()
}