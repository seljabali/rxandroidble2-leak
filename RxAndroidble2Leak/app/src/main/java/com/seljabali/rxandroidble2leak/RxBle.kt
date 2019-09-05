package com.seljabali.rxandroidble2leak

import android.content.Context
import android.util.Log
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.exceptions.BleException
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import com.polidea.rxandroidble2.LogConstants
import com.polidea.rxandroidble2.LogOptions

object RxBle {
    private lateinit var rxBleClient: RxBleClient

    fun init(context: Context) {
        rxBleClient = RxBleClient.create(context)
        RxBleClient.updateLogOptions(
            LogOptions.Builder()
                .setLogLevel(LogConstants.VERBOSE)
                .setMacAddressLogSetting(LogConstants.MAC_ADDRESS_FULL)
                .setUuidsLogSetting(LogConstants.UUIDS_FULL)
                .setShouldLogAttributeValues(true)
                .build()
        )
        RxJavaPlugins.setErrorHandler { throwable ->
            if (throwable is UndeliverableException && throwable.cause is BleException) {
                Log.e("RxBle","Suppressed UndeliverableException: $throwable")
            }
            throw RuntimeException("Unexpected Throwable in RxJavaPlugins error handler", throwable)
        }
    }

    fun get(): RxBleClient = rxBleClient
}