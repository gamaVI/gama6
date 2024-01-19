package com.mygdx.gama6map.lang

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.EarClippingTriangulator
import com.badlogic.gdx.math.Vector2
import com.mygdx.gama6map.utils.Constants
import com.mygdx.gama6map.utils.MapRasterTiles
import com.mygdx.gama6map.utils.ZoomXY
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.InputStream
import kotlin.Nothing
import kotlin.math.*

const val ERROR_STATE = 0

const val EOF_SYMBOL = -1
const val SKIP_SYMBOL = 0
const val ID_SYMBOL = 1
const val FLOAT_SYMBOL = 2
const val COMMA_SYMBOL = 3
const val SEMI_SYMBOL = 4
const val STRING_SYMBOL = 5
const val LPAREN_SYMBOL = 8
const val RPAREN_SYMBOL = 9
const val BEGIN_SYMBOL = 10
const val END_SYMBOL = 11
const val LINE_SYMBOL = 12
const val MARKER_SYMBOL = 13
const val BEND_SYMBOL = 14
const val BUILDING_SYMBOL = 15
const val ROAD_SYMBOL = 16
const val LET_SYMBOL = 17
const val EQUAL_SYMBOL = 18
const val PLUS_SYMBOL = 19
const val MINUS_SYMBOL = 20

const val EOF = -1
const val NEWLINE = '\n'.code

interface Regex {
    fun states(): Set<Int>
    fun isNullable(): Boolean
    fun first(): Set<Int>
    fun last(): Set<Int>
    fun follow(): Set<Pair<Int, Int>>
    fun codes(): Map<Int, Int>
    fun symbols(symbol: Int): Map<Int, Int>
}

fun <A,B> product(xs: Set<A>, ys: Set<B>): Set<Pair<A, B>> =
    xs.flatMap { x -> ys.map {y -> x to y } }.toSet()

fun <A, B> Collection<Pair<A, B>>.toMultiMap(): Map<A, Set<B>> =
    this.groupBy({ it.first }, { it.second }).mapValues { it.value.toSet() }

infix fun <A> Set<A>.overlaps(other: Set<A>) =
    intersect(other).isNotEmpty()

object Null: Regex {

    override fun states(): Set<Int> = emptySet()

    override fun isNullable(): Boolean = true

    override fun first(): Set<Int> = emptySet()

    override fun last(): Set<Int> = emptySet()

    override fun follow(): Set<Pair<Int, Int>> = emptySet()

    override fun codes(): Map<Int, Int> = emptyMap()

    override fun symbols(symbol: Int): Map<Int, Int> = emptyMap()
}

class Chr(private val code: Int, private val state: Int): Regex {

    override fun states(): Set<Int> = setOf(state)

    override fun isNullable(): Boolean = false

    override fun first(): Set<Int> = setOf(state)

    override fun last(): Set<Int> = setOf(state)

    override fun follow(): Set<Pair<Int, Int>> = emptySet()

    override fun codes(): Map<Int, Int> =
        mapOf(state to code)

    override fun symbols(symbol: Int): Map<Int, Int> =
        mapOf(state to symbol)
}

class Concat(private val left: Regex, private val right: Regex, private val nullable: Boolean): Regex {
    override fun states(): Set<Int> =
        left.states() + right.states()

    override fun isNullable(): Boolean = nullable

    override fun first(): Set<Int> =
        left.first() + if (left.isNullable()) right.first() else emptySet()

    override fun last(): Set<Int> =
        right.last() + if (right.isNullable()) left.last() else emptySet()

    override fun follow(): Set<Pair<Int, Int>> =
        left.follow() + right.follow() +  product(left.last(), right.first())

    override fun codes(): Map<Int, Int> =
        left.codes() + right.codes()

    override fun symbols(symbol: Int): Map<Int, Int> =
        left.symbols(symbol) + right.symbols(symbol)
}

class Union(private val left: Regex, private val right: Regex, private val nullable: Boolean): Regex {
    override fun states(): Set<Int> =
        left.states() + right.states()

