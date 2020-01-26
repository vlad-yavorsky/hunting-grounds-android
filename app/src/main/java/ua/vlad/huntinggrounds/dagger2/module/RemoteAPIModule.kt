package ua.vlad.huntinggrounds.dagger2.module

import android.app.Application
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.vlad.huntinggrounds.data.remote.HuntingGroundsAPI
import ua.vlad.huntinggrounds.view.SettingsFragment


@Module
class RemoteAPIModule {

    companion object {
        const val DEFAULT_API_HOST = "http://192.168.1.102:8080/api/"
    }

    @Provides
    internal fun provideRetrofitInterface(application: Application): Retrofit {
        val preferences = PreferenceManager.getDefaultSharedPreferences(application)
        var baseUrl = preferences.getString(SettingsFragment.KEY_API_HOST, DEFAULT_API_HOST)!!
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/"
        }
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Provides
    internal fun provideRemoteApi(retrofit: Retrofit): HuntingGroundsAPI {
        return retrofit.create(HuntingGroundsAPI::class.java)
    }

}
