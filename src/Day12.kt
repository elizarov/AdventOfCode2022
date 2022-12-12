fun main() {
    val dayId = "12"
    val a = readInput("Day${dayId}").map { it.toCharArray() }
    val n = a.size
    val m = a[0].size
    data class P(val i: Int, val j: Int)
    var e: P? = null
    var s: P? = null
    for (i in 0 until n) for (j in 0 until m) {
        when (a[i][j]) {
            'E' -> e = P(i, j)
            'S' -> s = P(i, j)
        }
    }
    check(e != null)
    check(s != null)
    val q = ArrayDeque<P>()
    val v = HashMap<P, Int>()
    fun enq(i: Int, j: Int, d: Int) {
        val p = P(i, j)
        if (p in v) return
        v[p] = d
        q += p
    }
    fun el(h: Char): Int = when(h) {
        'S' -> 0
        'E' -> 'z' - 'a'
        else -> h - 'a'
    }
    fun go(i: Int, j: Int, d: Int, h: Int) {
        if (i !in 0 until n || j !in 0 until m) return
        val t = el(a[i][j])
        if (h <= t + 1) enq(i, j, d)
    }
    enq(e.i, e.j, 0)
    while (q.isNotEmpty()) {
        val p = q.removeFirst()
        val d = v[p]!! + 1
        val h = el(a[p.i][p.j])
        go(p.i - 1, p.j, d, h)
        go(p.i + 1, p.j, d, h)
        go(p.i, p.j - 1, d, h)
        go(p.i, p.j + 1, d, h)
    }
    var ans = Int.MAX_VALUE
    for (i in 0 until n) for (j in 0 until m) {
        if (a[i][j] == 'a') {
            val d = v[P(i, j)]
            if (d != null) ans = minOf(ans, d)
        }
    }
    println(ans)
}
