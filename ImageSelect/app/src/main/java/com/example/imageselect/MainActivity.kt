package com.example.imageselect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.RadioGroup
import com.example.imageselect.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var posX: Float = 0.0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init2()
        //init()
    }
    fun init2(){
        binding.radioGroup.setOnCheckedChangeListener {
            _, checkedID ->
            when(checkedID){
                binding.radioButton1.id -> {
                    binding.imageView.setImageResource(R.drawable.extinction)
                }
                binding.radioButton2.id -> {
                    binding.imageView.setImageResource(R.drawable.gas)
                }
                binding.radioButton3.id -> {
                    binding.imageView.setImageResource(R.drawable.glacier)
                }
            }
        }
    }
    fun init(){
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val imageView = findViewById<ImageView>(R.id.imageView)
        radioGroup.setOnCheckedChangeListener { radioGroup, checkedID ->
            when(checkedID){
                R.id.radioButton1 -> {
                    imageView.setImageResource(R.drawable.extinction)
                }
                R.id.radioButton2 -> {
                    imageView.setImageResource(R.drawable.gas)
                }
                R.id.radioButton3 -> {
                    imageView.setImageResource(R.drawable.glacier)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                posX = event.rawX
            }
            MotionEvent.ACTION_UP -> {
                val distX = posX - event.rawX
                if(distX > 0){
                    when(binding.radioGroup.checkedRadioButtonId){
                        binding.radioButton1.id -> {
                            binding.radioGroup.check(binding.radioButton2.id)
                        }
                        binding.radioButton2.id -> {
                            binding.radioGroup.check(binding.radioButton3.id)
                        }
                    }
                }
                else if(distX < 0) {
                    when(binding.radioGroup.checkedRadioButtonId){
                        binding.radioButton2.id -> {
                            binding.radioGroup.check(binding.radioButton1.id)
                        }
                        binding.radioButton3.id -> {
                            binding.radioGroup.check(binding.radioButton2.id)
                        }
                    }
                }
            }
        }
        return true
    }
}