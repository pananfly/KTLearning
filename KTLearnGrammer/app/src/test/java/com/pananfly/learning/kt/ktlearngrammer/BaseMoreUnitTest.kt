@file:JvmName("BaseMoreUnitTest") // 必须在顶层切必须在package之前
package com.pananfly.learning.kt.ktlearngrammer

import android.view.View
import org.junit.Test
import kotlin.reflect.KClass
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaGetter

class BaseMoreUnitTest {
    data class Result(val result: Int, val status: String)

    fun testDecompileObject(): Result {
        return Result(0, "pananfly")
    }

    fun testTypeConvert(x: Any) {
        if(x !is String) return
        println(x.length)
        // `||` 右侧的x自动转换为字符串类型
        if(x !is String || x.length == 0) return
        when(x) {
            is Int -> 123
            is String -> x.length + 1 // 右侧的x自动转换为字符串类型
        }
    }

    fun testTypeConvert2(y: Any) {
        val x: String = y as String // 不安全的转换, 会抛出异常
        val x2: String? = y as String? // 安全的转换, 不会抛异常
        val x3: String? = y as? String // 安全可空的转换，在失败时返回null
    }

    @Test
    fun testDecompile() {
        val (result, status) = testDecompileObject()
        println("result:$result, status:$status")
        val (_, status1) = testDecompileObject()
        val maps = mapOf<String, String>("123" to "1111", "234" to "2222")
        println(maps.mapValues{ (_, value): Map.Entry<String, String> -> "$value!" }) // 指定整个解构的参数类型
        println(maps.mapValues{ (_, value: String) -> "$value??" }) // 指定特定组件类型

        // page 431
    }

    fun testGeneric1(some: Any?) {
        if(some is List<*>) {
            some.forEach {
                println(it) // it-> Any?
            }
        }
//        if(some is ArrayList<String>) { // 不能类型擦除为某种具体的类型
//
//        }
    }

    fun testGeneric2(some: List<String>) {
        if(some is ArrayList) { // 此处类型判断可省略范型指定
            // some 在这里会自动转换为Array<String>
        }
        some as ArrayList
    }

    inline fun <reified A, reified B> Pair<*, *>.asPairOf(): Pair<A, B>? {
        if(first !is A || second !is B) return null
        return first as A to second as B
    }
    inline fun <reified T> List<*>.asListOfType(): List<T>? =
        if(all {it is T})
            @Suppress("UNCHECKED_CAST")
            this as List<T>
        else null

    @Test
    fun testGeneric3() {
        val some: Pair<Any?, Any?> = "items" to listOf(1, 2, 3)
        println(some)
        val stringToSome = some.asPairOf<String, Any>()
        println("stringToSome: $stringToSome")
        val stringToInt = some.asPairOf<String, Int>()
        println("stringToInt: $stringToInt") // null
        val stringToList = some.asPairOf<String, List<*>>()
        println("stringToList: $stringToList") //
        val stringToListString = some.asPairOf<String, List<String>>() // 破坏类型安全
        println("stringToListString: $stringToListString") //
    }

    class ThisA {
        val a1 = 1
        inner class ThisB {
            val b1 = 2
            fun Int.foo() {
                val a = this@ThisA // ThisA的this
                val b = this@ThisB // THisB的this
                val c = this // foo()的接收者，一个Int
                val c1 = this@foo // foo()的接收者，一个Int

                val funLit = lambda@ fun String.() {
                    val d = this // funLit的接收者
                    return@lambda
                }
                val funLit2 = {
                    s: String -> val e = this // foo()的接收者，因为包含的lambda表达式没有任何接收者
                }
                funLit2("123")
            }
        }
    }

    @Test
    fun testEqual() {
        val a: Any? = null
        val b = null
        println(a == b) // 结构相等比较
        println(a === b) // 饮用相等比较
        println(a?.equals(b) ?: (b === null)) // b == null 跟 b === null 是等价的，只有跟null或者原生类型之间比较时才等价
    }

    operator fun Point.unaryMinus() = Point(-x, -y) // -Point
    // 一元操作符重载
    data class Point(var x: Int, var y: Int)
    operator fun Point.unaryPlus() = Point(+x, +y) // +Point
//    operator fun Point.not() = Point(!x, !y) // !Point
//    operator fun Point.inc(): Point = Point(x++, y++) // Point++
//    operator fun Point.dec(): Point = Point(x--, y--) // Point--

