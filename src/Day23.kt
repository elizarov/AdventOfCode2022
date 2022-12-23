fun main() {
    day23Part(1)
    day23Part(2)
}

fun day23Part(part: Int) {
    val dayId = "23"
    val input = readInput("Day${dayId}")
    data class P(val i: Int, val j: Int)
    val ps = HashSet<P>()
    for (i in input.indices) for (j in input[i].indices) if (input[i][j] == '#') {
        ps += P(i, j)
    }
    // NSWE
    val di = listOf(-1, 1, 0, 0)
    val dj = listOf(0, 0, -1, 1)
    val adj1 = listOf(2, 2, 0, 0)
    val adj2 = listOf(3, 3, 1, 1)
    var round = 0
    fun P.nextIn(d: Int) = P(i + di[d], j + dj[d])
    fun P.neighbours(d: Int) : List<P> = buildList {
        val d1 = adj1[d]
        val d2 = adj2[d]
        add(nextIn(d))
        add(P(i + di[d] + di[d1], j + dj[d] + dj[d1]))
        add(P(i + di[d] + di[d2], j + dj[d] + dj[d2]))
    }
    while (true) {
        val plan = ps.associateWith { p ->
            val ds = (0..3).map { (round + it) % 4 }.filter { d -> p.neighbours(d).none { it in ps } }
            if (ds.size in 1..3) p.nextIn(ds[0]) else p
        }.filter { it.key != it.value }
        val good = plan.values.groupingBy { it }.eachCount().filter { it.value == 1 }.keys
        for (p in ps.toList()) {
            val p1 = plan[p] ?: continue
            if (p1 !in good) continue
            ps -= p
            ps += p1
        }
        round++
        when (part) {
            1 -> if (round == 10) break
            2 -> if (good.isEmpty()) break
        }
    }
    if (part == 1) {
        val i0 = ps.minOf { it.i }
        val i1 = ps.maxOf { it.i }
        val j0 = ps.minOf { it.j }
        val j1 = ps.maxOf { it.j }
        val ans = (i1 - i0 + 1) * (j1 - j0 + 1) - ps.size
        println("part1 = $ans")
    } else {
        println("part2 = $round")
    }
}
