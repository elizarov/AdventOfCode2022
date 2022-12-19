import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

fun main() {
    day19Part(1)
    day19Part(2)
}

@OptIn(ExperimentalTime::class)
fun day19Part(part: Int) {
    println("=============== Solving part$part")
    val start = TimeSource.Monotonic.markNow()
    val dayId = "19"
    val input = readInput("Day${dayId}")
    var ans = if (part == 1) 0 else 1
    val tl = if (part == 1) 24 else 32
    val r = listOf("ore", "clay", "obsidian", "geode")
    val rc = r.size
    var totalStates = 0
    for ((id0, ss) in input.withIndex()) {
        val name = "Blueprint ${id0 + 1}: "
        println(name)
        check(ss.startsWith(name))
        val split = ss.substringAfter(": ").split(".")
        val ds = Array(rc) { i ->
            val prefix = "Each ${r[i]} robot costs "
            val si = split[i].trim()
            check(si.startsWith(prefix)) { "$si, expect=$prefix" }
            val rem = si.substring(prefix.length).split(" ")
            val d = IntArray(rc)
            for (j in 1 until rem.size) if (rem[j] in r) {
                val k = r.indexOf(rem[j])
                d[k] = rem[j - 1].toInt()
            }
            d
        }
        val max0 = ds.maxOf { it[0] }
        var cs = LHS()
        var ns = LHS()
        cs.add(1L)
        fun produce(n: Long): Long {
            var nn = n
            for (j in 0 until rc) nn = nn.plus(rc + j, n[j])
            return nn
        }
        for (t in 1..tl) {
            val rem = tl - t
            fun Long.gg(): Int {
                val g = this[2 * rc - 1]
                val gr = this[rc - 1]
                return g + rem * gr
            }
            var bestGG = -1
            cs.forEach { s ->
                bestGG = maxOf(bestGG, s.gg())
            }
            cs.forEach { s ->
                totalStates++
                fun addNext(n: Long) {
                    val g = n[2 * rc - 1]
                    val gr = n[rc - 1]
                    val gg = g + rem * gr
                    val estG = gg + rem * (rem - 1) / 2
                    if (estG <= bestGG) return
                    ns.add(n)
                }
                var canBuildCnt = 0
                val isMax0 = s[0] >= max0
                val i0 = if (isMax0) 1 else 0
                build@for (i in i0 until rc) {
                    val d = ds[i]
                    for (j in 0 until rc) if (s[rc + j] < d[j]) continue@build
                    canBuildCnt++
                    var n = s
                    for (j in 0 until rc) n = n.minus(rc + j, d[j])
                    n = produce(n)
                    n = n.plus(i, 1)
                    addNext(n)
                }
                if (canBuildCnt < rc - i0) {
                    addNext(produce(s))
                }
            }
            println("$t -> ${ns.size}")
            cs = ns.also { ns = cs }
            ns.clear()
        }
        var best = 0
        cs.forEach { s ->
            best = maxOf(best, s[2 * rc - 1])
        }
        println("== $best")
        if (part == 1) {
            ans += (id0 + 1) * best
        } else {
            ans *= best
            if (id0 == 2) break
        }
    }
    println("---------------> part$part = $ans; " +
            "analyzed $totalStates states in ${start.elapsedNow().toString(DurationUnit.SECONDS, 3)} sec")
}

private const val SH = 6
private const val MASK = (1L shl SH) - 1
private operator fun Long.get(i: Int): Int = ((this shr (i * SH)) and MASK).toInt()
private fun Long.plus(i: Int, d: Int): Long = this + (d.toLong() shl (i * SH))
private fun Long.minus(i: Int, d: Int): Long = this - (d.toLong() shl (i * SH))

private const val MAGIC = 0x9E3779B9.toInt()
private const val TX = ((2/3.0) * (1L shl 32)).toLong().toInt()

class LHS(var shift: Int = 29) {
    var a = LongArray(1 shl (32 - shift))
    var size = 0
    var tx = TX ushr shift

    fun clear() {
        a.fill(0L)
        size = 0
    }

    fun add(k: Long) {
        require(k != 0L)
        if (size >= tx) rehash()
        val hc0 = k.toInt() * 31 + (k ushr 32).toInt()
        var i = (hc0 * MAGIC) ushr shift
        while (true) {
            if (a[i] == 0L) {
                a[i] = k
                size++
                return
            }
            if (a[i] == k) return
            if (i == 0) i = a.size
            i--
        }
    }

    inline fun forEach(op: (Long) -> Unit) {
        for (i in 0 until a.size) {
            if (a[i] != 0L) op(a[i])
        }
    }

    private fun rehash() {
        val b = LHS(shift - 1)
        forEach { k -> b.add(k) }
        check(b.size == size)
        shift = b.shift
        a = b.a
        tx = b.tx
    }
}