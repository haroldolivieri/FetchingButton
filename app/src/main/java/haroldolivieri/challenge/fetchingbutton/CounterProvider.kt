package haroldolivieri.challenge.fetchingbutton

import haroldolivieri.challenge.CounterLocalCache
import javax.inject.Inject

class CounterProvider @Inject constructor(
    private val counter: CounterLocalCache
) {
    fun get(): Int = counter.fetch()

    fun increment() {
        counter.save(counter.fetch() + 1)
    }
}