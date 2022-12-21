fun main() {
    day21Part(1)
    day21Part(2)
}

private const val ROOT = "root"
private const val HUMN = "humn"

fun day21Part(part: Int) {
    val dayId = "21"
    val input = readInput("Day${dayId}")

    data class Monkey(val name: String, var x: Long?, val a: String, var op: String, val b: String)

    val ms = input.map { s ->
        val (name, os) = s.split(": ")
        val x = os.toLongOrNull()
        if (x != null) {
            Monkey(name, x, "", "", "")
        } else {
            val (a, op, b) = os.split(" ")
            Monkey(name, null, a, op, b)
        }
    }.associateBy { it.name }

    if (part == 2) {
        ms[HUMN]!!.x = null
        ms[ROOT]!!.op = "="
    }

    fun compute(name: String): Long? {
        val m = ms[name]!!
        if (m.name == HUMN || m.x != null) return m.x
        val av = compute(m.a)
        val bv = compute(m.b)
        if (av == null || bv == null) return null
        val x = when (m.op) {
            "+" -> av + bv
            "-" -> av - bv
            "*" -> av * bv
            "/" -> av / bv
            else -> error(m.op)
        }
        m.x = x
        return x
    }

    val ans1 = compute(ROOT)

    if (part == 1) {
        println("part1 = $ans1")
        return
    }

    var r = ROOT
    var c = 0L
    while (r != HUMN) {
        val m = ms[r]!!
        val a = ms[m.a]!!
        val b = ms[m.b]!!
        when (m.op) {
            "=" -> when {
                a.x != null -> {
                    check(b.x == null)
                    r = m.b
                    c = a.x!!
                }
                b.x != null -> {
                    check(a.x == null)
                    r = m.a
                    c = b.x!!
                }
                else -> error(m)
            }
            "+" -> when {
                a.x != null -> {
                    check(b.x == null)
                    r = m.b
                    c -= a.x!!
                }
                b.x != null -> {
                    check(a.x == null)
                    r = m.a
                    c -= b.x!!
                }
                else -> error(m)
            }
            "-" -> when {
                a.x != null -> {
                    check(b.x == null) { "$m" }
                    r = m.b
                    c = a.x!! - c
                }
                b.x != null -> {
                    check(a.x == null)
                    r = m.a
                    c += b.x!!
                }
                else -> error(m)
            }
            "*" -> when {
                a.x != null -> {
                    check(b.x == null)
                    r = m.b
                    c /= a.x!!
                }
                b.x != null -> {
                    check(a.x == null)
                    r = m.a
                    c /= b.x!!
                }
                else -> error(m)
            }
            "/" -> when {
                a.x != null -> {
                    check(b.x == null)
                    r = m.b
                    c = a.x!! / c
                }
                b.x != null -> {
                    check(a.x == null)
                    r = m.a
                    c *= b.x!!
                }
                else -> error(m)
            }
            else -> error(m)
        }
    }
    println("part2 = $c")
}
