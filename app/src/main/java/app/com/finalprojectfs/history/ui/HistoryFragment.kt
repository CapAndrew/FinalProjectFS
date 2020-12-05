package app.com.finalprojectfs.history.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.com.finalprojectfs.R
import app.com.finalprojectfs.history.di.HistoryPresenterFactory
import app.com.finalprojectfs.history.domain.entity.LoanItem
import app.com.finalprojectfs.history.presentation.HistoryPresenter
import kotlinx.android.synthetic.main.history_fragment.*

class HistoryFragment : Fragment() {

    private var presenter: HistoryPresenter? = null

    companion object {
        fun newInstance() = HistoryFragment()
    }

    private val loanAdapter = LoanAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "История займов"
        return inflater.inflate(R.layout.history_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initPresenter()
        initViews()

        super.onActivityCreated(savedInstanceState)
    }

    private fun initPresenter() {
        presenter = HistoryPresenterFactory.create()
        presenter?.attachView(this)
    }

    private fun initViews() {
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = loanAdapter

        val contactsList: MutableList<LoanItem> = arrayListOf()
        contactsList.add(LoanItem("12.05.1992", "RUN", "3000p"))
        contactsList.add(LoanItem("12.05.1992", "RUN", "3000p"))
        contactsList.add(LoanItem("12.05.1992", "RUN", "3000p"))


        loanAdapter.updateItem(contactsList)



        newLoanButton.setOnClickListener {

        }
    }

    override fun onDestroy() {
        presenter?.detachView()
        super.onDestroy()
    }
}