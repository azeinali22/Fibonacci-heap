import kotlin.math.ln
import kotlin.math.floor

//  fibonacci tree
class FibonacciTree(var value: Int) {
    val children: MutableList<FibonacciTree> = mutableListOf()
    var order: Int = 0

    // Adding a tree at the end of the list of children
    fun addAtEnd(tree: FibonacciTree) {
        children.add(tree)
        order++
    }
}

// Developing Fibonacci heap
class FibonacciHeap {
    private val trees: MutableList<FibonacciTree> = mutableListOf()
    var least: FibonacciTree? = null
        private set
    var count: Int = 0
        private set

    // Add a node
    fun insertNode(value: Int) {
        val newTree = FibonacciTree(value)
        trees.add(newTree)
        if (least == null || value < least!!.value) {
            least = newTree
        }
        count++
    }

    // Get minimum value
    fun getMin(): Int? {
        return least?.value
    }

    // Extract the minimum value
    fun extractMin(): Int? {
        val smallest = least
        if (smallest != null) {
            for (child in smallest.children) {
                trees.add(child)
            }
            trees.remove(smallest)
            if (trees.isEmpty()) {
                least = null
            } else {
                least = trees[0]
                consolidate()
            }
            count--
            return smallest.value
        }
        return null
    }

    // Combine  the trees
    private fun consolidate() {
        val aux = arrayOfNulls<FibonacciTree>(floorLog(count) + 1)

        while (trees.isNotEmpty()) {
            var x = trees[0]
            var order = x.order
            trees.removeAt(0)
            while (aux[order] != null) {
                var y = aux[order]!!
                if (x.value > y.value) {
                    val temp = x
                    x = y
                    y = temp
                }
                x.addAtEnd(y)
                aux[order] = null
                order++
            }
            aux[order] = x
        }

        least = null
        for (tree in aux) {
            if (tree != null) {
                trees.add(tree)
                if (least == null || tree.value < least!!.value) {
                    least = tree
                }
            }
        }
    }
}

//  function to calculate floor log
fun floorLog(x: Int): Int {
    if (x <= 0) throw IllegalArgumentException("Input must be greater than 0")
    return floor(ln(x.toDouble()) / ln(2.0)).toInt()
}

fun main() {
    val fibonacciHeap = FibonacciHeap()

    fibonacciHeap.insertNode(7)
    fibonacciHeap.insertNode(3)
    fibonacciHeap.insertNode(17)
    fibonacciHeap.insertNode(24)

    println("The minimum value of the Fibonacci heap: ${fibonacciHeap.getMin()}")
    println("The minimum value removed: ${fibonacciHeap.extractMin()}")
    println("The new minimum value of the Fibonacci heap: ${fibonacciHeap.getMin()}")
}