    override fun isNullable(): Boolean = nullable

    override fun first(): Set<Int> =
        left.first() + right.first()

    override fun last(): Set<Int> =
        left.last() + right.last()

    override fun follow(): Set<Pair<Int, Int>> =
        left.follow() + right.follow()

    override fun codes(): Map<Int, Int> =
        left.codes() + right.codes()

    override fun symbols(symbol: Int): Map<Int, Int> =
        left.symbols(symbol) + right.symbols(symbol)
}

class Plus(private val inner: Regex, private val nullable: Boolean): Regex {
    override fun states(): Set<Int> =
        inner.states()

    override fun isNullable(): Boolean =
        nullable

    override fun first(): Set<Int> =
        inner.first()

    override fun last(): Set<Int> =
        inner.last()

    override fun follow() =
        inner.follow() + product(inner.last(), inner.first())

    override fun codes(): Map<Int, Int> =
        inner.codes()

    override fun symbols(symbol: Int): Map<Int, Int> =
        inner.symbols(symbol)
}

class Label(private val symbol: Int, private val inner: Regex): Regex {
    override fun states(): Set<Int> =
        inner.states()

    override fun isNullable(): Boolean =
        inner.isNullable()

    override fun first(): Set<Int> =
        inner.first()

    override fun last(): Set<Int> =
        inner.last()

    override fun follow() =
        inner.follow()

    override fun codes(): Map<Int, Int> =
        inner.codes()

    override fun symbols(symbol: Int): Map<Int, Int> =
        inner.symbols(this.symbol)
}

operator fun Regex.plus(other: Regex) =
    Union(this, other, this.isNullable() || other.isNullable())

operator fun Regex.times(other: Regex) =
    Concat(this, other, this.isNullable() && other.isNullable())

operator fun Regex.unaryPlus() =
    Plus(this, this.isNullable())

class ChrBuilder {
    private var counter = 0

    operator fun invoke(code: Int) =
        Chr(code, counter++)

    operator fun invoke(char: Char) =
        Chr(char.code, counter++)
}

fun range(chr: ChrBuilder, chars: Iterable<Char>) =
    chars.map { chr(it) }.reduce(Regex::plus)

data class DFA(val states: Set<Int>, val transitions: Map<Int, Map<Int, Int>>, val startState: Int, val finalStates: Set<Int>, val symbols: Map<Int, Int>) {
    fun next(state: Int, code: Int): Int {
        assert(states.contains(state))
        return transitions[state]!![code] ?: ERROR_STATE
    }

    fun symbol(state: Int): Int {
        assert(states.contains(state))
        return symbols[state]!!
    }
}

data class RFA(val states: Set<Int>, val first: Set<Int>, val last: Set<Int> , val follow: Map<Int, Set<Int>>, val codes: Map<Int, Int>, val symbols: Map<Int, Int>) {
    companion object {
        fun ofRegex(re: Regex): RFA =
            RFA(re.states(), re.first(), re.last(), re.follow().toMultiMap(), re.codes(), re.symbols(SKIP_SYMBOL))
    }

    fun subset(): DFA {
        var counter = 0
        val renumber = mutableMapOf<Set<Int>, Int>()
        val transitions = mutableMapOf<Int, Map<Int, Int>>()

        fun cycle(states: Set<Int>): Int =
            renumber[states] ?: run {
                val state = counter++
                renumber[states] = state
                transitions[state] = states
                    .mapNotNull(this.follow::get)
                    .fold(emptySet(), Set<Int>::union)
                    .groupBy(this.codes::getValue)
                    .mapValues { (_, next) -> cycle(next.toSet()) }
                state
            }

        val startState = cycle(first)
        val states = renumber.values.toSet()
        val finalStates = renumber.filter { (states, _) -> states overlaps last }.values.toSet()
        val symbols = renumber.map { (states, state) -> state to states.maxOf(this.symbols::getValue) }.toMap()
        return DFA(states, transitions, startState, finalStates, symbols)
    }
}

