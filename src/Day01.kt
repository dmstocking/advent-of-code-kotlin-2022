fun main() {
    fun part1(input: List<String>): Int {
        val grouped = mutableListOf<MutableList<Int>>()
        grouped.add(mutableListOf())
        input.forEach {
            if (it.isBlank()) {
                grouped.add(mutableListOf())
            } else {
                grouped[grouped.size-1].add(it.toInt())
            }
        }
        return grouped.maxOf { it.sum() }
    }

    fun part2(input: List<String>): Int {
        val grouped = mutableListOf<MutableList<Int>>()
        grouped.add(mutableListOf())
        input.forEach {
            if (it.isBlank()) {
                grouped.add(mutableListOf())
            } else {
                grouped[grouped.size-1].add(it.toInt())
            }
        }
        return grouped.map { it.sum() }.sortedDescending().take(3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
