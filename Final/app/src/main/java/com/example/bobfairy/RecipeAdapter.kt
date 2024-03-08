package com.example.bobfairy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bobfairy.databinding.RowStepBinding
import com.squareup.picasso.Picasso

class RecipeAdapter(val items: ArrayList<Step>) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: RowStepBinding) : RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeAdapter.ViewHolder =
        ViewHolder(RowStepBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecipeAdapter.ViewHolder, position: Int) {
        holder.binding.step.text = items[position].step
        holder.binding.description.text = items[position].description
        Picasso.get().load(items[position].image).into(holder.binding.image)
    }

    override fun getItemCount(): Int = items.size
}