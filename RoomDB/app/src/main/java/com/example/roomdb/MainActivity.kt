package com.example.roomdb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.insert
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdb.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var db: ProductDatabase
    var recordset = ArrayList<Product>()
    var adapter = MyAdapter(ArrayList())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        initLayout()
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter.itemClickListener = object: MyAdapter.OnItemClickListener{
            override fun onClick(position: Int) {
                binding.pIdEdit.setText(adapter.items[position].pId.toString())
                binding.pNameEdit.setText(adapter.items[position].pName)
                binding.pQuantityEdit.setText(adapter.items[position].pQuantity.toString())
            }
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun initLayout() {
        db = ProductDatabase.getDatabase(this)

        CoroutineScope(Dispatchers.IO).launch {
            getAllRecord()
        }

        binding.apply {
            insertBtn.setOnClickListener {
                val name = pNameEdit.text.toString()
                val quantity = pQuantityEdit.text.toString().toInt()
                val product = Product(0, name, quantity)
                CoroutineScope(Dispatchers.IO).launch {
                    insert(product)
                }
            }
            deleteBtn.setOnClickListener {
                val pid = pIdEdit.text.toString().toInt()
                val name = pNameEdit.text.toString()
                val quantity = pQuantityEdit.text.toString().toInt()
                val product = Product(pid, name, quantity)
                CoroutineScope(Dispatchers.IO).launch {
                    delete(product)
                }
            }
            updateBtn.setOnClickListener {
                val pid = pIdEdit.text.toString().toInt()
                val name = pNameEdit.text.toString()
                val quantity = pQuantityEdit.text.toString().toInt()
                val product = Product(pid, name, quantity)
                CoroutineScope(Dispatchers.IO).launch {
                    update(product)
                }
            }
            findBtn.setOnClickListener {
                val name = pNameEdit.text.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    findProduct(name)
                }
            }
            findBtn2.setOnClickListener {
                var name = pNameEdit.text.toString()
                name += "%"
                CoroutineScope(Dispatchers.IO).launch {
                    findProduct2(name)
                }
            }
        }
    }

    private fun insert(product: Product){
        db.productDao().insertProduct(product)
        getAllRecord()
    }

    private fun delete(product: Product){
        db.productDao().deleteProduct(product)
        getAllRecord()
    }

    private fun update(product: Product){
        db.productDao().updateProduct(product)
        getAllRecord()
    }

    private fun findProduct(pname: String){
        recordset = db.productDao().findProduct(pname) as ArrayList<Product>
        adapter.items = recordset
        CoroutineScope(Dispatchers.Main).launch{
            adapter.notifyDataSetChanged()
        }
    }

    private fun findProduct2(pname: String){
        recordset = db.productDao().findProduct2(pname) as ArrayList<Product>
        adapter.items = recordset
        CoroutineScope(Dispatchers.Main).launch{
            adapter.notifyDataSetChanged()
        }
    }

    private fun getAllRecord(){
        recordset = db.productDao().getAllRecord() as ArrayList<Product>
        adapter.items = recordset
        CoroutineScope(Dispatchers.Main).launch{
            adapter.notifyDataSetChanged()
        }
    }
}