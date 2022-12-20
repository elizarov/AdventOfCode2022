fun main() {
    day20part(1)
    day20part(2)
}

fun day20part(part: Int) {
    val dayId = "20"
    val input = readInput("Day${dayId}")
    val mul = if (part == 1) 1L else 811589153L
    val rounds = if (part == 1) 1 else 10
    data class Num(var p: Int, val x: Long)
    val a = input.mapIndexed { p, s -> Num(p, s.toLong() * mul) }
    val n = a.size
    val c = a.toMutableList()
    repeat(rounds) {
        for (num in a) {
            val (p, x) = num
            c.removeAt(p)
            for (i in p until n - 1) c[i].p--
            val j = (p + x).mod(n - 1)
            num.p = j
            c.add(j, num)
            for (i in j + 1 until n) c[i].p++
        }
    }
    var ans = 0L
    val k0 = c.indexOfFirst { it.x == 0L }
    for (i in 1..3) {
        ans += c[(k0 + i * 1000) % n].x
    }
    println("part$part = $ans")
}
