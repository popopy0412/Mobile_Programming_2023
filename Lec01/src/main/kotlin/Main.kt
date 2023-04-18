fun decimalDigitValue(c: Char): Int{
    if(c !in '0'..'9') throw IllegalArgumentException("Out of range")
    return c.code - '0'.code
}
fun main() {
    val data = '7'
    val num = decimalDigitValue(data)
    println(num)
}