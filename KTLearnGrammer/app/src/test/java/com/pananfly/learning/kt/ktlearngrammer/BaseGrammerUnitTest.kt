package com.pananfly.learning.kt.ktlearngrammer

import org.junit.Test

class BaseGrammerUnitTest {

    fun test_fun_sum(a: Int, b: Int): Int {
        return a + b;
    }

    @Test
    fun test_fun() {
        println("3 + 5 = " + test_fun_sum(3 , 5))
    }
}