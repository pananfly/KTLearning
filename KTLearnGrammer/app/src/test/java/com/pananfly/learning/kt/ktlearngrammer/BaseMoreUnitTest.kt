package com.pananfly.learning.kt.ktlearngrammer

import org.junit.Test

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
}