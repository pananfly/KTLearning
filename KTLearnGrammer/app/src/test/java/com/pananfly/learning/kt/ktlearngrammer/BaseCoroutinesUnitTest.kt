package com.pananfly.learning.kt.ktlearngrammer

import kotlinx.coroutines.*
import org.junit.Test

// https://www.kotlincn.net/docs/reference/coroutines/basics.html
class BaseCoroutinesUnitTest {

    @Test
    fun testHelloWord() {
        GlobalScope.launch {
            delay(4000L) // 特殊的挂起函数，不会造成线程阻塞，但是会挂起协程，并且只能在协程中使用
            println(" Word")
        }
        print("Hello,")
        Thread.sleep(5000L) // 阻塞保证jvm存活
    }

    @Test
    fun testHelloWord2() {
        GlobalScope.launch {
            delay(4000L)
            println(" Word")
        }
        print("Hello,")
        runBlocking {
            delay(5000L)
        }
    }

    @Test
    fun testHelloWord3() = runBlocking<Unit> {
        GlobalScope.launch {
            delay(4000L)
            println(" Word")
        }
        print("Hello,")
        delay(5000L)
    }

    class e1 {
        suspend fun testJob1() {
            val job = GlobalScope.launch {
                delay(4000L)
                println(" Word")
            }
            print("Hello,")
            job.join()
        }
    }

    @Test
    fun testHelloWord4()  {
        val e = e1()
        // e.testJob1() // Suspend function 'testJob1' should be called only from a coroutine or another suspend function
    }

    @Test
    fun testHelloWord5() = runBlocking<Unit> {
        launch {
            delay(4000L)
            println(" Word")
        }
        print("Hello,")
        delay(5000L)
    }

    @Test
    fun testHelloWord6() = runBlocking<Unit> {
        launch {
            delay(200L)
            println("Task for runBlocking.")
        }

        coroutineScope {  // 协程作用域
            launch {
                delay(500L)
                println("Task for nested launch.")
            }
            delay(100L)
            println("Task for coroutineScope.")
        }

        println("Over.")
        """
            Task for coroutineScope.
            Task for runBlocking.
            Task for nested launch.
            Over.
        """.trimIndent()
    }

    suspend fun doWorld7() { // 挂起函数
        delay(4000L)
        println("World!")
    }

    @Test
    fun testHelloWord7() = runBlocking<Unit> {
        launch {
            doWorld7()
        }
        print("Hello,")
    }

    @Test
    fun testRepeat() = runBlocking {
        repeat(1000){// 启动1000个协程，延迟一秒后都输出.
            launch {
                delay(1000L)
                print(".")
            }
        }
    }

    @Test
    fun testRepeat2() = runBlocking<Unit> {
        GlobalScope.launch {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }
        delay(1300L) // 在延迟后退出
        // 输出三次后退出
        """
            I'm sleeping 0 ...
            I'm sleeping 1 ...
            I'm sleeping 2 ...
        """.trimIndent()
    }

    @Test
    fun testCancel1() = runBlocking<Unit> {
        val job = launch {
            repeat(1000) { i ->
                println("jpb: I'm sleeping $i ...")
                delay(500L)
            }
        }
        delay(1300L)
        println("main: I'm tired of waiting...")
        job.cancel() // 取消作业
        job.join() // 等待作业执行结束
        println("Over.")
    }

    @Test
    fun testCancel2() = runBlocking<Unit> {
        val job = launch {
            repeat(1000) { i ->
                println("jpb: I'm sleeping $i ...")
                delay(500L)
            }
        }
        delay(1300L)
        println("main: I'm tired of waiting...")
        job.cancelAndJoin() // 取消作业并等待作业执行结束
        println("Over.")
    }

    @Test
    fun testCancel3() = runBlocking<Unit> {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            while (i < 5) { // 一个执行计算的循环，只是为了占用 CPU
                // 每秒打印消息两次
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("job: I'm sleeping ${i++} ...")
                    nextPrintTime += 500L
                }
            }
        }
        delay(1300L) // 等待一段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消一个作业并且等待它结束
        println("main: Now I can quit.")

        // 没有检查协程中可取消的操作，在取消后依然会执行，就如以下打印
        """
            job: I'm sleeping 0 ...
            job: I'm sleeping 1 ...
            job: I'm sleeping 2 ...
            main: I'm tired of waiting!
            job: I'm sleeping 3 ...
            job: I'm sleeping 4 ...
            main: Now I can quit.
        """.trimIndent()
    }

    @Test
    fun testCancel4() = runBlocking<Unit> {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            while (isActive) { // 可以被取消的计算循环
                // 每秒打印消息两次
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("job: I'm sleeping ${i++} ...")
                    nextPrintTime += 500L
                }
            }
        }
        delay(1300L) // 等待一段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消该作业并等待它结束
        println("main: Now I can quit.")

        // 使用协程的isActive扩展属性后，在取消后就不会再执行操作
        """
            job: I'm sleeping 0 ...
            job: I'm sleeping 1 ...
            job: I'm sleeping 2 ...
            main: I'm tired of waiting!
            main: Now I can quit.
        """.trimIndent()
    }

    @Test
    fun testCancel5() = runBlocking<Unit> {
        val job = launch {
            try {
                repeat(1000) { i ->
                    println("job: I'm sleeping $i ...")
                    delay(500L)
                }
            } finally {
                println("job: I'm running finally")
            }
        }
        delay(1300L) // 延迟一段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消该作业并且等待它结束
        println("main: Now I can quit.")

        // finally 在结束时会执行
        """
            job: I'm sleeping 0 ...
            job: I'm sleeping 1 ...
            job: I'm sleeping 2 ...
            main: I'm tired of waiting!
            job: I'm running finally
            main: Now I can quit.
        """.trimIndent()
    }
}