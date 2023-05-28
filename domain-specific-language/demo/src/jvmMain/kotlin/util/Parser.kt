import task.*

class ParseException(symbol: Int, lexeme: String, row: Int, column: Int) :
    Exception("PARSE ERROR (${name(symbol)}, $lexeme) at $row:$column")


class Parser(private val scanner: Scanner) {
    private var last: Token? = null
    private val variables = mutableMapOf<String, Double>()


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
            if_SYMBOL -> parseIf()
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
            //for_SYMBOL -> parseFor()

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
        val temp = BoxNode(point1, point2)
        return BuildingNode(name, temp)
    }

    private fun parsePoint(): PointNode {
        parseTerminal(l_paren_SYMBOL)
        val x = if (last?.symbol == number_SYMBOL) parseTerminal(number_SYMBOL).toDouble() else variables[parseTerminal(
            string_SYMBOL
        )] ?: panic()
        parseTerminal(comma_SYMBOL)
        val y = if (last?.symbol == number_SYMBOL) parseTerminal(number_SYMBOL).toDouble() else variables[parseTerminal(
            string_SYMBOL
        )] ?: panic()
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
        val value = parseTerminal(number_SYMBOL).toDouble()
        parseTerminal(semicolon_SYMBOL)
        variables[name] = value
        return LetNode(name, value)
    }

    private fun parseNumber(): NumberNode {
        val value = parseTerminal(number_SYMBOL)
        return NumberNode(value.toInt())
    }

    private fun parseIf(): IfNode {
        parseTerminal(if_SYMBOL)
        val condition = parseCondition()
        parseTerminal(l_w_paren_SYMBOL) // changed to left brace symbol
        val city = parseCity()
        parseTerminal(r_w_paren_SYMBOL) // changed to right brace symbol
        return IfNode(condition, city)
    }

    private fun parseCondition(): ConditionNode {
        val num1 = parseNumberOrVariable()
        val comparison = parseComparison()
        val num2 = parseNumberOrVariable()
        return ConditionNode(num1, comparison, num2)
    }

    private fun parseNumberOrVariable(): NumberNode {
        return if (last?.symbol == number_SYMBOL) {
            parseNumber()
        } else {
            val name = parseTerminal(string_SYMBOL)
            NumberNode(variables[name]?.toInt() ?: panic())
        }
    }


    private fun parseComparison(): ComparisonNode {
        return when (last?.symbol) {
            greater_SYMBOL -> {
                parseTerminal(greater_SYMBOL)
                ComparisonNode(ComparisonNode.Operator.GT)
            }

            less_SYMBOL -> {
                parseTerminal(less_SYMBOL)
                ComparisonNode(ComparisonNode.Operator.LT)
            }

            superequal_SYMBOL -> {
                parseTerminal(superequal_SYMBOL)
                ComparisonNode(ComparisonNode.Operator.EQ)
            }

            else -> panic()
        }
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
