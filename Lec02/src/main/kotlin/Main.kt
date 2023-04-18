import java.util.*

fun main() {
    val scan = Scanner(System.`in`)

    print("학과 : ")
    val dept = scan.nextLine()
    print("학번 : ")
    val id = scan.nextLine()
    print("이름 : ")
    val name = scan.nextLine()
    print("가장 많이 사용하는 앱 : ")
    val app = scan.nextLine()

    println()
    println("=== 입력한 정보 확인 ===")
    println("학과 : $dept")
    println("학번 : $id")
    println("이름 : $name")
    println("가장 많이 사용하는 앱 : $app")
}
