package app.com.finalprojectfs.main.presentation

import app.com.finalprojectfs.main.model.AuthTokenRepository
import app.com.finalprojectfs.main.ui.MainActivity

class MainPresenter {

    private lateinit var sharedPrefs: AuthTokenRepository
    private var view: MainActivity? = null

    fun attachView(view: MainActivity) {
        this.view = view
        sharedPrefs = AuthTokenRepository(view.applicationContext)
    }

    fun detachView() {
        this.view = null
    }

    fun openMainFragment() {
        val authToken = sharedPrefs.getAuthToken()

        if (authToken.isEmpty()) {
            view?.openLogin()
        } else {
            view?.openHistory(authToken)
        }
    }
}
