package com.shanu.mypocket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class TransactionAdapter(private val transactions: List<Transactions>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label : TextView = view.findViewById(R.id.label)
        val amount : TextView = view.findViewById(R.id.amount)
        val date : TextView = view.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_layout, parent, false)
        return TransactionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        val context = holder.amount.context

        if (transaction.amount >= 0) {
            holder.amount.text = "+ $%.2f".format(abs(transaction.amount))
            holder.amount.setTextColor(context.getColor(R.color.green))

        } else {
            holder.amount.text = "- $%.2f".format(abs(transaction.amount))
            holder.amount.setTextColor(context.getColor(R.color.red))
        }
        holder.label.text = transaction.label
        holder.amount.text = transaction.amount.toString()
        holder.date.text = transaction.date

    }


}