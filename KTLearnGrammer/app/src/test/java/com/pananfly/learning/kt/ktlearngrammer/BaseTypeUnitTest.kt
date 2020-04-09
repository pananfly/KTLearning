package com.pananfly.learning.kt.ktlearngrammer

import org.junit.Test
import java.io.File

class BaseTypeUnitTest {

    private fun numTypesBits(type: Any) {
        when(type) {
            is Byte -> Byte.SIZE_BITS // 8
            is Short -> Short.SIZE_BITS // 16
            is Int -> Int.SIZE_BITS // 32
            is Long -> Long.SIZE_BITS // 64
            is Float -> 32
            is Double -> 64
            else -> 0
        }
    }

    private fun testTypePrintDouble(d: Double) {
        println(d)
    }

    @Test
    fun testTypeNumber() {
        val one = 1 // Int
        val threeBillion = 3000000000 // 超出Int自动判断为Long型
        println(if(threeBillion is Long) "Long" else "Int")
        val oneLong = 1L // Long
        val oneByte: Byte = 1 //Byte

        val pi = 3.14 // Double
        val eFloat = 2.71828f // Float
        testTypePrintDouble(pi)
//        testTypePrintDouble(one) // 类型不匹配
//        testTypePrintDouble(eFloat) // 类型不匹配

        // page 114

        // 不支持八进制
        val a = 123 // 十进制
        val b = 0x0F // 十六进制
        val c = 0b0010101 // 二进制
        val double1 = 123.5
        val double2 = 123.5e10
        val float1 = 123.5f
        val float2 = 123.5F

        // 数字下划线增加可读性
        val oneMillion = 1_000_000
        val creditCardNum = 1234_5678_9012_3456L
        val socialSecurityNumber = 999_99_9999L
        val hexBytes = 0xFF_EC_DE_5E
        val bytes = 0b11010101_01100010

        val boxReal: Int? = 100
        println("box compared ${a === a}")
        // 重新装箱-不一定保留同一性，保留相等性
        val boxSecond: Int? = boxReal
        val boxThird: Int? = boxReal
        println("box after compared ${boxSecond === boxThird}")
        println("box after compared value ${boxSecond == boxThird}")


        println("=========1==========")
        val justInt: Int? = 1000000000
        // 不被支持 小类型不能直接赋值给大类型
//        val justLong: Long? = justInt
//        println(justInt == justLong)
        // 可以使用显式转换
        val justLong2: Long? = justInt?.toLong()
        println("justLong2: $justLong2")

        // page 116
    }

    @Test
    fun testNumberDivide() {
        var x = 5 / 2.toDouble()
        println(x)
        var y = 5 / 2
        println(y)
        var z = 5L / 2
        println(z)
    }

    @Test
    fun testBitsCalc() {
        //位运算只适用于Int Long
        //左移
        val x = (1 shl 2)
        println(x)
        //左移再位与
        val x1 = (1 shl 2) and 0x000FF000
        println(x1)
        //右移
        val y = 4 shr 1
        println(y)
        //无符号右移
        val z = -4 ushr 8
        println(z)
        //位或
        val zof = 4 or 4
        println(zof)
        //位异或
        val zxor = 3 xor 4
        println(zxor)
        //位非= positive: -x - 1 ,negative: -x + 1
        val zinv = 10.inv()
        println(zinv)
    }

    private fun testCharToInt(c: Char): Int? {
        if(c in  '0'..'9') {
            return c.toInt() - '0'.toInt()
        }
        return null;
    }

    @Test
    fun testChar() {
        val c = '1'
        if(c == '2') {}
        println(testCharToInt(c))
        println(testCharToInt('a'))
    }

    @Test
    fun testArray() {
        //page 119
        val asc = Array(5) {i -> i * i}
        asc.forEach { println(it) }

        println("======1=======")
        //无装箱开箱的数组 　ByteArray ShortArray IntArray等 与Array不是继承关系，但有相同的方法和属性
        val x: IntArray = intArrayOf(1 , 2, 3)
        x[0] = x[1] + x[2]
        x.forEach { println(it) }
        println("======2=======")
        //初始化为0
        var arr1 = IntArray(5)
        arr1.forEach { println(it) }
        println("======3=======")
        //初始化为42
        var arr2 = IntArray(5){42}
        arr2.forEach { println(it) }
        println("======4=======")
        //初始化为
        var arr3 = IntArray(5){it}
        arr3.forEach { println(it) }
        // page 120
    }
}