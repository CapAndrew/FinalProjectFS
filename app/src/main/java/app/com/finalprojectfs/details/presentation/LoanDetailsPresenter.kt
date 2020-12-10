package app.com.finalprojectfs.details.presentation

import android.util.Log
import app.com.finalprojectfs.details.model.retrofit.LoanDetailsApi
import app.com.finalprojectfs.details.model.retrofit.RetrofitLoanDetailsService
import app.com.finalprojectfs.details.ui.LoanDetailsFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoanDetailsPresenter {

    private var view: LoanDetailsFragment? = null
    private var dService: RetrofitLoanDetailsService? = null
    var disposable: CompositeDisposable? = null

    fun attachView(view: LoanDetailsFragment) {
        this.view = view
        dService = LoanDetailsApi.loanDetailsService
    }

    fun fetchLoanById(loanId: Long, authToken: String) {
        Log.e("LoanDetailsPresenter", "Fetch loan by ID: $loanId, authToken = $authToken")
        val historyDisposable = dService?.getLoansById(authToken, loanId)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                { response ->
                    Log.e("LoanDetailsPresenter", "Success: $response")
                    view?.updateLoanDetails(response)
                },
                { t ->
                    Log.e("LoanDetailsPresenter", "Error: $t")
                })

        disposable?.add(historyDisposable!!)
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