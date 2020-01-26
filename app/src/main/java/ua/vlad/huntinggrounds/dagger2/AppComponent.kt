package ua.vlad.huntinggrounds.dagger2

import dagger.Component
import ua.vlad.huntinggrounds.dagger2.module.*
import ua.vlad.huntinggrounds.data.GroundsRepository
import ua.vlad.huntinggrounds.presenter.MapsPresenter


@Component(modules = [AppModule::class, LocalDatabaseModule::class, RemoteAPIModule::class, ModelsModule::class])
interface AppComponent {

    fun inject(repository: GroundsRepository)

    fun inject(presenter: MapsPresenter)

    fun add(module: MapsContractModule): MapsComponent

}
