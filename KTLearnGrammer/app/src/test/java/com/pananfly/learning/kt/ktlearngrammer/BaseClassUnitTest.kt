package com.pananfly.learning.kt.ktlearngrammer

import android.os.SystemClock
import org.junit.Test
//import javax.inject.Inject

class BaseClassUnitTest {

    class InitOrderDemo(name: String) {
        val firstProperty = "First property: $name".also(::println)

        init {
            println("First initializer block that prints $name")
        }

        val secondProperty = "Second property: ${name.length}".also(::println)

        init {
            println("Second initialize block that prints ${name.length}")
        }

        val thirdProperty = "Third property: ${name.toUpperCase()}".also(::println)
    }

    class ValVarProperty(val firstName: String , val lastName: String, var age: Int){

    }

//    class  InjectClass @Inject constructor(name: String) {
//
//    }

    class UnVisibleClass private constructor() {

    }

    @Test
    fun testInitOrder() {
        val v = InitOrderDemo("lfjlasdf")
        //val unvisible = UnVisibleClass(); // failed
        // page 138
    }

    class SecondConstructorClass {
        val childs: MutableList<SecondConstructorClass> = mutableListOf<SecondConstructorClass>();
        constructor(scc: SecondConstructorClass) {
            scc.childs.add(this)
        }
    }

    class SecondConstructorClass2(name: String = "") {

        val childs: MutableList<SecondConstructorClass2> = mutableListOf<SecondConstructorClass2>();

        init {
            println("init block")
        }

        //有主构造函数需要委托主构造函数
        constructor(name: String , scc: SecondConstructorClass2) : this(name) {
            println("Second constructor block")
            scc.childs.add(this)
        }

        //有主构造函数需要委托主构造函数或者通过其他次构造函数间接委托
        constructor(age: Int , scc: SecondConstructorClass2) : this(scc) {
            println("Second constructor 2 block")
            scc.childs.add(this)
        }

        //有主构造函数需要委托主构造函数或者通过其他次构造函数间接委托
        constructor(scc: SecondConstructorClass2) : this() {
            println("Second constructor 3 block")
            scc.childs.add(this)
        }
    }

    @Test
    fun testSecondConstructor() {
        val ssc =  SecondConstructorClass2()
        val ssc2 =  SecondConstructorClass2("123" , ssc) // init block -> second constructor block
        val ssc3 =  SecondConstructorClass2(0 , ssc) // init block -> second constructor 3 block -> second constructor 2 block
    }

    class DefaultSimpleClass // 从Any隐式继承

    open class InheritBaseClass(p: Int) //使用open定义为父类，可被其他类继承

    class DerivedBaseClass(p: Int) : InheritBaseClass(p) // 继承

    class DerivedBaseClass2 : InheritBaseClass { // 派生类没有构造函数的话，在次构造函数主动调super初始化基类
        constructor(p: Int) : super(p) {

        }
    }

    @Test
    fun testInheritableClass() {
        // page 141
        var c: DefaultSimpleClass
        // Any 类型都定义了这些方法
//        c.equals()
//        c.hashCode()
//        c.toString()
    }

    open class ClassOverrideFun {
        open fun draw() {}
        open fun up() {}
        fun fill() {}
    }

    open class ClassOverrideFunExtend1 : ClassOverrideFun() {
        override fun draw() {
            super.draw()
        }

        final override fun up() {
            super.up()
        }
    }

    open class ClassOverrideFunExtend2 : ClassOverrideFunExtend1() {
        override fun draw() {
            super.draw()
        }
        // final 方法不允许再重写
//        final override fun up() {
//            super.up()
//        }
    }

    @Test
    fun testClassOverrideFun() {

    }


    open class ClassOverrideProperties {
        open val count: Int = 0
        open val count0: Int = 0
        open var count1: Int = 0
        open var count2: Int = 0
    }

    open class ClassOverridePropertiesExtend1 : ClassOverrideProperties() {
        //val可以覆盖val
        override val count: Int = 4
        //var可以覆盖val-本质上是增加一个set方法
        override var count0: Int = 4
        //var可以覆盖var
        override var count1: Int = 4
        //不允许val覆盖var
//        override val count2: Int = 4
    }

    interface ClassOverrideProperties2 {
        val count: Int
        fun printCount()
    }

    //主构造函数覆盖接口的参数
    class ClassOverrideProperties2Extend1(override val count: Int) : ClassOverrideProperties2 {
        override fun printCount() {
            println(count)
        }
    }

    //类中必须手动覆盖，不然报错
    class ClassOverrideProperties2Extend2 : ClassOverrideProperties2 {
        override val count: Int = 40
        override fun printCount() {
            println(count)
        }
    }

    @Test
    fun testClassOverrideProperties() {
        var p1: ClassOverrideProperties2Extend1 = ClassOverrideProperties2Extend1(3)
        p1.printCount()
        var p2: ClassOverrideProperties2Extend2 = ClassOverrideProperties2Extend2()
        p2.printCount()
    }

    open class DeriveInitOrderBase(val name: String) {
        init {
            println("Base init scope.")
        }

        open val size: Int = name?.length.also {
            kotlin.io.println("Base size init: $it")
        }
    }

    class DeriveInitOrderImpl (
        name: String,
        val lastName: String
    ) : DeriveInitOrderBase(name.capitalize().also { println("Argument for Base: $it") }) {
        init {
            println("Impl init scope.")
        }

        override val size: Int
            get() = (super.size + lastName.length).also { println("Impl size: $it") }
    }

    @Test
    fun testDeriveInitOrder() {
        //基类/派生类在初始化时，基类/派生类的open成员
        // 还没有初始化，避免在构造函数、属性初始化器及init中使用open成员
        val impl: DeriveInitOrderImpl = DeriveInitOrderImpl("a123" , "311232")
    }

    open class ClassSuperBase {
        open fun draw() {
            println("Base draw.")
        }

        open fun swipe() {
            println("Base swipe.")
        }

        val color: String get() = "black"
    }

    interface ClassSuperInterface {
        fun swipe() {
            println("Interface swipe.")
        }
    }

    class ClassSuperImpl1 : ClassSuperBase() , ClassSuperInterface {
        override fun draw() {
            super.draw()
            println("Impl1 draw.")
        }

        override fun swipe() {
            //有同样实现的，调用超类的实现需要指定限定的super
            super<ClassSuperBase>.swipe()
            super<ClassSuperInterface>.swipe()
        }

        val fillColor: String get() = super.color

        inner class Filler {
            fun fill() {
                println("Filler fill.")
            }

            fun drawAndFill() {
                //调用外部类的方法和成员变量
                super@ClassSuperImpl1.draw()
                fill()
                println("Filler draw and fill with color ${super@ClassSuperImpl1.color}")
            }
        }
    }

    @Test
    fun testClassSuper() {
        val aa: ClassSuperImpl1 = ClassSuperImpl1()
        println("=====1111")
        aa.draw()
        println("=====2222")
        aa.swipe()
        println("=====3333")
        val vv: ClassSuperImpl1.Filler = ClassSuperImpl1().Filler()
        vv.drawAndFill()
    }

    open class AbstractClassBase {
        open fun draw() {}
        open fun swipe() {}
    }

    abstract class AbstractClassImpl : AbstractClassBase() {
        override fun draw() {
            super.draw()
        }

        abstract override fun swipe()
    }

    class CompanionClass {
        companion object Factory {
            fun create() : CompanionClass = CompanionClass()
        }
    }

    @Test
    fun testAbstractClass() {
        val c: CompanionClass = CompanionClass.create()
        // page 145
    }
}