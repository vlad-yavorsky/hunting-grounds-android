package ua.vlad.huntinggrounds.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ua.vlad.huntinggrounds.data.model.GroundDto

interface HuntingGroundsAPI {

    @GET("grounds")
    fun getGrounds(): Call<List<GroundDto>>

    @GET("ground/{id}")
    fun getGround(@Path("id") id: Long): Call<GroundDto>

}
