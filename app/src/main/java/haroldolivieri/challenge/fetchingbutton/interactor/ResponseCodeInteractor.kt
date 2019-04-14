package haroldolivieri.challenge.fetchingbutton.interactor

import haroldolivieri.challenge.fetchingbutton.gateway.Gateway
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class ResponseCodeInteractor @Inject constructor(private val gateway: Gateway) {
    fun fetchNewResponseCode(url: String) =
        gateway.fetchUUID(url)
            .observeOn(AndroidSchedulers.mainThread())
}