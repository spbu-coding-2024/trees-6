package trees.base

abstract class AbstractTree<K: Comparable<K>, V, N: Node<K, V>>: Tree<K, V> {
    private var root: N? = null
    private var countNodes: Int = 0

    abstract override fun add(key: K, value: V)
    abstract override fun delete(value: K): Boolean
    abstract override fun search(key: K): V?

    override fun clear() {
        root = null
        countNodes = 0
    }

    override fun max(): V? {
        TODO("Not yet implemented")
    }

    override fun min(): V? {
        TODO("Not yet implemented")
    }

    override fun size(): Int {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }
}