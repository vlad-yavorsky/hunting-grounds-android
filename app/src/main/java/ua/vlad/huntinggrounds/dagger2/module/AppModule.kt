package ua.vlad.huntinggrounds.dagger2.module

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: Application) {

    @Provides
    fun providesApplication(): Application {
        return application
    }

}
