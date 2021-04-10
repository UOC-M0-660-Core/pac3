package edu.uoc.pac3.data.network

import android.content.Context
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*

/**
 * Created by alex on 07/09/2020.
 * DO NOT CHANGE the methods' signature
 */
object Network {

    private const val TAG = "Network"

    fun createHttpClient(context: Context, twitchClientId: String, twitchClientSecret: String): HttpClient {
        return HttpClient(OkHttp) {
            // TODO: Setup HttpClient
            // Tip: Add header with *twitchClientId* to all requests

            // TODO: Add token if available
            // Tip: Use Context to create a new *SessionManager* to obtain the access token

            // TODO (Ex 6): Use OAuthFeature to refresh token when Twitch returns 401 Unauthorized
            // Tip: Use *twitchClientId* and *twitchClientSecret* to refresh the token
        }
    }
}