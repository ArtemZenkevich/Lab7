import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketException
import java.util.concurrent.*
import java.util.concurrent.locks.ReentrantLock
import java.util.regex.Pattern


object UDPServer {
    // Серверный UDP-сокет запущен на этом порту

    const val SERVICE_PORT = 50001
    val serverSocket = DatagramSocket(SERVICE_PORT)
    var user_id = 0;
    var pattern: Pattern = Pattern.compile("[A-Za-z]+[ ]?[A-Za-z]*")
    val commander = Commander()
    var inputPacket: DatagramPacket? = null
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>){
        var first = true
                try{
                /* Создайте экземпляр UDP-пакета для хранения клиентских данных с использованием буфера для полученных данных */
                println("Waiting for a client to connect...")
                /** Поле инициализации класса @see commander */
                var executor: ThreadPoolExecutor = (Executors.newFixedThreadPool(10) as ThreadPoolExecutor)
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
                    var forkJoinPool= ForkJoinPool()
                    if (first || user_id==0){
                     if (massage=="logIn"){
                         var lock:ReentrantLock = ReentrantLock()

                         var  result: ForkJoinTask<Int>? = forkJoinPool.submit(logInClass(senderAddress, senderPort, lock))
                         user_id = result?.join()!!
                     }
                     else if(massage=="authuorize"){
                         var lock:ReentrantLock = ReentrantLock()
                         var  result: ForkJoinTask<Int>? = forkJoinPool.submit(authuorizeModClass(senderAddress, senderPort, lock))
                         user_id = result?.join()!!

                     }
                        first = false
                    }
                    else if(massage!="") {
                        var lock:ReentrantLock = ReentrantLock()
                        var  result: ForkJoinTask<String?>? = forkJoinPool.submit(interactiveModClass(massage, senderAddress, senderPort, user_id, lock))
                        result?.join()
                    }

                }
                serverSocket.close()
            } catch (e: SocketException) {
                e.printStackTrace()
            }


}
    class resive(inputPacket: DatagramPacket, lock: ReentrantLock): Runnable{
        private var lock: ReentrantLock? = null
        var inputPacket: DatagramPacket? = null
        init {
            this.lock = lock
            this.inputPacket=inputPacket
        }

        override fun run() {
            lock?.lock()
            try {
                serverSocket.receive(inputPacket)
            }finally{
                lock?.unlock()
            }
        }
    }
    class authuorizeModClass(senderAddress: InetAddress, senderPort: Int, lock: ReentrantLock) : RecursiveTask<Int>() {
        private var lock: ReentrantLock? = null
        var senderAddress: InetAddress? =null
        var senderPort:Int = 0
        init{
            if (senderAddress != null) {
                this.lock = lock
                this.senderAddress=senderAddress
                this.senderPort=senderPort
            }
        }
        override fun compute(): Int {
            lock?.lock()
            try {
                return senderAddress?.let { commander.authuorizeMod(serverSocket, it, senderPort) }!!
            }finally {
                lock?.unlock()
            }
        }
    }
    class interactiveModClass(massage:String, senderAddress: InetAddress, senderPort: Int, user_id:Int, lock: ReentrantLock) : RecursiveTask<String?>() {
        private var lock: ReentrantLock? = null
        var massage:String =""
        var senderAddress: InetAddress? =null
        var senderPort:Int = 0
        var user_id = 0
        init{
            if (senderAddress != null) {
                this.lock = lock
                this.massage = massage
                this.senderAddress=senderAddress
                this.senderPort=senderPort
                this.user_id = user_id
            }
        }
        override fun compute(): String {
            lock?.lock()
            try {
            senderAddress?.let { commander.interactiveMod(massage, serverSocket, senderAddress!!, senderPort, user_id) }!!
            return ""
                }finally {
                lock?.unlock()
                }
        }
    }
    class logInClass(senderAddress: InetAddress, senderPort: Int, lock: ReentrantLock) : RecursiveTask<Int>() {
        private var lock: ReentrantLock? = null
        var senderAddress: InetAddress? =null
        var senderPort:Int = 0
        init{
            if (senderAddress != null) {
                this.lock = lock
                this.senderAddress=senderAddress
                this.senderPort=senderPort
            }
        }
        override fun compute(): Int {lock?.lock()
            try {
                return senderAddress?.let { commander.logInMod(serverSocket, it, senderPort) }!!
            }finally {
                lock?.unlock()
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