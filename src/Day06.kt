fun main() {
    fun part1(input: List<String>): Int {
        var i = 0
        input.first()
            .asSequence()
            .windowed(4) { it.toSet().size }
            .onEach { i += 1 }
            .dropWhile { it != 4 }
            .take(1)
            .first()
        return i + 3
    }

    fun part2(input: List<String>): Int {
        var i = 0
        input.first()
            .asSequence()
            .windowed(14) { it.toSet().size }
            .onEach { i += 1 }
            .dropWhile { it != 14 }
            .take(1)
            .first()
        return i + 13
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 7)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
