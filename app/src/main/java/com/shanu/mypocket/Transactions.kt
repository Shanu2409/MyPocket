package com.shanu.mypocket

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat

@Entity(tableName = "transactions")
data class Transactions(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val label: String,
    val amount: Double,
    val date: String = SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis()),
    val description: String = "") {

}