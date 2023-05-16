package com.example.roomdb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdb.databinding.RowBinding

class MyAdapter(var items: ArrayList<Product>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    var itemClickListener: OnItemClickListener? = null
    interface OnItemClickListener{
        fun onClick(position: Int)
    }
    inner class ViewHolder(val binding: RowBinding) : RecyclerView.ViewHolder(binding.root){
        init{
            binding.layout.setOnClickListener {
                itemClickListener?.onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.ViewHolder {
        val view = RowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyAdapter.ViewHolder, position: Int) {
        holder.binding.pId.text = items[position].pId.toString()
        holder.binding.pName.text = items[position].pName
        holder.binding.pQuantity.text = items[position].pQuantity.toString()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}