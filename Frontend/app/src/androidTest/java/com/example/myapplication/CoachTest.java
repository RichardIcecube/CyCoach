package com.example.myapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.assertj.core.api.Assertions.assertThat;

import android.os.Bundle;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myexperiments.Coach;
import com.example.myexperiments.Dashboard;
import com.example.myexperiments.MainActivity;
import com.example.myexperiments.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;



public class CoachTest {

    private static final String mockCoach = "{\"classType\": \"2\"}";

    public JSONObject createMockCoach() throws JSONException {
        JSONObject mockCoach1 = new JSONObject();
        mockCoach1.put("firstName", "Jack");
        mockCoach1.put("lastName", "Smith");
        mockCoach1.put("id", 2);
        mockCoach1.put("classType", 2);
        return mockCoach1;
    }

    private FragmentScenario<Coach> coachScenario;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void workoutEditerButton_navigatesToWorkoutEditer() {
        //setup
        Bundle bundle = new Bundle();
        bundle.putString("currUser", mockCoach);
        coachScenario = FragmentScenario.launchInContainer(Coach.class, bundle, R.style.TestTheme);

        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.Coach);
        });

        coachScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        onView(withId(R.id.addWorkoutCoach)).perform(click());
        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.AddWorkout);
    }

    @Test
    public void viewButton_navigatesToWorkouts() throws JSONException {
        //setup
        Bundle bundle = new Bundle();
        bundle.putString("currUser", String.valueOf(createMockCoach()));
        coachScenario = FragmentScenario.launchInContainer(Coach.class, bundle, R.style.TestTheme);

        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.Coach);
        });

        coachScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        int viewButtonBaseId = 100000; // Use the same predefined base ID for view buttons
        int viewButtonId = viewButtonBaseId + (0 * 2); //index = 0
        Helpers.waitForView(viewButtonId, 7000);
        onView(withId(viewButtonId)).perform(click());
        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.Workouts);
    }

    @Test
    public void viewButton1_navigatesToWorkouts() throws JSONException {
        //setup
        Bundle bundle = new Bundle();
        bundle.putString("currUser", String.valueOf(createMockCoach()));
        coachScenario = FragmentScenario.launchInContainer(Coach.class, bundle, R.style.TestTheme);

        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.Coach);
        });

        coachScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        int viewButtonBaseId = 100000; // Use the same predefined base ID for view buttons
        int viewButtonId = viewButtonBaseId + (1 * 2); //index = 1
        Helpers.waitForView(viewButtonId, 11000);
        onView(withId(viewButtonId)).perform(click());
        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.Workouts);
    }

    @Test
    public void modifyButton_navigatesToWorkoutEditer() throws JSONException {
        //setup
        Bundle bundle = new Bundle();
        bundle.putString("currUser", String.valueOf(createMockCoach()));
        coachScenario = FragmentScenario.launchInContainer(Coach.class, bundle, R.style.TestTheme);

        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.Coach);
        });

        coachScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        int modifyButtonBaseId = 200000; // Use the same predefined base ID for view buttons
        int modifyButtonId = modifyButtonBaseId + (0 * 2) + 1; //index = 0
        Helpers.waitForView(modifyButtonId, 9000);
        onView(withId(modifyButtonId)).perform(click());
        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.AddWorkout);
    }

    @Test
    public void modifyButton1_navigatesToWorkoutEditer() throws JSONException {
        //setup
        Bundle bundle = new Bundle();
        bundle.putString("currUser", String.valueOf(createMockCoach()));
        coachScenario = FragmentScenario.launchInContainer(Coach.class, bundle, R.style.TestTheme);

        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.Coach);
        });

        coachScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        int modifyButtonBaseId = 200000; // Use the same predefined base ID for view buttons
        int modifyButtonId = modifyButtonBaseId + (1 * 2) + 1; //index = 1
        Helpers.waitForView(modifyButtonId, 9000);
        onView(withId(modifyButtonId)).perform(click());
        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.AddWorkout);
    }
}
