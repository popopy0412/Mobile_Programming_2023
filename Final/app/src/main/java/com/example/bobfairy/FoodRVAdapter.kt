package com.example.bobfairy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bobfairy.databinding.RowRecipeBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodRVAdapter(val items: ArrayList<Food>) : RecyclerView.Adapter<FoodRVAdapter.ViewHolder>(){

    var itemClickListener: OnItemClickListener? = null
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
    inner class ViewHolder(val binding: RowRecipeBinding) : RecyclerView.ViewHolder(binding.root){
        init{
            binding.bookmark.setOnClickListener {
                items[adapterPosition].bookmark = !items[adapterPosition].bookmark
                notifyItemChanged(adapterPosition)
            }
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.foodName.text = items[position].name
        holder.binding.foodName.isSelected = true
        holder.binding.foodType.text = items[position].type
        holder.binding.foodCalorie.text = items[position].cal
        holder.binding.bookmark.setImageResource(if (items[position].bookmark) R.drawable.like else R.drawable.notlike)
        Picasso.get().load(items[position].image).into(holder.binding.foodImage)
    }

    override fun getItemCount(): Int = items.size
}