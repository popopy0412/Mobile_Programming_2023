package com.example.bobfairy

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.example.bobfairy.databinding.ActivityRestaurantListBinding
import com.example.bobfairyq.restaurantDataAdapter
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantListActivity : AppCompatActivity() {
    lateinit var binding: ActivityRestaurantListBinding
    var list: apiResponse? = null
    lateinit var rdb: DatabaseReference

    val retrofit = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/maps/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(PlaceAPI::class.java)

    lateinit var adapter: restaurantDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initUI()
    }

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode== Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK, Intent())
            finish()
        }
    }

    @Suppress("DEPRECATION")
    fun initData() {
        val likeList = ArrayList<String> ()
        rdb = Firebase.database.getReference("Food/Restaurant")//참조 객체
        rdb.orderByChild("_like").equalTo(true).get().addOnSuccessListener {
            if(it.value == null) return@addOnSuccessListener
            val item = it.value!! as HashMap<*, *>
            item.keys.all {
                likeList.add(it as String)
            }
        }

        val query = intent.getSerializableExtra("query") as query
        val param = mapOf(
            "location" to (query.lat.toString() + "," + query.lng.toString()),
            "key" to BuildConfig.MAPS_API_KEY,
            "radius" to query.radius.toString(),
            "keyword" to query.keyword,
            "language" to "ko",
        )

        api.getPlace(param).enqueue(object : Callback<apiResponse> {
            override fun onResponse(
                call: Call<apiResponse>,
                response: Response<apiResponse>
            ) {
                Log.d("TAG", "성공 : ${response.body()}")
                list = response.body()

                if (list != null) {
                    val items = list!!.results
                    rdb = Firebase.database.getReference("Food/Restaurant")//참조 객체
                    val arr = ArrayList<String>()
                    rdb.orderByChild("_like").equalTo(true).get().addOnSuccessListener {
                        if(it.value != null) {
                            val item = it.value!! as HashMap<*, *>
                            item.keys.all {
                                arr.add(it as String)
                                true
                            }
                            for (item in items) {
                                for (i in arr) {
                                    if (item.place_id == i) {
                                        item.is_like = true
                                        break
                                    }
                                }
                            }
                        }
                        adapter = restaurantDataAdapter(list!!.results, LatLng(query.lat, query.lng))
                        adapter.itemClickListener = object :restaurantDataAdapter.OnItemClickListener {
                            override fun OnItemClick(data: PlaceData, position: Int) {
                                val i = Intent(this@RestaurantListActivity, RestaurantDetailActivity::class.java)
                                i.putExtra("placeId", data.place_id)
                                launcher.launch(i)
                            }

                            override fun OnLikeClick(data: PlaceData, position: Int) {
                                data.is_like = !data.is_like
                                rdb= Firebase.database.getReference("Food/Restaurant")//참조 객체
                                if(data.is_like){//찜 - 삽입
                                    val item = data
                                    rdb.child(data.place_id).setValue(item)
                                }else{//찜 x - 삭제
                                    rdb.child(data.place_id).removeValue()
                                }
                                // DB 저장
                                adapter.notifyItemChanged(position)
                            }
                        }
                        binding.recyclerView.adapter = adapter
                    }.addOnFailureListener {
                        Log.e("why", "why")
                    }
                }
            }

            override fun onFailure(call: Call<apiResponse>, t: Throwable) {
                Log.d("TAG", "실패 : $t")
            }
        })
    }

    fun initUI() {
        binding.apply {
            backBtn.setOnClickListener {
                finish()
            }
        }
    }
}