import src.main.kotlin.Server.MainLog.SQLConnection
import src.main.kotlin.Server.MainLog.WorkWithClient
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketException
import java.sql.SQLException
import java.util.regex.Pattern

object UDPServer {
    // Серверный UDP-сокет запущен на этом порту
    const val SERVICE_PORT = 50001
    val serverSocket = DatagramSocket(SERVICE_PORT)

    var pattern: Pattern = Pattern.compile("[A-Za-z]+[ ]?[A-Za-z]*")
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>){
        var first = true
                try{
                /* Создайте буферы для хранения отправляемых и получаемых данных.
Они временно хранят данные в случае задержек связи */


                /* Создайте экземпляр UDP-пакета для хранения клиентских данных с использованием буфера для полученных данных */

                println("Waiting for a client to connect...")
                /** Поле инициализации класса @see commander */
                val commander = Commander()
                /** Включение интерактивного режима @see commander */
                while (true) {
                    var receivingDataBuffer = ByteArray(10000)
                    var sendingDataBuffer = ByteArray(10000)
                    var inputPacket = DatagramPacket(receivingDataBuffer, receivingDataBuffer.size)
                    // Получите данные от клиента и сохраните их в inputPacket
                    serverSocket.receive(inputPacket)
                    // Выведите на экран отправленные клиентом данные
                    var receivedData = String(inputPacket.data)

                    var massage: String = WithOut(receivedData)
                    //Начало основной логики работы
                    val senderAddress = inputPacket.address
                    val senderPort = inputPacket.port
                    if (first){
                     if (massage.endsWith(""))
                    }
                    else {
                        commander.interactiveMod(massage, serverSocket, senderAddress, senderPort)
                    }
 /*                   sendingDataBuffer = receivedData.toByteArray()

                    // Создайте новый UDP-пакет с данными, чтобы отправить их клиенту
                    val outputPacket = DatagramPacket(
                        sendingDataBuffer, sendingDataBuffer.size,
                        senderAddress, senderPort
                    )

                    // Отправьте пакет клиенту
                    serverSocket.send(outputPacket)
*/
                }
                serverSocket.close()
            } catch (e: SocketException) {
                e.printStackTrace()
            }

    }
    fun WithOut(line:String):String {
        var end: String?= ""
        for (a in line){
            if (a!='\u0000') end += a
            else {
                break
            }
        }
        return end!!
    }
}