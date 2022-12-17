import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Sensor(val x: Int, val y: Int, val bx: Int, val by: Int) {
    val radius: Int = abs(bx - x) + abs(by - y)
    companion object {
        private val parseRegex = "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)".toRegex()
        fun parse(line: String): Sensor {
            val result = parseRegex.matchEntire(line)!!
            val (x, y, bx, by) = result.groupValues.drop(1).map { it.toInt() }
            return Sensor(x, y, bx, by)
        }
    }
}

fun List<IntRange>.merge(): List<IntRange> {
    var prev = this
    var next = fold(emptyList<IntRange>()) { ranges, next ->
        var found = false
        ranges
            .map { range ->
                if (range.overlaps(next)) {
                    found = true
                    min(next.first, range.first)..max(next.last, range.last)
                } else {
                    range
                }
            }
            .let { ranges ->
                if (found) {
                    ranges
                } else {
                    ranges.plusElement(next)
                }
            }
    }
    while (next != prev) {
        prev = next
        next = next.fold(emptyList()) { ranges, next ->
            var found = false
            ranges
                .map {  range ->
                    if (range.overlaps(next) || next.overlaps(range)) {
                        found = true
                        min(next.first, range.first)..max(next.last, range.last)
                    } else {
                        range
                    }
                }
                .let { ranges ->
                    if (found) {
                        ranges
                    } else {
                        ranges.plusElement(next)
                    }
                }
        }
    }
    return next
}

fun main() {
    fun part1(input: List<String>, row: Int): Int {
        return input
            .map { Sensor.parse(it) }
            .let { sensors ->
                val beaconsOnRow = sensors
                    .map { (_, _, x, y) -> x to y }
                    .filter { (_, y) -> y == row }
                    .map { (x) -> x }

                return sensors
                    .flatMap { sensor ->
                        val (x, y, bx, by) = sensor
                        val distanceToRow = abs(row - y)
                        val halfOverlap = sensor.radius - distanceToRow
                        if (halfOverlap >= 0) {
                            (x-halfOverlap)..(x+halfOverlap)
                        } else {
                            emptyList()
                        }
                    }
                    .toSet()
                    .minus(beaconsOnRow)
                    .size
            }
    }


    fun part2(input: List<String>): Long {
        val sensors = input.map { Sensor.parse(it) }

        (0..4_000_000).forEach {  row ->
            sensors
                .mapNotNull { sensor ->
                    val (x, y, _, _) = sensor
                    val distanceToRow = abs(row - y)
                    val halfOverlap = sensor.radius - distanceToRow
                    if (halfOverlap >= 0) {
                        (x-halfOverlap)..(x+halfOverlap)
                    } else {
                        null
                    }
                }
                .merge()
                .let { ranges ->
                    // 100% not complete solution. If there was a range that went outside 0..4_000_000 this wouldn't
                    // work. But that happened to not occur in this problem.
                    if (ranges.size >= 2) {
                        val column = (ranges.maxOf { it.first } - 1).toLong()
                        return column * 4_000_000L + row.toLong()
                    }
                }
        }
        throw Exception()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput, 10).also { println(it) } == 26)

    val input = readInput("Day15")
    println(part1(input, 2000000))
    println(part2(input))
}
