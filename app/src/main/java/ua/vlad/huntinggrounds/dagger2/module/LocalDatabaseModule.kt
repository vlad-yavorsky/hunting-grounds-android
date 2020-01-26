package ua.vlad.huntinggrounds.dagger2.module

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ua.vlad.huntinggrounds.data.local.AppDatabase
import ua.vlad.huntinggrounds.data.local.LocalDataSource

@Module
class LocalDatabaseModule {

    companion object {
        private const val APP_DB_NAME = "hunting-grounds.db"
    }

    @Provides
    internal fun providesAppDatabase(application: Application): AppDatabase {
        return Room
            .databaseBuilder(application, AppDatabase::class.java, APP_DB_NAME)
            .build()
    }

    @Provides
    internal fun localRepository(appDatabase: AppDatabase): LocalDataSource {
        return LocalDataSource(appDatabase)
    }

}
