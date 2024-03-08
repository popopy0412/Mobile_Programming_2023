package com.example.bobfairy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bobfairy.databinding.ActivityRecipeBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class RecipeList : AppCompatActivity() {
    lateinit var binding: ActivityRecipeBinding
    lateinit var adapter: RecipeAdapter
    lateinit var rdb: DatabaseReference
    lateinit var food: Food
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        val intent = intent
        @Suppress("DEPRECATION")
        food = intent.getSerializableExtra("food") as Food
        rdb = Firebase.database.getReference("Food/Recipe/${food.name}")

        binding.apply{
            foodName.text = food.name
            foodName.isSelected = true
            foodType.text = food.type
            foodCalorie.text = food.cal
            foodIngredient.text = food.ingredient
            bookmark.setImageResource(if(food.bookmark) R.drawable.like else R.drawable.notlike)
            Picasso.get().load(food.image).into(foodImage)

            adapter = RecipeAdapter(food.steps)
            recyclerView.layoutManager = LinearLayoutManager(this@RecipeList, LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = adapter

            prev.setOnClickListener {
                finish()
            }
            bookmark.setOnClickListener {
                food.bookmark = !food.bookmark
                bookmark.setImageResource(if(food.bookmark) R.drawable.like else R.drawable.notlike)
                rdb.child("BOOKMARK").setValue(if(food.bookmark) "true" else "false")
            }
        }
    }
}