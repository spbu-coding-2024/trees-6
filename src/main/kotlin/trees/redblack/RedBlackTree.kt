package trees.redblack

import trees.base.AbstractTree

class RedBlackTree<K: Comparable<K>, V> : AbstractTree<K, V, RedBlackNode<K, V>>() {
    private var root: RedBlackNode<K, V>? = null
    private var countNodes: Int = 0

    override fun insert(key: K, value: V): RedBlackNode<K, V> {
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
        // Тк в котлине в фукнцию передаются параметры, доступные
        // только для чтения, вынужден создать копию передаваемой ноды
        var node: RedBlackNode<K, V>? = originNode
        val grandparent: RedBlackNode<K, V>? = getGrandparentNodeOrNull(node)

        while (parentExistsAndItsRed(node)) {
            if (parentIsLeftChild(node)) {

                if (uncleIsRed(node)) {
                    recolorParentAndUncleToBlackAndGrandparentToRed(node)
                    node = getGrandparentNodeOrNull(node)

                } else {
                    if (nodeIsRightChild(node)) {
                        node = getParentNodeOrNull(node)
                        rotateLeft(node)
                    }
                    setParentToBlackAndGrandparentToRed(node)
                    rotateRight(grandparent)
                }

            } else {
                if (uncleIsRed(node)) {
                    recolorParentAndUncleToBlackAndGrandparentToRed(node)
                    node = getGrandparentNodeOrNull(node)

                } else {
                    if (nodeIsLeftChild(node)) {
                        node = getParentNodeOrNull(node)
                        rotateRight(node)
                    }
                    setParentToBlackAndGrandparentToRed(node)
                    rotateLeft(grandparent)
                }
            }

            if (isRoot(node)) {
                break
            }
        }

        setRootColorToBlack()
    }

    private fun getGrandparentNodeOrNull(node: RedBlackNode<K, V>?): RedBlackNode<K, V>? {
        val grandparent: RedBlackNode<K, V>? = node?.parent?.parent

        if (grandparent != null) {
            return grandparent
        }
        return null
    }

    private fun parentExistsAndItsRed(node: RedBlackNode<K, V>?): Boolean {
        val parent: RedBlackNode<K, V>? = getParentNodeOrNull(node)
        return (isRed(parent))
    }

    private fun parentIsLeftChild(node: RedBlackNode<K, V>?): Boolean {
        val parent = getParentNodeOrNull(node)

        return (nodeIsLeftChild(parent))
    }

    private fun uncleIsRed(node: RedBlackNode<K, V>?): Boolean {
        val uncleOfNode: RedBlackNode<K, V>? = node?.parent?.parent?.right
        return isRed(uncleOfNode)
    }

    private fun recolorParentAndUncleToBlackAndGrandparentToRed(node: RedBlackNode<K, V>?) {
        // Кейс: отец - левый ребенок в семье
        val grandparent: RedBlackNode<K, V>? = getGrandparentNodeOrNull(node)

        if (grandparent != null) {
            setBlack(getLeftChild(grandparent))
            setBlack(getRightChild(grandparent))
            setRed(grandparent)
        }
    }

