package com.example.voca

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.voca.databinding.RowBinding

class MyDataAdapter(val items: ArrayList<MyData>) : RecyclerView.Adapter<MyDataAdapter.ViewHolder>() {
    var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener{
        fun OnItemClick(holder: ViewHolder, position: Int)
    }
    inner class ViewHolder(val binding: RowBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.textView.setOnClickListener{
                val position = adapterPosition
                itemClickListener?.OnItemClick(this, position)
            }
        }
    }

    fun moveItem(oldPosition: Int, newPosition: Int){
        val temp = items[oldPosition]
        items.removeAt(oldPosition)
        items.add(newPosition, temp)
        notifyItemMoved(oldPosition, newPosition)
    }

    fun removeItem(position: Int){
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textView.text = items[position].word
        holder.binding.meaning.text = items[position].meaning
        holder.binding.meaning.visibility = if(items[position].viewType) TextView.VISIBLE else TextView.GONE
    }

    override fun getItemCount(): Int {
        return items.size
    }

}