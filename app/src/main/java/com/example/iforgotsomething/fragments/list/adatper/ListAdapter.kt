package com.example.iforgotsomething.fragments.list.adatper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.iforgotsomething.data.models.ForgotData
import com.example.iforgotsomething.databinding.RowLayoutBinding

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    internal var dataList = emptyList<ForgotData>()

    //Extends RecyclerView ViewHolder
    class MyViewHolder(private val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(forgotData: ForgotData) {
            binding.forgotData = forgotData
            //Update views in rowLayout
            binding.executePendingBindings()
        }
        //Return inflated layout
        companion object{
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowLayoutBinding.inflate(layoutInflater, parent,false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //get currentItem from forgotData dynamcly ( passed data )
        val currentItem = dataList[position]
        holder.bind(currentItem)
    }


    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(forgotData: List<ForgotData>){
        val forgotDiffUtil = ForgotDiffUtil(dataList, forgotData)
        val forgotDiffResult = DiffUtil.calculateDiff(forgotDiffUtil)

        this.dataList = forgotData
        forgotDiffResult.dispatchUpdatesTo(this)
    }

}