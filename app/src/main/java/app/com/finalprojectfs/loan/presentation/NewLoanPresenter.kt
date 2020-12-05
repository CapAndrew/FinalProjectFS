package app.com.finalprojectfs.loan.presentation

import app.com.finalprojectfs.loan.ui.NewLoanFragment

class NewLoanPresenter {

    private var view: NewLoanFragment? = null

    fun attachView(view: NewLoanFragment) {
        this.view = view
    }

    fun detachView() {
        this.view = null
    }
}