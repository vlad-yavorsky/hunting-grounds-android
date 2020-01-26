package ua.vlad.huntinggrounds.data.model

import androidx.room.Embedded

class GroundForView(
    val id: Int,
    val name: String,
    val kml: String,
    val area: Double,
    val markerCoords: String,
    @Embedded(prefix = "obl_") val oblast: Oblast
)
