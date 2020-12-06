package app.com.finalprojectfs.details.di

import app.com.finalprojectfs.details.presentation.LoanDetailPresenter

object LoanDetailPresenterFactory {

    fun create(): LoanDetailPresenter {

        return LoanDetailPresenter()
    }
}