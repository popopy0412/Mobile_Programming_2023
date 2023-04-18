package com.example.voca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voca.databinding.ActivityMainBinding
import com.example.voca.databinding.RowBinding
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: MyDataAdapter
    lateinit var tts: TextToSpeech
    var isTTSReady = false
    val data: ArrayList<MyData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initRecyclerView()
        initTTS()
    }

    private fun initTTS(){
        tts = TextToSpeech(this){
            isTTSReady = true
            tts.language = Locale.US
        }
    }

    fun readScanFile(scan: Scanner){
        while(scan.hasNextLine()){
            val word = scan.nextLine()
            val meaning = scan.nextLine()
            data.add(MyData(word, meaning, false))
        }
    }

    fun initData(){
        try {
            val scan2 = Scanner(openFileInput("voc.txt"))
            readScanFile(scan2)
        } catch (e: Exception) {
            Toast.makeText(this, "추가한 단어가 없습니다", Toast.LENGTH_SHORT).show()
        }
        val scan = Scanner(resources.openRawResource(R.raw.words))
        readScanFile(scan)
    }

    fun initRecyclerView(){
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = MyDataAdapter(data)
        adapter.itemClickListener = object: MyDataAdapter.OnItemClickListener{
            override fun OnItemClick(holder: MyDataAdapter.ViewHolder, position: Int) {
                data[position].viewType = !(data[position].viewType)
                if(isTTSReady) tts.speak(holder.binding.textView.text.toString(), TextToSpeech.QUEUE_ADD, null, null)
                adapter.notifyItemChanged(position)
            }
        }
        binding.recyclerView.adapter = adapter

        val simpleCallback = object: ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                adapter.moveItem(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeItem(viewHolder.adapterPosition)
            }

        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    override fun onStop() {
        super.onStop()
        tts.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.shutdown()
    }
}