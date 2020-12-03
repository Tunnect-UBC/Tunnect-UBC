package com.example.tunnect;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import junit.framework.TestCase;

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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.fail;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SimplicityTest {

    @Rule
    public ActivityScenarioRule<SplashActivity> activityRule = new ActivityScenarioRule<>(SplashActivity.class);

    @Test
    public void SimplicityTest() throws InterruptedException {
        int buttonCount = 0;

        Thread.sleep(15000);

        // Test Liking/Disliking
        buttonCount++;
        onView(withId(R.id.like_btn)).perform(click());
        if (buttonCount > 4)
            TestCase.fail("Too many button presses");
        buttonCount = 0;

        buttonCount++;
        onView(withId(R.id.dislike_btn)).perform(click());
        if (buttonCount > 4)
            TestCase.fail("Too many button presses");
        buttonCount = 0;

        // Test editing profile info
        buttonCount++;
        onView(withId(R.id.profile_btn)).perform(click());
        buttonCount++;
        onView(withId(R.id.save_profile)).perform(click());
        if (buttonCount > 4)
            TestCase.fail("Too many button presses");
        buttonCount = 0;

        // Navigate to the main screen
        onView(withContentDescription("Navigate up")).perform(click());

        // Test adding a song
        buttonCount++;
        onView(withId(R.id.profile_btn)).perform(click());
        buttonCount++;
        onView(withId(R.id.add_songs)).perform(click());
        onView(withId(R.id.search_bar)).perform(replaceText("all star"), closeSoftKeyboard());
        Thread.sleep(1000);
        buttonCount++;
        onView(withId(R.id.search_button)).perform(click());
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.add_btn), withText("Add"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.song_list),
                                        0),
                                3),
                        isDisplayed()));
        appCompatButton.perform(click());
        if (buttonCount > 4)
            TestCase.fail("Too many button presses");
        buttonCount = 0;

        // Test deleting account
        buttonCount++;
        //onView(withId(R.id.settings_btn)).perform(click());
        buttonCount++;
        //onView(withId(R.id.delete_account)).perform(click());

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
