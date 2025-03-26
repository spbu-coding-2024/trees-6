package trees.redblack

import trees.base.AbstractTree

class RedBlackTree<K: Comparable<K>, V> : AbstractTree<K, V, RedBlackNode<K, V>>() {
    private var root: RedBlackNode<K, V>? = null
    private var countNodes: Int = 0

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

    private fun fixInsertion(originNode: RedBlackNode<K, V>) {
        // Тк в котлине в фукнцию передаются параметры, доступные
        // только для чтения, вынужден создать копию передаваемой ноды
        var node: RedBlackNode<K, V>? = originNode

        while (node?.parent?.color == Colors.RED) {
            if (node.parent == node.parent?.parent?.left) {
                val uncleOfNode: RedBlackNode<K, V>? = node.parent?.parent?.right

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
                val uncleOfNode: RedBlackNode<K, V>? = node.parent?.parent?.left

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

    override fun insert(key: K, value: V): RedBlackNode<K, V> {
        val newNode: RedBlackNode<K, V> = RedBlackNode(key, value)

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

        addOneToCountNodes()
        return newNode
    }

    override fun delete(key: K): Boolean {
        var wanted: RedBlackNode<K, V>? = searchNode(key)

        if (wanted == null) {
            return true
        }

        var x: RedBlackNode<K, V>?

        var copyWanted: RedBlackNode<K, V>? = wanted
        var copyWantedOriginallyColor: Colors? = copyWanted?.color

        if (wanted.left == null) {
            x = wanted.right
            transplant(wanted, wanted.right)
        } else if (wanted.right == null) {
            x = wanted.left
            transplant(wanted, wanted.left)
        } else {
            copyWanted = youngestLeftChild(wanted.right)
            copyWantedOriginallyColor = copyWanted?.color
            x = copyWanted?.right

            if (copyWanted?.parent == wanted) {
                x?.parent = copyWanted
            } else {
                transplant(copyWanted,copyWanted?.right)
                copyWanted?.right = wanted.right
                copyWanted?.right?.parent = copyWanted
            }
        }
        if (copyWantedOriginallyColor == Colors.BLACK) {
            fixDelete(x)
        }

        return true
    }

    private fun fixDelete(_node: RedBlackNode<K, V>?) {
        var node = _node
        while (node != getRoot() && node?.color == Colors.BLACK) {
            if (node == node.parent?.left) {
                var w: RedBlackNode<K, V>? = node.parent?.right

                if (w?.color == Colors.RED) {
                    w.color = Colors.BLACK
                    node.parent?.color = Colors.RED
                    rotateLeft(node.parent)
                    w = node.parent?.right
                }

                if (w?.left?.color == Colors.BLACK && w.right?.color == Colors.BLACK) {
                    w.color = Colors.RED
                    node = node.parent
                } else {
                    if (w?.right?.color == Colors.BLACK) {
                        w.left?.color = Colors.BLACK
                        w.color = Colors.RED
                        rotateRight(w)
                        w = node.parent?.right
                    }
                    var color: Colors? = node.parent?.color
                    if (color == null) throw BrakeException()
                    w?.color = color
                    node.parent?.color = Colors.BLACK
                    w?.right?.color = Colors.BLACK
                    rotateLeft(node.parent)
                    node = getRoot()
                }
            } else {
                var w: RedBlackNode<K, V>? = node.parent?.left

                if (w?.color == Colors.RED) {
                    w.color = Colors.BLACK
                    node.parent?.color = Colors.RED
                    rotateRight(node.parent)
                    w = node.parent?.left
                }

                if (w?.right?.color == Colors.BLACK && w.left?.color == Colors.BLACK) {
                    w.color = Colors.RED
                    node = node.parent
                } else {
                    if (w?.left?.color == Colors.BLACK) {
                        w.right?.color = Colors.BLACK
                        w.color = Colors.RED
                        rotateLeft(w)
                        w = node.parent?.left
                    }
                    var color: Colors? = node.parent?.color
                    if (color == null) throw BrakeException()
                    w?.color = color
                    node.parent?.color = Colors.BLACK
                    w?.left?.color = Colors.BLACK
                    rotateRight(node.parent)
                    node = getRoot()
                }
            }
        }
        node?.color = Colors.BLACK
    }

    private fun transplant(u: RedBlackNode<K, V>?, v: RedBlackNode<K, V>?) {
        if (u?.parent == null) {
            setRoot(v)
        } else if (u == u.parent?.left) {
            u.parent?.left = v
        } else {
            u.parent?.right = v
        }

        v?.parent = u?.parent
    }

    private fun youngestLeftChild(node: RedBlackNode<K, V>?): RedBlackNode<K, V>? {
        var current = node
        while (current?.left != null) {
            current = current.left ?: throw trees.redblack.RedBlackTree.BrakeException()
        }
        return current
    }

    private fun searchNode(key: K): RedBlackNode<K, V>? {
        var wanted = getRoot()
        while (wanted != null && key != wanted.key) {
            if (key < wanted.key) {
                wanted = wanted.left
            } else {
                wanted = wanted.right
            }
        }
        return wanted
    }

    override fun search(key: K): V? {
        TODO()
    }

    private class BrakeException : Exception()
}
