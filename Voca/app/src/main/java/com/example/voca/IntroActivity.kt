package com.example.voca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.voca.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    lateinit var binding: ActivityIntroBinding
    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            val voc = it.data?.getSerializableExtra("voc") as MyData
            Toast.makeText(this, voc.word+" 추가됨", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        binding.voclistbtn.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
        binding.addvocbtn.setOnClickListener {
            val i = Intent(this, AddVocActivity::class.java)
            launcher.launch(i)
        }
    }
}