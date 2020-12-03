package app.com.finalprojectfs.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.com.finalprojectfs.R
import app.com.finalprojectfs.login.ui.LoginFragment
import app.com.finalprojectfs.main.di.MainPresenterFactory
import app.com.finalprojectfs.main.presentation.MainPresenter

class MainActivity : AppCompatActivity() {

	var presenter: MainPresenter? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		initPresenter()

		if (savedInstanceState == null&&!presenter?.isUserLoggedIn!!) {
			supportFragmentManager.beginTransaction()
				.replace(R.id.container, LoginFragment.newInstance())
				.commit()
		}
	}

	private fun initPresenter(){
		presenter = MainPresenterFactory.create()
	}



}