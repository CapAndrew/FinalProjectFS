package app.com.finalprojectfs.history.di

import app.com.finalprojectfs.login.presenter.LoginPresenter

object LoginPresenterFactory {

    fun create(): LoginPresenter{

        return LoginPresenter()
    }
}