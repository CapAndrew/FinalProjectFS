package app.com.finalprojectfs.details.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import app.com.finalprojectfs.R
import app.com.finalprojectfs.details.di.LoanDetailsPresenterFactory
import app.com.finalprojectfs.details.presentation.LoanDetailsPresenter
import app.com.finalprojectfs.login.ui.LoginFragment
import app.com.finalprojectfs.main.model.entity.LoanData
import app.com.finalprojectfs.main.model.entity.Result
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

        val bundle = arguments
        loanId = bundle?.getLong("loanId")
        authToken = bundle?.getString("authToken").toString()

        presenter?.fetchLoanById(loanId!!, authToken)
        activity?.title = "Детали перевода №$loanId"

        setHasOptionsMenu(true)
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.action_exit)
            presenter?.clearAuthorization()


        return super.onOptionsItemSelected(item)
    }

    fun openLogin() {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.container, LoginFragment.newInstance())
            ?.commit()
    }

    fun showActionFailed(errorText: String) {
        Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show()
    }

    fun updateLoanDetails(loanData: LoanData) {
        val lastFirstName = "${loanData.lastName} ${loanData.firstName}"

        loan_detail_date.text = loanData.date.toString()
        loan_detail_last_first_name.text = lastFirstName
        loan_detail_phone.text = loanData.phoneNumber
        loan_detail_amount.text = loanData.amount.toString()
        loan_detail_percent.text = loanData.percent.toString()
        loan_detail_period.text = loanData.period.toString()
        loan_detail_state.text = loanData.state.toString()
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