
package task

import jdk.dynalink.linker.ConversionComparator
import java.io.InputStream
import java.util.*
import java.io.File
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin


const val EOF = -1
const val NEWLINE = '\n'.code
const val ERROR_STATE = 0
const val EOF_SYMBOL = -1
const val SKIP_SYMBOL = 0
const val program_SYMBOL = 1
const val statement_SYMBOL = 2
const val city_SYMBOL = 3
const val let_SYMBOL = 4
const val for_SYMBOL = 5
const val if_SYMBOL = 6
const val list_SYMBOL = 7
const val city_element_SYMBOL = 8
const val road_SYMBOL = 9
const val building_SYMBOL = 10
const val park_SYMBOL = 11
const val river_SYMBOL = 12
const val restaurant_SYMBOL = 13
const val school_SYMBOL = 14
const val townhall_SYMBOL = 15
const val church_SYMBOL = 16
const val stadium_SYMBOL = 17
const val lines_SYMBOL = 18
const val point_SYMBOL = 19
const val expression_SYMBOL = 20
const val condition_SYMBOL = 21
const val arc_SYMBOL = 22
const val boolean_SYMBOL = 23
const val point_list_SYMBOL = 24
const val point_list_tail_SYMBOL = 25
const val number_SYMBOL = 26
const val string_SYMBOL = 27
const val l_w_paren_SYMBOL = 28
const val r_w_paren_SYMBOL = 29
const val r_paren_SYMBOL = 30
const val l_paren_SYMBOL = 31
const val semicolon_SYMBOL = 32
const val line_SYMBOL = 33
const val bend_SYMBOL = 34
const val box_SYMBOL = 35
const val circ_SYMBOL = 36
const val poly_SYMBOL = 37
const val equal_SYMBOL = 38 //=
const val in_SYMBOL = 39
const val dot_SYMBOL = 40
const val l_S_SYMBOL = 41
const val r_S_SYMBOL = 42
const val comma_SYMBOL = 43 //,
const val true_SYMBOL = 44
const val false_SYMBOL = 45
const val quotaitons_SYMBOL = 46
const val underscore_SYMBOL = 47
const val superequal_SYMBOL = 48//==
const val greater_SYMBOL = 49//>
const val less_SYMBOL = 50//<




interface DFA {
    val states: Set<Int>
    val alphabet: IntRange
    fun next(state: Int, code: Int): Int
    fun symbol(state: Int): Int
    val startState: Int
    val finalStates: Set<Int>
}

object Automaton : DFA {
    override val startState = 1
    override val states = setOf(-2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 ,24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56,   57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82,  83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95 ,96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111)
    override val finalStates = setOf(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 16, 20, 22, 29, 33,35,40, 45, 47, 48, 107, 52, 54, 56, 60, 63, 73, 77, 80, 86, 92, 96, 103, 104, 105, 108)
    override val alphabet = 0..255

    private val numberOfStates = states.max() + 1  // plus the ERROR_STATE
    //    private val numberOfStates
    private val numberOfCodes = alphabet.max() + 1 // plus the EOF...
    private val transitions = Array(numberOfStates) { IntArray(numberOfCodes) }
    private val values = Array(numberOfStates) { SKIP_SYMBOL }

    private fun setTransition(from: Int, chr: Char, to: Int) {
        transitions[from][chr.code + 1] = to // + 1 because EOF is -1 and the array starts at 0
    }

    private fun setTransition(from: Int, code: Int, to: Int) {
        transitions[from][code + 1] = to
    }
    private fun setTransition(from: Int, charRange: CharRange, to: Int) {
        for (chr in charRange) {
            transitions[from][chr.code + 1] = to
        }
    }
    private fun setTransitionWithout(from: Int, chr: Char, to: Int) {
        ('a'..'z').filter { it != chr }.forEach {
            setTransition(from, it, to)
        }
    }
    private fun setTransitionWithout(from: Int, charsToExclude: List<Char>, to: Int) {
        ('a'..'z').filter { it !in charsToExclude }.forEach {
            setTransition(from, it, to)
        }
    }


    private fun setSymbol(state: Int, symbol: Int) {
        values[state] = symbol
    }

    override fun next(state: Int, code: Int): Int {
        assert(states.contains(state))
        assert(alphabet.contains(code))
        return transitions[state][code + 1]
    }

    override fun symbol(state: Int): Int {
        assert(states.contains(state))
        return values[state]
    }

