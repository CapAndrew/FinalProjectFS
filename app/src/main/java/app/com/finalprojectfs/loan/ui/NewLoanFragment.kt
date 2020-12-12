package app.com.finalprojectfs.loan.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import app.com.finalprojectfs.R
import app.com.finalprojectfs.history.ui.HistoryFragment
import app.com.finalprojectfs.loan.di.NewLoanPresenterFactory
import app.com.finalprojectfs.loan.model.entity.LoanConditionsData
import app.com.finalprojectfs.loan.model.entity.NewLoanData
import app.com.finalprojectfs.loan.presentation.NewLoanPresenter
import app.com.finalprojectfs.login.ui.LoginFragment
import kotlinx.android.synthetic.main.activity_main.*
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
        val bundle = arguments
        authToken = bundle?.getString("authToken").toString()

        Log.d("AuthToken", "From onAcCrea newLoan $authToken")

        initPresenter()
        initViews()
        presenter?.fetchLoanConditions(authToken)

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

    fun updateLoanConditions(conditions: LoanConditionsData) {
        maxAmount = conditions.maxAmount!!

        loan_conditions_max_amount?.text = maxAmount.toString()
        loan_conditions_percent?.text = conditions.percent.toString()
        loan_conditions_period?.text = conditions.period.toString()
    }

    fun showProgress() {
        activity?.main_loading?.visibility = View.VISIBLE
    }

    fun hideProgress() {
        activity?.main_loading?.visibility = View.GONE
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
                loan_amount.text = presenter?.calculateAmountForSeekBar(p1, maxAmount)

                presenter?.onNewLoanDataUpdated(
                    loan_amount.text.toString().toInt(),
                    loan_last_name.text.toString(),
                    loan_first_name.text.toString(),
                    loan_phone.text.toString()
                )
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

    fun showErrorDialogWithTwoButtons(
        errorTitle: String,
        errorText: String,
        positiveButtonName: String,
        negativeButtonName: String
    ) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.error_dialog, null)
        dialogBuilder.setView(dialogView)

        dialogBuilder.setTitle(errorTitle)
        dialogBuilder.setMessage(errorText)
        dialogBuilder.setPositiveButton(positiveButtonName) { _, _ ->
            presenter?.fetchLoanConditions(authToken)
        }
        dialogBuilder.setNegativeButton(negativeButtonName) { _, _ ->
            activity?.finish()
        }

        dialogBuilder.setOnCancelListener {
            activity?.finish()
        }

        dialogBuilder
            .create()
            .show()
    }

    fun showAuthorizationErrorDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.error_dialog, null)
        dialogBuilder.setView(dialogView)

        dialogBuilder.setTitle("Ошибка авторизации")
        dialogBuilder.setMessage("Время сессии истекло. Авторизуйтесь заново.")
        dialogBuilder.setPositiveButton("Авторизоваться") { _, _ ->
            presenter?.clearAuthorization()
        }

        dialogBuilder.setOnCancelListener {
            activity?.finish()
        }

        dialogBuilder
            .create()
            .show()
    }

    fun showSuccessDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.error_dialog, null)
        dialogBuilder.setView(dialogView)

        dialogBuilder.setTitle("Успех!")
        dialogBuilder.setMessage("Заём успешно оформлен.")
        dialogBuilder.setPositiveButton("К истории") { _, _ ->
            openHistory(authToken)
        }

        dialogBuilder.setOnCancelListener {
            activity?.finish()
        }

        dialogBuilder
            .create()
            .show()
    }

    fun enableNewLoanButton(enable: Boolean) {
        newLoanButton.isEnabled = enable
    }

    fun openLogin() {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.container, LoginFragment.newInstance())
            ?.commit()
    }

    fun showActionFailed(errorText: String) {
        Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show()
    }

    fun showActionSuccess(successText: String) {
        Toast.makeText(context, successText, Toast.LENGTH_SHORT).show()
    }

    fun openHistory(authToken: String) {
        val fragment = HistoryFragment.newInstance()
        val bundle = Bundle()
        bundle.putString("authToken", authToken)
        fragment.arguments = bundle

        fragmentManager?.beginTransaction()
            ?.replace(R.id.container, fragment)
            ?.commit()
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