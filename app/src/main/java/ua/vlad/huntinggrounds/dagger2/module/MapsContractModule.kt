package ua.vlad.huntinggrounds.dagger2.module

import dagger.Module
import dagger.Provides
import ua.vlad.huntinggrounds.contract.MapsContract
import ua.vlad.huntinggrounds.presenter.MapsPresenter


@Module
class MapsContractModule(private val mView: MapsContract.View) {

    @Provides
    fun providesMapsView(): MapsContract.View {
        return mView
    }

    @Provides
    fun providesMapsPresenter(view: MapsContract.View): MapsPresenter {
        return MapsPresenter(view)
    }

}
