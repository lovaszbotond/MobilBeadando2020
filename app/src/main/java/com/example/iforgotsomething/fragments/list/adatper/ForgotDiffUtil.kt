package com.example.iforgotsomething.fragments.list.adatper

import androidx.recyclerview.widget.DiffUtil
import com.example.iforgotsomething.data.models.ForgotData

class ForgotDiffUtil(
    private val oldList: List<ForgotData>,
    private val newList: List<ForgotData>
) : DiffUtil.Callback()
{
    // Size of old List
    override fun getOldListSize(): Int {
        return oldList.size
    }

    // Size of the new List
    override fun getNewListSize(): Int {
        return newList.size
    }

    // same item check, or id check
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldList[oldItemPosition] === newList[newItemPosition]
    }

    // same data -> only if the 3rd method is true
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return   oldList[oldItemPosition].id == newList[newItemPosition].id                       &&
                 oldList[oldItemPosition].title == newList[newItemPosition].title                 &&
                 oldList[oldItemPosition].description == newList[newItemPosition].description     &&
                 oldList[oldItemPosition].priority == newList[newItemPosition].priority

    }

}