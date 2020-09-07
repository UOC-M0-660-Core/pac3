package edu.uoc.pac3.twitch.streams

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import edu.uoc.pac3.R
import edu.uoc.pac3.network.Endpoints
import edu.uoc.pac3.network.Network
import edu.uoc.pac3.oauth.LoginActivity
import edu.uoc.pac3.twitch.profile.ProfileActivity
import io.ktor.client.features.*
import io.ktor.client.request.*
import kotlinx.android.synthetic.main.activity_streams.*
import kotlinx.coroutines.launch

class StreamsActivity : AppCompatActivity() {

    private val TAG = "StreamsActivity"

    private val adapter = StreamsAdapter()
    private val layoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streams)
        // Init RecyclerView
        initRecyclerView()
        // Swipe to Refresh Listener
        swipeRefreshLayout.setOnRefreshListener {
            getStreams()
        }
        // Get Streams
        getStreams()
    }

    private fun initRecyclerView() {
        // Set Layout Manager
        recyclerView.layoutManager = layoutManager
        // Set Adapter
        recyclerView.adapter = adapter
        // Set Pagination Listener
        recyclerView.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                getStreams(nextCursor)
            }

            override fun isLastPage(): Boolean {
                return nextCursor == null
            }

            override fun isLoading(): Boolean {
                return swipeRefreshLayout.isRefreshing
            }
        })
    }

    private var nextCursor: String? = null
    private fun getStreams(cursor: String? = null) {
        Log.d(TAG, "Requesting streams with cursor $cursor")

        // Show Loading
        swipeRefreshLayout.isRefreshing = true

        // Get Twitch Streams
        lifecycleScope.launch {
            try {
                val response = Network.createHttpClient(this@StreamsActivity)
                    .get<StreamsResponse>(Endpoints.streamsUrl) {
                        cursor?.let { parameter("after", it) }
                    }
                Log.d("StreamsActivity", "Got Streams: $response")

                // Update UI with Streams
                val streams = response.data.orEmpty()
                if (cursor != null) {
                    // We are adding more items to the list
                    adapter.submitList(adapter.currentList.plus(streams))
                } else {
                    // It's the first n items, no pagination yet
                    adapter.submitList(streams)
                }
                // Save cursor for next request
                nextCursor = response.pagination?.cursor
            } catch (t: Throwable) {
                Log.w(TAG, "Error getting streams", t)
                // Show Error message to not leave the page empty
                if (adapter.currentList.isNullOrEmpty()) {
                    Toast.makeText(
                        this@StreamsActivity,
                        getString(R.string.error_streams), Toast.LENGTH_SHORT
                    ).show()
                }
                // Try to handle error
                when (t) {
                    is ClientRequestException -> {
                        // Check if it's a 401 Unauthorized
                        if (t.response?.status?.value == 401) {
                            // User was logged out, close screen and open login again
                            finish()
                            startActivity(Intent(this@StreamsActivity, LoginActivity::class.java))
                        }
                    }
                }
            }

            // Hide Loading
            swipeRefreshLayout.isRefreshing = false
        }

    }

    // region Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate Menu
        menuInflater.inflate(R.menu.menu_streams, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.menu_user -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    // endregion
}