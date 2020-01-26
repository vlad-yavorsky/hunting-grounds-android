package ua.vlad.huntinggrounds.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ua.vlad.huntinggrounds.data.local.converter.DateTypeConverter
import ua.vlad.huntinggrounds.data.model.Ground
import ua.vlad.huntinggrounds.data.model.Oblast

@Database(entities = [Ground::class, Oblast::class], version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groundDAO(): GroundDAO
    abstract fun oblastDAO(): OblastDAO
}
