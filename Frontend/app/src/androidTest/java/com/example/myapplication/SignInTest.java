package com.example.myapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.allOf;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myexperiments.MainActivity;
import com.example.myexperiments.R;
import com.example.myexperiments.SignIn;
import com.google.android.material.textfield.TextInputEditText;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SignInTest {

    private FragmentScenario<SignIn> signInScenario;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        signInScenario = FragmentScenario.launchInContainer(SignIn.class, null, R.style.TestTheme);
    }

    @Test
    public void signInButton_displaySnackBarForEmptyCredentials() {
        onView(withId(R.id.button)).perform(click());
        onView(withText("Please enter proper Username and Password")).check(matches(isDisplayed()));
    }

    @Test
    public void signInWithFakeCredentials_navigatesToDashboard() {
        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.SignIn);
        });

        signInScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        onView(allOf(isAssignableFrom(TextInputEditText.class), isDescendantOfA(withId(R.id.usernameText)))).perform(replaceText("jacksmith"));
        onView(allOf(isAssignableFrom(TextInputEditText.class), isDescendantOfA(withId(R.id.passwordText)))).perform(replaceText("baseball123"));
        onView(withId(R.id.button)).perform(click());

        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.Dashboard);
    }

    @Test
    public void registerButton_navigatesToRegisterFragment() {
        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.SignIn);
        });

        signInScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        onView(withId(R.id.registerbutton)).perform(click());
        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.Register);
    }
}