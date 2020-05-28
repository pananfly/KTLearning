package com.pananfly.learning.kt.ktlearngrammer

class BaseInterfaceUnitTest {
    interface TestInterfaceProperty { // 属性不能有幕后字段
        val prop: Int // 抽象的
        val propImp: String get() = "foo"
        fun foo() {
            println(propImp)
        }
    }

    interface TestItfExtend {
        val name: String
    }

    interface TestItfExtend2 : TestItfExtend {
        val firstName: String
        val lastName: String
        override val name: String
            get() =  "$firstName $lastName"
    }

    data class TestItfExtendClass (override val firstName: String,
                                   override val lastName: String,
                                   val pos: Int
    ) : TestItfExtend2
}