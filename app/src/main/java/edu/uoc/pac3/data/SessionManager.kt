package edu.uoc.pac3.data

import android.content.Context
import android.util.Log

/**
 * Created by alex on 06/09/2020.
 * DO NOT CHANGE the methods' signature
 */

class SessionManager(context: Context) {

    private val TAG = "SessionManager"

    // TODO: Access SharedPreferences or EncryptedSharedPreferences

    fun isUserAvailable(): Boolean {
        // TODO: Is User Available
        return false
    }

    fun getAccessToken(): String? {
        // TODO: Get Access Token
        return null
    }

    fun saveAccessToken(accessToken: String) {
        // TODO("Save Access Token")
        Log.i(TAG, "Access Token saved: $accessToken")
    }

    fun clearAccessToken() {
        // TODO("Clear Access Token")
        Log.i(TAG, "Access token cleared")
    }

    fun getRefreshToken(): String? {
        // TODO("Get Refresh Token")
        return null
    }

    fun saveRefreshToken(refreshToken: String) {
        // TODO("Save Refresh Token")
        Log.i(TAG, "Refresh Token saved: $refreshToken")
    }

    fun clearRefreshToken() {
        // TODO("Clear Refresh Token")
        Log.i(TAG, "Refresh token cleared")
    }

}