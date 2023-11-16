package com.shanu.mypocket

import androidx.room.Dao
import androidx.room.*

@Dao
interface tansactionDao {
    @Query("SELECT * FROM transactions")
    fun getAll(): List<Transactions>

    @Insert
    fun insert(vararg transaction: Transactions)

    @Delete
    fun delete(transaction: Transactions)

    @Update
    fun update(vararg transaction: Transactions)
}