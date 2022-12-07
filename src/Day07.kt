private const val CD = "$ cd "

fun main() {
    val dayId = "07"
    val input = readInput("Day${dayId}")
    class Dir(val up: Dir? = null) {
        val sd = HashMap<String, Dir>()
        val fs = HashMap<String, Long>()
        var sum = 0L
    }
    val root = Dir()
    var cd = root
    for (c in input) {
        when {
            c.startsWith(CD) -> {
                val d = c.substringAfter(CD).trim()
                when (d) {
                    "/" -> cd = root
                    ".." -> cd = cd.up!!
                    else -> cd = cd.sd.getOrPut(d) { Dir(cd) }
                }
            }
            c.startsWith("$ ls") -> {}
            else -> {
                val (dsz, name) = c.split(" ")
                if (dsz == "dir") {
                    cd.sd.getOrPut(name) { Dir(cd) }
                } else {
                    cd.fs[name] = dsz.toLong()
                }
            }
        }
    }
    var ans = 0L
    val ds = ArrayList<Dir>()
    fun scan(cd: Dir): Long {
        var sum = 0L
        for (d in cd.sd.values) sum += scan(d)
        sum += cd.fs.values.sum()
        if (sum <= 100000) ans += sum
        cd.sum = sum
        ds += cd
        return sum
    }
    val all = scan(root)
    println(ans)
    val total = 70000000L
    val need = 30000000L
    ds.sortBy { it.sum }
    val maxAll = total - need
    check(all > maxAll)
    val del = all - maxAll
    val dd = ds.find { it.sum >= del }
    println(dd!!.sum)
}
