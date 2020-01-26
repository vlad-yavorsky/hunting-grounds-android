package ua.vlad.huntinggrounds.util

import com.google.android.gms.maps.model.*

enum class Color(val value: Int) {
    BLACK_ARGB(0x00000000),
    WHITE_ARGB(0x00ffffff),
    GREEN_ARGB(0x00388E3C),
    PURPLE_ARGB(0x00f81C784),
    ORANGE_ARGB(0x77F57F17),
    BLUE_ARGB(0x771d58b6),
    RED_ARGB(0x77d90000)
}

enum class Width(val value: Float) {
    POLYGON_STROKE(2f)
}

fun PolygonOptions.setDefaultStyle(): PolygonOptions {
    strokeWidth(Width.POLYGON_STROKE.value)
    strokeColor(Color.BLUE_ARGB.value)
    fillColor(Color.BLUE_ARGB.value)
    return this
}

fun Polygon.setRedColor() {
    strokeColor = Color.RED_ARGB.value
    fillColor = Color.RED_ARGB.value
}

fun Polygon.setBlueColor() {
    strokeColor = Color.BLUE_ARGB.value
    fillColor = Color.BLUE_ARGB.value
}

fun MarkerOptions.setDefaultStyle(): MarkerOptions {
    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
    return this
}

fun Marker.setRedIcon() {
    setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
}

fun Marker.setBlueIcon() {
    setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
}
