package com.example.myapplication;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.anything;

import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myexperiments.CoachLink;
import com.example.myexperiments.Link;
import com.example.myexperiments.MainActivity;
import com.example.myexperiments.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;

public class LinkTest {

    public JSONObject createMockManager() throws JSONException {
        JSONObject mockManager = new JSONObject();
        mockManager.put("firstName", "Zane");
        mockManager.put("lastName", "Eason");
        mockManager.put("id", 1);
        mockManager.put("classType", 3);
        return mockManager;
    }

    public JSONArray createMockAthletes() throws JSONException {
        JSONObject mockAthlete1User = new JSONObject();
        mockAthlete1User.put("firstName", "George");
        mockAthlete1User.put("lastName", "Washington");

        JSONObject mockAthlete1 = new JSONObject();
        mockAthlete1.put("user", mockAthlete1User);
        mockAthlete1.put("id", 3);
        mockAthlete1.put("classType", 1);

        JSONObject mockAthlete2User = new JSONObject();
        mockAthlete2User.put("firstName", "John");
        mockAthlete2User.put("lastName", "Adams");

        JSONObject mockAthlete2 = new JSONObject();
        mockAthlete2.put("user", mockAthlete2User);
        mockAthlete2.put("id", 4);
        mockAthlete2.put("classType", 1);

        JSONArray mockAthletes = new JSONArray();
        mockAthletes.put(mockAthlete1);
        mockAthletes.put(mockAthlete2);

        return mockAthletes;
    }

    public JSONArray createMockCoaches() throws JSONException {
        JSONObject mockCoach1User = new JSONObject();
        mockCoach1User.put("firstName", "Jack");
        mockCoach1User.put("lastName", "Smith");

        JSONObject mockCoach1 = new JSONObject();
        mockCoach1.put("user", mockCoach1User);
        mockCoach1.put("id", 2);
        mockCoach1.put("classType", 2);

        JSONObject mockCoach2User = new JSONObject();
        mockCoach2User.put("firstName", "James");
        mockCoach2User.put("lastName", "Monroe");

        JSONObject mockCoach2 = new JSONObject();
        mockCoach2.put("user", mockCoach2User);
        mockCoach2.put("id", 8);
        mockCoach2.put("classType", 2);

        JSONArray mockCoaches = new JSONArray();
        mockCoaches.put(mockCoach1);
        mockCoaches.put(mockCoach2);

        return mockCoaches;
    }

    private FragmentScenario<Link> linkScenario;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void simulatesLink_NavigatesToManagerScreen() throws JSONException {
        //setup
        Bundle bundle = new Bundle();
        bundle.putString("currUser", createMockManager().toString());
        bundle.putString("coaches", createMockCoaches().toString());
        bundle.putString("athletes", createMockAthletes().toString());
        linkScenario = FragmentScenario.launchInContainer(Link.class, bundle, R.style.TestTheme);

        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.Link);
        });

        linkScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        onView(withId(R.id.coachDropdown)).perform(click());
        onData(anything()).inRoot(isPlatformPopup()).atPosition(0).perform(click());

        onView(withId(R.id.athleteDropdown)).perform(click());
        onData(anything()).inRoot(isPlatformPopup()).atPosition(0).perform(click());
        onView(withId(R.id.linkButton)).perform(click());
        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.Manager);
    }
}
