package edu.uoc.pac3.data.oauth

/**
 * Created by alex on 07/09/2020.
 */

// TODO: Fill with missing parameters
// TODO: Serialize from JSON

data class OAuthTokensResponse(
    val accessToken: String,
    val refreshToken: String? = null,
)