fun buildAutomaton(chr: ChrBuilder, res: Map<Int, Regex>) =
    RFA.ofRegex(chr(0) * res.map { (symbol, re) -> Label(symbol, re) }.reduce(Regex::plus)).subset()

val AUTOMATON = run {
    val chr = ChrBuilder()
    buildAutomaton(chr, mapOf(
        EOF_SYMBOL to chr(EOF),
        SKIP_SYMBOL to +(chr(' ') + chr(NEWLINE)),
        ID_SYMBOL to + range(chr, ('a'..'z') + ('A'..'Z')),
        //ID_SYMBOL to + (range(chr, ('a'..'z') + ('A'..'Z')) * range(chr,('a'..'z') + ('A'..'Z')+('0'..'9'))),
        FLOAT_SYMBOL to +range(chr, '0'..'9') * (chr('.') * +range(chr, '0'..'9') + Null),
        COMMA_SYMBOL to chr(','),
        SEMI_SYMBOL to chr(';'),
        STRING_SYMBOL to chr('"') * (+range(chr, (' ' .. '~')  - '"') + Null) * chr('"'),
        LPAREN_SYMBOL to chr('('),
        RPAREN_SYMBOL to chr(')'),
        BEGIN_SYMBOL to chr('b') * chr('e') * chr('g') * chr('i') * chr('n'),
        END_SYMBOL to chr('e') * chr('n') * chr('d'),
        LINE_SYMBOL to chr('l') * chr('i') * chr('n') * chr('e'),
        MARKER_SYMBOL to chr('m') * chr('a') * chr('r') * chr('k') * chr('e') * chr('r'),
        BEND_SYMBOL to chr('b') * chr('e') * chr('n') * chr('d'),
        BUILDING_SYMBOL to chr('b') * chr('u') * chr('i') * chr('l') * chr('d') * chr('i') * chr('n') * chr('g'),
        ROAD_SYMBOL to chr('r') * chr('o') * chr('a') * chr('d'),
        LET_SYMBOL to chr('l') * chr('e') * chr('t'),
        EQUAL_SYMBOL to chr('='),
        PLUS_SYMBOL to chr('+'),
        MINUS_SYMBOL to chr('-')
    ))
}

data class Token(val symbol: Int, val lexeme: String, val startRow: Int, val startColumn: Int)

class Scanner(private val automaton: DFA, private val stream: InputStream) {
    private var last: Int? = null
    private var row = 1
    private var column = 1

    private fun updatePosition(code: Int) {
        if (code == NEWLINE) {
            row += 1
            column = 1
        } else {
            column += 1
        }
    }

    fun getToken(): Token {
        val startRow = row
        val startColumn = column
        val buffer = mutableListOf<Char>()

        var code = last ?: stream.read()
        var state = automaton.startState
        while (true) {
            val nextState = automaton.next(state, code)
            if (nextState == ERROR_STATE) break // Longest match

            state = nextState
            updatePosition(code)
            buffer.add(code.toChar())
            code = stream.read()
        }
        last = code // The code following the current lexeme is the first code of the next lexeme

        if (automaton.finalStates.contains(state)) {
            val symbol = automaton.symbol(state)
            return if (symbol == SKIP_SYMBOL) {
                getToken()
            } else {
                val lexeme = String(buffer.toCharArray())
                Token(symbol, lexeme, startRow, startColumn)
            }
        } else {
            throw Error("Invalid pattern at ${row}:${column}")
        }
    }
}

fun name(symbol: Int) =
    when (symbol) {
        ID_SYMBOL -> "id"
        FLOAT_SYMBOL -> "float"
        COMMA_SYMBOL -> "comma"
        SEMI_SYMBOL -> "semi"
        STRING_SYMBOL -> "string"
        LPAREN_SYMBOL -> "lparen"
        RPAREN_SYMBOL -> "rparen"
        BEGIN_SYMBOL -> "begin"
        END_SYMBOL -> "end"
        LINE_SYMBOL -> "line"
        MARKER_SYMBOL -> "marker"
        BEND_SYMBOL -> "bend"
        BUILDING_SYMBOL -> "building"
        ROAD_SYMBOL -> "road"
        else -> throw Error("Invalid symbol")
    }

