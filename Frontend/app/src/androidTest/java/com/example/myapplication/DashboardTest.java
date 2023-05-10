package com.example.myapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.assertj.core.api.Assertions.assertThat;

import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myexperiments.Dashboard;
import com.example.myexperiments.MainActivity;
import com.example.myexperiments.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DashboardTest {

    private static final String mockAthlete = "{\"classType\": \"1\"}";
    private static final String mockCoach = "{\"classType\": \"2\"}";

    private static final String mockManager = "{\"classType\": \"3\"}";

    private FragmentScenario<Dashboard> dashboardScenario;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void editProfileButton_navigatesToUserProfile() {
        //setup user
        Bundle bundle = new Bundle();
        bundle.putString("currUser", mockAthlete);
        dashboardScenario = FragmentScenario.launchInContainer(Dashboard.class, bundle, R.style.TestTheme);

        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.Dashboard);
        });

        dashboardScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        onView(withId(R.id.edit_profile_panel)).perform(click());
        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.UserProfile);
    }

    @Test
    public void workoutButton_navigatesToWorkouts() {
        //setup
        Bundle bundle = new Bundle();
        bundle.putString("currUser", mockAthlete);
        dashboardScenario = FragmentScenario.launchInContainer(Dashboard.class, bundle, R.style.TestTheme);

        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.Dashboard);
        });

        dashboardScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        onView(withId(R.id.workout_button)).perform(click());
        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.Workouts);
    }

    @Test
    public void coachButton_navigatesToCoaches() {
        //setup
        Bundle bundle = new Bundle();
        bundle.putString("currUser", mockCoach);
        dashboardScenario = FragmentScenario.launchInContainer(Dashboard.class, bundle, R.style.TestTheme);

        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.Dashboard);
        });

        dashboardScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        onView(withId(R.id.coach_button)).perform(click());
        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.Coach);
    }

    @Test
    public void managerButton_navigatesToManagerPage() {
        //setup
        Bundle bundle = new Bundle();
        bundle.putString("currUser", mockManager);
        dashboardScenario = FragmentScenario.launchInContainer(Dashboard.class, bundle, R.style.TestTheme);

        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.Dashboard);
        });

        dashboardScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        onView(withId(R.id.workout_button)).perform(click());
        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.Manager);
    }
}
