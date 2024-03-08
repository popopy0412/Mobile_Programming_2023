package com.example.bobfairy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bobfairy.databinding.ActivityRecipeListBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FoodList : AppCompatActivity() {
    lateinit var rdb: DatabaseReference
    lateinit var adapter: FoodAdapter
    lateinit var option: FirebaseRecyclerOptions<Food>
    lateinit var binding: ActivityRecipeListBinding
    companion object {
        private var category: String? = null
        fun getCategory(): String = category!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDB()
        initLayout()
    }

    private fun initLayout() {
        binding.apply {
            adapter = FoodAdapter(option, "")
            recyclerView.layoutManager =
                LinearLayoutManager(this@FoodList, LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = adapter

            initAdapter()

            searchBtn.setOnClickListener {
                if(adapter != null) adapter.stopListening()
                val search = searchText.text.toString()
                val query = rdb.orderByChild("RCP_PAT2").equalTo(category)
                var filteredList: List<DataSnapshot>

                option = FirebaseRecyclerOptions.Builder<Food>()
                    .setQuery(query) {
                        val item = it.value as HashMap<String, String>
                        if(search.isEmpty() || (search.isNotEmpty() && item["RCP_NM"]!!.contains(search))) makeFood(item) else Food()
                    }
                    .build()
                adapter = FoodAdapter(option, search)
                initAdapter()
                recyclerView.adapter = adapter
                adapter.startListening()
            }
            prev.setOnClickListener {
                finish()
            }
        }
    }

    private fun initAdapter() {
        adapter.itemClickListener = object : FoodAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, name: String) {
                val intent = Intent(this@FoodList, RecipeList::class.java)
                rdb.child(name).get().addOnSuccessListener {
                    val item = it.value!! as HashMap<String, String>
                    intent.putExtra("food", makeFood(item))
                    startActivity(intent)
                }.addOnFailureListener {
                }
            }
        }
        adapter.bookmarkClickListener = object: FoodAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, name: String) {
                var bookmark: Boolean
                rdb.child(name).get().addOnSuccessListener {
                    val item = it.value!! as HashMap<String, String>
                    bookmark = item["BOOKMARK"] as String == "true"
                    rdb.child(name).child("BOOKMARK").setValue(if(bookmark) "false" else "true")
                    adapter.getItem(position).bookmark = !adapter.getItem(position).bookmark
                    adapter.notifyItemChanged(position)
                }.addOnFailureListener {
                }
            }
        }
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
            if (item.containsKey("MANUAL$step") && item["MANUAL$step"]!!.isNotEmpty()) {
                val description = item["MANUAL$step"]!!
                val image = item["MANUAL_IMG$step"]!!
                steps.add(Step("${j}단계", description, image))
            }
        }
        return Food(name, type, cal, image, ingredient, bookmark, steps)
    }
    fun getDB() {
        category = intent.getStringExtra("category")
        rdb = Firebase.database.getReference("Food/Recipe") //참조 객체
        val query = rdb.orderByChild("RCP_PAT2").equalTo(category)
        option = FirebaseRecyclerOptions.Builder<Food>()
            .setQuery(query) {
                val item = it.value as HashMap<String, String>
                makeFood(item)
            }
            .build()
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        //adapter.stopListening()
    }
}