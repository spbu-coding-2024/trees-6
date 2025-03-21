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
    abstract override fun search(key: K): V?
    abstract override fun height(): Int
    abstract fun inOrder(): List<N>

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