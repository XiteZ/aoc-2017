package seven

import java.io.File

fun loadInputFromFile(filePath: String): List<String> {
    return File(filePath).readLines()
}

fun transformInput(rawInput: List<String>): HashSet<Program> {
    val deserializedInput = rawInput.map { s -> deserialize(s) }
    deserializedInput.forEach { p ->
        val childs = p.childNames.map { childName ->
            deserializedInput.first { p ->
                p.name == childName
            }
        }
        p.addChilds(childs)
    }
    return HashSet(deserializedInput)
}

fun deserialize(s: String): Program {
    val input = s.split(" ").filter { s -> s != "->" }
    return Program(input[0], input[1].trim('(', ')').toInt(), childNames = input.drop(2).map { c -> c.trim(',') })
}

fun main(args: Array<String>) {
    val programs = transformInput(loadInputFromFile("resources/input_7.txt"))
    val rootProgram = firstPart(programs)
    println(rootProgram.name)
    println(secondPart(rootProgram))

}

fun firstPart(programs: HashSet<Program>): Program {
    return programs.first { p -> p.parent == null }
}

fun secondPart(rootProgram : Program): Int {
    val un = rootProgram.findUnbalancedChild()
    val weightDifference = un.parent!!.childs.first {it != un}.totalWeight - un.totalWeight
    return un.weight + weightDifference
}



data class Program(val name: String, val weight: Int, val childNames: List<String>) {
    var parent: Program? = null
    val childs = mutableListOf<Program>()
    val totalWeight: Int by lazy { weight + childs.sumBy { p -> p.totalWeight } }
    override fun hashCode(): Int = name.hashCode()
    override fun equals(other: Any?) = other is Program && other.name == this.name

    fun addChild(program: Program) {
        childs.add(program)
        program.parent = this
    }

    fun addChilds(programs: Iterable<Program>) {
        programs.forEach(this::addChild)
    }

    fun findUnbalancedChild() : Program
    {
        var weight = 0
        for (child in childs) {
            if (weight == 0) {
                weight = child.totalWeight
                continue
            }
            if (weight - child.totalWeight != 0) {
                return child.findUnbalancedChild()
            }
        }
        return this
    }
}