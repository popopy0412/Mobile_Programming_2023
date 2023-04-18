package com.example.jokbo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jokbo.databinding.ActivityModifyInfoBinding

class ModifyInfo : AppCompatActivity() {
    lateinit var binding: ActivityModifyInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun init(){
        val i = intent
        val temp = intent.getSerializableExtra("person") as Person
        binding.name.setText(temp.name)
        binding.company.setText(temp.company)
        binding.telnumber.setText(temp.tel)

        binding.ok.setOnClickListener {
            val name = binding.name.text.toString()
            val company = binding.company.text.toString()
            val tel = binding.telnumber.text.toString()
            val pos = i.getIntExtra("position", 0)
            println(pos)
            i.putExtra("name", name)
            i.putExtra("company", company)
            i.putExtra("tel", tel)
            i.putExtra("pos", pos)
            setResult(RESULT_OK, i)
            finish()
        }
        binding.cancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}