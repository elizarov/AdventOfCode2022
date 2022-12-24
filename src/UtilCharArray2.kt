typealias CharArray2 = Array<CharArray>

fun List<String>.toCharArray2() = Array(size) { get(it).toCharArray() }

fun CharArray2.size2(): P2 {
    val n = size
    val m = get(0).size
    for (i in 1 until n) require(get(i).size == m) { "Row $i has size ${get(i)}, but expected $m" }
    return P2(n, m)
}

inline fun CharArray2.forEachIndexed2(action: (i: Int, j: Int, c: Char) -> Unit) {
    for (i in indices) {
        val b = get(i)
        for (j in b.indices) {
            action(i, j, b[j])
        }
    }
}

inline fun <R> CharArray2.mapIndexed2NotNull(transform: (i: Int, j: Int, c: Char) -> R?): List<R> = buildList {
    forEachIndexed2 { i, j, c ->
        transform(i, j, c)?.let { add(it) }
    }
}

inline fun CharArray2.toListOfP2If(predicate: (c: Char) -> Boolean): List<P2> = buildList {
    forEachIndexed2 { i, j, c ->
        if (predicate(c)) add(P2(i, j))
    }
}
