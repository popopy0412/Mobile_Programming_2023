package com.example.webparsing

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.webparsing.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.parser.Parser

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: MyAdapter
    val data = ArrayList<MyData>()
    val url = "https://www.melon.com/chart/index.htm"
    val rssUrl = "http://fs.jtbc.co.kr/RSS/culture.xml"
    val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    fun getRSSNews(){
        scope.launch {
            adapter.items.clear()
            val doc = Jsoup.connect(rssUrl).parser(Parser.xmlParser()).get()
//            Log.i("check", doc.toString())
            val headlines = doc.select("item")
            for(news in headlines){
                adapter.items.add(MyData(news.select("title").text(), news.select("link").text()))
            }
            withContext(Dispatchers.Main){
                adapter.notifyDataSetChanged()
                binding.swipe.isRefreshing = false
            }
        }
    }

    fun getNews(){
        scope.launch {
            adapter.items.clear()
            val doc = Jsoup.connect(url).get()
//            Log.i("check", doc.toString())
            val songs = doc.select("div>div.clfix>div.my_fold>div>div>form>div.service_list_song.type02.d_song_list>table>tbody>tr")
//            Log.i("check", songs.text())
            for(song in songs){
                adapter.items.add(MyData(song.select("td>div.wrap>div.wrap_song_info>div.ellipsis.rank01>span>a").text(), song.select("td>div.wrap>div.wrap_song_info>div.ellipsis.rank02>span>a").text()))
            }
            withContext(Dispatchers.Main){
                adapter.notifyDataSetChanged()
                binding.swipe.isRefreshing = false
            }
        }
    }

    private fun initLayout() {
        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = true
            getNews()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        adapter = MyAdapter(data)
        adapter.itemClickListener = object: MyAdapter.OnItemClickListener {
            override fun OnItemClick(position: Int) {
                Toast.makeText(this@MainActivity, data[position].url, Toast.LENGTH_SHORT).show()
            }
        }
        binding.recyclerView.adapter = adapter
        getNews()
    }
}