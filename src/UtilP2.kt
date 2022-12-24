/**
 * (Row, Column) coordinate in 2D grid.
 */
data class P2(val i: Int, val j: Int)

/**
 * Right, Down, Left, Up directions on a 2D grid. Usage:
 *
 * ```
 * val (di, dj) = RDLU_DIRS
 * ```
 */
val RDLU_DIRS: Pair<IntArray, IntArray> = Pair(
    intArrayOf(0, 1, 0, -1),
    intArrayOf(1, 0, -1, 0)
)