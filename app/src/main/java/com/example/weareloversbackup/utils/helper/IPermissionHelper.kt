package com.example.weareloversbackup.utils.helper

import android.app.Activity
import android.content.Context

interface IPermissionHelper {
    interface PermissionListener {
        fun onPermissionGranted()
        fun onPermissionDenied(deniedPermissions: List<String>)
    }

    fun isPermissionGranted(context: Context, permission: String): Boolean
    fun allPermissionGranted(context: Context, permissions: List<String>): Boolean

    fun registerPermissionListener(requestCode: Int, listener: PermissionListener)
    fun unregisterPermissionListener(requestCode: Int, listener: PermissionListener)

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
}