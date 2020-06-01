package com.pananfly.learning.kt.ktlearngrammer

import org.junit.Test

class BaseObjectUnitTest {
    open class VisibleTest {
        private val a = 1
        protected open val b = 2
        internal val c = 3
        val d = 4 // 默认public
        protected class VisibleInner {
            public val e = 5
            private val f = 6
            protected val g = 7
        }
    }
    class VisibleSub : VisibleTest() {
        override val b: Int
            get() = super.b
        fun lll() {
            // a 不可见
            // println(a)
            // b c d 可见
            println(b)
            println(c)
            println(d)
            // VisibleInner 可见
            val lll: VisibleInner = VisibleInner()
            // VisibleInner e 可见
            println(lll.e)
            // VisibleInner f 不可见
            // println(lll.f)
            // VisibleInner e 不可见
            // println(lll.g)
        }
    }

    class VisibleSub2(v: VisibleTest) {
        fun kkk(v: VisibleTest) {
            //a b VisibleInner及e f g都不可见
            //c 和 d可见
            println(v.c)
            println(v.d)

        }
    }

    // internal 在一个intellij模块内，在一个maven项目内，一个gradle源，一次<kotlinc> Ant编译所执行的一套源内可见

    //扩展
    fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
        val temp = this[index1];
        this[index1] = this[index2]
        this[index2] = temp
    }

    @Test
    fun testExpend() {
        val list = mutableListOf<Int>(0, 1, 2)
        list.swap(0, 2)
        list.forEach { println(it) }
    }

    open class ShapeExtend

    class RectangleExtend: ShapeExtend()

    fun ShapeExtend.getName() = "shape"

    fun RectangleExtend.getName() = "rectangle"

    fun printClassName(s: ShapeExtend) {
        println(s.getName())
    }

    class CircleExtend {
        fun printName() {
            println("Circle")
        }

    }

    fun CircleExtend.printName() {
        println("Circle extend name")
    }


    fun CircleExtend.printName(i: Int) {
        println("Circle extend name $i")
    }

    @Test
    fun testClassExtendFun() {
        //打印shape, 说明扩展是静态分发的，不是根据接收者的类型调用，只针对函数的声明类型去调用
        printClassName(RectangleExtend())
        //打印Circle，说明扩展了跟成员函数一样的函数名并函数参数相同的，优先调用成员函数
        CircleExtend().printName()
        //打印Circle extend name 2
        CircleExtend().printName(2)
    }

    //扩展属性
    val <T> List<T>.lastIndex: Int
        get() = size - 1
    //不能有幕后字段所以不能显式赋值
    //val <T> List<T>.lastSecondIndex: Int = 1

    class CompanionExtend {
        companion object {}
    }

    fun CompanionExtend.Companion.print() {
        println("companion extend")
    }

    class ExtendVisible {
        class aaa {

        }

        fun aaa.ttt() {

        }
    }


    fun ExtendVisible.aaa.sss() {

    }




    fun testExtendVisible() {
        val v: ExtendVisible.aaa = ExtendVisible.aaa()
        // ttt() 不可见
        //v.ttt()
        v.sss()

    }

    class Host(val hostname: String ) {
        fun printHostname() {
            print(hostname)
        }
    }

    class Connection(val host: Host, val port: Int) {
        fun printPort() {
            print(port)
        }

        fun Host.printConnectInformation() {
            printHostname() // Host的函数
            print(":")
            printPort() // Connection 的函数
        }

        fun Host.printConnectInformation2() {
            print(toString()) // Host的函数
            print(":")
            //限定分发接收者
            print(this@Connection.toString()) // Connection 的函数
        }

        fun connect() {
            host.printConnectInformation()
            println()
            host.printConnectInformation2()
        }
    }

    @Test
    fun testExtendVisible2() {
        Connection(Host("pananfly.com"), 1024).connect()
        // printConnectInformation() 函数不可见
        // Host("1111").printConnectInformation()
        // page 162
    }

    open class ExtendOpen

    class ExtendOpenDerived: ExtendOpen()

    open class ExtendOpenCaller {
        open fun ExtendOpen.printInfo() {
            println("ExtendOpen info")
        }

        open fun ExtendOpenDerived.printInfo() {
            println("ExtendOpenDerived info")
        }

        fun call(o: ExtendOpen) {
            o.printInfo()
        }
    }

    class ExtendOpenDerivedCaller : ExtendOpenCaller() {
        override fun ExtendOpen.printInfo() {
            println("ExtendOpen override info")
        }

        override fun ExtendOpenDerived.printInfo() {
            println("ExtendOpenDerived override info")
        }
    }

    @Test
    fun testExtendVisible3() {
        ExtendOpenCaller().call(ExtendOpen()) // ExtendOpen info
        ExtendOpenDerivedCaller().call(ExtendOpen()) // ExtendOpen override info-分发接收者虚拟解析
        ExtendOpenDerivedCaller().call(ExtendOpenDerived()) // ExtendOpen override info-扩展接收者静态解析

    }

    data class TestDataClass(var i: Int) {
        var str: String = ""
        constructor(i: Int, j: String) : this(i) {
            str = j
        }
    }

    data class TestDataClass2(var i: Int = 0, var str: String = "") {
    }

    @Test
    fun testDataClass1() {
        val d1: TestDataClass = TestDataClass(1, "123456")
        println(d1.toString()) // toString只会打印主函数中定义的i
        val d2: TestDataClass2 = TestDataClass2() // 无参构造函数
        println(d2.toString()) // toString只会打印主函数中定义的i
        //在数据类中toString() equals() hashCode() copy() 等函数只对主构造函数的属性进行操作，其他将被忽略
        val d11: TestDataClass = TestDataClass(1, "45435646")
        println(d1.equals(d11)) // 将会打印true，因为这里只比较了i，对str将进行忽略，所以对忽略的属性可放在类中定义
        val d12 = d11.copy() // 单纯拷贝
        val d13 = d11.copy(12) // 拷贝时修改某一或所有主构造函数定义的属性
        println("============")
        val d21: TestDataClass2 = TestDataClass2(123, "123456")
        val (age, name) = d21 // 数据类的解构
        println("age: $age, name: $name")
    }

    sealed class ExprSealed // 构造函数只能是private
    // un support, why the document writes this
//    data class ConstSealed(val number: Int) : ExprSealed() {
//
//    }
//    object ObjSealed : ExprSealed()

    class BoxGeneric<T>(t: T) {
        var value = t
    }

    fun testGeneric() {
        val bb : BoxGeneric<Int> = BoxGeneric(1) // 指定类型
        val bc = BoxGeneric(1) // 自动推导
        // page 168
    }

}