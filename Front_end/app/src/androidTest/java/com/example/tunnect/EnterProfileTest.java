package com.example.tunnect;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EnterProfileTest {
    @Rule
    public ActivityScenarioRule<SplashActivity> activityRule = new ActivityScenarioRule<>(SplashActivity.class);

    @Test
    public void NewUserEnterProfile() throws InterruptedException {
        Thread.sleep(10000);

        // Open up profile from main activity
        ViewInteraction messageButton = onView(withId(R.id.messages_btn));
        messageButton.check(matches(isDisplayed()));
        messageButton.perform(click());

        ViewInteraction saveButton = onView(withId(R.id.save_profile));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        // Check all elements in profile page exist
        ViewInteraction button = onView(withId(R.id.save_profile));
        button.check(matches(isDisplayed()));
        ViewInteraction editText1 = onView(withId(R.id.enter_username));
        editText1.check(matches(isDisplayed()));
        ViewInteraction editText2 = onView(withId(R.id.enter_favourite_artist));
        editText2.check(matches(isDisplayed()));
        ViewInteraction enterColour = onView(withId(R.id.enter_colour));
        enterColour.check(matches(isDisplayed()));

        // Try to enter in first field then press save
        editText1.perform(replaceText("Test Name"), closeSoftKeyboard());
        editText1.perform(pressImeActionButton());

        saveButton.perform(click());

        // Try to enter in second field then press save
        editText2.perform(replaceText("Test Artist"), closeSoftKeyboard());
        editText2.perform(pressImeActionButton());

        saveButton.perform(click());

        // Select a colour then press save
        enterColour.perform(click());

        ViewInteraction okColourBtn = onView(withId(R.id.okColorButton));
        okColourBtn.perform(click());

        saveButton.perform(click());
    }
}
