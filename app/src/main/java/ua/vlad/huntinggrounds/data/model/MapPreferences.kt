package ua.vlad.huntinggrounds.data.model

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

class MapPreferences(context: Context) {

    companion object {
        const val TYPE = "MAP"
    }

    enum class MapCamera(val title: String, val defVal: Float) {
        LATITUDE("lat", 49.482354f),
        LONGITUDE("lng", 30.821903f),
        ZOOM("zoom", 5f),
        BEARING("bearing", 0f),
        TILT("tilt", 0f)
    }

    enum class Map(val title: String, val defVal: Int) {
        TYPE("type", GoogleMap.MAP_TYPE_NORMAL),
        ACTIVE_GROUND("active_ground", 0)
    }

    private var preferences = context.getSharedPreferences(TYPE, Context.MODE_PRIVATE)

    fun saveCameraPosition(cameraPosition: CameraPosition) {
        val editor = preferences.edit()
        editor.putFloat(MapCamera.LATITUDE.title, cameraPosition.target.latitude.toFloat())
        editor.putFloat(MapCamera.LONGITUDE.title, cameraPosition.target.longitude.toFloat())
        editor.putFloat(MapCamera.ZOOM.title, cameraPosition.zoom)
        editor.putFloat(MapCamera.BEARING.title, cameraPosition.bearing)
        editor.putFloat(MapCamera.TILT.title, cameraPosition.tilt)
        editor.apply()
    }

    fun restoreCameraPosition(): CameraPosition {
        return CameraPosition.Builder()
            .target(
                LatLng(
                    preferences.getFloat(MapCamera.LATITUDE.title, MapCamera.LATITUDE.defVal).toDouble(),
                    preferences.getFloat(MapCamera.LONGITUDE.title, MapCamera.LONGITUDE.defVal).toDouble()
                )
            )
            .zoom(preferences.getFloat(MapCamera.ZOOM.title, MapCamera.ZOOM.defVal))
            .bearing(preferences.getFloat(MapCamera.BEARING.title, MapCamera.BEARING.defVal))
            .tilt(preferences.getFloat(MapCamera.TILT.title, MapCamera.TILT.defVal))
            .build()
    }

    fun saveMapType(mapType: Int) {
        val editor = preferences.edit()
        editor.putInt(Map.TYPE.title, mapType)
        editor.apply()
    }

    fun restoreMapType(): Int {
        return preferences.getInt(Map.TYPE.title, Map.TYPE.defVal)
    }

    fun saveActiveGround(groundId: Int) {
        val editor = preferences.edit()
        editor.putInt(Map.ACTIVE_GROUND.title, groundId)
        editor.apply()
    }

    fun restoreActiveGround(): Int {
        return preferences.getInt(Map.ACTIVE_GROUND.title, Map.ACTIVE_GROUND.defVal)
    }
}
