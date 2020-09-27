package com.pananfly.servicevideoplayer.service

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.IBinder
import android.view.Surface
import com.pananfly.servicevideoplayer.IPlayerInterface

class PlayerService : Service(){

    private var mMediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder? {
        return MyBinder()
    }

    inner class MyBinder: IPlayerInterface.Stub() {
        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String?
        ) {
            TODO("Not yet implemented")
        }

        override fun play(url: String?): Boolean {
            mMediaPlayer?.let {
                if(it.isPlaying) {
                    it.stop()
                }
            }
            mMediaPlayer?.reset()
            mMediaPlayer?.isLooping = true
            mMediaPlayer?.setAudioAttributes(AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build())
            mMediaPlayer?.setOnPreparedListener { it.start() }
            mMediaPlayer?.setDataSource(url)
            mMediaPlayer?.prepareAsync()
            return true
        }

        override fun setSurface(surface: Surface?): Boolean {
            mMediaPlayer?.setSurface(surface)
            return true
        }

        override fun stop(): Boolean {
            mMediaPlayer?.stop()
            mMediaPlayer = null
            return true
        }
    }

    override fun onCreate() {
        super.onCreate()
        mMediaPlayer = MediaPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMediaPlayer?.let {
            if(it.isPlaying) {
                it.stop()
            }
        }
        mMediaPlayer = null
    }
}