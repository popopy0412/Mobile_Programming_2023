package com.example.firebase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase.databinding.RowBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class MyProductAdapter(options: FirebaseRecyclerOptions<Product>)
    : FirebaseRecyclerAdapter<Product, MyProductAdapter.ViewHolder>(options){

    interface OnItemClickListener{
        fun OnItemClick(position: Int)
    }

    var itemClickListener : OnItemClickListener? = null

    inner class ViewHolder(val binding: RowBinding) : RecyclerView.ViewHolder(binding.root){
        init{
            binding.root.setOnClickListener {
                itemClickListener!!.OnItemClick(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProductAdapter.ViewHolder {
        return ViewHolder(RowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: MyProductAdapter.ViewHolder,
        position: Int,
        model: Product
    ) {
        holder.binding.apply {
            productid.text = model.pid.toString()
            productname.text = model.pName
            productquantity.text = model.pQuantity.toString()
        }
    }
}