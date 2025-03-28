# ***Trees Library*** :deciduous_tree:

This Kotlin library provides implementations of various tree data structures. 
Currently, the following three types are supported:
+ ***Binary Tree***  
  A basic binary tree implementation
+ ***Red-Black Tree***  
  It is a self-balancing tree where the notes are red and black
+ ***AVL Tree***  
  A self-balancing tree where the height balance is maintained

## ***Features***

### **Supported Methods**

+ `insert(key: K, value: V): Node<K, V>`  
  Adds a new node or changes the value in an existing one
+ `search(key: K): V?`  
  Searches for a value by key
+ `delete(key: K): Boolean`  
  Deletes a value by key
+ `clear()`  
  Cleans the entire tree
+ `size(): Int`  
  Returns the number of elements in the tree
+ `inOrder(): List<Node<K, V>>`  
  Returns a list of all tree elements in ascending order
+ `isEmpty(): Boolean`  
  Checks if the tree is empty
+ `min(): K?`  
  Returns the minimum key in the tree
+ `max(): K?`  
  Returns the maximum key in the tree
+ `range(start: K, end: K): MutableList<V>?`  
  Returns a list with values taken by keys from the range
+ `contains(value: V): Boolean`  
  Checks whether the node exists by value

## ***Usage***

### **AVLTree simply examples**

These examples can be adapted for other tree types as well.  

A simple example of using `AVLTree<K : Comparable<K>, V>`:

```kotlin
val avl = AVLTree<Int, String>()
val key: Int = 10
val value: String = "Some string"
avl.insert(key, value)

val resultSearch = avl.search(key) ?: "value not found =("
println(resultSearch)
```

*Output:*
```
Some string
```

### **Advanced Example**

Below is an example of working with insert(), delete() and inOrder() methods:

```kotlin
fun printAllNodes(avl: AVLTree<Int, String>) {
    val order = avl.inOrder()
    for (i in order.indices) {
        println("Key: ${order[i].key} Value: ${order[i].value}")
    }
}

val avl = AVLTree<Int, String>()
avl.insert(15, "Some text")
avl.insert(10, "qwerty")
avl.insert(52, "52")

printAllNodes(avl)
println("_____Before some changes:_____")

avl.delete(15)

printAllNodes(avl)
```

*Output:*

```
Key: 10 Value: qwerty
Key: 15 Value: Some text
Key: 52 Value: 52
_____Before some changes:_____
Key: 10 Value: qwerty
Key: 52 Value: 52
```


## ***Documentation***

The documentation is available in the directory [docs/](docs/)

## ***License*** :clipboard:

This project is licensed under the [MIT License](LICENSE).