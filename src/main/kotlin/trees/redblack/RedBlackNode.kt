package trees.redblack

import trees.base.Node

class NilNode<K : Comparable<K>, V> : RedBlackNode<K, V>(
        key = 9999999 as K,
        value = 9999999 as V,
        color = Colors.BLACK
)

open class RedBlackNode<K : Comparable<K>, V>(
        override var key: K,
        override var value: V,
        var parent: RedBlackNode<K, V>? = null,
        override var left: RedBlackNode<K, V>? = null,
        override var right: RedBlackNode<K, V>? = null,
        var color: Colors = Colors.RED
) : Node<K, V, RedBlackNode<K, V>>