import java.io.IOException
import java.net.*
import java.util.concurrent.*
import java.util.concurrent.locks.ReentrantLock
import java.util.regex.Pattern


object UDPServer {
    // Серверный UDP-сокет запущен на этом порту



    var pattern: Pattern = Pattern.compile("[A-Za-z]+[ ]?[A-Za-z]*")
    val commander = Commander()
    var inputPacket: DatagramPacket? = null
    val executor:ThreadPoolExecutor = Executors.newFixedThreadPool(10) as ThreadPoolExecutor;
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>){
                var worker: () -> Unit = {NewServerThread()}
                executor.execute(worker)
                executor.shutdown()
                while (!executor.isTerminated) {
                }
                println("Finished all threads")
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
    private fun NewServerThread(){
        try{
            val ip: InetAddress
            try {
                ip = InetAddress.getLocalHost()
                println("Current IP address : " + ip.hostAddress)
            } catch (e: UnknownHostException) {
                e.printStackTrace()
            }
            /* Создайте экземпляр UDP-пакета для хранения клиентских данных с использованием буфера для полученных данных */
            println("Waiting for a client to connect...")
            /** Поле инициализации класса @see commander */
            val SERVICE_PORT = 5432
            val serverSocket = DatagramSocket(SERVICE_PORT)
            var user_id = 0;
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

                var numberOfProcessors = Runtime.getRuntime().availableProcessors()

                if (user_id==0){
                    var forkJoinPool = ForkJoinPool(numberOfProcessors)
                    val result: ForkJoinTask<Int> = forkJoinPool.submit(LogInAndAuth(serverSocket, senderAddress, senderPort, massage, commander))
                    user_id = result.join()
                }
                else if(massage!="") {
                    var forkJoinPool = ForkJoinPool(numberOfProcessors)
                    forkJoinPool.execute(interactive(serverSocket, senderAddress, senderPort, massage, commander, user_id))

                    commander.interactiveMod(massage, serverSocket, senderAddress!!, senderPort, user_id)
                }

            }
            serverSocket.close()
        } catch (e: SocketException) {
            e.printStackTrace()
        }
    }
    private class interactive(
        serverSocket: DatagramSocket,
        senderAddress: InetAddress,
        senderPort: Int,
        massage: String,
        commander: Commander,
        id:Int
    ) : RecursiveTask<Int>() {
        var id = 0
        var lock = ReentrantLock()
        var serverSocket:DatagramSocket?= null
        var senderAddress:InetAddress? = null
        var senderPort:Int? = null
        var message = ""
        var commander = null as Commander
        init {
            this.serverSocket = serverSocket
            this.senderAddress = senderAddress
            this.senderPort=senderPort
            this.message= message
            this.commander = commander
            this.id = id
        }

        override fun compute(): Int {
            lock.lock()
            try{
                commander.interactiveMod(message, serverSocket!!, senderAddress!!, senderPort!!, id)
            }finally{
                lock.unlock()
            }
            return id
        }
    }
    private class LogInAndAuth(
        serverSocket: DatagramSocket,
        senderAddress: InetAddress,
        senderPort: Int,
        massage: String,
        commander: Commander
    ) : RecursiveTask<Int>() {
        var id = 0
        var lock = ReentrantLock()
        var serverSocket:DatagramSocket?= null
        var senderAddress:InetAddress? = null
        var senderPort:Int? = null
        var message = ""
        var commander = null as Commander
        init {
            this.serverSocket = serverSocket
            this.senderAddress = senderAddress
            this.senderPort=senderPort
            this.message= message
            this.commander = commander
        }

        override fun compute(): Int {
            lock.lock()
            try{

                if (message=="logIn"){
                    id = commander.logInMod(serverSocket!!, senderAddress!!, senderPort!!)
                }
                else if (message=="authorize"){
                    id = commander.authuorizeMod(serverSocket!!, senderAddress!!, senderPort!!)
                }
            }finally{
                lock.unlock()
            }
            return id
        }
    }
}
