import jdk.nashorn.internal.runtime.JSType.isNumber
import src.main.kotlin.Server.MainLog.WorkWithClient
import java.io.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.ServerSocket
import java.util.*

/**
 * Класс реализации приказов {@see CommanderManager}
 */
class CollectionManager:WorkWithClient() {
    /**
     * Поле хранения коллекции
     */
    private var Flats: PriorityQueue<Flat>? = null
    /**
     * Поле хранения времени создания коллекции
     */
    private var time: Date? = null
    /**
     * Поле хранения максимального значения {@see area}
     */
    private var max: Double? = null
    /**
     * Поле хранения списка доступных команд и их описание
     */
    private var manual: HashMap<String, String> = HashMap()
    /**
     * Инициализация {@see CollectionManager}
     */
    init {
        time = Date()
        Flats = PriorityQueue<Flat>(FlateComparator)
        manual.put("help", "вывести справку по доступным командам")
        manual.put(
            "info",
            "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)"
        )
        manual.put("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении")
        manual.put("add", "добавить новый элемент в коллекцию")
        manual.put("update", "обновить значение элемента коллекции, id которого равен заданному")
        manual.put("remove_by_id", "удалить элемент из коллекции по его id")
        manual.put("clear", "очистить коллекцию")
        manual.put("save", "сохранить коллекцию в файл")
        manual.put(
            "load",
            "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме."
        )
        manual.put("exit", "завершить программу (без сохранения в файл)")
        manual.put(
            "add_if_max",
            "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции"
        )
        manual.put("remove_greater", "удалить из коллекции все элементы, превышающие заданный")
        manual.put("history", "вывести последние 13 команд (без их аргументов)")
        manual.put(
            "remove_all_by_house house",
            "удалить из коллекции все элементы, значение поля house которого эквивалентно заданному"
        )
        manual.put(
            "filter_contains_name name",
            "вывести элементы, значение поля name которых содержит заданную подстроку"
        )
        manual.put("filter_less_than_house house", "вывести элементы, значение поля house которых меньше заданного")

    }
    /**
     * Функция добавления элементов коллекции
     */
    fun add(serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int) {
        try {
            var flag = true
            var name1: String? = null
            do {
                Send2Client(serverSocket, senderAddress, senderPort, "Название:")
                name1 = GetFromClient(serverSocket) as String?
                if (name1 == "") {
                    flag = true
                    Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенное название квартиры.")
                } else {
                    flag = false
                }
            } while (flag)
            var id1: Int? = Flats?.size?.plus(1)
            var x1: Int? = null
            do {
                Send2Client(serverSocket, senderAddress, senderPort, "Координата X:")
                x1 = GetFromClient(serverSocket)?.toInt()
                if (x1 == null) {
                    flag = true
                    Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенная координата X.")
                } else {
                    flag = false
                }
            } while (flag)
            var y1: Int? = null
            do {
                Send2Client(serverSocket, senderAddress, senderPort, "Координата Y:")
                y1 = GetFromClient(serverSocket)?.toInt()
                if (y1 == null) {
                    flag = true
                    Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенная координата Y.")
                } else {
                    flag = false
                }
            } while (flag)
            var area1: Double? = null
            do {
                Send2Client(serverSocket, senderAddress, senderPort, "Площадь: ")
                area1 = GetFromClient(serverSocket)?.toDouble()
                if (area1 == null || !isNumber(area1)) {
                    flag = true
                    Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенная площадь.")
                } else {
                    flag = false
                }
            } while (flag)
            var numberOfRooms1: Long? = null
            do {
                Send2Client(serverSocket, senderAddress, senderPort, "Количество комнат: ")
                println()
                numberOfRooms1 = GetFromClient(serverSocket)?.toLong()
                if (numberOfRooms1 == null) {
                    flag = true
                    Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенное количество комнат.")
                } else {
                    flag = false
                }
            } while (flag)
            var price1: Long? = null
            do {
                Send2Client(serverSocket, senderAddress, senderPort, "Цена: ")
                price1 = GetFromClient(serverSocket)?.toLong()
                if (price1 == null) {
                    flag = true
                    Send2Client(serverSocket, senderAddress, senderPort, "Не правильная введенная цена.")
                } else {
                    flag = false
                }
            } while (flag)
            var furnish1: String? = null
            do {
                Send2Client(serverSocket, senderAddress, senderPort, "Предоставленное состояние квартиры: ")
                furnish1 = GetFromClient(serverSocket) as String?
                if (furnish1?.let { furnish(it) } == Furnish.NONE) {
                    flag = true
                    Send2Client(serverSocket, senderAddress, senderPort, "Нет такого формата ответа.")
                } else {
                    flag = false
                }
            } while (flag)
            var view1: String? = null
            do {
                Send2Client(serverSocket, senderAddress, senderPort, "Какой вид из окна: ")
                view1 = GetFromClient(serverSocket) as String?
                if (view1?.let { view(it) } == null) {
                    flag = true
                    Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенный вид из окна.")
                } else {
                    flag = false
                }
            } while (flag)
            var houseName1: String? = null
            do {
                Send2Client(serverSocket, senderAddress, senderPort, "Какое название дома:")
                houseName1 = GetFromClient(serverSocket) as String?
                if (houseName1 == " " || houseName1 == null) {
                    flag = true
                    Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенное название дома.")
                } else {
                    flag = false
                }
            } while (flag)
            var year1: Int? = null
            do {
                Send2Client(serverSocket, senderAddress, senderPort, "Год постройки дома: ")
                year1 = GetFromClient(serverSocket)?.toInt()
                if (year1 == null) {
                    flag = true
                    Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенное название дома.")
                } else {
                    flag = false
                }
            } while (flag)
            var numberOfFloors1: Int? = null
            do {
                Send2Client(serverSocket, senderAddress, senderPort, "Количество этажей в доме: ")
                numberOfFloors1 = GetFromClient(serverSocket)?.toInt()
                if (numberOfFloors1 == null) {
                    flag = true
                    Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенное количество этажей в доме.")
                } else {
                    flag = false
                }
            } while (flag)
            var flatnew: Flat = Flat(
                id1!!.toLong(), name1!!,
                x1!!, y1!!, area1!!, numberOfRooms1!!, price1!!, furnish(furnish1!!),
                view(view1!!)!!, houseName1!!, year1!!, numberOfFloors1!!
            )

            Flats?.add(flatnew)
            Send2Client(serverSocket, senderAddress, senderPort, "Элемент удачно добавлен.")
        } catch (ex: ArrayIndexOutOfBoundsException) {
            Send2Client(serverSocket, senderAddress, senderPort, "Ошибка переполнения коллекции")
        }
    }
    /**
     * Функция вывода размера коллекции
     */
    fun getSize(): Int? {
        return Flats?.size
    }
    /**
     * Функция обновления элементов коллекции
     */
    fun update(flat: Flat, serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int): Flat {
        try {
            var flag = true
            var answer: String? = null
            var name1: String? = null
            Send2Client(serverSocket, senderAddress, senderPort, "Хотите ли вы поменять название квартиры?(True/False)")
            answer = GetFromClient(serverSocket) as String?
            if (answer.toBoolean()) {
                do {
                    Send2Client(serverSocket, senderAddress, senderPort, "Название:")
                    name1 = GetFromClient(serverSocket) as String?
                    if (name1 == "" || name1 == null) {
                        flag = true
                        Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенное название квартиры.")
                        println()
                    } else {
                        flag = false
                    }
                } while (flag)
            } else {
                name1 = flat.getName()
            }
            var id1: Int? = flat.getId()?.toInt()
            var x1: String? = null
            Send2Client(serverSocket, senderAddress, senderPort, "Хотите ли вы поменять координату X квартиры?(True/False)")
            answer = GetFromClient(serverSocket) as String?
            if (answer.toBoolean()) {
                do {
                    Send2Client(serverSocket, senderAddress, senderPort, "Координата X:")
                    x1 = GetFromClient(serverSocket) as String?
                    if (x1 == null) {
                        flag = true
                        Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенная координата X.")
                    } else {
                        flag = false
                    }
                } while (flag)
            } else {
                x1 = flat.getCoordinates()?.x.toString()
            }
            var y1: String? = null
            Send2Client(serverSocket, senderAddress, senderPort, "Хотите ли вы поменять координату Y квартиры?(True/False)")
            answer = GetFromClient(serverSocket) as String?
            if (answer.toBoolean()) {
                do {
                    Send2Client(serverSocket, senderAddress, senderPort, "Координата Y:")
                    y1 = GetFromClient(serverSocket) as String?
                    if (y1 == null) {
                        flag = true
                        Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенная координата Y.")
                    } else {
                        flag = false
                    }
                } while (flag)
            } else {
                y1 = flat.getCoordinates()?.y.toString()
            }
            var area1: String? = null
            Send2Client(serverSocket, senderAddress, senderPort, "Хотите ли вы поменять площадь квартиры?(True/False)")
            answer = GetFromClient(serverSocket) as String?
            if (answer.toBoolean()) {
                do {
                    Send2Client(serverSocket, senderAddress, senderPort, "Площадь: ")
                    area1 = GetFromClient(serverSocket) as String?
                    if (area1 == null) {
                        flag = true
                        Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенная площадь.")
                    } else {
                        flag = false
                    }
                } while (flag)
            } else {
                area1 = flat.getArea().toString()
            }
            var numberOfRooms1: String? = null
            Send2Client(serverSocket, senderAddress, senderPort, "Хотите ли вы поменять количество комнат?(True/False)")
            answer = GetFromClient(serverSocket) as String?
            if (answer.toBoolean()) {
                do {
                    Send2Client(serverSocket, senderAddress, senderPort, "Количество комнат: ")
                    println()
                    numberOfRooms1 = GetFromClient(serverSocket) as String?
                    if (numberOfRooms1 == null) {
                        flag = true
                        Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенное количество комнат.")
                    } else {
                        flag = false
                    }
                } while (flag)
            } else {
                numberOfRooms1 = flat.getNumberOfRooms().toString()
            }
            var price1: String? = null
            Send2Client(serverSocket, senderAddress, senderPort, "Хотите ли вы поменять цену квартиры?(True/False)")
            answer = GetFromClient(serverSocket) as String?
            if (answer.toBoolean()) {
                do {
                    Send2Client(serverSocket, senderAddress, senderPort, "Цена: ")
                    price1 = GetFromClient(serverSocket) as String?
                    if (price1 == null) {
                        flag = true
                        Send2Client(serverSocket, senderAddress, senderPort, "Не правильная введенная цена.")
                    } else {
                        flag = false
                    }
                } while (flag)
            } else {
                price1 = flat.getPrice().toString()
            }
            var furnish1: String? = null
            Send2Client(serverSocket, senderAddress, senderPort, "Хотите ли вы поменять состояние квартиры?(True/False)")
            answer = GetFromClient(serverSocket) as String?
            if (answer.toBoolean()) {
                do {
                    Send2Client(serverSocket, senderAddress, senderPort, "Предоставленное состояние квартиры: ")
                    furnish1 = GetFromClient(serverSocket) as String?
                    if (furnish1?.let { furnish(it) } == Furnish.NONE) {
                        flag = true
                        Send2Client(serverSocket, senderAddress, senderPort, "Нет такого формата ответа.")
                    } else {
                        flag = false
                    }
                } while (flag)
            } else {
                furnish1 = flat.getFurnish().toString()
            }
            var view1: String? = null
            Send2Client(serverSocket, senderAddress, senderPort, "Хотите ли вы поменять как выглядит квартира?(True/False)")
            answer = GetFromClient(serverSocket) as String?
            if (answer.toBoolean()) {
                do {
                    Send2Client(serverSocket, senderAddress, senderPort, "Какой вид из окна: ")
                    view1 = GetFromClient(serverSocket) as String?
                    if (view1?.let { view(it) } == null) {
                        flag = true
                        Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенный вид из окна.")
                    } else {
                        flag = false
                    }
                } while (flag)
            } else {
                furnish1 = flat.getFurnish().toString()
            }
            var houseName1: String? = null
            Send2Client(serverSocket, senderAddress, senderPort, "Хотите ли вы поменять название дома?(True/False)")
            answer = GetFromClient(serverSocket) as String?
            if (answer.toBoolean()) {
                do {
                    Send2Client(serverSocket, senderAddress, senderPort, "Какое название дома:")
                    houseName1 = GetFromClient(serverSocket) as String?
                    if (houseName1 == " " || houseName1 == null) {
                        flag = true
                        Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенное название дома.")
                    } else {
                        flag = false
                    }
                } while (flag)
            } else {
                houseName1 = flat.getHouse()?.getHouseName().toString()
            }
            var year1: String? = null
            Send2Client(serverSocket, senderAddress, senderPort, "Хотите ли вы поменять год создания дома?(True/False)")
            answer = GetFromClient(serverSocket) as String?
            if (answer.toBoolean()) {
                do {
                    Send2Client(serverSocket, senderAddress, senderPort, "Год постройки дома: ")
                    year1 = GetFromClient(serverSocket) as String?
                    if (year1 == null) {
                        flag = true
                        Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенное название дома.")
                    } else {
                        flag = false
                    }
                } while (flag)
            } else {
                year1 = flat.getHouse()?.getHouseYear().toString()
            }
            var numberOfFloors1: String? = null
            Send2Client(serverSocket, senderAddress, senderPort, "Хотите ли вы поменять количество этажей дома?(True/False)")
            answer = GetFromClient(serverSocket) as String?
            if (answer.toBoolean()) {
                do {
                    Send2Client(serverSocket, senderAddress, senderPort, "Количество этажей дома: ")
                    year1 = GetFromClient(serverSocket) as String?
                    if (year1 == null) {
                        flag = true
                        Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенное количество этажей дома.")
                    } else {
                        flag = false
                    }
                } while (flag)
            } else {
                numberOfFloors1 = flat.getHouse()?.getHouseNumberOfFloors().toString()
            }
            var flatnew: Flat = Flat(
                id1!!.toLong(),
                name1!!,
                x1!!.toInt(),
                y1!!.toInt(),
                area1!!.toDouble(),
                numberOfRooms1!!.toLong(),
                price1!!.toLong(),
                furnish(furnish1!!),
                view(view1!!)!!,
                houseName1!!,
                year1!!.toInt(),
                numberOfFloors1!!.toInt()
            )
            return flatnew
        } catch (ex: ArrayIndexOutOfBoundsException) {
            Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенное количество этажей дома.")
            return flat
        }
    }
    /**
     * Функция удаления элементов, превышающее значения элемента параметра
     * @param float имя элемента коллекции
     */
    fun remove_greater(float: String?, serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int) {

        if (Flats!!.size != 0) {
            val beginSize = Flats!!.size
            for (i in Flats!!) {
                if (i.getName() == float) {
                    max = i.getArea()
                    break
                }
            }
            Flats?.stream()?.filter { p -> p.getArea()!! > max!! }?.forEach { p -> Flats?.remove(p) }//Лямбда выражение
            Send2Client(serverSocket, senderAddress, senderPort, "Из коллекции удалено \" + (beginSize - Flats!!.size) + \" элементов.")
        }
    }
    /**
     * Функция получения времени создания коллекции
     */
    fun getTime(): Date? {
        return time
    }
    /**
     * Функция вывода содержимого коллекции
     */
    fun show(serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int) {
        var info:String? = ""
        for (i in Flats!!) {
            info += i.show()
        }
        if (Flats!!.size == 0) {
            Send2Client(serverSocket, senderAddress, senderPort, "Коллекция пуста.")
            println("Коллекция пуста.")
        }
        if (info != null) {
            Send2Client(serverSocket, senderAddress, senderPort, info)
        }
    }
    /**
     * Функция очистки коллекции
     */
    fun clear(serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int) {
        Flats!!.clear()
        Send2Client(serverSocket, senderAddress, senderPort, "Коллекция очищена.")
    }
    /**
     * Функция удаления элемент по Id
     * @param id
     */
    fun remove_by_id(id: String?, serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int) {
        var flag = false
        try {
            Flats?.stream()?.filter { p->p.getId().toString()==id }?.forEach { p->Flats?.remove(p) }
            for (i in Flats!!) {
                if (i.getId().toString() == id) {
                    Flats!!.remove(i)
                    Send2Client(serverSocket, senderAddress, senderPort, "Был успешно удалён элемент с индексом $id")
                    flag = true
                    break
                }
            }
            if (!flag) {
                Send2Client(serverSocket, senderAddress, senderPort, "Нет такого элемента с id $id")
            }
        } catch (ex: ArrayIndexOutOfBoundsException) {
            Send2Client(serverSocket, senderAddress, senderPort, "Ошибка переполнения индекса")
        }
    }
    /**
     * Функция загрузки Json файла с коллекцией элементов
     * @param CollactionPath
     */
    fun load(CollactionPath: String?, serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int) {
        try {
            val file = File(CollactionPath)
            val `is`: InputStream = FileInputStream(file)
            val bis = BufferedInputStream(`is`)
            var word: String? = ""
            var last: Char? = null
            var l = 0
            var flag = true
            var name1: String = ""
            var x1: String = ""
            var y1: String = ""
            var area1: String = ""
            var numberOfRooms1: String = ""
            var price1: String = ""
            var furnish1: String = ""
            var view: String = ""
            var housename1: String = ""
            var houseyear1: String = ""
            var kol = 0
            var first = true
            while (l != 74) {

                flag = true
                kol = 0
                while (flag) {
                    last = bis.read().toChar()
                    if (last == '\"') {
                        kol++
                        if (kol == 2) {
                            flag = !flag
                        }
                    } else if (kol == 1) {
                        word += last
                    }
                }
                if (first) {
                    word = ""
                    first = !first
                }
                if (word != "") {
                    l++
                    if (l % 25 == 0) {
                        l++
                    }
                    flag = !flag
                    when (l % 25) {
                        1 -> word = ""
                        2 -> {
                            name1 = word!!
                        }
                        3 -> word = ""
                        4 -> word = ""
                        5 -> {
                            x1 = word!!
                        }
                        6 -> word = ""
                        7 -> {
                            y1 = word!!
                        }
                        8 -> word = ""
                        9 -> {
                            area1 = word!!
                        }
                        10 -> word = ""
                        11 -> {
                            numberOfRooms1 = word!!
                        }
                        12 -> word = ""
                        13 -> {
                            price1 = word!!
                        }
                        14 -> word = ""
                        15 -> {
                            furnish1 = word!!
                        }
                        16 -> word = ""
                        17 -> {
                            view = word!!
                        }
                        18 -> word = ""
                        19 -> word = ""
                        20 -> {
                            housename1 = word!!
                        }
                        21 -> word = ""
                        22 -> {
                            houseyear1 = word!!
                        }
                        23 -> word = ""
                        24 -> {
                            var numberOfFloors1 = word
                            var flat1: Flat = Flat(
                                Flats?.size!!.toLong() + 1,
                                name1,
                                x1.toInt(),
                                y1.toInt(),
                                area1.toDouble(),
                                numberOfRooms1.toLong(),
                                price1.toLong(),
                                furnish(furnish1),
                                view(view)!!,
                                housename1,
                                houseyear1.toInt(),
                                numberOfFloors1!!.toInt()
                            )
                            Flats!!.add(flat1)
                        }
                    }
                }
            }
            Send2Client(serverSocket, senderAddress, senderPort, "Загрузка совершилась с успехом.")
        } catch (ex: IOException) {
            Send2Client(serverSocket, senderAddress, senderPort, "Нет такого файла.")
        }

    }
    /**
     * Функция вывода доступной информации
     */
    fun help(serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int) {
        var info: String?=null
        info+=("\"\"\"\n" +
                "                Lab6 made by Artem Zenkevich\n" +
                "                \"\"\".trimIndent()")
        for (i in manual) {
            info+= (i.key + ": " + i.value)+"\n"
        }
        if (info != null) {
            Send2Client(serverSocket, senderAddress, senderPort, info)
        }
    }
    /**
     * Функция сохранения информации в файл
     * @param FileName имф файла для сохранения
     */
    fun save(FileName: String?, serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int) {
        var file = File("$FileName.txt")
        file.createNewFile()
        val writer = FileWriter(file)
        writer.write(
            "{\n" +
                    "  \"Flats\":["
        )
        var l = 0
        for (i in Flats!!) {
            l++
            writer.write(
                "{\n" +
                        "      \"name\": \"${i.getName()}\",\n" +
                        "      \"Cooridanates\": {\n" +
                        "        \"x\": \"${i.getCoordinates()?.x}\",\n" +
                        "        \"y\": \"${i.getCoordinates()?.y}\"\n" +
                        "      },\n" +
                        "      \"area\": \"${i.getArea()}\",\n" +
                        "      \"numberOfRooms\": \"${i.getNumberOfRooms()}\",\n" +
                        "      \"price\": \"${i.getPrice()}\",\n" +
                        "      \"furnish\": \"${i.getFurnish()}\",\n" +
                        "      \"view\": \"${i.getView()}\",\n" +
                        "      \"house\":{\n" +
                        "        \"name\": \"${i.getHouse()?.name}\",\n" +
                        "        \"year\": \"${i.getHouse()?.year}\",\n" +
                        "        \"numberOfFloors\": \"${i.getHouse()?.year}\"\n" +
                        "      }\n" +
                        "    }"
            )
            if (l != Flats!!.size) {
                writer.write(",\n")
            } else {
                writer.write("\n")
            }
        }
        writer.write(
            "   ]\n" +
                    "}"
        )
        writer.flush()
        writer.close()
        Send2Client(serverSocket, senderAddress, senderPort, ("Сохранено. Сохранение находится в " + file.path))
    }
    /**
     * Функция вывода hashCode
     */
    override fun hashCode(): Int {
        return Objects.hash(manual)
    }
    /**
     * Функция обновления данных об элементе коллекции
     * @param EnterFlat имя изменяемого элемента
     */
    fun update(EnterFlat: String?,  serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int) {
        var flag = false
        for (i in Flats!!) {
            if (i.getName() == EnterFlat) {
                var newFlat = update(i,  serverSocket, senderAddress, senderPort)
                Flats!!.remove(i)
                Flats!!.add(newFlat)
                flag = true
                Send2Client(serverSocket, senderAddress, senderPort, ("Обновление было успешно."))
                println("Обновление было успешно.")
                break
            }
        }
        if (flag) {
            println("Не существует такая квартира")
        }
    }
    /**
     * Функция добавления элемента в случае превосходства
     */

