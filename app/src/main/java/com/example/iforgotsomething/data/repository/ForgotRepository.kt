package com.example.iforgotsomething.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.iforgotsomething.data.ForgotDao
import com.example.iforgotsomething.data.models.ForgotData

//Get the reference for Dao interface
class ForgotRepository(private val forgotDao: ForgotDao) {

    // Hold the reference from our dao to our repository
    val getAllData: LiveData<List<ForgotData>> = Transformations.map(forgotDao.getAllData()) {
        it
    }


          //  forgotDao.getAllData()

    val sortByHighPriority: LiveData<List<ForgotData>> = forgotDao.sortByHighPriority()
    val sortByLowPriority: LiveData<List<ForgotData>> = forgotDao.sortByLowPriority()

    suspend fun insertData(forgotData: ForgotData){

        forgotDao.insertData(forgotData)
    }

    suspend fun updateData(forgotData: ForgotData){
        forgotDao.updateData(forgotData)
    }

    suspend fun deleteItem(forgotData: ForgotData){
        forgotDao.deleteItem(forgotData)
    }

    suspend fun deleteAll(){
        forgotDao.deleteAll()
    }

    fun searchDatabase(searchQuery: String): LiveData<List<ForgotData>> {
        return forgotDao.searchDatabase(searchQuery)
    }

}