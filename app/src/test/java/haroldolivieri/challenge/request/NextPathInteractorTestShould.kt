package haroldolivieri.challenge.request

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import haroldolivieri.challenge.fetchingbutton.gateway.Gateway
import haroldolivieri.challenge.fetchingbutton.interactor.NextPathInteractor


class NextPathInteractorTestShould {

    private val nextPathSubject = Sing
    private val gateway: Gateway = mock {
        on {fetchCorrectPath()}.thenReturn(nextPathSubject)
    }

    private val interactor = NextPathInteractor(gateway)
}