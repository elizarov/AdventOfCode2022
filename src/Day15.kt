import kotlin.math.*

fun main() {
    val dayId = "15"
    val input = readInput("Day${dayId}")
    data class SB(val sx: Int, val sy: Int, val bx: Int, val by: Int)
    val sbs = input.map { s ->
        val (sx, sy, bx, by) = Regex("Sensor at x=([-0-9]+), y=([-0-9]+): closest beacon is at x=([-0-9]+), y=([-0-9]+)")
            .matchEntire(s)!!.groupValues.drop(1).map { it.toInt() }
        SB(sx, sy, bx, by)
    }
    val maxC = 4000000
    data class Ev(val x: Int, val d: Int)
    val es = ArrayList<Ev>()
    for (ty in 0..maxC) {
        es.clear()
        for (sb in sbs) with(sb) {
            val d = abs(sx - bx) + abs(sy - by)
            if (abs(ty - sy) <= d) {
                val w = d - abs(ty - sy)
                es += Ev(sx - w, 1)
                es += Ev(sx + w + 1, -1)
            }
//            if (sb.by == ty) {
//                es += Ev(sb.bx, -1)
//                es += Ev(sb.bx + 1, 1)
//            }
        }
        es.sortWith(compareBy(Ev::x, Ev::d))
        var px = es[0]!!.x
        var cnt = 0
        for (e in es) {
            if (e.x > px) {
                if (cnt == 0 && px in 0..maxC) {
                    println("$px $ty")
                    println(px * 4000000L + ty)
                }
                px = e.x
            }
            cnt += e.d
        }
    }
}