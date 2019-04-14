package haroldolivieri.challenge.fetchingbutton.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import haroldolivieri.challenge.*
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainView {

    @Inject
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        performInjections()

        presenter.onAttach(this)
    }

    private fun performInjections() {
        (application as App).component.inject(this)
    }

    override fun clicks(): Observable<Unit> =
        fetchDataButton.debounceClicks()

    override fun showLoading() {
        loading.changeVisibility(true)
    }

    override fun hideLoading() {
        loading.changeVisibility(false)
    }

    override fun showResponseCodeError() {
        fetchDataButton.showSnackbar(R.string.response_code_error, R.string.retry) { presenter.retry() }
    }

    override fun showNextPathError() {
        fetchDataButton.showSnackbar(R.string.next_path_error, R.string.retry) { presenter.retry() }
    }

    override fun showResponseCode(uuid: String) {
        responseCode.text = uuid
    }

    override fun showCounterTimes(times: String) {
        timesFetched.text = times
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }
}