    init {
        //vse tranzicije in setSymbol
        //stevilke
        setTransition(1, '0'..'9', 2)
        setTransition(2, '0'..'9', 2)
        //klasicni simboli (od 3 do 12)

        setTransition(1, '(', 3)
        setTransition(1, ')', 4)
        setTransition(1, '}', 5)
        setTransition(1, '{', 6)
        setTransition(1, ']', 7)
        setTransition(1, '[', 8)
        setTransition(1, '=', 9)
        setTransition(9, '=', 109)
        setTransition(1, ';', 10)
        setTransition(1, '.', 11)
        setTransition(1, ',', 12)
        setTransition(1, '\"', 108)
        setTransition(1, '>', 110)
        setTransition(1, '<', 111)


        //string iz 1 do 14
        setTransition(1, 'd'..'e', 14)
        setTransition(1, 'g'..'h', 14)
        setTransition(1, 'j'..'k', 14)
        setTransition(1, 'm'..'o', 14)
        setTransition(1, 'q', 14)
        setTransition(1, 'u'..'z', 14)
        setTransition(14, 'a'..'z', 14)
        setTransition(14, '_', 14)
        //arc
        setTransition(1, 'a', 13)
        setTransition(13, 'r', 15)
        setTransition(15, 'c', 16)

        setTransition(13, 'a'..'q', 14)
        setTransition(13, 's'..'z', 14)
        setTransition(15, 'a'..'b', 14)
        setTransition(15, 'd'..'z', 14)
        setTransition(16, 'a'..'z', 14)
        //bend
        setTransition(1, 'b', 17)//box in building
        setTransition(17, 'e', 18)
        setTransition(18, 'n', 19)
        setTransition(19, 'd', 20)

        setTransition(17, 'a', 14)
        setTransition(17, 'c'..'d', 14)
        setTransition(17, 'f'..'n', 14)
        setTransition(17, 'p'..'t', 14)
        setTransition(17, 'v'..'z', 14)

        setTransitionWithout(18, 'n', 14)
        setTransitionWithout(19, 'd', 14)
        setTransition(20, 'a'..'z', 14)

        //box
        setTransition(17, 'o', 21)
        setTransition(21, 'x', 22)

        setTransitionWithout(21, 'x', 14)
        setTransition(22, 'a'..'z', 14)
        //building
        setTransition(17, 'u', 23)
        setTransition(23, 'i', 24)
        setTransition(24, 'l', 25)
        setTransition(25, 'd', 26)
        setTransition(26, 'i', 27)
        setTransition(27, 'n', 28)
        setTransition(28, 'g', 29)

        setTransitionWithout(23, 'i', 14)
        setTransitionWithout(24, 'l', 14)
        setTransitionWithout(25, 'd', 14)
        setTransitionWithout(26, 'i', 14)
        setTransitionWithout(27, 'n', 14)
        setTransitionWithout(28, 'g', 14)
        setTransition(29, 'a'..'z', 14)
        //circ
        setTransition(1, 'c', 30)//city church
        setTransition(30, 'i', 31)//city
        setTransition(31, 'r', 34)
        setTransition(34, 'c', 35)

        setTransitionWithout(30,listOf('h', 'i'), 14)
        setTransitionWithout(31, listOf('r', 't'), 14)
        setTransitionWithout(34, 'c', 14)
        setTransition(35, 'a'..'z', 14)
        //city
        setTransition(31, 't', 32)
        setTransition(32, 'y', 33)

        setTransitionWithout(32, 'y', 14)
        setTransition(33, 'a'..'z', 14)
        //church
        setTransition(30, 'h', 36)
        setTransition(36, 'u', 37)
        setTransition(37, 'r', 38)
        setTransition(38, 'c', 39)
        setTransition(39, 'h', 40)

        setTransitionWithout(36, 'u', 14)
        setTransitionWithout(37, 'r', 14)
        setTransitionWithout(38, 'c', 14)
        setTransitionWithout(39, 'h', 14)
        setTransition(40, 'a'..'z', 14)
        //false
        setTransition(1, 'f', 41)
        setTransition(41, 'a', 42)
        setTransition(42, 'l', 43)
        setTransition(43, 's', 44)
        setTransition(44, 'e', 45)

        setTransitionWithout(41, listOf('a', 'o'), 14)
        setTransitionWithout(42, 'l', 14)
        setTransitionWithout(43, 's', 14)
        setTransitionWithout(44, 'e', 14)
        setTransition(45, 'a'..'z', 14)
        //for
        setTransition(41, 'o', 46)
        setTransition(46, 'r', 47)

        setTransitionWithout(46, 'r', 14)
        setTransition(47, 'a'..'z', 14)
        //if
        setTransition(1, 'i', 106)
        setTransition(106, 'f', 48)

        setTransitionWithout(106, listOf('f', 'n'), 14)
        setTransition(48, 'a'..'z', 14)
        //in
        setTransition(106, 'n', 107)

        setTransition(107, 'a'..'z', 14)
        //line
        setTransition(1, 'l', 49)
        setTransition(49, 'i', 50)
        setTransition(50, 'n', 51)
        setTransition(51, 'e', 52)

        setTransitionWithout(49, listOf('i', 'e'), 14)
        setTransitionWithout(50, listOf('s', 'n'), 14)
        setTransitionWithout(51, 'e', 14)
        setTransition(52, 'a'..'z', 14)
        //list
        setTransition(50, 's', 53)
        setTransition(53, 't', 54)

        setTransitionWithout(53, 't', 14)
        setTransition(54, 'a'..'z', 14)
        //let
        setTransition(49, 'e', 55)
        setTransition(55, 't', 56)

        setTransitionWithout(55, 't', 14)
        setTransition(56, 'a'..'z', 14)
        //park
        setTransition(1, 'p', 57)
        setTransition(57, 'a', 58)
        setTransition(58, 'r', 59)
        setTransition(59, 'k', 60)

        setTransitionWithout(57, listOf('a', 'o'), 14)
        setTransitionWithout(58, 'r', 14)
        setTransitionWithout(59, 'k', 14)
        setTransition(60, 'a'..'z', 14)

        //poly
        setTransition(57, 'o', 61)
        setTransition(61, 'l', 62)
        setTransition(62, 'y', 63)

        setTransitionWithout(61, 'l', 14)
        setTransitionWithout(62, 'y', 14)
        setTransition(63, 'a'..'z', 14)

        //restaurant
        setTransition(1, 'r', 64)
        setTransition(64, 'e', 65)
        setTransition(65, 's', 66)
        setTransition(66, 't', 67)
        setTransition(67, 'a', 68)
        setTransition(68, 'u', 69)
        setTransition(69, 'r', 70)
        setTransition(70, 'a', 71)
        setTransition(71, 'n', 72)
        setTransition(72, 't', 73)

        setTransitionWithout(64, listOf('o', 'i', 'e'), 14)
        setTransitionWithout(65, 's', 14)
        setTransitionWithout(66, 't', 14)
        setTransitionWithout(67, 'a', 14)
        setTransitionWithout(68, 'u', 14)
        setTransitionWithout(69, 'r', 14)
        setTransitionWithout(70, 'a', 14)
        setTransitionWithout(71, 'n', 14)
        setTransitionWithout(72, 't', 14)
        setTransition(73, 'a'..'z', 14)

        //river
        setTransition(64, 'i', 74)
        setTransition(74, 'v', 75)
        setTransition(75, 'e', 76)
        setTransition(76, 'r', 77)

        setTransitionWithout(74, 'v', 14)
        setTransitionWithout(75, 'e', 14)
        setTransitionWithout(76, 'r', 14)
        setTransition(77, 'a'..'z', 14)

        //road
        setTransition(64, 'o', 78)
        setTransition(78, 'a', 79)
        setTransition(79, 'd', 80)

        setTransitionWithout(78, 'a', 14)
        setTransitionWithout(79, 'd', 14)
        setTransition(80, 'a'..'z', 14)

        //school
        setTransition(1, 's', 81)
        setTransition(81, 'c', 82)
        setTransition(82, 'h', 83)
        setTransition(83, 'o', 84)
        setTransition(84, 'o', 85)
        setTransition(85, 'l', 86)

        setTransitionWithout(81, listOf('c', 't'), 14)
        setTransitionWithout(82, 'h', 14)
        setTransitionWithout(83, 'o', 14)
        setTransitionWithout(84, 'o', 14)
        setTransitionWithout(85, 'l', 14)
        setTransition(86, 'a'..'z', 14)
        //stadium
        setTransition(81, 't', 87)
        setTransition(87, 'a', 88)
        setTransition(88, 'd', 89)
        setTransition(89, 'i', 90)
        setTransition(90, 'u', 91)
        setTransition(91, 'm', 92)

        setTransitionWithout(87, 'a', 14)
        setTransitionWithout(88, 'd', 14)
        setTransitionWithout(89, 'i', 14)
        setTransitionWithout(90, 'u', 14)
        setTransitionWithout(91, 'm', 14)
        setTransition(92, 'a'..'z', 14)
        //true
        setTransition(1, 't', 93)
        setTransition(93, 'r', 94)
        setTransition(94, 'u', 95)
        setTransition(95, 'e', 96)

        setTransitionWithout(93, listOf('r', 'o'), 14)
        setTransitionWithout(94, 'u', 14)
        setTransitionWithout(95, 'e', 14)
        setTransition(96, 'a'..'z', 14)
        //townhall
        setTransition(93, 'o', 97)
        setTransition(97, 'w', 98)
        setTransition(98, 'n', 99)
        setTransition(99, 'h', 100)
        setTransition(100, 'a', 101)
        setTransition(101, 'l', 102)
        setTransition(102, 'l', 103)

        setTransitionWithout(97, 'w', 14)
        setTransitionWithout(98, 'n', 14)
        setTransitionWithout(99, 'h', 14)
        setTransitionWithout(100, 'a', 14)
        setTransitionWithout(101, 'l', 14)
        setTransitionWithout(102, 'l', 14)
        setTransition(103, 'a'..'z', 14)


        //SKIP pri skip(15) naredis samo transisione in nakoncu ne setas nobenga sybola.. pri EOF pa
        setTransition(1, '\t', 104)
        setTransition(1, '\r', 104)
        setTransition(1, '\n', 104)
        setTransition(1, ' ', 104)

        setTransition(1, EOF, 105)
        setSymbol(105, EOF_SYMBOL)

        //setSymbol
        setSymbol(2, number_SYMBOL)
        setSymbol(3, l_paren_SYMBOL)
        setSymbol(4, r_paren_SYMBOL)
        setSymbol(5, r_w_paren_SYMBOL)
        setSymbol(6, l_w_paren_SYMBOL)
        setSymbol(7, r_S_SYMBOL)
        setSymbol(8, l_S_SYMBOL)
        setSymbol(9, equal_SYMBOL)
        setSymbol(10, semicolon_SYMBOL)
        setSymbol(11, dot_SYMBOL)
        setSymbol(12, comma_SYMBOL)
        setSymbol(14, string_SYMBOL)
        setSymbol(16, arc_SYMBOL)
        setSymbol(20, bend_SYMBOL)
        setSymbol(22, box_SYMBOL)
        setSymbol(29, building_SYMBOL)
        setSymbol(33, city_SYMBOL)
        setSymbol(35, circ_SYMBOL)
        setSymbol(40, church_SYMBOL)
        setSymbol(45, false_SYMBOL)
        setSymbol(47, for_SYMBOL)
        setSymbol(48, if_SYMBOL)
        setSymbol(107, in_SYMBOL)
        setSymbol(52, line_SYMBOL)
        setSymbol(54, list_SYMBOL)
        setSymbol(56, let_SYMBOL)
        setSymbol(60, park_SYMBOL)
        setSymbol(63, poly_SYMBOL)
        setSymbol(73, restaurant_SYMBOL)
        setSymbol(77, river_SYMBOL)
        setSymbol(80, road_SYMBOL)
        setSymbol(86, school_SYMBOL)
        setSymbol(92, stadium_SYMBOL)
        setSymbol(96, true_SYMBOL)
        setSymbol(103, townhall_SYMBOL)
        setSymbol(105, EOF_SYMBOL)
        setSymbol(108, quotaitons_SYMBOL)
        setSymbol(109, superequal_SYMBOL)
        setSymbol(110, less_SYMBOL)
        setSymbol(111, greater_SYMBOL)

    }


}
data class Token(val symbol: Int, val lexeme: String, val startRow: Int, val startColumn: Int)

