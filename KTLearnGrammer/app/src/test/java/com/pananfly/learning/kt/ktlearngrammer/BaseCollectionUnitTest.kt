package com.pananfly.learning.kt.ktlearngrammer

import org.junit.Test

class BaseCollectionUnitTest {

    //可以接收不同集合类型, 并且只能只读，不可写
    fun printCollection(c: Collection<String>) {
        for(s in c) {
            print("$s ")
        }
        println()
    }

    // MutableList 可读可写，这里的参数只能接收指定的MutableList集合类型
    fun List<String>.filterShortWords(words: MutableList<String>, maxLen: Int) {
        this.filterTo(words) {it.length <= maxLen} // 过滤字符串长度是maxLen 以下的
        val articles = setOf<String>("a", "A", "an", "An", "the", "The")
        words -= articles // 过滤掉含有articles集合的内容字符串
    }

    @Test
    fun testCollection() {
        val list = listOf<String>("1", "2")
        val set = setOf<String>("11", "22")
        printCollection(list)
        printCollection(set)
        println("====0=====")
        val words = "A long time ago in a galaxy far far away".split(" ")
        val shortWords = mutableListOf<String>()
        words.filterShortWords(shortWords, 3)
        println(shortWords)
        println("====1=====")
        val numbers = listOf<String>("one", "two", "three", "four")
        println("size: ${numbers.size}")
        println("Third element: ${numbers.get(2)}")
        println("Fourth element: ${numbers[3]}")
        println("index element: ${numbers.indexOf("two")}")
        println("====2=====")
        //mutableList
        val mList = mutableListOf<Int>(1, 2, 3, 4)
        mList.add(5)
        mList.removeAt(1)
        mList[0] = 0
        mList.shuffle() // 随机排列
        println(mList)
    }

    @Test
    fun testSimpleSet() {
        //HashSet 不保留元素的顺序，但可以只需要较少的内存来存储相同的元素集合
        val numbers = setOf<Int>(1, 2, 3, 4) // 默认实现LinkedHashSet 保留元素插入的顺序
        println("size: ${numbers.size}")
        if(numbers.contains(1)) {
            println("1 is in the set.")
        }
        val numbers2 = setOf<Int>(1, 2, 3, 4)
        println("the two sets are equal: ${numbers == numbers2}")
        println("====0=====")
        val mSet = mutableSetOf<Int>(1, 2, 3)
        mSet.remove(4) // 不存在则不会影响
        println(mSet)
        mSet.add(1)
        println(mSet)
    }

    @Test
    fun testSimpleMap() {
        // 无论键值对顺序如果，包含相同的键值对的两个map是相等的
        val maps = mapOf<Int, Int>(1 to 11, 2 to 22) // 默认实现LinkedHashMap
        println("All keys: ${maps.keys}")
        println("All values: ${maps.values}")
        if(1 in maps) {
            println("value of key \"1\" is: ${maps[1]}")
        }
        if(11 in maps.values) {
            println("value \"11\" in the map ")
        }
        if(maps.values.contains(11)) {
            println("value \"11\" in the map ")
        }
        println("=====0=====")
        val mMaps = mutableMapOf<Int, Int>(1 to 11, 2 to 22)
        println("The two maps equal:${maps == mMaps}") // 不可写和可读写的两个map元素一样也是相等
        mMaps.put(3, 33)
        mMaps[4] = 44

    }

    fun emptyCollection() {
        val emptyList = emptyList<String>()
        val emptySet = emptySet<String>()
        val emptyMap = emptyMap<String, Int>()
    }

    fun copyCollection() {
        val sourceList = mutableListOf<Int>(1, 2, 3)
        val copyList = sourceList.toList()
        val copyList2 = sourceList.toMutableSet()
        val copyList3 = sourceList.toMutableList()

        //构建引用
        val referenceList: List<Int> = sourceList
        // referenceList[1] = 1 // 不允许在引用上修改
    }

    @Test
    fun handleCollection() {
        val num1 = listOf<String>("one", "two", "three", "four")
        val numFilter = num1.filter { it.length > 3 }
        println(numFilter)
        println(num1.associateWith { it.length }) // 生成map
        println("=====0======")
        val num2 = setOf<Int>(1, 2, 3)
        println(num2.map { it * 3 })
        println(num2.mapIndexed { index, value -> index * value })
        // page 234
    }

