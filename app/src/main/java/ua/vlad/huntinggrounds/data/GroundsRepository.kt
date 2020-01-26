package ua.vlad.huntinggrounds.data

import ua.vlad.huntinggrounds.dagger2.App
import ua.vlad.huntinggrounds.data.local.LocalDataSource
import ua.vlad.huntinggrounds.data.model.Ground
import ua.vlad.huntinggrounds.data.model.GroundForView
import ua.vlad.huntinggrounds.data.model.Oblast
import ua.vlad.huntinggrounds.data.remote.RemoteDataSource
import javax.inject.Inject

class GroundsRepository : GroundsForViewDataSource {

    @Inject lateinit var localDS: LocalDataSource
    @Inject lateinit var remoteDS: RemoteDataSource

    init {
        App.appComponent.inject(this)
    }

    override fun getGroundsForView(callback: GroundsForViewDataSource.Callback) {
        loadFromLocal(callback)
    }

    private fun loadFromLocal(callback: GroundsForViewDataSource.Callback) {
        localDS.getGroundsForView(object : GroundsForViewDataSource.Callback {
            override fun onReceived(grounds: List<GroundForView>) {
                callback.onReceived(grounds)
            }

            override fun onFailure(errorResource: Int, t: Throwable?) {
                updateDatabase(callback)
            }
        })
    }

    fun updateDatabase(callback: GroundsForViewDataSource.Callback) {
        remoteDS.getGrounds(object : GroundsDataSource.Callback {
            override fun onReceived(grounds: List<Ground>) {
                val oblasts = mutableSetOf<Oblast>()
                grounds.forEach { ground ->
                    oblasts.add(ground.oblast!!)
                }
                localDS.saveOblastsAndGrounds(oblasts, grounds)
                loadFromLocal(callback)
            }

            override fun onFailure(errorResource: Int, t: Throwable?) {
                callback.onFailure(errorResource, t)
            }
        })
    }

}
