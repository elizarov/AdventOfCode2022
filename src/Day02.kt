fun main() {
    val dayId = "02"
    val input = readInput("Day${dayId}")
    val ans = input.map { l ->
        val o = l[0] - 'A'
        val y = when (l[2]) {
            'X' -> (o + 2) % 3
            'Y' -> o
            'Z' -> (o + 1) % 3
            else -> error("!!")
        }
        var s = y + 1
        if (o == y) {
            s += 3
        } else if (y == (o + 1) % 3) {
            s += 6
        }
        s
    }.sum()
    println(ans)
}
