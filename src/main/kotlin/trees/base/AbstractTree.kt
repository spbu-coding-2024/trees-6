package trees.base

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
    abstract override fun height(): Int


    override fun clear() {
        setRoot(null)
        setCountNodes(0)
    }

    override fun size(): Int {
        return getCountNodes()
    }

    override fun isEmpty(): Boolean {
        return getRoot() == null
    }

    override fun inOrder(): List<Node<K, V, N>> {
        val result = mutableListOf<Node<K, V, N>>()
        inOrderRecursively(getRoot(), result)
        return result
    }

    private fun inOrderRecursively(node: Node<K, V, N>?, result: MutableList<Node<K, V, N>>) {
        if (node != null) {
            inOrderRecursively(node.left, result)
            result.add(node)
            inOrderRecursively(node.right, result)
        }
    }

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

    fun range(start: K, end: K): MutableList<V>? {
        if (!validateKeysForRangeMethod(start, end)) return null
        val order = inOrder()
        val result = mutableListOf<V>()
        for (i in order.indices) {
            if (order[i].key in start..end) result.add(order[i].value)
        }
        return result
    }

    private fun validateKeysForRangeMethod(first: K, second: K): Boolean {
        if (search(first) == null || search(second) == null) return false
        if (first > second) return false
        return true
    }

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