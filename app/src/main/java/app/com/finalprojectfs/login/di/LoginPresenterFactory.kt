package app.com.finalprojectfs.login.di

import app.com.finalprojectfs.login.presenter.LoginPresenter

object LoginPresenterFactory {

    fun create(): LoginPresenter {

        return LoginPresenter()
    }
}