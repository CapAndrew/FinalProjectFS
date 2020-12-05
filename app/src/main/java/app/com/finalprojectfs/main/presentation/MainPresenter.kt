package app.com.finalprojectfs.main.presentation

import app.com.finalprojectfs.main.ui.MainActivity

class MainPresenter {

    var isUserLoggedIn: Boolean = true

    private var view: MainActivity? = null

    fun attachView(view: MainActivity) {
        this.view = view
    }

    fun detachView() {
        this.view = null
    }

    fun openMainFragment() {
        if (!isUserLoggedIn) {
            view?.openLogin()
        } else {
            view?.openHistory()
        }
    }
}
