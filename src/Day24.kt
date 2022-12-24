fun main() {
    day24Part(1)
    day24Part(2)
}

fun day24Part(part: Int) {
    val dayId = "24"
    val input = readInput("Day${dayId}")
    val a = input.toCharArray2()
    val (n, m) = a.size2()
    val (di, dj) = RDLU_DIRS
    data class B(val i: Int, val j: Int, val d: Int)
    val bs = a.mapIndexed2NotNull { i, j, c ->
        val d = when (c) {
            '>' -> 0
            'v' -> 1
            '<' -> 2
            '^' -> 3
            '#', '.' -> -1
            else -> error(a[i][j])
        }
        if (d >= 0) B(i, j, d) else null
    }
    var pc = HashSet<P2>()
    var pn = HashSet<P2>()
    var s = P2(0, 1)
    var f = P2(n - 1, m - 2)
    pc += s
    val totalPhases = if (part == 1) 0 else 2
    var phase = 0
    var t = 0
    while (phase < totalPhases || f !in pc) {
        if (f in pc) {
            phase++
            pc.clear()
            f = s.also { s = f }
            pc += s
        }
        val bn = bs.map { b ->
            P2(
                (b.i + di[b.d] * (t + 1) - 1).mod(n - 2) + 1,
                (b.j + dj[b.d] * (t + 1) - 1).mod(m - 2) + 1
            )
        }.toSet()
        for (p in pc) {
            for (d in 0..3) {
                val p1 = P2(p.i + di[d], p.j + dj[d])
                if (p1 in bn) continue
                if (p1.i in 0 until n && p1.j in 0 until m && a[p1.i][p1.j] != '#') {
                    pn += p1
                }
            }
            if (p !in bn) pn += p
        }
        pc = pn.also { pn = pc }
        pn.clear()
        t++
    }
    println("part$part = $t")
}
