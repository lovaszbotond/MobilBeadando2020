package com.example.iforgotsomething.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.iforgotsomething.data.models.Priority
import kotlinx.android.parcel.Parcelize

//Entity class
@Entity(tableName = "forgot_table")
@Parcelize
data class ForgotData(

        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var title: String,
        var priority: Priority,
        var description: String

) : Parcelable
