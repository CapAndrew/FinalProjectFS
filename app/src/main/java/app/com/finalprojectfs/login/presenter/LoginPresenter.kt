package app.com.finalprojectfs.login.presenter

import app.com.finalprojectfs.login.model.LoginData
import app.com.finalprojectfs.login.model.Result
import app.com.finalprojectfs.login.model.retrofit.LoginApi
import app.com.finalprojectfs.login.model.retrofit.RetrofitLoginService
import app.com.finalprojectfs.login.ui.LoginFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException


class LoginPresenter {

    private var view: LoginFragment? = null
    private var lService: RetrofitLoginService = LoginApi.retrofitService
    var disposable: CompositeDisposable? = null

    fun attachView(view: LoginFragment) {
        this.view = view
    }

    fun detachView() {
        this.view = null
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


        disposable?.add(lService.postLogin(LoginData(login, password))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    handleLoginResult(Result.Success(response))
                },
                { t ->
                    handleLoginResult(Result.Error(IOException("Error logging in", t)))
                }))
    }

    fun onRegisterButtonClicked(login: String, password: String) {
        view?.showProgress()
        view?.showToast("Register with $login and $password")
    }

    fun destroyDisposables(){
        disposable?.dispose()
    }

    private fun handleLoginResult(result: Result<String>?) {
        view?.hideProgress()

        if (result is Result.Success) {
            view?.openHistory()
        } else {
            view?.showLoginFailed()
        }
    }
}