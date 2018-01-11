package six

val Input6 = "11\t11\t13\t7\t0\t15\t5\t5\t4\t4\t1\t1\t7\t1\t15\t11"
//val Input6 = "0\t2\t7\t0"
fun transformInput(input: String) = input.split("\t").mapTo(mutableListOf(), { i -> i.toInt()})

fun main(args: Array<String>) {
    val transformedInput = {transformInput(Input6)}
    println(firstPart(transformedInput()))
    println(secondPart(transformedInput()))
}

fun firstPart(input : MutableList<Int>) : Int {
    val history = hashSetOf<String>()
    return distributeBlocks(history, input)
}

fun secondPart(input : MutableList<Int>) : Int {
    val history = linkedSetOf<String>()
    val cycles = distributeBlocks(history, input)
    val inp = input.joinToString { i -> i.toString() }
    val ind = history.indexOf(inp)
    return cycles - history.indexOf(inp)
}

tailrec fun distributeBlocks(history : HashSet<String>, input : MutableList<Int>) : Int{
    val inputAsString = input.joinToString { i -> i.toString() }
    if (history.contains(inputAsString))
        return history.size
    history += inputAsString
    val memBankWithMostBlocks = input.indexOf(input.max())
    var blocksToDistribute = input[memBankWithMostBlocks]
    input[memBankWithMostBlocks] = 0
    var index = memBankWithMostBlocks + 1
    while (blocksToDistribute > 0)
    {
        if (index >= input.size) index = 0
        input[index]++
        blocksToDistribute--
        index++
    }
    return distributeBlocks(history, input)
}


