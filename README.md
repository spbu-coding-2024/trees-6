# ***Trees Library*** :deciduous_tree:

This Kotlin library provides implementations of various tree data structures. 
Currently, the following three types are supported:
+ ***Binary Tree***  
  A basic binary tree implementation
+ ***Red-Black Tree***  
  It is a self-balancing tree where the notes are red and black
+ ***AVL Tree***  
  A self-balancing tree where the height balance is maintained

## ***Technologies***
+ JUnit5
+ Apache Common Lang 3 3.12.0
+ Dokka 1.9.10
+ JaCoCo
+ Groovy 3.0.22
+ Kotlin 1.9.24
+ Gradle 8.10

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
+ `range(start: K, end: K): List<V>?`  
  Returns a list with values taken by keys from the range
+ `contains(value: V): Boolean`  
  Checks whether the node exists by value

## ***Usage***

### ***Quick start***
+ Download the repository from GitHub via ssh or https:
```
git clone https://github.com/spbu-coding-2024/trees-6.git
```
or
```
git clone git@github.com:spbu-coding-2024/trees-6.git
```

+ Check out the [samples/](src/main/kotlin/samples)

### **AVLTree simply examples**

**These examples can be adapted for other types of trees.**

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
Additional examples can be found in the directory [samples/](src/main/kotlin/samples)

## ***Gradle commands*** :elephant:
Commands should be run from the root directory of the project.

+ Running tests
```
./gradlew test
```
+ Build
```
./gradlew build
```

+ Documentation generation
```
./gradlew dokkaHtml
```
The documentation will be updated in the directory [docs/](docs/)

## ***Documentation***

The documentation is available in the directory [docs/](docs/)

## ***Contributors***

[Кривоносов Константин](https://github.com/fUS1ONd)
[Галай Анастасия](https://github.com/Galay-Nastya)
[Варлыга Максим](https://github.com/vvmaksim)

## ***License*** :clipboard:

This project is licensed under the [MIT License](LICENSE)
