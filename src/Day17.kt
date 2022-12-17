fun main() {
    val dayId = "17"
    val input = readInput("Day${dayId}")
    fun shape(si: Int, u: (Int, Int) -> Unit) {
        when (si) {
            0 -> {
                u(0, 0)
                u(0, 1)
                u(0, 2)
                u(0, 3)
            }
            1 -> {
                u(0, 1)
                u(1, 0)
                u(1, 1)
                u(1, 2)
                u(2, 1)
            }
            2 -> {
                u(0, 0)
                u(0, 1)
                u(0, 2)
                u(1, 2)
                u(2, 2)
            }
            3 -> {
                u(0, 0)
                u(1, 0)
                u(2, 0)
                u(3, 0)
            }
            4 -> {
                u(0, 0)
                u(0, 1)
                u(1, 0)
                u(1, 1)
            }
            else -> error(si)
        }
    }
    val f = ArrayList<BooleanArray>()
    fun test(si: Int, i0: Int, j0: Int): Boolean {
        if (i0 < 0) return false
        var ok = true
        shape(si) { di, dj ->
            val i = i0 + di
            val j = j0 + dj
            if (j !in 0..6) {
                ok = false
            } else {
                if (i in f.indices && f[i][j]) ok = false
            }
        }
        return ok
    }
    fun put(si: Int, i0: Int, j0: Int) {
        shape(si) { di, dj ->
            val i = i0 + di
            val j = j0 + dj
            while (i > f.lastIndex) f += BooleanArray(7)
            f[i][j] = true
        }
    }
    val p = input[0]
    var pj = 0
    val sn = 5
    var si = sn - 1
    fun mask(): Long {
        var m = 0L
        var k = 1L
        for (di in 0..7) for (j in 0..6) {
            val i = f.lastIndex - di
            if (i < 0 || f[i][j]) m = m or k
            k = k shl 1
        }
        return m
    }
    data class Upd(val m1: Long, val si: Int, val pj: Int, val ds: Int, val i: Int, val j: Int)
    val us = ArrayList<Upd>()
    val ui = HashMap<Upd, Int>()
    var ans = 0L
    val times = 1000000000000L // 2022 in Part 1
    fun rec(u: Upd): Boolean {
        val rn = us.size
        val prev = ui[u]
        if (prev == null) {
            ui[u] = rn
            us += u
            return false
        }
        val clen = rn - prev
        val rem = times - rn
        var ds0 = 0
        val iRest = prev + (rem % clen).toInt()
        for (k in 0 until rn) ds0 += us[k].ds
        for (k in prev until iRest) ds0 += us[k].ds
        var dsCycle = 0
        for (k in prev until rn) dsCycle += us[k].ds
        ans = ds0 + (rem / clen) * dsCycle
        return true
    }
    for (rn in 0 until times) {
        si = (si + 1) % sn
        val s0 = f.size
        var i = s0 + 3
        var j = 2
        check(test(si, i, j))
        while (true) {
            val d = p[pj]
            pj = (pj + 1) % p.length
            val dj = when (d) {
                '<' -> -1
                '>' -> 1
                else -> error(d)
            }
            if (test(si, i, j + dj)) j += dj
            if (!test(si, i - 1, j)) break
            i--
        }
        val m1 = mask()
        put(si, i, j)
        if (rec(Upd(m1, si, pj, f.size - s0, s0 - i, j))) break
    }
    println(ans)
}
