package haroldolivieri.challenge.fetchingbutton.interactor

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import haroldolivieri.challenge.RxTestRule
import haroldolivieri.challenge.fetchingbutton.gateway.Gateway
import haroldolivieri.challenge.model.CorrectPath
import io.reactivex.subjects.SingleSubject
import org.junit.Rule
import org.junit.Test


class NextPathInteractorTestShould {

    @Rule
    @JvmField
    val rule = RxTestRule()

    private val nextPathSubject = SingleSubject.create<CorrectPath>()
    private val gateway : Gateway = mock {
        on {fetchCorrectPath()}.thenReturn(nextPathSubject)
    }

    private val interactor = NextPathInteractor(gateway)

    @Test
    fun `call gateway when events() is called first time`() {
        interactor.events().test()

        verify(gateway).fetchCorrectPath()
    }

    @Test
    fun `do not call gateway when events() was already called before`() {
        nextPathSubject.onSuccess(CorrectPath("next_path"))

        interactor.events().test()
        reset(gateway)

        interactor.events().test()
        verify(gateway, never()).fetchCorrectPath()
    }
}