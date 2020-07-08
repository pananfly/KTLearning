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

    @Test
    fun testContext() = runBlocking<Unit> {
        GlobalScope.launch { // 跟Default一样
            println("GlobalScope launch      : I'm working in thread ${Thread.currentThread().name}")
        }
        launch { // 运行在父协程的上下文中，即 runBlocking 主协程
            println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
        }
        launch(Dispatchers.Unconfined) { // 不受限的——将工作在主线程中
            println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
        }
        launch(Dispatchers.Default) { // 将会获取默认调度器
            println("Default               : I'm working in thread ${Thread.currentThread().name}")
        }
        launch(newSingleThreadContext("MyOwnThread")) { // 将使它获得一个新的线程
            println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
        }

        """
            GlobalScope launch      : I'm working in thread DefaultDispatcher-worker-1 @coroutine#2
            Unconfined            : I'm working in thread main @coroutine#4
            Default               : I'm working in thread DefaultDispatcher-worker-1 @coroutine#5
            newSingleThreadContext: I'm working in thread MyOwnThread @coroutine#6
            main runBlocking      : I'm working in thread main @coroutine#3
        """.trimIndent()
    }

    @Test
    fun testContext2() = runBlocking<Unit> {
        println("main-===      : I'm working in thread ${Thread.currentThread().name}")
        launch(Dispatchers.Unconfined) { // 非受限的——将和主线程一起工作
            println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
            delay(500)
            println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
        }
        launch { // 父协程的上下文，主 runBlocking 协程
            println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
            delay(1000)
            println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
        }

        // Unconfined协程中被挂起后使用的是默认的执行者线程中恢复执行
        // 非受限的调度器是一种高级机制，可以在某些极端情况下提供帮助而不需要调度协程以便稍后执行或产生不希望的副作用，
        // 因为某些操作必须立即在协程中执行。 非受限调度器不应该在通常的代码中使用。
        """
            Unconfined      : I'm working in thread main @coroutine#2
            main runBlocking: I'm working in thread main @coroutine#3
            Unconfined      : After delay in thread kotlinx.coroutines.DefaultExecutor @coroutine#2
            main runBlocking: After delay in thread main @coroutine#3
        """.trimIndent()
    }

    @Test
    fun testContext3() = runBlocking<Unit> {
        // 使用标准函数use释放线程
        newSingleThreadContext("Ctx1").use { ctx1 ->
            newSingleThreadContext("Ctx2").use { ctx2 ->
                runBlocking(ctx1) {
                    println("Started in ctx1 in thread ${Thread.currentThread().name}")
                    withContext(ctx2) {
                        println("Working in ctx2 in thread ${Thread.currentThread().name}")
                    }
                    println("Back to ctx1 in thread ${Thread.currentThread().name}")
                }
            }
        }

        """
            Started in ctx1 in thread Ctx1 @coroutine#2
            Working in ctx2 in thread Ctx2 @coroutine#2
            Back to ctx1 in thread Ctx1 @coroutine#2
        """.trimIndent()
    }

    @Test
    fun testContext4() = runBlocking<Unit> {
        println("My job is ${coroutineContext[Job]}")
        println("My job isActive ${coroutineContext[Job]?.isActive}")
        println("My job isCompleted ${coroutineContext[Job]?.isCompleted}")
        println("My job isCancelled ${coroutineContext[Job]?.isCancelled}")

        // 检索协程上下文中job的状态
        """
            My job is "coroutine#1":BlockingCoroutine{Active}@34033bd0
            My job isActive true
            My job isCompleted false
            My job isCancelled false
        """.trimIndent()
    }

    @Test
    fun testContext5() = runBlocking<Unit> {

        // 启动一个协程来处理某种传入请求（request）
        val request = launch {
            // 孵化了两个子作业, 其中一个通过 GlobalScope 启动
            GlobalScope.launch {
                println("job1: I run in GlobalScope and execute independently!")
                delay(1000)
                println("job1: I am not affected by cancellation of the request")
            }
            // 另一个则承袭了父协程的上下文
            launch {
                delay(100)
                println("job2: I am a child of the request coroutine")
                delay(1000)
                println("job2: I will not execute this line if my parent request is cancelled")
            }
        }
        delay(500)
        request.cancel() // 取消请求（request）的执行
        delay(1000) // 延迟一秒钟来看看发生了什么
        println("main: Who has survived request cancellation?")

        // GlobalScope子协程在父协程被取消时不影响其执行
        """
            job1: I run in GlobalScope and execute independently!
            job2: I am a child of the request coroutine
            job1: I am not affected by cancellation of the request
            main: Who has survived request cancellation?
        """.trimIndent()
    }

    @Test
    fun testContext6() = runBlocking<Unit> {
        // 启动一个协程来处理某种传入请求（request）
        val request = launch {
            repeat(3) { i -> // 启动少量的子作业
                launch  {
                    delay((i + 1) * 200L) // 延迟 200 毫秒、400 毫秒、600 毫秒的时间
                    println("Coroutine $i is done")
                }
            }
            println("request: I'm done and I don't explicitly join my children that are still active")
        }
        request.join() // 等待请求的完成，包括其所有子协程
        println("Now processing of the request is complete")
    }

    fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

    @Test
    fun testContext7() = runBlocking<Unit>(CoroutineName("main")) {
        log("Started main coroutine")
        // run two background value computations
        val v1 = async(CoroutineName("v1coroutine")) {
            delay(500)
            log("Computing v1")
            252
        }
        val v2 = async(CoroutineName("v2coroutine")) {
            delay(1000)
            log("Computing v2")
            6
        }
        log("The answer for v1 / v2 = ${v1.await() / v2.await()}")

        //自定义协程名称
        """
            [main @main#1] Started main coroutine
            [main @v1coroutine#2] Computing v1
            [main @v2coroutine#3] Computing v2
            [main @main#1] The answer for v1 / v2 = 42
        """.trimIndent()
    }

    @Test
    fun testContext8() = runBlocking<Unit> {
        // 组合上下文元素
        launch(Dispatchers.Default + CoroutineName("testName")) {
            println("I'm working in thread ${Thread.currentThread().name}")
        }

        """
            I'm working in thread DefaultDispatcher-worker-1 @testName#2
        """.trimIndent()
    }

    class MainScopeTest {
//        private val mainScope = MainScope() // MainScope需要用到安卓的东西，在这里执行不了，会报异常
        private val mainScope = CoroutineScope(Dispatchers.Default)

        fun destroy() {
            mainScope.cancel()
        }

        fun doSomething() {
            // 在示例中启动了 10 个协程，且每个都工作了不同的时长
            repeat(10) { i ->
                mainScope.launch {
                    delay((i + 1) * 200L) // 延迟 200 毫秒、400 毫秒、600 毫秒等等不同的时间
                    println("Coroutine $i is done")
                }
            }
        }
    }

    @Test
    fun testContext9() = runBlocking<Unit> {
        val activity = MainScopeTest()
        activity.doSomething() // 运行测试函数
        println("Launched coroutines")
        delay(500L) // 延迟半秒钟
        println("Destroying activity!")
        activity.destroy() // 取消所有的协程
        delay(1000) // 为了在视觉上确认它们没有工作

        """
            Launched coroutines
            Coroutine 0 is done
            Coroutine 1 is done
            Destroying activity!
        """.trimIndent()
    }

    val threadLocal = ThreadLocal<String?>() // declare thread-local variable

    @Test
    fun testContext10() = runBlocking<Unit> {
        threadLocal.set("main")
        println("Pre-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
        val job = launch(Dispatchers.Default + threadLocal.asContextElement(value = "launch")) {
            println("Launch start, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
            yield()
            println("After yield, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
            threadLocal.ensurePresent()
        }
        job.join()
        println("Post-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")

        """
            ThreadLocal 具有一流的支持，可以与任何 kotlinx.coroutines 提供的原语一起使用。 但它有一个关键限制，
            即：当一个线程局部变量变化时，则这个新值不会传播给协程调用者（因为上下文元素无法追踪所有 ThreadLocal 对象访问），
            并且下次挂起时更新的值将丢失。
        """.trimIndent()

        """
            Pre-main, current thread: Thread[main @coroutine#1,5,main], thread local value: 'main'
            Launch start, current thread: Thread[DefaultDispatcher-worker-1 @coroutine#2,5,main], thread local value: 'launch'
            After yield, current thread: Thread[DefaultDispatcher-worker-1 @coroutine#2,5,main], thread local value: 'launch'
            Post-main, current thread: Thread[main @coroutine#1,5,main], thread local value: 'main'
        """.trimIndent()
    }
}