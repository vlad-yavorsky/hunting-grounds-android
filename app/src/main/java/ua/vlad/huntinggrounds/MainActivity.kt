package ua.vlad.huntinggrounds

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import ua.vlad.huntinggrounds.view.CatalogFragment
import ua.vlad.huntinggrounds.view.FavoritesFragment
import ua.vlad.huntinggrounds.view.MapsFragment
import ua.vlad.huntinggrounds.view.SettingsFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            showMapFragment()
            navView.setCheckedItem(R.id.nav_map)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_map -> showMapFragment()
            R.id.nav_catalog -> showCatalogFragment()
            R.id.nav_favorites -> showFavoritesFragment()
            R.id.nav_settings -> showSettingsFragment()
            R.id.nav_share -> {}
            R.id.nav_send -> {}
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
//            .addToBackStack(null)
            .commit()
    }

    private fun showMapFragment() {
        changeFragment(MapsFragment())
    }

    private fun showCatalogFragment() {
        changeFragment(CatalogFragment())
    }

    private fun showFavoritesFragment() {
        changeFragment(FavoritesFragment())
    }

    private fun showSettingsFragment() {
        changeFragment(SettingsFragment())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        val states = LocationSettingsStates.fromIntent(data)
        if (requestCode == MapsFragment.GPS_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> showToast("RESULT_OK")
                Activity.RESULT_CANCELED -> showToast("RESULT_CANCELED")
                else -> {}
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

}
