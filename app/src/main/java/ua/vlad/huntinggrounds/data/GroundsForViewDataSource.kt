package ua.vlad.huntinggrounds.data

import ua.vlad.huntinggrounds.data.model.GroundForView

interface GroundsForViewDataSource {

    fun getGroundsForView(callback: Callback)

    interface Callback {
        fun onReceived(grounds: List<GroundForView>)
        fun onFailure(errorResource: Int, t: Throwable?)
    }

}
