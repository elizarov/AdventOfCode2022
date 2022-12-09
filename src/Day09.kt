import kotlin.math.*

fun main() {
    val dayId = "09"
    val input = readInput("Day${dayId}")
    data class P(val x: Int, val y: Int)
    val v = HashSet<P>()
    v.add(P(0, 0))
    val k = 10
    val x = IntArray(k)
    val y = IntArray(k)
    for (s in input) {
        val ss = s.split(" ")
        val d = ss[0]
        val c = ss[1].toInt()
        repeat(c) {
            when (d[0]) {
                'U' -> y[0]--
                'D' -> y[0]++
                'R' -> x[0]++
                'L' -> x[0]--
                else -> error("$d")
            }
            for (i in 1 until k) {
                val dx = x[i - 1] - x[i]
                val dy = y[i - 1] - y[i]
                if (abs(dx) > 1 || abs(dy) > 1) {
                    x[i] += dx.sign
                    y[i] += dy.sign
                }
            }
            v.add(P(x[k - 1], y[k - 1]))
        }
    }
    println(v.size)
}