    @Test
    fun testIterator() {
        val nums = listOf<Int>(1, 2, 3)
        val numsIterator = nums.iterator()
        while (numsIterator.hasNext()) {
            print("${numsIterator.next()} ")
        }
        println()
        for (item in nums) {
            print("$item ")
        }
        println()
        nums.forEach {
            print("$it ")
        }
        println()

        println("====0=====")
        //ListIterator 可以正向和反响迭代
        val numsListIterator = nums.listIterator()
        while (numsListIterator.hasNext()) {
            println("${numsListIterator.nextIndex()}: ${numsListIterator.next()}")
        }
        println("====1=====")
        while (numsListIterator.hasPrevious()) {
            println("${numsListIterator.previousIndex()}: ${numsListIterator.previous()}")
        }
        println("====1=====")
        val mNums = mutableListOf<Int>(1, 2, 3, 4, 5)
        val mIterator = mNums.iterator()
        if(mIterator.hasNext()) {
            mIterator.next()
            mIterator.remove()
        }
        if(mIterator.hasNext()) {
            mIterator.next()
            mIterator.remove()
        }
        mNums.forEach{print("$it ")}
        println()
        println("====2=====")
        val mListIterator = mNums.listIterator()
        if (mListIterator.hasNext()) {
            mListIterator.next()
        }
        mListIterator.add(123) // add value
        if (mListIterator.hasNext()) {
            mListIterator.next()
            mListIterator.set(666) // set current index's value
        }
        mNums.forEach{print("$it ")}
        println()
    }

    class Version(val major: Int, val minor: Int) : Comparable<Version> {
        override fun compareTo(other: Version): Int {
            if(this.major != other.major) {
                return this.major - other.major
            }
            return this.minor - other.minor
        }
    }

    @Test
    fun testRange() {
        val x = 1
        if(x in 1..4) {
            println("x=$x in 1 to 4 range")
        }
        println("====0=====")
        for (i in 1..4) {
            print("$i ")
        }
        println()
        println("====1=====")
        for (i in 4 downTo 1) {
            print("$i ")
        }
        println()
        println("====2=====")
        for (i in 1..4 step  2) {
            print("$i ")
        }
        println()
        println("====3=====")
        for (i in 4 downTo 1 step  2) {
            print("$i ")
        }
        println()
        println("====4=====")
        for (i in 1 until 4) { // 不包含4
            print("$i ")
        }
        println()
        println("====5=====")
        val versionRange = Version(1, 11)..Version(1, 30) // 创建区间使用 ..
        println(Version(0, 9) in  versionRange) // false
        println(Version(1, 20) in  versionRange) // true
        // page 239
    }

    @Test
    fun testSequence() {
        val numberSequence = sequenceOf(1, 2, 3, 4) // 直接创建
        val nums = listOf<Int>(1, 2)
        val numberSequence2 = nums.asSequence() // 从Iterable创建
        val oddNumbers = generateSequence(1) { it + 2} // 函数创建，此序列是无限的，it是上一个元素的值，所以lambda表达式的操作只对第二个及之后的元素有作用
        println(oddNumbers.take(5).toList())
        val oddNumbers2 = generateSequence(1){ if(it < 10) it + 2 else null } // 最后返回null则会创建一个有效的序列
        println("size: ${oddNumbers2.count()} -> ${oddNumbers2.toList()}")
        val blockSequences = sequence {
            yield(1)
            yieldAll(listOf(3, 5))
            yieldAll(generateSequence (7){ it + 2 }) // 这里是无限的元素，之后再写其他的yield是无效的
        }
        println(blockSequences.take(10).toList())
    }

    data class FullName (val firstName: String, val lastName: String)
    fun parseFullName(fullName: String?): FullName {
        val params = fullName?.split(" ")
        return if(params?.size == 2) {
            FullName(params[0], params[1])
        } else {
            FullName(fullName.toString(), fullName.toString())
        }
    }