    // 二元操作符
    operator fun Point.plus(point: Point): Point = Point(x + point.x, y + point.y) // Point + Point
    operator fun Point.minus(point: Point): Point = Point(x - point.x, y - point.y) // Point - Point
    operator fun Point.times(point: Point): Point = Point(x * point.x, y * point.y) // Point * Point
    operator fun Point.div(point: Point): Point = Point(x = if(point.x == 0) 0 else (x / point.x), y = if(point.y == 0) 0 else (y / point.y)) // Point / Point
    operator fun Point.rem(point: Point): Point = Point(x = if(point.x == 0) 0 else (x % point.x), y = if(point.y == 0) 0 else (y % point.y)) // Point % Point
    operator fun Point.rangeTo(point: Point): List<Point> = Point(x, y)..Point(point.x, point.y) // Point .. Point
//    operator fun Point.contains(point: Point): Boolean = this in point // Point in Point
//    operator fun Point.contains(point: Point): Boolean = this !in point // Point !in Point
//    operator fun Point.plusAssign(point: Point): Point =  // Point !in Point

    @Test
    fun testNullAble() {
        var a: String? = "123"
        a = null
        val len = if( a != null) a.length else 0
        val list = listOf<String?>("123", null)
        for(l in list) {
            l?.let { println(it) }
        }

        //Elvis 操作符
        val len2 = a?.length ?: 0

//        val aLen = a!!.length // !!非空断言如果a为null则会抛出异常

        val aInt: Int? = a as? Int // 转换不成功会返回null
        println("aInt:$aInt")

        //可空类型的集合
        val nullableList: List<Int?> = listOf(1, null, 3, 4)
        val notNullList = nullableList.filterNotNull()
        println("notNullList:$notNullList")
    }

    //nothing可以标记成一个不会返回的函数，当你调用此函数时，编译器将不会继续向下执行
    fun fail(msg: String): Nothing {
        throw IllegalStateException(msg)
    }

    @Target(AnnotationTarget.CLASS, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.EXPRESSION)
    @Retention(AnnotationRetention.SOURCE)
    @MustBeDocumented
    annotation class Fancy

    @Fancy class FooInject {
        @Fancy fun foo( @Fancy foo: Int): Int {
            return (@Fancy 1)
        }
    }

    annotation class ReplaceWith(val msg: String)

    annotation class Deprecated(
        val msg: String,
        val replaceWith: ReplaceWith = ReplaceWith("")
    )

    annotation class Ann(val arg1: KClass<*>, val arg2: KClass<out Any>)
    // kotlin 自动将其转换为java类型
    @Ann(String::class, Int::class) class MyClass

    annotation class Suspendable
    // 此注解会应用在该表达式的invoke()方法上
    val f = @Suspendable { println("123") }

    class ReflectClass(val x: Int) {
        fun filterInt(y: Int) = y % 2 != 0
    }

    fun filterInt(x: Int) = x % 2 != 0
    fun stringLen(s: String) = s.length
    fun <A, B, C> composeFilter(f: (B) -> C, g: (A) -> B): (A) -> C {
        return {x -> f(g(x))}
    }

    var xVar = 100

    val String.lastChar: Char get() = this[length] - 1

    @Test
    fun testReflect() {
        val reflect = ReflectClass(123)
        val c = ReflectClass::class // kclass
        println(c)
        val c1 = ReflectClass::class.java // kclass<T>, java类引用
        println(c1)
        val d = c.qualifiedName //
        println(d)
        println("=====0=====")

        val numbers = listOf<Int>(1, 2, 3, 4)
        // 函数引用
        println(numbers.filter(::filterInt)) // ::filterInt是(Int) -> Boolean的一个值
        val filterIntRef: ReflectClass.(Int) -> Boolean = ReflectClass::filterInt // 类的方法引用
        val ret = filterIntRef(reflect, 2) // false 这种要先传一个类的实体，再传参才行
        println(ret)
        println("=====1=====")
        val strings = listOf<String>("a", "ab", "abc", "abcd")
        println(strings.filter(composeFilter(::filterInt, ::stringLen)))

        println("=====2=====")
        //属性引用
        println(::xVar.get()) // 不能在调用处内部定义的
        println(::xVar.name)
        ::xVar.set(-19)
        println(::xVar.get())

        println("=====3=====")
        // 访问类的成员属性
        val reflectClassProp = ReflectClass::x
        println(reflectClassProp.get(reflect))
        // 扩展函数的属性
//        println(String::lastChar.get("123")) // 这里报错
        println(ReflectClass::x.javaGetter)
        println(ReflectClass::x.javaField)

        println("====4====")
        // 构造函数引用
        fun constructorRef(factory: (Int) -> ReflectClass): ReflectClass {
            val x: ReflectClass = factory(1)
            return x
        }
        constructorRef(::ReflectClass) // 引用构造函数

        println("====5====")
        val numberRegex = "^\\d+$".toRegex()
        val isNumber = numberRegex::matches
        println("isNumber: ${isNumber("23")}")
        val numberList = listOf<String>("abc", "1234", "6", "ft56")
        println(numberList.filter(isNumber))
        val matches: (Regex, CharSequence) -> Boolean = Regex::matches
        println(matches(numberRegex, "123")) // true


        // inner类的引用通过外部类的实例来获得
        class Outer {
            inner class Inner
        }
        val o = Outer()
        val innerRef = o::Inner // 或者inner类的引用
        // page 463
    }

}