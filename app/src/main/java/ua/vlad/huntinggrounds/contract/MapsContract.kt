package ua.vlad.huntinggrounds.contract

import android.app.Activity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import ua.vlad.huntinggrounds.data.model.GroundGoogleMapData

interface MapsContract {
    interface View {
        fun showInfoWindow(mapData: GroundGoogleMapData)
        fun showToast(resource: Int)
        fun showToast(resource: Int, t: Throwable?)
    }
    interface Presenter {
        fun getGrounds(map: GoogleMap)
        fun setActiveGround(selectedMapData: GroundGoogleMapData, map: GoogleMap, moveToMarker: Boolean): Boolean
        fun removeActiveGround()
        fun requestLocation(activity: Activity)
        fun saveCameraPosition(cameraPosition: CameraPosition)
        fun restoreCameraPosition(): CameraPosition
        fun saveMapType(mapType: Int)
        fun restoreMapType(): Int
    }
}