    @Test
    fun testHandleCollection2() {
        val numbers = listOf<String>("one", "two", "three", "four")
        val filterNumbers = numbers.filter { it.length > 3 } // 此操作不会对numbers本身有影响, 过滤结果赋值给了filterNumbers
        println("Numbers are still $numbers")
        println("filterNumbers are $filterNumbers")
        // page 247

        val result = numbers.mapTo(HashSet()) {it.length}
        println(result)
        println("====0======")
        val numbers2 = mutableListOf<String>("one", "two", "three", "four")
        val sortNumbers = numbers2.sorted()
        println(sortNumbers)
        println(numbers2 == sortNumbers) // false
        println("====1======")
        numbers2.sort()
        println(numbers2)
        println(numbers2 == sortNumbers) // true
        println("====2======")
        val numbers3 = listOf<Int>(1, 2, 3)
        println(numbers3.map { it * 3 }) // 不会对numbers3有影响
        println(numbers3)
        println(numbers3.mapIndexed { index, value -> index * value })
        println(numbers3.mapNotNull { if (it == 2) null else it * 3 })
        println(numbers3.mapIndexedNotNull { index, i -> if(index == 0) null else index * i })
        println("====3======")
        val numbersMap = mapOf<String, Int>("Key1" to 1, "Key2" to 2, "Key3" to 3, "Key11" to 11)
        println(numbersMap.mapKeys { it.key.toUpperCase() }) // 对key做转换
        println(numbersMap.mapValues { it.value + it.key.length }) // 对值做转换
        println("====4======")
        val colors = listOf<String>("Red", "brown", "grey")
        val animals = listOf<String>("fox", "brear", "wolf")
        println(colors zip animals) // 返回pair对象的列表 [(Red, fox), (brown, brear), (grey, wolf)]
        val lessAnimals = listOf<String>("fox", "brear")
        println(colors.zip(lessAnimals)) // 根据较少的返回，较长的部分忽略 [(Red, fox), (brown, brear)]
        println(colors.zip(animals) {color, animal -> "The ${animal.capitalize()} is $color"}) // 返回List<T>
        println("====5======")
        val numberPairs = listOf("one" to 1, "two" to 2, "three" to 3, "four" to 4)
        println(numberPairs.unzip()) // 返回Pair<List<T>, List<R>> 第一个List返回键, 第二个List返回值
        println("====6======")
        println(numbers.associateWith { it.length }) // 返回map<K, V>, 原始值是键, 转换函数中产生值
        println(numbers.associateBy { it.first().toUpperCase() })// 返回map<K, V>, 原始值是值, 转换函数中产生键, 有相同键则只保留最后一个
        println(numbers.associateBy(keySelector = {it.first().toUpperCase()}, valueTransform = {it.length}))// 返回map<K, V>, 自指定键值, 有相同键则只保留最后一个
        val names = listOf("Alice Adams", "Brian Brown", "Clara Campbell")
        println(names.associate { name -> parseFullName(name).let { it.lastName to it.firstName  } })// 返回Map<K, V>
    }

    @Test
    fun testHandleCollection3() {
        val numberSets = listOf(setOf(1, 2, 3), setOf(4, 5, 6), setOf(1, 2))
        println(numberSets.flatten()) // 返回List<T> 返回所有元素的List集合
        println(numberSets.flatMap { it.plus("100").minus(1)}) // 返回List<T>, 对每个set进行转换，在其后拼接100, 并减去集合中的1
        println("======0======")
        val numbers = listOf<String>("one", "two", "three", "four")
        println(numbers.joinToString()) // 构建成单个String
        println(numbers.joinToString {"Element: ${it.toUpperCase()}"}) // 构建成单个String
        println(numbers.joinToString(separator = "---", prefix = "####", postfix = "=====")) // 构建成单个String, 自定义拼接、前缀、后缀字符
        val strBuffer = StringBuffer("pananfly :")
        numbers.joinTo(strBuffer) // numbers 拼接到strBuffer之后并赋值给strBuffer
        println(strBuffer)
        println(numbers)
    }

