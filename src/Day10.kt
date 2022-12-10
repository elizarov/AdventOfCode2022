fun main() {
    val dayId = "10"
    val input = readInput("Day${dayId}")
    var ans = 0
    var i = 1
    var x = 1
    val a = Array(6) { CharArray(40) { '.' } }
    fun check() {
        val pos = (i - 1) % 40
        if (x in pos - 1 .. pos + 1) {
            a[(i - 1) / 40][pos] = '#'
        }
        if (i in listOf(20, 60, 100, 140, 180, 220)) {
            ans += x * i
        }
    }
    for (s in input) {
        val ss = s.split(" ")
        var il = 0
        var inc = 0
        when(ss[0]) {
            "noop" -> il = 1
            "addx" -> {
                il = 2
                inc = ss[1].toInt()
            }
        }
        repeat(il) {
            check()
            i++
        }
        x += inc
    }
    println(ans)
    a.forEach { println(it.concatToString()) }
}
