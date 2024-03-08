package com.example.bobfairy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.example.bobfairy.databinding.RowRecipeBinding
import com.squareup.picasso.Picasso

class FoodAdapter(options : FirebaseRecyclerOptions<Food>, val name: String)
    : FirebaseRecyclerAdapter<Food, FoodAdapter.ViewHolder>(options) {
    interface OnItemClickListener{
        fun onItemClick(position: Int, name: String)
    }
    var itemClickListener: OnItemClickListener? = null
    var bookmarkClickListener: OnItemClickListener? = null

    inner class ViewHolder(val binding: RowRecipeBinding): RecyclerView.ViewHolder(binding.root){
        val param = ConstraintLayout.LayoutParams(0, 0)
        init {
            binding.root.setOnClickListener{
                itemClickListener!!.onItemClick(bindingAdapterPosition, binding.foodName.text.toString())
            }
            binding.bookmark.setOnClickListener {
                bookmarkClickListener!!.onItemClick(bindingAdapterPosition, binding.foodName.text.toString())
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowRecipeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Food) {
        if(name.isNotEmpty() && (!model.name.contains(name) || name == "noinfo")){
            holder.binding.root.visibility = View.GONE
            holder.binding.root.layoutParams = holder.param
            return
        }
        else {
            holder.binding.foodName.text = model.name
            holder.binding.foodName.isSelected = true
            holder.binding.foodType.text = model.type
            holder.binding.foodCalorie.text = model.cal + " kcal"
            holder.binding.bookmark.setImageResource(if (model.bookmark) R.drawable.like else R.drawable.notlike)
            Picasso.get().load(model.image).into(holder.binding.foodImage)
        }
    }
}