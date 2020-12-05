package app.com.finalprojectfs.history.di

import app.com.finalprojectfs.history.presentation.HistoryPresenter

object HistoryPresenterFactory {

    fun create(): HistoryPresenter {
        return HistoryPresenter()
    }
}