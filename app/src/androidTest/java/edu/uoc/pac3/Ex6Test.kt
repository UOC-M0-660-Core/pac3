package edu.uoc.pac3

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import edu.uoc.pac3.data.SessionManager
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by alex on 24/10/2020.
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class Ex6Test {

    private val twitchService =
        TestData.provideTwitchService(ApplicationProvider.getApplicationContext())

    @Test
    fun refreshesTokenOnUnauthorized() {
        val sessionManager = SessionManager(ApplicationProvider.getApplicationContext())
        // Save valid refreshToken
        sessionManager.saveRefreshToken(TestData.refreshToken)
        // Save invalid accessToken
        sessionManager.saveAccessToken(TestData.dummyAccessToken)
        Thread.sleep(TestData.sharedPrefsWaitingMillis)
        // Test Streams Request
        runBlocking {
            val streams = twitchService.getStreams()
            assert(sessionManager.getAccessToken() != TestData.dummyAccessToken) {
                "New access token should be saved"
            }
            assert(streams != null) {
                "Streams should be retrieved correctly with the new access token"
            }
        }
    }

}