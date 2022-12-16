// Fast solution that builds upon Part1
// n - number of interesting valves with r > 0
// m - number of connections
// t - number of time steps
//
//   O(t * 2^n * m) time to solve Part1
// + O(3^n)         time to solve Part2

fun main() {
    val dayId = "16"
    val input = readInput("Day${dayId}")
    class VD(val id: Int, val r: Int, val us: List<String>)
    var cnt = 0
    val vs = HashMap<String, VD>()
    for (s in input) {
        val (v, r, t) = Regex("Valve ([A-Z][A-Z]) has flow rate=(\\d+); tunnel[s]? lead[s]? to valve[s]? ([A-Z, ]+)")
            .matchEntire(s)!!.groupValues.drop(1)
        check(vs.put(v, VD(cnt++, r.toInt(), t.split(", "))) == null)
    }
    val tl = 26
    data class ST(val m: Long, val c: String)
    val dp = Array(tl + 1) { HashMap<ST, Int>() }
    fun put(t: Int, m: Long, c: String, p: Int) {
        val st = ST(m, c)
        val cur = dp[t][st]
        if (cur == null || p > cur) dp[t][st] = p
    }
    put(0, 0, "AA", 0)
    for (t in 0 until tl) {
        println("$t ${dp[t].size}")
        for ((st, p) in dp[t]) {
            val (m, c) = st
            val v = vs[c]!!
            val mask = 1L shl v.id
            if (v.r > 0 && (mask and m) == 0L) {
                put(t + 1, m or mask, c, p + (tl - t - 1) * v.r)
            }
            for (u in v.us) {
                put(t + 1, m, u, p)
            }
        }
    }
    val bm = dp[tl].toList()
        .groupingBy { it.first.m }
        .fold(0) { a, e -> maxOf(a, e.second) }
    val g = vs.values.filter { it.r > 0 }.map { it.id }
    fun find(i: Int, m1: Long, m2: Long): Int {
        if (i == g.size) {
            val b1 = bm[m1] ?: return 0
            val b2 = bm[m2] ?: return 0
            return b1 + b2
        }
        val r1 = find(i + 1, m1, m2)
        val r2 = find(i + 1, m1 or (1L shl g[i]), m2)
        val r3 = find(i + 1, m1, m2 or (1L shl g[i]))
        return maxOf(r1, r2, r3)
    }
    val ans = find(0, 0, 0)
    println(ans)
}
