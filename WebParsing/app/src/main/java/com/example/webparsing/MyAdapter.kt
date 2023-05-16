package com.example.webparsing

import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.webparsing.databinding.RowBinding

class MyAdapter(val items: ArrayList<MyData>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    interface OnItemClickListener{
        fun OnItemClick(position: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val binding: RowBinding) : RecyclerView.ViewHolder(binding.root){
        init{
            binding.newstitle.setOnClickListener {
                itemClickListener?.OnItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.newstitle.text = items[position].newstitle
    }
}