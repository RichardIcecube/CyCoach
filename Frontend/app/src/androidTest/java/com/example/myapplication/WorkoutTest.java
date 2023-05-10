package com.example.myapplication;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matcher;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.example.myexperiments.BR.hasWorkouts;

import static org.assertj.core.api.Assertions.assertThat;

import android.os.Bundle;
import android.view.View;

import com.example.myexperiments.MainActivity;
import com.example.myapplication.Helpers;
import com.example.myexperiments.R;
import com.example.myexperiments.Workout;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class WorkoutTest {

    private FragmentScenario<Workout> workoutScenario;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    public JSONArray createMockWorkouts() throws JSONException {

        JSONObject mockExercise = new JSONObject();
        mockExercise.put("id", 37);
        mockExercise.put("exerciseName", "Deadlift");

        JSONObject mockWorkout = new JSONObject();
        mockWorkout.put("id", 48);
        mockWorkout.put("sets", 4);
        mockWorkout.put("reps", 12);
        mockWorkout.put("duration", "2 min");
        mockWorkout.put("rest", "30 sec");
        mockWorkout.put("video", null);
        mockWorkout.put("exercise", mockExercise);

        JSONArray mockWorkoutList = new JSONArray();
        mockWorkoutList.put(mockWorkout);

        return mockWorkoutList;
    }

    @Test
    public void displayWorkoutList_workoutListIsNotVisible() {
        Bundle bundle = new Bundle();
        String mockAthlete = "{\"classType\": \"1\"}";
        bundle.putString("currUser", mockAthlete);
        workoutScenario = FragmentScenario.launchInContainer(Workout.class, bundle, R.style.TestTheme);
        // Check if the tableLayout containing the workout list is visible
        onView(withId(R.id.no_workouts_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.firstNameTextView)).check(matches(withText("null null's")));
    }

    @Test
    public void displayWorkoutList_workoutListIsVisible() throws JSONException {
        JSONObject mockAthlete = new JSONObject();
        mockAthlete.put("firstName", "John");
        mockAthlete.put("lastName", "Adams");
        mockAthlete.put("id", 4);
        mockAthlete.put("classType", 1);
        mockAthlete.put("workoutList", createMockWorkouts());
        Bundle bundle = new Bundle();
        bundle.putString("currUser", String.valueOf(mockAthlete));
        bundle.putString("workouts", createMockWorkouts().toString());
        workoutScenario = FragmentScenario.launchInContainer(Workout.class, bundle, R.style.TestTheme);
        onView(withId(R.id.firstNameTextView)).check(matches(withText("John Adams's")));
        Helpers.waitForView(R.id.table_layout, 7000);
        onView(withId(R.id.table_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void videoNavButton_isDisplayed() throws JSONException {
        JSONObject mockAthlete = new JSONObject();
        mockAthlete.put("firstName", "John");
        mockAthlete.put("lastName", "Adams");
        mockAthlete.put("id", 4);
        mockAthlete.put("classType", 1);
        Bundle bundle = new Bundle();
        bundle.putString("currUser", String.valueOf(mockAthlete));
        bundle.putString("workouts", createMockWorkouts().toString());
        workoutScenario = FragmentScenario.launchInContainer(Workout.class, bundle, R.style.TestTheme);

        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.Workouts);
        });

        workoutScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));
        Helpers.waitForView(R.id.videoNavButton, 11000);
        onView(withId(R.id.videoNavButton)).check(matches(isDisplayed()));
    }

}