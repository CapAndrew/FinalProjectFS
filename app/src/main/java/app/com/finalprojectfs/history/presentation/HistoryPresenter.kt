package app.com.finalprojectfs.history.presentation

import app.com.finalprojectfs.history.ui.HistoryFragment
import app.com.finalprojectfs.login.model.LoginAdapter
import app.com.finalprojectfs.login.ui.LoginFragment

class HistoryPresenter {

    private var view: HistoryFragment? = null

    fun attachView(view: HistoryFragment) {
        this.view = view
    }

    fun detachView() {
        this.view = null
    }
}