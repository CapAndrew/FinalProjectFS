package app.com.finalprojectfs.history.presentation

import app.com.finalprojectfs.history.domain.entity.LoanItem
import app.com.finalprojectfs.history.ui.HistoryFragment

class HistoryPresenter {

    private var view: HistoryFragment? = null

    fun attachView(view: HistoryFragment) {
        this.view = view
    }

    fun detachView() {
        this.view = null
    }

    fun updateHistoryList() {
        val contactsList: MutableList<LoanItem> = arrayListOf()
        contactsList.add(LoanItem("12.05.1992", "RUN", "3000p"))
        contactsList.add(LoanItem("12.05.1992", "RUN", "3000p"))
        contactsList.add(LoanItem("12.05.1992", "RUN", "3000p"))

    // view?.showEmptyHistory()
        view?.showHistory(contactsList)
    }
}