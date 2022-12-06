fun main() {
    val dayId = "06"
    val input = readInput("Day${dayId}")
    val s = input[0]
    val k = 14
    for (i in 0 until s.length - k) {
        val q = s.substring(i, i + k).toSet()
        if (q.size == k) {
            println(i + k)
            break
        }
    }
}
