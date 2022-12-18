fun main() {
    val dayId = "18"
    val input = readInput("Day${dayId}")
    val cs = input.map { s -> s.split(",").map { it.toInt() }}.toHashSet()
    var ans = 0
    val mins = List(3) { i -> cs.map { it[i] }.min() }
    val maxs = List(3) { i -> cs.map { it[i] }.max() }
    val v = HashMap<List<Int>, Boolean>()
    fun scan(c: List<Int>, vv: HashSet<List<Int>>): Boolean {
        v[c]?.let { return it }
        for (i in 0..2) {
            if (c[i] !in mins[i]..maxs[i]) return true
        }
        v[c] = false
        vv += c
        for (i in 0..2) for (jj in 0..1) {
            val j = jj * 2 - 1
            val t = c.toMutableList()
            t[i] += j
            if (t in cs) continue
            if (scan(t, vv)) {
                return true
            }
        }
        return false
    }

    for (c in cs) {
        for (i in 0..2) for (jj in 0..1) {
            val j = jj * 2 - 1
            val t = c.toMutableList()
            t[i] += j
            if (t !in cs) {
                val vv: HashSet<List<Int>> = HashSet()
                if (scan(t, vv)) {
                    for (cc in vv) v[cc] = true
                    ans++
                }
            }
        }
    }
    println(ans)
}
