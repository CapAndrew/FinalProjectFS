package app.com.finalprojectfs.login.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import app.com.finalprojectfs.R
import app.com.finalprojectfs.history.ui.HistoryFragment
import app.com.finalprojectfs.login.di.LoginPresenterFactory
import app.com.finalprojectfs.login.presenter.LoginPresenter
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : Fragment() {

    private var presenter: LoginPresenter? = null

    companion object {
        fun newInstance() = LoginFragment()

        private const val REQUEST_CODE_PERMISSION_INTERNET = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "Авторизация"
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initPresenter()
        initViews()

        if (!checkInternetPermission()) {
            askPermission(Manifest.permission.INTERNET)
        }
    }

    private fun initPresenter() {
        presenter = LoginPresenterFactory.create()
        presenter?.attachView(this)
    }

    private fun initViews() {
        userLogin.afterTextChanged {
            presenter?.onLoginDataUpdated(
                userLogin.text.toString(),
                userPassword.text.toString()
            )
        }

        userPassword.afterTextChanged {
            presenter?.onLoginDataUpdated(
                userLogin.text.toString(),
                userPassword.text.toString()
            )
        }

        loginButton.setOnClickListener {
            presenter?.onLoginButtonClicked(
                userLogin.text.toString(),
                userPassword.text.toString()
            )
        }

        registerButton.setOnClickListener {
            presenter?.onRegisterButtonClicked(
                userLogin.text.toString(),
                userPassword.text.toString()
            )
        }
    }

    fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun showProgress() {
        loading.visibility = View.VISIBLE
        userLogin.clearFocus()
        userPassword.clearFocus()
    }

    fun hideProgress() {
        loading.visibility = View.GONE
    }

    fun showLoginFailed() {
        Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
    }

    fun enableLoginButton(enable: Boolean) {
        loginButton.isEnabled = enable
    }

    fun enableRegisterButton(enable: Boolean) {
        registerButton.isEnabled = enable
    }

    fun openHistory() {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.container, HistoryFragment.newInstance())
            ?.commit()
    }

    private fun checkInternetPermission(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.INTERNET
        )
    }

    private fun askPermission(vararg permissions: String) {
        requestPermissions(permissions, REQUEST_CODE_PERMISSION_INTERNET)
    }

    override fun onDestroy() {
        presenter?.destroyDisposables()
        presenter?.detachView()
        super.onDestroy()
    }
}

private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}