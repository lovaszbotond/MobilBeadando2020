package com.example.iforgotsomething.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.iforgotsomething.data.ForgotDatabase
import com.example.iforgotsomething.data.models.ForgotData
import com.example.iforgotsomething.data.repository.ForgotRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

//Android view model contains the application context
@InternalCoroutinesApi
class ForgotViewModel(application: Application): AndroidViewModel(application) {

    @InternalCoroutinesApi
    private val forgotDao = ForgotDatabase.getDatabase(application).forgotDao()

    private val repository: ForgotRepository

    val getAllData: LiveData<List<ForgotData>>

    val sortByHighPriority: LiveData<List<ForgotData>>
    val sortByLowPriority: LiveData<List<ForgotData>>

    //When the view model initialized first -> We wanna get the data's from the repository!
    init{
        repository = ForgotRepository(forgotDao)
        getAllData = repository.getAllData
        sortByHighPriority = repository.sortByHighPriority
        sortByLowPriority = repository.sortByLowPriority
    }

    fun insertData(forgotData: ForgotData){

        viewModelScope.launch(Dispatchers.IO){
            repository.insertData(forgotData)
        }
    }


    fun updateData(forgotData: ForgotData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(forgotData)
        }
    }

    fun deleteItem(forgotData: ForgotData){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteItem(forgotData)
        }
    }

    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAll()
        }
    }


    fun searchDatabase(searchQuery: String): LiveData<List<ForgotData>>{
        return repository.searchDatabase(searchQuery)
    }

}