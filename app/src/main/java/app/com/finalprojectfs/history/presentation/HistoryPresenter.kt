package app.com.finalprojectfs.history.presentation

import app.com.finalprojectfs.history.model.entity.LoanItem
import app.com.finalprojectfs.history.model.retrofit.HistoryApi
import app.com.finalprojectfs.history.model.retrofit.RetrofitHistoryService
import app.com.finalprojectfs.history.ui.HistoryFragment
import app.com.finalprojectfs.main.model.AuthTokenRepository
import app.com.finalprojectfs.main.model.entity.LoanData
import app.com.finalprojectfs.main.model.entity.Result
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

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
        view?.showProgress()

        val historyDisposable = hService?.getLoansAll(authToken)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnComplete {
                updateHistoryList()
            }
            ?.subscribe(
                { response ->
                    handleFetchLoansAllResult(Result.Success(response))
                },
                { t ->
                    handleFetchLoansAllResult(
                        Result.Error(t)
                    )
                })

        disposable?.add(historyDisposable!!)
    }

    private fun handleFetchLoansAllResult(result: Result) {
        view?.hideProgress()
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
            handleFetchLoansAllError(result.data as Exception)
        }
    }

    private fun handleFetchLoansAllError(throwable: Throwable) {

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
}