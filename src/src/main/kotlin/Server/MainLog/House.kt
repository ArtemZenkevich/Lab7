/**
 * Класс реализации объектов домов.
 * @param name - название дома
 * @param year - год постройки
 * @param numberOfFloors - количество квартир
 */
class House constructor(name: String, year: Int, numberOfFloors: Int) {
    /**Поле названия дома */
    var name: String
    /**Поле года постройки дома*/
    var year: Int = 0
    var numberOfFloors: Int = 0
    /**  Конструктор - создание нового объекта с определенными значениями */
    init {
        this.name = name
        this.year = year
        this.numberOfFloors = numberOfFloors
    }
    /**
     * Функция получения значения поля {@link name}
     * @return возвращает название дома
     */
    fun getHouseName(): String {
        return name
    }
    /**
     * Функция получения значения поля {@link year}
     * @return возвращает год постройки дома
     */
    fun getHouseYear(): Int {
        return year
    }
    /**
     * Функция получения значения поля {@link numberOfFloors}
     * @return возвращает год постройки дома
     */
    fun getHouseNumberOfFloors(): Int {
        return numberOfFloors
    }

}