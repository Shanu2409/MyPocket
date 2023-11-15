package com.shanu.mypocket

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {
    private var transactions: ArrayList<Transactions> = ArrayList()
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initTransactions()

        transactionAdapter = TransactionAdapter(transactions)
        layoutManager = LinearLayoutManager(this)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)

        recyclerView.adapter = transactionAdapter
        recyclerView.layoutManager = layoutManager

        updateBalance()

        val btnAdd = findViewById<FloatingActionButton>(R.id.addBtn)
        val btnSub = findViewById<FloatingActionButton>(R.id.subBtn)

        btnAdd.setOnClickListener {
            // give Toast of "Add"
//            Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }

        btnSub.setOnClickListener {
            // give Toast of "Sub"
//            Toast.makeText(this, "Sub", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SubTransaction::class.java)
            startActivity(intent)
        }
    }

    private fun initTransactions() {
        transactions = arrayListOf(
            Transactions("Salary", 1000.0, "12/12/2020"),
            Transactions("some", 3000.0, "12/12/2020"),
            Transactions("thing", -4000.0, "12/12/2020"),
            Transactions("i", -300.0, "12/12/2020"),
            Transactions("don't", 15500.0, "12/12/2020"),
            Transactions("know", -5200.0, "12/12/2020"),
            Transactions("food", 5200.0, "12/12/2020")
        )
    }

    private fun updateBalance() {
        if (transactions.isNotEmpty()) {
            val totalAmount: Double = transactions.map { it.amount }.sum()
            val budget: Double = transactions.filter { it.amount > 0 }.map { it.amount }.sum()
            val expense: Double = totalAmount - budget

            val balance = findViewById<TextView>(R.id.balance)
            val budgetText = findViewById<TextView>(R.id.budget)
            val expenseText = findViewById<TextView>(R.id.expanse)

            balance.text = "₹%.2f".format(totalAmount)
            budgetText.text = "₹%.2f".format(budget)
            expenseText.text = "₹%.2f".format(expense)
        }
    }
}
