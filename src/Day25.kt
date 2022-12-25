fun main() {
    val dayId = "25"
    val input = readInput("Day${dayId}")
    val sum = input.sumOf { s ->
        var r = 0L
        for (c in s) {
            r *= 5
            r += when (c) {
                '-' -> -1
                '=' -> -2
                else -> c - '0'
            }
        }
        r
    }
    println(sum)
    var r = sum
    val b = StringBuilder()
    while (r != 0L) {
        val d = (r + 2).mod(5) - 2
        when (d) {
            -1 -> '-'
            -2 -> '='
            else -> '0' + d
        }.let { b.append(it) }
        r -= d
        r /= 5
    }
    b.reverse()
    println(b)
}
