private const val OLD = -1

fun main() {
    val dayId = "11"
    val input = readInput("Day${dayId}")

    class R(var r: IntArray)

    class Monkey(
        val its: ArrayList<R>,
        val oc: Char,
        val z: Int,
        val d: Int,
        val tt: Int,
        val tf: Int
    ) {
        var cnt: Int = 0
    }

    val ms = input.parts { s ->
        val si = s[1].substringAfter("Starting items: ").split(", ").map { it.toInt() }
        val op = s[2].substringAfter("Operation: new = old ")
        val oc = op[0]
        val zz = op.substring(2)
        val z = if (zz == "old") OLD else zz.toInt()
        val d = s[3].substringAfter("Test: divisible by ").toInt()
        val tt = s[4].substringAfter("If true: throw to monkey ").toInt()
        val tf = s[5].substringAfter("If false: throw to monkey ").toInt()
        Monkey(ArrayList(si.map { R(intArrayOf(it)) }), oc, z, d, tt, tf)
    }
    val ds = ms.map { it.d }
    println(ds)
    val n = ds.size
    for (m in ms) {
        for (i in m.its) {
            val b = IntArray(n)
            for (j in 0 until n) {
                b[j] = i.r[0] % ds[j]
            }
            i.r = b
        }
    }
    repeat(10000) {
        for (m in ms) {
            for (i in m.its) {
                m.cnt++
                for (j in 0 until n) {
                    var w = i.r[j]
                    val z = if (m.z == OLD) w else m.z
                    when (m.oc) {
                        '*' -> w *= z
                        '+' -> w += z
                        else -> error(m.oc)
                    }
                    w %= ds[j]
                    i.r[j] = w
                }
                val j = ds.indexOf(m.d)
                when (i.r[j]) {
                    0 -> ms[m.tt].its += i
                    else -> ms[m.tf].its += i
                }
            }
            m.its.clear()
        }
    }
    val (a, b) = ms.sortedByDescending { it.cnt }.take(2)
    val ans = a.cnt.toLong() * b.cnt
    println(ans)
}
