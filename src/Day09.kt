import java.lang.Exception
import kotlin.math.abs

data class Position(val x: Int, val y: Int) {

    fun follow(other: Position): Position {
        return if (!near(other)) {
            moveTowards(other)
        } else {
            this
        }
    }

    fun near(other: Position): Boolean {
        return other.x - x in -1..1 && other.y - y in -1..1
    }

    fun moveTowards(other: Position): Position {
        var newX = x
        var newY = y
        val xDirection = other.x - x
        val yDirection = other.y - y
        val shouldMoveX = xDirection < -1 || 1 < xDirection
        val shouldMoveY = yDirection < -1 || 1 < yDirection
        if (abs(xDirection) > 0 && abs(yDirection) > 0 && (shouldMoveX || shouldMoveY)) {
            newX += xDirection / abs(xDirection)
            newY += yDirection / abs(yDirection)
        } else {
            if (shouldMoveX) {
                newX += xDirection / abs(xDirection)
            }
            if (shouldMoveY) {
                newY += yDirection / abs(yDirection)
            }
        }
        return Position(newX, newY)
    }

    fun up(): Position = copy(y = y + 1)
    fun down(): Position = copy(y = y - 1)
    fun right(): Position = copy(x = x + 1)
    fun left(): Position = copy(x = x - 1)
}

data class Snake(val knots: List<Position>) {
    fun move(direction: String): Snake {
        return knots
            .runningFold(null as? Position) { knot, next ->
                if (knot == null) {
                    when (direction) {
                        "U" -> next.up()
                        "D" -> next.down()
                        "R" -> next.right()
                        "L" -> next.left()
                        else -> throw Exception()
                    }
                } else {
                    next.follow(knot)
                }
            }
            .filterNotNull()
            .let { Snake(it) }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        var head = Position(0, 0)
        return input
            .flatMap {
                val (move, times) = it.split(" ")
                List(times.toInt()) { move }
            }
            .runningFold(Position(0, 0)) { tail, move ->
                head = when (move) {
                    "U" -> head.copy(y = head.y + 1)
                    "D" -> head.copy(y = head.y - 1)
                    "R" -> head.copy(x = head.x + 1)
                    "L" -> head.copy(x = head.x - 1)
                    else -> throw Exception()
                }
                if (!tail.near(head)) {
                    tail.moveTowards(head)
                } else {
                    tail
                }
            }
            .toSet()
            .let { it.size }
    }

    fun part2(input: List<String>): Int {
        return input
            .flatMap {
                val (move, times) = it.split(" ")
                List(times.toInt()) { move }
            }
            .runningFold(Snake(List(10) { Position(0, 0)})) { snake, direction ->
                snake.move(direction)
            }
            .map { it.knots.last() }
            .toSet()
            .let { it.size }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    println(part1(testInput))
    check(part1(testInput) == 13)

    val testInput2 = readInput("Day09_test_2")
    println(part2(testInput2))
    check(part2(testInput2) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
