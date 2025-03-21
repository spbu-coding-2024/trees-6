package trees.redblack

import trees.avl.AVLNode
import trees.base.Node
import trees.redblack.Colors

class RedBlackNode<K: Comparable<K>, V>(
    override val key: K,
    override val value: V,
    var parent: RedBlackNode<K, V>? = null,
    override var left: RedBlackNode<K, V>? = null,
    override var right: RedBlackNode<K, V>? = null,
    var color: Colors = Colors.RED
): Node<K, V, RedBlackNode<K, V>>