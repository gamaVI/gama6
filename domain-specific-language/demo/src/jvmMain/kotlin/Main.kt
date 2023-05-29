package task

import DFA
import Automaton
import Parser
import Scanner
import Token

import jdk.dynalink.linker.ConversionComparator
import name
import org.openqa.selenium.JavascriptExecutor
import java.io.InputStream
import java.util.*
import java.io.File
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import java.awt.Desktop
import java.net.URI
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver


const val EOF = -1
const val NEWLINE = '\n'.code
const val ERROR_STATE = 0
const val EOF_SYMBOL = -1
const val SKIP_SYMBOL = 0
const val city_SYMBOL = 3
const val let_SYMBOL = 4
const val for_SYMBOL = 5
const val if_SYMBOL = 6
const val list_SYMBOL = 7
const val road_SYMBOL = 9
const val building_SYMBOL = 10
const val park_SYMBOL = 11
const val river_SYMBOL = 12
const val restaurant_SYMBOL = 13
const val school_SYMBOL = 14
const val townhall_SYMBOL = 15
const val church_SYMBOL = 16
const val stadium_SYMBOL = 17
const val arc_SYMBOL = 22
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

fun main() {
    System.setProperty(
        "webdriver.chrome.driver",
        "C:\\Users\\LENOVO\\OneDrive\\gradivo za feri\\4.Semester\\Praktikum\\chromedriver.exe"
    )
    val driver: WebDriver = ChromeDriver()
    driver.get("http://geojson.io/#map=3.51/11.85/15.62")

    val input = """     
   
  let spremenljivka = 2;
        let var = 1;
        if spremenljivka > var {
        city maribor {
            building bajta {
                box (3, 4) (5, 6)
            }
            building bajta {
                box (5, 4) (7, 6)
            }
            building bajta {
                box (7, 4) (9, 6)
            }
            building bajta {
                box (9, 4) (11, 6)
            }
            building bajta {
                box (11, 4) (13, 6)
            }
            building bajta {
                box (3, 0) (5, 2)
            }
            building bajta {
                box (5, 0) (7, 2)
            }
            building bajta {
                box (7, 0) (9, 2)
            }
            building bajta {
                box (9, 0) (11, 2)
            }
            building bajta {
                box (11, 0) (13, 2)
            }

            road koroska {
                line((1, 3) (13, 3));
                bend ((13, 3) (15, 6) 2);
                bend ((13, 3) (15, 0) 2);
                line((15, 6) (15, 20));
            }
              park promenada { circ (17, 3) 2 }
             building cerkev {
                box (16, 5) (18, 4)
            }
              church sv_jurija (16, 5)
               building bajta {
                box (16, 6) (18, 8)
            }
               building bajta {
                box (16, 8) (18, 10)
            }
              building bajta {
                box (16, 10) (18, 12)
            }
              building bajta {
                box (16, 12) (18, 14)
            }
             building bajta {
                box (16, 14) (18, 16)
            }

            building univerza_v_mariboru {
                box (9, 7) (14, 18)
            }
            restaurant eat_smart_feri (10, 8)
            school feri (12, 10)
            river drava { poly ((20, 25), (22, 23), (20, 21), (22, 19), (20, 17), (22, 15), (20, 13), (22, 11), (20, 9), (22, 7), (20, 5), (22, 3), (20, 1)) }
            road smetanova {
            line ((5, 20) (25, 20));
            bend ((25, 20) (27, 18) 2);
            bend ((25, 20) (27, 22) 2);
            line ((27, 18) (27, 0));
            }
            building mestna_obcina_maribor {
                box (26, 17) (23, 12)
            }
            townhall mestna_obcina_maribor (24, 14)

            park vijolcni_park { circ (24, 9) 2 }
            stadium ljudski_vrt (24, 9)


        }
        }

    """.trimIndent()

    val scanner = Scanner(Automaton, input.byteInputStream())
    val parser = Parser(scanner)
    val program = parser.parse()
    val geoJSON = program.toGeoJSON()

    println(geoJSON)

    // Pocakaj, da se stran nalozi
    Thread.sleep(500)

    // Vstavi geoJSON v brskalnik
    val js = driver as JavascriptExecutor
    js.executeScript(
        "var editor= document.querySelector('.CodeMirror').CodeMirror; editor.setValue(arguments[0]);",
        geoJSON
    )

    // pustimo stran odprto
    Thread.sleep(400000)

    //zapremo brskalnik
    driver.quit()
}


//        printTokens(Scanner(Automaton, ("  < > == if = { } ( ) [ ] . , \" road park river church stadium restaurant school townhall building " +
//            " list let if for in true false city line bend circ box poly population xos stevilo mesta gostilne kipec neboticnik juznaplaza trznica dvorec mestnipark zupanciceva pinestreet celjskipark bajte ljudski vrt joze mariborska obcina feri poper drava dvorana stozice peter obcina ljubljana fri soncni_park presernova ulica ljubljana maribor celje ptuj piran novo mesto koper murska sobota kranj").byteInputStream()))


//printTokens(Scanner(Automaton, "city \"ljubljana\" {\nroad \"presernova ulica\" {\nline((2.5, 2), (2, 5)); \n line((5, 4), (6, 4));\nbend ((1, 1), (2, 2), 20);\n}\npark \"soncni park\" { circ (5, 8) 5 }\n restaurant \"hut burger\" (13, 7)\nschool \"fri\" (15, 7)\n townhall \"obcina ljubljana\" (10, 4)\nchurch \"sv. peter\" (4, 9)\nstadium \"stozice\" (8, 8)\nlet \"population\" = 15\nlet \"xos\" = 16\nfor \"mesto\" in 1 .. population {\nbuilding \"hisa\" { box ( xos , 2) ( xos , 3) }\n}\n}".byteInputStream()))


