package com.shanu.mypocket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class SubTransaction : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_transaction)

        val labelInput = findViewById<TextInputEditText>(R.id.labelInput)
        val descriptionInput = findViewById<TextInputEditText>(R.id.descriptionInput)
        val amountInput = findViewById<TextInputEditText>(R.id.amountInput)

        val labelLayout = findViewById<TextInputLayout>(R.id.labelLayout)
        val amountLayout = findViewById<TextInputLayout>(R.id.amountLayout)

        val addTransactionBtn = findViewById<Button>(R.id.addTransactionBtn)

        val closeBtn = findViewById<ImageButton>(R.id.closeBtn)

        labelInput.addTextChangedListener {
            if(it!!.isNotEmpty()) {
                labelLayout.error = null
            }
        }

        amountInput.addTextChangedListener {
            if(it!!.isNotEmpty()) {
                amountLayout.error = null
            }
        }

        addTransactionBtn.setOnClickListener {
            val label = labelInput.text.toString()
            val description = descriptionInput.text.toString()
            var amount = amountInput.text.toString().toDoubleOrNull()

//            remove any signs from tha amount only keep decimal numbers
            if(amount != null) {
                amount = amount.toString().replace(Regex("[^0-9.]"), "").toDouble()
            }

//            add "-" sign to amount if it is a sub transaction
            if (amount != null) {
                amount *= -1
            }

            if(label.isEmpty())
                labelLayout.error = "Please enter a valid label"

            else if(amount == null)
                amountLayout.error = "Please enter a valid amount"
            else {
                val transaction  = Transactions(0, label= label, amount =  amount, description = description, date = SimpleDateFormat("dd-MM-yyyy").format(System.currentTimeMillis()))
                insertData(transaction)
            }
        }

        closeBtn.setOnClickListener {
            finish()
        }
    }

    private fun insertData(Transaction: Transactions) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "transactions"
        ).build()

        GlobalScope.launch {
            db.transactionDao().insert(Transaction)
//            fetchAllData()
            finish()
        }
    }

}