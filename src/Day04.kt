fun main() {
    val dayId = "04"
    val input = readInput("Day${dayId}")
    val ans = input.count { s ->
        val ss = s.split(",")
        val (a, b) = ss[0].split("-").map { it.toInt() }
        val (c, d) = ss[1].split("-").map { it.toInt() }
        a in c..d || b in c..d || c in a..b || d in a..b
    }
    println(ans)
}