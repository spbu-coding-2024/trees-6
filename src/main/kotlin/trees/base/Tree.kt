package trees.base

interface Tree<K: Comparable<K>, V> {
    // methods for changing the tree
    fun add(key: K, value: V)
    fun delete(value: K): Boolean
    fun search(key: K): V?
    fun clear()

    // methods for getting values
    fun size(): Int
    fun min(): V?
    fun max(): V?

    // methods for checks
    fun isEmpty(): Boolean
}