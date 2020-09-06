package edu.uoc.pac3.twitch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.uoc.pac3.R

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        getUserProfile()
    }

    private fun getUserProfile() {
        // TODO: Retrieve the Twitch User Profile using the API

        // TODO: Update the UI with the user data
    }


    private fun updateUserDescription(description: String) {
        // TODO: Update the Twitch User Description using the API


        // Refresh
        getUserProfile()
    }
}