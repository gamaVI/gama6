import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

interface Node {
    fun toGeoJSON(): String
    fun replaceVariable(name: String, value: Double): Node {
        // vrača samo samega sebe
        return this
    }
}


data class ProgramNode(val elements: List<Node>) : Node {
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


data class NumberNode(val number: Int) : Node {
    override fun toGeoJSON(): String {
        return """
            {
                "type": "Point",
                "coordinates": [$number, $number]
            }
        """.trimIndent()
    }
}

data class RoadNode(val name: String, val elements: List<Node>) : Node {
    override fun toGeoJSON(): String {
        val coordinates = elements.map {
            when (it) {
                is LineNode -> "[${it.toGeoJSON()}]"
                is BendNode -> "[${it.toGeoJSON()}]"
                else -> "[]" // nesme prit do tega... ampak tudi če ne bo vračalo napake in geoJson bo delal
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


data class ChurchNode(val name: String, val point: PointNode) : Node {
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


data class PointNode(val x: Double, val y: Double) : Node {
    override fun toGeoJSON(): String {
        return "[$x, $y]"
    }
}

data class CityNode(val name: String, val elements: List<Node>) : Node {
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


data class LineNode(val point1: PointNode, val point2: PointNode) : Node {
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

class Bezier(
    private val p0: Coordinates,
    private val p1: Coordinates,
    private val p2: Coordinates,
    private val p3: Coordinates
) {
    fun at(t: Double): Coordinates {
        return p0 * (1.0 - t).pow(3.0) + p1 * 3.0 * (1.0 - t).pow(2.0) * t + p2 * 3.0 * (1.0 - t) * t.pow(2.0) + p3 * t.pow(
            3.0
        )
    }

    fun toPoints(segmentsCount: Int): List<Coordinates> {
        val ps = mutableListOf<Coordinates>()
        for (i in 0..segmentsCount) {
            val t = i / segmentsCount.toDouble()
            ps.add(at(t))
        }
        return ps
    }
}

data class BendNode(val point1: PointNode, val point2: PointNode, val bendFactor: Int) : Node {
    override fun toGeoJSON(): String {
        val p0 = Coordinates(point1.x, point1.y)
        val p3 = Coordinates(point2.x, point2.y)
        val midX = (p0.x + p3.x) / 2
        val midY = (p0.y + p3.y) / 2

        val scaleFactor = 0.5// lahko nastavlaš kak hudo močen bend bo(priporočano med 0.1 in 2)
        val shift = bendFactor * scaleFactor

        val p1 = Coordinates(midX, p0.y)
        val p2 = Coordinates(midX + shift, midY)

        val bezier = Bezier(p0, p1, p2, p3)

        // nastaviš koliko točk naj bo med začetno in končno točko
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

            val x = center.x+ radius * cos(angle)
            val y = center.y + radius * sin(angle)

            coordinates.add(Coordinates(x, y))
        }

        // mores na koncu dodat se prvo tocko da se zapre poligon
        coordinates.add(coordinates.first())

        // pretvorba v geoJson
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


data class BuildingNode(val name: String, val box: BoxNode) : Node {
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

    override fun replaceVariable(name: String, value: Double): Node {
        return BuildingNode(name, box.replaceVariable(name, value) as BoxNode)
    }
}


data class BoxNode(val point1: PointNode, val point2: PointNode) : Node {
    override fun toGeoJSON(): String {
        val x1 = point1.x
        val y1 = point1.y
        val x2 = point2.x
        val y2 = point2.y

        // spet mores na koncu dodat se prvo tocko da se zapre poligon
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

data class RestaurantNode(val name: String, val point: PointNode) : Node {
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

data class SchoolNode(val name: String, val point: PointNode) : Node {
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

data class TownhallNode(val name: String, val point: PointNode) : Node {
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

data class StadiumNode(val name: String, val point: PointNode) : Node {
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

data class LetNode(val name: String, val expression: Double) : Node {
    override fun toGeoJSON(): String {
        throw UnsupportedOperationException("LetNode cannot be directly converted to GeoJSON")
    }
}



class ComparisonNode(val operator: Operator) : Node {
    enum class Operator {
        GT,
        LT,
        EQ
    }

    override fun toGeoJSON(): String {
        //tu samo vrnemo operator
        return operator.toString()
    }
}

class ConditionNode(val left: NumberNode, val comparison: ComparisonNode, val right: NumberNode) : Node {
    override fun toGeoJSON(): String {
        // tu samo vrnemo prazno string ce je false, ce je true pa vrnemo city.toGeoJSON()
        return ""
    }

    fun evaluate(): Boolean {
        return when (comparison.operator) {
            ComparisonNode.Operator.GT -> left.number > right.number
            ComparisonNode.Operator.LT -> left.number < right.number
            ComparisonNode.Operator.EQ -> left.number == right.number
        }
    }

}


class IfNode(val condition: ConditionNode, val city: CityNode) : Node {
    override fun toGeoJSON(): String {
        // tu samo vrnemo prazno string ce je false, ce je true pa vrnemo city.toGeoJSON()
        return if (condition.evaluate()) city.toGeoJSON() else ""
    }
}

