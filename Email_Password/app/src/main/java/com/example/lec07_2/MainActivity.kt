package com.example.lec07_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.lec07_2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    fun initLayout(){
        binding.emailtext.addTextChangedListener {
            if(it.toString().contains('@')){
                binding.textInputLayout.error = null
            }
            else{
                binding.textInputLayout.error = "이메일 형식이 올바르지 않습니다."
            }
        }
    }
}