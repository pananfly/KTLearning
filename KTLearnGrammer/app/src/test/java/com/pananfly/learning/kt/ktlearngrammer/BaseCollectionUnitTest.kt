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
}