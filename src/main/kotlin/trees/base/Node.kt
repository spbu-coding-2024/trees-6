package trees.base

interface Node<K, V, N: Node<K, V, N>> {
    val key: K
    val value: V
    var left: N?
    var right: N?
}