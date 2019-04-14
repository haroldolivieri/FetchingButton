package haroldolivieri.challenge

import android.app.Application
import haroldolivieri.challenge.di.ApplicationComponent
import haroldolivieri.challenge.di.DaggerApplicationComponent


class App : Application() {

    val component by lazy { applicationComponent }

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder()
                .application(this)
                .build()
    }

    private companion object {
        lateinit var applicationComponent: ApplicationComponent
    }
}