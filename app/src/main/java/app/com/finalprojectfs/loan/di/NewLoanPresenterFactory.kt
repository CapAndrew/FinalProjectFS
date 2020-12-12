package app.com.finalprojectfs.loan.di

import app.com.finalprojectfs.loan.presentation.NewLoanPresenter

object NewLoanPresenterFactory {

    fun create(): NewLoanPresenter = NewLoanPresenter()
}