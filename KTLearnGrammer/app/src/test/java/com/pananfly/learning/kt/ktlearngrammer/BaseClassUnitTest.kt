package com.pananfly.learning.kt.ktlearngrammer

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
    }
}