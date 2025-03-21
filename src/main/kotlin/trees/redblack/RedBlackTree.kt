package trees.redblack

import trees.base.AbstractTree

class RedBlackTree<K: Comparable<K>, V> : AbstractTree<K, V, RedBlackNode<K, V>>() {
    private var root: RedBlackNode<K, V>? = null
    private var countNodes: Int = 0

    override fun getRoot(): RedBlackNode<K, V>>? { return root }
    override fun setRoot(newValue: RedBlackNode<K, V>>?): Boolean { root = newValue; return true }

    override fun insert(key: K, value: V): RedBlackNode<K, V> {
        val newNode: RedBlackNode<K, V> = RedBlackNode(key, value)


        if (isEmpty()) {
            setRoot(newNode)
            newNode.color = Colors.BLACK
            addOneToCountNodes()
        } else {
            var parent: RedBlackNode<K, V>? = getRoot()
            var current: RedBlackNode<K, V>? = getRoot()

            while (current != null) {
                parent = current
                current = when {
                    key > current.key -> current.right
                    else -> current.left
                }
            }

            newNode.parent = parent
            
            when {
                parent == null -> setRoot(newNode)
                key > parent.key -> parent.right = newNode
                else -> parent.left = newNode
            }

            addOneToCountNodes()
            fixInsertion(newNode) // balance RBTree
        }
        return newNode
    }

    private fun fixInsertion(node: RedBlackNode<K, V>) {}

    override fun delete(value: K): Boolean {
        TODO("Not yet implemented")
    }
    override fun search(key: K): V? {
        TODO("Not yet implemented")
    }
    override fun height(): Int {
        TODO("Not yet implemented")
    }
}