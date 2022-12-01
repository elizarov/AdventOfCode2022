fun main() {
    val dayId = "01"
    val input = readInput("Day${dayId}")
    val ans = input
        .parts { it.map { it.toLong() }.sum() }
        .sortedDescending()
        .take(3)
        .sum()
    println(ans)
}
