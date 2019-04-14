package haroldolivieri.challenge.fetchingbutton.presentation

import haroldolivieri.challenge.R
import haroldolivieri.challenge.StringResolver
import haroldolivieri.challenge.fetchingbutton.interactor.NextPathInteractor
import haroldolivieri.challenge.fetchingbutton.interactor.ResponseCodeInteractor
import haroldolivieri.challenge.model.CorrectPath
import haroldolivieri.challenge.fetchingbutton.CounterProvider
import haroldolivieri.challenge.request.Event
import haroldolivieri.challenge.request.reducer
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val nextPathInteractor: NextPathInteractor,
    private val responseCodeInteractor: ResponseCodeInteractor,
    private val counterProvider: CounterProvider,
    private val stringResolver: StringResolver
) {

    private var view: MainView? = null
    private val disposables = CompositeDisposable()
    private var disposable = Disposables.disposed()

    fun onAttach(view: MainView) {
        this.view = view

        view.clicks()
            .flatMap {
                nextPathInteractor.events()
            }.subscribe { event ->
                cachedEventReducer(event)
            }.addTo(disposables)

        showCounter()
    }

    fun retry() {
        nextPathInteractor.retry()
    }

    fun onDetach() {
        disposables.clear()
        disposable.dispose()
    }

    private fun cachedEventReducer(event: Event<CorrectPath>) {
        view?.run {
            event.reducer(
                onLoading = {
                    showLoading()
                },
                onError = {
                    hideLoading()
                    showNextPathError()
                },
                onDataArrived = {
                    fetchNextData(it.nextPath)
                }
            )
        }
    }

    private fun fetchNextData(nextPath: String) {
        disposable.dispose()

        view?.run {
            disposable = responseCodeInteractor
                .fetchNewResponseCode(nextPath)
                .subscribe { response, error ->
                    hideLoading()
                    if (error != null) {
                        showResponseCodeError()
                    } else {
                        showResponseCode(
                            stringResolver
                                .getString(R.string.response_code).format(response.code)
                        )
                        counterProvider.increment()
                        showCounter()
                    }
                }
        }
    }

    private fun showCounter() {
        view?.showCounterTimes(
            stringResolver
                .getString(R.string.times_fetched).format(counterProvider.get())
        )
    }
}

interface MainView {
    fun clicks(): Observable<Unit>

    fun showLoading()
    fun hideLoading()

    fun showResponseCodeError()
    fun showNextPathError()

    fun showResponseCode(uuid: String)
    fun showCounterTimes(times: String)
}