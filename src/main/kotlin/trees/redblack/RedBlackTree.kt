package trees.redblack

import trees.base.AbstractTree

class RedBlackTree<K: Comparable<K>, V> : AbstractTree<K, V, RedBlackNode<K, V>>() {
    private var root: RedBlackNode<K, V>? = null
    private var countNodes: Int = 0

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
            balance(newNode) // balance RBTree
        }
        return newNode
    }

    private fun balance(node: RedBlackNode<K, V>) {
        var current: RedBlackNode<K, V> = node
        if (current == getRoot()) {
            current.color = Colors.BLACK
            return
        }

        while (current.parent?.color == Colors.RED) {
            val parent = current.parent
            val grandParent = parent?.parent

            if (parent == grandParent?.left) {
                val uncle = grandParent?.right

                if (uncle?.color == Colors.RED) {
                    parent.color = Colors.BLACK
                    uncle.color = Colors.BLACK
                    grandParent.color = Colors.RED
                    current = grandParent
                } else {
                    if (current == parent?.right) {
                        current = parent
                        rotateLeft(current)
                    }
                    parent?.color = Colors.BLACK
                    grandParent?.color = Colors.RED
                    rotateRight(grandParent)
                }
            } else {
                val uncle = grandParent?.left

                if (uncle?.color == Colors.RED) {
                    parent.color = Colors.BLACK
                    uncle.color = Colors.BLACK
                    grandParent.color = Colors.RED
                    current = grandParent
                } else {
                    if (current == parent?.left) {
                        current = parent
                        rotateRight(current)
                    }
                    parent?.color = Colors.BLACK
                    grandParent?.color = Colors.RED
                    rotateLeft(grandParent)
                }
            }
        }
    }

    private fun rotateLeft(node: RedBlackNode<K, V>?) {}

    private fun rotateRight(node: RedBlackNode<K, V>?) {}

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