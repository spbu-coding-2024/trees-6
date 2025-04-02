package trees.redblack

import trees.base.AbstractTree

class RedBlackTree<K : Comparable<K>, V : Any> : AbstractTree<K, V, RedBlackNode<K, V>>() {

    private fun rotateRight(pivot: RedBlackNode<K, V>) {
        val parent = pivot.parent
        val leftChild = pivot.left ?: return

        pivot.left = leftChild.right
        leftChild.right?.parent = pivot

        leftChild.right = pivot
        pivot.parent = leftChild

        updateParentReference(parent, pivot, leftChild)
    }

    private fun rotateLeft(pivot: RedBlackNode<K, V>) {
        val parent = pivot.parent
        val rightChild = pivot.right ?: return

        pivot.right = rightChild.left
        rightChild.left?.parent = pivot

        rightChild.left = pivot
        pivot.parent = rightChild

        updateParentReference(parent, pivot, rightChild)
    }

    private fun updateParentReference(parent: RedBlackNode<K, V>?, oldChild: RedBlackNode<K, V>, newChild: RedBlackNode<K, V>?) {
        when {
            parent == null -> setRoot(newChild)
            parent.left == oldChild -> parent.left = newChild
            parent.right == oldChild -> parent.right = newChild
        }
        newChild?.parent = parent
    }

    private fun isNodeBlack(node: RedBlackNode<K, V>?): Boolean {
        return node == null || node.color == Colors.BLACK
    }

    override fun insert(key: K, value: V): RedBlackNode<K, V> {
        val existingNode = findNodeByKey(key)

        if (existingNode != null) {
            existingNode.value = value
            return existingNode
        }

        var currentNode = getRoot()
        var parentNode: RedBlackNode<K, V>? = null

        while (currentNode != null) {
            parentNode = currentNode
            currentNode = if (key < currentNode.key) currentNode.left else currentNode.right
        }

        val newNode = RedBlackNode(key, value)
        when {
            parentNode == null -> setRoot(newNode)
            key < parentNode.key -> parentNode.left = newNode
            else -> parentNode.right = newNode
        }
        newNode.parent = parentNode

        balanceAfterInsert(newNode)
        addOneToCountNodes()
        return newNode
    }

    private fun balanceAfterInsert(newNode: RedBlackNode<K, V>) {
        var parent = newNode.parent

        if (parent == null) {
            newNode.color = Colors.BLACK
            return
        }

        if (parent.color == Colors.BLACK) return

        val grandparent = parent.parent ?: throw IllegalStateException()
        val uncle = getUncleNode(parent)

        if (uncle != null && uncle.color == Colors.RED) {
            parent.color = Colors.BLACK
            grandparent.color = Colors.RED
            uncle.color = Colors.BLACK

            balanceAfterInsert(grandparent)
        } else if (parent == grandparent.left) {
            if (newNode == parent.right) {
                rotateLeft(parent)
                parent = newNode
            }
            rotateRight(grandparent)
            parent.color = Colors.BLACK
            grandparent.color = Colors.RED
        } else {
            if (newNode == parent.left) {
                rotateRight(parent)
                parent = newNode
            }
            rotateLeft(grandparent)
            parent.color = Colors.BLACK
            grandparent.color = Colors.RED
        }
    }

    private fun getUncleNode(parent: RedBlackNode<K, V>): RedBlackNode<K, V>? {
        val grandparent = parent.parent ?: return null
        return if (parent == grandparent.left) grandparent.right else grandparent.left
    }

    override fun delete(key: K): Boolean {
        var nodeToDelete = getRoot()

        while (nodeToDelete != null && nodeToDelete.key != key) {
            nodeToDelete = if (key < nodeToDelete.key) nodeToDelete.left else nodeToDelete.right
        }

        if (nodeToDelete == null) return false

        val replacementNode: RedBlackNode<K, V>?
        val deletedNodeColor: Colors

        if (nodeToDelete.left == null || nodeToDelete.right == null) {
            replacementNode = removeNodeWithSingleChild(nodeToDelete)
            deletedNodeColor = nodeToDelete.color
        } else {
            val successor = findMinimumNode(nodeToDelete.right) ?: throw IllegalStateException()
            nodeToDelete.key = successor.key
            nodeToDelete.value = successor.value
            replacementNode = removeNodeWithSingleChild(successor)
            deletedNodeColor = successor.color
        }

        if (deletedNodeColor == Colors.BLACK) {
            balanceAfterDelete(replacementNode ?: throw IllegalStateException())

            if (replacementNode is NilNode) {
                updateParentReference(replacementNode.parent, replacementNode, null)
            }
        }
        removeOneFromCountNodes()
        return true
    }

