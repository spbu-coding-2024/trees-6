package trees.redblack

import trees.base.AbstractTree

/**
 * The RedBlackTree is a self-balancing tree that maintains balance with node color
 *
 * @author Krivonosov Konstantin (https://github.com/fUS1ONd)
 * @param K Universal comparable type for key storage
 * @param V Universal type for storing values
 */
class RedBlackTree<K : Comparable<K>, V : Any> : AbstractTree<K, V, RedBlackNode<K, V>>() {

    /**
     * Adds a new `RedBlackNode<K, V>` to the tree
     * If the key already exists, its value is updated
     *
     * @param key The key with which the value will be added to the tree, must be comparable
     * @param value The value that will be added with the key or will be updated if such a key already exists
     * @return `RedBlackNode<K, V>`
     */
    override fun insert(key: K, value: V): RedBlackNode<K, V> {
        val existingNode = findNodeByKey(key)

        // if node to insert is exist, revalue it and return
        if (existingNode != null) {
            existingNode.value = value
            return existingNode
        }

        var currentNode = getRoot()
        var parentNode: RedBlackNode<K, V>? = null // parent of inserted node

        while (currentNode != null) {
            parentNode = currentNode
            currentNode = if (key < currentNode.key) currentNode.left else currentNode.right
        }

        val newNode = RedBlackNode(key, value)
        when {
            parentNode == null -> setRoot(newNode) // if parent is null, so newNode is a root
            key < parentNode.key -> parentNode.left = newNode
            else -> parentNode.right = newNode
        }
        newNode.parent = parentNode

        balanceAfterInsert(newNode)
        addOneToCountNodes()
        return newNode
    }

    /**
     * Restores Red-Black properties after insertion
     * Handles 3 cases:
     * 1. Red uncle -> recolor parent, grandparent and uncle
     * 2. Black uncle (left-right case) -> left rotate parent, then right rotate grandparent
     * 3. Black uncle (right-left case) -> right rotate parent, then left rotate grandparent
     */
    private fun balanceAfterInsert(newNode: RedBlackNode<K, V>) {
        var parent = newNode.parent

        // Case 1: Parent is null, we've reached the root
        if (parent == null) {
            newNode.color = Colors.BLACK
            return
        }

        // Parent is black -> nothing to do
        if (parent.color == Colors.BLACK) return

        // From here on, parent is red
        val grandparent = parent.parent ?: throw IllegalStateException()
        val uncle = getUncleNode(parent)

        // Case 2: Uncle is red -> recolor parent, grandparent and uncle
        if (uncle != null && uncle.color == Colors.RED) {
            parent.color = Colors.BLACK
            grandparent.color = Colors.RED
            uncle.color = Colors.BLACK

            balanceAfterInsert(grandparent)
        }
        // Parent is left child of grandparent
        else if (parent == grandparent.left) {
            // Case 3: Black uncle and node is left-right "inner child"
            if (newNode == parent.right) {
                rotateLeft(parent)
                parent = newNode
            }
            // Case 4: Black uncle and node is left-left "outer child"
            rotateRight(grandparent)
            parent.color = Colors.BLACK
            grandparent.color = Colors.RED
        }
        // Parent is right child of grandparent
        else {
            // Case 3: Black uncle and node is right-left "inner child"
            if (newNode == parent.left) {
                rotateRight(parent)
                parent = newNode
            }
            // Case 4: Black uncle and node is right-right "outer child"
            rotateLeft(grandparent)
            parent.color = Colors.BLACK
            grandparent.color = Colors.RED
        }
    }

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

    /**
     * Updates parent and child references when tree structural changes occur.
     *
     * @param parent The parent node whose child is being changed. If null, oldChild was the root.
     * @param oldChild The node being replaced (the original child of parent)
     * @param newChild A new node that becomes a child of parent (maybe null)
     *
     * @see rotateLeft
     * @see rotateRight
     * @see removeNodeWithZeroOrOneChild
     */
    private fun updateParentReference(parent: RedBlackNode<K, V>?, oldChild: RedBlackNode<K, V>, newChild: RedBlackNode<K, V>?) {
        when {
            parent == null -> setRoot(newChild)
            parent.left == oldChild -> parent.left = newChild
            parent.right == oldChild -> parent.right = newChild
        }
        newChild?.parent = parent
    }

    private fun findNodeByKey(key: K): RedBlackNode<K, V>? {
        var current = getRoot()
        while (current != null && key != current.key) {
            current = if (key < current.key) current.left else current.right
        }
        return current
    }

    private fun getUncleNode(parent: RedBlackNode<K, V>): RedBlackNode<K, V>? {
        val grandparent = parent.parent ?: return null
        return if (parent == grandparent.left) grandparent.right else grandparent.left
    }

