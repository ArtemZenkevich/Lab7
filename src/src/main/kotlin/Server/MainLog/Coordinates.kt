/**
 * Класс реализации координат квартир.
 * @param x1 - название дома
 * @param y1 - год постройки
 */
class Coordinates constructor(var x1: Int?, var y1: Int?) {
    /** Поле координаты X*/
    var x: Int? = null
    /** Поле координаты Y*/
    var y: Int? = null
    /**  Конструктор - создание нового объекта с определенными значениями
     * */
    init {
        this.x = x1
        this.y = y1
    }
}