package trees.avl

import trees.base.Node

class AVLNode<K : Comparable<K>, V>(
    override var key: K,
    override var value: V,
    override var left: AVLNode<K, V>? = null,
    override var right: AVLNode<K, V>? = null,
    var height: Int = 1
) : Node<K, V, AVLNode<K, V>>