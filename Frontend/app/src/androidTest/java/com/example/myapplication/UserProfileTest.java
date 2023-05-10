package com.example.myapplication;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.anything;

import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myexperiments.MainActivity;
import com.example.myexperiments.R;
import com.example.myexperiments.UserProfile;
import com.example.myexperiments.Videos;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;

public class UserProfileTest {

    private FragmentScenario<UserProfile> userProfileScenario;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void rendersUserProfile_NavigatesToRegister() throws JSONException {
        JSONObject mockAthlete = new JSONObject();
        mockAthlete.put("firstName", "George");
        mockAthlete.put("lastName", "Washington");
        mockAthlete.put("id", 3);
        mockAthlete.put("classType", 1);
        Bundle bundle = new Bundle();
        bundle.putString("currUser", String.valueOf(mockAthlete));
        userProfileScenario = FragmentScenario.launchInContainer(UserProfile.class, bundle, R.style.TestTheme);

        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.UserProfile);
        });

        userProfileScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        onView(withId(R.id.editProfileButton)).perform(click());
        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.Register);
    }
}
