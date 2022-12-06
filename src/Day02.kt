import java.lang.Exception

enum class Play {
    ROCK, PAPER, SCISSORS;
    fun losses(other: Play): Boolean {
        return Play.values()[(this.ordinal + 1) % 3] == other
    }
}

fun Play.loser(): Play {
    return Play.values()[(this.ordinal - 1).mod(3)]
}

fun Play.winner(): Play {
    return Play.values()[(this.ordinal + 1).mod(3)]
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.map {
            val (his, mine) = it.split("\\s".toRegex())
            val play = when(his) {
                "A" -> Play.ROCK
                "B" -> Play.PAPER
                "C" -> Play.SCISSORS
                else -> throw Exception()
            }
            val response = when(mine) {
                "X" -> Play.ROCK
                "Y" -> Play.PAPER
                "Z" -> Play.SCISSORS
                else -> throw Exception()
            }

            val result = if (play == response) {
                3
            } else if (response.losses(play)) {
                0
            } else {
                6
            }

            result + response.ordinal + 1
        }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input.map {
            val (his, mine) = it.split("\\s".toRegex())
            val play = when(his) {
                "A" -> Play.ROCK
                "B" -> Play.PAPER
                "C" -> Play.SCISSORS
                else -> throw Exception()
            }
            val response = when(mine) {
                "X" -> play.loser() // Lose
                "Y" -> play // Draw
                "Z" -> play.winner() // Win
                else -> throw Exception()
            }

            val result = if (play == response) {
                3
            } else if (response.losses(play)) {
                0
            } else {
                6
            }

            result + response.ordinal + 1
        }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
