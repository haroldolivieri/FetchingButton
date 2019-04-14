package haroldolivieri.challenge.request

import com.google.common.truth.Truth.assertThat
import haroldolivieri.challenge.RxTestRule
import io.reactivex.*
import org.junit.Rule
import org.junit.Test

@Suppress("VARIABLE_WITH_REDUNDANT_INITIALIZER")
class InMemoryRequestTestShould {

    @Rule
    @JvmField
    val rule = RxTestRule()

    private val request = InMemoryRequest(getSingle())
    private lateinit var emitter: SingleEmitter<List<Int>>
    private var subscriptionCounter = 0
    private val error = Throwable("error")

    @Test
    fun `starts with loading`() {
        val test = request.events().test()

        test.assertValue(Event.loading())
    }

    @Test
    fun `receive data`() {
        val test = request.events().test()

        emitter.onSuccess(listOf(1, 2, 3, 4))

        test.assertValues(Event.loading(), Event.data(listOf(1, 2, 3, 4)))
    }

    @Test
    fun `receive error`() {
        val test = request.events().test()

        emitter.onError(error)

        test.assertValues(Event.loading(), Event.error(error))
    }

    @Test
    fun `return cached value`() {
        var test = request.events().test()

        emitter.onSuccess(listOf(1, 2, 3, 4))

        test = request.events().test()

        test.assertValue(Event.data(listOf(1, 2, 3, 4)))
    }

    @Test
    fun `return cached error`() {
        var test = request.events().test()

        emitter.onError(error)

        test = request.events().test()

        test.assertValue(Event.error(error))
    }

    @Test
    fun `subscribe only once`() {
        for (i in 0..100) {
            request.events().subscribe()
        }

        assertThat(subscriptionCounter).isEqualTo(1)
    }

    @Test
    fun `retry resubscribes`() {
        request.events().subscribe()
        request.retry()

        assertThat(subscriptionCounter).isEqualTo(2)
    }

    @Test
    fun `emits loading again after retry`() {
        val test = request.events().test()

        emitter.onSuccess(listOf(1, 2, 3))
        request.retry()
        emitter.onSuccess(listOf(4, 5, 6))

        test.assertValues(
            Event.loading(),
            Event.data(listOf(1, 2, 3)),
            Event.loading(),
            Event.data(listOf(4, 5, 6))
        )
    }

    @Test
    fun `work correctly with multiple retries`() {
        val test = request.events().test()

        emitter.onError(error)

        request.retry()
        emitter.onSuccess(listOf(1, 2, 3))

        request.retry()
        emitter.onSuccess(listOf(4, 5, 6))

        request.retry()
        emitter.onSuccess(listOf(7, 8, 9))

        request.retry()
        emitter.onError(error)

        test.assertValues(
            Event.loading(), Event.error(error),
            Event.loading(), Event.data(listOf(1, 2, 3)),
            Event.loading(), Event.data(listOf(4, 5, 6)),
            Event.loading(), Event.data(listOf(7, 8, 9)),
            Event.loading(), Event.error(error)
        )
    }

    private fun getSingle(): Single<List<Int>> {
        return Single.create {
            subscriptionCounter++
            emitter = it
        }
    }
}