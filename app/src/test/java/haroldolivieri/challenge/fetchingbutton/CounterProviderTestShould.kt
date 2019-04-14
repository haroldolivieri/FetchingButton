package haroldolivieri.challenge.fetchingbutton

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import haroldolivieri.challenge.CounterLocalCache
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class CounterProviderTestShould {

    private val localCache : CounterLocalCache = mock()
    private val counterProvider = CounterProvider(localCache)

    @Test
    @Parameters(method = "counters")
    fun `save incremented value`(current: Int, incremented: Int) {
        whenever(localCache.fetch()).thenReturn(current)

        counterProvider.increment()

        argumentCaptor<Int>().apply {
            verify(localCache).save(capture())
            assertThat(firstValue).isEqualTo(incremented)
        }
    }

    @Suppress("unused")
    companion object {
        @JvmStatic
        fun counters() = arrayOf(
            arrayOf(1, 2),
            arrayOf(2, 3),
            arrayOf(3, 4),
            arrayOf(4, 5),
            arrayOf(5, 6),
            arrayOf(6, 7),
            arrayOf(100, 101),
            arrayOf(10000, 10001)
        )
    }

}