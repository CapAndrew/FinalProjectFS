package app.com.finalprojectfs.details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.com.finalprojectfs.R
import app.com.finalprojectfs.details.di.LoanDetailsPresenterFactory
import app.com.finalprojectfs.details.model.entity.LoanDetailsData
import app.com.finalprojectfs.details.presentation.LoanDetailsPresenter
import kotlinx.android.synthetic.main.loan_detail_fragment.*

class LoanDetailsFragment() : Fragment() {

    private var presenter: LoanDetailsPresenter? = null
    private var loanId: Long? = null
    private lateinit var authToken: String

    companion object {
        fun newInstance() = LoanDetailsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.loan_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initPresenter()

        super.onActivityCreated(savedInstanceState)

        val bundle = arguments
        loanId = bundle?.getLong("loanId")
        authToken = bundle?.getString("authToken").toString()

        presenter?.fetchLoanById(loanId!!, authToken)
        activity?.title = "Детали перевода №$loanId"
    }

    fun updateLoanDetails(loanDetails: LoanDetailsData){
        val lastFirstName = "${loanDetails.lastName} ${loanDetails.firstName}"
        loan_detail_date.text = loanDetails.date.toString()
        loan_detail_last_first_name.text = lastFirstName
        loan_detail_phone.text = loanDetails.phoneNumber
        loan_detail_amount.text = loanDetails.amount.toString()
        loan_detail_percent.text = loanDetails.percent.toString()
        loan_detail_period.text = loanDetails.period.toString()
        loan_detail_state.text = loanDetails.state.toString()
    }

    private fun initPresenter() {
        presenter = LoanDetailsPresenterFactory.create()
        presenter?.attachView(this)
    }

    override fun onDestroy() {
        presenter?.destroyDisposables()
        presenter?.detachView()
        super.onDestroy()
    }
}