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
        //初始化为0-4
        var arr3 = IntArray(5){it}
        arr3.forEach { println(it) }
        // page 120
    }

    @Test
    fun testUnSignNumber() {
        val b: UByte = 1u // UByte
        val s: UShort = 1U // UShort
        val l: ULong = 1u // ULong

        val a1 = 42u // UInt
        val a2 = 0xFFFF_FFFF_FFFFu // ULong:未提供预期类型，不适用于UInt
    }

    @Test
    fun testString() {
        val str = "abcd"
        for(c in str) {
            println(c)
        }
        println("=====1========")
        val s = "abc" + "efg" + 1
        println(s)
        println("=====2========")
        // 文档说$符号不支持转义，试了为啥又是可以???
        val s2 = "abc\$\n123"
        println(s2)
        println("=====3========")
        val text = """
            for (c in "foo")
                println(c)
        """
        println(text)
        println("=====4========")
        //只清除前后空格，并不能对齐到0位置
        val text2 = """
            for (c in "foo")
                println(c)
        """.trimMargin()
        println(text2)
        println("=====5========")
        // 对齐到0
        val text3 = """
            for (c in "foo")
                println(c)
        """.trimIndent()
        println(text3)
        println("=====6========")
        // 不支持转义的类似$符号，可以使用如下方式
        val price = """
            ${'$'}9.99
        """.trimIndent()
        println("$price is cheap")
    }

    @Test
    fun testIf() {
        val a = 1
        val b = 2
        var max: Int
        if (a > b) {
            max = a
        } else {
            max = b
        }

        //表达式需要有else
        val max2 = if (a > b) a else b
        //最后的表达式代表返回的值
        val max3 = if(a > b) {
            println("a")
            a
            a + 1
        } else {
            println("b")
            println("c")
            b
            b + 3
        }
        println(max3)
    }

    @Test
    fun testWhen() {
        val x = 1

        when (x) {
            1 -> println("x==1")
            2 -> println("x==2")
            else -> println("x not 1 or 2")
        }
        when (x) {
            1 , 2 -> println("x==1 or x==2")
            else -> println("x not 1 or 2")
        }
        println("=====1=====")
        val y = when(x) {
            1 , 2 -> {
                println("x==1 or x==2")
                x
            }
            else -> {
                println("not match")
                0
            }
        }
        println(y)
        println("=====2=====")
        when(x) {
            in 1..10 -> println("x is in 1 to 10.")
            !in 10..20 -> println("x is not in 10 to 20.")
            else -> println("not match.")
        }
        println("=====3=====")
        fun iiii(x1: Any) = when(x1) {
            is String -> x1.startsWith("p")
            is Int -> x1 == 10
            else -> false
        }
        val z = iiii("cvc")
        println(z)
        println("=====4=====")
        fun executeRequest(): Int {
            return 1
        }
        fun getValue() =
            when(val response = executeRequest()) {
                in 1..10 -> response + 1
                else -> 0
            }
        println(getValue())

    }

    @Test
    fun testArrayOfWithIndex() {
        val array = arrayOf("a" , "b" , "c")
        for(i in array.indices) {
            println(array[i])
        }
        println("===1===")
        for((index , value) in array.withIndex()) {
            println("$index = $value")
        }
    }

    private fun testBreakAndContinueLabelFoo() {
        //return to call func
        listOf(1 , 2 , 3 , 4 , 5).forEach {
            if(it == 3) return
            println(it)
        }
        println("Un reach end.")
    }

    private fun testBreakAndContinueLabelFoo2() {
        //return to forEach
        listOf(1 , 2 , 3 , 4 , 5).forEach lit@{
            if(it == 3) return@lit
            println(it)
        }
        println("reach end.")
    }

    private fun testBreakAndContinueLabelFoo3() {
        //return to forEach
        listOf(1 , 2 , 3 , 4 , 5).forEach {
            if(it == 3) return@forEach
            println(it)
        }
        println("reach end.")
    }

    private fun testBreakAndContinueLabelFoo4() {
        //return to forEach 匿名内部类
        listOf(1 , 2 , 3 , 4 , 5).forEach(fun(value: Int) {
            if(value == 3) return
            println(value)
        })
        println("reach end.")
    }

    private fun testBreakAndContinueLabelFoo5() {
        //return to loop
        run loop@{
            listOf(1 , 2 , 3 , 4 , 5).forEach {
                if (it == 3) return@loop
                println(it)
            }
        }
        println("reach end.")
    }

    private fun testBreakAndContinueLabelFoo6() {
        //return to loop 返回return 后的值
        val xx = run loop@{
            listOf(1 , 2 , 3 , 4 , 5).forEach {
                if (it == 6) return@loop it
                println(it)
            }
            return@loop 0
        }
        println("reach end xx:$xx.")
    }

    @Test
    fun testBreakAndContinueLabel() {
        // loop 等标签名称只要是有效写法的就可以
        loop@ for (i in 1..5) {
            for (j in 1..3) {
                println("i:$i -> j:$j")
                if(i == 2 && j == 2) {
                    break@loop
                }
            }
        }
        println("====1=====")
        loop@ for (i in 1..5) {
            for (j in 1..3) {
                println("i:$i -> j:$j")
                if(i == 2 && j == 2) {
                    continue@loop
                }
            }
        }
        println("====2=====")
        testBreakAndContinueLabelFoo()
        println("====3=====")
        testBreakAndContinueLabelFoo2()
        println("====4=====")
        testBreakAndContinueLabelFoo3()
        println("====5=====")
        testBreakAndContinueLabelFoo4()
        println("====6=====")
        testBreakAndContinueLabelFoo5()
        println("====7=====")
        testBreakAndContinueLabelFoo6()

        // page 135
    }
}