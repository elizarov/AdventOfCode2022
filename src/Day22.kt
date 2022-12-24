// Note: Part2 in this code works only on the actual input and does not work on the example,
// because its cube-folding is hard-coded

fun main() {
    day22Part(1)
    day22Part(2)
}

fun day22Part(part: Int) {
    val dayId = "22"
    val input = readInput("Day${dayId}")
    val (aa, ss) = input.parts { it }
    val a = aa.map { it.toCharArray() }
    val s = ss[0]
    var p = 0
    var i = 0
    var j = a[0].indexOf('.').also { check(it >= 0) }
    var d = 0
    val (di, dj) = RDLU_DIRS

    data class MC(val i: Int, val j: Int, val d: Int)
    // --------------- MANUALLY FOLDED CUBE FOR THE TEST INPUT ---------------
    val mc = mutableMapOf(
        MC(0, 1 ,2) to MC(2, 0, 0), // flip
        MC(0, 1, 3) to MC(3, 0, 0),
        MC(0, 2,0) to MC(2, 1, 2), // flip
        MC(0, 2, 1) to MC(1, 1, 2),
        MC(0, 2, 3) to MC(3, 0, 3),
        MC(1, 1, 2) to MC(2, 0, 1),
        MC(3, 0, 0) to MC(2, 1, 3),
    )
    // ----------------------------------------------------------------------

    for ((u, v) in mc.toList()) {
        mc[v.copy(d = (v.d + 2) % 4)] = u.copy(d = (u.d + 2) % 4)
    }
    while (p < s.length) {
        when (s[p]) {
            in '0'..'9' -> {
                var x = s[p++] - '0'
                while (p < s.length && s[p] in '0'..'9') {
                    x = x * 10 + (s[p++] - '0')
                }
                move@for (step in 1..x) {
                    var i1 = i + di[d]
                    var j1 = j + dj[d]
                    var d1 = d
                    if (i1 !in a.indices || j1 !in a[i1].indices || a[i1][j1] == ' ') {
                        if (part == 1) {
                            // Part1 wrapping
                            when (d) {
                                0 -> j1 = 0
                                1 -> i1 = 0
                                2 -> j1 = a[i].size - 1
                                3 -> i1 = a.size - 1
                                else -> error(d)
                            }
                            while (i1 !in a.indices || j1 !in a[i1].indices || a[i1][j1] == ' ') {
                                i1 += di[d]
                                j1 += dj[d]
                            }
                        } else {
                            // Part2 wrapping
                            var ii = i % 50
                            var jj = j % 50
                            val u = MC(i / 50, j / 50, d)
                            val v = mc[u]!!
                            when (v.d) {
                                0 -> {
                                    when (d) {
                                        1, 3 -> ii = jj
                                        2 -> ii = 49 - ii
                                    }
                                    i1 = v.i * 50 + ii
                                    j1 = v.j * 50
                                }

                                1 -> {
                                    when (d) {
                                        0, 2 -> jj = ii
                                    }
                                    i1 = v.i * 50
                                    j1 = v.j * 50 + jj
                                }

                                2 -> {
                                    when (d) {
                                        1, 3 -> ii = jj
                                        0 -> ii = 49 - ii
                                    }
                                    i1 = v.i * 50 + ii
                                    j1 = v.j * 50 + 49
                                }

                                3 -> {
                                    when (d) {
                                        0, 2 -> jj = ii
                                    }
                                    i1 = v.i * 50 + 49
                                    j1 = v.j * 50 + jj
                                }
                            }
                            d1 = v.d
                        }
                    }
                    when (a[i1][j1]) {
                        '.' -> {
                            i = i1
                            j = j1
                            d = d1
                        }
                        '#' -> break@move
                        else -> error("!!!")
                    }
                }
            }
            'L' -> {
                d = (d + 3) % 4
                p++
            }
            'R' -> {
                d = (d + 1) % 4
                p++
            }
            else -> error(s[p])
        }
    }
    val ans = 1000 * (i + 1) + 4 * (j + 1) + d
    println("part$part = $ans")
}

