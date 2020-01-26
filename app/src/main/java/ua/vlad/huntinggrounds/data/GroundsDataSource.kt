package ua.vlad.huntinggrounds.data

import ua.vlad.huntinggrounds.data.model.Ground

interface GroundsDataSource {

    fun getGrounds(callback: Callback)

    interface Callback {
        fun onReceived(grounds: List<Ground>)
        fun onFailure(errorResource: Int, t: Throwable?)
    }

}
