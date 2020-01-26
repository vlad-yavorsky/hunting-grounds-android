package ua.vlad.huntinggrounds.presenter

import android.app.Activity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ua.vlad.huntinggrounds.contract.MapsContract
import ua.vlad.huntinggrounds.dagger2.App
import ua.vlad.huntinggrounds.data.GroundsForViewDataSource
import ua.vlad.huntinggrounds.data.GroundsRepository
import ua.vlad.huntinggrounds.data.model.GroundForView
import ua.vlad.huntinggrounds.data.model.GroundGoogleMapData
import ua.vlad.huntinggrounds.data.model.LocationRequestModel
import ua.vlad.huntinggrounds.data.model.MapPreferences
import ua.vlad.huntinggrounds.util.*
import javax.inject.Inject


class MapsPresenter (private val view: MapsContract.View): MapsContract.Presenter {

    @Inject lateinit var groundsRepository: GroundsRepository
    @Inject lateinit var locationRequestModel: LocationRequestModel
    @Inject lateinit var mapPreferences: MapPreferences

    private var prevGoogleMapData: GroundGoogleMapData? = null

    init {
        App.appComponent.inject(this)
    }

    override fun getGrounds(map: GoogleMap) {
        groundsRepository.getGroundsForView(object : GroundsForViewDataSource.Callback {
            override fun onReceived(grounds: List<GroundForView>) {
                onGroundsReceived(grounds, map)
            }

            override fun onFailure(errorResource: Int, t: Throwable?) {
                onError(errorResource, t)
            }
        })
    }

    private fun onGroundsReceived(grounds: List<GroundForView>, map: GoogleMap) {
        GlobalScope.launch {
            grounds.forEach { ground ->
                val groundMapData = GroundGoogleMapData()
                groundMapData.id = ground.id
                groundMapData.name = ground.name
                groundMapData.area = Math.round(ground.area / 10.0) / 100.0
                groundMapData.oblast = ground.oblast
                val polygons = KmlDocument(ground.kml).extractPolygons()
                polygons.forEach { polygonOptions ->
                    GlobalScope.launch(Dispatchers.Main) {
                        map.addPolygon(polygonOptions).also { polygon ->
                            groundMapData.polygons.add(polygon)
                            polygon.tag = groundMapData
                        }
                    }
                }
                if (!ground.markerCoords.isBlank()) {
                    val markerCoords = ground.markerCoords.split(",".toRegex())
                    val markerLatLng = LatLng(markerCoords[0].toDouble(), markerCoords[1].toDouble())
                    GlobalScope.launch(Dispatchers.Main) {
                        map.addMarker(
                            MarkerOptions().setDefaultStyle().position(markerLatLng)
                        ).also { marker ->
                            groundMapData.marker = marker
                            marker.tag = groundMapData
                        }
                    }
                }

                GlobalScope.launch(Dispatchers.Main) {
                    val savedId = mapPreferences.restoreActiveGround()
                    if (savedId != MapPreferences.Map.ACTIVE_GROUND.defVal && savedId == ground.id) {
                        setActiveGround(groundMapData, map, false)
                    }
                }
            }
        }
    }

    private fun onError(errorCode: Int, t: Throwable?) {
        view.showToast(errorCode, t)
    }

    private fun switchColorsOfGrounds(groundGoogleMapData: GroundGoogleMapData?) {
        if (prevGoogleMapData == groundGoogleMapData) {
            return
        }
        prevGoogleMapData?.marker?.setBlueIcon()
        prevGoogleMapData?.polygons?.forEach { it.setBlueColor() }
        prevGoogleMapData = groundGoogleMapData
        if (prevGoogleMapData != null) {
            prevGoogleMapData?.polygons?.forEach { it.setRedColor() }
            prevGoogleMapData?.marker?.setRedIcon()
        }
    }

    override fun setActiveGround(selectedMapData: GroundGoogleMapData, map: GoogleMap, moveToMarker: Boolean): Boolean {
        if (selectedMapData.marker != null && moveToMarker) {
            map.animateCamera(CameraUpdateFactory.newLatLng(selectedMapData.marker!!.position), 500, null)
        }
        view.showInfoWindow(selectedMapData)
        saveActiveGround(selectedMapData.id!!)
        if (selectedMapData == prevGoogleMapData) {
            return true
        }
        switchColorsOfGrounds(selectedMapData)
        return true
    }

    private fun saveActiveGround(groundId: Int) {
        mapPreferences.saveActiveGround(groundId)
    }

    override fun removeActiveGround() {
        mapPreferences.saveActiveGround(MapPreferences.Map.ACTIVE_GROUND.defVal)
        switchColorsOfGrounds(null)
    }

    // todo: inject activity using dagger 2
    override fun requestLocation(activity: Activity) {
        locationRequestModel.requestLocation(activity)
    }

    override fun saveCameraPosition(cameraPosition: CameraPosition) {
        mapPreferences.saveCameraPosition(cameraPosition)
    }

    override fun restoreCameraPosition(): CameraPosition {
        return mapPreferences.restoreCameraPosition()
    }

    override fun saveMapType(mapType: Int) {
        mapPreferences.saveMapType(mapType)
    }

    override fun restoreMapType(): Int {
        return mapPreferences.restoreMapType()
    }
}
