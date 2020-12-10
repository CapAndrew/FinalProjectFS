package app.com.finalprojectfs.details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.com.finalprojectfs.R
import app.com.finalprojectfs.details.di.LoanDetailPresenterFactory
import app.com.finalprojectfs.details.presentation.LoanDetailPresenter
import kotlinx.android.synthetic.main.loan_detail_fragment.*

class LoanDetailFragment() : Fragment() {

    private var presenter: LoanDetailPresenter? = null

    companion object {
        fun newInstance() = LoanDetailFragment()
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
        val loanId = bundle?.getInt("loan_id").toString()
        activity?.title = "Детали $loanId"
    }

    private fun initPresenter() {
        presenter = LoanDetailPresenterFactory.create()
        presenter?.attachView(this)
    }

    override fun onDestroy() {
        presenter?.detachView()
        super.onDestroy()
    }
}