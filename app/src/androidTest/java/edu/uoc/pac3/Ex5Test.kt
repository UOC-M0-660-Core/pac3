package edu.uoc.pac3

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import edu.uoc.pac3.twitch.streams.StreamsActivity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by alex on 24/10/2020.
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class Ex5Test : TwitchTest() {

    @Test
    fun retrievesNextPageOfStreams() {
        runBlocking {
            val firstStreams = twitchService.getStreams()
            val cursor = firstStreams?.pagination?.cursor
            assert(cursor != null) {
                "Cursor must not be null"
            }
            val nextStreams = twitchService.getStreams(cursor)
            assert(firstStreams?.data != nextStreams?.data) {
                "Next Streams must be different"
            }
        }
    }

    @Test
    fun recyclerViewBottomScrollLoadsMoreStreams() {
        // Start Activity
        val scenario = ActivityScenario.launch(StreamsActivity::class.java)

        var previousItemCount = 0
        // Wait to load
        Thread.sleep(TestData.networkWaitingMillis)
        scenario.onActivity {
            val recyclerView = it.findViewById<RecyclerView>(R.id.recyclerView)
            assert(recyclerView != null && recyclerView!!.adapter != null) {
                "Recyclerview and Adapter cannot be null"
            }
            previousItemCount = recyclerView!!.adapter!!.itemCount
            assert(previousItemCount > 0) {
                "Adapter cannot be empty"
            }
        }
        // Scroll to bottom
        for (i in 0..3) {
            Espresso.onView(
                CoreMatchers.allOf(
                    withId(R.id.recyclerView),
                    ViewMatchers.isDisplayed()
                )
            ).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    (previousItemCount - 2).toInt(),
                    ViewActions.swipeUp()
                )
            )
        }
        // Wait to load
        Thread.sleep(TestData.networkWaitingMillis)
        // Assert more items added
        scenario.onActivity {
            val recyclerView = it.findViewById<RecyclerView>(R.id.recyclerView)
            val currentItemCount = recyclerView!!.adapter!!.itemCount
            assert(currentItemCount > previousItemCount) {
                "More items were not added: Previous $previousItemCount -> Current: $currentItemCount"
            }
        }

        // End Activity
        scenario.close()
    }

}