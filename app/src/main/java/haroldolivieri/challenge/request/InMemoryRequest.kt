package haroldolivieri.challenge.request

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject

class InMemoryRequest<T>(private var source: Single<T>) {

    private val initialState: Event<T> = Event.loading()
    private val eventsSubject = BehaviorSubject.createDefault(initialState)
    private val isInitialState: Boolean get() = eventsSubject.value === initialState

    private val disposable = CompositeDisposable()

    fun events(): Observable<Event<T>> =
        eventsSubject.doOnSubscribe {
            if (isInitialState) {
                subscribe(source)
            }
        }

    fun retry() {
        dispose()
        subscribe(source)
    }

    private fun dispose() {
        disposable.clear()
    }

    private fun subscribe(source: Single<T>) {
        source
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onLoading() }
            .subscribe({
                onData(it)
            }, {
                onError(it)
            }).addTo(disposable)
    }

    private fun onLoading() {
        eventsSubject.onNext(Event.loading())
    }

    private fun onData(data: T) {
        eventsSubject.onNext(Event.data(data))
    }

    private fun onError(throwable: Throwable) {
        eventsSubject.onNext(Event.error(throwable))
    }
}
