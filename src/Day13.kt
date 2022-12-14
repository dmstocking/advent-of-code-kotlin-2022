
sealed class Item : Comparable<Item> {
    override fun compareTo(other: Item): Int {
        return this.compare(other)
    }
}
data class ItemList(val items: List<Item>): Item()

data class Value(val value: Int): Item()

fun Item.compare(right: Item): Int {
    val left = this
    return when {
        left is ItemList && right is ItemList -> left.compare(right)
        left is ItemList && right is Value -> left.compare(ItemList(listOf(right)))
        left is Value && right is ItemList -> ItemList(listOf(left)).compare(right)
        left is Value && right is Value -> left.value - right.value
        else -> throw Exception()
    }
}

fun ItemList.compare(right: ItemList): Int {
    val left = this
    left.items.zip(right.items)
        .forEach { (l, r) ->
            val compare = l.compare(r)
            if (compare != 0) {
                return compare
            }
        }
    return left.items.size - right.items.size
}

class StringParser(private val string: String) {
    private var i = 0
    fun next(): Char = string[i++]
    fun peek(): Char = string[i]
}

fun parseItemList(stringParser: StringParser): ItemList {
    stringParser.next()
    val items = mutableListOf<Item>()
    while (true) {
        when (stringParser.peek()) {
            ']' -> {
                stringParser.next()
                return ItemList(items)
            }
            else -> {
                items.add(parseItem(stringParser))
                if (stringParser.peek() == ',') {
                    stringParser.next()
                }
            }
        }
    }
}

fun parseItem(stringParser: StringParser): Item {
    return when (stringParser.peek()) {
        '[' -> parseItemList(stringParser)
        else -> {
            generateSequence {
                if (stringParser.peek().isDigit()) {
                    stringParser.next()
                } else {
                    null
                }
            }
                .joinToString(separator = "")
                .toInt()
                .let { Value(it) }
        }
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        return input
            .chunked(3)
            .mapIndexed { i, lines ->
                val (left, right) = lines
                    .let { (left, right) -> StringParser(left) to StringParser(right) }
                    .let { (left, right) -> parseItem(left) to parseItem(right) }
                if (left.compare(right) < 0) i+1 else 0
            }
            .sumOf { it }
    }

    fun part2(input: List<String>): Int {
        return input
            .plus("")
            .plus("[[2]]")
            .plus("[[6]]")
            .chunked(3)
            .flatMap { lines ->
                val (left, right) = lines
                    .let { (left, right) -> StringParser(left) to StringParser(right) }
                    .let { (left, right) -> parseItem(left) to parseItem(right) }
                listOf(left, right)
            }
            .sortedBy { it }
            .let {
                val decoderKeys = listOf("[[2]]", "[[6]]").map { parseItem(StringParser(it)) }
                it.mapIndexedNotNull { i, item ->
                    if (item in decoderKeys) {
                        i + 1
                    } else {
                        null
                    }
                }
            }
            .let { (i, j) -> i * j }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
