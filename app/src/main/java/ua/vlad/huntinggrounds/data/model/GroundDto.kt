package ua.vlad.huntinggrounds.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class GroundDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("alias") val alias: String,
    @SerializedName("created") val created: Date,
    @SerializedName("kml") val kml: String,
    @SerializedName("area") val area: Double,
    @SerializedName("markerCoords") val markerCoords: String,
    @SerializedName("address") val address: String,
    @SerializedName("description") val description: String,
    @SerializedName("oblast") var oblast: Oblast
//    @SerializedName("polygons") val polygons: List<PolygonDto>,
//    @SerializedName("marker") val marker: LatLngDto?,
) {

    fun toGround(): Ground {
        val ground = Ground()
        ground.id = id
        ground.name = name
        ground.alias = alias
        ground.created = created
        ground.kml = kml
        ground.oblastId = oblast.id
        ground.area = area
        ground.markerCoords = markerCoords
        ground.address = address
        ground.description = description
        // ignored fields:
        ground.oblast = oblast
//        ground.polygons = polygons.map(PolygonDto::toPolygonOptions)
//        ground.marker = marker?.toLatLng()
        return ground
    }
}
