package app.com.finalprojectfs.login.presenter

import android.util.Log
import app.com.finalprojectfs.login.model.RegistrationData
import app.com.finalprojectfs.login.model.entity.LoginData
import app.com.finalprojectfs.login.model.retrofit.LoginApi
import app.com.finalprojectfs.login.model.retrofit.RetrofitLoginService
import app.com.finalprojectfs.login.ui.LoginFragment
import app.com.finalprojectfs.main.model.AuthTokenRepository
import app.com.finalprojectfs.main.model.entity.Result
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.io
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException


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
                    handleLoginResult(Result.Error(t))
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
                        Result.Error(t)
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
            handleLoginError(result.data as Exception)
        }
    }

    private fun handleLoginError(throwable: Throwable) {
        when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    404 -> {
                        view?.showLoginErrorDialog("login")
                    }
                    else -> {
                        view?.showErrorDialogWithTwoButtons(
                            "Внутренняя ошибка",
                            "Что-то пошло не так. Попробуйте ещё раз позже.",
                            "Обновить",
                            "Выйти",
                            "login"
                        )
                    }
                }
            }
            is UnknownHostException -> {
                view?.showErrorDialogWithTwoButtons(
                    "Нет интернета",
                    "Проверьте подключение к интернету и попробуйте ещё раз.",
                    "Обновить",
                    "Выйти",
                    "login"
                )
            }

            is ConnectException -> {
                view?.showErrorDialogWithTwoButtons(
                    "Нет подключения",
                    "Отсутствует подключение к серверу. Попробуйте ещё раз позже.",
                    "Обновить",
                    "Выйти",
                    "login"
                )
            }
            else -> {
                view?.showErrorDialogWithTwoButtons(
                    "Внутренняя ошибка",
                    "Что-то пошло не так. Попробуйте ещё раз позже",
                    "Обновить",
                    "Выйти",
                    "login"
                )
            }
        }
    }

    private fun handleRegistrationResult(result: Result) {

        view?.hideProgress()

        if (result is Result.Success) {
            val token: String = result.data as String
            sharedPrefs.setAuthToken(token)
            view?.openHistory(token)
        } else {
            handleRegistrationError(result.data as Exception)
        }
    }

    private fun handleRegistrationError(throwable: Throwable) {
        when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    400 -> {
                        view?.showLoginErrorDialog("registration")
                    }
                    else -> {
                        view?.showErrorDialogWithTwoButtons(
                            "Внутренняя ошибка",
                            "Что-то пошло не так. Попробуйте ещё раз позже.",
                            "Обновить",
                            "Выйти",
                            "login"
                        )
                    }
                }
            }
            is UnknownHostException -> {
                view?.showErrorDialogWithTwoButtons(
                    "Нет интернета",
                    "Проверьте подключение к интернету и попробуйте ещё раз.",
                    "Обновить",
                    "Выйти",
                    "login"
                )
            }

            is ConnectException -> {
                view?.showErrorDialogWithTwoButtons(
                    "Нет подключения",
                    "Отсутствует подключение к серверу. Попробуйте ещё раз позже.",
                    "Обновить",
                    "Выйти",
                    "login"
                )
            }
            else -> {
                view?.showErrorDialogWithTwoButtons(
                    "Внутренняя ошибка",
                    "Что-то пошло не так. Попробуйте ещё раз позже",
                    "Обновить",
                    "Выйти",
                    "login"
                )
            }
        }
    }

    fun destroyDisposables() {
        if (disposable != null) {
            disposable?.dispose()
        }
    }
}