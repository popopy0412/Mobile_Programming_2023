package com.example.bobfairy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bobfairy.databinding.ActivityMainBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val foods = ArrayList<Food>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        init()
        binding.button.setOnClickListener {
            try {
                val category = intent.getStringExtra("category")
                val like = intent.getStringExtra("like")

                val intent = Intent(this@MainActivity, FoodList::class.java)
                intent.putExtra("category", category)
                intent.putExtra("like", like)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
                Log.e("catch", e.toString())
            }
        }
        binding.update.setOnClickListener {
            try {
                val intent = Intent(this@MainActivity, DateBase::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
                Log.e("catch", e.toString())
            }
        }

    }

    private fun getAPI() {
        val rdb = Firebase.database.getReference("Food/Recipe")
        val queue = Volley.newRequestQueue(this)
        //val CHNG_DT = getDate()
        var count = -1
        for(i in 0..1) {
            val KEY_ID = "dadc5ae904c74ccb927d"
            val SERVICE_ID = "COOKRCP01"
            val START_IDX = "${i*1000+1}"
            val END_IDX = "${(i+1)*1000}"
            Log.i("start", START_IDX)
            Log.i("end", END_IDX)
            val URL = "https://openapi.foodsafetykorea.go.kr/api/$KEY_ID/$SERVICE_ID/json/$START_IDX/$END_IDX"
            val query = object : JsonObjectRequest(
                Method.GET, URL, null,
                Response.Listener {
                    Log.i("start", "start")
                    val isEnd = it.getJSONObject(SERVICE_ID).getJSONObject("RESULT").getString("CODE") == "INFO-200"
                    if(isEnd) return@Listener
                    val items = it.getJSONObject(SERVICE_ID).getJSONArray("row")
                    val msg = it.getJSONObject(SERVICE_ID).getJSONObject("RESULT").getString("MSG")
                    Log.i("msg", msg)
                    count += items.length()
                    Log.i("count", count.toString())
                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        for(j in item.keys()){
                            val value = item.getString(j)
//                            Log.i("value", value)
                            rdb.child(item.getString("RCP_NM").replace(".", "")).child(j).setValue(value)
                        }
                        rdb.child(item.getString("RCP_NM").replace(".", "")).child("BOOKMARK").setValue("false")
                    }
                },
                Response.ErrorListener {
                    Log.e("why", it.toString())
                }
            ) {}
            queue.add(query)
        }
    }

    private fun getDate(): String {
        val date = LocalDate.now()
        return date.format(DateTimeFormatter.ofPattern("YYYYMMdd"))
    }

    //=========================================================================

}