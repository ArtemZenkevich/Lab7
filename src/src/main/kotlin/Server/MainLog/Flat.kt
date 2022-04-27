import java.time.ZonedDateTime
/**
 * Класс реализации объектов квартир.
 * @autor Зенкевич Артем Андреевич
 * @version 1.0
 * @param id1 - Id
 * @param name1 - название квартиры
 * @param x1 - координата X
 * @param y1 - координата Y
 * @param area1 - площадь
 * @param numberOfRooms1 - количество комнат
 * @param price1 - цена
 * @param furnish1 - состояние
 * @param view1 - вид из окна
 * @param housename - название дома
 * @param year1 - постройка дома
 * @param numberOfFloors1 - количество этажей дома
 */
class Flat(
    id1: Long,
    name1: String,
    x1: Int,
    y1: Int,
    area1: Double,
    numberOfRooms1: Long,
    price1: Long,
    furnish1: Furnish,
    view1: View,
    housename: String,
    year1: Int,
    numberOfFloors1: Int
) {
    /** Поле ID квартиры */
    private var id //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
            : Long? = null
    /** Поле названия квартиры */
    private var name //Поле не может быть null, Строка не может быть пустой
            : String? = null
    /** Поле координаты квартиры X */
    private var Coordinates: Coordinates? = null  //Поле не может быть null
    /** Поле координаты квартиры Y*/
    private var creationDate //Поле не может быть null, Значение этого поля должно генерироваться автоматически
            : ZonedDateTime? = null
    /** Поле площади квартиры */
    private var area //Поле может быть null, Значение поля должно быть больше 0
            : Double? = null
    /** Поле количества комнат квартиры */
    private var numberOfRooms //Значение поля должно быть больше 0
            : Long? = null
    /** Поле цены квартиры */
    private var price //Значение поля должно быть больше 0
            : Long = 0
    /** Поле состояния квартиры */
    private var furnish //Поле не может быть null
            : Furnish? = null
    /** Поле площади квартиры */
    private var view //Поле не может быть null
            : View? = null
    private var house //Поле не может быть null
            : House? = null
    /**  Конструктор - создание нового объекта с определенными значениями
     * */
    init {
        this.id = id1//+
        this.name = name1//+
        this.Coordinates = Coordinates(x1, y1)//+
        this.creationDate = ZonedDateTime.now()//+
        this.area = area1//+
        this.numberOfRooms = numberOfRooms1//
        this.price = price1
        this.furnish = furnish1
        this.view = view1
        this.house = House(housename, year1, numberOfFloors1)
    }
    /**
     * Функция получения значения поля {@link name}
     * @return возвращает название квартиры
     */
    fun getName(): String {
        return name.toString()
    }
    /**
     * Функция получения значения поля {@link area}
     * @return возвращает площадь
     */
    fun getArea(): Double? {
        return area
    }
    /**
     * Функция получения значения поля {@link id}
     * @return возвращает Id
     */
    fun getId(): Long? {
        return id
    }
    /**
     * Функция получения значения поля {@link Coordinates}
     * @return возвращает координаты
     */
    fun getCoordinates(): Coordinates? {
        return Coordinates
    }
    /**
     * Функция получения значения поля {@link area}
     * @return возвращает координаты
     */
    fun getCreationDate(): ZonedDateTime? {
        return creationDate
    }
    /**
     * Функция получения значения поля {@link NumberOfRooms}
     * @return возвращает количество комнат
     */
    fun getNumberOfRooms(): Long? {
        return numberOfRooms
    }
    /**
     * Функция получения значения поля {@link price}
     * @return возвращает цену
     */
    fun getPrice(): Long? {
        return price
    }
    /**
     * Функция получения значения поля {@link Furnish}
     * @return возвращает состояние квартиры
     */
    fun getFurnish(): Furnish? {
        return furnish
    }
    /**
     * Функция получения значения поля {@link house}
     * @return возвращает состояние квартиры
     */
    fun getHouse(): House? {
        return house
    }
    /**
     * Функция получения значения поля {@link View}
     * @return возвращает вид из окна
     */
    fun getView(): View? {
        return view
    }
    /**
     * Функция получения сообщения с информацией об квартире
     * @return возвращает текст информации об квартире
     */
    fun show(): String {
        return "Комната: " +
                "name: $name " +
                "id: $id " +
                "Coordinates: ${Coordinates?.x} ${Coordinates?.y}" +
                " creationDate: $creationDate " +
                "area: $area " +
                "numberOfRooms: $numberOfRooms " +
                "price: $price  " +
                "furnish:  $furnish " +
                "view: $view  " +
                "house ${house?.name} ${house?.year} ${house?.numberOfFloors}"
    }
}


