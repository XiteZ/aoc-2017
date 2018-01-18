package nine

import java.io.File

fun loadInputFromFile(filePath: String): String {
    return File(filePath).readText()
}

fun main(args: Array<String>) {
    val input = loadInputFromFile("resources/input_9.txt")
    println(firstPart(input, 0, false, false, 0))
    println(secondPart(input, false, false, 0))
}

tailrec fun firstPart(input : String, level : Int, discardNext : Boolean, inGarbage : Boolean, totalScore : Int) : Int
{
    if (input.isEmpty())
        return totalScore
    val char = input[0]
    if (inGarbage)
    {
        if (discardNext)
        {
            return firstPart(input.drop(1), level, false, inGarbage, totalScore)
        }
        if (char == '>')
        {
            return firstPart(input.drop(1), level, discardNext, false, totalScore)
        }
        if (char == '!')
        {
            return firstPart(input.drop(1), level, true, inGarbage, totalScore)
        }
        return firstPart(input.drop(1), level, discardNext, inGarbage, totalScore)
    }
    if (char == '<')
    {
        return firstPart(input.drop(1), level, discardNext, true, totalScore)
    }
    if (char == '{')
    {
        return firstPart(input.drop(1), level + 1, discardNext, inGarbage, totalScore + level + 1)
    }
    if (char == '}')
    {
        return firstPart(input.drop(1), level - 1, discardNext, inGarbage, totalScore)
    }
    return firstPart(input.drop(1), level, discardNext, inGarbage, totalScore)
}

tailrec fun secondPart(input : String, discardNext : Boolean, inGarbage : Boolean, totalScore : Int) : Int
{
    if (input.isEmpty())
        return totalScore
    val char = input[0]
    if (inGarbage)
    {
        if (discardNext)
        {
            return secondPart(input.drop(1), false, inGarbage, totalScore)
        }
        if (char == '>')
        {
            return secondPart(input.drop(1), discardNext, false, totalScore)
        }
        if (char == '!')
        {
            return secondPart(input.drop(1), true, inGarbage, totalScore)
        }
        return secondPart(input.drop(1), discardNext, inGarbage, totalScore + 1)
    }
    if (char == '<')
    {
        return secondPart(input.drop(1), discardNext, true, totalScore)
    }
    return secondPart(input.drop(1), discardNext, inGarbage, totalScore)
}