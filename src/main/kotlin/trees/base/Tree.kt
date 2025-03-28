package trees.base

interface Tree<K : Comparable<K>, V, N : Node<K, V, N>> {
    // methods for changing the tree
    fun insert(key: K, value: V): N
    fun delete(key: K): Boolean
    fun search(key: K): V?
    fun clear()

    // methods for getting values
    fun size(): Int
    fun inOrder(): List<Node<K, V, N>>

    // methods for checks
    fun isEmpty(): Boolean
}