import kotlin.math.min

fun main() {
    val MAX_VAL = Int.MAX_VALUE.toLong() * 10
    fun mapOrders(input: List<String>): List<MutableList<Mapping>> {
        val maps = mutableMapOf<String, MutableList<Mapping>>()
        var i = 2
        while (i < input.size) {
            val title = input[i].split(" ")[0]
            maps[title] = mutableListOf()
            i++
            while (i < input.size && input[i].length > 2) {
                val nums = input[i].split(" ").map { it.toLong() }
                nums.forEach {
                    check(it < MAX_VAL)
                }
                maps[title]!!.add(Mapping(nums[0], nums[1], nums[2]))
                i++
            }
            i++
        }

        val mapOrder: List<MutableList<Mapping>> = listOf(
            maps["seed-to-soil"]!!,
            maps["soil-to-fertilizer"]!!,
            maps["fertilizer-to-water"]!!,
            maps["water-to-light"]!!,
            maps["light-to-temperature"]!!,
            maps["temperature-to-humidity"]!!,
            maps["humidity-to-location"]!!
        )

        return mapOrder
    }

    fun part1(input: List<String>): Long {
        val mapOrder = mapOrders(input)
        val seeds = input[0].split(": ")[1].split(" ").map { it.toLong() }
        var lowest: Long? = null
        for (seed in seeds) {
            var next = seed
            for (m in mapOrder) {
                for (mapping in m) {
                    if (next >= mapping.sourceRangeStart && next < mapping.sourceRangeStart + mapping.rangeLength) {
                        next = mapping.destRangeStart + (next - mapping.sourceRangeStart)
                        break
                    }
                }
            }
            if (lowest == null) {
                lowest = next
            }
            lowest = min(next, lowest)
        }

        return lowest!!
    }

    fun part2(input: List<String>): Long {
        val seeds = input[0].split(": ")[1].split(" ").map { it.toLong() }
        val seedRanges = ArrayDeque<SeedRange>()
        for (i in seeds.indices step 2) {
            seedRanges.add(SeedRange(seeds[i], seeds[i + 1]))
        }

        val mapOrder = mapOrders(input).map { it.toMutableList() }

        // fill gaps
        for (m in mapOrder) {
            m.sortBy { it.sourceRangeStart }
            val extras = mutableListOf<Mapping>()
            if (m[0].sourceRangeStart > 0) {
                extras.add(0, Mapping(0, 0, m[0].sourceRangeStart))
            }
            var i = 0
            while (i < m.size) {
                if (i < m.size - 1) {
                    val mapping = m[i]
                    val next = m[i + 1]
                    val end = (mapping.sourceRangeStart + mapping.rangeLength)
                    val length = next.sourceRangeStart - end
                    if (length > 0) {
                        extras.add(Mapping(end, end, length))
                    }
                }
                i += 1
            }
            m.addAll(extras)
            m.sortBy { it.sourceRangeStart }
            m.add(
                Mapping(
                    m.last().sourceRangeStart + m.last().rangeLength,
                    m.last().sourceRangeStart + m.last().rangeLength,
                    MAX_VAL
                )
            )
        }

        var lowest: Long = MAX_VAL
        while (seedRanges.isNotEmpty()) {
            val ranges = ArrayDeque<SeedRange>()
            ranges.add(seedRanges.removeFirst())
            for ((index, m) in mapOrder.withIndex()) {
                val numRanges = ranges.size
                val newRanges = ArrayDeque<SeedRange>()
                while (ranges.isNotEmpty()) {
                    val range = ranges.removeFirst()
                    for (mapping in m) {
                        val overlap = mapping.overlap(range)
                        if (overlap != null) {
                            newRanges.add(mapping.convertOverlap(overlap))
                        }
                    }
                }
                check(newRanges.size >= numRanges)
                ranges.addAll(newRanges)
            }
            val candidate = ranges.minOf { it.start }
            val newLowest = min(lowest, candidate)
            lowest = newLowest
        }

        return lowest
    }

    val sampleInput = readInput("day05-sample")
    val input = readInput("day05-input")

    println("part 1 sample input: ${part1(sampleInput)}")
    println("part 1 full input: ${part1(input)}")
    println("part 2 sample input: ${part2(sampleInput)}")
    println("part 2 full input: ${part2(input)}")
}

data class SeedRange(val start: Long, val size: Long)

data class Mapping(val destRangeStart: Long, val sourceRangeStart: Long, val rangeLength: Long) {
    fun contains(src: Mapping): Boolean {
        return contains(src.sourceRangeStart)
    }

    fun contains(src: Long): Boolean {
        return src >= sourceRangeStart && src < sourceRangeStart + rangeLength
    }

    fun overlap(src: SeedRange): SeedRange? {
        val start = src.start
        val end = src.start + src.size - 1
        val mStart = sourceRangeStart
        val mEnd = sourceRangeStart + rangeLength - 1
        return if (contains(src.start) && contains(end)) {
            SeedRange(src.start, src.size)
        } else if (contains(src.start)) {
            SeedRange(src.start, (sourceRangeStart + rangeLength) - src.start)
        } else if (contains(end)) {
            SeedRange(sourceRangeStart, src.size - (end - sourceRangeStart))
        } else if (mStart > start && mEnd < end) {
            SeedRange(mStart, rangeLength)
        } else {
            null
        }
    }

    fun convertOverlap(src: SeedRange): SeedRange {
        return SeedRange(destRangeStart + (src.start - sourceRangeStart), src.size)
    }
}