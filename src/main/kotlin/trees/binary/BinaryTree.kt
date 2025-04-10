package trees.binary

import trees.base.AbstractTree
import trees.base.Node

class BinaryTree<K : Comparable<K>, V> : AbstractTree<K, V, BinaryNode<K, V>>() {

    override fun insert(key: K, value: V): BinaryNode<K, V> {
        val newNode = BinaryNode(key, value)
        setRoot(insertRecursive(getRoot(), newNode))
        return newNode
    }

    private fun insertRecursive(currentNode: BinaryNode<K, V>?, nodeToInsert: BinaryNode<K, V>): BinaryNode<K, V> {
        return when {
            currentNode == null -> {
                addOneToCountNodes()
                nodeToInsert
            }
            nodeToInsert.key < currentNode.key -> {
                currentNode.left = insertRecursive(currentNode.left, nodeToInsert)
                currentNode
            }
            nodeToInsert.key > currentNode.key -> {
                currentNode.right = insertRecursive(currentNode.right, nodeToInsert)
                currentNode
            }
            else -> {
                currentNode.value = nodeToInsert.value
                currentNode
            }
        }
    }

    override fun delete(key: K): Boolean {
        val initialSize = size()
        setRoot(deleteNode(getRoot(), key))
        return size() < initialSize
    }

    private fun deleteNode(node: BinaryNode<K, V>?, key: K): BinaryNode<K, V>? {
        if (node == null) return null

        when {
            key < node.key -> {
                node.left = deleteNode(node.left, key)
                return node
            }
            key > node.key -> {
                node.right = deleteNode(node.right, key)
                return node
            }
            else -> {
                return when {
                    node.left == null -> {
                        removeOneFromCountNodes()
                        node.right
                    }
                    node.right == null -> {
                        removeOneFromCountNodes()
                        node.left
                    }
                    else -> {
                        val minRight = findMinNode(node.right ?: return node.left)
                        node.key = minRight.key
                        node.value = minRight.value
                        node.right = deleteNode(node.right, minRight.key)
                        node
                    }
                }
            }
        }
    }

    fun findMinNode(node: BinaryNode<K, V>): BinaryNode<K, V> {
        var current = node
        while (current.left != null) {
            current = current.left ?: break
        }
        return current
    }

    fun findMaxNode(node: BinaryNode<K, V>): BinaryNode<K, V> {
        var current = node
        while (current.right != null) {
            current = current.right ?: break
        }
        return current
    }

    fun height(): Int = height(getRoot())

    private fun height(node: BinaryNode<K, V>?): Int {
        return if (node == null) 0
        else maxOf(height(node.left), height(node.right)) + 1
    }

    fun getBalance(): BinaryTree<K, V> {
        val nodes = inOrder()
        setRoot(buildBalancedTree(nodes))
        return this
    }

    private fun buildBalancedTree(nodes: List<Node<K, V, BinaryNode<K, V>>>): BinaryNode<K, V>? {
        return buildBalancedTreeRecursive(nodes, 0, nodes.lastIndex)
    }

    private fun buildBalancedTreeRecursive(
        nodes: List<Node<K, V, BinaryNode<K, V>>>,
        start: Int,
        end: Int
    ): BinaryNode<K, V>? {
        if (start > end) return null

        val mid = (start + end) / 2
        val node = nodes[mid] as? BinaryNode<K, V> ?: return null

        node.left = buildBalancedTreeRecursive(nodes, start, mid - 1)
        node.right = buildBalancedTreeRecursive(nodes, mid + 1, end)

        return node
    }
}