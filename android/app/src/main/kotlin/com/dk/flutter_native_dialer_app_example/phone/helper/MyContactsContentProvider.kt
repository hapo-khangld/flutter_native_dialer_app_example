package com.dk.flutter_native_dialer_app_example.phone.helper

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.dk.flutter_native_dialer_app_example.phone.models.PhoneNumber
import com.dk.flutter_native_dialer_app_example.phone.models.SimpleContact
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MyContactsContentProvider {
    companion object {
        private const val AUTHORITY = "com.dk.flutter_native_dialer_app_example.contactsprovider"
        val CONTACTS_CONTENT_URI = Uri.parse("content://$AUTHORITY/contacts")

        const val FAVORITES_ONLY = "favorites_only"
        const val COL_RAW_ID = "raw_id"
        const val COL_CONTACT_ID = "contact_id"
        const val COL_NAME = "name"
        const val COL_PHOTO_URI = "photo_uri"
        const val COL_PHONE_NUMBERS = "phone_numbers"
        const val COL_BIRTHDAYS = "birthdays"
        const val COL_ANNIVERSARIES = "anniversaries"

        fun getSimpleContacts(context: Context, cursor: Cursor?): ArrayList<SimpleContact> {
            val contacts = ArrayList<SimpleContact>()
            val packageName = context.packageName.removeSuffix(".debug")
            Log.i(MyContactsContentProvider::class.java.name, "Package Name is : $packageName")
            if (packageName != "com.simplemobiletools.dialer" && packageName != "com.simplemobiletools.smsmessenger" && packageName != "com.simplemobiletools.calendar.pro") {
                return contacts
            }

            try {
                cursor?.use {
                    if (cursor.moveToFirst()) {
                        do {
                            val rawId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_RAW_ID))
                            val contactId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CONTACT_ID))
                            val name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME))
                            val photoUri = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHOTO_URI))
                            val phoneNumbersJson = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE_NUMBERS))
                            val birthdaysJson = cursor.getString(cursor.getColumnIndexOrThrow(COL_BIRTHDAYS))
                            val anniversariesJson = cursor.getString(cursor.getColumnIndexOrThrow(COL_ANNIVERSARIES))

                            val phoneNumbersToken = object : TypeToken<ArrayList<PhoneNumber>>() {}.type
                            val phoneNumbers = Gson().fromJson<ArrayList<PhoneNumber>>(phoneNumbersJson, phoneNumbersToken) ?: ArrayList()

                            val stringsToken = object : TypeToken<ArrayList<String>>() {}.type
                            val birthdays = Gson().fromJson<ArrayList<String>>(birthdaysJson, stringsToken) ?: ArrayList()
                            val anniversaries = Gson().fromJson<ArrayList<String>>(anniversariesJson, stringsToken) ?: ArrayList()

                            val contact = SimpleContact(rawId, contactId, name, photoUri, phoneNumbers, birthdays, anniversaries)
                            contacts.add(contact)
                        } while (cursor.moveToNext())
                    }
                }
            } catch (ignored: Exception) {
                throw ignored
            }
            return contacts
        }
    }
}