package trees.redblack

import trees.base.AbstractTree

class RedBlackTree<K : Comparable<K>, V : Any> : AbstractTree<K, V, RedBlackNode<K, V>>() {

    fun printTree() {
        printTreeRecursive(getRoot(), "", true)

        println()
    }

    private fun printTreeRecursive(node: RedBlackNode<K, V>?, indent: String, isRight: Boolean) {
        if (node == null) return

        val newIndent = indent + if (isRight) "    " else "│   "
        printTreeRecursive(node.right, newIndent, true)
        println("$indent${if (isRight) "└── " else "├── "}${node.key} (${node.color})")
        printTreeRecursive(node.left, newIndent, false)
    }

    fun checkRedBlackProperties(): Boolean {
        val root = getRoot() ?: return true

        if (root.color != Colors.BLACK) {
            throw IllegalStateException("Корень должен быть черным")
        }

        return checkNodeProperties(root)
    }

    private fun checkNodeProperties(node: RedBlackNode<K, V>?): Boolean {
        if (node == null) return true

        if (isRed(node)) {
            if (isRed(getLeftChild(node)) || isRed(getRightChild(node))) {
                throw IllegalStateException("У красного узла не может быть красных потомков")
            }
        }

        val leftBlackHeight = getBlackHeight(getLeftChild(node))
        val rightBlackHeight = getBlackHeight(getRightChild(node))
        if (leftBlackHeight != rightBlackHeight) {
            throw IllegalStateException("Черная высота должна быть одинаковой для всех путей")
        }

        return checkNodeProperties(getLeftChild(node)) && checkNodeProperties(getRightChild(node))
    }

    private fun getBlackHeight(node: RedBlackNode<K, V>?): Int {
        if (node == null) return 1
        val leftBlackHeight = getBlackHeight(node.left)
        if (node.color == Colors.BLACK) {
            return leftBlackHeight + 1
        }
        return leftBlackHeight
    }

    override fun insert(key: K, value: V): RedBlackNode<K, V> {
        val existingNode = searchNode(key)

        if (existingNode != null) {
            existingNode.value = value
            return existingNode
        }

        val newNode: RedBlackNode<K, V> = RedBlackNode(key, value)

        setRelationsOrSetRoot(newNode)

        fixInsertion(newNode)

        addOneToCountNodes()

        return newNode
    }

    private fun setRelationsOrSetRoot(node: RedBlackNode<K, V>) {
        val parentNode = findParentOfInsertedNode(node)

        node.parent = parentNode

        if (parentNode == null) {
            setRoot(node)
        } else if (node.key < parentNode.key) {
            parentNode.left = node
        } else {
            parentNode.right = node
        }
    }

    private fun findParentOfInsertedNode(node: RedBlackNode<K, V>): RedBlackNode<K, V>? {
        var parentNode: RedBlackNode<K, V>? = null
        var placeOfNewNode: RedBlackNode<K, V>? = getRoot() // куда бы встала нода

        while (placeOfNewNode != null) {
            parentNode = placeOfNewNode

            if (node.key < placeOfNewNode.key) {
                placeOfNewNode = getLeftChild(placeOfNewNode)
            } else {
                placeOfNewNode = getRightChild(placeOfNewNode)
            }
        }

        return parentNode
    }

    private fun fixInsertion(originNode: RedBlackNode<K, V>) {
        var node: RedBlackNode<K, V>? = originNode

        while (parentExistsAndItsRed(node)) {
            if (parentIsLeftChild(node)) {
                val uncle = getUncleWhenParentIsLeftChild(node)
                if (isRed(uncle)) {
                    setParentAndUncleToBlackAndGrandParentToRed(node)
                    node = getGrandParentNodeOrNull(node)
                } else {
                    if (nodeIsRightChild(node)) {
                        node = getParentNodeOrNull(node)
                        rotateLeft(node)
                    }
                    setParentToBlackAndGrandparentToRed(node)
                    rotateRight(getGrandParentNodeOrNull(node))
                }
            } else {
                val uncle = getUncleWhenParentIsRightChild(node)
                if (isRed(uncle)) {
                    setParentAndUncleToBlackAndGrandParentToRed(node)
                    node = getGrandParentNodeOrNull(node)
                } else {
                    if (nodeIsLeftChild(node)) {
                        node = getParentNodeOrNull(node)
                        rotateRight(node)
                    }
                    setParentToBlackAndGrandparentToRed(node)
                    rotateLeft(getGrandParentNodeOrNull(node))
                }
            }

            if (isRoot(node)) break
        }

        setRootColorToBlack()
    }

