package app.com.finalprojectfs.main.di

import app.com.finalprojectfs.main.presentation.MainPresenter

object MainPresenterFactory {

    fun create(): MainPresenter = MainPresenter()
}