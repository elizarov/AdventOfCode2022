fun main() {
    val dayId = "05"
    val input = readInput("Day${dayId}")
    val t = 9
    val st = List(t + 1) { ArrayList<Char>() }
    var i = 0
    while (true) {
        val s = input[i++]
        if (s.startsWith(" 1")) break
        var k = 0
        for (j in 1 until s.length step 4) {
            val c = s[j]
            k++
            if (c !in 'A'..'Z') continue
            st[k].add(0, c)
        }
    }
    i++
    while (i < input.size) {
        val s = input[i++]
        val m = Regex("move (\\d+) from (\\d+) to (\\d+)").matchEntire(s)!!
        val n = m.groupValues[1].toInt()
        val a = m.groupValues[2].toInt()
        val b = m.groupValues[3].toInt()
        val c = st[a].subList(st[a].size - n, st[a].size)
        st[b] += c
        c.clear()
    }
    val ans = (1..t).joinToString("") { st[it].last().toString() }
    println(ans)
}
