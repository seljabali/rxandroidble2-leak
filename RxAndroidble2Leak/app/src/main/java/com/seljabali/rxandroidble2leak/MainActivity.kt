package com.seljabali.rxandroidble2leak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RxBle.init(this)
//        launchScanCallBackFragment()
        launchScanRxFragment()
    }

    private fun launchScanRxFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.frameLayout, ScanRxBleFragment.newInstance())
            .addToBackStack(ScanRxBleFragment.TAG)
            .commit()
    }

    private fun launchScanCallBackFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.frameLayout, ScanCallBackBleFragment.newInstance())
            .addToBackStack(ScanCallBackBleFragment.TAG)
            .commit()
    }
}
