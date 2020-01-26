package ua.vlad.huntinggrounds.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ua.vlad.huntinggrounds.data.model.Oblast

@Dao
interface OblastDAO {

    @Query("DELETE FROM oblast")
    fun deleteAll()

    @Insert
    fun insert(oblasts: Set<Oblast>)

}
