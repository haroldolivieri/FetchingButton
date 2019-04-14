package haroldolivieri.challenge

import android.content.Context
import javax.inject.Inject

class StringResolver @Inject constructor(private val context: Context) {
    fun getString(id: Int): String = context.resources.getString(id)
}