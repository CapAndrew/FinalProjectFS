package app.com.finalprojectfs.login.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import app.com.finalprojectfs.R
import app.com.finalprojectfs.history.di.LoginPresenterFactory
import app.com.finalprojectfs.login.presenter.LoginPresenter
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : Fragment() {

    private var presenter: LoginPresenter? = null

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initPresenter()
        initViews()
        super.onActivityCreated(savedInstanceState)
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

    fun enableLoginButton(enable: Boolean) {
        loginButton.isEnabled = enable
    }

    fun enableRegisterButton(enable: Boolean) {
        registerButton.isEnabled = enable
    }

    override fun onDestroy() {
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