fun printTokens(scanner: Scanner) {
    val token = scanner.getToken()
    if (token.symbol != EOF_SYMBOL) {
        print("${name(token.symbol)}(\"${token.lexeme}\") ")
        printTokens(scanner)
    }
}

class ParseException(symbol: Int, row: Int, column: Int) : Exception("PARSE ERROR (${name(symbol)}) at $row:$column")

class Parser(private val scanner: Scanner) {
    private var last: Token? = null

    private fun panic(): Nothing =
        last?.let { throw ParseException(it.symbol, it.startRow, it.startColumn) } ?: error("cannot happen")

    fun parse(): City {
        last = scanner.getToken()
        val result = City(parseBlock())
        return when(last?.symbol) {
            EOF_SYMBOL -> result
            else -> panic()
        }
    }

    private fun parseBlock(): Block =
        when (last?.symbol) {
            BUILDING_SYMBOL -> {
                parseTerminal(BUILDING_SYMBOL)
                val name = parseTerminal(STRING_SYMBOL).trim('"')
                parseTerminal(BEGIN_SYMBOL)
                Building(name, parseStmt(), parseBlock())
            }
            ROAD_SYMBOL -> {
                parseTerminal(ROAD_SYMBOL)
                val name = parseTerminal(STRING_SYMBOL).trim('"')
                parseTerminal(BEGIN_SYMBOL)
                Road(name, parseStmt(), parseBlock())
            }
            MARKER_SYMBOL -> {
                parseTerminal(MARKER_SYMBOL)
                Marker(parseTerminal(STRING_SYMBOL).trim('"'), parseExpr(), parseBlock())
            }
            EOF_SYMBOL ->
                Final()
            else -> panic()
        }

    private fun parseStmt(): Stmt =
        when(last?.symbol) {
            LET_SYMBOL -> {
                parseTerminal(LET_SYMBOL)
                val name = parseTerminal(ID_SYMBOL)
                parseTerminal(EQUAL_SYMBOL)
                val expr = parseExpr()
                parseTerminal(SEMI_SYMBOL)
                Let(name, expr, parseStmt())
            }
            LINE_SYMBOL -> {
                parseTerminal(LINE_SYMBOL)
                parseTerminal(LPAREN_SYMBOL)
                val from = parseExpr()
                parseTerminal(COMMA_SYMBOL)
                val to = parseExpr()
                parseTerminal(RPAREN_SYMBOL)
                parseTerminal(SEMI_SYMBOL)
                Line(from, to, parseStmt())
            }
            BEND_SYMBOL -> {
                parseTerminal(BEND_SYMBOL)
                parseTerminal(LPAREN_SYMBOL)
                val from = parseExpr()
                parseTerminal(COMMA_SYMBOL)
                val to = parseExpr()
                parseTerminal(COMMA_SYMBOL)
                val angle = parseExpr()
                parseTerminal(RPAREN_SYMBOL)
                parseTerminal(SEMI_SYMBOL)
                Bend(from, to, angle, parseStmt())
            }
            END_SYMBOL -> {
                parseTerminal(END_SYMBOL)
                End()
            }
            else -> panic()
        }

    private fun parseExpr(): Expr = parseAdditive()


    private fun parseAdditive(): Expr =
        when(last?.symbol) {
            FLOAT_SYMBOL, MINUS_SYMBOL, ID_SYMBOL, LPAREN_SYMBOL ->
                parseAdditiveTail(parseUnary())
            else -> panic()
        }

