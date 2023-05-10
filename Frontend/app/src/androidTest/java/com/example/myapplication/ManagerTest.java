package com.example.myapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myexperiments.MainActivity;
import com.example.myexperiments.Manager;
import com.example.myexperiments.R;
import com.example.myexperiments.Register;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;

public class ManagerTest {

    public JSONObject createMockManager() throws JSONException {
        JSONObject mockManager = new JSONObject();
        mockManager.put("firstName", "Zane");
        mockManager.put("lastName", "Eason");
        mockManager.put("id", 1);
        mockManager.put("classType", 3);
        return mockManager;
    }
    private FragmentScenario<Manager> managerScenario;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void checksAllButtonsDisplay() throws JSONException {
        Bundle bundle = new Bundle();
        bundle.putString("currUser", createMockManager().toString());
        managerScenario = FragmentScenario.launchInContainer(Manager.class, bundle, R.style.TestTheme);
        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.Manager);
        });

        int viewButtonBaseId = 100000;
        int viewButtonBaseId1 = 100001;
        Helpers.waitForView(viewButtonBaseId, 15000);
        onView(withId(viewButtonBaseId)).check(matches(isDisplayed()));
        onView(withId(R.id.coachLinkButton)).check(matches(isDisplayed()));
        onView(withId(R.id.linkScreenButton)).check(matches(isDisplayed()));
    }
}
