package com.pananfly.learning.kt.ktlearngrammer

import android.net.Network
import org.junit.Test
import java.util.function.BinaryOperator
import java.util.function.IntBinaryOperator
import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

//必须在文件根部声明
typealias Predicate<T> = (T) -> Boolean
//为范型类型提醒别名
typealias ModeSet = Set<Network>
//为函数提供别名
typealias MyHandler = (Int, String, Any) -> Unit
// 类型别名不会创建新的类型
typealias AInner = BaseObjectUnitTest.Inner_A.Inner



interface inlineInterface {
    fun aaa()
}
//内联类，必须含有唯一一个属性在主构造函数中初始化，运行时将使用这个唯一属性来标识内联类的实例
// 不能有init代码块，不能含有幕后字段
inline class Password(val value: String) : inlineInterface {
    val length: Int
        get() = value.length

    fun greet() {
        println("Hello, $value")
    }

    override fun aaa() {
        println("aaa()")
    }
}


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
            // VisibleInner g 不可见
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
        //在数据类中toString() equals() hashCode() copy() 等函数只对主构造函数的属性进行操作，其他将被忽略
        val d11: TestDataClass = TestDataClass(1, "45435646")
        println(d1.equals(d11)) // 将会打印true，因为这里只比较了i，对str将进行忽略，所以对忽略的属性可放在类中定义
        val d12 = d11.copy() // 单纯拷贝
        val d13 = d11.copy(12) // 拷贝时修改某一或所有主构造函数定义的属性
        println("============")
        val d2: TestDataClass2 = TestDataClass2() // 无参构造函数
        println(d2.toString())
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
        var value: T = t
    }

    fun testGeneric() {
        val bb : BoxGeneric<Int> = BoxGeneric(1) // 指定类型
        val bc = BoxGeneric(1) // 自动推导
        // page 168
    }

    fun covariantCopy(from: Array<out Any>, to: Array<Any>) {
        if(from.isEmpty() || to.isEmpty()) {
            return
        }
        to[0] = from[0]
        // from 定义了生产者out，不能对其进行赋值
        // from[1] = to[1]
    }

    fun covariantCopy2(from: Array<out  Any>, to: Array<in Any>) {
        if(from.isEmpty() || to.isEmpty()) {
            return
        }
        to[0] = from[0]
        // from 定义了生产者out，不能对其进行赋值, 只能调用get
        // from[1] = to[1]
        // from.set(0, 1)
        val t = to[0]
    }

    @Test
    fun testCovariantCopy() {
        val ints: Array<Int> = arrayOf(1, 2, 3)
        val any = Array<Any>(3) { "" }
        covariantCopy2(ints, any)
    }

    // 上界，只有Comparable<T>的子类型可以代替T
    fun <T : Comparable<T>> sortSomething(list: List<T>) {

    }
    // 多个上界
    fun <T> sortSomething2(list: List<T>)
        where T : CharSequence,
              T : Comparable<T>{

    }

    @Test
    fun testUpperExtend() {
        sortSomething(listOf<Int>(1))
        // sortSomething(listOf(HashMap<Int, String>(1)))  // 错误:HashMap<Int, String> 不是 Comparable<HashM ap<Int, String>> 的子类型
        // sortSomething2(listOf<Int>(1)) // 不满足CharSequence界定
        sortSomething2(listOf<String>("123"))

        // page 174
    }

    class OuterClass {
        private val bar: Int = 1
        inner class InnerClass {
            fun foo() = bar // inner嵌套类可以访问外部类的成员
        }
    }

    enum class EnumClass {
        NORTH,
        SOUTH,
        WEST,
        EAST
    }

    enum class EnumColorClass(val rgb: Int) {
        RED(0xFF0000),
        GREEN(0x00FF00),
        BLUE(0x0000FF)
    }

    //范型访问枚举中的常量
    inline fun <reified T : Enum<T>> printAllEnumValues() {
        println(enumValues<T>().joinToString { it.name + " : " + it.ordinal + " : " + enumValueOf<T>(it.name) })
    }

    enum class ProtocolState {
        WAITING {
            override fun signal(): ProtocolState {
                println("WAITING signal")
                return TALKING
            }
        },
        TALKING {
            override fun signal(): ProtocolState {
                println("TALKING signal")
                return WAITING
            }
        }; // 必须有分号结尾

        //这个定义必须在最后
        abstract fun signal(): ProtocolState
    }

    enum class EnumImplInterface : BinaryOperator<Int>, IntBinaryOperator {
        PLUS {
            //单独重写的
            override fun apply(t: Int, u: Int): Int {
                return t + u
            }
        },
        TIMES {
            //单独重写的
            override fun apply(t: Int, u: Int): Int {
                return t * u
            }
        };

        //共同重写的
        override fun applyAsInt(left: Int, right: Int): Int {
            return apply(left, right)
        }
    }

    @Test
    fun testEnum() {
        val colorRed: EnumColorClass = EnumColorClass.valueOf("RED")
        // val colorRed1: EnumColorClass = EnumColorClass.valueOf("RED1") // java.lang.IllegalArgumentException: No enum constant com.pananfly.learning.kt.ktlearngrammer.BaseObjectUnitTest.EnumColorClass.RED1
        println(colorRed.rgb)
        val colorArray: Array<EnumColorClass> = EnumColorClass.values()
        colorArray.forEach { println("${it.name} : ${it.rgb}") }
        println("=======0========")
        printAllEnumValues<EnumColorClass>()
        println("=======1========")
        var w: ProtocolState = ProtocolState.WAITING
        w = w.signal()
        w = w.signal()
        println("========2=======")
        val a = 13
        val b = 31
        for(f in EnumImplInterface.values()) {
            println("$f($a, $b) = ${f.apply(a, b)} = ${f.applyAsInt(a, b)}")
        }
    }

    @Test
    fun simpleObject() {
        val adHoc = object {
            var x: Int = 0
            var y: Int = 10
        }
        println(adHoc.x + adHoc.y)
    }

    class AnonymousObject {
        // 私有函数，返回类型是匿名对象类型
        private fun foo() = object {
            val x: Int = 1
        }

        // 共有函数，返回类型是Any
        fun publicFoo() = object {
            val x: Int = 1
        }

        fun bar() {
            val x1 = foo().x
            // val x2 = publicFoo().x // error, 不能访问到x
        }
    }

    interface MouseEvent {
        fun mouseClick()
        fun mouseEnter()
    }

    class WindComponent {
        private lateinit var e: MouseEvent
        fun addMouseEventListener(ev: MouseEvent) {
            e = ev
        }

        fun click() {
            if(!this::e.isInitialized) {
                println("MouseEvent not initialize")
                return
            }
            e?.mouseClick()
        }

        fun enter() {
            if(!this::e.isInitialized) {
                println("MouseEvent not initialize")
                return
            }
            e?.mouseEnter()
        }
    }

    fun countClicks(window: WindComponent) {
        var clickCount = 0
        var enterCount = 0
        window.addMouseEventListener(object : MouseEvent {
            override fun mouseClick() {
                clickCount ++ // 对象表达式可以访问来自包含它的作用域的对象
                println("click count: $clickCount")
            }

            override fun mouseEnter() {
                enterCount ++
                println("enter count: $enterCount")
            }
        })
    }

    @Test
    fun testCountClicks() {
        val w: WindComponent = WindComponent()
        countClicks(w)
        w.click()
        w.enter()
        w.enter()
        w.enter()
        w.enter()
    }

    class CompanionClass1 {
        companion object Factory {
            fun create(): CompanionClass1 = CompanionClass1()
        }
    }

    fun testCompanionClass1() {
        val instance = CompanionClass1.create()
    }

    class CompanionClass2 {
        companion object{

        }
    }

    fun testCompanionClass2() {
        val instance = CompanionClass2
    }

    interface CompanionInt<T> {
        fun create(): T
    }

    class CompanionInterface {
        companion object : CompanionInt<CompanionInterface> {
            override fun create(): CompanionInterface {
                return CompanionInterface()
            }
        }
    }

    fun testCompanionClass3() {
        val instance:CompanionInt<CompanionInterface>  = CompanionInterface
    }

    class Inner_A {
        inner class Inner
    }

    fun fooPredicate(p: Predicate<Int>) = p(42)

    fun aliasFun(a: Int, b: String, c: Any): UInt {
        return a.toUInt()
    }
    fun aliasFun2(m: MyHandler) = m(1, "2", 1)

    @Test
    fun testTypeAlias() {
        val f: (Int) -> Boolean = {it > 0}
        println(fooPredicate(f)) // true
        val p: Predicate<Int> = {it > 0}
        println(listOf(1, -2).filter(p)) // [1]

//        val m: (Int, String, Any) -> Unit = {1; "3"; 1; it}
//        println(aliasFun2(m))
    }

    @Test
    fun testInlineClass() {
        //不会生成Password实例对象， 对象仅包含String
        val p = Password("DDDDDD")
        val b = p is Password
        println(b) // true
        println(p.value)
    }



    interface BaseBy {
        val message: String
        fun print()
        fun printMsg()
    }

    class BaseByImpl(val x: Int) : BaseBy {

        override val message: String
            get() = "BaseByImpl mmm: $x"

        override fun print() {
            println("BaseByImpl print $x")
        }

        override fun printMsg() {
            println("BaseByImpl print msg")
        }
    }

    //将所有公有成员都委托给指定对象来实现接口BaseBy
    class DerivedBy(b: BaseBy) : BaseBy by b {
        override val message: String
            get() = "DerivedBy mmm"
        override fun printMsg() {
            println("DerivedBy print msg")
        }
    }

    class Delegate {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
            return "$thisRef, thank you for delegating '${property.name}' to me!"
        }
        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            println("$value has been assigned to '${property.name}' in $thisRef.")
        }
    }

    class ExampleBy {
        var p: String by Delegate()
        val lazyVal: String by lazy {
            println("Init property lazyVal") // 只会在调用第一次才会执行，后面则只取值
            p
        }
        var name: String by Delegates.vetoable("1"){ // 观察属性，可以解析并处理是否允许变化
            _, oldValue, newValue -> // 不会使用到的属性，可改为下划线
            println("$oldValue -> $newValue")
            newValue != "first name" // 最后要返回一个boolean
        }
        var name2: String by Delegates.observable("none") { // 观察属性，有变化则会收到监听
            property, oldValue, newValue ->
            println("${property.name} : $oldValue -> $newValue")
        }
    }

    //把属性委托储存在映射中
    class MutableUser(val map: MutableMap<String, Any?>) {
        var name: String by map
        var age: Int by map
    }

    class FooLazy {

//        companion object Factory {
//            fun create():FooLazy  {
//                println("Create")
//                return FooLazy()
//            }
//        }

        companion object {
            fun create():FooLazy  {
                println("Create")
                return FooLazy()
            }
        }

        fun isValid(): Boolean {
            return true
        }
        fun doSomething() {
            println("doSomething()")
        }
    }

    fun getFooLazy(): FooLazy {
        return FooLazy.create()
    }

    fun funcOfLazy(cond: Boolean, foo:() -> FooLazy) {
        val mem by lazy(foo)
        if(cond && mem.isValid()) { // mem 只会在第一次访问时计算，如果cond为false, mem根本不会参与计算
            mem.doSomething()
        }
    }


    class ResourceDelegate<T> : ReadOnlyProperty<MyUI, T> {
        override fun getValue(thisRef: MyUI, property: KProperty<*>): T {
            TODO("Not yet implemented")
        }
    }

    class ResourceID<T> {
//        var image_id = 1
        var text_id = 1
    }

    class ResourceLoader<T>(id: ResourceID<T>) {
        operator fun provideDelegate(thisRef: MyUI, property: KProperty<*>) : ReadOnlyProperty<MyUI, T> {
            checkProperty(thisRef, property.name)
            return ResourceDelegate()
        }

        private fun checkProperty(thisRef: MyUI, name: String) {

        }
    }

    class MyUI {
        fun <T> bindResource(id: ResourceID<T>) : ResourceLoader<T> {
            return ResourceLoader<T>(id)
        }

//        val image by bindResource(ResourceID.image_id)
//        val text by bindResource(ResourceID.text_id)
    }

    @Test
    fun testBy() {
        val b = BaseByImpl(10)
        val d = DerivedBy(b)
        d.print()
        // 有覆盖则调用自身覆盖的方法
        d.printMsg()
        // 打印自己覆盖的属性内容
        println(d.message)

        println("======0======")

        val e = ExampleBy()
        println(e.p)
        e.p = "fdsafjla"
        println("======1======")
        println(e.lazyVal)
        println(e.lazyVal)
        println("======2======")
        e.name = "first name"
        e.name = "last name"
        e.name2 = "last name"
        println("======3======")

        val user = MutableUser(
            mutableMapOf("name" to "pananfly", "age" to 18)
        )
        println(user.name)
        println(user.age)

        println("======4======")
        funcOfLazy(false, this::getFooLazy)
        funcOfLazy(true, this::getFooLazy)
        // page 200
    }

}