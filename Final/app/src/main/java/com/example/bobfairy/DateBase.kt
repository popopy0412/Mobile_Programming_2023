package com.example.bobfairy

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bobfairy.databinding.ActivityDateBaseBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DateBase : AppCompatActivity() {
    lateinit var binding:ActivityDateBaseBinding
    lateinit var layoutManager: LinearLayoutManager
    lateinit var recipeAdapter:FoodAdapter
    lateinit var restAdapter: restaurantFBAdapter
    lateinit var rdb: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDateBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        btnClick()
    }

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode== Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK, Intent())
            finish()
        }
    }
    private fun btnClick() {

        binding.prev.setOnClickListener {
            finish()
        }

        binding.btnRecipe.setOnClickListener {
            rdb= Firebase.database.getReference("Food/Recipe")//참조 객체
            val query = rdb.orderByChild("BOOKMARK").equalTo("true")

            val option = FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(query) {
                    val item = it.value as HashMap<String, String>
                    makeFood(item)
                }
                .build()

            recipeAdapter = FoodAdapter(option, "")
            recipeAdapter.startListening()
            //option 객체로 adapter 객체 생성
            recipeAdapter.bookmarkClickListener = object: FoodAdapter.OnItemClickListener {
                override fun onItemClick(position: Int, name: String) {
                    var bookmark: Boolean
                    rdb.child(name).get().addOnSuccessListener {
                        Log.i("what", it.value!!.javaClass.toString())
                        val item = it.value!! as HashMap<String, String>
                        bookmark = item["BOOKMARK"] as String == "true"
                        rdb.child(name).child("BOOKMARK").setValue(if(bookmark) "false" else "true")
                        recipeAdapter.getItem(position).bookmark = !recipeAdapter.getItem(position).bookmark
                        recipeAdapter.notifyItemChanged(position)
                    }.addOnFailureListener {
                        Log.e("err", it.toString())
                    }
                }
            }
            recipeAdapter.itemClickListener=object:FoodAdapter.OnItemClickListener{
                override fun onItemClick(position: Int, name: String) {
                    val intent = Intent(this@DateBase, RecipeList::class.java)
                    intent.putExtra("pos", position)
                    intent.putExtra("food", recipeAdapter.getItem(position))
                    startActivity(intent)
                    Log.i("start", "start")
                }
            }
            binding.recyclerView.adapter = recipeAdapter
            binding.recyclerView.layoutManager=layoutManager
        }
        binding.btnRestaurant.setOnClickListener {
            rdb= Firebase.database.getReference("Food/Restaurant")//참조 객체
            val query = rdb.orderByChild("_like").equalTo(true)

            val option = FirebaseRecyclerOptions.Builder<PlaceData>()
                .setQuery(query, PlaceData::class.java)
                .build()
            //query객체로부터 옵션 객체 생성

            restAdapter = restaurantFBAdapter(option)
            restAdapter.startListening()

            restAdapter.itemClickListener=object:restaurantFBAdapter.OnItemClickListener {
                override fun OnItemClick(data: PlaceData, position: Int) {
                    val i = Intent(this@DateBase, RestaurantDetailActivity::class.java)
                    i.putExtra("placeId", data.place_id)
                    launcher.launch(i)
                }

                override fun OnLikeClick(data: PlaceData, position: Int) {
                    rdb.child(data.place_id).removeValue()
                    restAdapter.notifyItemChanged(position)
                }
            }
            binding.recyclerView.adapter = restAdapter
        }
    }

    fun init() {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rdb= Firebase.database.getReference("Food/Recipe")//참조 객체
        val query = rdb.orderByChild("BOOKMARK").equalTo("true")

        val option = FirebaseRecyclerOptions.Builder<Food>()
            .setQuery(query) {
                val item = it.value as HashMap<String, String>
                makeFood(item)
            }
            .build()

        recipeAdapter = FoodAdapter(option, "")
        recipeAdapter.startListening()
        //option 객체로 adapter 객체 생성
        recipeAdapter.bookmarkClickListener = object: FoodAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, name: String) {
                var bookmark: Boolean
                rdb.child(name).get().addOnSuccessListener {
                    Log.i("what", it.value!!.javaClass.toString())
                    val item = it.value!! as HashMap<String, String>
                    bookmark = item["BOOKMARK"] as String == "true"
                    rdb.child(name).child("BOOKMARK").setValue(if(bookmark) "false" else "true")
                    recipeAdapter.getItem(position).bookmark = !recipeAdapter.getItem(position).bookmark
                    recipeAdapter.notifyItemChanged(position)
                }.addOnFailureListener {
                    Log.e("err", it.toString())
                }
            }
        }
        recipeAdapter.itemClickListener=object:FoodAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, name: String) {
                val intent = Intent(this@DateBase, RecipeList::class.java)
                intent.putExtra("pos", position)
                intent.putExtra("food", recipeAdapter.getItem(position))
                startActivity(intent)
                Log.i("start", "start")
            }
        }

        binding.recyclerView.adapter = recipeAdapter

        binding.recyclerView.layoutManager=layoutManager
    }

    fun makeFood(item: HashMap<String, String>): Food{
        val name = item["RCP_NM"]!! // 음식 이름
        val type = item["RCP_PAT2"]!! // 음식 종류
        val cal = item["INFO_ENG"]!! // 칼로리
        val ingredient = item["RCP_PARTS_DTLS"]!! // 재료
        val bookmark = item["BOOKMARK"]!! == "true" // 북마크
        val image = item["ATT_FILE_NO_MAIN"]!! // 이미지 링크(string)
        val steps = ArrayList<Step>() // 단계
        for (j in 1..20) {
            val step = (if (j in 1..9) "0" else "") + "$j"
            Log.i("step", name + " " + step)
            if (item.containsKey("MANUAL$step") && item["MANUAL$step"]!!.isNotEmpty()) {
                val description = item["MANUAL$step"]!!
                val image = item["MANUAL_IMG$step"]!!
                steps.add(Step("${j}단계", description, image))
            }
        }
        return Food(name, type, cal, image, ingredient, bookmark, steps)
    }
}