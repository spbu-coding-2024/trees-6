package samples.avl

import trees.avl.AVLTree

fun sampleInsert() {
    val avl = AVLTree<Int, String>()
    avl.insert(52, "Some string")
    println(avl.search(52)) // return "Some string"
    avl.insert(52, "Updated string") // Updated value in node with key: 52
    println(avl.search(52)) // return "Updated string"
}

fun sampleDelete() {
    val avl = AVLTree<Int, String>()
    avl.insert(52, "Some string")
    avl.delete(52) // return false (key not found)
    println(avl.size()) // Print: 1
    avl.delete(52) // return true (node deleted)
    println(avl.size()) // Print: 0
}

fun sampleHeight() {
    val avl = AVLTree<Int, String>()
    println(avl.height()) // Print: 0
    avl.insert(52, "Some string")
    println(avl.height()) // Print: 1
}

fun sampleClear() {
    val avl = AVLTree<Int, String>()
    avl.insert(52, "Some string")
    println(avl.size()) // Print: 1
    avl.clear()
    println(avl.size()) // Print: 0
}

fun sampleSize() {
    val avl = AVLTree<Int, String>()
    println(avl.size()) // Print: 0
    avl.insert(52, "Some string")
    println(avl.size()) // Print: 1
}

fun sampleIsEmpty() {
    val avl = AVLTree<Int, String>()
    println(avl.isEmpty()) // Print: true
    avl.insert(52, "Some string")
    println(avl.isEmpty()) // Print: false
}

fun sampleInOrder() {
    val avl = AVLTree<Int, String>()
    avl.insert(52, "Some string 1")
    avl.insert(15, "Some string 2")
    avl.insert(24, "Some string 3")
    avl.insert(152, "Some string 4")
    val order = avl.inOrder()
    for (i in order.indices) {
        println("Key: ${order[i].key} Value: ${order[i].value}")
    }
    // Print:
    // Key: 15 Value: Some string 2
    // Key: 24 Value: Some string 3
    // Key: 52 Value: Some string 1
    // Key: 152 Value: Some string 4
}

fun sampleSearch() {
    val avl = AVLTree<Int, String>()
    avl.insert(52, "Some string")
    println(avl.search(52)) // Print: "Some string"
    println(avl.search(123)) // Print: null
}

fun sampleMax() {
    val avl = AVLTree<Int, String>()
    println(avl.max()) // Print: null
    avl.insert(52, "Some string 1")
    avl.insert(66, "Some string 2")
    avl.insert(25, "Some string 3")
    println(avl.max()) // Print: 66
}

fun sampleMin() {
    val avl = AVLTree<Int, String>()
    println(avl.min()) // Print: null
    avl.insert(52, "Some string 1")
    avl.insert(25, "Some string 2")
    avl.insert(66, "Some string 3")
    println(avl.min()) // Print: 25
}

fun sampleRange() {
    val avl = AVLTree<Int, String>()
    avl.insert(52, "Some string 1")
    avl.insert(25, "Some string 2")
    avl.insert(66, "Some string 3")
    avl.insert(17, "Some string 4")
    avl.insert(51, "Some string 5")
    val range = avl.range(25, 52)
    if (range.isNullOrEmpty()) { println(null); return }
    for (i in range.indices) {
        println("Value: ${range[i]}")
    }
    // Print:
    // Value: Some string 2
    // Value: Some string 5
    // Value: Some string 1
}

fun sampleContains() {
    val avl = AVLTree<Int, String>()
    avl.insert(52, "Some string")
    println(avl.contains("Some string")) // Print: true
    println(avl.contains("Random string")) // Print: false
}