class Scanner(private val automaton: DFA, private val stream: InputStream) {
    //private var last: Int? = null

    private var state = automaton.startState
    private var last: Int? = null
    private var buffer = LinkedList<Byte>()
    //____________________

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

        if (automaton.finalStates.contains(state)) {//če je mozno da je to state končni poem se izvede
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
        city_SYMBOL -> "city"
        let_SYMBOL -> "let"
        for_SYMBOL -> "for"
        if_SYMBOL -> "if"
        list_SYMBOL -> "list"
        road_SYMBOL -> "road"
        building_SYMBOL -> "building"
        park_SYMBOL -> "park"
        river_SYMBOL -> "river"
        restaurant_SYMBOL -> "restaurant"
        school_SYMBOL -> "school"
        townhall_SYMBOL -> "townhall"
        church_SYMBOL -> "church"
        stadium_SYMBOL -> "stadium"
        arc_SYMBOL -> "arc"
        number_SYMBOL -> "number" //[0-9]+
        string_SYMBOL -> "string" //[a-zA-Z0-9\s]*
        l_w_paren_SYMBOL -> "l_w_paren" //{
        r_w_paren_SYMBOL -> "r_w_paren"//}
        r_paren_SYMBOL -> "r_paren"//)
        l_paren_SYMBOL -> "l_paren"//(
        semicolon_SYMBOL -> "semicolon"//;
        line_SYMBOL -> "line"
        bend_SYMBOL -> "bend"
        box_SYMBOL -> "box"
        circ_SYMBOL -> "circ"
        poly_SYMBOL -> "poly"
        equal_SYMBOL -> "equal" //=
        in_SYMBOL -> "in"
        dot_SYMBOL -> "dot"//.
        l_S_SYMBOL -> "l_S"//[
        r_S_SYMBOL -> "r_S"//]
        comma_SYMBOL -> "comma" //,
        true_SYMBOL -> "true"
        false_SYMBOL -> "false"
        quotaitons_SYMBOL -> "quotaitons"
        superequal_SYMBOL -> "superequal"
        less_SYMBOL -> "less"
        greater_SYMBOL -> "greater"


        else -> throw Error("Invalid symbol")
    }

fun printTokens(scanner: Scanner) {
    val token = scanner.getToken()
    if (token.symbol != EOF_SYMBOL) {
        print("${name(token.symbol)}(\"${token.lexeme}\") ")
        printTokens(scanner)
    }
}
interface Node {
    fun toGeoJSON(): String


}
data class StatmentNode(val statment: Node): Node{
    override fun toGeoJSON(): String {
        return statment.toGeoJSON()
    }
}
data class ProgramNode(val elements: List<Node>): Node{
    override fun toGeoJSON(): String {
        val features = elements.filterNot { it is LetNode }.map { it.toGeoJSON() }.joinToString(",")
        return """
            {
                "type": "FeatureCollection",
                "features": [$features]
            }
        """.trimIndent()
    }
}



data class NumberNode(val number: Int): Node{
    override fun toGeoJSON(): String {
        return """
            {
                "type": "Point",
                "coordinates": [$number, $number]
            }
        """.trimIndent()
    }
}

data class RoadNode(val name: String, val elements: List<Node>): Node {
    override fun toGeoJSON(): String {
        val coordinates = elements.map {
            when (it) {
                is LineNode -> "[${it.toGeoJSON()}]"
                is BendNode -> "[${it.toGeoJSON()}]"
                else -> "[]" // Should not happen
            }
        }.joinToString(",")
        return """
            {
                "type": "Feature",
                "properties": {
                    "name": "$name"
                },
                "geometry": {
                    "type": "MultiLineString",
                    "coordinates": [$coordinates]
                }
            }
        """.trimIndent()
    }
}


data class ChurchNode(val name: String, val point: PointNode): Node {
    override fun toGeoJSON(): String {
        return """
            {
                "type": "Feature",
                "geometry": {
                  "type": "Point",
                  "coordinates": [${point.x}, ${point.y}]
                },
                "properties": {
                  "name": "$name"
                }
            }
        """.trimIndent()
    }
}


data class PointNode(val x: String, val y: String): Node {
    override fun toGeoJSON(): String {
        return "[$x, $y]"
    }
}

data class CityNode(val name: String, val elements: List<Node>): Node {
    override fun toGeoJSON(): String {
        val cityFeature = """
            {
                "type": "Feature",
                "properties": {
                    "type": "City",
                    "name": "$name"
                },
                "geometry": null
            }
        """.trimIndent()

        val buildingFeatures = elements.map { it.toGeoJSON() }.joinToString(",")
        return "$cityFeature,$buildingFeatures"
    }
}



data class LineNode(val point1: PointNode, val point2: PointNode): Node {
    override fun toGeoJSON(): String {
        return """
            
                [${point1.x}, ${point1.y}],
                [${point2.x}, ${point2.y}]
            
        """.trimIndent()
    }
}
data class Coordinates(val x: Double, val y: Double) {
    operator fun times(scalar: Double): Coordinates {
        return Coordinates(x * scalar, y * scalar)
    }
    operator fun plus(other: Coordinates): Coordinates {
        return Coordinates(x + other.x, y + other.y)
    }
}

class Bezier(private val p0: Coordinates, private val p1: Coordinates, private val p2: Coordinates, private val p3: Coordinates) {
    fun at(t: Double): Coordinates {
        return p0 * (1.0 - t).pow(3.0) + p1 * 3.0 * (1.0 - t).pow(2.0) * t + p2 * 3.0 * (1.0 - t) * t.pow(2.0) + p3 * t.pow(3.0)
    }
    fun toPoints(segmentsCount: Int): List<Coordinates> {
        val ps = mutableListOf<Coordinates>()
        for (i in 0 .. segmentsCount) {
            val t = i / segmentsCount.toDouble()
            ps.add(at(t))
        }
        return ps
    }
}

data class BendNode(val point1: PointNode, val point2: PointNode, val bendFactor: Int) : Node {
    override fun toGeoJSON(): String {
        val p0 = Coordinates(point1.x.toDouble(), point1.y.toDouble())
        val p3 = Coordinates(point2.x.toDouble(), point2.y.toDouble())
        val midX = (p0.x + p3.x) / 2
        val midY = (p0.y + p3.y) / 2

        val scaleFactor = 0.5// Choose this factor according to your desired range for bendFactor
        val shift = bendFactor * scaleFactor

        val p1 = Coordinates(midX, p0.y)
        val p2 = Coordinates(midX + shift, midY)

        val bezier = Bezier(p0, p1, p2, p3)

        // Always generate 20 points between p0 and p3
        val coordinates = bezier.toPoints(100)

        val geoJsonCoordinates = coordinates.joinToString(",") {
            "[${it.x},${it.y}]"
        }

        return "$geoJsonCoordinates"
    }
}





data class ParkNode(val name: String, val center: PointNode, val radius: Double) : Node {
    override fun toGeoJSON(): String {
        val coordinates = mutableListOf<Coordinates>()
        val step = 2 * PI / 30

        for (i in 0 until 30) {
            val angle = step * i

            val x = center.x.toDouble() + radius * cos(angle)
            val y = center.y.toDouble() + radius * sin(angle)

            coordinates.add(Coordinates(x, y))
        }

        // Add the first point again at the end to close the polygon
        coordinates.add(coordinates.first())

        // Construct the coordinates string
        val geoJsonCoordinates = coordinates.joinToString(",") {
            "[${it.x},${it.y}]"
        }

        return """
            {
              "type": "Feature",
              "geometry": {
                "type": "Polygon",
                "coordinates": [
                  [$geoJsonCoordinates]
                ]
              },
              "properties": {
                "name": "$name"
              }
            }
        """.trimIndent()
    }
}






data class BuildingNode(val name: String, val box: BoxNode): Node {
    override fun toGeoJSON(): String {
        return """
            {
                "type": "Feature",
                "geometry": {
                    "type": "Polygon",
                    "coordinates": [${box.toGeoJSON()}]
                },
                "properties": {
                    "name": "$name"
                }
            }
        """.trimIndent()
    }
}


data class BoxNode(val point1: PointNode, val point2: PointNode): Node{
    override fun toGeoJSON(): String {
        val x1 = point1.x.toDouble()
        val y1 = point1.y.toDouble()
        val x2 = point2.x.toDouble()
        val y2 = point2.y.toDouble()

        // Assuming the box is axis aligned, return just the coordinates
        return """
            [
                [$x1, $y1],
                [$x2, $y1],
                [$x2, $y2],
                [$x1, $y2],
                [$x1, $y1]
            ]
        """.trimIndent()
    }
}


data class RiverNode(val name: String, val points: List<PointNode>) : Node {
    override fun toGeoJSON(): String {
        val geoJsonCoordinates = points.joinToString(",") {
            "[${it.x},${it.y}]"
        }

        return """
            {
              "type": "Feature",
              "geometry": {
                "type": "MultiLineString",
                "coordinates": [
                  [$geoJsonCoordinates]
                ]
              },
              "properties": {
                "name": "$name"
              }
            }
        """.trimIndent()
    }
}
data class RestaurantNode(val name: String, val point: PointNode): Node {
    override fun toGeoJSON(): String {
        return """
            {
                "type": "Feature",
                "geometry": {
                  "type": "Point",
                  "coordinates": [${point.x}, ${point.y}]
                },
                "properties": {
                  "name": "$name"
                }
            }
        """.trimIndent()
    }
}

data class SchoolNode(val name: String, val point: PointNode): Node {
    override fun toGeoJSON(): String {
        return """
            {
                "type": "Feature",
                "geometry": {
                  "type": "Point",
                  "coordinates": [${point.x}, ${point.y}]
                },
                "properties": {
                  "name": "$name"
                }
            }
        """.trimIndent()
    }
}

data class TownhallNode(val name: String, val point: PointNode): Node {
    override fun toGeoJSON(): String {
        return """
            {
                "type": "Feature",
                "geometry": {
                  "type": "Point",
                  "coordinates": [${point.x}, ${point.y}]
                },
                "properties": {
                  "name": "$name"
                }
            }
        """.trimIndent()
    }
}

data class StadiumNode(val name: String, val point: PointNode): Node {
    override fun toGeoJSON(): String {
        return """
            {
                "type": "Feature",
                "geometry": {
                  "type": "Point",
                  "coordinates": [${point.x}, ${point.y}]
                },
                "properties": {
                  "name": "$name"
                }
            }
        """.trimIndent()
    }
}
data class LetNode(val name: String, val expression: Node): Node {
    override fun toGeoJSON(): String {
        throw UnsupportedOperationException("LetNode cannot be directly converted to GeoJSON")
    }
}
data class StringNode(val value: String): Node {
    override fun toGeoJSON(): String {
        throw UnsupportedOperationException("StringNode cannot be directly converted to GeoJSON")
    }
}
enum class Comparison {
    GT, LT, EQ
}
data class ConditionNode(val left: NumberNode, val comparison: Comparison, val right: NumberNode): Node {
    fun evaluate(): Boolean {
        return when (comparison) {
            Comparison.GT -> left.number > right.number
            Comparison.LT -> left.number < right.number
            Comparison.EQ -> left.number == right.number
        }
    }

