import java.lang.Exception
import java.util.Base64

fun parseMonkey(lines: List<String>, constructor: (Long, (Long) -> Long, (Long) -> Int, List<Long>) -> Monkey): Monkey {
    val items = lines[1].substring(18).split(", ").map { it.toLong() }
    val (a, op, b) = lines[2].substring(19).split(" ")
    val operation = { old: Long ->
        val a = a.toLongOrNull() ?: old
        val b = b.toLongOrNull() ?: old
        when (op) {
            "*" -> a * b
            "+" -> a + b
            else -> throw Exception()
        }
    }
    val divisible = lines[3].substring(21).toLong()
    val t = lines[4].substring(29).toInt()
    val f = lines[5].substring(30).toInt()
    return constructor(divisible, operation, { if (it.mod(divisible) == 0L) t else f }, items)
}

open class Monkey(val divisible: Long, protected val operation: (Long) -> Long, private val throwTo: (Long) -> Int, items: List<Long>) {
    private val items = ArrayDeque(items.toList())
    var inspections = 0L

    fun turn() {
        while (items.size > 0) {
            inspect()
        }
    }

    private fun inspect() {
        inspections += 1
        items
            .removeFirstOrNull()
            ?.let { evalWorry(it) }
            ?.let { monkey[throwTo(it)].catch(it) }
    }

    open fun evalWorry(item: Long): Long {
        return operation(item) / 3
    }

    private fun catch(item: Long) {
        items.addLast(item)
    }
}

class BadMonkey(divisible: Long, operation: (Long) -> Long, throwTo: (Long) -> Int, items: List<Long>): Monkey(divisible, operation, throwTo, items) {
    var mod = 0L

    override fun evalWorry(item: Long): Long {
        return operation(item) % mod
    }
}

var monkey = listOf<Monkey>()

fun main() {
    fun part1(input: List<String>): Long {
        monkey = input.chunked(7).map { parseMonkey(it, ::Monkey) }
        (1..20).forEach { _ ->
            monkey.forEach { it.turn() }
        }
        return monkey.map { it.inspections }.sortedByDescending { it }.take(2).let { (a, b) -> a * b }
    }

    fun part2(input: List<String>): Long {
        val m = input.chunked(7).map { parseMonkey(it, ::BadMonkey) }.map { it as BadMonkey }
        monkey = m
        val mod = m.map { it.divisible }.reduce(Long::times)
        m.forEach { it.mod = mod }
        (1..10000).forEach { _ ->
            m.forEach { it.turn() }
        }
        return m
            .map { it.inspections }
            .sortedByDescending { it }
            .take(2)
            .let { (a, b) -> a * b }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    println(part1(testInput))
    println(part2(testInput))
    check(part1(testInput) == 10605L)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
