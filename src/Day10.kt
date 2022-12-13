class Cpu {

    private var tick = 0
    private var x = 1

    fun line() = StringBuilder("........................................")

    var screen = listOf(line())

    var data = listOf<Int>()

    fun noop() = tick()

    fun addx(i: Int) {
        tick()
        tick { x += i }
    }

    fun tick(action: () -> Unit = {}) {
        val position = tick.mod(40)
        val sprite = (x-1)..(x+1)
        screen.last()[position] = if(position in sprite) '#' else '.'
        tick += 1
        if (tick.plus(20).mod(40) == 0) {
            screen = screen + line()
            data = data + (tick * x)
        }
        action()
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val cpu = Cpu()
        input.forEach {
            val add = it.drop(5).toIntOrNull()
            if (add == null) {
                cpu.noop()
            } else {
                cpu.addx(add)
            }
        }
        return cpu.data.reduce(Int::plus)
    }

    fun part2(input: List<String>): String {
        val cpu = Cpu()
        input.forEach {
            val add = it.drop(5).toIntOrNull()
            if (add == null) {
                cpu.noop()
            } else {
                cpu.addx(add)
            }
        }
        return cpu.screen.joinToString(separator = "\n") { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    println(part1(testInput))
    check(part1(testInput) == 13140)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
