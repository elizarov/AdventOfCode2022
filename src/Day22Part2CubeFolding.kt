// This code for Day22 Part 2 works on any input (including the example),
// because it can fold an arbitrary cube in 3D

import kotlin.math.sqrt

fun main() {
    val dayId = "22"
    val input = readInput("Day${dayId}")
    val (aa, ss) = input.parts { it }
    val a = aa.map { it.toCharArray() }
    val s = ss[0]

    // compute size
    val totalCnt = a.sumOf { r -> r.count { it != ' ' } }
    val fs = sqrt(totalCnt / 6.0).toInt() // face size
    check(fs * fs * 6 == totalCnt)

    // extract faces
    data class F(val i: Int, val j: Int)
    val fm = buildList {
        for (i in a.indices) for (j in a[i].indices) if (a[i][j] != ' ') {
            add(F(i / fs, j / fs))
        }
    }.groupingBy { it }.eachCount()
    check(fm.values.all { it == fs * fs })

    // directions
    val (di, dj) = RDLU_DIRS

    // 3D vectors
    data class V3(val i: Int, val j: Int, val k: Int)
    operator fun V3.unaryMinus() = V3(-i, -j, -k)
    operator fun V3.plus(v: V3) = V3(i + v.i, j + v.j, k + v.k)
    operator fun V3.times(x: Int) = V3(i * x, j * x, k * x)
    operator fun V3.times(v: V3): Int = i * v.i + j * v.j + k * v.k

    // 3D faces basis
    data class FV(val i: V3, val j: V3, val n: V3)
    fun FV.d2v(d: Int): V3 = i * di[d] + j * dj[d]
    fun FV.v2d(v: V3): Int = (0..3).single { d -> d2v(d) == v }

    // dfs on faces to construct folding
    val f2v = HashMap<F, FV>() // visited faces to vectors
    val n2f = HashMap<V3, F>() // normals to visited faces
    val f2d = HashMap<F, List<FV>>() // neighbour faces in all directions
    fun dfs(f: F, fv: FV) {
        if (f in f2v) return
        f2v[f] = fv
        n2f[fv.n] = f
        f2d[f] = List(4) { d ->
            val fd = F(f.i + di[d], f.j + dj[d])
            val fvd = when (d) {
                0 -> FV(fv.i, fv.n, -fv.j)
                1 -> FV(fv.n, fv.j, -fv.i)
                2 -> FV(fv.i, -fv.n, fv.j)
                3 -> FV(-fv.n, fv.j, fv.i)
                else -> error(d)
            }
            if (fd in fm) dfs(fd, fvd) // fold neighbour
            fvd
        }
    }
    val f0 = fm.keys.filter { it.i == 0 }.minBy { it.j } // initial face
    dfs(f0, FV(V3(1, 0, 0), V3(0, 1, 0), V3(0, 0, 1)))

    // start walking for solution
    var p = 0
    var i = 0
    var j = f0.j * fs
    var d = 0
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
                        // transition according to the folded cube
                        val f = F(i / fs, j / fs) // the original face
                        val fv = f2d[f]!![d]       // basis of the next face if it was connected
                        val f1 = n2f[fv.n]!!        // next face (look up by the normal)
                        val fv1 = f2v[f1]!!        // the actual basis of the new face
                        d1 = fv1.v2d(fv.d2v(d))        // direction on the new face
                        // compute 3D vector of the offset on the face in the original basis
                        val vv = fv.i * (i1.mod(fs) + 1) + fv.j * (j1.mod(fs) + 1)
                        // project offset in the new basis
                        fun flipNeg(s: Int) = if (s < 0) fs + 1 + s else s
                        i1 = f1.i * fs + flipNeg(vv * fv1.i) - 1
                        j1 = f1.j * fs + flipNeg(vv * fv1.j) - 1
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
    println("part2 = $ans")
}
