package haroldolivieri.challenge.network

import android.annotation.SuppressLint
import android.content.SharedPreferences
import haroldolivieri.challenge.di.ApplicationScope
import javax.inject.Inject

private const val COUNTER_KEY = "counter_key"

@ApplicationScope
class CounterProvider @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun get(): Int = sharedPreferences.getInt(COUNTER_KEY, 0)

    @SuppressLint("ApplySharedPref")
    fun increment() {
        val current = get()
        sharedPreferences.edit().putInt(COUNTER_KEY, current + 1).commit()
    }
}