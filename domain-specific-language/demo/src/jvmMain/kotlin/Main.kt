package task

import DFA
import Automaton
import Parser
import Scanner
import Token

import jdk.dynalink.linker.ConversionComparator
import name
import java.io.InputStream
import java.util.*
import java.io.File
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import java.awt.Desktop
import java.net.URI


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


fun printTokens(scanner: Scanner) {
    val token = scanner.getToken()
    if (token.symbol != EOF_SYMBOL) {
        print("${name(token.symbol)}(\"${token.lexeme}\") ")
        printTokens(scanner)
    }
}

fun main(){
    val input = """
    let spremenljivka = 2;
    let var = 1;
    if spremenljivka > var {
    city ljubljana {
        building hisa {
            box (5, 5) (3, 3)
        }
        road presernova {
            line((2, 2) (2, 4));
            line((5, 3) (6, 4));
            bend ((2, 2) (3, 3) 2);
        }
        park celjski_park { circ (4, 4) 1 }
        river sava { poly ((1, 1), (1, 3), (3, 5), (5, 5)) }
        restaurant hut_burger (7, 2)
        school fri (8, 2)
        townhall obcina_ljubljana (6, 6)
        church peter (3, 7)
        stadium stozice (9, 4)
    }
    }
""".trimIndent()

    val scanner = Scanner(Automaton, input.byteInputStream())
    val parser = Parser(scanner)
    val program = parser.parse()
    val geoJSON = program.toGeoJSON()

    println(geoJSON)

    // Open geojson.io in your default web browser
    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
        Desktop.getDesktop().browse(URI("http://geojson.io"))
    }
}


//        printTokens(Scanner(Automaton, ("  < > == if = { } ( ) [ ] . , \" road park river church stadium restaurant school townhall building " +
//            " list let if for in true false city line bend circ box poly population xos stevilo mesta gostilne kipec neboticnik juznaplaza trznica dvorec mestnipark zupanciceva pinestreet celjskipark bajte ljudski vrt joze mariborska obcina feri poper drava dvorana stozice peter obcina ljubljana fri soncni_park presernova ulica ljubljana maribor celje ptuj piran novo mesto koper murska sobota kranj").byteInputStream()))


//printTokens(Scanner(Automaton, "city \"ljubljana\" {\nroad \"presernova ulica\" {\nline((2.5, 2), (2, 5)); \n line((5, 4), (6, 4));\nbend ((1, 1), (2, 2), 20);\n}\npark \"soncni park\" { circ (5, 8) 5 }\n restaurant \"hut burger\" (13, 7)\nschool \"fri\" (15, 7)\n townhall \"obcina ljubljana\" (10, 4)\nchurch \"sv. peter\" (4, 9)\nstadium \"stozice\" (8, 8)\nlet \"population\" = 15\nlet \"xos\" = 16\nfor \"mesto\" in 1 .. population {\nbuilding \"hisa\" { box ( xos , 2) ( xos , 3) }\n}\n}".byteInputStream()))


