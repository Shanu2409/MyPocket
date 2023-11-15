package com.shanu.mypocket

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AddTransactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

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
            val amount = amountInput.text.toString().toDoubleOrNull()

            if(label.isEmpty())
                labelLayout.error = "Please enter a valid label"

            else if(amount == null)
                amountLayout.error = "Please enter a valid amount"
//            else {
//                val transaction  =Transaction(0, label, amount, description)
//                insert(transaction)
//            }
        }

        closeBtn.setOnClickListener {
            finish()
        }
    }

//    private fun insert(transaction: Transaction){
//        val db = Room.databaseBuilder(this,
//            AppDatabase::class.java,
//            "transactions").build()
//
//        GlobalScope.launch {
//            db.transactionDao().insertAll(transaction)
//            finish()
//        }
//    }
}