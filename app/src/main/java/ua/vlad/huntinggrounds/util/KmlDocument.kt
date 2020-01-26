package ua.vlad.huntinggrounds.util

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import java.io.BufferedReader

class KmlDocument {

    enum class Tag(val value: String) {
        PLACEMARK("Placemark"),
        MULTI_GEOMETRY("MultiGeometry"),
        POLYGON("Polygon"),
        OUTER_BOUNDARY_IS("outerBoundaryIs"),
        INNER_BOUNDARY_IS("innerBoundaryIs"),
        COORDINATES("coordinates")
    }

    private var document: Document

    constructor(kml: String) {
        document = Jsoup.parse(kml, "", Parser.xmlParser())
    }

    constructor(context: Context, resource: Int) {
        val inputStream = context.resources.openRawResource(resource)
        val kml = inputStream.bufferedReader().use(BufferedReader::readText)
        document = Jsoup.parse(kml, "", Parser.xmlParser())
    }

    fun extractPolygons(): List<PolygonOptions> {
        val polygons = mutableListOf<PolygonOptions>()
        document.select(Tag.PLACEMARK.value).forEach { placemark ->
            placemark.childNodes().forEach { node ->
                if (node is Element) {
                    extractPolygons(polygons, node)
                }
            }
        }
        return polygons
    }

    private fun extractPolygons(polygons: MutableList<PolygonOptions>, geometry: Element) {
        when (geometry.tag().name) {
            Tag.MULTI_GEOMETRY.value -> geometry.childNodes().forEach { node ->
                if (node is Element) {
                    extractPolygons(polygons, node)
                }
            }
            Tag.POLYGON.value -> {
                polygons.add(createPolygon(geometry))
            }
        }
    }

    private fun createPolygon(geometry: Element): PolygonOptions {
        return PolygonOptions()
            .clickable(true)
            .setDefaultStyle()
            .also { polygonOptions ->
                geometry.childNodes().forEach { node ->
                    if (node is Element) {
                        when (node.tag().name) {
                            Tag.OUTER_BOUNDARY_IS.value -> polygonOptions.addAll(extractBounds(node))
                            Tag.INNER_BOUNDARY_IS.value -> polygonOptions.addHole(extractBounds(node))
                        }
                    }
                }
            }
    }

    private fun extractBounds(element: Element): List<LatLng> {
        val coordinatesStrList = element.select(Tag.COORDINATES.value).first().text().split(" ".toRegex())
        val latLngList = mutableListOf<LatLng>()
        coordinatesStrList.forEach { coordinatesString ->
            val coordinates = coordinatesString.split(",".toRegex())
            latLngList.add(LatLng(coordinates[1].toDouble(), coordinates[0].toDouble()))
        }
        return latLngList
    }
}