    private fun fixDeletion(affectedNode: RedBlackNode<K, V>?) {
        var currentNode = affectedNode
        while (currentNode != getRoot() && currentNode?.color == Colors.BLACK) {
            if (nodeIsLeftChild(currentNode)) {
                var sibling = currentNode.parent?.right
                if (isRed(sibling)) {
                    setBlack(sibling)
                    setRed(currentNode.parent)
                    rotateLeft(currentNode.parent)
                    sibling = currentNode.parent?.right
                }

                if (childrenAreBlack(sibling)) {
                    setRed(sibling)
                    currentNode = getParentNodeOrNull(currentNode)
                } else {
                    if (isBlack(sibling?.right)) {
                        setBlack(sibling?.left)
                        setRed(sibling)
                        rotateRight(sibling)
                        sibling = currentNode.parent?.right
                    }

                    sibling?.color = currentNode.parent?.color ?: Colors.BLACK
                    setBlack(currentNode.parent)
                    setBlack(sibling?.right)
                    rotateLeft(currentNode.parent)
                    currentNode = getRoot()
                }
            } else {
                var sibling = currentNode.parent?.left
                if (isRed(sibling)) {
                    setBlack(sibling)
                    setRed(currentNode.parent)
                    rotateRight(currentNode.parent)
                    sibling = currentNode.parent?.left
                }

                if (childrenAreBlack(sibling)) {
                    setRed(sibling)
                    currentNode = currentNode.parent
                } else {
                    if (isBlack(sibling?.left)) {
                        setBlack(sibling?.right)
                        setRed(sibling)
                        rotateLeft(sibling)
                        sibling = currentNode.parent?.left
                    }

                    sibling?.color = currentNode.parent?.color ?: Colors.BLACK
                    setBlack(currentNode.parent)
                    setBlack(sibling?.left)
                    rotateRight(currentNode.parent)
                    currentNode = getRoot()
                }
            }
        }
        setBlack(currentNode)
    }

    private fun deleteNode(root: RedBlackNode<K, V>?, key: K): Boolean {
        var currentNode = root
        var targetNode: RedBlackNode<K, V>? = null
        while (currentNode != null) {
            currentNode = if (key < currentNode.key) {
                currentNode.left
            } else {
                if (key == currentNode.key) targetNode = currentNode
                getRightChild(currentNode)
            }
        }

        if (targetNode == null) {
            return false
        }

        var replacementNode = targetNode
        var originalColor = replacementNode.color
        val affectedNode: RedBlackNode<K, V>?

        when {
            targetNode.left == null -> {
                affectedNode = targetNode.right
                transplant(targetNode, targetNode.right)
            }

            targetNode.right == null -> {
                affectedNode = targetNode.left
                transplant(targetNode, targetNode.left)
            }

            else -> {
                replacementNode = findLeftmostChild(targetNode.right)
                if (replacementNode?.color != null) {
                    originalColor = replacementNode.color
                }

                affectedNode = replacementNode?.right
                if (replacementNode?.parent == targetNode) {
                    affectedNode?.parent = replacementNode
                } else {
                    transplant(replacementNode, replacementNode?.right)
                    replacementNode?.right = targetNode.right
                    replacementNode?.right?.parent = replacementNode
                }
                transplant(targetNode, replacementNode)
                replacementNode?.left = targetNode.left
                replacementNode?.left?.parent = replacementNode
                replacementNode?.color = targetNode.color
            }
        }

        if (originalColor == Colors.BLACK) {
            fixDeletion(affectedNode)
        }

        removeOneFromCountNodes()
        return true
    }

