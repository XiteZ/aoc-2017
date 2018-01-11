package three

import kotlin.math.sqrt

const val Input3 = 265149

fun main(args: Array<String>) {
    println(firstPart())
    println(secondPart())
}

fun firstPart(): Int {
    val sqrt = sqrt(Input3.toFloat())
    val maxMoves = maxMoves(sqrt)
    val endOfBlock = Math.pow(maxMoves.toDouble() + 1, 2.toDouble()).toInt()
    val difference = endOfBlock - Input3
    val (lowerBound, upperBound) = findBounds(Input3, endOfBlock, maxMoves)

    return (Input3.toFloat() - (lowerBound + upperBound) / 2 + maxMoves / 2).toInt()
}

fun maxMoves(sqrt: Float): Int {
    val sqrtFloor = kotlin.math.floor(sqrt).toInt()
    if (sqrtFloor - sqrt == 0F) {
        return sqrtFloor - 1
    }
    return if (sqrtFloor.rem(2) == 0) {
        sqrtFloor
    } else {
        sqrtFloor + 1
    }
}

fun findBounds(input: Int, endOfBlock: Int, maxMoves: Int): Pair<Int, Int> {
    var lowerBound = endOfBlock
    do {
        lowerBound -= maxMoves
    } while (lowerBound > input)
    val upperBound = lowerBound + maxMoves
    return Pair(lowerBound, upperBound)
}

fun secondPart(): Int {
    val grid = Grid()
    do {
        grid.move()
    } while (grid.activeGridCell.value <= Input3)
    return grid.activeGridCell.value
}

class Grid() {
    enum class Direction {
        RIGHT, UP, LEFT, BOTTOM
    }
    var activeGridCell = GridCell(Point(0, 0)).apply { value = 1; visited = true }
    val grid = hashMapOf(Pair(activeGridCell.coords, activeGridCell))
    var direction = Direction.RIGHT
    private var stepsForNextDirectionChange = 1
    private var steps = 0
    private var directionChangedCount = 0
    init {
        val points = activeGridCell.adjacentPoints
        grid.putAll(points.associate { p -> Pair(p, GridCell(p)) })
    }

init {
    for (point in activeGridCell.adjacentPoints)
    {
        grid[point]!!.value += activeGridCell.value
    }
}
    fun move() {
        if (steps == stepsForNextDirectionChange)
        {
            steps = 0
            changeDirection()
        }
        val newPoint = when (direction) {
            Direction.RIGHT -> activeGridCell.coords.copy(x = activeGridCell.coords.x + 1)
            Direction.UP -> activeGridCell.coords.copy(y = activeGridCell.coords.y - 1)
            Direction.LEFT -> activeGridCell.coords.copy(x = activeGridCell.coords.x - 1)
            Direction.BOTTOM -> activeGridCell.coords.copy(y = activeGridCell.coords.y + 1)
        }
        activeGridCell = grid[newPoint]!!
        activeGridCell.visited = true
        val adjacentPoints = activeGridCell.adjacentPoints
        grid.putAll((adjacentPoints - grid.keys).associate { p -> Pair(p, GridCell(p)) })
        for (point in adjacentPoints)
        {
            grid[point]!!.value += activeGridCell.value
        }
        if (directionChangedCount.rem(2) == 0 && directionChangedCount != 0) {
            stepsForNextDirectionChange++
            directionChangedCount = 0
        }
        steps++
    }

    private fun changeDirection() {
        direction = when (direction) {
            Direction.RIGHT -> Direction.UP
            Direction.UP -> Direction.LEFT
            Direction.LEFT -> Direction.BOTTOM
            Direction.BOTTOM -> Direction.RIGHT
        }
        directionChangedCount++
    }
}

class GridCell(val coords: Point) {
    var visited = false
    var value = 0
        set(value) {
            if (!visited) field = value
        }
    val adjacentPoints : Set<Point> = generateAdjacentPoints()

    private fun generateAdjacentPoints() = mutableSetOf<Point>().apply {
        add(Point(coords.x, coords.y - 1))
        add(Point(coords.x, coords.y + 1))
        add(Point(coords.x - 1, coords.y - 1))
        add(Point(coords.x - 1, coords.y))
        add(Point(coords.x - 1, coords.y + 1))
        add(Point(coords.x + 1, coords.y - 1))
        add(Point(coords.x + 1, coords.y))
        add(Point(coords.x + 1, coords.y + 1))
    }
}

data class Point(val x: Int, val y: Int)