package com.example.lec11

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.lec11.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
        checkPermissions()
    }

    val permissions = arrayOf(android.Manifest.permission.CALL_PHONE, android.Manifest.permission.CAMERA)

    val multiplePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            val resultPermission = it.all{map ->
                map.value
            }
            if(!resultPermission){
                //finish()
                Toast.makeText(this, "모든 권한 승인되어야 함", Toast.LENGTH_SHORT).show()
            }
        }

    fun checkPermissions(){
        when{
            (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) &&
                    (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) -> {
                        Toast.makeText(this, "모든 권한 승인됨", Toast.LENGTH_SHORT).show()

            }
            ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.CALL_PHONE) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.CAMERA)-> {
                permissionCheckAlertDialog()
            }
            else -> {
                multiplePermissionLauncher.launch(permissions)
            }
        }
    }

    fun permissionCheckAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("반드시 CALL_PHONE과 CAMERA 권한이 모두 허용되어야 합니다.").setTitle("권한 체크").setPositiveButton("OK"){
                _, _ ->
            multiplePermissionLauncher.launch(permissions)
        }.setNegativeButton("Cancel"){
                dlg, _ -> dlg.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun allPermissionGranted() = permissions.all{
        ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    fun callPhonePermissionGranted() = ActivityCompat.checkSelfPermission(this,
        android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED

    fun cameraPermissionGranted() = ActivityCompat.checkSelfPermission(this,
        android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if(it){
            callAction()
        } else{
            Toast.makeText(this, "권한 승인이 거부되었습니다.", Toast.LENGTH_SHORT).show()
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

    fun callAction(){
        val number = Uri.parse("tel:010-1234-1234")
        val callIntent = Intent(Intent.ACTION_CALL, number)
        if(allPermissionGranted())
            startActivity(callIntent)
        else
            checkPermissions()
    }

//    fun callAction(){
//        val number = Uri.parse("tel:010-1234-1234")
//        val callIntent = Intent(Intent.ACTION_CALL, number)
//        when{
//            ActivityCompat.checkSelfPermission(this,
//                android.Manifest.permission.CALL_PHONE)
//                    == PackageManager.PERMISSION_GRANTED -> {
//                startActivity(callIntent)
//                    }
//            ActivityCompat.shouldShowRequestPermissionRationale(this,
//                android.Manifest.permission.CALL_PHONE) -> {
//                callAlertDialog()
//                }
//            else -> {
//                permissionLauncher.launch(android.Manifest.permission.CALL_PHONE)
//            }
//        }
//    }

    fun initLayout() {
        binding.apply {
            callBtn.setOnClickListener {
                callAction()
            }
            msgBtn.setOnClickListener {
                val message = Uri.parse("sms:010-1234-1234")
                val messageIntent = Intent(Intent.ACTION_SENDTO, message)
                messageIntent.putExtra("sms_body", "Hello World!")
                startActivity(messageIntent)
            }
            webBtn.setOnClickListener {
                val webpage = Uri.parse("https://www.naver.com")
                val webIntent = Intent(Intent.ACTION_VIEW, webpage)
                startActivity(webIntent)
            }
            mapBtn.setOnClickListener {
                val location = Uri.parse("geo:37.543684, 127.077103?z=16")
                val mapIntent = Intent(Intent.ACTION_VIEW, location)
                startActivity(mapIntent)
            }
            cameraBtn.setOnClickListener {
                cameraAction()
            }
        }
    }

    private fun cameraAction() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(cameraPermissionGranted())
            startActivity(intent)
        else
            checkPermissions()
    }
}