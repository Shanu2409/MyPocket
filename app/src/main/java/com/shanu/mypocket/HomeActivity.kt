package com.shanu.mypocket

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Global
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var oldTransactions: List<Transactions>
    private lateinit var deletedTransaction: Transactions
    private lateinit var transactions: List<Transactions>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var db : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        transactions = arrayListOf()

        transactionAdapter = TransactionAdapter(transactions)
        layoutManager = LinearLayoutManager(this)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)

        recyclerView.adapter = transactionAdapter
        recyclerView.layoutManager = layoutManager

        db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "transactions").build()

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

//        fetchAllData()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteTransaction(transactions[viewHolder.adapterPosition])
            }

        }

        val swipeHelper = ItemTouchHelper(itemTouchHelper)
        swipeHelper.attachToRecyclerView(recyclerView)


    }

    private fun fetchAllData() {
        GlobalScope.launch {
            transactions = db.transactionDao().getAll()
            runOnUiThread {
                transactionAdapter.setData(transactions)
                updateBalance()
            }
        }
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
        else {
            // show all with 0.00
            val balance = findViewById<TextView>(R.id.balance)
            val budgetText = findViewById<TextView>(R.id.budget)
            val expenseText = findViewById<TextView>(R.id.expanse)

            balance.text = "₹0.00"
            budgetText.text = "₹0.00"
            expenseText.text = "₹0.00"

        }
    }

    override fun onResume() {
        super.onResume()
        fetchAllData()
    }

    private fun undoDelete(){
        GlobalScope.launch {
            db.transactionDao().insert(deletedTransaction)

            transactions = oldTransactions

            runOnUiThread {
                transactionAdapter.setData(transactions)
                updateBalance()
            }
        }
    }
    private fun showSnackbar(){
        val view = findViewById<View>(com.google.android.material.R.id.coordinator)
        val snackbar = Snackbar.make(view, "Transaction deleted!",Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo"){
            undoDelete()
        }
            .setActionTextColor(ContextCompat.getColor(this, R.color.red))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .show()
    }
    private fun deleteTransaction(transaction: Transactions){
        deletedTransaction = transaction
        oldTransactions = transactions

        GlobalScope.launch {
            db.transactionDao().delete(transaction)

            transactions = transactions.filter { it.id != transaction.id }
            runOnUiThread {
                updateBalance()
                transactionAdapter.setData(transactions)
                showSnackbar()
            }
        }
    }
}
