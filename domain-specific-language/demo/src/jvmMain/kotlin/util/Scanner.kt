import task.*
import java.io.InputStream
import java.util.*

data class Token(val symbol: Int, val lexeme: String, val startRow: Int, val startColumn: Int)

class Scanner(private val automaton: DFA, private val stream: InputStream) {
        private var last: Int? = null

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
        last = code

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
