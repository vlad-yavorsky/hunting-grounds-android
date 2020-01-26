package ua.vlad.huntinggrounds.data.model

import com.google.android.gms.maps.model.PolygonOptions
import com.google.gson.annotations.SerializedName
import ua.vlad.huntinggrounds.util.setDefaultStyle

data class PolygonDto(
    @SerializedName("outerBounds") val outerBounds: List<LatLngDto>? = null,
    @SerializedName("innerBoundsList") val innerBoundsList: List<List<LatLngDto>>? = null
) {
    fun toPolygonOptions(): PolygonOptions {
        return PolygonOptions()
            .clickable(true)
            .setDefaultStyle()
            .addAll(outerBounds!!.map(LatLngDto::toLatLng))
            .also { polygonOptions -> innerBoundsList!!.forEach { innerBounds -> polygonOptions.addHole(innerBounds.map(LatLngDto::toLatLng)) } }
    }
}
