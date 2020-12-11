package app.com.finalprojectfs.history.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.com.finalprojectfs.R
import app.com.finalprojectfs.details.ui.LoanDetailsFragment
import app.com.finalprojectfs.history.di.HistoryPresenterFactory
import app.com.finalprojectfs.history.model.entity.LoanItem
import app.com.finalprojectfs.history.presentation.HistoryPresenter
import app.com.finalprojectfs.loan.ui.NewLoanFragment
import app.com.finalprojectfs.login.ui.LoginFragment
import kotlinx.android.synthetic.main.history_fragment.*

class HistoryFragment : Fragment() {

    private var presenter: HistoryPresenter? = null

    companion object {
        fun newInstance() = HistoryFragment()
    }

    private val loanAdapter =
        HistoryAdapter(onClickListener = { _, loanItem -> openLoanDetails(loanItem) })
    private lateinit var authToken: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        activity?.title = "История займов"
        return inflater.inflate(R.layout.history_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val bundle = arguments
        authToken = bundle?.getString("authToken").toString()

        initPresenter()
        initViews()
        presenter?.fetchLoansAll(authToken)

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

    private fun initPresenter() {
        presenter = HistoryPresenterFactory.create()
        presenter?.attachView(this)
    }

    private fun initViews() {
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = loanAdapter

        newLoanButton.setOnClickListener {
            openNewLoan()
        }
    }

    fun showEmptyHistory() {
        empty_history.visibility = View.VISIBLE
        recycler.visibility = View.GONE
    }

    fun showHistory(loanList: MutableList<LoanItem>) {
        loanAdapter.updateItem(loanList)
        recycler.visibility = View.VISIBLE
        empty_history.visibility = View.GONE
    }

    fun showActionFailed(errorText: String) {
        Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show()
    }

    fun openNewLoan() {
        val fragment = NewLoanFragment.newInstance()
        val bundle = Bundle()
        bundle.putString("authToken", authToken)
        fragment.arguments = bundle

        Log.e("ActualToken", authToken)

        fragmentManager?.beginTransaction()
            ?.replace(R.id.container, fragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    fun openLogin() {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.container, LoginFragment.newInstance())
            ?.commit()
    }

    fun openLoanDetails(loanItem: LoanItem) {
        val fragment = LoanDetailsFragment.newInstance()
        val bundle = Bundle()
        bundle.putLong("loanId", loanItem.id!!)
        bundle.putString("authToken", authToken)
        fragment.arguments = bundle

        fragmentManager?.beginTransaction()
            ?.replace(R.id.container, fragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    override fun onDestroy() {
        presenter?.destroyDisposables()
        presenter?.detachView()
        super.onDestroy()
    }
}