    @Test
    fun testHandleCollection4() {
        val numbers = listOf("one", "two", "three", "four")
        println(numbers.filter { it.length > 3 })
        println(numbers.filterIndexed { i, s -> i != 0 && s.length < 5 })
        println(numbers.filterNot { it.length <= 3 }) // 取过滤结果为false的集合
        val numbersMap = mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key11" to 11)
        println(numbersMap.filter { (k, v) -> k.endsWith("1") && v > 10 })
        val numbers2 = listOf(null, 1, "two", 3.0, "four")
        print("Filter String result: ")
        numbers2.filterIsInstance<String>().forEach { print("$it ") } // 指定类型过滤
        println()
        val numbers3 = listOf(null, "123", "two", "four")
        println(numbers3.filterNotNull()) // 过滤null

        println("=====0======")
        val (match, rest) = numbers.partition { it.length > 3 } // 返回Pair<List<T>, List<T>> 前者是符合条件的结果，后者是其他的
        println(match)
        println(rest)
        println("=====1======")
        val empty = emptyList<Int>()
        println(numbers.any { it.endsWith("e") }) // true// 至少有一个元素匹配则返回true
        println(numbers.any()) // true
        println(empty.any()) // false
        println("=====2======")
        println(numbers.none { it.endsWith("a") }) // true // 没有一个元素匹配则返回true
        println(numbers.none()) // false 有元素则为false
        println(empty.none()) // true
        println("=====3======")
        println(numbers.all { it.endsWith("e") })  // false // 所有元素都匹配才返回true，否则false
        println(empty.all { it > 5 }) // true // 在一个空集合上调用all都会返回true

        // page 258
    }

    @Test
    fun testHandleCollection5() {
        val numbers = listOf<Int>(1, 2, 3, 4, 5, 2, 4, 6)
        val plusNumbers = numbers + 55
        println(plusNumbers)
        // plus(+) 和 minus(-) 都是返回的只读集合
        val minusNumbers = numbers - 2 // 当第二个操作数是一个元素的时候，只移除第一个操作数中符合的第一个元素
        println(minusNumbers) // [1, 3, 4, 5, 2, 4, 6]
        val minusNumbers2 = numbers - listOf<Int>(2) // 当第二个操作数是一个集合的时候，移除第一个操作数中所有符合的元素
        println(minusNumbers2) // [1, 3, 4, 5, 4, 6]
        println("========")
        val mapNumbers = mapOf<Int, Int>(1 to 1, 2 to 2, 3 to 3, 4 to 4)
        val plusMapNumbers = mapNumbers + (5 to 5)
        println(plusMapNumbers)
        val minusMapNumbers = mapNumbers - (1 to 1) // 操作无效
        println(minusMapNumbers)
        val minusMapNumbers2 = mapNumbers - mapOf<Int, Int>(1 to 1) // 操作无效
        println(minusMapNumbers2)
        println(mapNumbers)
    }

    @Test
    fun testHandleCollection6() {
        val numbers = listOf("one", "two", "three", "four", "five")
        println(numbers.groupBy { it.first().toUpperCase() })// 返回的是map<k, List<T>>
        println(numbers.groupBy(keySelector = {it.first()}, valueTransform = {it.toUpperCase()}))
        println(numbers.groupingBy { it.first() }.eachCount()) // 计算每个分组的个数

        println(numbers.groupingBy { it.first() }.fold(initialValueSelector = { _, _ -> kotlin.jvm.internal.Ref.IntRef() },
            operation = { _, acc, _ -> acc.apply { element += 1 } })) // 计算每个分组的个数
        val numbers2 = listOf<Any>("one", "two", "three", "four", "five", 1)
        println(numbers2.groupingBy { it.toString().length }.fold(0, {t, k -> if(k !is String) t else k.length}))
    }

