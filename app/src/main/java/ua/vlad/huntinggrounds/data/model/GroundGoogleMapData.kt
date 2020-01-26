package ua.vlad.huntinggrounds.data.model

import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polygon

class GroundGoogleMapData {
    var id: Int? = null
    val polygons = mutableListOf<Polygon>()
    var marker: Marker? = null
    var name: String? = null
    var area: Double? = null
    var oblast: Oblast? = null
}
