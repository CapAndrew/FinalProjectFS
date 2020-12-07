package app.com.finalprojectfs.history.ui

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.com.finalprojectfs.R
import app.com.finalprojectfs.history.domain.entity.LoanItem

class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var date: TextView = itemView.findViewById(R.id.loan_date)
    private var state: TextView = itemView.findViewById(R.id.loan_state)
    private var amount: TextView = itemView.findViewById(R.id.loan_amount)

    fun bind(item: LoanItem) {
        date.text = item.date
        state.text = item.state
        amount.text = item.amount
    }
}