package ua.vlad.huntinggrounds.data.model

import androidx.room.*
import java.util.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = Oblast::class,
        parentColumns = ["id"],
        childColumns = ["oblastId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["oblastId"])]
)
class Ground {

    @PrimaryKey(autoGenerate = false)
    var id: Long? = null
    var name: String? = null
    var alias: String? = null
    var created: Date? = null
    var kml: String? = null
    var oblastId: Long? = null
    var area: Double? = null
    var markerCoords: String? = null
    var address: String? = null
    var description: String? = null

    @Ignore var oblast: Oblast? = null
//    @Ignore var polygons: List<PolygonOptions>? = null
//    @Ignore var marker: LatLng? = null
}