    private fun parseAdditiveTail(accumulator: Expr): Expr =
        when(last?.symbol) {
            PLUS_SYMBOL -> {
                parseTerminal(PLUS_SYMBOL)
                parseAdditiveTail(Add(accumulator, parseUnary()))
            }
            MINUS_SYMBOL -> {
                parseTerminal(MINUS_SYMBOL)
                parseAdditiveTail(Sub(accumulator, parseUnary()))
            }
            RPAREN_SYMBOL, COMMA_SYMBOL, SEMI_SYMBOL, EOF_SYMBOL, MARKER_SYMBOL, ROAD_SYMBOL, BUILDING_SYMBOL ->
                accumulator
            else -> panic()
        }

    private fun parseUnary(): Expr =
        when(last?.symbol) {
            MINUS_SYMBOL -> {
                parseTerminal(MINUS_SYMBOL)
                UnaryMinus(parsePrimary())
            }
            FLOAT_SYMBOL, ID_SYMBOL, LPAREN_SYMBOL ->
                parsePrimary()
            else -> panic()
        }

    private fun parsePrimary(): Expr =
        when(last?.symbol) {
            FLOAT_SYMBOL -> {
                Num(parseTerminal(FLOAT_SYMBOL).toDouble())
            }
            ID_SYMBOL -> {
                Var(parseTerminal(ID_SYMBOL))
            }
            LPAREN_SYMBOL -> {
                parseTerminal(LPAREN_SYMBOL)
                parsePrimaryTail(parseExpr())
            }
            else -> panic()
        }

    private fun parsePrimaryTail(first: Expr): Expr =
        when(last?.symbol) {
            COMMA_SYMBOL -> {
                parseTerminal(COMMA_SYMBOL)
                val second = parseExpr()
                parseTerminal(RPAREN_SYMBOL)
                Point(first, second)
            }
            RPAREN_SYMBOL -> {
                parseTerminal(RPAREN_SYMBOL)
                first
            }
            else -> panic()
        }

    private fun parseTerminal(symbol: Int): String =
        if (last?.symbol == symbol) {
            val lexeme = last!!.lexeme
            last = scanner.getToken()
            lexeme
        } else {
            panic()
        }
}

object TypeException: Exception("TYPE ERROR")

// RRI
data class Context(val shapeRenderer: ShapeRenderer, val camera: Camera, val beginTile: ZoomXY)

// RRI
data class Coordinates(val lat: Double, val lon: Double) {

    fun toGeoJSON(): JSONArray =
        JSONArray(listOf(lon, lat))

    fun toPixelPosition(beginTile: ZoomXY): Vector2 =
        MapRasterTiles.getPixelPosition(lat, lon, MapRasterTiles.TILE_SIZE, Constants.ZOOM, beginTile.x, beginTile.y, Constants.MAP_HEIGHT)

    operator fun plus(other: Coordinates) =
        Coordinates(lat + other.lat, lon + other.lon)

    operator fun times(s: Double) =
        Coordinates(s * lat, s * lon)

    fun angle(other: Coordinates) =
        atan2(other.lon - lon, other.lat - lat)

    fun dist(other: Coordinates) =
        hypot(other.lat - lat, other.lon - lon)

    fun offset(s: Double, angle: Double) =
        this + Coordinates(cos(angle), sin(angle)) * s
}

// RRI
fun List<Coordinates>.toDeconstructedPixelPositions(ctx: Context) =
    this.map { it.toPixelPosition(ctx.beginTile)}
        .flatMap { listOf(it.x, it.y) }
        .toFloatArray()

// RRI
class Bezier(private val p0: Coordinates, private val p1: Coordinates, private val p2: Coordinates, private val p3: Coordinates) {

    fun at(t: Double) =
        p0 * (1.0 - t).pow(3.0) + p1 * 3.0 * (1.0 - t).pow(2.0) * t + p2 * 3.0 * (1.0 - t) * t.pow(2.0) + p3 * t.pow(3.0)

    fun toPoints(segmentsCount: Int): List<Coordinates> {
        val ps = mutableListOf<Coordinates>()
        for (i in 0 .. segmentsCount) {
            val t = i / segmentsCount.toDouble()
            ps.add(at(t))
        }
        return ps
    }

    fun approxLength(): Double {
        val midpoint = at(0.5)
        return p0.dist(midpoint) + midpoint.dist(p3)
    }

