package com.pananfly.learning.kt.ktlearngrammer
import org.junit.Test
//import javax.inject.Inject
class BasePropertyUnitTest {

    class TestProperty1 {
        val a: Int = 0
        var b: Int = 0
        val c: Int get() = 1 // val没有set函数
        var d: Int
            get() = 3
            set(value) {value + 1}
        var e: Int = 1
            private set // e的set函数是私有的
//        var f: Int = 2
//            @Inject set // 用inject 注解f的set函数
        var g: Int = 4 //使用幕后属性
            set(value) {
                if (value > 0) field = value
            }
        //const val VAVA: String = "4123432423" // const 只允许在顶层或者object声明或者伴生类的一个成员，以String或者原生类型初始化，没有自定义getter

    }

    class TestLateInitProperty {
//        lateinit var aa: Int // 不能是原生类型
        lateinit var bb: TestProperty1
        lateinit var cc: String
        val flag: Boolean = true

    }

    @Test
    fun testProperty1() {
        var l: TestLateInitProperty = TestLateInitProperty()
//        val b: Boolean = l::bb.isInitialized //为啥这里的isInitialized访问不了呢
        // page 150
    }
}