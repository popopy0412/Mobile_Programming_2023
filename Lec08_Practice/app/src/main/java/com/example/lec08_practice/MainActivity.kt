package com.example.lec08_practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import androidx.core.view.isVisible
import com.example.lec08_practice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var checkboxArr: ArrayList<CheckBox>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    fun init(){
        checkboxArr = arrayListOf(binding.check1, binding.check2, binding.check3, binding.check4, binding.check5,
            binding.check6, binding.check7, binding.check8, binding.check9, binding.check10)
        for(checkbox in checkboxArr){
            checkbox.setOnCheckedChangeListener { button, isChecked ->
                val imgId = resources.getIdentifier(button.text.toString(), "id", packageName)
                val imgView = findViewById<ImageView>(imgId)
                imgView.visibility = if(isChecked) ImageView.VISIBLE else ImageView.INVISIBLE
            }
        }
    }
}