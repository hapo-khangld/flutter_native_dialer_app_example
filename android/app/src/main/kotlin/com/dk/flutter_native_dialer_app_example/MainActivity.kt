package com.dk.flutter_native_dialer_app_example

import android.Manifest
import android.annotation.SuppressLint
import android.app.role.RoleManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.telecom.PhoneAccount
import android.telecom.TelecomManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.dk.flutter_native_dialer_app_example.phone.extensions.telecomManager
import com.dk.flutter_native_dialer_app_example.phone.helper.CallBlockingHelper
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterFragmentActivity() {
    private val CHANNEL = "example.com/native_channel"
    private val REQUEST_ID = 1

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        val methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
        methodChannel.setMethodCallHandler { call, result ->
            when (call.method) {
                "requestDialerRole" -> requestDialerRole(result)
                "addPhoneNumberToBlocked" -> {
                    val blockedHelper = CallBlockingHelper(this)
                    blockedHelper.blockNumber("0961783723")
                }
                "actionCallToNumber" -> {
                    val phoneNumber = call.argument<String>("phoneNumber")
                    if (phoneNumber != null) {
                        initOutgoingCall(phoneNumber)
                        result.success(true)
                    } else {
                        result.success(false)
                    }
                }

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

    private fun initOutgoingCall(number: String?) {
        Log.i(TAG, "Phone number is: $number")
        if (number != null) {
            val callNumber = Uri.parse("tel:$number")
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.i(TAG, "No permission to call.")
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), 2)
                return
            } else {
                val handle = telecomManager.getDefaultOutgoingPhoneAccount(PhoneAccount.SCHEME_TEL)
                if (handle != null) {
                    val bundle = Bundle().apply {
                        putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, handle)
                        putBoolean(TelecomManager.EXTRA_START_CALL_WITH_VIDEO_STATE, false)
                        putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, false)
                    }
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        Log.i(TAG, "No permission to call.")
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
                        return
                    }
                    telecomManager.placeCall(callNumber, bundle)
                }
            }
        }
    }
}
