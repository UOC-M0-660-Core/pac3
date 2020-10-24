package edu.uoc.pac3.twitch.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import edu.uoc.pac3.R
import edu.uoc.pac3.data.network.Endpoints
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.oauth.LoginActivity
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.user.User
import edu.uoc.pac3.data.user.UsersResponse
import io.ktor.client.request.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private val TAG = "ProfileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        // Get User Profile
        lifecycleScope.launch {
            getUserProfile()
        }
        // Update Description Button Listener
        updateDescriptionButton.setOnClickListener {
            // Update User Description
            lifecycleScope.launch {
                updateUserDescription(userDescriptionEditText.text?.toString() ?: "")
            }
        }
        // Logout Button Listener
        logoutButton.setOnClickListener {
            // Logout
            logout()
        }
    }

    private suspend fun getUserProfile() {
        progressBar.visibility = VISIBLE
        // Retrieve the Twitch User Profile using the API
        try {
            val response = Network.createHttpClient(this)
                .get<UsersResponse>(Endpoints.usersUrl)

            response.data?.firstOrNull()?.let { user ->
                // Update the UI with the user data
                setUserInfo(user)
            }
        } catch (t: Throwable) {
            Log.w(TAG, "Error getting user profile", t)
            showError(getString(R.string.error_profile))
        }
        progressBar.visibility = GONE
    }


    private suspend fun updateUserDescription(description: String) {
        progressBar.visibility = VISIBLE
        // Update the Twitch User Description using the API
        try {
            val response = Network.createHttpClient(this)
                .put<UsersResponse>(Endpoints.usersUrl) {
                    parameter("description", description)
                }
            // Refresh
            response.data?.firstOrNull()?.let { user ->
                setUserInfo(user)
            }
        } catch (t: Throwable) {
            Log.w(TAG, "Error updating user description", t)
            showError(getString(R.string.error_profile_update))
        }

        progressBar.visibility = GONE
    }

    private fun setUserInfo(user: User) {
        // Set Texts
        userNameTextView.text = user.userName
        userDescriptionEditText.setText(user.description ?: "")
        // Avatar Image
        user.profileImageUrl?.let {
            Glide.with(this)
                .load(user.getSizedImage(it, 128, 128))
                .centerCrop()
                .transform(CircleCrop())
                .into(imageView)
        }
        // Views
        viewsText.text = getString(R.string.views_text, user.viewCount)
    }

    private fun logout() {
        SessionManager(this).clearAccessToken()
        SessionManager(this).clearRefreshToken()
        // Open Login
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun showError(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    // Override Action Bar Home button to just finish the Activity
    // not to re-launch the parent Activity (StreamsActivity)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            false
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}