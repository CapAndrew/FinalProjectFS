package app.com.finalprojectfs.loan.presentation

import android.util.Log
import app.com.finalprojectfs.loan.domain.entity.NewLoanData
import app.com.finalprojectfs.loan.domain.retrofit.NewLoanApi
import app.com.finalprojectfs.loan.domain.retrofit.RetrofitNewLoanService
import app.com.finalprojectfs.loan.ui.NewLoanFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NewLoanPresenter {

    private var view: NewLoanFragment? = null
    private var nLService: RetrofitNewLoanService? = null
    var disposable: CompositeDisposable? = null

    fun attachView(view: NewLoanFragment) {
        this.view = view
        nLService = NewLoanApi.newLoanService
    }

    fun fetchLoanConditions(authToken: String){
        Log.e("NewLoanPresenter", "Fetch new conditions by authToken = $authToken")
        val loanConditionDisposable = nLService?.getLoansConditions(authToken)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                { response ->
                    Log.e("NewLoanPresenter", "Success: $response")
                    view?.updateLoanConditions(response)
                },
                { t ->
                    Log.e("NewLoanPresenter", "Error: $t")
                })

        disposable?.add(loanConditionDisposable!!)


    }

    fun onNewLoanButtonClicked(authToken: String, newLoan: NewLoanData){
        Log.e("NewLoanPresenter", "Fetch new conditions by authToken = $authToken")

        val newLoanDisposable = nLService?.postLoans(authToken, newLoan)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                { response ->
                    Log.e("NewLoanPresenter", "Success: $response")
                },
                { t ->
                    Log.e("NewLoanPresenter", "Error: $t")
                })

        disposable?.add(newLoanDisposable!!)
    }

    fun onNewLoanDataUpdated(amount: Int, lastName: String, firstName: String, phone: String) {
        if (lastName.isNotEmpty() && firstName.isNotEmpty() && phone.isNotEmpty() && amount != 0) {
            view?.enableNewLoanButton(enable = true)
        } else  {
            view?.enableNewLoanButton(enable = false)
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