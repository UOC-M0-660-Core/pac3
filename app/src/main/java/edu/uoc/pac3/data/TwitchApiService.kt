package edu.uoc.pac3.data

import edu.uoc.pac3.data.oauth.OAuthConfig
import edu.uoc.pac3.data.oauth.OAuthTokensResponse
import edu.uoc.pac3.data.streams.StreamsResponse
import edu.uoc.pac3.data.user.User
import io.ktor.client.*

/**
 * Service class to Interact with the Twitch API.
 * DO NOT CHANGE the methods' signature
 */

class TwitchApiService(private val httpClient: HttpClient) {
    private val TAG = "TwitchApiService"

    /// Gets Access and Refresh Tokens on Twitch
    suspend fun getTokens(authorizationCode: String, config: OAuthConfig): OAuthTokensResponse? {
        // TODO("Get Tokens from Twitch")
        return null
    }

    /// Gets Streams on Twitch
    suspend fun getStreams(cursor: String? = null): StreamsResponse? {
        // TODO("Get Streams from Twitch")
        // TODO("Support Pagination")
        return null
    }

    /// Gets Current Authorized User on Twitch
    suspend fun getUser(): User? {
        // TODO("Get User from Twitch")
        return null
    }

    /// Gets Current Authorized User on Twitch
    suspend fun updateUserDescription(description: String): User? {
        // TODO("Update User Description on Twitch")
        return null
    }
}