    fun resolutionToSegmentsCount(resolution: Double) =
        (resolution * approxLength()).coerceAtLeast(2.0).toInt()

    companion object {
        fun bend(t0: Coordinates, t1: Coordinates, relativeAngle: Double): Bezier {
            val relativeAngle = Math.toRadians(relativeAngle)
            val oppositeRelativeAngle = PI - relativeAngle

            val angle = t0.angle(t1)
            val dist = t0.dist(t1)
            val constant = (4 / 3) * tan(PI / 8)

            val c0 = t0.offset(constant * dist, angle + relativeAngle)
            val c1 = t1.offset(constant * dist, angle + oppositeRelativeAngle)

            return Bezier(t0, c0, c1, t1)
        }
    }
}

interface Expr {
    fun evalPartial(env: Map<String, Expr>): Expr

    fun add(other: Expr): Expr =
        throw TypeException

    fun addNum(first: Num): Expr =
        throw TypeException

    fun addPoint(first: Point): Expr =
        throw TypeException

    fun sub(other: Expr): Expr =
        throw TypeException

    fun subNum(first: Num): Expr =
        throw TypeException

    fun subPoint(first: Point): Expr =
        throw TypeException

    fun unaryMinus(): Expr =
        throw TypeException

    fun toCoordinate(): Double =
        throw TypeException

    fun toCoordinates(): Coordinates =
        throw TypeException
}

class UnaryMinus(private val inner: Expr): Expr {
    override fun toString(): String =
        "-($inner)"

    override fun evalPartial(env: Map<String, Expr>): Expr =
        inner.unaryMinus()
}

class Add(private val left: Expr, private val right: Expr): Expr {
    override fun toString(): String =
        "($left + $right)"

    override fun evalPartial(env: Map<String, Expr>): Expr =
        left.evalPartial(env).add(right.evalPartial(env))
}

class Sub(private val left: Expr, private val right: Expr): Expr {
    override fun toString(): String =
        "($left - $right)"

    override fun evalPartial(env: Map<String, Expr>): Expr =
        left.evalPartial(env).sub(right.evalPartial(env))
}

class Var(private val name: String): Expr {
    override fun toString(): String =
        name

    override fun evalPartial(env: Map<String, Expr>): Expr =
        env[name]!!
}

class Point(private val left: Expr, private val right: Expr): Expr {
    override fun toString(): String =
        "($left, $right)"

    override fun evalPartial(env: Map<String, Expr>): Expr =
        Point(left.evalPartial(env), right.evalPartial(env))

    override fun add(other: Expr): Expr =
        other.addPoint(this)

    override fun addPoint(first: Point): Expr =
        Point(first.left.add(this.left), first.right.add(this.right))

    override fun sub(other: Expr): Expr =
        other.subPoint(this)

    override fun subPoint(first: Point): Expr =
        Point(first.left.sub(this.left), first.right.sub(this.right))

    override fun unaryMinus(): Expr =
        Point(left.unaryMinus(), right.unaryMinus())

    override fun toCoordinates(): Coordinates =
        Coordinates(left.toCoordinate(), right.toCoordinate())
}

class Num(private val value: Double): Expr {
    override fun unaryMinus(): Expr =
        Num(-value)

    override fun toString(): String =
        value.toString()

    override fun evalPartial(env: Map<String, Expr>): Expr =
        this

    override fun add(other: Expr): Expr =
        other.addNum(this)

    override fun addNum(first: Num): Expr =
        Num(first.value + this.value)

    override fun sub(other: Expr): Expr =
        other.subNum(this)

    override fun subNum(first: Num): Expr =
        Num(first.value - this.value)

    override fun toCoordinate(): Double =
        value
}

interface Stmt {
    fun evalPartial(env: Map<String, Expr>): Stmt

    fun render(ctx: Context): Unit =
        throw TypeException

    fun outline(): List<List<Coordinates>> =
        throw TypeException
}

class Let(private val name: String, private var expr: Expr, private var next: Stmt): Stmt {