    private fun setParentToBlackAndGrandparentToRed(node: RedBlackNode<K, V>?) {
        // Кейс: отец - левый ребенок в семье
        val grandparent: RedBlackNode<K, V>? = getGrandparentNodeOrNull(node)
        val parent: RedBlackNode<K, V>? = getParentNodeOrNull(node)

        setRed(grandparent)
        setBlack(parent)
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


    override fun delete(key: K): Boolean {

        val nodeToDelete: RedBlackNode<K, V>? = searchNode(key)

        if (nodeToDelete == null) {
            return false
        }

        val replacementNode: RedBlackNode<K, V>?  // Нода, которая заменит удаляемую
        var successorNode: RedBlackNode<K, V>? = nodeToDelete  // Преемник
        var originalSuccessorColorTypeNode: RedBlackNode<K, V>? = successorNode

        if (leftChildIsNull(nodeToDelete)) {
            // Кейс 1: нет левого ребенка - заменяем его правым
            replacementNode = getRightChild(nodeToDelete)
            transplant(nodeToDelete, getRightChild(nodeToDelete))
        } else if (rightChildIsNull(nodeToDelete)) {
            // Кейс 2: нет правого - заменяем леввым
            replacementNode = getLeftChild(nodeToDelete)
            transplant(nodeToDelete, getLeftChild(nodeToDelete))
        } else {
            // Кейс 3: Есть оба ребенка - находим минимального в правом поддереве
            successorNode = findLeftmostChild(getRightChild(nodeToDelete))
            originalSuccessorColorTypeNode = successorNode
            replacementNode = getRightChild(successorNode)

            if (getParentNodeOrNull(successorNode) != nodeToDelete) {
                transplant(successorNode, getRightChild(successorNode))
                changeRightRelations(successorNode, nodeToDelete)
            }

            transplant(nodeToDelete, successorNode)
            changeLeftRelations(successorNode, nodeToDelete)

            successorNode?.color = getColour(nodeToDelete)
        }

        // Если удалили черную ноду
        if (isBlack(originalSuccessorColorTypeNode)) {
            fixDeleteViolations(replacementNode)
        }

        removeOneFromCountNodes()

        return true
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

        newNode?.parent = getParentNodeOrNull(oldNode)
    }

    /**
     * Заменяет левого ребёнка и устанавливает связи
     * @param parent - кому внедрить в качестве левого ребёнка
     * @param newLeftChild - кого внедрить
     */
    private fun changeLeftRelations(parent: RedBlackNode<K, V>?, newLeftChild: RedBlackNode<K, V>?) {
        parent?.left = getLeftChild(newLeftChild)
        parent?.left?.parent = parent
    }

    /**
     * Заменяет правого ребёнка и устанавливает связи
     * @param parent - кому внедрить в качестве правого ребёнка
     * @param newRightChild - кого внедрить
     */
    private fun changeRightRelations(parent: RedBlackNode<K, V>?, newRightChild: RedBlackNode<K, V>?) {
        parent?.right = getRightChild(newRightChild)
        parent?.right?.parent = parent
    }

    private fun fixDeleteViolations(node: RedBlackNode<K, V>?) {
        var currentNode = node

        while (!isRoot(currentNode) && isBlack(currentNode)) {
            if (nodeIsLeftChild(currentNode)) {
                //  Берем брата ноды
                var rightBrotherOfCurrentNode = getRightBrother(currentNode)
                var parent = getParentNodeOrNull(currentNode)

                if (isRed(rightBrotherOfCurrentNode)) {
                    setBlack(rightBrotherOfCurrentNode)
                    setRed(parent)
                    rotateLeft(parent)
                    rightBrotherOfCurrentNode = getRightBrother(currentNode)
                }


                if (childrenAreBlack(rightBrotherOfCurrentNode)) {
                    setRed(rightBrotherOfCurrentNode)
                    currentNode = getParentNodeOrNull(currentNode)

                } else {
                    if (rightChildIsBlack(rightBrotherOfCurrentNode)) {
                        setBlack(getLeftChild(rightBrotherOfCurrentNode))
                        setRed(rightBrotherOfCurrentNode)
                        rotateRight(rightBrotherOfCurrentNode)
                        rightBrotherOfCurrentNode = getRightBrother(currentNode)
                    }

                    parent = getParentNodeOrNull(currentNode) ?: throw Exception()

                    rightBrotherOfCurrentNode?.color = getColour(parent)
                    setBlack(parent)
                    setBlack(getRightChild(rightBrotherOfCurrentNode))
                    rotateLeft(parent)
                    currentNode = getRoot()
                }


            } else {
                // Симметричный случай: Текущая нода - правый ребенок
                var leftBrotherOfCurrentNode = getLeftBrother(currentNode)
                var parent = getParentNodeOrNull(currentNode)

                if (isRed(leftBrotherOfCurrentNode)) {
                    setBlack(leftBrotherOfCurrentNode)
                    setRed(parent)
                    rotateRight(parent)
                    leftBrotherOfCurrentNode = getLeftBrother(currentNode)
                }


                if (childrenAreBlack(leftBrotherOfCurrentNode)) {
                    setRed(leftBrotherOfCurrentNode)
                    currentNode = getGrandparentNodeOrNull(currentNode)
                } else {

                    if (leftChildIsBlack(leftBrotherOfCurrentNode)) {
                        setBlack(getRightChild(leftBrotherOfCurrentNode))
                        setRed(leftBrotherOfCurrentNode)
                        rotateLeft(leftBrotherOfCurrentNode)
                        leftBrotherOfCurrentNode = getLeftBrother(currentNode)
                    }

                    parent = getParentNodeOrNull(currentNode) ?: throw Exception()

                    leftBrotherOfCurrentNode?.color = getColour(parent)
                    setBlack(parent)
                    setBlack(getLeftChild(leftBrotherOfCurrentNode))
                    rotateRight(parent)
                    currentNode = getRoot()
                }
            }
        }

        setBlack(currentNode)
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

    private fun getRightBrother(node: RedBlackNode<K, V>?): RedBlackNode<K, V>? {
        val parent: RedBlackNode<K, V>? = getParentNodeOrNull(node)
        val rightBrotherOfNode: RedBlackNode<K, V>? = getRightChild(parent)
        return rightBrotherOfNode
    }

    private fun getLeftBrother(node: RedBlackNode<K, V>?): RedBlackNode<K, V>? {
        val parent: RedBlackNode<K, V>? = getParentNodeOrNull(node)
        val leftBrotherOfNode: RedBlackNode<K, V>? = getLeftChild(parent)
        return leftBrotherOfNode
    }

    private fun getColour(node: RedBlackNode<K, V>): Colors {
        return node.color
    }

    private fun findLeftmostChild(node: RedBlackNode<K, V>?): RedBlackNode<K, V>? {
        var current = node

        while (leftChildIsNotNull(current)) {
            current = getLeftChild(current)
        }
        return current
    }

    private fun isBlack(node: RedBlackNode<K, V>?): Boolean {
        return node?.color == Colors.BLACK
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

        if (parent != null) {
            return parent
        }
        return null
    }

    private fun leftChildIsNull(node: RedBlackNode<K, V>): Boolean {
        return (node.left == null)
    }

    private fun rightChildIsNull(node: RedBlackNode<K, V>): Boolean {
        return (node.right == null)
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

    override fun height(): Int {
        TODO("Not yet implemented")
    }

    override fun search(key: K): V? {
        TODO()
    }

    private class BrakeException : Exception()
}