    override fun toGeoJSON(): String {
        throw UnsupportedOperationException("ConditionNode cannot be directly converted to GeoJSON")
    }
}

data class IfNode(val condition: ConditionNode, val body: CityNode): Node {
    override fun toGeoJSON(): String {
        return if (condition.evaluate()) {
            body.toGeoJSON()
        } else {
            ""
        }
    }
}






class ParseException(symbol: Int, lexeme: String, row: Int, column: Int) : Exception("PARSE ERROR (${name(symbol)}, $lexeme) at $row:$column")


class Parser(private val scanner: Scanner){
    private var last: Token? = null
    private val variables = mutableMapOf<String, PointNode>()


    private fun panic(): Nothing =
        last?.let { throw ParseException(it.symbol, it.lexeme, it.startRow, it.startColumn) } ?: error("cannot happen")

    //<program> ::= <statement>*
    fun parse(): Node {
        last = scanner.getToken()
        val statements = mutableListOf<Node>()
        while (last?.symbol != EOF_SYMBOL) {
            statements.add(parseStatement())
        }
        return ProgramNode(statements)
    }
    //<statement> ::= <city> | <let> | <if>
    private fun parseStatement(): Node {
        return when (last?.symbol) {

            city_SYMBOL -> parseCity()
            let_SYMBOL -> parseLet()
            //if_SYMBOL -> parseIf()
            else -> panic()
        }
    }
    //<city> ::= "city" <string> "{" <city_element>* "}"
    private fun parseCity(): CityNode {
        parseTerminal(city_SYMBOL)
        val name = parseTerminal(string_SYMBOL)
        parseTerminal(l_w_paren_SYMBOL) // changed to left brace symbol
        val elements = mutableListOf<Node>()
        while (last?.symbol != r_w_paren_SYMBOL) { // changed to right brace symbol
            elements.add(parseCityElement())
        }
        parseTerminal(r_w_paren_SYMBOL) // changed to right brace symbol
        return CityNode(name, elements)
    }
    //<city_element> ::= <road> | <building> | <park> | <river> | <restaurant> | <school> | <townhall> | <church> | <stadium> | <let> | <for>
    private fun parseCityElement(): Node {
        return when (last?.symbol) {
            road_SYMBOL -> parseRoad()
            building_SYMBOL -> parseBuilding()
            park_SYMBOL -> parsePark()
            river_SYMBOL -> parseRiver()
            restaurant_SYMBOL -> parseRestaurant()
            school_SYMBOL -> parseSchool()
            townhall_SYMBOL -> parseTownhall()
            church_SYMBOL -> parseChurch()
            stadium_SYMBOL -> parseStadium()
//            let_SYMBOL -> parseLet()
//            for_SYMBOL -> parseFor()
//            if_SYMBOL -> parseIf()
            else -> panic()
        }
    }
    //<building> ::= "building" <string> "{" "box" <point> <point> "}"
    private fun parseBuilding(): BuildingNode {
        parseTerminal(building_SYMBOL)
        val name = parseTerminal(string_SYMBOL)
        parseTerminal(l_w_paren_SYMBOL) // changed to left brace symbol
        parseTerminal(box_SYMBOL)
        val point1 = parsePoint()
        val point2 = parsePoint()
        parseTerminal(r_w_paren_SYMBOL) // changed to right brace symbol
        val temp= BoxNode(point1, point2)
        return BuildingNode(name, temp)
    }

