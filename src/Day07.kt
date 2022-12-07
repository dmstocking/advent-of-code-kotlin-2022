import java.util.Stack

sealed class Node {
    abstract fun size(): Int
}
class Directory(val children: MutableMap<String, Node> = mutableMapOf()): Node() {
    override fun size(): Int = children.entries.sumOf { it.value.size() }
}
class File(private val size: Int): Node() {
    override fun size(): Int = size
}

fun main() {
    fun part1(input: List<String>): Int {
        var i = 0
        val root = Directory()
        val stack = Stack<Directory>()
        stack.push(root)
        while (i < input.size) {
            val line = input[i]
            i += 1
            when {
                line == "$ cd .." -> stack.pop()
                line == "$ cd /" -> {
                    stack.clear()
                    stack.push(root)
                }

                line.startsWith("$ cd") -> {
                    val directory = line.substring(5)
                    stack
                        .peek()
                        .children
                        .computeIfAbsent(directory) { Directory() }
                        .let { it as? Directory }
                        ?.let { stack.push(it) }
                }
                line == "$ ls" -> {
                    while (i < input.size) {
                        val output = input[i]
                        if (output.startsWith("$")) {
                            break
                        }
                        i += 1

                        val (size, name) = output.split(" ")
                        val children = stack
                            .peek()
                            .children
                        if (size == "dir") {
                            children.computeIfAbsent(name) { Directory() }
                        } else {
                            children.computeIfAbsent(name) { File(size.toInt()) }
                        }
                    }
                }
            }
        }
        var sum = 0
        root.children.forEach { (_, node) ->
            walk(node) { node ->
                if (node is Directory) {
                    val size = node.size()
                    if (size <= 100000) {
                        sum += size
                    }
                }
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        var i = 0
        val root = Directory()
        val stack = Stack<Directory>()
        stack.push(root)
        while (i < input.size) {
            val line = input[i]
            i += 1
            when {
                line == "$ cd .." -> stack.pop()
                line == "$ cd /" -> {
                    stack.clear()
                    stack.push(root)
                }

                line.startsWith("$ cd") -> {
                    val directory = line.substring(5)
                    stack
                        .peek()
                        .children
                        .computeIfAbsent(directory) { Directory() }
                        .let { it as? Directory }
                        ?.let { stack.push(it) }
                }
                line == "$ ls" -> {
                    while (i < input.size) {
                        val output = input[i]
                        if (output.startsWith("$")) {
                            break
                        }
                        i += 1

                        val (size, name) = output.split(" ")
                        val children = stack
                            .peek()
                            .children
                        if (size == "dir") {
                            children.computeIfAbsent(name) { Directory() }
                        } else {
                            children.computeIfAbsent(name) { File(size.toInt()) }
                        }
                    }
                }
            }
        }
        val total = root.size()
        val target = 30000000 - (70000000 - total)
        var min = Int.MAX_VALUE
        root.children.forEach { (_, node) ->
            walk(node) { node ->
                if (node is Directory) {
                    val size = node.size()
                    if (size >= target) {
                        if (size < min) {
                            min = size
                        }
                    }
                }
            }
        }
        return min
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

fun walk(node: Node, action: (Node) -> Unit) {
    action(node)
    if (node is Directory) {
        node.children.forEach { (_, node) -> walk(node, action) }
    }
}
