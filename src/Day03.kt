import java.lang.Exception

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map { line ->
                val first = line.substring(0, line.length/2)
                val second = line.substring(line.length/2)
                first
                    .toSet()
                    .intersect(second.toSet())
                    .first()
                    .let {
                        if (it in 'a'..'z') {
                            it.code - 96
                        } else {
                            it.code - 64 + 26
                        }
                    }
            }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input
            .chunked(3)
            .map { sacks ->
                sacks.map { it.toSet() }
                    .reduce { acc, chars -> acc.intersect(chars) }
                    .first()
                    .let {
                        if (it in 'a'..'z') {
                            it.code - 96
                        } else {
                            it.code - 64 + 26
                        }
                    }
            }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