    private fun parsePoint(): PointNode {
        parseTerminal(l_paren_SYMBOL)
        val xToken = last
        val xString = if (last?.symbol == number_SYMBOL) parseTerminal(number_SYMBOL) else parseTerminal(string_SYMBOL)
        val x = if (variables.containsKey(xString)) {
            variables[xString]?.x ?: panic()
        } else {
            xString
        }
        parseTerminal(comma_SYMBOL)
        val yToken = last
        val yString = if (last?.symbol == number_SYMBOL) parseTerminal(number_SYMBOL) else parseTerminal(string_SYMBOL)
        val y = if (variables.containsKey(yString)) {
            variables[yString]?.y ?: panic()
        } else {
            yString
        }
        parseTerminal(r_paren_SYMBOL)
        return PointNode(x, y)
    }


    fun parseRoad(): RoadNode {
        parseTerminal(road_SYMBOL)
        val roadName = parseTerminal(string_SYMBOL)
        parseTerminal(l_w_paren_SYMBOL)
        val elements = mutableListOf<Node>()
        while (last?.symbol != r_w_paren_SYMBOL) {
            when (last?.symbol) {
                line_SYMBOL -> elements.add(parseLine())
                bend_SYMBOL -> elements.add(parseBend())
                else -> panic()
            }
        }
        parseTerminal(r_w_paren_SYMBOL)
        return RoadNode(roadName, elements)
    }

