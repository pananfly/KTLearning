package com.pananfly.learning.kt.ktlearngrammer

import kotlinx.coroutines.*
import org.junit.Test
import kotlin.system.measureTimeMillis

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

    @Test
    fun testCancel6() = runBlocking<Unit> {
        val job = launch {
            try {
                repeat(1000) { i ->
                    println("job: I'm sleeping $i ...")
                    delay(500L)
                }
            } finally {
                withContext(NonCancellable) { // 挂起被取消的协程
                    println("job: I'm running finally")
                    delay(1000L)
                    println("job: And I've just delayed for 1 sec because I'm non-cancellable")
                }
            }
        }
        delay(1300L) // 延迟一段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消该作业并且等待它结束
        println("main: Now I can quit.")

        """
            job: I'm sleeping 0 ...
            job: I'm sleeping 1 ...
            job: I'm sleeping 2 ...
            main: I'm tired of waiting!
            job: I'm running finally
            job: And I've just delayed for 1 sec because I'm non-cancellable
            main: Now I can quit.
        """.trimIndent()
    }

    @Test
    fun testCancel7() = runBlocking<Unit> {
        withTimeout(1300L) {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }

        // withTimeout函数会抛出TimeoutCancellationException
        """
            I'm sleeping 0 ...
            I'm sleeping 1 ...
            I'm sleeping 2 ...

            kotlinx.coroutines.TimeoutCancellationException: Timed out waiting for 1300 ms
        """.trimIndent()
    }

    @Test
    fun testCancel8() = runBlocking<Unit> {
        val result = withTimeoutOrNull(1300L) {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
            "Done" // 在它运行得到结果之前取消它
        }
        println("Result is $result")

        // withTimeoutOrNull函数通过返回null来进行取消超时操作，从而替代抛出一个异常
        """
            I'm sleeping 0 ...
            I'm sleeping 1 ...
            I'm sleeping 2 ...
            Result is null
        """.trimIndent()
    }


    suspend fun doSomethingUsefulOne(): Int {
        delay(1000L) // 假设我们在这里做了一些有用的事
        return 13
    }

    suspend fun doSomethingUsefulTwo(): Int {
        delay(1000L) // 假设我们在这里也做了一些有用的事
        return 29
    }

    @Test
    fun testSuspend() = runBlocking<Unit>{
        val time = measureTimeMillis {
            val one = doSomethingUsefulOne()
            val two = doSomethingUsefulTwo()
            println("The answer is ${one + two}")
        }
        println("Completed in $time ms")

        """
            The answer is 42
            Completed in 2017 ms
        """.trimIndent()
    }

    @Test
    fun testSuspend2() = runBlocking<Unit>{
        """
            async 就类似于 launch。它启动了一个单独的协程，这是一个轻量级的线程并与其它所有的协程一起并发的工作。
            不同之处在于 launch 返回一个 Job 并且不附带任何结果值，而 async 返回一个 Deferred —— 一个轻量级的非阻塞 future， 
            这代表了一个将会在稍后提供结果的 promise。你可以使用 .await() 在一个延期的值上得到它的最终结果， 
            但是 Deferred 也是一个 Job，所以如果需要的话，你可以取消它。
        """.trimIndent()
        val time = measureTimeMillis {
            val one = async { doSomethingUsefulOne() }
            val two = async { doSomethingUsefulTwo() }
            println("The answer is ${one.await() + two.await()}")
        }
        println("Completed in $time ms")

        """
            The answer is 42
            Completed in 1022 ms
        """.trimIndent()
    }

    @Test
    fun testSuspend3() = runBlocking<Unit>{
        """
            async 可以通过将 start 参数设置为 CoroutineStart.LAZY 而变为惰性的。 
            在这个模式下，只有结果通过 await 获取的时候协程才会启动，或者在 Job 的 start 函数调用的时候。
        """.trimIndent()
        val time = measureTimeMillis {
            val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
            val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
            // 执行一些计算
            one.start() // 启动第一个
            two.start() // 启动第二个
            println("The answer is ${one.await() + two.await()}")
        }
        println("Completed in $time ms")

        """
            The answer is 42
            Completed in 1036 ms
        """.trimIndent()
    }

    @Test
    fun testSuspend4() = runBlocking<Unit>{
        val time = measureTimeMillis {
            val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
            val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
            // 执行一些计算
            //one.start() // 启动第一个
            //two.start() // 启动第二个
            println("The answer is ${one.await() + two.await()}")
        }
        println("Completed in $time ms")

        // 不调用start而只是调用await函数，结果会是顺序执行的
        """
            The answer is 42
            Completed in 2047 ms
        """.trimIndent()
    }

    // somethingUsefulOneAsync 函数的返回值类型是 Deferred<Int>
    fun somethingUsefulOneAsync() = GlobalScope.async {
        doSomethingUsefulOne()
    }

    // somethingUsefulTwoAsync 函数的返回值类型是 Deferred<Int>
    fun somethingUsefulTwoAsync() = GlobalScope.async {
        doSomethingUsefulTwo()
    }

    fun testSuspendUnSuitTest() {
        val time = measureTimeMillis {
            // 我们可以在协程外面启动异步执行
            val one = somethingUsefulOneAsync()
            val two = somethingUsefulTwoAsync()
            // 但是等待结果必须调用其它的挂起或者阻塞
            // 当我们等待结果的时候，这里我们使用 `runBlocking { …… }` 来阻塞主线程
            runBlocking {
                println("The answer is ${one.await() + two.await()}")
            }
        }
        println("Completed in $time ms")

        // 这里如果somethingUsefulOneAsync或者one.await()抛出异常，都会导致程序中止
    }





    suspend fun concurrentSum(): Int = coroutineScope {
        val one = async { doSomethingUsefulOne() }
        val two = async { doSomethingUsefulTwo() }
        one.await() + two.await()
    }

    @Test
    fun testSuspend5() = runBlocking<Unit>{
        val time = measureTimeMillis {
            println("The answer is ${concurrentSum()}")
        }
        println("Completed in $time ms")

        """
            The answer is 42
            Completed in 1040 ms
        """.trimIndent()
    }

    suspend fun failedConcurrentSum(): Int = coroutineScope {
        val one = async<Int> {
            try {
                delay(Long.MAX_VALUE) // 模拟一个长时间的运算
                42
            } finally {
                println("First child was cancelled")
            }
        }
        val two = async<Int> {
            println("Second child throws an exception")
            throw ArithmeticException()
        }
        one.await() + two.await()
    }

    @Test
    fun testSuspend6() = runBlocking<Unit> {
        try {
            failedConcurrentSum()
        } catch(e: ArithmeticException) {
            println("Computation failed with ArithmeticException")
        }

        // 一个协程失败就会导致其他在运行中或者正在等待执行的协程都会被取消
        """
            Second child throws an exception
            First child was cancelled
            Computation failed with ArithmeticException
        """.trimIndent()

    }


}