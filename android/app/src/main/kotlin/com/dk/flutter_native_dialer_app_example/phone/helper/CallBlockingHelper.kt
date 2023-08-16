package com.dk.flutter_native_dialer_app_example.phone.helper

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.BlockedNumberContract
import androidx.annotation.RequiresApi

class CallBlockingHelper(private val context: Context) {

    fun blockNumber(phoneNumber: String) {
        val values = ContentValues().apply {
            put(BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER, phoneNumber)
        }

        context.contentResolver.insert(BlockedNumberContract.BlockedNumbers.CONTENT_URI, values)
    }
}