    fun parseLine(): LineNode {
        parseTerminal(line_SYMBOL)
        parseTerminal(l_paren_SYMBOL)

        val point1 = parsePoint()
        val point2 = parsePoint()
        parseTerminal(r_paren_SYMBOL)
        parseTerminal(semicolon_SYMBOL)
        return LineNode(point1, point2)
    }

    fun parseBend(): BendNode {
        parseTerminal(bend_SYMBOL)
        parseTerminal(l_paren_SYMBOL)
        val point1 = parsePoint()
        val point2 = parsePoint()
        val bendFactor = parseTerminal(number_SYMBOL).toInt()
        parseTerminal(r_paren_SYMBOL)
        parseTerminal(semicolon_SYMBOL)
        return BendNode(point1, point2, bendFactor)
    }
    fun parsePark(): ParkNode {
        parseTerminal(park_SYMBOL)
        val parkName = parseTerminal(string_SYMBOL)

        parseTerminal(l_w_paren_SYMBOL) // Match opening brace
        parseTerminal(circ_SYMBOL) // Match 'circ' keyword

        val centerPoint = parsePoint() // Parse the center point

        val radius = parseTerminal(number_SYMBOL).toDouble() // Match the radius value

        parseTerminal(r_w_paren_SYMBOL) // Match closing brace

        return ParkNode(parkName, centerPoint, radius) // Construct and return the ParkNode object
    }

