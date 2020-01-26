package ua.vlad.huntinggrounds.dagger2

import android.app.Application
import ua.vlad.huntinggrounds.dagger2.module.AppModule
import ua.vlad.huntinggrounds.dagger2.module.LocalDatabaseModule
import ua.vlad.huntinggrounds.dagger2.module.ModelsModule
import ua.vlad.huntinggrounds.dagger2.module.RemoteAPIModule

class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .localDatabaseModule(LocalDatabaseModule())
            .remoteAPIModule(RemoteAPIModule())
            .modelsModule(ModelsModule())
            .build()
    }

}
