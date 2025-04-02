package trees.redblack

import trees.base.Node

const val PLUG = 0 // Doesn't matter that val it acquire

/**
 * NilNode is used for RBTree
 * Its like a plug for null nodes which needed for implementation of internal methods
 */
class NilNode<K : Comparable<K>, V> : RedBlackNode<K, V>(
        key = PLUG as K,
        value = PLUG as V,
        color = Colors.BLACK
)

/**
 * RedBlackNode is used for RBTree.
 * It stores all the necessary data to work correctly with RBTree
 *
 * @param K Universal comparable type for key storage
 * @param V Universal type for storing values
 * @property key The key which the value will be added to the tree
 * @property value The value which will be added to the tree with the key
 * @property left The left child of this node, or null if no left child exists
 * @property right The right child of this node, or null if no right child exists
 * @property color The color of this node in the tree, defaults to red
 * @constructor Creates a new node in the tree with the specified key and value parameters
 */
open class RedBlackNode<K : Comparable<K>, V>(
        override var key: K,
        override var value: V,
        var parent: RedBlackNode<K, V>? = null,
        override var left: RedBlackNode<K, V>? = null,
        override var right: RedBlackNode<K, V>? = null,
        var color: Colors = Colors.RED
) : Node<K, V, RedBlackNode<K, V>>
