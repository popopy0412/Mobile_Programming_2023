package com.example.bobfairy

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.bobfairy.databinding.ActivitySelectBinding

class SelectActivity : AppCompatActivity() {
    lateinit var binding : ActivitySelectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySelectBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        selectLayout()

    }

    private fun selectLayout(){

        val category = intent.getStringExtra("category")
        val likeFood = intent.getStringExtra("like")

        val restaurantLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode== Activity.RESULT_OK) {
                finish()
            }
        }

        binding.apply{

            prev.setOnClickListener {
                finish()
            }

            recipebtn.setOnClickListener {
                val i = Intent( this@SelectActivity, FoodList::class.java)
                i.putExtra("category", category)
                i.putExtra("like", likeFood)
                startActivity(i)
                // category 정보와 함께 레시피를 알려주는 Layout 으로 intent
            }

            storebtn.setOnClickListener {

                val i = Intent(this@SelectActivity, MapActivity::class.java)
                i.putExtra("category", category)
                i.putExtra("like", likeFood)
                restaurantLauncher.launch(i)
                // category 정보와 함께 음식점을 찾는 Layout 으로 intent
            }
        }

    }
}