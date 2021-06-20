package edu.uoc.pac3.oauth

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import edu.uoc.pac3.LaunchActivity
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Endpoints
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.oauth.OAuthConfigProvider
import kotlinx.android.synthetic.main.activity_oauth.*
import kotlinx.coroutines.launch
import java.util.*

class OAuthActivity : AppCompatActivity() {

    private val redirectUri = "http://localhost"
    private val scopes = listOf("user:read:email user:edit")
    private val uniqueState = UUID.randomUUID().toString()
    private val oauthConfig = OAuthConfigProvider.config

    private val TAG = "StreamsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oauth)
        launchOAuthAuthorization()
    }

    fun buildOAuthUri(): Uri {
        return Uri.parse(Endpoints.authorizationUrl)
            .buildUpon()
            .appendQueryParameter("client_id", oauthConfig.getClientId())
            .appendQueryParameter("redirect_uri", redirectUri)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("scope", scopes.joinToString(separator = " "))
            .appendQueryParameter("state", uniqueState)
            .build()
    }

    private fun launchOAuthAuthorization() {
        //  Create URI
        val uri = buildOAuthUri()

        // Set webView Redirect Listener
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                request?.let {
                    // Check if this url is our OAuth redirect, otherwise ignore it
                    if (request.url.toString().startsWith(redirectUri)) {
                        // To prevent CSRF attacks, check that we got the same state value we sent, otherwise ignore it
                        val responseState = request.url.getQueryParameter("state")
                        if (responseState == uniqueState) {
                            // This is our request, obtain the code!
                            request.url.getQueryParameter("code")?.let { code ->
                                // Got it!
                                Log.d("OAuth", "Here is the authorization code! $code")
                                onAuthorizationCodeRetrieved(code)
                                // Hide WebView
                                webView.visibility = View.GONE
                            } ?: run {
                                // User cancelled the login flow
                                // Handle error
                                Toast.makeText(
                                    this@OAuthActivity,
                                    getString(R.string.error_oauth),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        // Load OAuth Uri
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(uri.toString())
    }

    // Call this method after obtaining the authorization code
    // on the WebView to obtain the tokens
    private fun onAuthorizationCodeRetrieved(authorizationCode: String) {

        // Show Loading Indicator
        progressBar.visibility = View.VISIBLE

        // Create Twitch Service
        val service = TwitchApiService(Network.createHttpClient(this@OAuthActivity, OAuthConfigProvider.config))
        // Launch new thread attached to this Activity.
        // If the Activity is closed, this Thread will be cancelled
        lifecycleScope.launch {

            // Launch get Tokens Request
            service.getTokens(authorizationCode, oauthConfig)?.let { response ->
                // Success :)

                Log.d(TAG, "Got Access token ${response.accessToken}")

                // Save access token and refresh token using the SessionManager class
                val sessionManager = SessionManager(this@OAuthActivity)
                sessionManager.saveAccessToken(response.accessToken)
                response.refreshToken?.let {
                    sessionManager.saveRefreshToken(it)
                }
            } ?: run {
                // Failure :(

                // Show Error Message
                Toast.makeText(
                    this@OAuthActivity,
                    getString(R.string.error_oauth),
                    Toast.LENGTH_LONG
                ).show()
                // Restart Activity
                finish()
                startActivity(Intent(this@OAuthActivity, OAuthActivity::class.java))
            }

            // Hide Loading Indicator
            progressBar.visibility = View.GONE

            // Restart app to navigate to StreamsActivity
            startActivity(Intent(this@OAuthActivity, LaunchActivity::class.java))
            finish()
        }
    }
}