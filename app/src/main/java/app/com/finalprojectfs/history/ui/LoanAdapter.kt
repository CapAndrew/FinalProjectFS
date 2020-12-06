package app.com.finalprojectfs.history.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.com.finalprojectfs.R
import app.com.finalprojectfs.history.domain.entity.LoanItem

class LoanAdapter(private val onClickListener: (View, LoanItem) -> Unit) : RecyclerView.Adapter<LoanViewHolder>() {
    
    private val loansList = mutableListOf<LoanItem>()

    fun updateItem(newItems: List<LoanItem>) {
        loansList.clear()
        loansList.addAll(newItems)
        notifyDataSetChanged()
    }

    fun clearItem() {
        loansList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.loan_item, parent, false)
        return LoanViewHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        return loansList.size
    }

    override fun onBindViewHolder(holder: LoanViewHolder, position: Int) {
        val loanItem: LoanItem = loansList[position]
        holder.bind(loanItem)
        holder.itemView.setOnClickListener { view ->
            onClickListener.invoke(view, loanItem)
        }
    }
}