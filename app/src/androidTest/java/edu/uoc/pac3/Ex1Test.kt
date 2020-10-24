package edu.uoc.pac3

import android.net.Uri
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import edu.uoc.pac3.oauth.OAuthActivity
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

/**
 * Created by alex on 24/10/2020.
 */

@LargeTest
class Ex1Test {

    @get:Rule
    val activityRule = ActivityScenarioRule(OAuthActivity::class.java)

    @Test
    fun createsOauthUri() {
        activityRule.scenario.onActivity {
            val uri = it.buildOAuthUri()
            assert(uri != Uri.EMPTY)
            assert(uri.getQueryParameter("response_type") == "code") {
                "OAuth Flow must be Authorization Code"
            }
        }
    }

    @Test
    fun invalidAuthCodeReturnsNullTokens() {
        activityRule.scenario.onActivity {
            runBlocking {
                val apiService = TestData.provideTwitchService(it)
                val response = apiService.getTokens("12345")
                assert(response?.accessToken == null) {
                    "Invalid code should return null access token"
                }
            }
        }
    }
}