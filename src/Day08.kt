fun main() {
    fun part1(input: List<String>): Int {
        val heights = input
            .map { line -> line.map { it.digitToInt() } }
        val height = heights.size
        val width = heights.first().size
        return heights
            .mapIndexed { y, line ->
                line.mapIndexed { x, h ->
                    val left = (0 until x).map { heights[y][it] }.all { it < h }
                    val right = ((x+1) until width).map { heights[y][it] }.all { it < h }
                    val up = (0 until y).map { heights[it][x] }.all { it < h }
                    val down = ((y+1) until height).map { heights[it][x] }.all { it < h }
                    left || right || up || down
                }
            }
            .flatten()
            .map { if (it) 1 else 0 }
            .sumOf { it }
    }

    fun part2(input: List<String>): Int {
        val heights = input
            .map { line -> line.map { it.digitToInt() } }
        val height = heights.size
        val width = heights.first().size
        return heights
            .mapIndexed { y, line ->
                line.mapIndexed { x, h ->
                    val left = (0 until x).reversed().map { heights[y][it] }.takeWhileInclusive { it < h }.count()
                    val right = ((x+1) until width).map { heights[y][it] }.takeWhileInclusive { it < h }.count()
                    val up = (0 until y).reversed().map { heights[it][x] }.takeWhileInclusive { it < h }.count()
                    val down = ((y+1) until height).map { heights[it][x] }.takeWhileInclusive { it < h }.count()
                    left * right * up * down
                }
            }
            .flatten()
            .maxOf { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}

fun <T> Iterable<T>.takeWhileInclusive(predicate: (T) -> Boolean): List<T> {
    val list = ArrayList<T>()
    for (item in this) {
        list.add(item)
        if (!predicate(item))
            break
    }
    return list
}