    @Test
    fun testHandleCollection7() {
        val numbers = listOf("one", "two", "three", "four", "five", "six")
        println(numbers.slice(1..3)) // 返回List<T>, 取下标为1-3的元素
        println(numbers.slice(0..4 step 2)) // 步长为2
        println(numbers.slice(setOf(1, 5, 0)))
//        println(numbers.slice(setOf(1, 5, 0, 15))) // 15 会报ArrayIndexOutOfBoundsException
        println("=====0=====")
        println(numbers.take(3)) // 从头开始获取3个数量的元素
        println(numbers.takeLast(3)) // 从尾开始获取3个数量的元素，按照原来的顺序 [four, five, six]
        println(numbers.take(10)) // 不会报越界
        println(numbers.takeLast(10)) // 不会报越界
        println(numbers.drop(1)) // 从头开始去掉1个元素
        println(numbers.dropLast(2)) // 从尾部开始去掉2个元素
        println(numbers.drop(10)) // 不会报越界 返回空集合
        println(numbers.dropLast(10)) // 不会报越界 返回空集合
        println("=====1=====")
        // 下面这一段相当拗口
        """
            takeWhile() 是带有谓词的 take() ：它将不停获取元素直到排除与谓词匹配的首个元
        素。如果首个集合元素与谓词匹配，则结果为空。
            takeLastWhile() 与 takeLast() 类似：它从集合末尾获取与谓词匹配的元素区间。区间取集合的一部分
        的首个元素是与谓词不匹配的最后一个元素右边的元素。如果最后一个集合元素与谓词匹配，则结果为空。
            dropWhile() 与具有相同谓词的 takeWhile() 相反：它将首个与谓词不匹配的元素返回到末尾。
            dropLastWhile() 与具有相同谓词的 takeLastWhile() 相反：它返回从开头到最后一个与谓词不匹配的元素。
        """.trimIndent()
        val numbers2 = listOf("one", "1", "two", "three", "four", "five", "six")
        println(numbers2.takeWhile { it.length == 3 }) // 从集合首元素判断，第一个符合条件的则有值，第二个或之后有不符合条件的则停止判断
        println(numbers2.takeWhile { it.length == 5 }) // 从集合首元素判断，第一个不符合条件则为空
        println(numbers2.takeLastWhile { it.length == 3 }) // 只有最后一个元素符合条件才返回有值，否则为空
        println(numbers2.takeLastWhile { it.length == 4 }) // 只有最后一个元素符合条件才返回有值，否则为空
        println(numbers2.dropWhile { it.length == 3 }) // 根据条件去掉元素, 只有第一个符合条件才去掉，否则不处理后面的数据
        println(numbers2.dropLastWhile { it.length == 4 }) // 根据条件去掉元素, 只有最后一个符合条件才去掉，否则不处理后面的数据

    }

    @Test
    fun testHandleCollection8() {
        val numbers = (0..13).toList()
        println(numbers.chunked(3)) // 返回List<List<T>>
        println(numbers.chunked(3) {it.sum()}) // 返回List<T>
        println(numbers.chunked(15)) // 大于当前总数量，则内部list只有一个集合，不会为空
        println("===0====")
        println(numbers.windowed(3)) // 返回List<List<T>> 每3个元素作为一个集合，从下一个元素开始又3个元素作集合，一次类推
        println(numbers.windowed(3) {it.sum()}) //
        println(numbers.windowed(15)) // 元素不够返回空
        println(numbers.windowed(3, step = 2, partialWindows = true)) // partialWindows为真则最后不够3个元素的集合也会返回
        println(numbers.windowed(3, step = 2, partialWindows = false)) // partialWindows为假则最后不够3个元素的集合不会返回
        println("===1====")
        println(numbers.zipWithNext()) // 返回List<Pair<T, T>> 步进为1, 前后两个元素组合为一个pair [(0, 1), (1, 2), (2, 3), (3, 4), (4, 5), (5, 6), (6, 7), (7, 8), (8, 9), (9, 10), (10, 11), (11, 12), (12, 13)]
        println(numbers.zipWithNext {i1,i2 -> i1 > i2}) // 返回List<T>
    }

    @Test
    fun testHandleCollection9() {
        val numbers = linkedSetOf<Int>(1 , 2, 3, 4, 5)
        println(numbers.elementAt(3)) // 4
        println(numbers.elementAtOrNull(6)) // null
        println(numbers.elementAtOrElse(6) { 10000 }) // 10000
        println("====0=====")
        println(numbers.first()) // 1
        println(numbers.first { it > 2 }) // 3 按照条件取值
        println(numbers.firstOrNull { it > 6 }) // null
        println(numbers.find { it > 6 }) // null 同firstOrNull
        println("====1=====")
        println(numbers.last()) // 5
        println(numbers.last { it < 4 }) // 3 按照条件取值
        println(numbers.lastOrNull { it < 0 }) // null
        println(numbers.findLast { it < 0 }) // null 同lastOrNull
        println("====2=====")
        println(numbers.random())
        println(numbers.contains(2))
        println( 4 in numbers)
        println(numbers.containsAll(listOf(1, 2)))
        println(numbers.isEmpty())
        println(numbers.isNotEmpty())
        println("====3=====")
        val sortNumber = sortedSetOf("one", "two", "three", "four")
        println(sortNumber.elementAt(0)) // four

        //page 269
    }

