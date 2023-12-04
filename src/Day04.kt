fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        for (line in input) {
            val numbers = line.split(":")[1].split(" | ")
            val winning = numbers[0].trim().split("\\s+".toRegex()).map { it.toInt() }.toSet()
            val yours = numbers[1].trim().split("\\s+".toRegex()).map { it.toInt() }.toSet()
            val matches = yours.intersect(winning)
            if (matches.isNotEmpty()) {
                sum += 1 shl (matches.size - 1)
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val wonCards = IntArray(input.size) { 0 }
        for (line in input) {
            val card = line.split(":")
            val cardNumber = card[0].split("\\s+".toRegex())[1].trim().toInt()
            val numbers = card[1].split(" | ")
            val winning = numbers[0].trim().split("\\s+".toRegex()).map { it.toInt() }.toSet()
            val yours = numbers[1].trim().split("\\s+".toRegex()).map { it.toInt() }.toSet()
            val matches = yours.intersect(winning)
            wonCards[cardNumber - 1]++
            val copies = wonCards[cardNumber - 1]
            for (i in 1..matches.size) {
                wonCards[cardNumber - 1 + i] += copies
            }
        }
        return wonCards.sum()
    }

    val sampleInput = readInput("day04-sample")
    val input = readInput("day04-input")

    println("part 1 sample input: ${part1(sampleInput)}")
    println("part 1 full input: ${part1(input)}")
    println("part 2 sample input: ${part2(sampleInput)}")
    println("part 2 full input: ${part2(input)}")
}
