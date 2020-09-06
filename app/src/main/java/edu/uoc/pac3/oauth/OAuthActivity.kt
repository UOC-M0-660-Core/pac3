package edu.uoc.pac3.oauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import edu.uoc.pac3.LaunchActivity
import edu.uoc.pac3.R
import kotlinx.android.synthetic.main.activity_oauth.*
import kotlinx.coroutines.launch

class OAuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oauth)
        launchOAuthAuthorization()
    }

    private fun launchOAuthAuthorization() {
        // TODO: Create URI

        // TODO: Set webView Redirect Listener

        // TODO: Load OAuth Uri
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://id.twitch.tv/")
    }

    // Call this method after obtaining the authorization code
    // on the WebView to obtain the tokens
    private fun getTokens(authorizationCode: String) {

        // Show Loading Indicator
        progressBar.visibility = View.VISIBLE

        // Launch new thread attached to this Activity.
        // If the Activity is closed, this Thread will be cancelled
        lifecycleScope.launch {
            // TODO: Get Tokens from Twitch


            // TODO: Save access token and refresh token using the SessionManager class


            // Hide Loading Indicator
            progressBar.visibility = View.GONE

            // Restart app to navigate to StreamsActivity
            startActivity(Intent(this@OAuthActivity, LaunchActivity::class.java))
            finish()
        }
    }
}