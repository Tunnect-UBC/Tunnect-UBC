package com.example.tunnect;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertTrue;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SimplicityTest {

    @Rule
    public ActivityScenarioRule<SplashActivity> activityRule = new ActivityScenarioRule<>(SplashActivity.class);

    @Test
    public void simplicityTest() throws InterruptedException {
        int buttonCount = 0;

        // Sleep until the login process is finished
        Thread.sleep(5000);

        // Test Liking/Disliking
        buttonCount++;
        onView(withId(R.id.like_btn)).perform(click());
        assertTrue(buttonCount < 4);
        buttonCount = 0;

        buttonCount++;
        onView(withId(R.id.dislike_btn)).perform(click());
        assertTrue(buttonCount < 4);
        buttonCount = 0;

        // Test editing profile info
        buttonCount++;
        onView(withId(R.id.profile_btn)).perform(click());
        buttonCount++;
        onView(withId(R.id.save_profile)).perform(click());
        assertTrue(buttonCount < 4);
        buttonCount = 0;

        // Test adding a song
        buttonCount++;
        onView(withId(R.id.profile_btn)).perform(click());
        buttonCount++;
        onView(withId(R.id.add_songs)).perform(click());
        onView(withId(R.id.search_bar)).perform(replaceText("all star"), closeSoftKeyboard());
        Thread.sleep(1000);
        buttonCount++;
        onView(withId(R.id.search_button)).perform(click());
        ViewInteraction appCompatButton = onView(allOf(withId(R.id.add_btn), withText("Add"),
                childAtPosition(childAtPosition(withId(R.id.song_list), 0), 3), isDisplayed()));
        appCompatButton.perform(click());
        assertTrue(buttonCount < 4);
        buttonCount = 0;

        // Navigate to the main screen
        onView(withContentDescription("Navigate up")).perform(click());
        onView(withContentDescription("Navigate up")).perform(click());

        // Test sending a message
        buttonCount++;
        onView(withId(R.id.messages_btn)).perform(click());
        ViewInteraction recyclerView = onView(withId(R.id.chatOptions));
        buttonCount++;
        recyclerView.perform(actionOnItemAtPosition(0, click()));
        buttonCount++;
        onView(withId(R.id.send_btn)).perform(click());
        assertTrue(buttonCount < 4);
        buttonCount = 0;

        // Navigate to the main screen
        onView(withContentDescription("Navigate up")).perform(click());
        onView(withContentDescription("Navigate up")).perform(click());

        // Test deleting a song
        buttonCount++;
        onView(withId(R.id.profile_btn)).perform(click());
        Thread.sleep(1000);
        buttonCount++;
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.dlt_btn), withText("Delete"), childAtPosition(
                        childAtPosition(withId(R.id.selectedSongs), 0), 3), isDisplayed()));
        appCompatButton2.perform(click());
        buttonCount++;
        onView(withId(R.id.save_profile)).perform(click());
        assertTrue(buttonCount < 4);
        buttonCount = 0;


        // Test deleting account
        buttonCount++;
        onView(withId(R.id.settings_btn)).perform(click());
        buttonCount++;
        // We don't actually hit the delete account button so the test can be run multiple times
        assertTrue(buttonCount < 4);

    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
