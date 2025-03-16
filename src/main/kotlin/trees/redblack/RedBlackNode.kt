package trees.redblack

import trees.base.Node
import trees.redblack.Colors

class RedBlackNode<K: Comparable<K>, V>(
    override val key: K,
    override val value: V,
    val parent: Node<K, V>? = null,
    override var left: Node<K, V>? = null,
    override var right: Node<K, V>? = null,
    val color: Colors = Colors.RED
): Node<K, V>