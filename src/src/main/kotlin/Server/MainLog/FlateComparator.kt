/**
 * Класс реализации компаратора.
 */
class FlateComparator {
    companion object : Comparator<Flat> {
        override fun compare(a: Flat, b: Flat): Int = when {
            a.getArea()!! < b.getArea()!! -> 1
            a.getArea()!! > b.getArea()!! -> -1
            else -> 0
        }
    }
}