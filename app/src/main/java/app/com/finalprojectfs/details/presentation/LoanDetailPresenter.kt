package app.com.finalprojectfs.details.presentation

import app.com.finalprojectfs.details.ui.LoanDetailFragment

class LoanDetailPresenter {

    private var view: LoanDetailFragment? = null

    fun attachView(view: LoanDetailFragment) {
        this.view = view
    }

    fun detachView() {
        this.view = null
    }
}