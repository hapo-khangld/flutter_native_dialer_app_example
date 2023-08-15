package com.dk.flutter_native_dialer_app_example

import android.annotation.SuppressLint
import android.app.role.RoleManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterFragmentActivity() {
    private val CHANNEL = "example.com/native_channel"
    private val REQUEST_ID = 1

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        val methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
        methodChannel.setMethodCallHandler { call, result ->
            when (call.method) {
                "requestDialerRole" -> requestDialerRole(result)
                else -> result.notImplemented()
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun requestDialerRole(result: MethodChannel.Result) {
        val roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager

        if (roleManager.isRoleAvailable(RoleManager.ROLE_DIALER)) {
            val hasRole = roleManager.isRoleHeld(RoleManager.ROLE_DIALER)
            if (!hasRole) {
                val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
                val resolveInfos: List<ResolveInfo> = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                if (resolveInfos.isNotEmpty()) {
                    startActivityForResult(intent, REQUEST_ID)
                    result.success(true)
                } else {
                    result.error("NoActivity", "No activity found to handle the request.", null)
                }
            } else {
                result.success(true)
            }
        } else {
            result.error("RoleUnavailable", "Dialer role is not available on this device.", null)
        }
    }
}
