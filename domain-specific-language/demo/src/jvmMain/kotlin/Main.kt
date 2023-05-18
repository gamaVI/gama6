
package task

import java.io.InputStream
import java.util.*
import java.io.File


const val ERROR_SYMBOL = 0
const val EOF_SYMBOL = -1
const val SKIP_SYMBOL = -2
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
    override val states = setOf(-2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 ,24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44,45)
    override val finalStates = setOf(-2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 ,24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44,45)
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


}



fun main(){
    //printTokens(Scanner(ForForeachFFFAutomaton, "5 * #69f9D\n".byteInputStream()))
}