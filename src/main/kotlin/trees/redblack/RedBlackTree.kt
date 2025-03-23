package trees.redblack

import trees.base.AbstractTree

class RedBlackTree<K: Comparable<K>, V> : AbstractTree<K, V, RedBlackNode<K, V>>() {
    private var root: RedBlackNode<K, V>? = null
    private var countNodes: Int = 0

    override fun insert(key: K, value: V): RedBlackNode<K, V> {
        val newNode: RedBlackNode<K, V> = RedBlackNode(key, value)
    }

    private fun balance(node: RedBlackNode<K, V>) {

    }

    private fun rotateLeft(target: RedBlackNode<K, V>) {
        
        var copyOfRightSon: RedBlackNode<K, V>? = target.right
        target.right = copyOfRightSon?.left
        
        // устанавливаем relationships
        if (copyOfRightSon?.left != null) {
            copyOfRightSon.left?.parent = target
        }

        // Отцом копии назначаем деда
        copyOfRightSon?.parent = target.parent

        if (target.parent == null) {
            setRoot(copyOfRightSon)
        } else if (target == target.parent?.left) {
            target.parent?.left = copyOfRightSon
        } else {
            target.parent?.right = copyOfRightSon
        }

        copyOfRightSon?.left = target
        target.parent = copyOfRightSon
    }

    private fun rotateRight(target: RedBlackNode<K, V>) {

        var copyOfLeftSon: RedBlackNode<K, V>? = target.left
        target.left = copyOfLeftSon?.right

        if (copyOfLeftSon?.right != null) {
            copyOfLeftSon.right?.parent = target
        }

        copyOfLeftSon?.parent = target.parent

        if (target.parent == null) {
            setRoot(copyOfLeftSon)
        } else if (target == target.parent?.left) {
            target.parent?.left = copyOfLeftSon
        } else {
            target.parent?.right = copyOfLeftSon
        }

        copyOfLeftSon?.right = target
        target.parent = copyOfLeftSon
    }

    override fun delete(key: K): Boolean {
        TODO("Not yet implemented")
    }

    override fun search(key: K): V? {
        TODO("Not yet implemented")
    }

    override fun height(): Int {
        TODO("Not yet implemented")
    }

    override fun inOrder(): List<RedBlackNode<K, V>> {
        val result = mutableListOf<RedBlackNode<K, V>>()
        inOrderHelper(getRoot(), result)
        return result
    }

    private fun inOrderHelper(node: RedBlackNode<K, V>?, result: MutableList<RedBlackNode<K, V>>) {
        if (node == null) {
            return
        }

        inOrderHelper(node.left, result)
        result.add(node)
        inOrderHelper(node.right, result)
    }
}
