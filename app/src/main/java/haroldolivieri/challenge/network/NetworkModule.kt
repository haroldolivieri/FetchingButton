package haroldolivieri.challenge.network

import dagger.Module
import dagger.Provides
import haroldolivieri.challenge.di.ApplicationScope
import haroldolivieri.challenge.di.IOScheduler
import haroldolivieri.challenge.fetchingbutton.gateway.Gateway
import io.reactivex.Scheduler
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TIMEOUT = 20L
private const val BASE_URL = "http://10.0.2.2:8000"

@Module
class NetworkModule {

    @Provides
    @ApplicationScope
    fun provideProductService(retrofit: Retrofit): Gateway.Api =
            retrofit.create(Gateway.Api::class.java)

    @Provides
    @ApplicationScope
    fun provideOkHttpClient(builder: OkHttpClient.Builder): OkHttpClient = builder.build()

    @Provides
    @ApplicationScope
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @ApplicationScope
    fun provideOkHttpClientBuilder(
            httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient.Builder =
            OkHttpClient().newBuilder()
                    .addInterceptor(httpLoggingInterceptor)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)

    @Provides
    @ApplicationScope
    fun provideRetrofit(
            okHttpClient: OkHttpClient,
            @IOScheduler scheduler: Scheduler
    ): Retrofit =
            Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(scheduler))
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
}