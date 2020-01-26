package ua.vlad.huntinggrounds.view

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polygon
import ua.vlad.huntinggrounds.MainActivity
import ua.vlad.huntinggrounds.R
import ua.vlad.huntinggrounds.contract.MapsContract
import ua.vlad.huntinggrounds.dagger2.App
import ua.vlad.huntinggrounds.dagger2.module.MapsContractModule
import ua.vlad.huntinggrounds.data.model.GroundGoogleMapData
import ua.vlad.huntinggrounds.presenter.MapsPresenter
import javax.inject.Inject


class MapsFragment : Fragment(), MapsContract.View, OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnPolygonClickListener,
    GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMyLocationButtonClickListener {

    @Inject lateinit var presenter: MapsPresenter

    init {
        App.appComponent
            .add(MapsContractModule(this))
            .inject(this)
    }

    private lateinit var mMap: GoogleMap

    private lateinit var infoWindow: LinearLayout
    private lateinit var titleView: TextView
    private lateinit var areaView: TextView
    private lateinit var oblastView: TextView
    private lateinit var areaLineView: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar!!.setTitle(R.string.menu_map)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_maps, container, false)
        val mapFragment = childFragmentManager.findFragmentByTag("SupportMapFragment") as SupportMapFragment
        mapFragment.getMapAsync(this)
        titleView = view.findViewById(R.id.title) as TextView
        areaView = view.findViewById(R.id.area) as TextView
        oblastView = view.findViewById(R.id.oblast) as TextView
        areaLineView = view.findViewById(R.id.areaLine) as LinearLayout
        infoWindow = view.findViewById(R.id.info_window)
        infoWindow.setOnClickListener { } // need click listener to prevent onMapClick() todo: fill click listener
        return view
    }

    override fun onCameraIdle() {
        presenter.saveCameraPosition(mMap.cameraPosition)
    }

    override fun onPolygonClick(polygon: Polygon?) {
        presenter.setActiveGround(polygon!!.tag as GroundGoogleMapData, mMap, false)
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        return presenter.setActiveGround(marker!!.tag as GroundGoogleMapData, mMap, true)
    }

    override fun showInfoWindow(mapData: GroundGoogleMapData) {
        titleView.text = mapData.name
        areaView.text = mapData.area.toString()
        oblastView.text = mapData.oblast?.name
        areaLineView.visibility = if (mapData.area == 0.0) View.GONE else View.VISIBLE
        infoWindow.visibility = View.VISIBLE
    }

    override fun onMapClick(latLng: LatLng?) {
        infoWindow.visibility = View.GONE
        presenter.removeActiveGround()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnCameraIdleListener(this)
        mMap.setOnPolygonClickListener(this)
        mMap.setOnMarkerClickListener(this)
        mMap.setOnMapClickListener(this)
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(presenter.restoreCameraPosition()))
        setMapType(presenter.restoreMapType())

        if (isFineLocationPermissionGranted()) {
            mMap.isMyLocationEnabled = true
            mMap.setOnMyLocationButtonClickListener(this)
        } else {
            askFineLocationPermission(activity!!)
        }

        presenter.getGrounds(mMap)
    }

    /* Options Menu */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map, menu)
        checkMapTypeButton(menu, presenter.restoreMapType())
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun checkMapTypeButton(menu: Menu, mapType: Int) {
        when(mapType) {
            GoogleMap.MAP_TYPE_NORMAL -> menu.findItem(R.id.map_type_normal).isChecked = true
            GoogleMap.MAP_TYPE_HYBRID -> menu.findItem(R.id.map_type_hybrid).isChecked = true
            GoogleMap.MAP_TYPE_TERRAIN -> menu.findItem(R.id.map_type_terrain).isChecked = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.map_type_normal -> {
                item.isChecked = true
                setMapType(GoogleMap.MAP_TYPE_NORMAL)
            }
            R.id.map_type_hybrid -> {
                item.isChecked = true
                setMapType(GoogleMap.MAP_TYPE_HYBRID)
            }
            R.id.map_type_terrain -> {
                item.isChecked = true
                setMapType(GoogleMap.MAP_TYPE_TERRAIN)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun setMapType(newType: Int) {
        if (mMap.mapType != newType) {
            mMap.mapType = newType
            presenter.saveMapType(mMap.mapType)
        }
    }

    /* Access Fine Location Permission */
    companion object {
        const val ACCESS_FINE_LOCATION_REQUEST_CODE = 1000
        const val GPS_REQUEST_CODE = 1001
    }

    private fun isFineLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun askFineLocationPermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            ACCESS_FINE_LOCATION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ACCESS_FINE_LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.isMyLocationEnabled = true
                } else {
                    showToast(R.string.location_permission_denied)
                }
            }
        }
    }

    // todo: move camera to gps location
    override fun onMyLocationButtonClick(): Boolean {
        presenter.requestLocation(activity!!)
        return true
    }

    override fun showToast(resource: Int) {
        showToast(resource, null)
    }

    override fun showToast(resource: Int, t: Throwable?) {
        Toast.makeText(context, "${resources.getText(resource)}" + if (t != null) ": $t" else "", Toast.LENGTH_SHORT).show()
    }

}
