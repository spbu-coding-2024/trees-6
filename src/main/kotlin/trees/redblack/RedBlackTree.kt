package trees.redblack

import trees.base.AbstractTree

class RedBlackTree<K: Comparable<K>, V> : AbstractTree<K, V, RedBlackNode<K, V>>() {
    private var root: RedBlackNode<K, V>? = null
    private var countNodes: Int = 0

    override fun insert(key: K, value: V): RedBlackNode<K, V> {
        var newNode: RedBlackNode<K, V> = RedBlackNode(key, value)

        var parentFinder: RedBlackNode<K, V>? = null
        var placeOfNewNode: RedBlackNode<K, V>? = getRoot()
        while (placeOfNewNode != null) {
            parentFinder = placeOfNewNode
            if (newNode.key < placeOfNewNode.key) {
                placeOfNewNode = placeOfNewNode.left
            } else {
                placeOfNewNode = placeOfNewNode.right
            }
        }

        newNode.parent = parentFinder

        if (parentFinder == null) {
            setRoot(newNode)
        } else if (newNode.key < parentFinder.key) {
            parentFinder.left = newNode
        } else {
            parentFinder.right = newNode
        }

        fixInsertion(newNode)

        return newNode
    }

    private fun fixInsertion(originNode: RedBlackNode<K, V>) {
        // Тк в котлине в фукнцию передаются параметры, доступные
        // только для чтения, вынужден создать копию передаваемой ноды
        var node: RedBlackNode<K, V>? = originNode

        while (node?.parent?.color == Colors.RED) {
            if (node.parent == node.parent?.parent?.left) {
                var uncleOfNode: RedBlackNode<K, V>? = node.parent?.parent?.right

                if (uncleOfNode?.color == Colors.RED) {
                    node.parent?.color = Colors.BLACK
                    uncleOfNode.color = Colors.BLACK
                    node.parent?.parent?.color = Colors.RED
                    node = node.parent?.parent
                } else {
                    if (node == node.parent?.right) {
                        node = node.parent
                        rotateLeft(node)
                    }

                    node?.parent?.color = Colors.BLACK
                    node?.parent?.parent?.color = Colors.RED
                    rotateRight(node?.parent?.parent)
                }
            } else {
                var uncleOfNode: RedBlackNode<K, V>? = node.parent?.parent?.left

                if (uncleOfNode?.color == Colors.RED) {
                    node.parent?.color = Colors.BLACK
                    uncleOfNode.color = Colors.BLACK
                    node.parent?.parent?.color = Colors.RED
                    node = node.parent?.parent
                } else {
                    if (node == node.parent?.left) {
                        node = node.parent
                        rotateRight(node)
                    }

                    node?.parent?.color = Colors.BLACK
                    node?.parent?.parent?.color = Colors.RED
                    rotateLeft(node?.parent?.parent)
                }
            }

            if (node == getRoot()) {
                break
            }
        }

        // Root дерева запривачен, поэтому копипаст и setRoot
        val fixRoot: RedBlackNode<K, V>? = getRoot()
        fixRoot?.color = Colors.BLACK
        setRoot(fixRoot)
    }

    private fun rotateLeft(target: RedBlackNode<K, V>?) {
        
        val copyOfRightSon: RedBlackNode<K, V>? = target?.right
        target?.right = copyOfRightSon?.left
        
        // устанавливаем relationships
        if (copyOfRightSon?.left != null) {
            copyOfRightSon.left?.parent = target
        }

        // Отцом копии назначаем деда
        copyOfRightSon?.parent = target?.parent

        if (target?.parent == null) {
            setRoot(copyOfRightSon)
        } else if (target == target.parent?.left) {
            target.parent?.left = copyOfRightSon
        } else {
            target.parent?.right = copyOfRightSon
        }

        copyOfRightSon?.left = target
        target?.parent = copyOfRightSon
    }

    private fun rotateRight(target: RedBlackNode<K, V>?) {

        val copyOfLeftSon: RedBlackNode<K, V>? = target?.left
        target?.left = copyOfLeftSon?.right

        if (copyOfLeftSon?.right != null) {
            copyOfLeftSon.right?.parent = target
        }

        copyOfLeftSon?.parent = target?.parent

        if (target?.parent == null) {
            setRoot(copyOfLeftSon)
        } else if (target == target.parent?.right) {
            target.parent?.right = copyOfLeftSon
        } else {
            target.parent?.left = copyOfLeftSon
        }

        copyOfLeftSon?.right = target
        target?.parent = copyOfLeftSon
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
