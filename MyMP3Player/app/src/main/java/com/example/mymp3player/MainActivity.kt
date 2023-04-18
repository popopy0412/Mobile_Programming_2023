package com.example.mymp3player

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mymp3player.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var mediaPlayer: MediaPlayer? = null
    var vol = 0.5f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    fun initLayout(){
        binding.apply {
            imageView.setVolumeListener(object: VolumeControlView.VolumeListener{
                override fun onChanged(angle: Float) {
                    vol = if(angle > 0) {
                        angle / 360
                    } else{
                        (360+angle)/360
                    }
                    mediaPlayer?.setVolume(vol, vol)
                }
            })
            playBtn.setOnClickListener{
                if(mediaPlayer == null){
                    mediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.music)
                    mediaPlayer?.setVolume(vol, vol)
                }
                mediaPlayer?.start()
            }
            pauseBtn.setOnClickListener {
                mediaPlayer?.pause()
            }
            stopBtn.setOnClickListener {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
            }
        }
    }
}