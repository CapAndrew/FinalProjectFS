package app.com.finalprojectfs.login.presenter

import android.util.Log
import app.com.finalprojectfs.login.model.RegistrationData
import app.com.finalprojectfs.login.model.entity.LoginData
import app.com.finalprojectfs.main.model.entity.Result
import app.com.finalprojectfs.login.model.retrofit.LoginApi
import app.com.finalprojectfs.login.model.retrofit.RetrofitLoginService
import app.com.finalprojectfs.login.ui.LoginFragment
import app.com.finalprojectfs.main.model.AuthTokenRepository
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.io
import java.io.IOException
import java.lang.Exception


class LoginPresenter {

    private var view: LoginFragment? = null
    private var lService: RetrofitLoginService = LoginApi.loginService
    private lateinit var sharedPrefs: AuthTokenRepository
    var disposable: CompositeDisposable? = null
    lateinit var registrationResponse: RegistrationData
    lateinit var loginResponse: String

    fun attachView(view: LoginFragment) {
        this.view = view
        sharedPrefs = AuthTokenRepository(view.context)
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

        val loginDisposable = lService.postLogin(LoginData(login, password))
            .subscribeOn(io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    Log.e("LoginPresenter", "Success: ${Result.Success(response)}")
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
            lService.postRegistration(LoginData(login, password)),
            lService.postLogin(LoginData(login, password)),
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
                    handleRegistrationResult(Result.Success(loginResponse))
                },
                { t ->
                    handleRegistrationResult(
                        Result.Error(IOException("Registration error", t))
                    )
                }
            )
        disposable?.add(registrationDisposable)
    }

    private fun handleLoginResult(result: Result) {

        view?.hideProgress()

        Log.e("LoginPresenter", "Result.data: ${result.data}")


        if (result is Result.Success) {
            val authToken: String = result.data as String
            sharedPrefs.setAuthToken(authToken)
            Log.e("LoginPresenter", "Prefs: ${sharedPrefs.getAuthToken()}$")
            view?.openHistory(authToken)
        } else {
            handleLoginError(result.data as IOException)
        }
    }

    private fun handleLoginError(exception: Exception) {
        Log.e("LoginPresenter", ">>${exception.cause}<<")

        val exceptionText = when (exception.cause?.message) {
            "HTTP 404 " -> "Пользователь с указанными логином и паролем не найден"
            else -> "Ошибка авторизации"
        }
        view?.showActionFailed(exceptionText)
    }

    private fun handleRegistrationResult(result: Result) {

        view?.hideProgress()

        if (result is Result.Success) {
            val token: String = result.data as String
            sharedPrefs.setAuthToken(token)
            view?.openHistory(token)
        } else {
            handleRegistrationError(result.data as IOException)
        }
    }

    private fun handleRegistrationError(exception: Exception) {
        Log.e("LoginPresenter", ">>${exception.cause}<<")

        val exceptionText = when (exception.cause?.message) {
            "HTTP 400 " -> "Пользователь с таким именем уже существует"
            else -> "Ошибка регистрации"
        }
        view?.showActionFailed(exceptionText)
    }

    fun destroyDisposables() {
        if (disposable != null) {
            disposable?.dispose()
        }
    }
}