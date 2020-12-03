package com.example.tunnect;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
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


import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchActivityTest {

    @Rule
    public ActivityScenarioRule<SplashActivity> activityRule = new ActivityScenarioRule<>(SplashActivity.class);

    @Test
    public void searchActivityTest() throws InterruptedException {
        Thread.sleep(5000);

        onView(withId(R.id.profile_btn)).perform(click());
        onView(withId(R.id.add_songs)).perform(click());
        onView(withId(R.id.search_bar)).perform(replaceText("dfghdfghdfghdfghdfh"), closeSoftKeyboard());
        Thread.sleep(1000);
        onView(withId(R.id.search_button)).perform(click());

        onView(withRecyclerView(R.id.song_list).atPosition(0))
                .check(matches(hasDescendant(withText("No Search Results"))));

        onView(withId(R.id.search_bar)).perform(replaceText("Never Gonna Give You Up"), closeSoftKeyboard());
        Thread.sleep(1000);
        onView(withId(R.id.search_button)).perform(click());
        onView(withRecyclerView(R.id.song_list).atPosition(0))
                .check(matches(hasDescendant(withText("Never Gonna Give You Up"))));
        onView(withRecyclerView(R.id.song_list).atPosition(0))
                .check(matches(hasDescendant(withText("Rick Astley"))));
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }
}
