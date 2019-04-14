package haroldolivieri.challenge.fetchingbutton.interactor

import haroldolivieri.challenge.di.ApplicationScope
import haroldolivieri.challenge.fetchingbutton.gateway.Gateway
import haroldolivieri.challenge.model.CorrectPath
import haroldolivieri.challenge.request.Event
import haroldolivieri.challenge.request.InMemoryRequest
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

@ApplicationScope
class NextPathInteractor @Inject constructor(private val gateway: Gateway) {

    private var requestPath: InMemoryRequest<CorrectPath>? = null

    fun events(): Observable<Event<CorrectPath>> {
        if (requestPath == null) {
            requestPath = InMemoryRequest(source())
        }

        return requestPath!!.events()
    }

    fun retry() {
        requestPath?.retry()
    }

    private fun source(): Single<CorrectPath> = gateway.fetchCorrectPath()
}
