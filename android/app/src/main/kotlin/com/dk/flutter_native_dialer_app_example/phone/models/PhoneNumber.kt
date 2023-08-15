package com.dk.flutter_native_dialer_app_example.phone.models

data class PhoneNumber(var value: String, var type: Int, var label: String, var normalizedNumber: String, var isPrimary: Boolean = false)
