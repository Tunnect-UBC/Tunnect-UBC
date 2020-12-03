package com.example.tunnect;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;


import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertNotNull;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BadSearchTest {

    @Rule
    public ActivityScenarioRule<SplashActivity> activityRule = new ActivityScenarioRule<>(SplashActivity.class);

    @Test
    public void BadSearchTest() throws InterruptedException {
        Thread.sleep(5000);

        onView(withId(R.id.profile_btn)).perform(click());
        onView(withId(R.id.add_songs)).perform(click());
        onView(withId(R.id.search_bar)).perform(replaceText("dfghdfghdfghdfghdfh"), closeSoftKeyboard());
        Thread.sleep(1000);
        onView(withId(R.id.search_button)).perform(click());

        assertNotNull(onView(withRecyclerView(R.id.song_list).atPosition(0))
                .check(matches(hasDescendant(withText("No Search Results")))));

    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }
}
