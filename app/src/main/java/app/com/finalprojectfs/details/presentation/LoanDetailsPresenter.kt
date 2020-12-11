package app.com.finalprojectfs.details.presentation

import android.util.Log
import app.com.finalprojectfs.details.model.retrofit.LoanDetailsApi
import app.com.finalprojectfs.details.model.retrofit.RetrofitLoanDetailsService
import app.com.finalprojectfs.details.ui.LoanDetailsFragment
import app.com.finalprojectfs.main.model.AuthTokenRepository
import app.com.finalprojectfs.main.model.entity.LoanData
import app.com.finalprojectfs.main.model.entity.Result
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.lang.Exception

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
                    handleFetchLoanByIdResult(Result.Error(IOException("Fetch loan by id error", t)))
                })

        disposable?.add(historyDisposable!!)
    }

    fun clearAuthorization() {
        sharedPrefs.clearAuthToken()
        view?.openLogin()
    }

    private fun handleFetchLoanByIdResult(result: Result){

        if (result is Result.Success) {
        view?.updateLoanDetails(result.data as LoanData)
        }else{
            handleFetchByIdError(result.data as IOException)
        }
    }

    private fun handleFetchByIdError(exception: Exception){
        var exceptionText = "Внутренняя ошибка сервера"

        when (exception.cause?.message) {
            "HTTP 401" -> {
                exceptionText = "Время сессии истекло. Авторизуйтесь заново"
                clearAuthorization()
            }
        }
        view?.showActionFailed(exceptionText)
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