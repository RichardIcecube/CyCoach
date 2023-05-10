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

import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myexperiments.MainActivity;
import com.example.myexperiments.R;
import com.example.myexperiments.Register;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;

public class RegisterTest {
    private FragmentScenario<Register> registerScenario;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void signInButton_displaySnackBarForEmptyCredentials() {
        Bundle bundle = new Bundle();
        bundle.putString("title", "Register");
        registerScenario = FragmentScenario.launchInContainer(Register.class, bundle, R.style.TestTheme);
        onView(withId(R.id.registerbutton)).perform(click());
        onView(withText("Please enter proper User information")).check(matches(isDisplayed()));
    }

    @Test
    public void updateButton_displaySnackBarForEmptyCredentials() throws JSONException {
        JSONObject mockAthlete = new JSONObject();
        mockAthlete.put("firstName", "George");
        mockAthlete.put("lastName", "Washington");
        mockAthlete.put("id", 3);
        mockAthlete.put("classType", 1);
        Bundle bundle = new Bundle();
        bundle.putString("currUser", String.valueOf(mockAthlete));
        bundle.putString("title", "Edit Profile");
        registerScenario = FragmentScenario.launchInContainer(Register.class, bundle, R.style.TestTheme);
        onView(withId(R.id.registerbutton)).perform(click());
        onView(withText("Please enter proper User information")).check(matches(isDisplayed()));
    }

    @Test
    public void registerWithFakeCredentials_navigatesToSignIn() {
        Bundle bundle = new Bundle();
        bundle.putString("title", "Register");
        registerScenario = FragmentScenario.launchInContainer(Register.class, bundle, R.style.TestTheme);
        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.Register);
        });

        registerScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        onView(allOf(isAssignableFrom(TextInputEditText.class), isDescendantOfA(withId(R.id.firstname)))).perform(replaceText("test"));
        onView(allOf(isAssignableFrom(TextInputEditText.class), isDescendantOfA(withId(R.id.lastname)))).perform(replaceText("test"));
        onView(allOf(isAssignableFrom(TextInputEditText.class), isDescendantOfA(withId(R.id.email)))).perform(replaceText("test"));
        onView(allOf(isAssignableFrom(TextInputEditText.class), isDescendantOfA(withId(R.id.register_password)))).perform(replaceText("test"));
        onView(withId(R.id.checkbox)).perform(click());
        onView(withId(R.id.registerbutton)).perform(click());

        onView(withText("User Created Successfully!")).check(matches(isDisplayed()));
        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.SignIn);
    }

    @Test
    public void editProfileWithFakeCredentials_navigatesToUserProfile() throws JSONException {
        JSONObject mockAthlete = new JSONObject();
        mockAthlete.put("firstName", "George");
        mockAthlete.put("lastName", "Washington");
        mockAthlete.put("id", 3);
        mockAthlete.put("classType", 1);
        Bundle bundle = new Bundle();
        bundle.putString("currUser", String.valueOf(mockAthlete));
        bundle.putString("title", "Edit Profile");
        registerScenario = FragmentScenario.launchInContainer(Register.class, bundle, R.style.TestTheme);
        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.Register);
        });

        registerScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        onView(allOf(isAssignableFrom(TextInputEditText.class), isDescendantOfA(withId(R.id.firstname)))).perform(replaceText("test"));
        onView(allOf(isAssignableFrom(TextInputEditText.class), isDescendantOfA(withId(R.id.lastname)))).perform(replaceText("test"));
        onView(allOf(isAssignableFrom(TextInputEditText.class), isDescendantOfA(withId(R.id.email)))).perform(replaceText("test"));
        onView(allOf(isAssignableFrom(TextInputEditText.class), isDescendantOfA(withId(R.id.register_password)))).perform(replaceText("test"));
        onView(withId(R.id.registerbutton)).perform(click());

        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.UserProfile);
    }
}
