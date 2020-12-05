package com.example.tunnect;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
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
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertNotNull;

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
    public void newUserEnterProfile() throws InterruptedException {
        Thread.sleep(5000);

        // Profile activity opens up, check entries exist
        ViewInteraction saveButton = onView(withId(R.id.save_profile));
        assertNotNull(saveButton);
        ViewInteraction addSongsBtn = onView(withId(R.id.add_songs));
        assertNotNull(addSongsBtn);
        ViewInteraction enterUsername = onView(withId(R.id.enter_username));
        assertNotNull(enterUsername);
        ViewInteraction enterGenre = onView(withId(R.id.enter_fav_genre));
        assertNotNull(enterGenre);
        ViewInteraction enterColour = onView(withId(R.id.enter_colour));
        assertNotNull(enterColour);

        // Case check for null username
        saveButton.perform(click());
        Thread.sleep(2000);

        // Try to enter in username then press save
        enterUsername.perform(replaceText("Test Name"), closeSoftKeyboard());
        enterUsername.perform(pressImeActionButton());

        Thread.sleep(1000);
        saveButton.perform(click());
        Thread.sleep(2000);

        // Try to enter in genre then press save
        enterGenre.perform(replaceText("Love"), closeSoftKeyboard());
        enterGenre.perform(pressImeActionButton());

        Thread.sleep(1000);
        saveButton.perform(click());
        Thread.sleep(2000);

        // Check text fields values
        enterUsername.check(matches(withText("Test Name")));
        enterGenre.check(matches(withText("Love")));

        // Select a colour then press save
        enterColour.perform(click());

        Thread.sleep(1000);

        ViewInteraction okColourBtn = onView(withId(R.id.okColorButton));
        okColourBtn.perform(click());

        Thread.sleep(1000);
        saveButton.perform(click());
        Thread.sleep(1000);

        // Select songs
        addSongsBtn.perform(click());
        Thread.sleep(2000);

        ViewInteraction enterName = onView(withId(R.id.search_bar));
        enterName.perform(replaceText("Test"), closeSoftKeyboard());
        Thread.sleep(1000);

        ViewInteraction searchBtn = onView(withId(R.id.search_button));
        searchBtn.check(matches(isDisplayed()));
        searchBtn.perform(click());
        Thread.sleep(1000);

        ViewInteraction addSong1 = onView(allOf(withId(R.id.add_btn), withText("Add"),
                childAtPosition(childAtPosition(withId(R.id.song_list), 0), 3), isDisplayed()));
        addSong1.perform(click());
        Thread.sleep(1000);

        ViewInteraction addSong2 = onView(allOf(withId(R.id.add_btn), withText("Add"),
                childAtPosition(childAtPosition(withId(R.id.song_list), 1), 3), isDisplayed()));
        addSong2.perform(click());
        Thread.sleep(1000);

        ViewInteraction addSong3 = onView(allOf(withId(R.id.add_btn), withText("Add"),
                childAtPosition(childAtPosition(withId(R.id.song_list), 2), 3), isDisplayed()));
        addSong3.perform(click());
        Thread.sleep(1000);

        ViewInteraction addSong4 = onView(allOf(withId(R.id.add_btn), withText("Add"),
                childAtPosition(childAtPosition(withId(R.id.song_list), 3), 3), isDisplayed()));
        addSong4.perform(click());
        Thread.sleep(1000);

        ViewInteraction addSong5 = onView(allOf(withId(R.id.add_btn), withText("Add"),
                childAtPosition(childAtPosition(withId(R.id.song_list), 4), 3), isDisplayed()));
        addSong5.perform(click());
        Thread.sleep(1000);

        ViewInteraction appCompatImageButton = onView(allOf(withContentDescription("Navigate up"),
                childAtPosition(allOf(withId(R.id.action_bar), childAtPosition(withId(R.id.action_bar_container), 0)), 1), isDisplayed()));
        appCompatImageButton.perform(click());
        Thread.sleep(1000);

        // Press save to save account and get to main page
        saveButton.perform(click());
        Thread.sleep(3000);

        // Check an element from main page that it exits
        ViewInteraction profileBtn = onView(withId(R.id.profile_btn));
        profileBtn.check(matches(isDisplayed()));
    }

    @Test
    public void existingUserEditProfile() throws InterruptedException {
        Thread.sleep(5000);

        // Open up profile from main activity
        ViewInteraction profileButton = onView(withId(R.id.profile_btn));
        profileButton.check(matches(isDisplayed()));
        profileButton.perform(click());
        Thread.sleep(2000);

        // Profile activity opens up, check entries exist
        ViewInteraction saveButton = onView(withId(R.id.save_profile));
        assertNotNull(saveButton);
        ViewInteraction addSongsBtn = onView(withId(R.id.add_songs));
        assertNotNull(addSongsBtn);
        ViewInteraction enterUsername = onView(withId(R.id.enter_username));
        assertNotNull(enterUsername);
        ViewInteraction enterGenre = onView(withId(R.id.enter_fav_genre));
        assertNotNull(enterGenre);
        ViewInteraction enterColour = onView(withId(R.id.enter_colour));
        assertNotNull(enterColour);

        // Enter new field for name and genre
        enterUsername.perform(replaceText("New Name"), closeSoftKeyboard());
        enterUsername.perform(pressImeActionButton());
        Thread.sleep(1000);
        enterGenre.perform(replaceText("Rock"), closeSoftKeyboard());
        enterGenre.perform(pressImeActionButton());
        Thread.sleep(1000);

        saveButton.perform(click());
        Thread.sleep(2000);

        // Go back into profile and assert the changes
        profileButton.perform(click());
        Thread.sleep(1000);
        enterUsername.check(matches(withText("New Name")));
        enterGenre.check(matches(withText("Rock")));

        Thread.sleep(1000);
    }

    @Test
    public void editSongs() throws InterruptedException {
        Thread.sleep(5000);

        // Open up profile from main activity
        ViewInteraction profileButton = onView(withId(R.id.profile_btn));
        profileButton.check(matches(isDisplayed()));
        profileButton.perform(click());
        Thread.sleep(2000);

        // Profile activity opens up, check entries exist
        ViewInteraction saveButton = onView(withId(R.id.save_profile));
        assertNotNull(saveButton);
        ViewInteraction addSongsBtn = onView(withId(R.id.add_songs));
        assertNotNull(addSongsBtn);
        ViewInteraction enterUsername = onView(withId(R.id.enter_username));
        assertNotNull(enterUsername);
        ViewInteraction enterGenre = onView(withId(R.id.enter_fav_genre));
        assertNotNull(enterGenre);
        ViewInteraction enterColour = onView(withId(R.id.enter_colour));
        assertNotNull(enterColour);

        // Delete 3 songs, then save which will do nothing
        ViewInteraction deleteSong1 = onView(allOf(withId(R.id.dlt_btn), withText("Delete"),
                childAtPosition(childAtPosition(withId(R.id.selectedSongs),0),3), isDisplayed()));
        deleteSong1.perform(click());
        Thread.sleep(2000);
        ViewInteraction deleteSong2 = onView(allOf(withId(R.id.dlt_btn), withText("Delete"),
                childAtPosition(childAtPosition(withId(R.id.selectedSongs),0),3), isDisplayed()));
        deleteSong2.perform(click());
        Thread.sleep(2000);
        ViewInteraction deleteSong3 = onView(allOf(withId(R.id.dlt_btn), withText("Delete"),
                childAtPosition(childAtPosition(withId(R.id.selectedSongs),0),3), isDisplayed()));
        deleteSong3.perform(click());
        Thread.sleep(2000);

        saveButton.perform(click());
        Thread.sleep(1000);

        // Select songs
        addSongsBtn.perform(click());
        Thread.sleep(2000);

        ViewInteraction enterName = onView(withId(R.id.search_bar));
        enterName.perform(replaceText("Test"), closeSoftKeyboard());
        Thread.sleep(1000);

        ViewInteraction searchBtn = onView(withId(R.id.search_button));
        searchBtn.check(matches(isDisplayed()));
        searchBtn.perform(click());
        Thread.sleep(1000);

        ViewInteraction addSong1 = onView(allOf(withId(R.id.add_btn), withText("Add"),
                childAtPosition(childAtPosition(withId(R.id.song_list), 0), 3), isDisplayed()));
        addSong1.perform(click());
        Thread.sleep(1000);

        ViewInteraction addSong2 = onView(allOf(withId(R.id.add_btn), withText("Add"),
                childAtPosition(childAtPosition(withId(R.id.song_list), 1), 3), isDisplayed()));
        addSong2.perform(click());
        Thread.sleep(1000);

        ViewInteraction addSong3 = onView(allOf(withId(R.id.add_btn), withText("Add"),
                childAtPosition(childAtPosition(withId(R.id.song_list), 2), 3), isDisplayed()));
        addSong3.perform(click());
        Thread.sleep(1000);
        ViewInteraction appCompatImageButton = onView(allOf(withContentDescription("Navigate up"),
                childAtPosition(allOf(withId(R.id.action_bar), childAtPosition(withId(R.id.action_bar_container), 0)), 1), isDisplayed()));
        appCompatImageButton.perform(click());
        Thread.sleep(1000);

        // Save changes onto account
        saveButton.perform(click());
        Thread.sleep(1000);
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
