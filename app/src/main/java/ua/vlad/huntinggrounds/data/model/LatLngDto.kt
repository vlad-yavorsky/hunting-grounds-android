package ua.vlad.huntinggrounds.data.model

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

data class LatLngDto(
    @SerializedName("lat") val lat: Double? = null,
    @SerializedName("lng") val lng: Double? = null
) {
    fun toLatLng(): LatLng {
        return LatLng(lat!!, lng!!)
    }
}
