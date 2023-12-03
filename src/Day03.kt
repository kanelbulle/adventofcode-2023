fun main() {
    fun part1(input: List<String>): Int {
        val data = input.map { it.toCharArray() }
        var row = 0
        var sum = 0
        while (row < input.size) {
            var col = 0
            while (col < data[row].size) {
                val digits : MutableList<Char> = mutableListOf()
                val lCol = col
                while (col < data[row].size && data[row][col].isDigit()) {
                    digits.add(data[row][col])
                    col++
                }
                if (digits.size > 0) {
                    // Found a number, now see if symbol exists
                    if (hasNeighbor(data, row, lCol, lCol + digits.size - 1) {
                            !it.isDigit() && it != '.'
                        }) {
                        val found = digits.joinToString("").toInt()
                        sum += found
                    }
                }
                col++
            }
            row++
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val data = input.map { it.toCharArray() }
        var row = 0
        var sum = 0
        while (row < input.size) {
            var col = 0
            while (col < data[row].size) {
                if (data[row][col] == '*') {
                    val l = findDigit(data, row, col+1)
                    val r = findDigit(data, row, col-1)
                    val ld = findDigit(data, row+1, col-1)
                    val md = findDigit(data, row+1, col)
                    val rd = findDigit(data, row+1, col+1)
                    val lu = findDigit(data, row-1, col-1)
                    val mu = findDigit(data, row-1, col)
                    val ru = findDigit(data, row-1, col+1)
                    val digits = listOfNotNull(l, r, ld, md, rd, lu, mu, ru).distinct()
                    if (digits.size == 2) {
                        sum += digits[0].value * digits[1].value
                    }
                }
                col++
            }
            row++
        }
        return sum
    }

    val sampleInput = readInput("day03-sample")
    val input = readInput("day03-input")

    println("part 1 sample input: ${part1(sampleInput)}")
    println("part 1 full input: ${part1(input)}")
    println("part 2 sample input: ${part2(sampleInput)}")
    println("part 2 full input: ${part2(input)}")
}

fun findDigit(data: List<CharArray>, row: Int, startCol: Int) : Digit? {
    if (!inBounds(data, row, startCol) || !data[row][startCol].isDigit()) {
        return null
    }
    val digits : MutableList<Char> = mutableListOf()
    var lCol = startCol
    while (inBounds(data, row, lCol-1) && data[row][lCol-1].isDigit()) {
        lCol--
    }
    var col = lCol
    while (col < data[row].size && data[row][col].isDigit()) {
        digits.add(data[row][col])
        col++
    }
    check(digits.size > 0)
    return Digit(row, lCol, digits.joinToString("").toInt())
}

data class Digit(val row: Int, val col: Int, val value: Int)

fun hasNeighbor(data: List<CharArray>, row: Int, lCol: Int, rCol: Int, matcher: (Char) -> Boolean) : Boolean {
    for (r in row-1..row+1) {
        for (col in lCol-1..rCol+1) {
            if (inBounds(data, r, col)) {
                if (matcher(data[r][col])) {
                    return true
                }
            }
        }
    }
    return false
}

fun inBounds(data: List<CharArray>, row: Int, col: Int) : Boolean {
    return row >= 0 && col >= 0 && row < data.size && col < data[row].size
}
