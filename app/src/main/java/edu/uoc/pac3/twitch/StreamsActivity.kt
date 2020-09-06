package edu.uoc.pac3.twitch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.uoc.pac3.R
import kotlinx.android.synthetic.main.activity_streams.*

class StreamsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streams)
        // Get Streams
        getStreams()
        // Swipe to Refresh Listener
        swipeRefreshLayout.setOnRefreshListener {
            getStreams()
        }
    }

    private fun getStreams(cursor: String? = null) {
        // Show Loading
        swipeRefreshLayout.isRefreshing = true

        // TODO: Get Twitch Streams


        // TODO: Update UI with Streams


        // Hide Loading
        swipeRefreshLayout.isRefreshing = false
    }
}