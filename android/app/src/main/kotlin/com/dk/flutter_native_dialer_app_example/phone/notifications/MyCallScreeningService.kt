package com.dk.flutter_native_dialer_app_example.phone.notifications

import android.content.Intent
import android.telecom.Call
import android.telecom.CallScreeningService

class MyCallScreeningService : CallScreeningService() {
    override fun onScreenCall(callDetails: Call.Details) {
        // Kiểm tra xem cuộc gọi có phải là số bị chặn hay không
        val isBlocked = checkIfNumberIsBlocked(callDetails.handle.schemeSpecificPart)

        if (!isBlocked) {
            // Cuộc gọi không bị chặn, gửi Intent tới BroadcastReceiver
            val intent = Intent("com.example.ACTION_CALL_NOT_BLOCKED")
            intent.putExtra("incomingNumber", callDetails.handle.schemeSpecificPart)
            sendBroadcast(intent)
        } else {
            respondToCall(callDetails)
        }
    }

    // Hàm kiểm tra số bị chặn
    private fun checkIfNumberIsBlocked(phoneNumber: String): Boolean {
        // Thực hiện kiểm tra trong cơ sở dữ liệu hoặc các cơ chế chặn khác
        // Trả về true nếu số bị chặn, ngược lại trả về false
        return phoneNumber == "09617837266"
    }

    private fun respondToCall(callDetails: Call.Details) {
        val response = CallResponse.Builder()
            .setDisallowCall(true)
            .setRejectCall(true)
            .setSkipCallLog(true)
            .setSkipNotification(true)
            .build()
        respondToCall(callDetails, response)
    }
}