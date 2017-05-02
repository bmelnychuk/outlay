package app.outlay.example;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import app.outlay.R;
import app.outlay.view.activity.LoginActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Espresso test, which will execute on an Android device, and accesses the UI.
 * <p>
 * Cheat sheet for possible actions here
 * https://google.github.io/android-testing-support-library/docs/espresso/cheatsheet/
 */
@RunWith(AndroidJUnit4.class)
public class ExampleEspressoTest {

    // Starts the specified activity before the test cases run
    @Rule
    public final ActivityTestRule<LoginActivity> main = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void shouldHaveAppName() {
        onView(withText("Outlay")).check(matches(isDisplayed()));
    }

    @Test
    public void shouldHaveLoginButton() {
        onView(withText("Login")).check(matches(isDisplayed()));
    }

    @Test
    public void loginWithEmptyFields_shouldDisplayErrors() {
        onView(withText("Login")).perform(click());

        onView(withText(R.string.error_signin_invalid_email)).check(matches(isDisplayed()));
        onView(withText(R.string.error_signin_invalid_password)).check(matches(isDisplayed()));
    }

}
