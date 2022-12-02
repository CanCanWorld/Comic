package com.zrq.comic

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.tencent.mmkv.MMKV
import com.zrq.comic.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        requestPermissions()
        mainModel = ViewModelProvider(this)[MainModel::class.java]
        MMKV.initialize(this)
        initData()
        initEvent()
    }

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mainModel: MainModel

    @RequiresApi(Build.VERSION_CODES.P)
    private fun initData() {
        mainModel.setScreen = {
            Log.d(TAG, "setScreen: ")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            val lp: WindowManager.LayoutParams = window.attributes
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = lp
        }
        mainModel.setScreen()
    }

    private fun initEvent() {
    }

    //权限
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, 1)
    }

    companion object {
        val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )

        const val TAG = "MainActivity"
    }

}