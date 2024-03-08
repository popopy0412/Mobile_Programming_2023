package com.example.bobfairy

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.bobfairy.databinding.ActivityRestaurantDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

class RestaurantDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityRestaurantDetailBinding

    val retrofit = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/maps/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(PlaceAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail)

        binding = ActivityRestaurantDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initWebView()
        initUi()
    }
    fun initUi() {
        binding.confirmBtn.setOnClickListener {
            setResult(Activity.RESULT_OK, Intent())
            finish()
        }
    }

    fun initWebView() {
        val param = mapOf(
            "place_id" to intent.getStringExtra("placeId")!!,
            "key" to BuildConfig.MAPS_API_KEY,
            "language" to "ko",
        )

//        binding.web.webViewClient = object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//                view.loadUrl(url)
//                return false
//            }
//        }

        api.getPlaceDetail(param).enqueue(object : Callback<apiDetailResponse> {
            override fun onResponse(
                call: Call<apiDetailResponse>,
                response: Response<apiDetailResponse>
            ) {
                Log.d("TAG", "성공 : ${response.body()}")
                binding.web.settings.javaScriptEnabled = true
                binding.web.loadUrl(response.body()!!.result.url)
            }

            override fun onFailure(call: Call<apiDetailResponse>, t: Throwable) {
                Log.d("TAG", "실패 : $t")
            }
        })
    }
}