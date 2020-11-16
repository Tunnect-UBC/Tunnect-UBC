package com.example.tunnect;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MessageUserTest {

    @Rule
    public ActivityTestRule<SplashActivity> activityRule = new ActivityTestRule<>(SplashActivity.class);

    @Test
    public void MessageAnotherUser() throws InterruptedException {
        Thread.sleep(5000);

        // Open up a chat from main activity
        ViewInteraction messageButton = onView(withId(R.id.messages_btn));
        messageButton.check(matches(isDisplayed()));
        messageButton.perform(click());

        ViewInteraction recyclerView = onView(withId(R.id.chatOptions));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        // Try sending an empty message, then send an actual message
        ViewInteraction sendButton = onView(withId(R.id.send_btn));
        sendButton.check(matches(isDisplayed()));
        sendButton.perform(click());

        ViewInteraction editText = onView(withId(R.id.edit_text_chatbox));
        editText.check(matches(isDisplayed()));
        editText.perform(replaceText("Test Message"), closeSoftKeyboard());

        sendButton.perform(click());
        ViewInteraction sentM = onView(withId(R.id.sent_message));
        sentM.check(matches(withText("Test Message")));

        // Go back to main activity
        ViewInteraction return1 = onView(withContentDescription("Navigate up"));
        return1.perform(click());
        ViewInteraction return2 = onView(withContentDescription("Navigate up"));
        return2.perform(click());
    }

}
