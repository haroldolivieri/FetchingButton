package haroldolivieri.challenge.fetchingbutton.gateway

import haroldolivieri.challenge.model.CorrectPath
import haroldolivieri.challenge.model.ResponseCode
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url
import javax.inject.Inject


class Gateway @Inject constructor(
    private val api: Api
) {

    fun fetchCorrectPath() = api.fetchCorrectPath()
    fun fetchUUID(url: String) = api.fetchUUID(url)

    interface Api {
        @GET("/")
        fun fetchCorrectPath(): Single<CorrectPath>

        @GET
        fun fetchUUID(@Url url: String): Single<ResponseCode>
    }
}