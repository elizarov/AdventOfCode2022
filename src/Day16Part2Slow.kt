// Slow solution that keeps state which is too big
// Highly optimized, still works slowly
//
// n - number of interesting valves with r > 0
// N - total number of valves
// m - number of connections
// t - number of time steps
//
//   O(2^n * N^2) memory
//   O(t * 2^n * m^2) time

const val s1 = 16
const val s2 = 16 + 6
const val cdm = (1 shl 6) - 1

const val tl = 26

fun main() {
    val dayId = "16"
    val input = readInput("Day${dayId}")
    class VD(val idM: Int, val num: Int, val r: Int, val us: List<String>, val ns: ArrayList<Int> = ArrayList())
    var cntM = 0
    var num = 1
    val vs = HashMap<String, VD>()
    val ns = arrayOfNulls<String>(64)
    val vns = arrayOfNulls<VD>(64)
    for (s in input) {
        val (c, rs, t) = Regex("Valve ([A-Z][A-Z]) has flow rate=(\\d+); tunnel[s]? lead[s]? to valve[s]? ([A-Z, ]+)")
            .matchEntire(s)!!.groupValues.drop(1)
        val r = rs.toInt()
        val id = if (r > 0) cntM++ else 0
        val n = num++
        val v = VD(id, n, r, t.split(", "))
        check(vs.put(c, v) == null)
        ns[n] = c
        vns[n] = v
    }
    println(cntM)
    println(num)

    for (n in 1 until num) {
        val v = vns[n]!!
        for (u in v.us) v.ns.add(vs[u]!!.num)
    }

    val dp = Array(tl + 1) { HM() }
    fun pack(m: Int, cn: Int, dn: Int): Int = m or (cn shl s1) or (dn shl s2)
    fun unpackM(st: Int): Int = st and ((1 shl s1) - 1)
    fun unpackCN(st: Int): Int = (st shr s1) and cdm
    fun unpackDN(st: Int): Int = (st shr s2) and cdm

    fun put(t: Int, m: Int, cn: Int, dn: Int, p: Int) {
        if (t > tl) return
        val st = pack(m, cn, dn)
        val cur = dp[t][st]
        if (cur == -1 || p > cur) dp[t][st] = p
    }
    fun put(t: Int, m: Int, c: String, d: String, p: Int) {
        put(t, m, vs[c]!!.num, vs[d]!!.num, p)
    }

    put(0, 0, "AA", "AA",0)
    for (t in 0 until tl) {
        println("$t ${dp[t].size}")
        dp[t].forEach { st, p ->
            val m = unpackM(st)
            val cn = unpackCN(st)
            val dn = unpackDN(st)
            val vc = vns[cn]!!
            val vd = vns[dn]!!
            val maskc = 1 shl vc.idM
            val maskd = 1 shl vd.idM
            if (vc.r > 0 && (maskc and m) == 0) {
                put(t + 1, m or maskc, cn, dn, p + (tl - t - 1) * vc.r)
                for (ud in vd.ns) {
                    put(t + 1, m or maskc, cn, ud, p + (tl - t - 1) * vc.r)
                }
                if (cn != dn && vd.r > 0 && (maskd and m) == 0) {
                    put(t + 1, m or maskc or maskd, cn, dn,p + (tl - t - 1) * (vc.r + vd.r))
                }
            }
            if (vd.r > 0 && (maskd and m) == 0) {
                put(t + 1, m or maskd, cn, dn,p + (tl - t - 1) * vd.r)
                for (uc in vc.ns) {
                    put(t + 1, m or maskd, uc, dn,p + (tl - t - 1) * vd.r)
                }
            }
            for (uc in vc.us) {
                for (ud in vd.us) {
                    put(t + 1, m, uc, ud, p)
                }
            }
        }
        dp[t].dispose()
    }
    var ans = 0
    dp[tl].forEach { _, v -> if (v > ans) ans = v }
    println(ans)
}

private const val MAGIC = 0x9E3779B9.toInt()
private const val TX = ((2/3.0) * (1L shl 32)).toLong().toInt()

class HM(var shift: Int = 25) {
    var a = IntArray(1 shl (33 - shift))
    var size = 0
    var tx = TX ushr shift

    fun dispose() {
        a = intArrayOf()
    }

    operator fun get(k: Int): Int {
        var i = ((k * MAGIC) ushr shift) shl 1
        while (true) {
            if (a[i] == 0) return -1
            if (a[i] == k) return a[i + 1]
            if (i == 0) i = a.size
            i -= 2
        }
    }

    operator fun set(k: Int, v: Int) {
        require(k != 0)
        if (size >= tx) rehash()
        var i = ((k * MAGIC) ushr shift) shl 1
        while (true) {
            if (a[i] == 0) {
                a[i] = k
                a[i + 1] = v
                size++
                return
            }
            if (a[i] == k) {
                a[i + 1] = v
                return
            }
            if (i == 0) i = a.size
            i -= 2
        }
    }

    inline fun forEach(op: (Int, Int) -> Unit) {
        for (i in 0 until a.size step 2) {
            if (a[i] != 0) op(a[i], a[i+1])
        }
    }

    private fun rehash() {
        val b = HM(shift - 1)
        forEach { k, v -> b[k] = v }
        check(b.size == size)
        shift = b.shift
        a = b.a
        tx = b.tx
    }
}