package ua.vlad.huntinggrounds.dagger2.module

import android.app.Application
import dagger.Module
import dagger.Provides
import ua.vlad.huntinggrounds.data.GroundsRepository
import ua.vlad.huntinggrounds.data.model.LocationRequestModel
import ua.vlad.huntinggrounds.data.model.MapPreferences

@Module
class ModelsModule {

    @Provides
    internal fun providesGroundsRepository(): GroundsRepository {
        return GroundsRepository()
    }

    @Provides
    internal fun providesLocationRequestModel(): LocationRequestModel {
        return LocationRequestModel()
    }

    @Provides
    internal fun providesMapPreferences(application: Application): MapPreferences {
        return MapPreferences(application)
    }

}
