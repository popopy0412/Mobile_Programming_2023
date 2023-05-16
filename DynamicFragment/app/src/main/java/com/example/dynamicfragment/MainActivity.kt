package com.example.dynamicfragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dynamicfragment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val imageFrag = ImageFragment()
    val itemFrag = ItemFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        val fragment = supportFragmentManager.beginTransaction()
        fragment.replace(R.id.frameLayout, imageFrag)
        fragment.commit()
        binding.apply {
            button.setOnClickListener {
                if(!imageFrag.isVisible){
                    val fragment = supportFragmentManager.beginTransaction()
                    fragment.addToBackStack(null)
                    fragment.replace(R.id.frameLayout, imageFrag)
                    fragment.commit()
                }
            }
            button2.setOnClickListener {
                if(!itemFrag.isVisible){
                    val fragment = supportFragmentManager.beginTransaction()
                    fragment.addToBackStack(null)
                    fragment.replace(R.id.frameLayout, itemFrag)
                    fragment.commit()
                }
            }
        }
    }
}