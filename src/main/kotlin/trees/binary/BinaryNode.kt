package trees.binary

import trees.base.Node

class BinaryNode<K: Comparable<K>, V>(
    override val key: K,
    override val value: V,
    override var left: Node<K, V>? = null,
    override var right: Node<K, V>? = null
): Node<K, V>