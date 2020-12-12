package app.com.finalprojectfs.login.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.com.finalprojectfs.R
import app.com.finalprojectfs.history.ui.HistoryFragment
import app.com.finalprojectfs.login.di.LoginPresenterFactory
import app.com.finalprojectfs.login.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_main.*
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

    fun showErrorDialogWithTwoButtons(
        errorTitle: String,
        errorText: String,
        positiveButtonName: String,
        negativeButtonName: String,
        action: String
    ) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.error_dialog, null)
        dialogBuilder.setView(dialogView)

        dialogBuilder.setTitle(errorTitle)
        dialogBuilder.setMessage(errorText)
        dialogBuilder.setPositiveButton(positiveButtonName) { _, _ ->
            when (action) {
                "login" -> presenter?.onLoginButtonClicked(
                    userLogin.text.toString(),
                    userPassword.text.toString()
                )
                "registration" -> presenter?.onRegisterButtonClicked(
                    userLogin.text.toString(),
                    userPassword.text.toString()
                )
            }
        }

        dialogBuilder.setNegativeButton(negativeButtonName) { _, _ ->
            activity?.finish()
        }

        dialogBuilder.setOnCancelListener {
            activity?.finish()
        }

        dialogBuilder
            .create()
            .show()
    }

    fun showLoginErrorDialog(action: String) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.error_dialog, null)
        dialogBuilder.setView(dialogView)

        when (action) {
            "login" -> {
                dialogBuilder.setTitle("Ошибка авторизации")
                dialogBuilder.setMessage("Пользователь с указанными логином и паролем не найден.")
                dialogBuilder.setPositiveButton("Ок") { _, _ ->
                    userLogin.text.clear()
                    userPassword.text.clear()
                }
            }
            "registration" -> {
                dialogBuilder.setTitle("Ошибка регистрации")
                dialogBuilder.setMessage("Пользователь с указанным именем уже существует.")
                dialogBuilder.setPositiveButton("Ок") { _, _ ->
                    userLogin.text.clear()
                    userPassword.text.clear()
                }
            }
        }

        dialogBuilder.setOnCancelListener {
            activity?.finish()
        }

        dialogBuilder
            .create()
            .show()
    }

    fun showProgress() {
        activity?.main_loading?.visibility = View.VISIBLE
        userLogin.clearFocus()
        userPassword.clearFocus()
    }

    fun hideProgress() {
        activity?.main_loading?.visibility = View.GONE
    }

    fun enableLoginButton(enable: Boolean) {
        loginButton.isEnabled = enable
    }

    fun enableRegisterButton(enable: Boolean) {
        registerButton.isEnabled = enable
    }

    fun openHistory(authToken: String) {
        val fragment = HistoryFragment.newInstance()
        val bundle = Bundle()
        bundle.putString("authToken", authToken)
        fragment.arguments = bundle

        fragmentManager?.beginTransaction()
            ?.replace(R.id.container, fragment)
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