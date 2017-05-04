package app.outlay.view.activity;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import app.outlay.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddExpenseTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void addExpenseTest() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.signInEmail), isDisplayed()));
        appCompatEditText.perform(replaceText("iettester999@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.signInPassword), isDisplayed()));
        appCompatEditText2.perform(replaceText("iet20171"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.signInPassword), withText("iet20171"), isDisplayed()));
        appCompatEditText3.perform(pressImeActionButton());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.signInButton), withText("Login"),
                        withParent(withId(R.id.signInForm)),
                        isDisplayed()));
        appCompatButton.perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction numpadButton = onView(
                allOf(withId(R.id.btn4), isDisplayed()));
        numpadButton.perform(click());

        ViewInteraction numpadButton2 = onView(
                allOf(withId(R.id.btn5), isDisplayed()));
        numpadButton2.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.categoryIcon),
                        withParent(allOf(withId(R.id.categoryContainer),
                                withParent(withId(R.id.categoriesGrid)))),
                        isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(R.id.chartIcon), isDisplayed()));
        appCompatImageView2.perform(click());

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_list), withContentDescription("Expenses List"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction relativeLayout = onView(
                allOf(withId(R.id.container),
                        childAtPosition(
                                allOf(withId(R.id.recyclerView),
                                        childAtPosition(
                                                withId(R.id.coordinatorLayout),
                                                1)),
                                0),
                        isDisplayed()));
        relativeLayout.check(matches(isDisplayed()));

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Felfelé mozgatás"),
                        withParent(allOf(withId(R.id.toolbar),
                                withParent(withId(R.id.appBarLayout)))),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Felfelé mozgatás"),
                        withParent(allOf(withId(R.id.toolbar),
                                withParent(withId(R.id.appBarLayout)))),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatImageView3 = onView(
                allOf(withId(R.id.drawerIcon), isDisplayed()));
        appCompatImageView3.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.material_drawer_recycler_view),
                        withParent(allOf(withId(R.id.material_drawer_slider_layout),
                                withParent(withId(R.id.material_drawer_layout)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(4, click()));

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
