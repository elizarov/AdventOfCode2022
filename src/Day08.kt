fun main() {
    val dayId = "08"
    val input = readInput("Day${dayId}")
    val a = input.map { s -> s.map { it.digitToInt() } }
    val n = a.size
    val m = a[0].size
    // part 1
    val v = Array(n) { BooleanArray(m) }
    for (i in 0 until n) {
        var h = -1
        for (j in 0 until m) {
            if (a[i][j] > h) {
                h = a[i][j]
                v[i][j] = true
            }
        }
        h = -1
        for (j in m - 1 downTo 0) {
            if (a[i][j] > h) {
                h = a[i][j]
                v[i][j] = true
            }
        }
    }
    for (j in 0 until m) {
        var h = -1
        for (i in 0 until n) {
            if (a[i][j] > h) {
                h = a[i][j]
                v[i][j] = true
            }
        }
        h = -1
        for (i in n - 1 downTo 0) {
            if (a[i][j] > h) {
                h = a[i][j]
                v[i][j] = true
            }
        }
    }
    println(v.sumOf { it.count { it } })
    // part 2
    var ans2 = 0
    for (i in 0 until n) {
        for (j in 0 until m) {
            var d1 = j
            for (k in j - 1 downTo 0) {
                if (a[i][k] >= a[i][j]) {
                    d1 = j - k
                    break
                }
            }
            var d2 = m - j - 1
            for (k in j + 1 until m) {
                if (a[i][k] >= a[i][j]) {
                    d2 = k - j
                    break
                }
            }
            var d3 = i
            for (k in i - 1 downTo 0) {
                if (a[k][j] >= a[i][j]) {
                    d3 = i - k
                    break
                }
            }
            var d4 = n - i - 1
            for (k in i + 1 until n) {
                if (a[k][j] >= a[i][j]) {
                    d4 = k - i
                    break
                }
            }
            val f = d1 * d2 * d3 * d4
            if (f > ans2) ans2 = f
        }
    }
    println(ans2)
}
