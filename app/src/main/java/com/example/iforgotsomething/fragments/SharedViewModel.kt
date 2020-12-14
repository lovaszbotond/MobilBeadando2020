package com.example.iforgotsomething.fragments

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.iforgotsomething.R
import com.example.iforgotsomething.data.models.ForgotData
import com.example.iforgotsomething.data.models.Priority

//CleanCode!
class SharedViewModel(application: Application): AndroidViewModel(application) {

    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)

    // Empty not empty game
    fun checkIfDatabaseEmpty(forgotData: List<ForgotData>){
        emptyDatabase.value = forgotData.isEmpty()
    }

    val listener: AdapterView.OnItemSelectedListener = object :
        AdapterView.OnItemSelectedListener{

        override fun onNothingSelected(p0: AdapterView<*>?) {}

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when(position){
                0 -> {(parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.red))}
                1 -> {(parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.yellow))}
                2 -> {(parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.green))}
            }
            }
        }


    //Empty or not Empty that is the question
     fun verifyDataFromUser(cim: String, tartalom: String): Boolean {
        //Simplify version of code
        return !(cim.isEmpty() || tartalom.isEmpty())

        //Confused version of code
        /*
        /    return if(TextUtils.isEmpty(cim) || TextUtils.isEmpty(tartalom)){
        /           false
        /       }
        /           else !(cim.isEmpty() || tartalom.isEmpty())
        */
    }

    //Also a converter
     fun parsePriority(priority: String) : Priority {

        return when(priority){
            "Fontos" -> {
                Priority.HIGH}
            "Kevesbe Fontos" -> {
                Priority.MEDIUM}
            "Raer" -> {
                Priority.LOW}
            else -> Priority.LOW
        }
    }

}