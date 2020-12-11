package app.com.finalprojectfs.loan.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import app.com.finalprojectfs.R
import app.com.finalprojectfs.loan.di.NewLoanPresenterFactory
import app.com.finalprojectfs.loan.domain.entity.LoanConditionsData
import app.com.finalprojectfs.loan.domain.entity.NewLoanData
import app.com.finalprojectfs.loan.presentation.NewLoanPresenter
import kotlinx.android.synthetic.main.new_loan_fragment.*

class NewLoanFragment : Fragment() {

    private var presenter: NewLoanPresenter? = null
    private lateinit var authToken: String
    private var maxAmount: Int = 0


    companion object {
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
        super.onActivityCreated(savedInstanceState)

        val bundle = arguments
        authToken = bundle?.getString("authToken").toString()

        Log.d("AuthToken", "From onAcCrea newLoan $authToken")

        initPresenter()
        initViews()
        presenter?.fetchLoanConditions(authToken)
    }

    fun updateLoanConditions(conditions: LoanConditionsData) {
        maxAmount = conditions.maxAmount!!

        loan_conditions_max_amount.text = maxAmount.toString()
        loan_conditions_percent.text = conditions.percent.toString()
        loan_conditions_period.text = conditions.period.toString()

        loan_amount_seek_bar.max

    }

    private fun initPresenter() {
        presenter = NewLoanPresenterFactory.create()
        presenter?.attachView(this)
    }

    private fun initViews() {
        loan_first_name.afterTextChanged {
            presenter?.onNewLoanDataUpdated(
                loan_amount.text.toString().toInt(),
                loan_last_name.text.toString(),
                loan_first_name.text.toString(),
                loan_phone.text.toString()
            )
        }

        loan_last_name.afterTextChanged {
            presenter?.onNewLoanDataUpdated(
                loan_amount.text.toString().toInt(),
                loan_last_name.text.toString(),
                loan_first_name.text.toString(),
                loan_phone.text.toString()
            )
        }

        loan_phone.afterTextChanged {
            presenter?.onNewLoanDataUpdated(
                loan_amount.text.toString().toInt(),
                loan_last_name.text.toString(),
                loan_first_name.text.toString(),
                loan_phone.text.toString()
            )
        }

        loan_amount_seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                loan_amount.text = (p1 * maxAmount / 100).toString()

                presenter?.onNewLoanDataUpdated(
                    loan_amount.text.toString().toInt(),
                    loan_last_name.text.toString(),
                    loan_first_name.text.toString(),
                    loan_phone.text.toString()
                )
              //  TODO("Сделать функцию расчёта")


            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })


        newLoanButton.setOnClickListener {
            presenter?.onNewLoanButtonClicked(
                authToken, NewLoanData(
                    loan_amount.text.toString().toInt(),
                    loan_conditions_percent.text.toString().toDouble(),
                    loan_conditions_period.text.toString().toInt(),
                    loan_first_name.text.toString(),
                    loan_last_name.text.toString(),
                    loan_phone.text.toString()
                )
            )
        }
    }

    fun enableNewLoanButton(enable: Boolean){
        newLoanButton.isEnabled = enable
    }

    override fun onDestroy() {
        presenter?.destroyDisposables()
        presenter?.detachView()
        super.onDestroy()
    }
}

private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}