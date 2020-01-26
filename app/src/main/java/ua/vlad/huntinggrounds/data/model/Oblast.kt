package ua.vlad.huntinggrounds.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Oblast(

    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    val id: Long,

    @SerializedName("name")
    val name: String
)
