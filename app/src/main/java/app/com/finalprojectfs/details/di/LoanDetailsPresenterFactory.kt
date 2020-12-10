package app.com.finalprojectfs.details.di

import app.com.finalprojectfs.details.presentation.LoanDetailsPresenter

object LoanDetailsPresenterFactory {

    fun create(): LoanDetailsPresenter {

        return LoanDetailsPresenter()
    }
}