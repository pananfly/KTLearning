package com.pananfly.learning.kt.ktlearngrammer

import org.junit.Test
import java.io.File
import java.lang.IllegalStateException

class BaseGrammerUnitTest {

    private fun test_fun_sum(a: Int, b: Int): Int {
        return a + b
    }

    private fun test_fun_sum2(a: Int, b: Int) = a + b;

    @Test
    fun test_fun() {
        //page 72
        println("3 + 5 = " + test_fun_sum(3 , 5))
        println("4 + 5 = ${test_fun_sum2(4 , 5)}")
    }

    @Test
    fun test_val() {
        val a: Int = 1 //立即赋值
        val b = 2 //自动推断Int
        val c: Int //没有立即赋值类型不能省略
        c = 3
        println("a = ${a}, b = ${b}, c = ${c}")
    }

    @Test
    fun test_var() {
        var x = 5
        x += 1
        println("x = ${x}")
    }

    @Test
    fun test_string_replace() {
        var a = 1
        val s = "a is ${a}"
        a = 2
        val s2 = "${s.replace("is" , "was")} , but now is ${a}"
        println(s2)
    }

    private fun test_if_maximum(a: Int , b: Int) = if(a > b) a else b

    @Test
    fun test_if() {
        println("max of 0 and 42 is ${test_if_maximum(0 , 42)}")
    }

    private fun test_null_return_parseInt(str: String?): Int? {
        return str?.toIntOrNull()
    }

    @Test
    fun test_null_return() {
        val x = test_null_return_parseInt("1")
        val y = test_null_return_parseInt(null);
        println("x = ${x}, y = ${y}")
    }

    private fun test_type_getStringLength(obj: Any): Int? {
        return if (obj is String) obj.length else null
    }

    @Test
    fun test_type() {
        val a = "fjldkajfladjfl"
        println("'${a}' string length is ${test_type_getStringLength(a)}")
        val b = 1
        println("'${b}' string length is ${test_type_getStringLength(b)}")
        val c = listOf<Any>(Any())
        println("'${c}' string length is ${test_type_getStringLength(c)}")
        val d = ""
        println("'${d}' string length is ${test_type_getStringLength(d)}")
    }

    @Test
    fun test_for() {
        val items = listOf<String>("1=[['t" , "2tr" , "354sx")
        for(item in items) {
            println("item: $item")
        }
        println()
        for(index in items.indices) {
            println("item[$index] is ${items[index]}")
        }
    }

    @Test
    fun test_while() {
        val items = listOf<String>("1=[['t" , "2tr" , "354sx")
        var index = 0
        while (index < items.size) {
            println("item[$index] is ${items[index]}")
            index ++
        }
    }

    private fun test_when_describe(obj: Any): String? = when(obj) {
        1 -> "One"
        "Hi" -> "Hello"
        is Long -> "Long type"
        !is String -> "Not a string"
        else -> "Unknown"
    }

    @Test
    fun test_when() {
        println(test_when_describe(1))
        println(test_when_describe("Hi"))
        println(test_when_describe(100L))
        println(test_when_describe(2))
        println(test_when_describe("fjdslkfjlas"))
    }

    @Test
    fun test_range() {
        val x = 1
        val y = 9
        //包含头和尾----> 1 =< x <= y+1
        if(x in 1 .. y+1) {
            println("Fit in range.")
        } else {
            println("Not in range.")
        }
        val list = listOf<String>("a" , "b" , "c")
        if(2 in 0..list.lastIndex) {
            println("2 in list index")
        }
        if(list.size !in 0..list.lastIndex) {
            println("list.size is out of index")
        }
        println("=====1======")
        for (x in 1..5) {
            println(x)
        }
        println("=====2======")
        for (x in 1..10 step 2) { //上升-步进2
            println(x)
        }
        println("=====3======")
        for (x in 9 downTo 0 step 3) {//下降-步进3，步进可不设
            println(x)
        }
    }

    @Test
    fun test_set() {
        val items = listOf<String>("189fmg" , "2fdsa" , "fdyyusa" , "1jk" , "12ghj")
        for(item in items) {
            println(item)
        }
        println("==========1==========")
        //符合后就不再运行后面的
        when {
            "189fmg" in items -> println("Success!")
            "fjdsaj" !in items -> println("fjdsaj can not found.")
        }
        when {
            2 == 1 -> println("=＝＝＝＝＝＝")
            1 == 1 -> println("HHHHHHHHHHHH")
        }
        println("==========2==========")
        items.filter { it.startsWith("1") }
            .sortedBy { it }
            .map { it.toUpperCase() }
            .forEach { println(it) }

        //Page 84
    }

    abstract class Shape(val sides: List<Double>) {
        val perimeter: Double get() = sides.sum()
        abstract fun calculateArea(): Double
    }

    interface RectangleProperties {
        val isSquare: Boolean
    }

    class Rectangle(var width: Double , var height: Double)
        :Shape(listOf(width , height , width , height)) , RectangleProperties {
        override val isSquare: Boolean get() = width == height
        override fun calculateArea(): Double =width * height
    }

    class Triangle(var sideA: Double , var sideB: Double , var sideC: Double)
        :Shape(listOf(sideA , sideB , sideC)) {
        override fun calculateArea(): Double {
            val s = perimeter / 2
            return Math.sqrt(s * (s - sideA)* (s - sideB)* (s - sideC))
        }
    }

    @Test
    fun test_class_simple() {
        val rectangle = Rectangle(5.0 , 2.0)
        val triangle = Triangle(3.0 , 4.0 , 5.0)
        println("Area of rectangle is ${rectangle.calculateArea()}, it's perimeter is ${rectangle.perimeter}")
        println("Area of triangle is ${triangle.calculateArea()}, it's perimeter is ${triangle.perimeter}")
        //page 85
    }

    @Test
    fun test_map() {
        val map = mapOf<String, Int>("a" to 1 , "b" to 2 , "c" to 3)
        for((k , v) in map) {
            println("K: $k -> V: $v")
        }
        println("=====1====")
        println("${map["a"]}")//不可以是${map['a']}，key类型不匹配
        var map2 = mutableMapOf<Int , String>(1 to "fsdfl")
        map2[4] = "sffsa"
        println("=====2====")
        println(map2[0])//not exist is null
        println(map2[1])
        println(map2[2])
        println(map2[4])
    }

    fun String.expendFunc(): Int ? {
        return length;
        //return this.length; //The two are same
    }

    @Test
    fun test_expend_func() {
        val str = "121"
        println(str.expendFunc())
    }

    object Resource {
        const val name = "Single instance resource"
        var id = 0;
    }

    @Test
    fun test_single_obj() {
        println(Resource.name)
        Resource.id = 100
        println(Resource.id)
    }

    @Test
    fun test_if_not_null() {
        val files = File("Test").listFiles()
        println(files?.size)
    }

    @Test
    fun test_if_not_null_else() {
        val files = File("Test").listFiles()
        println(files?.size ?: "empty")
    }

    @Test
    fun test_if_null() {
        val emails = mapOf<String , String>("email" to "111@mail.com" , "email2" to "222@mail.com")
        val email = emails["email"] ?: throw IllegalStateException("email unexist")
        println(email)
        val email2 = emails["email3"] ?: "Can not found email3"
        println(email2)
        println("====1=====")
        val emails2 = listOf<String>("2sfas" , "5345325")
        val firstEmail = emails2.firstOrNull() ?: ""
        println("first email: $firstEmail")
        val emails3 = listOf<String>()
        val firstEmail2 = emails3.firstOrNull() ?: "list is empty"
        println("first email2: $firstEmail2")
        //page 88
    }
}