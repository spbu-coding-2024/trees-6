package trees.base

import trees.redblack.RedBlackNode

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
    abstract override fun search(key: K): V?
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

    // Максон, чекни ноду в определениях. Так оставляем чи меняем Node<> на N?
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

    fun max(): K? {
        TODO("Not yet implemented")
    }

    fun min(): K? {
        TODO("Not yet implemented")
    }

    fun range(start: K, end: K): List<V> {
        TODO("Not yet implemented")
    }

    fun contains(value: V): Boolean {
        TODO("Not yet implemented")
    }
}