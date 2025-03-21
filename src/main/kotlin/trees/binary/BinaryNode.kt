package trees.binary

import trees.base.Node

class BinaryNode<K: Comparable<K>, V>(
    override val key: K,
    override val value: V,
    override var left: BinaryNode<K, V>? = null,
    override var right: BinaryNode<K, V>? = null
): Node<K, V, BinaryNode<K, V>>