    override fun delete(key: K): Boolean {
        return deleteNode(getRoot(), key)
    }

    private fun getUncleWhenParentIsLeftChild(node: RedBlackNode<K, V>?): RedBlackNode<K, V>? {
        val grandParent = getGrandParentNodeOrNull(node)
        return getRightChild(grandParent)
    }

    private fun getUncleWhenParentIsRightChild(node: RedBlackNode<K, V>?): RedBlackNode<K, V>? {
        val grandParent = getGrandParentNodeOrNull(node)
        return getLeftChild(grandParent)
    }

    private fun getGrandParentNodeOrNull(node: RedBlackNode<K, V>?): RedBlackNode<K, V>? {
        val grandparent: RedBlackNode<K, V>? = node?.parent?.parent

        if (grandparent != null) {
            return grandparent
        }
        return null
    }

    private fun parentExistsAndItsRed(node: RedBlackNode<K, V>?): Boolean {
        val parent: RedBlackNode<K, V>? = getParentNodeOrNull(node)
        return (parent != null && isRed(parent))
    }

    private fun parentIsLeftChild(node: RedBlackNode<K, V>?): Boolean {
        val parent: RedBlackNode<K, V>? = getParentNodeOrNull(node)

        return (nodeIsLeftChild(parent))
    }

    private fun setParentAndUncleToBlackAndGrandParentToRed(node: RedBlackNode<K, V>?) {
        // Кейс: отец - левый ребенок в семье
        val grandparent: RedBlackNode<K, V>? = getGrandParentNodeOrNull(node)

        if (grandparent != null) {
            setBlack(getLeftChild(grandparent))
            setBlack(getRightChild(grandparent))
            setRed(grandparent)
        }
    }

    private fun setParentToBlackAndGrandparentToRed(node: RedBlackNode<K, V>?) {
        // Кейс: отец - левый ребенок в семье
        val grandparent: RedBlackNode<K, V>? = getGrandParentNodeOrNull(node)
        val parent: RedBlackNode<K, V>? = getParentNodeOrNull(node)

        setBlack(parent)
        setRed(grandparent)

    }

    private fun rotateLeft(target: RedBlackNode<K, V>?) {
        val copyOfRightSon: RedBlackNode<K, V>? = getRightChild(target)
        target?.right = getLeftChild(copyOfRightSon)

        // устанавливаем relationships
        if (leftChildIsNotNull(copyOfRightSon)) {
            copyOfRightSon?.left?.parent = target
        }

        // Отцом копии назначаем деда
        copyOfRightSon?.parent = getParentNodeOrNull(target)

        if (getParentNodeOrNull(target) == null) {
            setRoot(copyOfRightSon)
        } else if (nodeIsLeftChild(target)) {
            target?.parent?.left = copyOfRightSon
        } else {
            target?.parent?.right = copyOfRightSon
        }

        copyOfRightSon?.left = target
        target?.parent = copyOfRightSon
    }

    private fun rotateRight(target: RedBlackNode<K, V>?) {
        val copyOfLeftSon: RedBlackNode<K, V>? = getLeftChild(target)
        target?.left = getRightChild(copyOfLeftSon)

        if (rightChildIsNotNull(copyOfLeftSon)) {
            copyOfLeftSon?.right?.parent = target
        }

        copyOfLeftSon?.parent = getParentNodeOrNull(target)

        if (getParentNodeOrNull(target) == null) {
            setRoot(copyOfLeftSon)
        } else if (nodeIsRightChild(target)) {
            target?.parent?.right = copyOfLeftSon
        } else {
            target?.parent?.left = copyOfLeftSon
        }

        copyOfLeftSon?.right = target
        target?.parent = copyOfLeftSon
    }

