package trees.avl

import trees.base.AbstractTree

class AVLTree<K: Comparable<K>, V> : AbstractTree<K, V, AVLNode<K, V>>() {
    override fun insert(key: K, value: V): AVLNode<K, V> {
        TODO("Not yet implemented")
    }

    override fun delete(value: K): Boolean {
        TODO("Not yet implemented")
    }

    override fun search(key: K): V? {
        TODO("Not yet implemented")
    }

    override fun height(): Int {
        TODO("Not yet implemented")
    }

    private fun getBalanceFactor(node: AVLNode<K, V>): Int {
        val heightRightNode = node.right?.height ?: 0
        val heightLeftNode = node.left?.height ?: 0
        return heightRightNode - heightLeftNode
    }

    private fun getBalance(): Boolean {
        TODO("Not yet implemented")
    }

    private fun balance(): Boolean {
        TODO("Not yet implemented")
    }

    private fun rightTurn(): Boolean {
        TODO("Not yet implemented")
    }

    private fun leftTurn(): Boolean {
        TODO("Not yet implemented")
    }
}