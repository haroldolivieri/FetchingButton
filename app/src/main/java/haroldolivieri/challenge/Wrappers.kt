package haroldolivieri.challenge

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class StringResolver @Inject constructor(private val context: Context) {
    fun getString(id: Int): String = context.resources.getString(id)
}

class CounterLocalCache @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    @SuppressLint("ApplySharedPref")
    fun save(counter: Int) {
        sharedPreferences.edit().putInt(COUNTER_KEY, counter).commit()
    }

    fun fetch() = sharedPreferences.getInt(COUNTER_KEY, 0)

    private companion object {
        const val COUNTER_KEY = "counter_key"
    }
}