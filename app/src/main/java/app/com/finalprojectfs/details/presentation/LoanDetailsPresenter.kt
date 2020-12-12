package app.com.finalprojectfs.details.presentation

import android.util.Log
import app.com.finalprojectfs.details.model.retrofit.LoanDetailsApi
import app.com.finalprojectfs.details.model.retrofit.RetrofitLoanDetailsService
import app.com.finalprojectfs.details.ui.LoanDetailsFragment
import app.com.finalprojectfs.main.model.AuthTokenRepository
import app.com.finalprojectfs.main.model.entity.LoanData
import app.com.finalprojectfs.main.model.entity.Result
import app.com.finalprojectfs.main.model.entity.State
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

class LoanDetailsPresenter {

    private var view: LoanDetailsFragment? = null
    private var dService: RetrofitLoanDetailsService? = null
    var disposable: CompositeDisposable? = null
    private lateinit var sharedPrefs: AuthTokenRepository

    fun attachView(view: LoanDetailsFragment) {
        this.view = view
        dService = LoanDetailsApi.loanDetailsService
        sharedPrefs = AuthTokenRepository(view.context)
    }

    fun fetchLoanById(loanId: Long, authToken: String) {
        view?.showProgress()
        Log.e("LoanDetailsPresenter", "Fetch loan by ID: $loanId, authToken = $authToken")
        val historyDisposable = dService?.getLoansById(authToken, loanId)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                { response ->
                    Log.e("LoanDetailsPresenter", "Success: $response")
                    handleFetchLoanByIdResult(Result.Success(response))
                },
                { t ->
                    Log.e("LoanDetailsPresenter", "Error: $t")
                    handleFetchLoanByIdResult(Result.Error(t))
                })

        disposable?.add(historyDisposable!!)
    }

    fun clearAuthorization() {
        sharedPrefs.clearAuthToken()
        view?.openLogin()
    }

    private fun handleFetchLoanByIdResult(result: Result) {
        view?.hideProgress()

        val loanData = result.data as LoanData

        if (result is Result.Success) {
            view?.updateLoanDetails(loanData)
            if(loanData.state == State.APPROVED.toString()){
                view?.showHowToGetHint()
            }
        } else {
            handleFetchByIdError(result.data as Exception)
        }
    }

    private fun handleFetchByIdError(throwable: Throwable) {
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

    fun destroyDisposables() {
        if (disposable != null) {
            disposable?.dispose()
        }
    }

    fun detachView() {
        this.view = null
    }
}