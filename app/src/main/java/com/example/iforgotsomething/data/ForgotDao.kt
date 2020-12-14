package com.example.iforgotsomething.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.iforgotsomething.data.models.ForgotData


@Dao
interface ForgotDao {

    @Query("SELECT * FROM forgot_table ORDER BY id ASC")
    fun getAllData(): LiveData<List<ForgotData>>

    //Cause of this if the item which is coming is the same we are going to ignore that - suspend -> our function will be run inside the coroutine
    //We want our function to run on a background thread in The View Model!
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(forgotData: ForgotData)

    @Update
    suspend fun updateData(forgotData: ForgotData)

    @Delete
    suspend fun deleteItem(forgotData: ForgotData)

    @Query("DELETE FROM forgot_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM forgot_table WHERE title LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<ForgotData>>

    @Query("SELECT * FROM forgot_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(): LiveData<List<ForgotData>>

    @Query("SELECT * FROM forgot_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(): LiveData<List<ForgotData>>
}