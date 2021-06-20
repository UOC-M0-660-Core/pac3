package edu.uoc.pac3.data.network

import android.content.Context
import edu.uoc.pac3.data.oauth.OAuthConfig
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*

/**
 * Created by alex on 07/09/2020.
 * DO NOT CHANGE the methods' signature
 */
object Network {

    private const val TAG = "Network"

    // Create a new HttpClient when you need it
    // Example from an Activity: `Network.createHttpClient(this, OAuthConfigProvider.config)`
    fun createHttpClient(context: Context, config: OAuthConfig): HttpClient {
        return HttpClient(OkHttp) {
            // TODO: Setup HttpClient
            // Tip: Add header with *config.getClientId()* to all requests

            // TODO: Add token if available to every request
            // Tip: Use Context to create a new *SessionManager* to obtain the access token

            // TODO (Ex 6): Use OAuthFeature to refresh token when Twitch returns 401 Unauthorized
            // Tip: Use *twitchClientId* and *config.getClientSecret()* to refresh the token
            // Caution: OAuthFeature refreshToken is a "suspend function" where will need to
            // create a new client, perform the refresh tokens request to Twitch and save the result
        }
    }
}