    private fun removeNodeWithSingleChild(node: RedBlackNode<K, V>): RedBlackNode<K, V>? {
        return when {
            node.left != null -> {
                updateParentReference(node.parent, node, node.left)
                node.left
            }

            node.right != null -> {
                updateParentReference(node.parent, node, node.right)
                node.right
            }

            else -> {
                val replacement = if (node.color == Colors.BLACK) NilNode<K, V>() else null
                updateParentReference(node.parent, node, replacement)
                replacement
            }
        }
    }

    private fun balanceAfterDelete(node: RedBlackNode<K, V>) {
        if (node == getRoot()) {
            node.color = Colors.BLACK
            return
        }

        var sibling = getSiblingNode(node)

        if (sibling?.color == Colors.RED) {
            handleRedSiblingCase(node, sibling)
            sibling = getSiblingNode(node)
        }

        if (isNodeBlack(sibling?.left) && isNodeBlack(sibling?.right)) {
            sibling?.color = Colors.RED

            if (node.parent?.color == Colors.RED) {
                node.parent?.color = Colors.BLACK
            } else {
                balanceAfterDelete(node.parent ?: throw IllegalStateException())
            }
        } else {
            handleBlackSiblingWithRedChildCase(node, sibling ?: throw IllegalStateException())
        }
    }

    private fun getSiblingNode(node: RedBlackNode<K, V>): RedBlackNode<K, V>? {
        val parent = node.parent
        return if (node == parent?.left) parent.right else parent?.left
    }

    private fun handleRedSiblingCase(node: RedBlackNode<K, V>, sibling: RedBlackNode<K, V>) {
        sibling.color = Colors.BLACK
        val parent = node.parent ?: throw IllegalStateException()
        parent.color = Colors.RED

        if (node == parent.left) rotateLeft(parent) else rotateRight(parent)
    }

    private fun handleBlackSiblingWithRedChildCase(node: RedBlackNode<K, V>, siblingCopied: RedBlackNode<K, V>) {
        val isLeftChild = node == node.parent?.left
        var sibling: RedBlackNode<K, V> = siblingCopied
        if (isLeftChild && isNodeBlack(sibling.right)) {
            sibling.left?.color = Colors.BLACK
            sibling.color = Colors.RED
            rotateRight(sibling)
            sibling = node.parent?.right ?: throw IllegalStateException()
        } else if (!isLeftChild && isNodeBlack(sibling.left)) {
            sibling.right?.color = Colors.BLACK
            sibling.color = Colors.RED
            rotateLeft(sibling)
            sibling = node.parent?.left ?: throw IllegalStateException()
        }

        sibling.color = node.parent?.color ?: Colors.BLACK
        node.parent?.color = Colors.BLACK

        if (isLeftChild) {
            sibling.right?.color = Colors.BLACK
            rotateLeft(node.parent ?: throw IllegalStateException())
        } else {
            sibling.left?.color = Colors.BLACK
            rotateRight(node.parent ?: throw IllegalStateException())
        }
    }

    private fun findMinimumNode(node: RedBlackNode<K, V>?): RedBlackNode<K, V>? {
        var current = node
        while (current?.left != null) {
            current = current.left
        }
        return current
    }

    fun checkRedBlackProperties(): Boolean {
        val root = getRoot() ?: return true

        if (root.color != Colors.BLACK) {
            throw IllegalStateException("Root must be black")
        }

        return checkNodeProperties(root)
    }

    private fun checkNodeProperties(node: RedBlackNode<K, V>?): Boolean {
        if (node == null) return true

        if (isNodeRed(node)) {
            if (isNodeRed(node.left) || isNodeRed(node.right)) {
                throw IllegalStateException("Red node cannot have red children")
            }
        }

        val leftBlackHeight = calculateBlackHeight(node.left)
        val rightBlackHeight = calculateBlackHeight(node.right)
        if (leftBlackHeight != rightBlackHeight) {
            throw IllegalStateException("Black height must be equal for all paths")
        }

        return checkNodeProperties(node.left) && checkNodeProperties(node.right)
    }

    private fun calculateBlackHeight(node: RedBlackNode<K, V>?): Int {
        if (node == null) return 1
        val height = calculateBlackHeight(node.left)
        return if (node.color == Colors.BLACK) height + 1 else height
    }

    private fun findNodeByKey(key: K): RedBlackNode<K, V>? {
        var current = getRoot()
        while (current != null && key != current.key) {
            current = if (key < current.key) current.left else current.right
        }
        return current
    }

    private fun isNodeRed(node: RedBlackNode<K, V>?): Boolean {
        return node?.color == Colors.RED
    }
}