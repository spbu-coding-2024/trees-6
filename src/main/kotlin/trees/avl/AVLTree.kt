package trees.avl

import trees.base.AbstractTree

/**
 * The AVL Tree is a self-balancing tree that maintains height balance
 *
 * @author Maksim Varlyga (https://github.com/vvmaksim)
 * @param K Universal comparable type for key storage
 * @param V Universal type for storing values
 */
class AVLTree<K : Comparable<K>, V : Any> : AbstractTree<K, V, AVLNode<K, V>>() {
    /**
     * Adds a new `AVLNode<K, V>` to the tree
     * If the key already exists, its value is updated
     *
     * @param key The key with which the value will be added to the tree, must be comparable
     * @param value The value that will be added with the key or will be updated if such a key already exists
     * @return `AVLNode<K, V>`
     * @sample samples.avl.sampleInsert
     */
    override fun insert(
        key: K,
        value: V,
    ): AVLNode<K, V> {
        val newRoot = insertRecursively(getRoot(), key, value)
        setRoot(newRoot)
        return newRoot
    }

    /**
     * Deletes a node by key
     *
     * @param key The key used to delete the node
     * @return `true` if a node was deleted, `false` if the key was not found
     * @sample samples.avl.sampleDelete
     */
    override fun delete(key: K): Boolean {
        val startSize = getCountNodes()
        val newRoot = deleteRecursively(getRoot(), key)
        setRoot(newRoot)
        return startSize != getCountNodes()
    }

    /**
     * Returns the height of the tree, or 0 if the tree is empty
     *
     * @return The height of the tree, or 0 if the tree is empty
     * @sample samples.avl.sampleHeight
     */
    fun height(): Int = getRoot()?.height ?: 0

    private fun insertRecursively(
        node: AVLNode<K, V>?,
        key: K,
        value: V,
    ): AVLNode<K, V> {
        if (node == null) {
            val newNode = AVLNode(key, value)
            addOneToCountNodes()
            return newNode
        }
        if (key < node.key) {
            node.left = insertRecursively(node.left, key, value)
        } else if (key > node.key) {
            node.right = insertRecursively(node.right, key, value)
        } else {
            node.value = value
            return node
        }

        addValueToMaxHeight(node, 1)
        return balance(node)
    }

    private fun deleteRecursively(
        node: AVLNode<K, V>?,
        key: K,
    ): AVLNode<K, V>? {
        if (node == null) return null
        if (key < node.key) {
            node.left = deleteRecursively(node.left, key)
        } else if (key > node.key) {
            node.right = deleteRecursively(node.right, key)
        } else {
            if (node.left == null || node.right == null) {
                val temp = node.left ?: node.right
                removeOneFromCountNodes()
                return temp
            } else {
                val rightNode = node.right ?: throw IllegalStateException("Right node cannot be null")
                val temp = minNode(rightNode)
                node.key = temp.key
                node.value = temp.value
                node.right = deleteRecursively(node.right, temp.key)
            }
        }
        addValueToMaxHeight(node, 1)
        return balance(node)
    }

    private fun addValueToMaxHeight(
        node: AVLNode<K, V>,
        value: Int,
    ): Boolean {
        node.height = value + maxOf(node.left?.height ?: 0, node.right?.height ?: 0)
        return true
    }

    private fun getBalanceFactor(node: AVLNode<K, V>): Int = (node.right?.height ?: 0) - (node.left?.height ?: 0)

    private fun balance(node: AVLNode<K, V>): AVLNode<K, V> {
        val balance = getBalanceFactor(node)

        if (balance < -1) {
            val leftNode = node.left ?: throw IllegalStateException("Left node cannot be null")
            if (getBalanceFactor(leftNode) <= 0) {
                return rightTurn(node)
            } else {
                node.left = leftTurn(leftNode)
                return rightTurn(node)
            }
        }
        if (balance > 1) {
            val rightNode = node.right ?: throw IllegalStateException("Right node cannot be null")
            if (getBalanceFactor(rightNode) >= 0) {
                return leftTurn(node)
            } else {
                node.right = rightTurn(rightNode)
                return leftTurn(node)
            }
        }
        return node
    }

    private fun minNode(node: AVLNode<K, V>): AVLNode<K, V> {
        var current = node
        while (current.left != null) {
            current = current.left ?: throw BrakeException()
        }
        return current
    }

    private fun rightTurn(node: AVLNode<K, V>): AVLNode<K, V> {
        val leftNode = node.left ?: throw IllegalStateException("Left node cannot be null")
        val rightNodeOfLeftNode = leftNode.right
        leftNode.right = node
        node.left = rightNodeOfLeftNode
        addValueToMaxHeight(node, 1)
        addValueToMaxHeight(leftNode, 1)
        return leftNode
    }

    private fun leftTurn(node: AVLNode<K, V>): AVLNode<K, V> {
        val rightNode = node.right ?: throw IllegalStateException("Right node cannot be null")
        val leftNodeOfRightNode = rightNode.left
        rightNode.left = node
        node.right = leftNodeOfRightNode
        addValueToMaxHeight(node, 1)
        addValueToMaxHeight(rightNode, 1)
        return rightNode
    }
}