    fun add_if_max(serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int) {
        try {
            var flag = true
            var name1: String? = null
            do {
                Send2Client(serverSocket, senderAddress, senderPort, "Название:")
                name1 = GetFromClient(serverSocket) as String?
                if (name1 == "" || name1 == null) {
                    flag = true
                    Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенное название квартиры.")
                    println()
                } else {
                    flag = false
                }
            } while (flag)
            var id1: Int? = Flats?.size?.plus(1)
            var x1: Int? = null
            do {
                Send2Client(serverSocket, senderAddress, senderPort, "Координата X:")
                x1 = GetFromClient(serverSocket)?.toInt()
                if (x1 == null) {
                    flag = true
                    Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенная координата X.")
                } else {
                    flag = false
                }
            } while (flag)
            var y1: Int? = null
            do {
                Send2Client(serverSocket, senderAddress, senderPort, "Координата Y:")
                y1 = GetFromClient(serverSocket) as Int?
                if (y1 == null) {
                    flag = true
                    Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенная координата Y.")
                } else {
                    flag = false
                }
            } while (flag)
            var area1: Double? = null
            do {
                Send2Client(serverSocket, senderAddress, senderPort, "Площадь: ")
                area1 = GetFromClient(serverSocket) as Double?
                if (area1 == null) {
                    flag = true
                    Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенная площадь.")
                } else {
                    flag = false
                }
            } while (flag)
            var numberOfRooms1: Long? = null
            do {
                Send2Client(serverSocket, senderAddress, senderPort, "Количество комнат: ")
                println()
                numberOfRooms1 = GetFromClient(serverSocket) as Long?
                if (numberOfRooms1 == null) {
                    flag = true
                    Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенное количество комнат.")
                } else {
                    flag = false
                }
            } while (flag)
            var price1: Long? = null
            do {
                Send2Client(serverSocket, senderAddress, senderPort, "Цена: ")
                price1 = GetFromClient(serverSocket) as Long?
                if (price1 == null) {
                    flag = true
                    Send2Client(serverSocket, senderAddress, senderPort, "Не правильная введенная цена.")
                } else {
                    flag = false
                }
            } while (flag)
            var furnish1: String? = null
            do {
                Send2Client(serverSocket, senderAddress, senderPort, "Предоставленное состояние квартиры: ")
                furnish1 = GetFromClient(serverSocket) as String?
                if (furnish1?.let { furnish(it) } == Furnish.NONE) {
                    flag = true
                    Send2Client(serverSocket, senderAddress, senderPort, "Нет такого формата ответа.")
                } else {
                    flag = false
                }
            } while (flag)
            var view1: String? = null
            do {
                Send2Client(serverSocket, senderAddress, senderPort, "Какой вид из окна: ")
                view1 = GetFromClient(serverSocket) as String?
                if (view1?.let { view(it) } == null) {
                    flag = true
                    Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенный вид из окна.")
                } else {
                    flag = false
                }
            } while (flag)
            var houseName1: String? = null
            do {
                Send2Client(serverSocket, senderAddress, senderPort, "Какое название дома:")
                houseName1 = GetFromClient(serverSocket) as String?
                if (houseName1 == " " || houseName1 == null) {
                    flag = true
                    Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенное название дома.")
                } else {
                    flag = false
                }
            } while (flag)
            var year1: Int? = null
            do {
                Send2Client(serverSocket, senderAddress, senderPort, "Год постройки дома: ")
                year1 = GetFromClient(serverSocket) as Int?
                if (year1 == null) {
                    flag = true
                    Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенное название дома.")
                } else {
                    flag = false
                }
            } while (flag)
            var numberOfFloors1: Int? = null
            do {
                Send2Client(serverSocket, senderAddress, senderPort, "Количество этажей в доме: ")
                numberOfFloors1 = GetFromClient(serverSocket) as Int?
                if (numberOfFloors1 == null) {
                    flag = true
                    Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенное количество этажей в доме.")
                } else {
                    flag = false
                }
            } while (flag)
            var flatnew: Flat = Flat(
                id1!!.toLong(), name1!!,
                x1!!, y1!!, area1!!, numberOfRooms1!!, price1!!, furnish(furnish1!!),
                view(view1!!)!!, houseName1!!, year1!!, numberOfFloors1!!
            )
            var fl = true
            for (i in Flats!!) {
                if (i.getArea()!! > flatnew.getArea()!!) {
                    fl = false
                    println("Он не оказался превосходящим площади.")
                }
            }
            if (fl) {
                Flats?.add(flatnew)
                println("Элемент успешно добавлен.")
            }
        } catch (ex: ArrayIndexOutOfBoundsException) {
            println("Ошибка переполнения коллекции")
        }
    }

