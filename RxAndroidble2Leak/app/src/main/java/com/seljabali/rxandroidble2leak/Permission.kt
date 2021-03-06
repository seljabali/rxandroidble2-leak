package com.seljabali.rxandroidble2leak

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION_CODES.M
import androidx.annotation.IntRange
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object Permission {

    /**
     *  Has Permission
     */
    fun isNeeded(): Boolean = Build.VERSION.SDK_INT >= M

    fun isNotNeeded(): Boolean = !isNeeded()

    fun isGranted(context: Context, permission: String): Boolean = isNotNeeded() || ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    fun isNotGranted(context: Context, permission: String): Boolean = !isGranted(context, permission)

    /**
     *  Request Permission
     */
    fun request(activity: Activity, permission: String, @IntRange(from = 0, to = 65535) requestCode: Int) {
        ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
    }

    fun request(activity: Activity, permissions: Array<String>, @IntRange(from = 0, to = 65535) requestCode: Int) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }

    fun request(fragment: Fragment, permission: String, @IntRange(from = 0, to = 65535) requestCode: Int) {
        fragment.requestPermissions(arrayOf(permission), requestCode)
    }

    fun request(fragment: Fragment, permissions: Array<String>, @IntRange(from = 0, to = 65535) requestCode: Int) {
        fragment.requestPermissions(permissions, requestCode)
    }

    /**
     *  Granted Permission
     */
    fun wasGranted(grantResult: Int): Boolean = grantResult == PackageManager.PERMISSION_GRANTED

    fun wasGranted(grantResults: IntArray): Boolean = if (grantResults.isEmpty()) false else wasGranted(grantResults[0])
}