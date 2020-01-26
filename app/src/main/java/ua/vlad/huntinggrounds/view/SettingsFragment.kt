package ua.vlad.huntinggrounds.view

import android.os.Bundle
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import ua.vlad.huntinggrounds.MainActivity
import ua.vlad.huntinggrounds.R
import ua.vlad.huntinggrounds.dagger2.module.RemoteAPIModule
import ua.vlad.huntinggrounds.data.GroundsForViewDataSource
import ua.vlad.huntinggrounds.data.GroundsRepository
import ua.vlad.huntinggrounds.data.model.GroundForView
import java.text.SimpleDateFormat
import java.util.*


class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener,
    Preference.OnPreferenceClickListener {

    companion object {
        const val KEY_UPDATE = "key_update"
        const val KEY_API_HOST = "key_api_host"
    }

    private lateinit var update: Preference
    private lateinit var apiHost: EditTextPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)

        update = findPreference(KEY_UPDATE)!!
        update.onPreferenceClickListener = this
        update.summary = "Last update: " + SimpleDateFormat("dd/MM/yyyy", Locale.UK).format(Date()) // todo: save last update date

        apiHost = findPreference(KEY_API_HOST)!!
        apiHost.onPreferenceChangeListener = this
        if (apiHost.text.isNullOrBlank()) {
            apiHost.text = RemoteAPIModule.DEFAULT_API_HOST
        }
        apiHost.summary = apiHost.text
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar!!.setTitle(R.string.menu_settings)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        when (preference!!.key) {
            KEY_API_HOST -> {
                if (HttpUrl.parse(newValue.toString()) == null) {
                    showToast(R.string.invalid_url)
                    return false
                }
                apiHost.summary = newValue.toString()
            }
        }
        return true
    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        when (preference!!.key) {
            KEY_UPDATE -> {
                GroundsRepository().updateDatabase(object : GroundsForViewDataSource.Callback {
                    override fun onReceived(grounds: List<GroundForView>) {
//                        GlobalScope.launch {
//                            delay(3000)
//                        }
                        GlobalScope.launch(Dispatchers.Main) {
                            showToast(R.string.update_success)
                        }
                    }

                    override fun onFailure(errorResource: Int, t: Throwable?) {
                        GlobalScope.launch(Dispatchers.Main) {
                            showToast(errorResource, t)
                        }
                    }
                })
            }
        }
        return true
    }

    private fun showToast(resource: Int) {
        showToast(resource, null)
    }

    private fun showToast(resource: Int, t: Throwable?) {
        // todo: need to show Toast even when user changes display orientation or started another fragment
        if (context != null) {
            Toast.makeText(context, "${resources.getText(resource)}" + if (t != null) ": $t" else "", Toast.LENGTH_SHORT).show()
        }
    }

}
