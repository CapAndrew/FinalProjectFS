package app.com.finalprojectfs.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import app.com.finalprojectfs.R
import app.com.finalprojectfs.history.ui.HistoryFragment
import app.com.finalprojectfs.login.ui.LoginFragment
import app.com.finalprojectfs.main.di.MainPresenterFactory
import app.com.finalprojectfs.main.presentation.MainPresenter

class MainActivity : AppCompatActivity() {

    private var presenter: MainPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPresenter()
        if (!presenter?.isUserLoggedIn!!) {
            openLogin()
        }
    }

    private fun initPresenter() {
        presenter = MainPresenterFactory.create()
        presenter?.attachView(this)
    }

    fun openLogin() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, LoginFragment.newInstance())
            .commit()
    }

    override fun onDestroy() {
        presenter?.detachView()
        super.onDestroy()
    }
}