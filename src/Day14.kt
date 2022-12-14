fun main() {
    val dayId = "14"
    val input = readInput("Day${dayId}")
    data class P(val x: Int, val y: Int)
    val ps = input.map { s ->
        s.split(" -> ").map { t ->
            val (x, y) = t.split(",").map { it.toInt() }
            P(x, y)
        }
    }
    val pf = ps.flatten() + P(500, 0)
    val y0 = pf.minOf { it.y }
    val y1 = pf.maxOf { it.y } + 1
    val h = y1 - y0 + 10
    val x0 = pf.minOf { it.x } - h
    val x1 = pf.maxOf { it.x } + h
    val f = Array(x1 - x0 + 1) { BooleanArray(y1 - y0 + 1) }
    fun set(x: Int, y: Int) { f[x - x0][y - y0] = true }
    fun get(x: Int, y: Int): Boolean {
        if (x !in x0..x1 || y !in y0..y1) return false
        return f[x - x0][y - y0]
    }
    for (p in ps) {
        for (i in 1..p.lastIndex) {
            val a = p[i - 1]
            val b = p[i]
            when {
                a.x == b.x -> {
                    for (y in minOf(a.y, b.y)..maxOf(a.y, b.y)) set(a.x, y)
                }
                a.y == b.y -> {
                    for (x in minOf(a.x, b.x)..maxOf(a.x, b.x)) set(x, a.y)
                }
                else -> error("$a $b")
            }
        }
    }
    var ans = 0
    while (true) {
        var x = 500
        var y = 0
        if (get(x, y)) break
        while (true) {
            if (y == y1) break
            when {
                !get(x, y + 1) -> { y++ }
                !get(x - 1, y + 1) -> { x--; y++ }
                !get(x + 1, y + 1) -> { x++; y++ }
                else -> break
            }
        }
        set(x, y)
        ans++
    }
    println(ans)
}
