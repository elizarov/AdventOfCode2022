typealias List2<T> = List<List<T>>

fun <T> List2<T>.size2(): P2 {
    val n = size
    val m = get(0).size
    for (i in 1 until n) require(get(i).size == m) { "Row $i has size ${get(i)}, but expected $m" }
    return P2(n, m)
}

inline fun <T> List2<T>.forEachIndexed2(action: (i: Int, j: Int, value: T) -> Unit) {
    for (i in indices) {
        val b = get(i)
        for (j in b.indices) {
            action(i, j, b[j])
        }
    }
}

inline fun <T, R> List2<T>.mapIndexed2NotNull(transform: (i: Int, j: Int, value: T) -> R?): List<R> = buildList {
    forEachIndexed2 { i, j, c ->
        transform(i, j, c)?.let { add(it) }
    }
}
