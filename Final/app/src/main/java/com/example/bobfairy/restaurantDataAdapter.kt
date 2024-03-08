package com.example.bobfairyq

import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bobfairy.BuildConfig
import com.example.bobfairy.Food
import com.example.bobfairy.PlaceData
import com.example.bobfairy.R
import com.example.bobfairy.databinding.RestaurantRowBinding
import com.example.bobfairy.query
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.math.log
import kotlin.math.round

class restaurantDataAdapter(val items:ArrayList<PlaceData>, val curLocation: LatLng) : RecyclerView.Adapter<restaurantDataAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun OnItemClick(data: PlaceData, position: Int) {}

        fun OnLikeClick(data: PlaceData, position: Int) {}
    }

    var itemClickListener:OnItemClickListener ?= null

    inner class ViewHolder(var binding: RestaurantRowBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.row.setOnClickListener {
                itemClickListener?.OnItemClick(items[adapterPosition], adapterPosition)
            }

            binding.bookmark.setOnClickListener {
                itemClickListener?.OnLikeClick(items[adapterPosition], adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RestaurantRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (items[position].photos != null) {
            Glide.with(holder.itemView.context)
                .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" +
                        items[position].photos!![0].photo_reference + "&key=" + BuildConfig.MAPS_API_KEY)
                .into(holder.binding.restaurantImg);
        }

        holder.binding.name.text = items[position].name

        val targetLocation = items[position].geometry?.location
        val results = FloatArray(1)
        Location.distanceBetween(curLocation.latitude, curLocation.longitude, targetLocation!!.lat, targetLocation.lng, results)

        holder.binding.distance.text = (round(results[0] * 100) / 100).toString() + "m"

        holder.binding.ratingBar.max = 500
        holder.binding.ratingBar.progress = (items[position].rating * 100).toInt()

        holder.binding.bookmark.setImageResource(if (items[position].is_like) R.drawable.like else R.drawable.notlike)

    }
}