package src.main.kotlin.Server.MainLog

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

open abstract class WorkWithClient:SQLConnection(){


    fun Send2Client(serverSocket: DatagramSocket, senderAddress: InetAddress, senderPort:Int, message:String){
        var sendingDataBuffer =ByteArray(100000)
        sendingDataBuffer = message?.toByteArray()
        // Создайте новый UDP-пакет с данными, чтобы отправить их клиенту
        val outputPacket = DatagramPacket(
            sendingDataBuffer, sendingDataBuffer.size,
            senderAddress, senderPort
        )
        println(sendingDataBuffer.toString())
        // Отправьте пакет клиенту
        serverSocket.send(outputPacket)

    }
    fun next(serverSocket: DatagramSocket){
        var receivingDataBuffer = ByteArray(100000)

        var inputPacket = DatagramPacket(receivingDataBuffer, receivingDataBuffer.size)
        receivingDataBuffer = ByteArray(100000)
        serverSocket.receive(inputPacket)
        println(WithOut(String(inputPacket.data)))
    }
    fun GetFromClient(serverSocket: DatagramSocket): String? {
        var receivingDataBuffer = ByteArray(100000)

        var inputPacket = DatagramPacket(receivingDataBuffer, receivingDataBuffer.size)
        receivingDataBuffer = ByteArray(100000)
        serverSocket.receive(inputPacket)
        println(WithOut(String(inputPacket.data)))
        return WithOut(String(inputPacket.data))
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