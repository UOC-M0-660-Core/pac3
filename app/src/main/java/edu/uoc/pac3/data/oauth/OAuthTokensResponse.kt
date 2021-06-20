package edu.uoc.pac3.data.oauth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by alex on 07/09/2020.
 */

@Serializable
// Fill with missing parameters
// Serialize from JSON
// Important, remember that variables in Kotlin are camelCase!!
// https://kotlinlang.org/docs/coding-conventions.html#naming-rules
// Use the appropriate Serialization annotation to convert snake_case from the JSON to camelCase in Kotlin.

data class OAuthTokensResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("expires_in") val expiresInSeconds: Int? = null,
    @SerialName("token_type") val tokenType: String? = null,
    @SerialName("scope") val scopes: List<String>? = null,
)