    fun remove_all_by_house(houseName: String, serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int) {
        var size = Flats?.size
        for (i in Flats!!) {
            if (i.getHouse()?.getHouseName() == houseName) {
                Flats!!.remove(i)
            }
        }
        if (size != null) {
            size = size - Flats!!.size
        }
        Send2Client(serverSocket, senderAddress, senderPort, "Было удалено $size элементов")
    }

    fun remove_contains_name(name: String, serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int) {
        var size = Flats?.size
        for (i in Flats!!) {
            if (i.getName() == name) {
                Flats!!.remove(i)
            }
        }
        if (size != null) {
            size = size - Flats!!.size
        }
        Send2Client(serverSocket, senderAddress, senderPort, "Было удалено $size элементов")
        println("")

    }
    /**
     * Функция вывода информации об элементе с определенным домом
     * @param name имя дома нужного элемента
     */
    fun filter_less_than_house(name: String, serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int) {
        for (i in Flats!!) {
            if (i.getHouse()?.getHouseName() == name) {
                Flats!!.remove(i)
            }
        }
    }
    /**
     * Функция вывода информации об элементе с определенным именем
     * @param name имя изменяемого элемента
     */
    fun filter_contains_name(name: String, serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int) {
        for (i in Flats!!) {
            if (i.getName() == name) {
                Flats!!.remove(i)
            }
        }
    }
}