package com.example.jokbo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jokbo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: MyAdapter
    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            val name = it.data?.getStringExtra("name")!!
            val company = it.data?.getStringExtra("company")!!
            val tel = it.data?.getStringExtra("tel")!!
            val pos: Int = it.data?.extras!!.getInt("pos")!!
            data[pos].name = name
            data[pos].company = company
            data[pos].tel = tel
            adapter.notifyItemChanged(pos)
        }
    }
    val data: ArrayList<Person> = ArrayList()
    lateinit var num: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initLayout()
    }

    fun initLayout() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = MyAdapter(data)
        adapter.itemClickListener = object: MyAdapter.onItemClickListener{
            override fun onItemClick(holder: MyAdapter.ViewHolder, position: Int) {
                val intent = Intent(this@MainActivity, ModifyInfo::class.java)
                intent.putExtra("person", data[position])
                intent.putExtra("position", position)
                println(position)
                launcher.launch(intent)
            }
        }
        adapter.imageClickListener = object: MyAdapter.onItemClickListener{
            override fun onItemClick(holder: MyAdapter.ViewHolder, position: Int) {
                data[position].cnt++
                num = data[position].tel
                callAction()
                adapter.notifyItemChanged(position)
            }
        }
        binding.recyclerView.adapter = adapter
    }

    private fun permissionGranted(): Boolean = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED

    private fun callAction() {
        val number = Uri.parse("tel:$num")
        val intent = Intent(Intent.ACTION_CALL, number)
        when {
            permissionGranted() -> {
                startActivity(intent)
            }
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE) -> {
                callAlertDialog()
            }
            else -> {
                permissionLauncher.launch(Manifest.permission.CALL_PHONE)
            }
        }
    }

    val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if(it){
            callAction()
        }else{
        }
    }

    fun callAlertDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("반드시 CALL_PHONE 권한이 허용되어야 합니다.").setTitle("권한 체크").setPositiveButton("OK"){
                _, _ ->
            permissionLauncher.launch(android.Manifest.permission.CALL_PHONE)
        }.setNegativeButton("Cancel"){
                dlg, _ -> dlg.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun init(){
        binding.apply{
            registerBtn.setOnClickListener {
                data.add(Person(name.text.toString(), company.text.toString(), telnumber.text.toString(), 0))
                adapter.notifyDataSetChanged()
            }
        }
    }
}