fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        for (line in input) {
            val n1 = line.first { it.digitToIntOrNull() != null }
            val n2 = line.last { it.digitToIntOrNull() != null }
            sum += "$n1$n2".toInt()
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val textDigits = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        var index = 1
        textDigits.associateBy { index++ }
        val allDigits = List(9) {"${it + 1}"} + textDigits
        val digitLookup = (1..9).associateBy { textDigits[it - 1] }.toMutableMap()
        (1..9).forEach {
            digitLookup["$it"] = it
        }

        var sum = 0
        for (line in input) {
            var n1 = "" // will be either "one" or "1" etc
            var n2 = ""
            for (i in line.indices) {
                val d = allDigits // map of digit -> index
                    .associateWith { line.indexOf(it, i) }
                    .filter { it.value > -1 }
                    .minByOrNull { it.value }
                if (d != null) {
                    n1 = d.key
                    break
                }
            }
            for (i in line.indices.reversed()) {
                val d = allDigits // map of digit -> index
                    .associateWith { line.indexOf(it, i) }
                    .filter { it.value > -1 }
                    .maxByOrNull { it.value }
                if (d != null) {
                    n2 = d.key
                    break
                }
            }

            val found = "${digitLookup[n1]!!}${digitLookup[n2]!!}"
            sum += found.toInt()
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("day01-1-input")

    part2(input.subList(0, 5)).println()
    """
        sdpgz3five4seven6fiveh   3 five => 35
        876mbxbrntsfm            8 6 => 86
        fivek5mfzrdxfbn66nine8eight five eight => 58
        554qdg                      5 4 => 54
        ninevsgxnine6threesix8      nine 8 => 98
        
        35+86+58+54+98 = 331

    """.trimIndent()

    part2(input).println()
}
