fun main() {
    fun part1(input: List<String>): Int {
        val games = parse(input)
        return games.filter { game ->
            game.cubeSets.all { cubeSet -> isValidCubeSet(cubeSet) }
        }.sumOf { it.id }
    }

    fun part2(input: List<String>): Int {
        val games = parse(input)
        var sum = 0
        for (game in games) {
            val reqRed = game.cubeSets.maxOf { it.numRed }
            val reqGreen = game.cubeSets.maxOf { it.numGreen }
            val reqBlue = game.cubeSets.maxOf { it.numBlue }
            val pwr = reqRed * reqGreen * reqBlue
            sum += pwr
        }
        return sum
    }

    val sampleInput = readInput("day02-sample")
    val input = readInput("day02-input")

    println("part 1 sample input: ${part1(sampleInput)}")
    println("part 1 full input: ${part1(input)}")
    println("part 2 sample input: ${part2(sampleInput)}")
    println("part 2 full input: ${part2(input)}")
}

fun isValidCubeSet(
    cubeSet: CubeSet, maxRed: Int = 12, maxGreen: Int = 13, maxBlue: Int = 14
): Boolean {
    return cubeSet.numGreen <= maxGreen && cubeSet.numRed <= maxRed && cubeSet.numBlue <= maxBlue
}

fun parse(input: List<String>): List<Game> {
    val games = mutableListOf<Game>()
    var gameId = 1
    for (line in input) {
        val sets = line.split(": ").last().split("; ") // "3 blue, 4 red"
        games.add(Game(gameId++, sets.map { parseCubeSet(it) }))
    }
    return games
}

fun parseCubeSet(s: String): CubeSet { // s = "3 green, 4 blue, 1 red"
    val cubes = s.split(", ") // "3 green"
    check(cubes.size < 4)
    val values = cubes.associate {
        val splits = it.split(" ")
        splits[1] to splits[0].toInt()
    }
    return CubeSet(
        numRed = values["red"] ?: 0, numBlue = values["blue"] ?: 0, numGreen = values["green"] ?: 0
    )
}

data class Game(val id: Int, val cubeSets: List<CubeSet>)
data class CubeSet(var numBlue: Int = 0, var numRed: Int = 0, var numGreen: Int = 0)
