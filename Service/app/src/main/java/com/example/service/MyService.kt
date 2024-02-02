package com.example.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MyService : Service() {
    lateinit var song:String
    var player: MediaPlayer?=null

    var manager: NotificationManager? = null
    val notificationId = 100

    fun makeNoticication(){
        val channel = NotificationChannel("channel1", "mp3channel", NotificationManager.IMPORTANCE_DEFAULT)
        manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager?.createNotificationChannel(channel)
        val builder = NotificationCompat.Builder(this, "channel1")
            .setSmallIcon(R.drawable.baseline_audiotrack_24)
            .setContentTitle("MP3")
            .setContentText("MP3 플레이중...")

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        val pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        builder.setContentIntent(pendingIntent)

        val notification = builder.build()
        startForeground(notificationId, notification)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    var receiver = object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            playControl(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        registerReceiver(receiver, IntentFilter("com.example.MP3SERVICE"))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(player != null && player!!.isPlaying){
            val mainBRIntent = Intent("com.example.MP3ACTIVITY")
            mainBRIntent.putExtra("mode", "playing")
            mainBRIntent.putExtra("song", song)
            mainBRIntent.putExtra("currentPos", player!!.currentPosition)
            mainBRIntent.putExtra("duration", player!!.duration)
            sendBroadcast(mainBRIntent)
        }
        return START_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun playControl(intent: Intent?) {
        val mode = intent!!.getStringExtra("mode")
        if(mode != null){
            when(mode){
                "play" -> {
                    song = intent.getStringExtra("song")!!
                    startPlay()
                    makeNoticication()
                }
                "stop" -> {
                    stopPlay()
                    stopForeground(Service.STOP_FOREGROUND_REMOVE)
                }
            }
        }
    }

    private fun startPlay(){
        val songid = resources.getIdentifier(song, "raw", packageName)
        if (player != null && player!!.isPlaying) {
            player!!.stop()
            player!!.reset()
            player!!.release()
            player = null
        }

        player = MediaPlayer.create(this, songid)
        player!!.start()

        val mainBRIntent = Intent("com.example.MP3ACTIVITY")
        mainBRIntent.putExtra("mode", "play")
        mainBRIntent.putExtra("duration", player!!.duration)
        sendBroadcast(mainBRIntent)

        player!!.setOnCompletionListener {
            val mainBRIntent = Intent("com.example.MP3ACTIVITY")
            mainBRIntent.putExtra("mode", "stop")
            sendBroadcast(mainBRIntent)
            stopPlay()
        }
    }

    private fun stopPlay(){
        if(player!=null && player!!.isPlaying){
            player!!.stop()
            player!!.reset()
            player!!.release()
            player=null
        }
    }
}