package com.example.tunnect;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
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
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchActivityTest {

    @Rule
    public ActivityScenarioRule<SplashActivity> activityRule = new ActivityScenarioRule<>(SplashActivity.class);

    @Test
    public void searchActivityTest() throws InterruptedException {
        Thread.sleep(10000);

        onView(withId(R.id.profile_btn)).perform(click());
        onView(withId(R.id.add_songs)).perform(click());
        onView(withId(R.id.search_bar)).perform(replaceText("Never gonna give you up"), closeSoftKeyboard());
        Thread.sleep(3000);
        onView(withId(R.id.search_button)).perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.add_btn), withText("Add"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.song_list),
                                        0),
                                3),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction button = onView(
                allOf(withId(R.id.add_btn),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.song_list),
                                        0),
                                3),
                        isDisplayed()));
        button.check(matches(isDisplayed()));
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
