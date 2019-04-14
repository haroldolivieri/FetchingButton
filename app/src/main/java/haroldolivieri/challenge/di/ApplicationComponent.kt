package haroldolivieri.challenge.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import haroldolivieri.challenge.fetchingbutton.presentation.MainActivity
import haroldolivieri.challenge.network.NetworkModule
import javax.inject.Scope

@ApplicationScope
@Component(
    modules = [
        ApplicationModule::class,
        NetworkModule::class
    ]
)
interface ApplicationComponent {
    fun inject(activity: MainActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope