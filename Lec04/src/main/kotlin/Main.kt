import java.util.Scanner

fun main() {
    println("201911218 천성필")
    var scan = Scanner(System.`in`)
    val manager = ClientManager()
    while(true){
        println("1) 포인트 검색  2) 전화번호 검색  3) 종료")
        print("메뉴를 선택하세요 : ")
        var input = scan.nextLine()
        println()
        when(input) {
            "1" -> {
                print("회원 아이디를 입력하세요: ")
                val name = scan.nextLine()
                if (manager.findClient(name) != null)
                    println("${name}님의 포인트는 ${manager.getPoint(manager.findClient(name)!!)} 입니다.")
                else
                    println("아이디를 확인해주세요.")
            }

            "2" -> {
                print("회원 아이디를 입력하세요: ")
                val name = scan.nextLine()
                if (manager.findClient(name) != null)
                    println("${name}님의 전화번호는 ${manager.getTel(manager.findClient(name)!!)} 입니다.")
                else
                    println("아이디를 확인해주세요.")
            }

            "3" -> break

            else -> println("잘못 입력하셨습니다. 다시 입력해주세요.")
        }
        println()
    }
    println("시스템을 종료합니다.")
}