    override fun toString(): String =
        "let $name = $expr;\n$next"

    override fun evalPartial(env: Map<String, Expr>): Stmt =
        next.evalPartial(env + (name to expr.evalPartial(env)))
}

class Line(private val from: Expr, private val to: Expr, val next: Stmt): Stmt {
    override fun toString(): String =
        "line($from, $to);\n$next"

    override fun evalPartial(env: Map<String, Expr>): Stmt =
        Line(from.evalPartial(env), to.evalPartial(env), next.evalPartial(env))

    // RRI
    override fun render(ctx: Context): Unit {
        ctx.shapeRenderer.line(from.toCoordinates().toPixelPosition(ctx.beginTile), to.toCoordinates().toPixelPosition(ctx.beginTile))
        next.render(ctx)
    }

    override fun outline(): List<List<Coordinates>> =
        listOf(listOf(from.toCoordinates(), to.toCoordinates())) + next.outline()
}

class Bend(private val from: Expr, private val to: Expr, private val angle: Expr, private val next: Stmt): Stmt {
    override fun toString(): String =
        "bend($from, $to);\n$next"

    override fun evalPartial(env: Map<String, Expr>): Stmt =
        Bend(from.evalPartial(env), to.evalPartial(env), angle.evalPartial(env), next.evalPartial(env))

    // RRI
    private fun toPoints(): List<Coordinates> {
        val b = Bezier.bend(from.toCoordinates(), to.toCoordinates(), angle.toCoordinate())
        return b.toPoints(b.resolutionToSegmentsCount(500.0))
    }

    // RRI
    override fun outline(): List<List<Coordinates>> =
        listOf(toPoints()) + next.outline()

    // RRI
    override fun render(ctx: Context): Unit {
        val vertices = toPoints()
            .toDeconstructedPixelPositions(ctx)
        ctx.shapeRenderer.polyline(vertices)
        next.render(ctx)
    }

}

class End: Stmt {
    override fun toString(): String =
        "end"

    override fun evalPartial(env: Map<String, Expr>): Stmt =
        this

    // RRI
    override fun render(ctx: Context) {}

    // RRI
    override fun outline(): List<List<Coordinates>> = emptyList()
}


interface Block {
    fun evalPartial(env: Map<String, Expr>): Block

    fun toGeoJSON(): List<JSONObject> =
        throw TypeException

    // RRI
    fun render(ctx: Context): Unit =
        throw TypeException
}

object PolygonException: Exception("POLYGON ERROR")

// RRI
fun List<List<Coordinates>>.flatten(): List<Coordinates> {
    val segment = first() // It doesn't matter where we start
    return this.flattenHelper(segment.last())
}

// RRI
fun List<List<Coordinates>>.flattenHelper(active: Coordinates): List<Coordinates> =
    if (this.isEmpty())
        emptyList()
    else {
        val (connected, other) = this.partition { segment -> segment.first() == active }
        if (connected.size != 1) throw PolygonException
        val segment = connected.first()
        segment.drop(1) + other.flattenHelper(segment.last())
    }

// RRI
fun List<Coordinates>.close(): List<Coordinates> = // Close the loop
    this + listOf(this.first())

class Building(private val name: String, private val stmt: Stmt, private val next: Block): Block {
    override fun toString(): String =
        "building \"$name\" begin\n$stmt$next"

    override fun evalPartial(env: Map<String, Expr>): Block =
        Building(name, stmt.evalPartial(env), next.evalPartial(env))

    // RRI
    override fun render(ctx: Context) {
        ctx.shapeRenderer.projectionMatrix = ctx.camera.combined
        ctx.shapeRenderer.color = Color.GREEN
        ctx.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        val vertices = stmt.outline()
            .flatten()
            .toDeconstructedPixelPositions(ctx)

        val triangulator = EarClippingTriangulator()
        val triangles = triangulator.computeTriangles(vertices)
        for (i in 0 until triangles.size step 3) {
            val t0 = triangles[i + 0].toInt()
            val t1 = triangles[i + 1].toInt()
            val t2 = triangles[i + 2].toInt()
            val (x0, y0) = vertices[2 * t0] to vertices[2 * t0 + 1]
            val (x1, y1) = vertices[2 * t1] to vertices[2 * t1 + 1]
            val (x2, y2) = vertices[2 * t2] to vertices[2 * t2 + 1]
            ctx.shapeRenderer.triangle(x0, y0, x1, y1, x2, y2)
        }
        ctx.shapeRenderer.end()
        next.render(ctx)
    }