    @Test
    fun testHandleCollection10() {
        // 自定义排序
        val stringComparator = Comparator { t: String, t2: String -> t.length - t2.length }
        println(listOf("aaa", "bb", "c", "resaf").sortedWith(stringComparator))
        println(listOf("aaa", "bb", "c", "resaf").sortedWith(compareBy {it.length})) // 简要实现，同上面的stringComparator
        println("=====0=====")
        val numbers = listOf("one", "two", "three", "four")
        println("Sort ascending: ${numbers.sorted()}") // 升序
        println("Sort by length ascending: ${numbers.sortedBy { it.length }}") // 升序
        println("Sort descending: ${numbers.sortedDescending()}") // 降序
        println("Sort by last letter descending: ${numbers.sortedByDescending { it.last() }}") // 降序
        println("=====1=====")
        println("reversed: ${numbers.reversed()}") // 倒序 [four, three, two, one]
        println("as reversed: ${numbers.asReversed()}") // 倒序
        val numbers2 = mutableListOf("one", "two", "three", "four")
        val asReverNumber = numbers2.asReversed()
        println(asReverNumber) // [four, three, two, one]
        numbers2.add("five") // 当可变数组有改变时，asReversed() 产生的数组也会有相应的变化
        println(asReverNumber) // [five, four, three, two, one]
        println("=====2=====")
        println("shuffled: ${numbers.shuffled()}") // 随机排序
    }

    @Test
    fun testHandleCollection11() {
        val numbers = listOf(6, 42, 10, 4)
        println("Count: ${numbers.count()} -- size:${numbers.size}") // 集合的元素的数量
        println("Max: ${numbers.max()}")
        println("Min: ${numbers.min()}")
        println("Sum: ${numbers.sum()}")
        println("Average: ${numbers.average()}")
        println("Min by: ${numbers.minBy { it % 3 }}")
        println("4%3: ${4 % 3}")
        println("10%3: ${10 % 3}")
        println("max with: ${numbers.maxWith(compareBy {it % 3})}") // 10和4模3都是1，为啥就结果是10，难道是以第一个为主吗？？？
        println("sum by: ${numbers.sumBy { it * 2 }}")
        println("sum by double: ${numbers.sumByDouble { it.toDouble() * 2 }}")
        // page 274
        println("Sum by reduce: ${numbers.reduce { sum, element -> sum + element }}")  // reduce 第一次的sum是第一个元素，element是第二个元素
        println("Sum by fold: ${numbers.fold(0) {sum, element -> sum + element * 2}}") // fold 第一次的sum是指定的初始值，element是第一个元素
        println("Sum by reduce 2: ${numbers.reduce{sum, element -> sum + element * 2}}") // 结果有出入，第一个元素并不参与倍数2的操作
        // page 275

        println("Sum by reduceRight: ${numbers.reduceRight{element, sum -> sum + element}}") // reduceRight第一次的sum是最后一个元素，element是倒数第二个元素，注意和reduce的lambda表达式的区别，sum和element位置有区别
        println("Sum by foldRight: ${numbers.foldRight(0){element, sum -> sum + element}}") // reduceRight第一次的sum是初始化值，element是倒数第一个元素，注意和foldRight的lambda表达式的区别，sum和element位置有区别
        println("Sum by foldIndexed: ${numbers.foldIndexed(0){idx, sum, element -> if(idx % 2 == 0) sum + element else sum}}") //16： 0 + 6 + 10
        println("Sum by foldRightIndexed: ${numbers.foldRightIndexed(0){idx, element, sum -> if(idx % 2 == 0) sum + element else sum}}") //16： 0 + 10 + 6
        println("Sum by reduceIndexed: ${numbers.reduceIndexed{idx, sum, element -> if(idx % 2 == 0) sum + element else sum}}") // 16： 6 + 10
        println("Sum by reduceRightIndexed: ${numbers.reduceRightIndexed{idx, element, sum -> if(idx % 2 == 0) sum + element else sum}}") // 20： 4 + 10 + 6， 4是第一个sum
    }

