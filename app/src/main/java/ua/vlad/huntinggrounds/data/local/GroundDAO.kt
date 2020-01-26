package ua.vlad.huntinggrounds.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ua.vlad.huntinggrounds.data.model.Ground
import ua.vlad.huntinggrounds.data.model.GroundForView

@Dao
interface GroundDAO {

    @Query("SELECT g.id, g.name, g.kml, g.area, g.markerCoords, o.id as obl_id, o.name as obl_name FROM ground as g, oblast as o WHERE g.oblastId = o.id")
    fun getAll(): List<GroundForView>

    @Query("DELETE FROM ground")
    fun deleteAll()

    @Insert
    fun insert(grounds: List<Ground>)

}
