import jdk.nashorn.internal.runtime.JSType.isNumber
import src.main.kotlin.Server.MainLog.WorkWithClient
import java.io.*
import java.net.DatagramSocket
import java.net.InetAddress
import java.sql.Connection
import java.util.*

/**
 * Класс реализации приказов {@see CommanderManager}
 */
class CollectionManager:WorkWithClient() {
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
    fun add(serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int, user_id: Int){
        try {
            var connection: Connection? = getDBConnection()
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
            var id1: Int? =null
            var rs = getData("SELECT * FROM FLATS;", connection)
            while (rs?.next() == true) {
                id1 = rs.getInt("FLAT_ID")?.plus(1)
            }
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
            var info1 = "INSERT INTO Flats VALUES($id1, '$name1', $x1, $y1, '$time', $area1, $numberOfFloors1, $price1, '$furnish1', '$view1', '$houseName1', $user_id);"
            var id_house: Int? = 1
            var rl = getData("SELECT * FROM FLATS;", connection)
            while (rs?.next() == true) {
                id_house = rl?.getInt("FLAT_ID")?.plus(1)
            }

            var info2 = "INSERT INTO HOUSE VALUES($id_house, '$houseName1', $numberOfFloors1);"
            if (connection != null) {
                insertIntoDB(info1, connection)
                insertIntoDB(info2, connection)
            }

            Send2Client(serverSocket, senderAddress, senderPort, "Элемент удачно добавлен.")
        } catch (ex: ArrayIndexOutOfBoundsException) {
            Send2Client(serverSocket, senderAddress, senderPort, "Ошибка переполнения коллекции")
        }
    }
    /**
     * Функция обновления элементов коллекции
     */
    fun update(serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int, user_id: Int){
        try {
            var flag = true
            var answer: String? = null
            var name1: String? = null
            var connection: Connection? = getDBConnection()
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
            var id1: Int? =null
            if(getData("SELECT MAX('flat_id') FROM Flats;", connection!!)?.getInt("flat_id")!=null) {
                id1 = getData("SELECT MAX('flat_id') FROM Flats;", connection!!)?.getInt("flat_id")?.plus(1)
            }
            else{
                id1=1
            }
            var x1: String? = null
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

            var y1: String? = null

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

            var area1: String? = null
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
            var numberOfRooms1: String? = null

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

            var price1: String? = null

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
            var year1: String? = null
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
            var numberOfFloors1: String? = null
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

            var info = "UPDATE Flats SET FLAT_ID = $id1, FLAT_NAME = '$name1', COORDINATES_X = $x1, COORDINATES_Y = $x1, CREATION_DATE = '$time', AREA = $area1, NUMBER_OF_ROOMS = $numberOfRooms1, PRICE = $price1, FURNISH = '$furnish1', VIEW = '$view1', HOUSE = '$houseName1', USER_ID= $user_id;"
            if (connection != null) {
                UpdateDB(info, connection)
            }
        } catch (ex: ArrayIndexOutOfBoundsException) {
            Send2Client(serverSocket, senderAddress, senderPort, "Не правильно введенное количество этажей дома.")
        }
    }
    /**
     * Функция удаления элементов, превышающее значения элемента параметра
     * @param float имя элемента коллекции
     */
    fun remove_greater(float: String?, serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int, user_id: Int) {

        var connection: Connection? = getDBConnection()
        if (connection != null) {
            delInDB("DELETE FLATS WHERE AREA > $float AND USER_ID=$user_id;", connection)
            Send2Client(serverSocket, senderAddress, senderPort, "Было успешно удалено.")
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
    fun show(serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int, user_id: Int) {
        var connection: Connection? = getDBConnection()
        var rs = getData("SELECT * FROM FLATS;", connection)
        var info = ""
        while (rs?.next() == true) {
            info+="FLAT_ID = ${rs.getString("FLAT_ID")}, FLAT_NAME = '${rs.getString("FLAT_NAME")}', COORiNATE_X = ${rs.getString("COORiNATE_X")}, COORDINATE_Y = ${rs.getString("COORDINATE_Y")}, CREATION_DATE = '${rs.getString("CREATION_DATE")}', AREA = ${rs.getString("AREA")}, NUMBER_OF_ROOMS = ${rs.getString("NUMBER_OF_ROOMS")}, PRICE = ${rs.getString("PRICE")}, FURNISH = '${rs.getString("FURNISH")}', VIEW = '${rs.getString("VIEW")}', HOUSE = '${rs.getString("HOUSE")}'\n"
        }
        Send2Client(serverSocket, senderAddress, senderPort, info)
    }
    /**
     * Функция очистки коллекции
     */
    fun clear(serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int, user_id: Int) {
        var connection: Connection? = getDBConnection()
        if (connection != null) {
            delInDB("DELETE FROM FLATS WHERE USER_ID = $user_id;", connection)
            Send2Client(serverSocket, senderAddress, senderPort, "Было успешно удалено.")
        }

    }
    /**
     * Функция удаления элемент по Id
     * @param id
     */
    fun remove_by_id(id: String?, serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int, user_id: Int) {
        var connection: Connection? = getDBConnection()
        if (connection != null) {
            delInDB("DELETE FROM FLATS WHERE FLAT_ID=$id AND USER_ID=$user_id;", connection)
            Send2Client(serverSocket, senderAddress, senderPort, "Было успешно удалено.")
        }
    }
    /**
     * Функция загрузки Json файла с коллекцией элементов
     * @param CollactionPath
     */

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

    /**
     * Функция добавления элемента в случае превосходства
     */

    fun add_if_max(serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int, user_id: Int) {
        try {
            var connection: Connection? = getDBConnection()
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
            var id1: Int? =null
            if(getData("SELECT MAX('flat_id') FROM Flats;", connection!!)?.getInt("flat_id")!=null) {
                id1 = getData("SELECT MAX('flat_id') FROM Flats;", connection!!)?.getInt("flat_id")?.plus(1)
            }
            else{
                id1=1
            }
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
            var info = "UPDATE Flats SET FLAT_ID = $id1, FLAT_NAME = '$name1', COORDINATES_X = $x1, COORDINATES_Y = $x1, CREATION_DATE = '$time', AREA = $area1, NUMBER_OF_ROOMS = $numberOfRooms1, PRICE = $price1, FURNISH = '$furnish1', VIEW = '$view1', HOUSE = '$houseName1', USER_ID = '$user_id';"
            var max = getData("SELECT MAX(AREA) FROM FLAT;", connection)?.getString("AREA")

            if (max != null) {
                if (max< area1.toString()) {
                    if (connection != null) {
                        insertIntoDB(info, connection)
                        println("Элемент успешно добавлен.")
                    }

                }
            }
        } catch (ex: ArrayIndexOutOfBoundsException) {
            println("Ошибка переполнения коллекции")
        }
    }

    fun remove_all_by_house(houseName: String, serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int, user_id: Int) {
        var connection: Connection? = getDBConnection()
        var info ="DELETE FROM FLATS WHERE HOUSE = '$houseName' AND USER_ID = '$user_id';"
        if (connection != null) {
            delInDB(info, connection)
        }
        Send2Client(serverSocket, senderAddress, senderPort, "Было удалено успешно")
    }

    fun remove_contains_name(name: String, serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int, user_id: Int) {
        var connection: Connection? = getDBConnection()
        var info ="DELETE FROM FLATS WHERE FLAT_NAME = '$name' AND USER_ID = '$user_id';"
        if (connection != null) {
            delInDB(info, connection)
        }
        Send2Client(serverSocket, senderAddress, senderPort, "Было удалено успешно")

    }
    /**
     * Функция вывода информации об элементе с определенным домом
     * @param name имя дома нужного элемента
     */
    fun filter_less_than_house(name: String, serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int, user_id: Int) {
        var connection: Connection? = getDBConnection()
        var info ="DELETE FLATS WHERE FLAT_NAME = '$name' AND USER_ID = '$user_id';"
        if (connection != null) {
            delInDB(info, connection)
        }
        Send2Client(serverSocket, senderAddress, senderPort, "Было удалено успешно")
    }
    /**
     * Функция вывода информации об элементе с определенным именем
     * @param name имя изменяемого элемента
     */
    fun filter_contains_name(name: String, serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int, user_id: Int) {
        var connection: Connection? = getDBConnection()
        var rs = getData("SELECT * FROM FLATS WHERE FLAT_NAME='$name' AND USER_ID='$user_id';", connection)
        var info = ""
        while (rs?.next() == true) {
            info+="FLAT_ID = ${rs.getString("FLAT_ID")}, FLAT_NAME = '${rs.getString("FLAT_NAME")}', COORDINATES_X = ${rs.getString("COORDINATES_X")}, COORDINATES_Y = ${rs.getString("COORDINATES_Y")}, CREATION_DATE = '${rs.getString("CREATION_DATE")}', AREA = ${rs.getString("AREA")}, NUMBER_OF_ROOMS = ${rs.getString("NUMBER_OF_ROOMS")}, PRICE = ${rs.getString("PRICE")}, FURNISH = '${rs.getString("FURNISH")}', VIEW = '${rs.getString("VIEW")}', HOUSE = '${rs.getString("HOUSE")}'\n"
        }
        Send2Client(serverSocket, senderAddress, senderPort, info)
    }
}