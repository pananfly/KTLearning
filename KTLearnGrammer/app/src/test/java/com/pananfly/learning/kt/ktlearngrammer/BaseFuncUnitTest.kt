package com.pananfly.learning.kt.ktlearngrammer

import org.junit.Test
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.math.abs
import kotlin.math.cos

class BaseFuncUnitTest {
    fun defaultParamFunc(b: Int = 0, len: Int = 111) {

    }

    open class DeFuncA {
        open fun foo(i: Int = 10) {

        }
    }

    class DeFuncBDerived : DeFuncA() {
        override fun foo(i: Int) { // 重写有默认值参数的函数，重写的不能有默认值

        }

        fun bar(bar: Int = 0, baz: Int) {

        }

        fun xxx(bar: Int = 0, baz: Int = 1, qux: () -> Unit) {

        }

        fun varFunc(vararg strings: String) {

        }

        // 中缀函数必须是成员函数或者扩展函数
        // 必须只有一个参数
        // 参数不能是可变数量参数且不能有默认值
        // 调用的优先级低于算术操作复、类型转换以及rangeTo 操作符 1 shl 2 + 3 ==> 1 shl (2 + 3)
        // 调用的优先级高于布尔操作符 && 与 || ` is- in- 以及其他一些操作符 a && b xor c ==> a && (b xor c)
        // 总是要求有接收者和参数
        private infix fun add(s: String) {

        }

        fun testAdd() {
            this add "abc"
            add("abcd")
            //add "abcde" // 错误：必须指定接收者
        }

        val eps = 1E-10
        // tailrec 标记尾递归，编译器会优化改递归，留下一个快递而搞笑的基于循环的版本， 函数必须将其自身作为它执行的最后一个操作，后面不能有其他操作，并且不能用在try/catch/finally中
        tailrec fun findFixPoint(x: Double = 1.0): Double
            = if (abs(x - cos(x)) < eps) x else findFixPoint(cos(x))
    }

    fun testDeFunc() {
        val b: DeFuncBDerived = DeFuncBDerived()
        b.foo() // 可以不传参
        b.bar(baz = 1) // 使用具名参数调用函数，可以省略部分有默认值的参数
        b.xxx { println("fdsaf") } // 两个默认值，第三个参数在括号外传递
        b.xxx(1) { println("====")} // baz使用默认值，第三个参数在括号外传递
        b.xxx(qux = { println("fsdjaflja")}) // 两个默认值，第三个参数在括号内传递

        b.varFunc(strings = *arrayOf("1", "2")) //使用星号
        b.varFunc("1", "2") // 或者一个个传参

    }

    @Test
    fun testFuncLambda() {
        val repeatFun: String.(Int) -> String = {it -> this.repeat(it)}
        val twoParams: (String, Int) -> String = repeatFun
        fun runTrans(f: (String, Int) -> String): String {
            return f("Hello", 3)
        }
        val result = runTrans(repeatFun)
        println("result = $result")
        // page 214

        println("=====0=====")
        //invoke
        val stringPlus: (String, String) -> String = String::plus
        val stringPlus2: String.(String) -> String = String::plus
        val intPlus: Int.(Int)-> Int = Int::plus
        println(stringPlus.invoke("<-", "->"))
        println(stringPlus("<=", "=>")) // 二者等同
        println("panan".stringPlus2("fly")) // stringPlus2的定义方式三种用法都可以
        println(intPlus.invoke(1, 2))
        println(intPlus(1, 3))
        println(2.intPlus(4))
    }

    class HTML1 {
        fun body() {
            println("Body.")
        }
    }

    fun html(init: HTML1.() -> Unit): HTML1 {
        val html = HTML1()
        html.init() // 调用lambda
        return html
    }

    @Test
    fun testHtml() {
        val h: HTML1 = html { body() }
    }

    // 内联函数使用lambda
    inline fun <T> lock(lock: Lock, body: () -> T): T {
        lock.lock()
        val t: T =  body()
        lock.unlock()
        return t
    }

    fun lockFunc(): Int {
        return 1
    }

    // 禁用内联lambda
    inline fun noInlineLambda(inlined: () -> Unit, noinline notInlined: () -> Unit) {

    }

    // reified 修饰符来限定类型参数,现在可以在函数内部访问它了, 几乎就像是一个 普通的类一样。由于函数是内联的,不需要反射,正常的操作符如 !is 和 as 现在都能用 了。
//    inline fun <reified T> TreeNode.findParentOfType(): T? {
//        var p = parent
//        while (p != null && p !is T) {
//            p = p.parent
//        }
//        return p as T?
//    }

    inline fun <reified  T> membersOf() = T::class.members

    @Test
    fun testLock() {
        val l: Lock = ReentrantLock()
        val value = lock(l) {lockFunc()}
        println(value)
        println("====0=====")
        println(membersOf<StringBuilder>().joinToString("\n"))

        // page 223
    }

}