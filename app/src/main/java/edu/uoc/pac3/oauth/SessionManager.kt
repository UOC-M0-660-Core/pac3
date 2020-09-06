package edu.uoc.pac3.oauth

import android.content.Context

/**
 * Created by alex on 06/09/2020.
 */

class SessionManager(private val context: Context) {

    fun isUserAvailable(): Boolean {
        // TODO: Update if needed
        return getAccessToken() != null
    }

    fun getAccessToken(): String? {
        // TODO: Return Access token
        return null
    }

    fun saveAccessToken(accessToken: String) {
        // TODO: Save access token
    }

    fun clearAccessToken() {
        // TODO: Remove access token
    }

    fun getRefreshToken(): String? {
        // TODO: Return Refresh token
        return null
    }

    fun saveRefreshToken(refreshToken: String) {
        // TODO: Save refresh token
    }

    fun clearRefreshToken() {
        // TODO: Remove refresh token
    }
    
}