package app.com.finalprojectfs.history.presentation

import android.util.Log
import app.com.finalprojectfs.history.model.entity.LoanItem
import app.com.finalprojectfs.history.model.retrofit.HistoryApi
import app.com.finalprojectfs.history.model.retrofit.RetrofitHistoryService
import app.com.finalprojectfs.history.ui.HistoryFragment
import app.com.finalprojectfs.main.model.entity.Result
import app.com.finalprojectfs.main.model.AuthTokenRepository
import app.com.finalprojectfs.main.model.entity.LoanData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException

class HistoryPresenter {

    private var view: HistoryFragment? = null
    private var hService: RetrofitHistoryService? = null
    private val contactsList: MutableList<LoanItem> = arrayListOf()
    private lateinit var sharedPrefs: AuthTokenRepository
    var disposable: CompositeDisposable? = null

    fun attachView(view: HistoryFragment) {
        this.view = view
        hService = HistoryApi.historyService
        sharedPrefs = AuthTokenRepository(view.context)
    }

    fun detachView() {
        this.view = null
    }

    private fun updateHistoryList() {
        if (contactsList.isEmpty()) {
            view?.showEmptyHistory()
        } else {
            view?.showHistory(contactsList)
        }
    }

    fun clearAuthorization() {
        sharedPrefs.clearAuthToken()
        view?.openLogin()
    }


    fun fetchLoansAll(authToken: String) {
        Log.e("HistoryPresenter", "authToken: $authToken")

        val historyDisposable = hService?.getLoansAll(authToken)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnComplete {
                updateHistoryList()
            }
            ?.subscribe(
                { response ->
                    Log.e("HistoryPresenter", "Success: $response")
                    handleFetchLoansAllResult(Result.Success(response))
                },
                { t ->
                    Log.e("HistoryPresenter", "Error: $t")
                    handleFetchLoansAllResult(
                        Result.Error(IOException("Upload history error", t)))
                })

        disposable?.add(historyDisposable!!)
    }

    private fun handleFetchLoansAllResult(result: Result) {

        if (result is Result.Success) {
            val loansList: MutableList<LoanData> = result.data as MutableList<LoanData>

            loansList.forEach {
                contactsList.add(
                    LoanItem(
                        it.date,
                        it.state,
                        it.amount,
                        it.id
                    )
                )
            }

        } else {
            handleFetchLoansAllError(result.data as IOException)
        }
    }

    private fun handleFetchLoansAllError(exception: Exception) {
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
}