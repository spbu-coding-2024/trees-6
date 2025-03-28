package trees.avl

import trees.base.Node

/**
 * AVLNode is used for AVLTree.
 * It stores all the necessary data to work correctly with AVLTree
 *
 * @param K Universal comparable type for key storage
 * @param V Universal type for storing values
 * @property key The key which the value will be added to the tree
 * @property value The value which will be added to the tree with the key
 * @property left The left child of this node, or null if no left child exists
 * @property right The right child of this node, or null if no right child exists
 * @property height The height of this node in the tree, defaults to 1
 * @constructor Creates a new node in the tree with the specified key and value parameters
 */
class AVLNode<K : Comparable<K>, V>(
    override var key: K,
    override var value: V,
    override var left: AVLNode<K, V>? = null,
    override var right: AVLNode<K, V>? = null,
    var height: Int = 1
) : Node<K, V, AVLNode<K, V>>