    private fun searchNode(key: K): RedBlackNode<K, V>? {
        var wanted = getRoot()
        while (wanted != null && key != wanted.key) {
            if (key < wanted.key) {
                wanted = getLeftChild(wanted)
            } else {
                wanted = getRightChild(wanted)
            }
        }
        return wanted
    }

    private fun leftChildIsNotNull(node: RedBlackNode<K, V>?): Boolean {
        return (getLeftChild(node) != null)
    }

    private fun rightChildIsNotNull(node: RedBlackNode<K, V>?): Boolean {
        return (getRightChild(node) != null)
    }

    private fun getRightChild(node: RedBlackNode<K, V>?): RedBlackNode<K, V>? {
        return node?.right
    }

    private fun getLeftChild(node: RedBlackNode<K, V>?): RedBlackNode<K, V>? {
        return node?.left
    }

    /**
     * Заменяет поддерево с корнем в oldNode на поддерево с корнем в newNode.
     * @param oldNode нода, которую нужно заменить
     * @param newNode нода, которую будет на его месте
     */
    private fun transplant(oldNode: RedBlackNode<K, V>?, newNode: RedBlackNode<K, V>?) {
        if (isRoot(oldNode)) {
            setRoot(newNode)
        } else if (nodeIsLeftChild(oldNode)) {
            oldNode?.parent?.left = newNode
        } else {
            oldNode?.parent?.right = newNode
        }
        if (newNode != null) {
            newNode.parent = getParentNodeOrNull(oldNode)
        }
    }

    private fun rightChildIsBlack(node: RedBlackNode<K, V>?): Boolean {
        val rightChild = getRightChild(node)
        return isBlack(rightChild)
    }

    private fun leftChildIsBlack(node: RedBlackNode<K, V>?): Boolean {
        val leftChild = getLeftChild(node)
        return isBlack(leftChild)
    }

    private fun childrenAreBlack(node: RedBlackNode<K, V>?): Boolean {
        val leftChild = getLeftChild(node)
        val rightChild = getRightChild(node)
        return (isBlack(leftChild) && isBlack(rightChild))
    }

    private fun getColour(node: RedBlackNode<K, V>?): Colors? {
        if (node != null) {
            return node.color
        }
        return null
    }

    fun findLeftmostChild(node: RedBlackNode<K, V>?): RedBlackNode<K, V>? {
        var currentNode = node
        while (currentNode?.left != null) {
            currentNode = currentNode.left
        }
        return currentNode
    }

    private fun isBlack(node: RedBlackNode<K, V>?): Boolean {
        return node == null || node.color == Colors.BLACK
    }

    private fun isRed(node: RedBlackNode<K, V>?): Boolean {
        return node?.color == Colors.RED
    }

    private fun nodeIsRightChild(node: RedBlackNode<K, V>?): Boolean {
        return (node == node?.parent?.right)
    }

    private fun nodeIsLeftChild(node: RedBlackNode<K, V>?): Boolean {
        return (node == node?.parent?.left)
    }

    private fun getParentNodeOrNull(node: RedBlackNode<K, V>?): RedBlackNode<K, V>? {
        val parent: RedBlackNode<K, V>? = node?.parent

        return parent
    }

    private fun leftChildIsNull(node: RedBlackNode<K, V>): Boolean {
        return (getLeftChild(node) == null)
    }

    private fun rightChildIsNull(node: RedBlackNode<K, V>): Boolean {
        return (getRightChild(node) == null)
    }

    private fun setRootColorToBlack() {
        val fixRoot: RedBlackNode<K, V>? = getRoot()
        fixRoot?.color = Colors.BLACK
        setRoot(fixRoot)
    }

    private fun setRed(node: RedBlackNode<K, V>?) {
        node?.color = Colors.RED
    }

    private fun setBlack(node: RedBlackNode<K, V>?) {
        node?.color = Colors.BLACK
    }

    private fun isRoot(node: RedBlackNode<K, V>?): Boolean {
        return (node == getRoot())
    }
}
