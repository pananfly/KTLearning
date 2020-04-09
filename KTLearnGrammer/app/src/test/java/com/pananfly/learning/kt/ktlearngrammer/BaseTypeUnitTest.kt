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
}