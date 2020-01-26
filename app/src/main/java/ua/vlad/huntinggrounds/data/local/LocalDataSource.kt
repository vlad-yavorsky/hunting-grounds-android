package ua.vlad.huntinggrounds.data.local

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ua.vlad.huntinggrounds.data.GroundsForViewDataSource
import ua.vlad.huntinggrounds.data.model.Ground
import ua.vlad.huntinggrounds.data.model.Oblast
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val appDatabase: AppDatabase) : GroundsForViewDataSource {

    override fun getGroundsForView(callback: GroundsForViewDataSource.Callback) {
        GlobalScope.launch {
            //            callback.onFailure(-1)
            val cachedGrounds = appDatabase.groundDAO().getAll()
            if (cachedGrounds.isNotEmpty()) {
                callback.onReceived(cachedGrounds)
            } else {
                callback.onFailure(0, null)
            }
        }
    }

    fun saveOblastsAndGrounds(oblasts: Set<Oblast>, grounds: List<Ground>) {
        GlobalScope.launch {
            appDatabase.oblastDAO().deleteAll()
            appDatabase.oblastDAO().insert(oblasts)
            appDatabase.groundDAO().deleteAll()
            appDatabase.groundDAO().insert(grounds)
        }
    }

}
