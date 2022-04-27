/**
 * Перечисление видов состояния {@link View}
 */
enum class View {
    STREET, BAD, GOOD, TERRIBLE
}
/**
 * Функция получения значения  {@link View}
 * @return возвращает состояние вида из окна
 */
fun view(view1: String): View? {
    return when (view1) {
        "STREET" -> View.STREET
        "BAD" -> View.BAD
        "GOOD" -> View.GOOD
        "TERRIBLE" -> View.TERRIBLE
        "View.STREET" -> View.STREET
        "View.BAD" -> View.BAD
        "View.GOOD" -> View.GOOD
        "View.TERRIBLE" -> View.TERRIBLE
        else -> null
    }
}
