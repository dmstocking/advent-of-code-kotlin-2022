// Cheat the package to get true isolation between source files
package day12

import readInput
import java.util.PriorityQueue
import kotlin.math.abs

data class Position(val x: Int, val y: Int) {

    var cost = 0

    fun up(): Position = copy(y = y - 1)
    fun down(): Position = copy(y = y + 1)
    fun right(): Position = copy(x = x + 1)
    fun left(): Position = copy(x = x - 1)

    fun distance(other: Position) = abs(x - other.x) + abs(y - other.y)
}

data class Map(val start: Position, val end: Position, val data: List<List<Int>>) {

    fun shortestPath(start: Position): List<Position>? {
        val queue = PriorityQueue<Position> { a, b -> (a.distance(end) + a.cost).compareTo(b.distance(end) + b.cost) }
        var at = start
        queue.add(at)
        val closed = mutableSetOf<Position>()
        val cameFrom = mutableMapOf<Position, Position>()
        val cost = mutableMapOf<Position, Int>()
        while (at != end) {
            if (queue.size == 0) {
                return null
            }
            at = queue.remove()
            closed.add(at)
            listOf(at.up(), at.down(), at.right(), at.left())
                .filter { it.y in data.indices && it.x in data.first().indices }
                .filter { data[it.y][it.x] - data[at.y][at.x] <= 1 }
                .filter { it !in closed }
                .forEach {
                    val oldCost = cost[it]
                    val newCost = (cost[at] ?: 0) + 1
                    if (oldCost == null || newCost < oldCost) {
                        queue.remove(it)
                        it.cost = newCost
                        cost[it] = newCost
                        queue.add(it)
                        cameFrom[it] = at
                    }
                }
        }
        return generateSequence {
            val next = cameFrom[at]
            next?.let { at = next }
            next
        }
            .toList()
    }

    companion object {
        fun parse(input: List<String>): Map {
            var start: Position? = null
            var end: Position? = null

            val data = input.mapIndexed { y, line ->
                line.mapIndexed { x, c ->
                    val char = when (c) {
                        'S' -> {
                            start = Position(x, y)
                            'a'
                        }
                        'E' -> {
                            end = Position(x, y)
                            'z'
                        }
                        else -> c
                    }
                    char.code - 'a'.code
                }
            }
            return Map(start!!, end!!, data)
        }
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val map = Map.parse(input)
        return map.shortestPath(map.start)?.size ?: -1
    }

    fun part2(input: List<String>): Int {
        val map = Map.parse(input)
        return map
            .data
            .flatMapIndexed { y, line -> line.mapIndexed { x, elevation -> Position(x, y) to elevation }}
            .filter { (_, elevation) -> elevation == 0 }
            .map { (position, _) -> map.shortestPath(position)?.size ?: Int.MAX_VALUE }
            .minOf { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    println(part1(testInput))
    check(part1(testInput) == 31)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
