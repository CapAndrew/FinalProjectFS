package app.com.finalprojectfs.history.presentation

import android.util.Log
import app.com.finalprojectfs.history.model.entity.LoanItem
import app.com.finalprojectfs.history.model.retrofit.HistoryApi
import app.com.finalprojectfs.history.model.retrofit.RetrofitHistoryService
import app.com.finalprojectfs.history.ui.HistoryFragment
import app.com.finalprojectfs.main.model.AuthTokenRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

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
                    response.forEach {
                        contactsList.add(
                            LoanItem(
                                it.date,
                                it.state,
                                it.amount,
                                it.id
                            )
                        )
                    }
                },
                { t ->
                    Log.e("HistoryPresenter", "Error: $t")
                })

        disposable?.add(historyDisposable!!)
    }

    fun destroyDisposables() {
        if (disposable != null) {
            disposable?.dispose()
        }
    }
}