package com.example.bobfairy

import android.location.Location
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bobfairy.databinding.ActivityDateBaseBinding
import com.example.bobfairy.databinding.RestaurantRowBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.math.round

class restaurantFBAdapter(val options : FirebaseRecyclerOptions<PlaceData>)
    : FirebaseRecyclerAdapter<PlaceData, restaurantFBAdapter.ViewHolder>(options) {
    interface OnItemClickListener{
        fun OnItemClick(data: PlaceData, position: Int)
        fun OnLikeClick(data: PlaceData, position: Int)
    }
    var itemClickListener:OnItemClickListener?=null

    inner class ViewHolder(val binding: RestaurantRowBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.row.setOnClickListener {
                itemClickListener?.OnItemClick(options.snapshots[absoluteAdapterPosition], adapterPosition)
            }

            binding.bookmark.setOnClickListener {
                itemClickListener?.OnLikeClick(options.snapshots[absoluteAdapterPosition], adapterPosition)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RestaurantRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, items:PlaceData) {
        holder.binding.apply {
            if (items.photos != null) {
                Glide.with(holder.itemView.context)
                    .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" +
                            items.photos[0].photo_reference + "&key=" + BuildConfig.MAPS_API_KEY)
                    .into(holder.binding.restaurantImg);
            }

            holder.binding.name.text = items.name

            val targetLocation = items.geometry?.location
            val results = FloatArray(1)
            //Location.distanceBetween(curLocation.latitude, curLocation.longitude, targetLocation.lat, targetLocation.lng, results)

            holder.binding.distance.text = ""

            holder.binding.ratingBar.max = 500
            holder.binding.ratingBar.progress = (items.rating * 100).toInt()//round().toInt()

            holder.binding.bookmark.setImageResource(if (items.is_like) R.drawable.like else R.drawable.notlike)

        }
    }
}