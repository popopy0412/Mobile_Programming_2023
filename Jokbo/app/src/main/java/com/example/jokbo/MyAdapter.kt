package com.example.jokbo

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jokbo.databinding.RowBinding

class MyAdapter(val items: ArrayList<Person>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    var itemClickListener: onItemClickListener? = null
    var imageClickListener: onItemClickListener? = null
    lateinit var i: Intent

    interface onItemClickListener{
        fun onItemClick(holder: ViewHolder, position: Int)
    }
    inner class ViewHolder(val binding: RowBinding) : RecyclerView.ViewHolder(binding.root){
        init{
            binding.name.setOnClickListener{
                val position = adapterPosition
                itemClickListener?.onItemClick(this, position)
            }
            binding.image.setOnClickListener{
                val position = adapterPosition
                imageClickListener?.onItemClick(this, position)
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
        holder.binding.name.text = items[position].name
        holder.binding.company.text = items[position].company
        holder.binding.tel.text = items[position].tel
        holder.binding.btn.text = items[position].cnt.toString()
    }




}