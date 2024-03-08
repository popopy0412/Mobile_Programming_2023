package com.example.bobfairy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.bobfairy.databinding.ActivityInitMainBinding
import kotlin.random.Random

class InitMainActivity : AppCompatActivity() {

    lateinit var binding: ActivityInitMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLayout()
    }

    private fun initLayout(){


        val selectIntent = Intent(this, SelectActivity::class.java)

        binding.apply{

            korea.setOnClickListener {

                selectIntent.putExtra("category", "한식")
                startActivity(selectIntent)
            }

            japan.setOnClickListener {
                selectIntent.putExtra("category", "일식")
                startActivity(selectIntent)
            }

            china.setOnClickListener {
                selectIntent.putExtra("category", "중식")
                startActivity(selectIntent)
            }

            western.setOnClickListener {
                selectIntent.putExtra("category", "양식")
                startActivity(selectIntent)
            }

            random.setOnClickListener {

                when(getRandomNumber()){
                    1->{
                        selectIntent.putExtra("category", "한식")
                        selectIntent.putExtra("like", 0 )
                        startActivity(selectIntent)
                    }
                    2->{
                        selectIntent.putExtra("category", "일식")
                        selectIntent.putExtra("like", 0 )
                        startActivity(selectIntent)
                    }
                    3->{
                        selectIntent.putExtra("category", "중식")
                        selectIntent.putExtra("like", 0 )
                        startActivity(selectIntent)
                    }
                    4->{
                        selectIntent.putExtra("category", "양식")
                        selectIntent.putExtra("like", 0 )
                        startActivity(selectIntent)
                    }
                }

            }
            val intent = Intent(this@InitMainActivity, DateBase::class.java)
            like.setOnClickListener {
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this@InitMainActivity, "오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                        .show()
                    Log.e("catch", e.toString())
                }
            }

        }

    }

    fun getRandomNumber(): Int {
        return Random.nextInt(1, 4)
    }

}