package ua.vlad.huntinggrounds.dagger2

import dagger.Subcomponent
import ua.vlad.huntinggrounds.dagger2.module.MapsContractModule
import ua.vlad.huntinggrounds.view.MapsFragment

@Subcomponent(
    modules = [MapsContractModule::class]
)
interface MapsComponent {

    fun inject(fragment: MapsFragment)

}
