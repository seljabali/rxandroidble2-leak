package com.seljabali.rxandroidble2leak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RxBle.init(this)
        supportFragmentManager.beginTransaction()
            .add(R.id.frameLayout, ScanBleFragment.newInstance())
            .addToBackStack(ScanBleFragment.TAG)
            .commit()
    }
}
