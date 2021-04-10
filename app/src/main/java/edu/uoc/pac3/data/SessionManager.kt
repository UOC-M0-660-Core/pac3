package edu.uoc.pac3.data

import android.content.Context

/**
 * Created by alex on 06/09/2020.
 * DO NOT CHANGE the methods' signature
 */

class SessionManager(context: Context) {

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
    }

    fun clearAccessToken() {
        // TODO("Clear Access Token")
    }

    fun getRefreshToken(): String? {
        // TODO("Get Refresh Token")
        return null
    }

    fun saveRefreshToken(refreshToken: String) {
        // TODO("Save Refresh Token")
    }

    fun clearRefreshToken() {
        // TODO("Clear Refresh Token")
    }

}