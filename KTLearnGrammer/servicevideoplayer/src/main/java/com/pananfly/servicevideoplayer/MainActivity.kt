package com.pananfly.servicevideoplayer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import com.pananfly.servicevideoplayer.databinding.ActivityMainBinding
import com.pananfly.servicevideoplayer.service.PlayerService
import java.lang.Exception
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding
    private var mPlayerItf: IPlayerInterface? = null
    private var mSurfaceHolder: SurfaceHolder? = null
    private val mPaths = arrayOf<String>("https://zhstatic.zhihu.com/cfe/griffith/zhihu2018_hd.mp4", "rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov", "http://v.cctv.com/flash/mp4video6/TMS/2011/01/05/cf752b1c12ce452b3040cab2f90bc265_h264818000nero_aac32-1.mp4")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.svVideo.holder.addCallback(callback)
        binding.btnChange.setOnClickListener { startPlay(mPaths[getPlayIndex()]) }
        bindPlayerService()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.svVideo.holder.removeCallback(callback)
        unBindPlayerService()
    }

    private fun bindPlayerService() {
        try {
            bindService(Intent(this, PlayerService::class.java), connection, Context.BIND_AUTO_CREATE)
            Log.i(TAG, "Bind player service.")
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun unBindPlayerService() {
        try {
            unbindService(connection)
            Log.i(TAG, "Unbind player service.")
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getPlayIndex(): Int {
        return Random.nextInt(0, 3)
    }

    private fun startPlay(path: String) {
        mPlayerItf?.setSurface(mSurfaceHolder?.surface)
        mPlayerItf?.play(path)
        Log.i(TAG, "Start play path: $path .")
    }


    private fun stopPlay() {
        mPlayerItf?.stop()
        Log.i(TAG, "Stop play .")
    }

    private val callback = object : SurfaceHolder.Callback {

        override fun surfaceCreated(holder: SurfaceHolder) {
            Log.i(TAG, "Surface create.")
            mSurfaceHolder = holder
            mPlayerItf?.setSurface(holder.surface)
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            Log.i(TAG, "Surface destroy.")
            mSurfaceHolder = null
            mPlayerItf?.setSurface(null)
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mPlayerItf = IPlayerInterface.Stub.asInterface(service)
            Log.i(TAG, "ServiceConnected.")
            startPlay(mPaths[getPlayIndex()])
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            stopPlay()
            mPlayerItf = null
        }
    }
}
