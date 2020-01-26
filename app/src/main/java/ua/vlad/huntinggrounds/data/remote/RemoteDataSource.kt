package ua.vlad.huntinggrounds.data.remote

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.vlad.huntinggrounds.R
import ua.vlad.huntinggrounds.data.GroundsDataSource
import ua.vlad.huntinggrounds.data.model.Ground
import ua.vlad.huntinggrounds.data.model.GroundDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val remoteAPI: HuntingGroundsAPI) : GroundsDataSource {

    override fun getGrounds(callback: GroundsDataSource.Callback) {
        remoteAPI.getGrounds().enqueue(object : Callback<List<GroundDto>> {
            override fun onResponse(call: Call<List<GroundDto>>, response: Response<List<GroundDto>>) {
                if (response.body() != null) {
                    val grounds = mutableListOf<Ground>()
                    response.body()!!.forEach { groundDto ->
                        grounds.add(groundDto.toGround())
                    }
                    callback.onReceived(grounds)
                } else {
                    callback.onFailure(R.string.no_data_available, null)
                }
            }

            override fun onFailure(call: Call<List<GroundDto>>, t: Throwable) {
                callback.onFailure(R.string.request_failure, t)
            }
        })
    }

}
