package ten

const val Input10 = "187,254,0,81,169,219,1,190,19,102,255,56,46,32,2,216"
const val SALT = "17,31,73,47,23"
const val ITERATIONS = 64

fun main(args: Array<String>) {
    println(firstPart())
    println(secondPart())
}

fun secondPart(): String {
    val input = Input10.map { it.toInt() }
    val salt = SALT.split(',').map { Integer.parseInt(it) }
    val lengths = input + salt
    val data = MutableCircularList((0..255).toMutableList())
    shuffle(lengths, data, ITERATIONS)
    val splittedSparseHash = data.windowed(16, 16)
    val denseHash = splittedSparseHash.map { it.reduce { acc, element -> acc xor element } }
    return denseHash.joinToString("") { it.toString(16) }
}

fun firstPart(): Int {
    val input = Input10.split(',').map { Integer.parseInt(it) }
    val list = MutableCircularList((0..255).toMutableList())
    shuffle(input, list)
    return list[0] * list[1]
}

fun shuffle(lengths: List<Int>, data: MutableCircularList<Int>, iterations: Int = 1): MutableCircularList<Int> {
    var position = 0
    var skip = 0
    repeat(iterations) {
        for (length in lengths) {
            val reversed = data.subList(position, position + length).asReversed()
            data.replace(position, reversed)
            position += length + skip
            skip++
        }
    }
    return data

}

open class CircularList<out T>(private val list: List<T>) : List<T> by list {

    override fun get(index: Int): T = list[normalizeIndex(index)]

    override fun listIterator(index: Int): ListIterator<T> = list.listIterator(normalizeIndex(index))

    override fun subList(fromIndex: Int, toIndex: Int): List<T> {
        val fIndex = normalizeIndex(fromIndex)
        val tIndex = normalizeIndex(toIndex)
        if (fromIndex == toIndex) return list.subList(fIndex, tIndex)
        if (fIndex == tIndex) return list.subList(fIndex, size) + list.subList(0, tIndex)
        return if (fIndex <= tIndex) {
            list.subList(fIndex, tIndex)
        } else {
            list.subList(fIndex, size) + list.subList(0, tIndex)
        }
    }

    protected fun normalizeIndex(index: Int): Int =
        if (index < 0) (index % size + size) else index % size
}

class MutableCircularList<T>(private val list: MutableList<T>) : CircularList<T>(list), MutableList<T> by list {
    override fun add(index: Int, element: T) = list.add(normalizeIndex(index), element)
    override fun set(index: Int, element: T) = list.set(normalizeIndex(index), element)
    override fun contains(element: T) = super.contains(element)
    override fun containsAll(elements: Collection<T>) = super.containsAll(elements)
    override fun indexOf(element: T) = super.indexOf(element)
    override fun isEmpty() = super.isEmpty()
    override fun iterator() = list.iterator()
    override fun lastIndexOf(element: T) = super.lastIndexOf(element)
    override fun listIterator() = list.listIterator()
    override fun get(index: Int) = super.get(index)
    override fun listIterator(index: Int) = list.listIterator(normalizeIndex(index))
    override fun subList(fromIndex: Int, toIndex: Int) = super.subList(fromIndex, toIndex).toMutableList()
    override val size: Int
        get() = super.size

    fun replace(startIndex: Int, elements: Collection<T>) {
        val sIndex = normalizeIndex(startIndex)
        val iterator = elements.iterator()
        for (index in sIndex..(sIndex + elements.size - 1)) {
            this[index] = iterator.next()
        }
    }
}