package app.com.finalprojectfs.main.ui

import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
        presenter?.openMainFragment()
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

    fun openHistory(authToken: String) {
        val fragment = HistoryFragment.newInstance()
        val bundle = Bundle()
        bundle.putString("authToken", authToken)
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    override fun onDestroy() {
        presenter?.detachView()
        super.onDestroy()
    }
}