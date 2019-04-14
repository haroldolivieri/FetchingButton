package haroldolivieri.challenge.fetchingbutton.interactor

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import haroldolivieri.challenge.RxTestRule
import haroldolivieri.challenge.fetchingbutton.gateway.Gateway
import haroldolivieri.challenge.model.ResponseCode
import io.reactivex.subjects.SingleSubject
import org.junit.Rule
import org.junit.Test

class ResponseCodeInteractorTestShould {

    @Rule
    @JvmField
    val rule = RxTestRule()

    private val responseCodeSubject = SingleSubject.create<ResponseCode>()
    private val gateway: Gateway = mock {
        on { fetchUUID(any()) }.thenReturn(responseCodeSubject)
    }

    private val interactor = ResponseCodeInteractor(gateway)

    @Test
    fun `call fetchUUID() on gateway when fetchNewResponseCode() is called`() {
        val url = "next_path"
        interactor.fetchNewResponseCode(url)

        verify(gateway).fetchUUID(url)
    }
}