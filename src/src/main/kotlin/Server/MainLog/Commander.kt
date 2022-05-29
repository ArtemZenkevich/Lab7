import src.main.kotlin.Server.MainLog.WorkWithClient
import src.main.kotlin.Server.MainLog.md5Custom
import sun.security.util.Password
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.*
/**
 * Класс реализации логики приказывания команд менеджеру коллекций
 */
class Commander :WorkWithClient(){

                /**
                 * Поле - менеджер
                 */
    private var manager: CollectionManager? = CollectionManager()
    /**
     * Поле считающее действующую команду
     */

    /**
     * Поле первой части команды
     */
    private var first_word: String = ""
    /**
     * Поле второй части команды
     */
    private var second_word: String? = null
    /**
     * Поле считающее действующую команду без пробелов по бокам
     */
    private var finalUserCommand: String? = null

    /**
     * Поле массива действующей истории команд
     */
    private var history: Array<String>? = Array(13, { i -> "NONE" })
    /**
     * Поле длины истории
     */
    private var historylong: Int = 0
    /**
     * Функция активирования интерактивного режима взаимодействия с коллекцией
     */
    fun interactiveMod(commandReader:String, serverSocket:DatagramSocket, senderAddress: InetAddress, senderPort:Int, user_id: Int) {
            var userCommand: String? = null
            userCommand = commandReader
            finalUserCommand = userCommand?.trim().toString()
            val final: String? = finalUserCommand
            first_word = final?.substringBefore(" ").toString()
            second_word = final?.substringAfter(" ").toString()
            historycontrol()
            try {
                when (first_word) {
                    "remove_by_id" -> {
                        manager?.remove_by_id(second_word, serverSocket, senderAddress, senderPort, user_id)
                    }
                    "add" -> {
                        manager?.add(serverSocket, senderAddress, senderPort, user_id)
                    }
                    "remove_greater" -> {
                        manager?.remove_greater(second_word, serverSocket, senderAddress, senderPort, user_id)
                    }
                    "show" -> {
                        manager?.show(serverSocket, senderAddress, senderPort, user_id)
                    }
                    "clear" -> {
                        manager?.clear(serverSocket, senderAddress, senderPort, user_id)
                    }
                    "info" -> {
                        System.out.println("Тип: PriorityQueue<Flat>, время создания:" + manager!!.getTime())
                    }
                    "add_if_max" -> {
                        manager?.add_if_max(serverSocket, senderAddress, senderPort, user_id)
                    }
                    "help" -> {
                        manager?.help(serverSocket, senderAddress, senderPort)
                    }
                    "remove_all_by_house" -> {
                        manager?.remove_all_by_house(second_word!!, serverSocket, senderAddress, senderPort, user_id)
                    }
                    "remove_contains_name" -> {
                        manager?.remove_contains_name(second_word!!, serverSocket, senderAddress, senderPort, user_id)
                    }
                    "filter_contains_name" -> {
                        manager?.filter_contains_name(second_word!!, serverSocket, senderAddress, senderPort, user_id)
                    }
                    "filter_less_than_house" -> {
                        manager?.filter_less_than_house(second_word!!, serverSocket, senderAddress, senderPort, user_id)
                    }
                    "exit" -> {
                    }
                    "history" -> {
                        history(serverSocket, senderAddress, senderPort)
                    }
                    else -> {
                        Send2Client(serverSocket, senderAddress, senderPort, "Неопознанная команда. Наберите 'help' для справки.")
                    }
                }
            } catch (ex: ArrayIndexOutOfBoundsException) {
                Send2Client(serverSocket, senderAddress, senderPort, "Отсутствует аргумент.")
                System.out.println("Отсутствует аргумент.")
            }


    }
    fun logInMod( serverSocket:DatagramSocket, senderAddress: InetAddress, senderPort:Int ): Int? {
        var connection = getDBConnection()
        Send2Client(serverSocket, senderAddress, senderPort, "Surname:")
        var surname = GetFromClient(serverSocket)
        Send2Client(serverSocket, senderAddress, senderPort, "Password:")
        var Password = GetFromClient(serverSocket)?.let { md5Custom(it) }
        var res = getData("SELECT * FROM PERSON WHERE USERNAME = '$surname';", connection)
        var id = getData("SELECT * FROM PERSON;", connection)?.let { getMaxId(it) }?.plus(1)
        if (res?.next() == false) {
            if (connection != null) {
                insertIntoDB("INSERT INTO PERSON VALUES($id, '$surname', '$Password');", connection)
                Send2Client(serverSocket, senderAddress, senderPort, "Вы зарегистрировались $surname.")
                return id.toString().toInt()
            }
        }
        else{
            res = getData("SELECT * FROM PERSON WHERE USERNAME = '$surname';", connection)
            while(res?.next() == true) {
                Send2Client(
                    serverSocket, senderAddress, senderPort, "Уже существует $surname. Попробуйте снова.\n" +
                            "Surname:"
                )
                var surname = GetFromClient(serverSocket)
                Send2Client(serverSocket, senderAddress, senderPort, "Password:")
                var Password = GetFromClient(serverSocket)?.let { md5Custom(it) }
                var res = getData("SELECT * FROM PERSON WHERE USERNAME = '$surname';", connection)
                var id = getData("SELECT * FROM PERSON;", connection)?.let { getMaxId(it) }?.plus(1)
                if (res?.next() == true) {
                    if (connection != null) {
                        insertIntoDB("INSERT INTO PERSON VALUES($id, '$surname', '$Password');", connection)
                        Send2Client(serverSocket, senderAddress, senderPort, "Вы зарегистрировались $surname.")
                        return id
                    }
                }
            }
        }
        return id.toString().toInt()
    }
    fun authuorizeMod( serverSocket:DatagramSocket, senderAddress: InetAddress, senderPort:Int): Int{
        var id:Int =0
        Send2Client(serverSocket, senderAddress, senderPort, "Surname:")
        var surname = GetFromClient(serverSocket)
        Send2Client(serverSocket, senderAddress, senderPort, "Password:")
        var Password = GetFromClient(serverSocket)?.let { md5Custom(it) }
        var res = getData("SELECT * FROM PERSON WHERE USERNAME='$surname' AND USER_PASSWORD='$Password'", getDBConnection())
        if (res?.next() == true) {
            Send2Client(serverSocket, senderAddress, senderPort, "Вы зарегистрировались.")
            id = res.getInt("USER_ID")
            return id
        }else{

            while(res?.next() == false) {
                Send2Client(serverSocket, senderAddress, senderPort, "Неверное имя или пароль.\n" +
                        "Surname:")
                var surname = GetFromClient(serverSocket)
                Send2Client(serverSocket, senderAddress, senderPort, "Password:")
                var Password = GetFromClient(serverSocket)?.let { md5Custom(it) }
                res = getData("SELECT * WHERE USER_NAME='$surname' AND USER_PASSWORD='$Password'", getDBConnection())

            }
            Send2Client(serverSocket, senderAddress, senderPort, "Вы зарегистрировались.")
            id = res?.getInt("USER_ID")!!
            return id
            }
    }
    /**
     * Функция взаимодействия с историей команд
     */
    private fun historycontrol() {
        historylong++
        if (historylong > 13) {
            for (i in 1..12) {
                history?.set(i - 1, history!![i])
            }
            first_word.let { history?.set(12, it) }
            historylong--
        } else {
            history?.set(historylong - 1, first_word)
        }
    }
    /**
     * Функция вывода истории
     */
    private fun history(serverSocket:DatagramSocket, senderAddress: InetAddress, senderPort:Int) {
        var info: String=""
        for (i in 0..historylong - 1) {
            info += history?.get(i) + " "
        }
        if (info != null) {
            Send2Client(serverSocket, senderAddress, senderPort, info)
        }
        else(Send2Client(serverSocket, senderAddress, senderPort, "История пуста."))

    }

}