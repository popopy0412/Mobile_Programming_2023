package com.example.lec09

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.lec09.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var data:ArrayList<MyData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu1, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuitem1 -> {
                binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            }
            R.id.menuitem2 -> {
                binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
            }
            R.id.menuitem3 -> {
                binding.recyclerView.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = MyDataAdapter(data)
    }

    fun initData(){
        data.add(MyData("item1", 10))
        data.add(MyData("item2", 8))
        data.add(MyData("item3", 15))
        data.add(MyData("item4", 20))
        data.add(MyData("item5", 7))
        data.add(MyData("item6", 12))
        data.add(MyData("item7", 19))
        data.add(MyData("item8", 10))
        data.add(MyData("item9", 6))
        data.add(MyData("item10", 20))
        data.add(MyData("item11", 9))
        data.add(MyData("item12", 13))
    }
}