package four

import java.io.File

fun loadInputFromFile(filePath : String) : List<String>
{
    return File(filePath).readLines()
}

fun transformInput(input : List<String>) : List<List<String>>
{
    return input.map{word -> word.split(" ")}
}

fun main(args: Array<String>) {
    val input = loadInputFromFile("resources/input_4.txt")
    val transformedInput = transformInput(input)
    println("first part answer: ${firstPart(transformedInput)}")
    println("second part answer: ${secondPart(transformedInput)}")
}

fun firstPart(input : List<List<String>>) : Int
{
    return input.count { passphrase -> passphrase.size == passphrase.distinct().size }
}

fun secondPart(input : List<List<String>>) : Int
{
    return input.count{ passphrase -> passphrase.size == passphrase.distinctBy { s -> s.toList().sorted() }.size}
}