    /**
     * Deletes a node by key
     *
     * @param key The key used to delete the node
     * @return `true` if a node was deleted, `false` if the key was not found
     */
    override fun delete(key: K): Boolean {
        var nodeToDelete = getRoot()

        // Find the node to be deleted
        while (nodeToDelete != null && nodeToDelete.key != key) {
            nodeToDelete = when {
                key < nodeToDelete.key -> nodeToDelete.left
                else -> nodeToDelete.right
            }
        }

        // If we couldn't delete node because it doesn't exist return false
        if (nodeToDelete == null) return false

        // In this variable, we'll store the node at which we're going to start to fix the R-B
        // properties after deleting a node.
        val replacementNode: RedBlackNode<K, V>?
        val deletedNodeColor: Colors

        // nodeToDelete has zero or one child
        if (nodeToDelete.left == null || nodeToDelete.right == null) {
            replacementNode = removeNodeWithZeroOrOneChild(nodeToDelete)
            deletedNodeColor = nodeToDelete.color
        }
        // nodeToDelete has two children
        else {
            // Find minimum node of right subtree
            val successor = findMinimumNode(nodeToDelete.right) ?: throw IllegalStateException()

            // Copy data with keeping color
            nodeToDelete.key = successor.key
            nodeToDelete.value = successor.value

            replacementNode = removeNodeWithZeroOrOneChild(successor)
            deletedNodeColor = successor.color
        }

        if (deletedNodeColor == Colors.BLACK) {
            balanceAfterDelete(replacementNode ?: throw IllegalStateException())

            // Remove nil node
            if (replacementNode is NilNode) {
                updateParentReference(replacementNode.parent, replacementNode, null)
            }
        }
        removeOneFromCountNodes()
        return true
    }

    private fun findMinimumNode(node: RedBlackNode<K, V>?): RedBlackNode<K, V>? {
        var current = node
        while (current?.left != null) {
            current = current.left
        }
        return current
    }

    /**
     * Removes node with 0 or 1 child
     * @return The moved-up node that replaces the deleted node
     */
    private fun removeNodeWithZeroOrOneChild(node: RedBlackNode<K, V>): RedBlackNode<K, V>? {
        return when {
            // Node has ONLY a left child --> replace by its left child
            node.left != null -> {
                updateParentReference(node.parent, node, node.left)
                node.left
            }

            // Analogically
            node.right != null -> {
                updateParentReference(node.parent, node, node.right)
                node.right
            }

            // Node has no children ->
            // - node is red -> just remove it
            // - node is black -> replace it by a NIL node (needs to fix RBT properties)
            else -> {
                val replacement = if (node.color == Colors.BLACK) NilNode<K, V>() else null
                updateParentReference(node.parent, node, replacement)
                replacement
            }
        }
    }

    private fun balanceAfterDelete(node: RedBlackNode<K, V>) {
        // Case 1: Examined node is root, end of recursion
        if (node == getRoot()) {
            node.color = Colors.BLACK
            return
        }

        var sibling = getSiblingNode(node)

        // Case 2: Red sibling
        if (sibling?.color == Colors.RED) {
            handleRedSiblingCase(node, sibling)
            sibling = getSiblingNode(node)
        }

        // Cases 3+4: Black sibling with two black children
        if (isNodeBlack(sibling?.left) && isNodeBlack(sibling?.right)) {
            sibling?.color = Colors.RED

            // Case 3: Black sibling with two black children + red parent
            if (node.parent?.color == Colors.RED) {
                node.parent?.color = Colors.BLACK
            }
            // Case 4: Black sibling with two black children + black parent
            else {
                balanceAfterDelete(node.parent ?: throw IllegalStateException())
            }
        }
        // Case 5+6: Black sibling with at least one red child
        else {
            handleBlackSiblingWithRedChildCase(node, sibling ?: throw IllegalStateException())
        }
    }

    private fun getSiblingNode(node: RedBlackNode<K, V>): RedBlackNode<K, V>? {
        val parent = node.parent
        return if (node == parent?.left) parent.right else parent?.left
    }

    private fun isNodeBlack(node: RedBlackNode<K, V>?): Boolean {
        return node == null || node.color == Colors.BLACK
    }

    private fun handleRedSiblingCase(node: RedBlackNode<K, V>, sibling: RedBlackNode<K, V>) {
        sibling.color = Colors.BLACK
        val parent = node.parent ?: throw IllegalStateException()
        parent.color = Colors.RED

        if (node == parent.left) rotateLeft(parent) else rotateRight(parent)
    }

    private fun handleBlackSiblingWithRedChildCase(node: RedBlackNode<K, V>, siblingCopied: RedBlackNode<K, V>) {
        val isLeftChild = node == node.parent?.left
        var sibling: RedBlackNode<K, V> = siblingCopied // we cant change val of params in function

        // Case 5: Black sibling with at least one red child + "outer nephew" is black
        // -> Recolor sibling and its child, and rotate around sibling
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

        // Case 6: Black sibling with at least one red child + "outer nephew" is red
        // --> Recolor sibling + parent + sibling's child, and rotate around parent
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

    /**
     * Check RedBlackTree properties such as:
     * 1) The root is black
     * 2) Red node has no red child
     * 3) All paths from a node to its Nil children contain the same number of black nodes (black height)
     *
     * @return returns 'true' when all properties are met, else IllegalStateException
     */
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

    private fun isNodeRed(node: RedBlackNode<K, V>?): Boolean {
        return node?.color == Colors.RED
    }
}
