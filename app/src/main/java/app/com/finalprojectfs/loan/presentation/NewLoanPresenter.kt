package app.com.finalprojectfs.loan.presentation

import app.com.finalprojectfs.loan.model.entity.LoanConditionsData
import app.com.finalprojectfs.loan.model.entity.NewLoanData
import app.com.finalprojectfs.loan.model.retrofit.NewLoanApi
import app.com.finalprojectfs.loan.model.retrofit.RetrofitNewLoanService
import app.com.finalprojectfs.loan.ui.NewLoanFragment
import app.com.finalprojectfs.main.model.AuthTokenRepository
import app.com.finalprojectfs.main.model.entity.Result
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

class NewLoanPresenter {

    private var view: NewLoanFragment? = null
    private var nLService: RetrofitNewLoanService? = null
    var disposable: CompositeDisposable? = null
    private lateinit var sharedPrefs: AuthTokenRepository

    fun attachView(view: NewLoanFragment) {
        this.view = view
        nLService = NewLoanApi.newLoanService
        sharedPrefs = AuthTokenRepository(view.context)
    }

    fun clearAuthorization() {
        sharedPrefs.clearAuthToken()
        view?.openLogin()
    }

    fun fetchLoanConditions(authToken: String) {
        view?.showProgress()

        val loanConditionDisposable = nLService?.getLoansConditions(authToken)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                { response ->
                    handleFetchLoanConditionResult(Result.Success(response))
                },
                { t ->
                    handleFetchLoanConditionResult(
                        Result.Error(t)
                    )
                })

        disposable?.add(loanConditionDisposable!!)
    }

    private fun handleFetchLoanConditionResult(result: Result) {
        view?.hideProgress()
        if (result is Result.Success) {
            view?.updateLoanConditions(result.data as LoanConditionsData)
        } else {
            handleFetchLoanConditionError(result.data as Exception)
        }
    }

    private fun handleFetchLoanConditionError(throwable: Throwable) {
        when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    403 -> {
                        view?.showAuthorizationErrorDialog()
                    }
                    else -> {
                        view?.showErrorDialogWithTwoButtons(
                            "Внутренняя ошибка",
                            "Что-то пошло не так. Попробуйте ещё раз позже.",
                            "Обновить",
                            "Выйти"
                        )
                    }
                }
            }
            is UnknownHostException -> {
                view?.showErrorDialogWithTwoButtons(
                    "Нет интернета",
                    "Проверьте подключение к интернету и попробуйте ещё раз.",
                    "Обновить",
                    "Выйти"
                )
            }

            is ConnectException -> {
                view?.showErrorDialogWithTwoButtons(
                    "Нет подключения",
                    "Отсутствует подключение к серверу. Попробуйте ещё раз позже.",
                    "Обновить",
                    "Выйти"
                )
            }
            else -> {
                view?.showErrorDialogWithTwoButtons(
                    "Внутренняя ошибка",
                    "Что-то пошло не так. Попробуйте ещё раз позже",
                    "Обновить",
                    "Выйти"
                )
            }
        }
    }

    fun onNewLoanButtonClicked(authToken: String, newLoan: NewLoanData) {
        val newLoanDisposable = nLService?.postLoans(authToken, newLoan)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                { response ->
                    handleNewLoanButtonClickedResult(Result.Success(response))
                },
                { t ->
                    handleNewLoanButtonClickedResult(Result.Error(t))
                })

        disposable?.add(newLoanDisposable!!)
    }

    private fun handleNewLoanButtonClickedResult(result: Result) {

        if (result is Result.Success) {
            view?.showSuccessDialog()
        } else {
            handleNewLoanButtonClickedError(result.data as Exception)
        }
    }

    private fun handleNewLoanButtonClickedError(throwable: Throwable) {
        when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    403 -> {
                        view?.showAuthorizationErrorDialog()
                    }
                    else -> {
                        view?.showErrorDialogWithTwoButtons(
                            "Внутренняя ошибка",
                            "Что-то пошло не так. Попробуйте ещё раз позже.",
                            "Обновить",
                            "Выйти"
                        )
                    }
                }
            }
            is UnknownHostException -> {
                view?.showErrorDialogWithTwoButtons(
                    "Нет интернета",
                    "Проверьте подключение к интернету и попробуйте ещё раз.",
                    "Обновить",
                    "Выйти"
                )
            }

            is ConnectException -> {
                view?.showErrorDialogWithTwoButtons(
                    "Нет подключения",
                    "Отсутствует подключение к серверу. Попробуйте ещё раз позже.",
                    "Обновить",
                    "Выйти"
                )
            }
            else -> {
                view?.showErrorDialogWithTwoButtons(
                    "Внутренняя ошибка",
                    "Что-то пошло не так. Попробуйте ещё раз позже",
                    "Обновить",
                    "Выйти"
                )
            }
        }
    }

    fun onNewLoanDataUpdated(amount: Int, lastName: String, firstName: String, phone: String) {
        if (lastName.isNotEmpty() && firstName.isNotEmpty() && phone.isNotEmpty() && amount != 0) {
            view?.enableNewLoanButton(enable = true)
        } else {
            view?.enableNewLoanButton(enable = false)
        }
    }

    fun calculateAmountForSeekBar(maxAmount: Int, currentPosition: Int): String =
        (currentPosition * maxAmount / 100).toString()

    fun destroyDisposables() {
        if (disposable != null) {
            disposable?.dispose()
        }
    }

    fun detachView() {
        this.view = null
    }
}