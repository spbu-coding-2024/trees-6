package trees.binary

import trees.base.Node

class BinaryNode<K: Comparable<K>, V>(
    override var key: K,
    override var value: V,
    override var left: BinaryNode<K, V>? = null,
    override var right: BinaryNode<K, V>? = null
): Node<K, V, BinaryNode<K, V>>