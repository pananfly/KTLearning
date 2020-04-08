package com.pananfly.learning.kt.ktlearngrammer

import org.junit.Test

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
    }
}