    @Test
    fun testHandleCollection12() {
        val numbers = mutableListOf<Int>(1, 2, 3, 4)
        numbers.add(5)
        numbers.addAll(arrayListOf(6, 7, 8))
        println("1: $numbers")
        numbers.addAll(2, setOf(9, 10))// 在2位置添加2个元素，原本在集合中2后面的元素向后挪位置
        println("2: $numbers")
        numbers += 11
        numbers += listOf<Int>(12, 13)
        println("3: $numbers")
        numbers.remove(3) // 直接删除元素
        println("4: $numbers")
        numbers.remove(100) // 不存在则不做处理
        println("5: $numbers")
        var ret = numbers.removeAll { it == 2 } // 移除符合条件的元素
        println("removeAll: ${numbers}, ret: $ret")
        ret = numbers.removeAll(setOf(1, 2)) // 移除所给集合中的的元素, 2不存在则不处理
        println("removeAll 1, 2: ${numbers}, ret: $ret")
        ret = numbers.retainAll {it % 3 == 0} // 返回符合条件的元素
        println("retainAll: $numbers, ret: $ret")
        numbers += listOf<Int>(109, 111, 110, 121, 221, 121)
        numbers -= 110 // 只删除第一个符合的元素
        println("6: $numbers")
        numbers -= setOf<Int>(111, 121) // 删除第二个操作中，在第一个操作所有符合的元素
        println("7: $numbers")
        numbers.clear() // 移除所有的元素并置空
        println("8: $numbers")
    }

    @Test
    fun testHandleCollection13() {
        var numbers = mutableListOf<String>("one", "two", "three")
        numbers[1] = "www"
        println(numbers)
        numbers.sortWith(compareBy<String> {it.length}.thenBy { it })
        println(numbers)
        numbers.removeAt(1)
        println(numbers)
        numbers.fill("pananfly") // 所有元素都赋值一样
        println(numbers)
        numbers.apply { this.forEachIndexed{ index, s -> set(index, s + index) } }
        println(numbers)
    }

    @Test
    fun testHandleCollection14() {
        val numbers = setOf<String>("one", "two", "three")
        println(numbers union setOf("four", "five")) // set 的联合操作
        println(numbers intersect setOf("three", "four")) // set 的交集操作，查找跟另一个集合中有同样的元素
        println(numbers subtract setOf("three", "four")) // set 的差集操作, 查找另一个集合中不存在的元素
    }

    @Test
    fun testHandleCollection15() {
        val numbersMap = mapOf("one" to 1, "two" to 2, "three" to 3)
        println(numbersMap.get("one"))
        println(numbersMap["one"])
        println(numbersMap.getOrDefault("on", 0))
        println(numbersMap.keys)
        println(numbersMap.values)
        println(numbersMap.filter { (k,_) -> k.contains("t") })
        println(numbersMap.filterKeys { it.contains("t") })
        println(numbersMap.filterValues { it != 2 })
        println(numbersMap + Pair("123", 123))
        println(numbersMap + Pair("one", 11))
        println(numbersMap + mapOf("one" to 11))
        println("====0=====")
        val numbers = mutableMapOf<String, Int>("one" to 1, "two" to 2)
        numbers["three"] = 3
        numbers.putAll(setOf("four" to 4, "five" to 5))
        println("1:$numbers")
        numbers += mapOf<String, Int>("six" to 6)
        println("2:$numbers")
        numbers.remove("one")
        println("3:$numbers")
        numbers.remove("four", 5) // 键或者值，某一个不对应就不会删除
        println("4:$numbers")
        numbers.keys.remove("two") // 通过键删除
        println("5:$numbers")
        numbers.values.remove(6) // 通过值删除
        println("6:$numbers")
        numbers -= "three" // 通过键删除
        println("7:$numbers")
        numbers -= "ssss" // 通过键删除, 不存在不影响
        println("8:$numbers")
        // page 291
    }

}