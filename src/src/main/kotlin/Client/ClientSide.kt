import src.main.kotlin.Server.MainLog.SQLConnection
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketException
import java.sql.SQLException
import java.util.*
import java.util.regex.Pattern


object UDPClient {
    /* Порт сервера, к которому собирается
подключиться клиентский сокет */
    const val SERVICE_PORT = 50001
    val AddressIP = InetAddress.getByName("localhost")

    var scanner: Scanner =Scanner(System.`in`)
    var clientSocket = DatagramSocket()
    var pattern: Pattern = Pattern.compile("[A-Za-z]+[ ]?[A-Za-z]*")
    var line: String? = null
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        while (true) {
            var sendingDataBuffer = ByteArray(10000)
            var receivingDataBuffer = ByteArray(10000)
        try {
            line= scanner.nextLine().toString()
            if (line=="exit"){
                break
            }
                sendingDataBuffer = line.toString().toByteArray()
            // Создайте UDP-пакет
            val sendingPacket = DatagramPacket(sendingDataBuffer, sendingDataBuffer.size, AddressIP, SERVICE_PORT)
            // Отправьте UDP-пакет серверу
            clientSocket.send(sendingPacket)

            // Получите ответ от сервера, т.е. предложение из заглавных букв
            var receivingPacket = DatagramPacket(receivingDataBuffer, receivingDataBuffer.size)
            clientSocket.receive(receivingPacket)

            var receivedData = WithOut(String(receivingPacket.data))
            println(receivedData)
        } catch (e: SocketException) {
            System.err.println("Ошибка подключения к серверу")
            clientSocket.close()
            break
        }
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