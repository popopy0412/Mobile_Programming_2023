package com.example.intentfilter

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intentfilter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initReCyclerView()
    }

    private fun initReCyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = MyAdapter(ArrayList())
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val applist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.queryIntentActivities(intent, PackageManager.ResolveInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            packageManager.queryIntentActivities(intent, 0)
        }

        if(applist.size > 0){
            for(appinfo in applist){
                var applabel: String = appinfo.loadLabel(packageManager).toString()
                var appclass: String = appinfo.activityInfo.name
                var apppackname: String = appinfo.activityInfo.packageName
                var appicon: Drawable = appinfo.loadIcon(packageManager)

                adapter.items.add(MyData(applabel, appclass, apppackname, appicon))
            }
        }

        adapter.itemClickListener = object: MyAdapter.onItemClickListener{
            override fun OnItemClick(data: MyData) {
                val intent = packageManager.getLaunchIntentForPackage(data.apppackname)
                startActivity(intent)
            }

        }

        binding.recyclerView.adapter = adapter
    }
}