    fun parseRiver(): RiverNode {
        parseTerminal(river_SYMBOL)
        val riverName = parseTerminal(string_SYMBOL)
        parseTerminal(l_w_paren_SYMBOL) // Match opening brace
        parseTerminal(poly_SYMBOL) // Match 'poly' keyword

        val points = parsePointList()

        parseTerminal(r_w_paren_SYMBOL)
        return RiverNode(riverName, points) // Construct and return the RiverNode object
    }

    private fun parsePointList(): List<PointNode> {
        parseTerminal(l_paren_SYMBOL)
        val points = mutableListOf<PointNode>()
        points.add(parsePoint())
        while (last?.symbol == comma_SYMBOL) {
            parseTerminal(comma_SYMBOL)
            points.add(parsePoint())
        }
        parseTerminal(r_paren_SYMBOL)
        return points
    }

    private fun parseRestaurant(): RestaurantNode {
        parseTerminal(restaurant_SYMBOL)
        val name = parseTerminal(string_SYMBOL)
        val point = parsePoint()
        return RestaurantNode(name, point)
    }

    private fun parseSchool(): SchoolNode {
        parseTerminal(school_SYMBOL)
        val name = parseTerminal(string_SYMBOL)
        val point = parsePoint()
        return SchoolNode(name, point)
    }

    private fun parseTownhall(): TownhallNode {
        parseTerminal(townhall_SYMBOL)
        val name = parseTerminal(string_SYMBOL)
        val point = parsePoint()
        return TownhallNode(name, point)
    }

    private fun parseStadium(): StadiumNode {
        parseTerminal(stadium_SYMBOL)
        val name = parseTerminal(string_SYMBOL)
        val point = parsePoint()
        return StadiumNode(name, point)
    }
    private fun parseChurch(): ChurchNode {
        parseTerminal(church_SYMBOL)
        val name = parseTerminal(string_SYMBOL)
        val point = parsePoint()
        return ChurchNode(name, point)
    }
    private fun parseLet(): LetNode {
        parseTerminal(let_SYMBOL)
        val name = parseTerminal(string_SYMBOL)
        parseTerminal(equal_SYMBOL)
        val point = parsePoint()
        parseTerminal(semicolon_SYMBOL)
        variables[name] = point
        return LetNode(name, point)
    }