    override fun toGeoJSON(): List<JSONObject> =
        listOf(JSONObject()
            .put("type", "Feature")
            .put("geometry", JSONObject()
                .put("type", "Polygon")
                .put("coordinates", JSONArray(listOf(JSONArray(stmt.outline().flatten().close().map { it.toGeoJSON() })))))
            .put("properties", JSONObject()
                .put("name", name))) + next.toGeoJSON()
}

class Road(private val name: String, private val stmt: Stmt, private val next: Block): Block {
    override fun toString(): String =
        "road \"$name\" begin\n$stmt$next"

    override fun evalPartial(env: Map<String, Expr>): Block =
        Road(name, stmt.evalPartial(env), next.evalPartial(env))

    // RRI
    override fun render(ctx: Context) {
        ctx.shapeRenderer.projectionMatrix = ctx.camera.combined
        ctx.shapeRenderer.color = Color.BLUE
        ctx.shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        stmt.render(ctx)
        ctx.shapeRenderer.end()
        next.render(ctx)
    }

    override fun toGeoJSON(): List<JSONObject> =
        listOf(JSONObject()
            .put("type", "Feature")
            .put("geometry", JSONObject()
                .put("type", "MultiLineString")
                .put("coordinates", JSONArray(stmt.outline().map { segment -> JSONArray(segment.map { it.toGeoJSON() }) })))
            .put("properties", JSONObject()
                .put("name", name))) + next.toGeoJSON()
}

class Marker(private val name: String, private val at: Expr, private val next: Block): Block {
    override fun toString(): String =
        "marker \"$name\" $at\n$next"

    override fun evalPartial(env: Map<String, Expr>): Block =
        Marker(name, at.evalPartial(env), next.evalPartial(env))

    // RRI
    override fun render(ctx: Context) {
        val position = at.toCoordinates().toPixelPosition(ctx.beginTile)
        ctx.shapeRenderer.projectionMatrix = ctx.camera.combined
        ctx.shapeRenderer.color = Color.RED
        ctx.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        ctx.shapeRenderer.circle(position.x, position.y, 10.0f)
        ctx.shapeRenderer.end()
        next.render(ctx)
    }

    override fun toGeoJSON(): List<JSONObject> =
        listOf(JSONObject()
            .put("type", "Feature")
            .put("geometry", JSONObject()
                .put("type", "Point")
                .put("coordinates", JSONArray(at.toCoordinates().toGeoJSON())))
            .put("properties", JSONObject()
                .put("name", name))) + next.toGeoJSON()
}

class Final: Block {
    override fun evalPartial(env: Map<String, Expr>): Block =
        this

    override fun toGeoJSON(): List<JSONObject> = emptyList()

    // RRI
    override fun render(ctx: Context) {}
}

class City(private val block: Block) {

    fun evalPartial(env: Map<String, Expr>): City =
        City(block.evalPartial(env))

    fun toGeoJSON(): JSONObject =
        JSONObject()
            .put("type", "FeatureCollection")
            .put("features", JSONArray(block.toGeoJSON()))

    // RRI
    fun render(ctx: Context): Unit =
        block.render(ctx)
}

fun load(stream: InputStream) =
    Parser(Scanner(AUTOMATON, stream)).parse().evalPartial(emptyMap())

// RRI
class Renderer {
    fun render(stream: InputStream, ctx: Context) =
        load(stream).render(ctx)
}

fun main() {
    println(load(File("program.txt").inputStream()).toGeoJSON().toString(2))
}
