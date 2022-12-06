fun main() {
    fun part1(input: List<String>): Int {
        return input
            .filter { line ->
                val (first, second) = line.split(",")
                    .map {
                        val (a, b) = it.split("-").map(String::toInt)
                        a..b
                    }
                first in second || second in first
            }
            .size
    }

    fun part2(input: List<String>): Int {
        return input
            .filter { line ->
                val (first, second) = line.split(",")
                    .map {
                        val (a, b) = it.split("-").map(String::toInt)
                        a..b
                    }
                first.overlaps(second) || second.overlaps(first)
            }
            .size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

operator fun IntRange.contains(other: IntRange): Boolean {
    return other.first in this && other.last in this
}

fun IntRange.overlaps(other: IntRange): Boolean {
    return other.first in this || other.last in this
}
