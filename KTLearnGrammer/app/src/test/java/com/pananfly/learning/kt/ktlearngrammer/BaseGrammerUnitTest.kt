package com.pananfly.learning.kt.ktlearngrammer

import org.junit.Test

class BaseGrammerUnitTest {

    fun test_fun_sum(a: Int, b: Int): Int {
        return a + b
    }

    fun test_fun_sum2(a: Int, b: Int) = a + b;

    @Test
    fun test_fun() {
        //page 72
        println("3 + 5 = " + test_fun_sum(3 , 5))
        println("4 + 5 = ${test_fun_sum2(4 , 5)}")
    }
}