    private fun parseExpression(): Node {
        return when (last?.symbol) {
            number_SYMBOL -> parseNumber()
            string_SYMBOL -> parseString()
            point_SYMBOL -> parsePoint()
            else -> panic()
        }
    }

    private fun parseNumber(): NumberNode {
        val value = parseTerminal(number_SYMBOL)
        return NumberNode(value.toInt())
    }

    private fun parseString(): StringNode {
        val value = parseTerminal(string_SYMBOL)
        return StringNode(value)
    }
//    private fun parseIf(): Node {
//        parseTerminal(if_SYMBOL)
//        val condition = parseCondition()
//        parseTerminal(l_w_paren_SYMBOL) // Open brace
//        val city = parseCity()
//        parseTerminal(r_w_paren_SYMBOL) // Close brace
//        return IfNode(condition, city)
//    }
//
//    private fun parseCondition(): Node {
//        val num1 = parseTerminal(number_SYMBOL).toDouble()
//        val comparison = parseComparison()
//        val num2 = parseTerminal(number_SYMBOL).toDouble()
//        return ConditionNode(num1, comparison, num2)
//    }
//    private fun parseComparison(): Comparison {
//        val symbol = parseTerminal(comparison_SYMBOL)
//        return when (symbol) {
//            ">" -> ConversionComparator.Comparison.GT
//            "<" -> ConversionComparator.Comparison.LT
//            "==" -> ConversionComparator.Comparison.EQ
//            else -> panic()
//        }
//    }


    private fun parseTerminal(symbol: Int): String =
        if (last?.symbol == symbol) {
            val lexeme = last!!.lexeme
            last = scanner.getToken()
            lexeme
        } else {
            panic()
        }
}






fun main(){
    val input = """
        let spremenljivka = (1, 2);
        city ljubljana {
            building hisa {
                box (spremenljivka, 2) (4, 3)
            }
            road presernova {
                line((2, 2) (2, 5));
                line((5, 4) (6, 4));
                bend ((1, 1) (2, 2) 2);
            }
              park celjski_park { circ (1, 1) 2 }
              river sava { poly ((2, 3), (4, 6), (8, 8)) }
              restaurant hut_burger (13, 7)
              school fri (15, 7)
              townhall obcina_ljubljana (10, 4)
              church peter (4, 9)
              stadium stozice (8, 8)
        }
    """.trimIndent()

    val scanner = Scanner(Automaton, input.byteInputStream())
    val parser = Parser(scanner)
    val program = parser.parse()
    val geoJSON = program.toGeoJSON()

    println(geoJSON)
}


//printTokens(Scanner(Automaton, "city \"ljubljana\" {\nroad \"presernova ulica\" {\nline((2.5, 2), (2, 5)); \n line((5, 4), (6, 4));\nbend ((1, 1), (2, 2), 20);\n}\npark \"soncni park\" { circ (5, 8) 5 }\n restaurant \"hut burger\" (13, 7)\nschool \"fri\" (15, 7)\n townhall \"obcina ljubljana\" (10, 4)\nchurch \"sv. peter\" (4, 9)\nstadium \"stozice\" (8, 8)\nlet \"population\" = 15\nlet \"xos\" = 16\nfor \"mesto\" in 1 .. population {\nbuilding \"hisa\" { box ( xos , 2) ( xos , 3) }\n}\n}".byteInputStream()))





//    printTokens(Scanner(Automaton, (" { } ( ) [ ] . , \" road park river church stadium restaurant school townhall building " +
//            " list let if for in true false city line bend circ box poly population xos stevilo mesta gostilne kipec neboticnik juznaplaza trznica dvorec mestnipark zupanciceva pinestreet celjskipark bajte ljudski vrt joze mariborska obcina feri poper drava dvorana stozice peter obcina ljubljana fri soncni_park presernova ulica ljubljana maribor celje ptuj piran novo mesto koper murska sobota kranj").byteInputStream()))










//pri boxu je treba iz dveh zracunat se druge dve in v formatu je peta tocka se enkrat 1.
//pri bendu je treba izracunat se vmesne manjse tocke...tak:
//Calculate the differences in x (dx = x2 - x1) and y (dy = y2 - y1).
//Divide these differences by the number of steps you want in the curve (n), in your case 10. This will give you small increments in x (dx/n) and y (dy/n).
//Start from the first point (x1, y1), and add the increments to x and y to get to the next point. Repeat this step until you get to the last point (x2, y2).


//za circ tudi:
//For example, if you wished to approximate a circle centered at (3,3) with radius 2 using a 10-sided polygon, you would calculate the points like this:
//
//The formula to generate each point (x, y) on the circumference of the circle is:
//x = rcos(theta) + x_center
//y = rsin(theta) + y_center
//
//Where:
//
//r is the radius of the circle.
//theta is the angle for the point, which we increment evenly to generate all points. For a 10-sided polygon, we would increment theta by 36 degrees (or 2*pi/10 in radians) for each point.
//(x_center, y_center) are the coordinates of the center of the circle.
//ven pride to:
//[2.34, 4.19],
//[1, 3.41],
//[1, 2.59],
//[2.34, 1.81],
//[3.66, 1.81],
//[5, 2.59],
//[5, 3.41],
//[3.66, 4.19],
//[2.34, 4.19]