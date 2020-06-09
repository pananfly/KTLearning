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
}