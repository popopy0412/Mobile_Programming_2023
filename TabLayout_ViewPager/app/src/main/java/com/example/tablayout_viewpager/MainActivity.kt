package com.example.tablayout_viewpager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tablayout_viewpager.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    val textarr = arrayListOf("이미지", "리스트", "팀 소개")
    val imgarr = arrayListOf(R.drawable.baseline_casino_24, R.drawable.baseline_local_hospital_24, R.drawable.baseline_list_alt_24)
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        binding.viewpager.adapter = MyViewPagerAdapter(this)
        TabLayoutMediator(binding.tablayout, binding.viewpager){
            tab, pos ->
            tab.text = textarr[pos]
            tab.setIcon(imgarr[pos])
        }.attach()
    }
}