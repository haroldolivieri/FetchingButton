package haroldolivieri.challenge.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Qualifier

@Module
class ApplicationModule {

    @Provides
    @ApplicationScope
    fun provideContext(application: Application): Context = application

    @Provides
    @IOScheduler
    @ApplicationScope
    fun providesScheduler(): Scheduler = Schedulers.io()

    @Provides
    @ApplicationScope
    fun provideSharedPreferences(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IOScheduler