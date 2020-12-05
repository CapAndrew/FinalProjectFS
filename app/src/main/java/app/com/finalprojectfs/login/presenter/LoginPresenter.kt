package app.com.finalprojectfs.login.presenter

import app.com.finalprojectfs.login.model.LoginAdapter
import app.com.finalprojectfs.login.model.Result
import app.com.finalprojectfs.login.ui.LoginFragment

class LoginPresenter {

    private var view: LoginFragment? = null
    private var adapter: LoginAdapter? = null

    fun attachView(view: LoginFragment) {
        this.view = view
        adapter = LoginAdapter()
    }

    fun detachView() {
        this.view = null
        adapter = null
    }

    fun onLoginDataUpdated(login: String, password: String) {
        if (login.isNotEmpty() && password.isNotEmpty()) {
            view?.enableLoginButton(enable = true)
            view?.enableRegisterButton(enable = true)
        } else if (login.isEmpty() || password.isEmpty()) {
            view?.enableLoginButton(enable = false)
            view?.enableRegisterButton(enable = false)
        }
    }

    fun onLoginButtonClicked(login: String, password: String) {
        view?.showProgress()


        val result = adapter?.login(login, password)
        handleLoginResult(result = result)

        view?.showToast("$result")
    }

    fun onRegisterButtonClicked(login: String, password: String) {
        view?.showProgress()
        view?.showToast("Register with $login and $password")
    }

    private fun handleLoginResult(result: Result<String>?) {
        // view?.hideProgress()

        if (result is Result.Success) {
            view?.openHistory()
        } else {
            view?.showLoginFailed()
        }
    }
}