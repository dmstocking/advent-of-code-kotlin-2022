fun main() {
    fun part1(input: List<String>): Int {
        val data = List(700) { List(200) { 0 }.toMutableList() }.toMutableList()
        input.forEach { line ->
            val points = line
                .split(" -> ")
                .map {
                    it
                        .split(",")
                        .let { (x, y) -> x.toInt() to y.toInt() }
                }
            points.zip(points.drop(1)) { (x1, y1), (x2, y2) ->
                if (x1 == x2) {
                    val (yf, yl) = minOf(y1, y2) to maxOf(y1, y2)
                    (yf..yl).forEach {
                        data[x1][it] = 1
                    }
                } else {
                    val (xf, xl) = minOf(x1, x2) to maxOf(x1, x2)
                    (xf..xl).forEach {
                        data[it][y1] = 1
                    }
                }
            }
        }
        var i = 0
        var x = 500
        var y = 0
        while (true) {
            if (x !in 0 until 700 || y !in 0 until 199) {
                return i
            }
            if (data[x][y+1] == 0) {
                y++
            } else if (data[x-1][y+1] == 0) {
                x--
                y++
            } else if (data[x+1][y+1] == 0) {
                x++
                y++
            } else {
                data[x][y] = 1
                x = 500
                y = 0
                i++
            }
        }
    }

    fun part2(input: List<String>): Int {
        val data = List(700) { List(200) { 0 }.toMutableList() }.toMutableList()
        var max = 0
        input.forEach { line ->
            val points = line
                .split(" -> ")
                .map {
                    it
                        .split(",")
                        .let { (x, y) -> x.toInt() to y.toInt() }
                }
            points.zip(points.drop(1)) { (x1, y1), (x2, y2) ->
                if (x1 == x2) {
                    val (yf, yl) = minOf(y1, y2) to maxOf(y1, y2)
                    (yf..yl).forEach {
                        data[x1][it] = 1
                    }
                    max = maxOf(max, y2)
                } else {
                    val (xf, xl) = minOf(x1, x2) to maxOf(x1, x2)
                    (xf..xl).forEach {
                        data[it][y1] = 1
                    }
                    max = maxOf(max, y1)
                }
            }
        }
        max += 2
        data.indices.forEach { i ->
            data[i][max] = 1
        }
        var i = 0
        var x = 500
        var y = 0
        while (true) {
            if (x !in 0 until 700 || y !in 0 until 199) {
                throw Exception()
            }
            if (data[x][y+1] == 0) {
                y++
            } else if (data[x-1][y+1] == 0) {
                x--
                y++
            } else if (data[x+1][y+1] == 0) {
                x++
                y++
            } else {
                data[x][y] = 2
                i++
                if (x == 500 && y == 0) {
                    return i
                }
                x = 500
                y = 0
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    println(part1(testInput))
    println(part2(testInput))
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
