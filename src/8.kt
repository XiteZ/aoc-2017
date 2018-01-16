package eight

import java.io.File

fun loadInputFromFile(filePath: String): List<String> {
    return File(filePath).readLines()
}

fun transformInput(input: List<String>): List<Instruction> {
    return input.map(::deserializeInstruction)
}

fun deserializeInstruction(serializedInstruction: String): Instruction {
    val splittedSerializedInstruction = serializedInstruction.split(" ")
    val condition = Condition(splittedSerializedInstruction[4], splittedSerializedInstruction[5], Integer.parseInt(splittedSerializedInstruction[6]))
    return Instruction(splittedSerializedInstruction[0], normalizeInstructionValue(splittedSerializedInstruction[1], Integer.parseInt(splittedSerializedInstruction[2])), condition)
}

fun normalizeInstructionValue(operation: String, value: Int): Int {
    return when (operation) {
        "dec" -> -value
        else -> value
    }
}

fun main(args: Array<String>) {
    val instructions = transformInput(loadInputFromFile("resources/input_8.txt"))

    println(firstPart(instructions, createRegisters(instructions)))
    println(secondPart(instructions, createRegisters(instructions)))
}

fun createRegisters(instructions: List<Instruction>) : HashMap<String, Int>{
    val registersList = instructions.map { it.registerName }.distinct()
    return registersList.associateTo(hashMapOf(), { Pair(it, 0) })
}

fun firstPart(instructions: List<Instruction>, registers: HashMap<String, Int>): Int {
    for (instruction in instructions) {
        registers[instruction.registerName] = registers[instruction.registerName]!! + processInstruction(instruction, registers)
    }
    return registers.maxBy { it.value }!!.value
}

fun processInstruction(inst: Instruction, registers: HashMap<String, Int>): Int {
    val registerValue = registers[inst.condition.lhs]!!
    return when (inst.condition.operator) {
        ">" -> if (registerValue > inst.condition.rhs) inst.value else 0
        "<" -> if (registerValue < inst.condition.rhs) inst.value else 0
        "<=" -> if (registerValue <= inst.condition.rhs) inst.value else 0
        ">=" -> if (registerValue >= inst.condition.rhs) inst.value else 0
        "==" -> if (registerValue == inst.condition.rhs) inst.value else 0
        else -> if (registerValue != inst.condition.rhs) inst.value else 0
    }
}

fun secondPart(instructions: List<Instruction>, registers: HashMap<String, Int>): Int {
    var maxRegisterValue = 0
    for (instruction in instructions) {
        val newRegisterValue = registers[instruction.registerName]!! + processInstruction(instruction, registers)
        if (newRegisterValue > maxRegisterValue)
        {
            maxRegisterValue = newRegisterValue
            println(maxRegisterValue)
        }
        registers[instruction.registerName] = newRegisterValue
    }
    return maxRegisterValue
}


data class Instruction(val registerName: String, val value: Int, val condition: Condition)

class Condition(val lhs: String, val operator: String, val rhs: Int)