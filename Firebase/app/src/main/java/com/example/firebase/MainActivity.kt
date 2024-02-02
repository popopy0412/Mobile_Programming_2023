package com.example.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebase.databinding.ActivityMainBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: MyProductAdapter
    lateinit var rdb: DatabaseReference
    var findQuery = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rdb = Firebase.database.getReference("Products/items")
        val query = rdb.limitToLast(50)
        val option = FirebaseRecyclerOptions.Builder<Product>()
            .setQuery(query, Product::class.java)
            .build()
        adapter = MyProductAdapter(option)
        adapter.itemClickListener = object: MyProductAdapter.OnItemClickListener {
            override fun OnItemClick(position: Int) {
                binding.apply {
                    pIdEdit.setText(adapter.getItem(position).pid.toString())
                    pNameEdit.setText(adapter.getItem(position).pName)
                    pQuantityEdit.setText(adapter.getItem(position).pQuantity.toString())
                }
            }
        }
        binding.apply {
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
            insertBtn.setOnClickListener {
                initQuery()
                val item = Product(pIdEdit.text.toString().toInt(), pNameEdit.text.toString(), pQuantityEdit.text.toString().toInt())
                rdb.child(pIdEdit.text.toString()).setValue(item)
                clearInput()
            }

            findBtn.setOnClickListener {
                if(!findQuery) findQuery = true
                if(adapter != null) adapter.stopListening()
                val query = rdb.orderByChild("pname").equalTo(pNameEdit.text.toString())
                val option = FirebaseRecyclerOptions.Builder<Product>()
                    .setQuery(query, Product::class.java)
                    .build()
                adapter = MyProductAdapter(option)
                adapter.itemClickListener = object: MyProductAdapter.OnItemClickListener {
                    override fun OnItemClick(position: Int) {
                        binding.apply {
                            pIdEdit.setText(adapter.getItem(position).pid.toString())
                            pNameEdit.setText(adapter.getItem(position).pName)
                            pQuantityEdit.setText(adapter.getItem(position).pQuantity.toString())
                        }
                    }
                }
                recyclerView.adapter = adapter
                adapter.startListening()
            }

            deleteBtn.setOnClickListener {
                initQuery()
                rdb.child(pIdEdit.text.toString()).removeValue()
                clearInput()
            }

            updateBtn.setOnClickListener {
                initQuery()
                rdb.child(pIdEdit.text.toString())
                    .child("pquantity")
                    .setValue(pQuantityEdit.text.toString().toInt())
                clearInput()
            }
        }
    }

    private fun initQuery() {
        if(findQuery) {
            findQuery = false
            if (adapter != null) adapter.stopListening()
            val query = rdb.limitToLast(50)
            val option = FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(query, Product::class.java)
                .build()
            adapter = MyProductAdapter(option)
            adapter.itemClickListener = object : MyProductAdapter.OnItemClickListener {
                override fun OnItemClick(position: Int) {
                    binding.apply {
                        pIdEdit.setText(adapter.getItem(position).pid.toString())
                        pNameEdit.setText(adapter.getItem(position).pName)
                        pQuantityEdit.setText(adapter.getItem(position).pQuantity.toString())
                    }
                }
            }
            binding.recyclerView.adapter = adapter
            adapter.startListening()
        }
    }

    private fun clearInput(){
        binding.apply {
            pIdEdit.text.clear()
            pNameEdit.text.clear()
            pQuantityEdit.text.clear()
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}