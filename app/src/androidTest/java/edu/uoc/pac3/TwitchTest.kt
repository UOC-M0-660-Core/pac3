package edu.uoc.pac3

import androidx.test.core.app.ApplicationProvider
import edu.uoc.pac3.data.oauth.OAuthConfigProvider
import kotlinx.coroutines.runBlocking
import org.junit.Before

/**
 * Created by alex on 24/10/2020.
 */

abstract class TwitchTest {

    protected val twitchService =
        TestData.provideTwitchService(ApplicationProvider.getApplicationContext())

    @Before
    fun saveAccessToken() {
        // Launch Refresh Request
        runBlocking {
            TestData.setAccessToken(ApplicationProvider.getApplicationContext())
        }
    }

    @Before
    fun setTestOAuthConfig() {
        OAuthConfigProvider.config = TestData.testOAuthConfig
    }
}