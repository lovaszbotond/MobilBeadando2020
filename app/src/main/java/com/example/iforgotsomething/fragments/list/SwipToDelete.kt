package com.example.iforgotsomething.fragments.list

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeToDelete : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    // object error if not overrided in ListFragment
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }
}