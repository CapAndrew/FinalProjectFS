package app.com.finalprojectfs.login.presenter

import android.util.Log
import app.com.finalprojectfs.login.model.LoginData
import app.com.finalprojectfs.login.model.RegistrationData
import app.com.finalprojectfs.login.model.Result
import app.com.finalprojectfs.login.model.retrofit.LoginApi
import app.com.finalprojectfs.login.model.retrofit.RetrofitLoginService
import app.com.finalprojectfs.login.ui.LoginFragment
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.io
import java.io.IOException


class LoginPresenter {

    private var view: LoginFragment? = null
    private var lService: RetrofitLoginService? = null
    var disposable: CompositeDisposable? = null
    lateinit var registrationResponse: RegistrationData
    lateinit var loginResponse: String

    fun attachView(view: LoginFragment) {
        this.view = view
        lService = LoginApi.retrofitService
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

        val loginDisposable = lService?.postLogin(LoginData(login, password))
            ?.subscribeOn(io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                { response ->
                    Log.e("LoginPresenter", "Success: $response")
                    handleLoginResult(Result.Success(response))

                },
                { t ->
                    Log.e("LoginPresenter", "Error: $t")
                    handleLoginResult(Result.Error(IOException("Error logging in", t)))
                })

        disposable?.add(loginDisposable!!)
    }

    fun onRegisterButtonClicked(login: String, password: String) {
        view?.showProgress()

        val registrationDisposable = Flowable.zip(
            lService?.postRegistration(LoginData(login, password)),
            lService?.postLogin(LoginData(login, password)),
            { registrationResponse, loginResponse ->
                this.registrationResponse = registrationResponse
                this.loginResponse = loginResponse
            }
        )
            .subscribeOn(io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.e("LoginPresenter", "Success: $registrationResponse$")
                    Log.e("LoginPresenter", "Success: $loginResponse$")
                    handleLoginResult(Result.Success(loginResponse))
                },
                { t ->
                    handleRegistrationResult(Result.Error(IOException("Registration error", t)))
                }
            )
        disposable?.add(registrationDisposable)
    }

    fun destroyDisposables() {
        if (disposable != null) {
            disposable?.dispose()
        }
    }

    private fun handleLoginResult(result: Result<String>?) {
        view?.hideProgress()

        if (result is Result.Success) {
            view?.openHistory()
        } else {
            view?.showLoginFailed()
        }
    }

    private fun handleRegistrationResult(result: Result<String>?) {
        view?.hideProgress()

        if (result is Result.Success) {
            view?.openHistory()
        } else {
            view?.showRegistrationFailed()
        }
    }
}