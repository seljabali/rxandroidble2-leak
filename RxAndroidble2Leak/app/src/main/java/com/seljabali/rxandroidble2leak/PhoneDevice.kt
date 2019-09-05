package com.seljabali.rxandroidble2leak

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager

object PhoneDevice {

    /**
     *  Bluetooth
     */
    @JvmStatic
    fun hasBlueToothLowEnergy(context: Context): Boolean = context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)

    @JvmStatic
    fun hasBlueToothService(context: Context): Boolean = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager?)?.adapter != null

    @SuppressLint("MissingPermission")
    @JvmStatic
    fun hasBlueToothEnabled(): Boolean = BluetoothAdapter.getDefaultAdapter()?.isEnabled == true

    @JvmStatic
    fun hasGPS(context: Context) = context.packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)

    @JvmStatic
    fun hasGPSEnabled(context: Context): Boolean = (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)

}