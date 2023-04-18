import java.math.BigInteger

fun main(args: Array<String>) {
    val n = 10000
    var first = BigInteger("0")
    var second = BigInteger("1")
    println(fibonacci(n, first, second))

    val add: (Int, Int) -> Int = {x, y -> x+y}
}

tailrec fun fibonacci(n: Int, a: BigInteger, b: BigInteger) : BigInteger{
    return if(n == 0) a else fibonacci(n-1, b, a+b)
}