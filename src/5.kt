package five

import java.io.File

fun loadInputFromFile(filePath : String) : List<String>
{
    return File(filePath).readLines()
}

fun transformInput(input : List<String>) : List<Int>
{
    return input.map{ i -> i.toInt()}
}

fun main(args: Array<String>) {
    val transformedInput = transformInput(loadInputFromFile("resources/input_5.txt"))
    println(firstPart(transformedInput))
    println(secondPart(transformedInput))
}

fun firstPart(input : List<Int>) : Int {
    return jump(input.toMutableList(), 0, 0)
}

fun secondPart(input : List<Int>) : Int{
    return jumpSecondPart(input.toMutableList(), 0,0)
}

tailrec fun jump(input: MutableList<Int>, index : Int, counter : Int) : Int{
    if (index < 0 || index > input.size - 1)
    {
        return counter
    }
    val offset = input[index]
    input[index]++
    return jump(input, index + offset, counter + 1)
}

tailrec fun jumpSecondPart(input: MutableList<Int>, index : Int, counter : Int) : Int{
    if (index < 0 || index > input.size - 1)
    {
        return counter
    }
    val offset = input[index]
    input[index] = if (offset >= 3) input[index] - 1 else input[index] + 1
    return jumpSecondPart(input, index + offset, counter + 1)
}