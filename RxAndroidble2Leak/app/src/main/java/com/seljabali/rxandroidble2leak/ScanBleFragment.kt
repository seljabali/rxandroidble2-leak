package com.seljabali.rxandroidble2leak

import android.Manifest
import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_scan.*
import android.bluetooth.BluetoothAdapter
import android.content.DialogInterface
import android.content.Intent
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.polidea.rxandroidble2.scan.ScanSettings
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ScanBleFragment : Fragment() {

    companion object {
        private const val REQUEST_ENABLE_BLUETOOTH: Int = 1239
        private const val REQUEST_COARSE_LOCATION: Int = 9358
        private const val REQUEST_ENABLE_GPS: Int = 1514
        val TAG: String = ScanBleFragment::class.java.simpleName
        fun newInstance(): ScanBleFragment = ScanBleFragment()
    }

    private var scanDisposable: Disposable? = null
    private var bluetoothAdapter: BluetoothAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_scan, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureScanButton()
        attemptScan()
    }

    override fun onStop() {
        stopScan()
        bluetoothAdapter?.cancelDiscovery()
        bluetoothAdapter = null
        fabScanDevices.setOnClickListener(null)
        fragmentScanOuterViewGroup.removeAllViews()
        super.onStop()
    }

    private fun canDeviceBleScan(): Boolean = PhoneDevice.hasBlueToothLowEnergy(context!!) && PhoneDevice.hasBlueToothService(context!!)

    private fun configureScanButton() {
        fabScanDevices.setOnClickListener { attemptScan() }
        fabScanDevices.isEnabled = canDeviceBleScan()
    }

    private fun attemptScan() {
        if (!canDeviceBleScan()) {
            return
        }
        if (!PhoneDevice.hasBlueToothEnabled()) {
            startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BLUETOOTH)
            return
        }
        if (Permission.isNotGranted(context!!, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Permission.request(this, Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_COARSE_LOCATION)
            return
        }
        if (!PhoneDevice.hasGPSEnabled(context!!)) {
            MaterialAlertDialogBuilder(context!!)
                .setMessage("gps_needs_to_be_enabled_for_scanning_to_work")
                .setNegativeButton("cancel") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
                .setPositiveButton("go_to_settings") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                    startActivityForResult(
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY),
                        REQUEST_ENABLE_GPS
                    )
                }.setOnDismissListener {
                }.setCancelable(true)
                .show()
            return
        }
        if (isScanning()) {
            stopScan()
            return
        }
        startScan()
    }

    private fun startScan() {
        val startedDiscovery = bluetoothAdapter?.startDiscovery()
        if (startedDiscovery == false) {
            stopScan()
            return
        }
        scanDisposable = RxBle.get()
            .scanBleDevices(
                ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                    .build()
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                onScanError() // commenting this out stops leak
            }
            .doOnNext {
            }
            .subscribe()
    }

    private fun stopScan() {
        clearScanSubscription()
    }

    private fun onScanError() {
    }

    private fun clearScanSubscription() {
        if (scanDisposable?.isDisposed == false) {
            scanDisposable?.dispose()
        }
        scanDisposable = null
    }

    private fun isScanning(): Boolean = scanDisposable != null && !scanDisposable!!.isDisposed

}
