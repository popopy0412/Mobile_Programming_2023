import java.io.File
import java.util.*

class ClientManager {
    var client = mutableMapOf<String, Client>()
    init {
        val scan = Scanner(File("client.txt"))
        while(scan.hasNext()){
            val str = scan.nextLine()
            val line = str.split(" ")
            client[line[0]] = Client(line[0], line[1], line[2], line[3].toInt())
        }
    }
    fun findClient(id: String) = client[id]
    fun getPoint(c: Client) = c.point
    fun getTel(c: Client) = c.tel
}