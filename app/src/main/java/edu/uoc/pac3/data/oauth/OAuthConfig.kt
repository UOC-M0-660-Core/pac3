package edu.uoc.pac3.data.oauth

/**
 * Created by alex on 4/26/21.
 */
interface OAuthConfig {

    fun getClientId(): String
    fun getClientSecret(): String
}

// The OAuth config implementation for your app
// Example usage: `config.getClientId()`
object MyOAuthConfig : OAuthConfig {
    // TODO: Set your Client ID from Twitch
    override fun getClientId(): String = "123456"

    // TODO: Set your Client Secret from Twitch
    override fun getClientSecret(): String = "abcdefg"
}

// Use this singleton class to access clientId and clientSecret when needed on the app
// Example: `OAuthConfigProvider.config`
object OAuthConfigProvider {
    var config: OAuthConfig = MyOAuthConfig
}