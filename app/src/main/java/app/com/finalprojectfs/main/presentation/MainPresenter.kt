package app.com.finalprojectfs.main.presentation

import app.com.finalprojectfs.main.ui.MainActivity

class MainPresenter {

    var isUserLoggedIn: Boolean = false

    private var view: MainActivity? = null

    fun attachView(view: MainActivity) {
        this.view = view
    }

    fun detachView() {
        this.view = null
    }
}
