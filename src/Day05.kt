import java.util.Stack

fun main() {
    val regex = "move (\\d+) from (\\d+) to (\\d+)".toRegex()

    fun part1(input: List<String>): String {
        val stacks = input
            .first { it[1] == '1' }
            .trim()
            .split("\\s+".toRegex())
            .last()
            .toInt()
            .let { 0 until it }
            .map { Stack<Char>() }

        input
            .takeWhile { it[1] != '1' }
            .forEach { line ->
                line.crates(stacks.size)
                    .mapIndexed { index, c -> index to c }
                    .filter { (_, c) -> c != ' ' }
                    .forEach { (i, c) ->
                        stacks[i].add(0, c)
                    }
            }

        input
            .dropWhile { !it.startsWith("move") }
            .flatMap {
                val match = regex.matchEntire(it)!!
                val n = match.groupValues[1].toInt()
                List(n) {
                    match.groupValues[2].toInt() to match.groupValues[3].toInt()
                }
            }
            .map { (f, t) -> (f-1) to (t-1) }
            .forEach { (from, to) ->
                stacks[to].add(stacks[from].pop())
            }

        return stacks.map { it.last() }.joinToString(separator = "")
    }

    fun part2(input: List<String>): String {
        val stacks = input
            .first { it[1] == '1' }
            .trim()
            .split("\\s+".toRegex())
            .last()
            .toInt()
            .let { 0 until it }
            .map { Stack<Char>() }

        input
            .takeWhile { it[1] != '1' }
            .forEach { line ->
                line.crates(stacks.size)
                    .mapIndexed { index, c -> index to c }
                    .filter { (_, c) -> c != ' ' }
                    .forEach { (i, c) ->
                        stacks[i].add(0, c)
                    }
            }

        input
            .dropWhile { !it.startsWith("move") }
            .forEach {
                val match = regex.matchEntire(it)!!
                val n = match.groupValues[1].toInt()
                val from = stacks[match.groupValues[2].toInt()-1]
                val to = stacks[match.groupValues[3].toInt()-1]
                val at = from.size - n
                repeat(n) {
                    to.push(from.removeAt(at))
                }
            }

        return stacks.map { it.last() }.joinToString(separator = "")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

fun String.crates(numberOfStacks: Int): List<Char> {
    val crates = mutableListOf<Char>()
    var index = 1
    while (index < length) {
        crates.add(this[index])
        index += 4
    }
    while (crates.size < numberOfStacks) {
        crates.add(' ')
    }
    return crates
}
