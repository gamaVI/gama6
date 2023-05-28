import task.*

object Automaton : DFA {
    override val startState = 1
    override val states = setOf(-2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 ,24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56,   57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82,  83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95 ,96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111)
    override val finalStates = setOf(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 16, 20, 22, 29, 33,35,40, 45, 47, 48, 107, 52, 54, 56, 60, 63, 73, 77, 80, 86, 92, 96, 103, 104, 105, 108, 109, 110, 111)
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
        setSymbol(110, greater_SYMBOL)
        setSymbol(111, less_SYMBOL)

    }


}