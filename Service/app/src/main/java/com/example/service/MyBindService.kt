package com.example.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class MyBindService : Service() {
    lateinit var song:String
    var player: MediaPlayer?=null
    val binder = MyBinder()

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    inner class MyBinder: Binder(){
        fun getService(): MyBindService = this@MyBindService
    }

    fun getMaxDuration(): Int{
        if(player != null) return player!!.duration
        else return 0
    }

    fun startPlay(sname: String){
        song = sname
        val songid = resources.getIdentifier(song, "raw", packageName)
        if (player != null && player!!.isPlaying) {
            player!!.stop()
            player!!.reset()
            player!!.release()
            player = null
        }

        player = MediaPlayer.create(this, songid)
        player!!.start()

        player!!.setOnCompletionListener {
            stopPlay()
        }
    }

    fun stopPlay(){
        if(player!=null && player!!.isPlaying){
            player!!.stop()
            player!!.reset()
            player!!.release()
            player=null
        }
    }
}