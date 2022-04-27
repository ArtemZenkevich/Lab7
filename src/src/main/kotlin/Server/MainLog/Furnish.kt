
/**
 * Перечисление видов состояния {@link Furnish}
 */
enum class Furnish {
    DESIGNER, NONE, FINE, BAD, LITTLE
}
/**
 * Функция получения значения  {@link Furnish}
 * @return возвращает состояние квартиры
 */
fun furnish(funish1: String): Furnish {
    return when (funish1) {
        "DESIGNER" -> Furnish.DESIGNER
        "FINE" -> Furnish.FINE
        "BAD" -> Furnish.BAD
        "LITTLE" -> Furnish.LITTLE
        "Furnish.DESIGNER" -> Furnish.DESIGNER
        "Furnish.FINE" -> Furnish.FINE
        "Furnish.BAD" -> Furnish.BAD
        "Furnish.LITTLE" -> Furnish.LITTLE
        else -> Furnish.NONE
    }
}