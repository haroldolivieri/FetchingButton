package haroldolivieri.challenge.fetchingbutton.presentation

import com.nhaarman.mockito_kotlin.*
import haroldolivieri.challenge.R
import haroldolivieri.challenge.RxTestRule
import haroldolivieri.challenge.StringResolver
import haroldolivieri.challenge.fetchingbutton.interactor.NextPathInteractor
import haroldolivieri.challenge.fetchingbutton.interactor.ResponseCodeInteractor
import haroldolivieri.challenge.model.CorrectPath
import haroldolivieri.challenge.model.ResponseCode
import haroldolivieri.challenge.fetchingbutton.CounterProvider
import haroldolivieri.challenge.request.Event
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.SingleSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val URL = "URL"

class MainPresenterTestShould {

    @Rule
    @JvmField
    val rule = RxTestRule()

    private val nextPathSubject =
        BehaviorSubject.createDefault(Event.data(CorrectPath(URL)))
    private val nextPathInteractor: NextPathInteractor = mock {
        on { events() }.thenReturn(nextPathSubject)
    }

    private val responseCodeSubject = SingleSubject.create<ResponseCode>()
    private val responseCodeInteractor: ResponseCodeInteractor = mock {
        on { fetchNewResponseCode(URL) }.thenReturn(responseCodeSubject)
    }

    private val counterProvider: CounterProvider = mock {
        on { get() }.thenReturn(10, 11)
    }
    private val stringResolver: StringResolver = mock {
        on { getString(R.string.times_fetched) }.thenReturn("Times fetched: %1\$d")
        on { getString(R.string.response_code) }.thenReturn("Response Code: %1\$s")
    }

    private val clicksSubject = BehaviorSubject.create<Unit>()
    private val view: MainView = mock {
        on { clicks() }.thenReturn(clicksSubject)
    }

    private val presenter = MainPresenter(
        nextPathInteractor,
        responseCodeInteractor,
        counterProvider,
        stringResolver
    )

    @Before
    fun setup() {
        presenter.onAttach(view)
    }

    @Test
    fun `show formatted cached counter on attach view`() {
        verify(counterProvider).get()
        verify(view).showCounterTimes("Times fetched: 10")
    }

    @Test
    fun `show loading when nextPathInteractor returns loading event`() {
        clicksSubject.onNext(Unit)
        nextPathSubject.onNext(Event.loading())

        verify(view).showLoading()
    }

    @Test
    fun `call responseCodeInteractor when nextPathInteractor returns data event`() {
        clicksSubject.onNext(Unit)

        verify(responseCodeInteractor).fetchNewResponseCode(URL)
    }

    @Test
    fun `never call responseCodeInteractor when nextPathInteractor returns error event`() {
        nextPathSubject.onNext(Event.error(Throwable()))

        clicksSubject.onNext(Unit)

        verify(responseCodeInteractor, never()).fetchNewResponseCode(any())
    }

    @Test
    fun `hide loading when nextPathInteractor returns error event`() {
        nextPathSubject.onNext(Event.error(Throwable()))

        clicksSubject.onNext(Unit)

        verify(view).hideLoading()
    }

    @Test
    fun `never hide loading when nextPathInteractor returns data event`() {
        clicksSubject.onNext(Unit)

        verify(view, never()).hideLoading()
    }

    @Test
    fun `show error when nextPathInteractor returns error event`() {
        nextPathSubject.onNext(Event.error(Throwable()))

        clicksSubject.onNext(Unit)

        verify(view).showNextPathError()
    }

    @Test
    fun `hide loading when responseCodeInteractor returns error`() {
        clicksSubject.onNext(Unit)

        responseCodeSubject.onError(Throwable())

        verify(view).hideLoading()
    }

    @Test
    fun `hide loading when responseCodeInteractor returns data`() {
        clicksSubject.onNext(Unit)

        responseCodeSubject.onSuccess(ResponseCode("next_path", "123"))

        verify(view).hideLoading()
    }

    @Test
    fun `show formatted response code when responseCodeInteractor returns data`() {
        clicksSubject.onNext(Unit)

        responseCodeSubject.onSuccess(ResponseCode("next_path", "123"))

        verify(view).showResponseCode("Response Code: 123")
    }

    @Test
    fun `show counter when responseCodeInteractor returns data`() {
        clicksSubject.onNext(Unit)

        responseCodeSubject.onSuccess(ResponseCode("next_path", "123"))

        verify(view).showCounterTimes("Times fetched: 11")
    }

    @Test
    fun `show error when responseCodeInteractor returns error`() {
        clicksSubject.onNext(Unit)

        responseCodeSubject.onError(Throwable())

        verify(view).showResponseCodeError()
    }

    @Test
    fun `increment counter when responseCodeInteractor returns data`() {
        clicksSubject.onNext(Unit)

        responseCodeSubject.onSuccess(ResponseCode("next_path", "123"))

        verify(counterProvider).increment()
    }

    @Test
    fun `never increment counter when responseCodeInteractor returns error`() {
        clicksSubject.onNext(Unit)

        responseCodeSubject.onError(Throwable())

        verify(counterProvider, never()).increment()
    }
}