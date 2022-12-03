fun main() {
    val dayId = "03"
    val input = readInput("Day${dayId}")
    val ans = input.chunked(3).map { s ->
        val s1 = s[0].toSet()
        val s2 = s[1].toSet()
        val s3 = s[2].toSet()
        val c = (s1 intersect s2 intersect  s3).single()
        val p = if (c in 'a'..'z') c - 'a' + 1 else c - 'A' + 27
        p
    }.sum()
    println(ans)
}
