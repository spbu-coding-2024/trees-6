package trees.avl

import trees.base.Node

class AVLNode<K: Comparable<K>, V>(
    override val key: K,
    override val value: V,
    override var left: Node<K, V>? = null,
    override var right: Node<K, V>? = null,
    var height: Int = 1
): Node<K, V>