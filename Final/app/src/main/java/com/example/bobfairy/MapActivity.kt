package com.example.bobfairy

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.bobfairy.databinding.ActivityMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener

class MapActivity : AppCompatActivity() {
    lateinit var binding: ActivityMapBinding
    lateinit var googleMap: GoogleMap

    var loc = LatLng(37.554752, 126.970631)
    var rangeIdx = 1
    var rangeList = arrayOf(100, 300, 500, 1000)
    var zoomList = arrayOf(17.5f, 16f, 15f, 14f)
    lateinit var circle: Circle

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    var permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initMap()
        initUi()
    }

    override fun onStop() {
        super.onStop()
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
            initLocation()
        }
    }

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode== Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK, Intent())
            finish()
        }
    }

    fun initUi() {
        binding.apply {
            viewBtn.setOnClickListener {
                val keyword = intent.getStringExtra("category") ?: "중식"
                val i = Intent(this@MapActivity, RestaurantListActivity::class.java)
                Log.i("test", "rad :: " + rangeList[rangeIdx].toString())
                i.putExtra("query", query(rangeList[rangeIdx], loc.latitude, loc.longitude, keyword))
                launcher.launch(i)
            }

            rangeBtn.setOnClickListener {
                changeRange()
            }

            locationBtn.setOnClickListener {
                resetPosition()
            }

            prev.setOnClickListener {
                finish()
            }
        }
    }

    fun changeRange() {
        if (rangeIdx < rangeList.size - 1)
            rangeIdx++
        else
            rangeIdx = 0

        binding.rangeBtn.text = rangeList[rangeIdx].toString() + "m"
        circle.remove()

        val circleOption = CircleOptions()
            .center(loc)
            .strokeWidth(6f)
            .strokeColor(Color.rgb(255, 255, 0))
            .fillColor(Color.argb(100, 255, 255, 153))
            .radius(rangeList[rangeIdx].toDouble())
        circle = googleMap.addCircle(circleOption)

//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, zoomList[rangeIdx]))
    }

    fun resetPosition() {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, zoomList[rangeIdx]))
    }

    fun initLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
    }

    fun setCurrentLocation(location: LatLng){
        val option = MarkerOptions()
        option.position(loc)
        option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        googleMap.addMarker(option)

        val circleOption = CircleOptions()
            .center(loc)
            .strokeWidth(6f)
            .strokeColor(Color.rgb(255, 255, 0))
            .fillColor(Color.argb(100, 255, 255, 153))
            .radius(rangeList[rangeIdx].toDouble())

        circle = googleMap.addCircle(circleOption)

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, zoomList[rangeIdx]))
    }

    private fun getLastLocation() {
        when {
            checkFineLocationPermission()->{
//                if(!checkGPSProvider()) {
//                    showGPSSetting()
//                } else {
                    fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,
                        object : CancellationToken() {
                            override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                                return CancellationTokenSource().token
                            }
                            override fun isCancellationRequested(): Boolean {
                                return false
                            }
                        }).addOnSuccessListener {
                        if (it != null) {
                            loc = LatLng(it.latitude, it.longitude)
                        }
                        setCurrentLocation(loc)
                    }

//                }
            }
            checkCoarseLocationPermission()->{
                fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                    object : CancellationToken() {
                        override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                            return CancellationTokenSource().token
                        }
                        override fun isCancellationRequested(): Boolean {
                            return false
                        }
                    }).addOnSuccessListener {
                    if (it != null) {
                        loc = LatLng(it.latitude, it.longitude)
                    }
                    setCurrentLocation(loc)
                }
            }

            ActivityCompat.shouldShowRequestPermissionRationale(this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION)->{
                showPermissionRequestDlg()
            }

            else->{
                locationPermissionRequest.launch(permissions)
            }
        }
    }

    private fun checkFineLocationPermission():Boolean{
        return ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkCoarseLocationPermission():Boolean{
        return ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    val gpsSettingsLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if(checkGPSProvider()) {
                getLastLocation()
            } else {
                setCurrentLocation(loc)
            }
        }

    val locationPermissionRequest =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                        permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    getLastLocation()
                }
                else -> {
                    setCurrentLocation(loc)
                }
            }
        }

    private fun checkGPSProvider(): Boolean {
        val locationManager = getSystemService(LOCALE_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun showGPSSetting() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n 위치 설정을 허용하겠습니까?")
        builder.setPositiveButton("설정", DialogInterface.OnClickListener { dialog, id ->
            val GpsSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            gpsSettingsLauncher.launch(GpsSettingIntent)
        })
        builder.setNegativeButton("취소", DialogInterface.OnClickListener { dialog, id ->
            dialog.dismiss()
            setCurrentLocation(loc)
        })
        builder.create().show()
    }

    private fun showPermissionRequestDlg(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("위치 서비스 제공")
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n 위치 설정을 허용하겠습니까?")
        builder.setPositiveButton("설정", DialogInterface.OnClickListener { dialog, id ->
            locationPermissionRequest.launch(permissions)
        })
        builder.setNegativeButton("취소", DialogInterface.OnClickListener { dialog, id ->
            dialog.dismiss()
            setCurrentLocation(loc)
        })
        builder.create().show()
    }
}