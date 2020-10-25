package edu.uoc.pac3

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.network.Endpoints
import edu.uoc.pac3.data.oauth.OAuthTokensResponse
import edu.uoc.pac3.twitch.streams.StreamsActivity
import io.ktor.client.request.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by alex on 24/10/2020.
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class Ex3Test : TwitchTest() {

    @get:Rule
    val activityRule = ActivityScenarioRule(StreamsActivity::class.java)

    @Test
    fun getsNewAccessToken() {
        val context: Context = ApplicationProvider.getApplicationContext()
        runBlocking {
            val response =
                TestData.provideHttpClient(context).post<OAuthTokensResponse>(Endpoints.tokenUrl) {
                    parameter("client_id", "efwo35z4mgyiyhje8bbp73b98oyavf")
                    parameter("client_secret", "7fl44yqjm5tjdx73z45dd9ybwuuiez")
                    parameter("refresh_token", TestData.refreshToken)
                    parameter("grant_type", "refresh_token")
                }
            // Save new access token
            SessionManager(context).saveAccessToken(response.accessToken)
            delay(TestData.sharedPrefsWaitingMillis)
            assert(SessionManager(context).getAccessToken() != null) {
                "Access token cannoot be null"
            }
        }
    }

    @Test
    fun retrievesStreams() {
        runBlocking {
            val streams = twitchService.getStreams()
            assert(!streams?.data.isNullOrEmpty()) {
                "Streams response cannot be empty"
            }
        }
    }

    @Test
    fun listDisplaysStreamProperties() {
        // Give some time for actual data to be loaded on RecyclerView
        Thread.sleep(TestData.networkWaitingMillis)
        // Get Streams
        val streams = runBlocking {
            twitchService.getStreams()?.data.orEmpty()
        }
        // Check they are displayed in list
        Espresso.onView(ViewMatchers.withText(streams.first().userName ?: ""))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(streams.first().title ?: ""))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

}