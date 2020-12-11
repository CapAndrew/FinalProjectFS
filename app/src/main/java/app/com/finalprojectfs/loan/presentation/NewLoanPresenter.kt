package app.com.finalprojectfs.loan.presentation

import android.util.Log
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
import java.io.IOException
import java.lang.Exception

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
        Log.e("NewLoanPresenter", "Fetch new conditions by authToken = $authToken")
        val loanConditionDisposable = nLService?.getLoansConditions(authToken)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                { response ->
                    Log.e("NewLoanPresenter", "Success: $response")
                    handleFetchLoanConditionResult(Result.Success(response))
                },
                { t ->
                    Log.e("NewLoanPresenter", "Error: $t")
                    handleFetchLoanConditionResult(
                        Result.Error(
                            IOException(
                                "Fetch loan conditions error",
                                t
                            )
                        )
                    )
                })

        disposable?.add(loanConditionDisposable!!)
    }

    private fun handleFetchLoanConditionResult(result: Result) {

        if (result is Result.Success) {
            view?.updateLoanConditions(result.data as LoanConditionsData)
        } else {
            handleFetchLoanConditionError(result.data as IOException)
        }
    }

    private fun handleFetchLoanConditionError(exception: Exception) {
        var exceptionText = "Внутренняя ошибка сервера"

        when (exception.cause?.message) {
            "HTTP 401" -> {
                exceptionText = "Время сессии истекло. Авторизуйтесь заново"
                clearAuthorization()
            }
        }
        view?.showActionFailed(exceptionText)
    }

    fun onNewLoanButtonClicked(authToken: String, newLoan: NewLoanData) {
        Log.e("NewLoanPresenter", "Fetch new conditions by authToken = $authToken")

        val newLoanDisposable = nLService?.postLoans(authToken, newLoan)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                { response ->
                    Log.e("NewLoanPresenter", "Success: $response")
                    handleNewLoanButtonClickedResult(Result.Success(response), authToken)
                },
                { t ->
                    Log.e("NewLoanPresenter", "Error: $t")
                    handleNewLoanButtonClickedResult(Result.Error(IOException("Request new loan error", t)), null)
                })

        disposable?.add(newLoanDisposable!!)
    }

    private fun handleNewLoanButtonClickedResult(result: Result, authToken: String?) {

        if (result is Result.Success) {
            view?.showActionSuccess("Заявка на займ успешно отправлена")
            view?.openHistory(authToken!!)
        } else {
            handleNewLoanButtonClickedError(result.data as IOException)
        }
    }

    private fun handleNewLoanButtonClickedError(exception: Exception) {
        var exceptionText = "Внутренняя ошибка сервера"

        when (exception.cause?.message) {
            "HTTP 401" -> {
                exceptionText = "Время сессии истекло. Авторизуйтесь заново"
                clearAuthorization()
            }
        }
        view?.showActionFailed(exceptionText)
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