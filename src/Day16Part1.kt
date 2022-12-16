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
    val tl = 30
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
    val ans = dp[tl].map { it.value }.max()
    println(ans)
}
