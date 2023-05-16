package com.example.notification

import android.app.*
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.notification.databinding.ActivityMainBinding
import com.example.notification.databinding.MytimepickerdlgBinding
import java.util.*

class MainActivity : AppCompatActivity(), OnTimeSetListener {
    lateinit var binding: ActivityMainBinding
    var msg: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //initLayout()
        initLayout2()
    }

    private fun initLayout2() {
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            msg = "${year}년 ${month+1}월 ${dayOfMonth}일 "
            val cal = Calendar.getInstance()
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minute = cal.get(Calendar.MINUTE)

            val dlgBinding = MytimepickerdlgBinding.inflate(layoutInflater)
            dlgBinding.timePicker.hour = hour
            dlgBinding.timePicker.minute = minute
            val dlgBuilder = AlertDialog.Builder(this)
            dlgBuilder.setView(dlgBinding.root)
                .setPositiveButton("추가"){_, _ ->
                    msg += "${dlgBinding.timePicker.hour}시 ${dlgBinding.timePicker.minute}분 => "
                    msg += dlgBinding.message.text.toString()

                    val timerTask = object: TimerTask(){
                        override fun run() {
                            makeNotification()
                        }
                    }

                    val timer = Timer()
                    timer.schedule(timerTask, 2000)
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("취소"){_, _ ->

                }
                .show()
        }
    }

    private fun initLayout() {
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            msg = "${year}년 ${month+1}월 ${dayOfMonth}일 "
            val cal = Calendar.getInstance()
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minute = cal.get(Calendar.MINUTE)

            val timePicker = TimePickerDialog(this, this, hour, minute, true)
            timePicker.show()
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        if(view != null){
            msg += "${hourOfDay}시 ${minute}분"

            val timerTask = object: TimerTask(){
                override fun run() {
                    makeNotification()
                }
            }

            val timer = Timer()
            timer.schedule(timerTask, 2000)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun makeNotification() {
        val id = "MyChannel"
        val name = "TimeCheckChannel"
        val notificationChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.enableVibration(true)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        val builder = NotificationCompat.Builder(this, id)
            .setSmallIcon(R.drawable.baseline_access_alarm_24)
            .setContentTitle("일정 알람")
            .setContentText(msg)
            .setAutoCancel(true)

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("time", msg)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        val pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        builder.setContentIntent(pendingIntent)

        val notification = builder.build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(notificationChannel)
        manager.notify(10, notification)
    }
}