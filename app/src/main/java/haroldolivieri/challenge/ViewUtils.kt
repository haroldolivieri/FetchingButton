package haroldolivieri.challenge

import android.view.View
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.clicks
import java.util.concurrent.TimeUnit

fun View.showSnackbar(message: Int, actionTitle: Int? = null, action: () -> Unit = {}) {
    with(Snackbar.make(this, message, Snackbar.LENGTH_LONG)) {
        actionTitle?.let {
            setAction(it) {
                action()
            }
        }
        show()
    }
}

fun Button.debounceClicks() = clicks().throttleFirst(600, TimeUnit.MILLISECONDS)

fun View.changeVisibility(visible: Boolean) {
    this.visibility = if (visible) {
        View.VISIBLE
    } else View.GONE
}