package app.com.finalprojectfs.loan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.com.finalprojectfs.R
import app.com.finalprojectfs.loan.di.NewLoanPresenterFactory
import app.com.finalprojectfs.loan.presentation.NewLoanPresenter

class NewLoanFragment : Fragment() {

    private var presenter: NewLoanPresenter? = null

    companion object{
        fun newInstance() = NewLoanFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "Новый заём"
        return inflater.inflate(R.layout.new_loan_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initPresenter()

        super.onActivityCreated(savedInstanceState)
    }

    private fun initPresenter(){
        presenter = NewLoanPresenterFactory.create()
        presenter?.attachView(this)
    }

    override fun onDestroy() {
        presenter?.detachView()
        super.onDestroy()
    }
}