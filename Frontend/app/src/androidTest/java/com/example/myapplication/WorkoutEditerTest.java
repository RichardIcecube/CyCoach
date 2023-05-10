package com.example.myapplication;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.hasToString;

import android.os.Bundle;
import android.os.SystemClock;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myexperiments.MainActivity;
import com.example.myexperiments.R;
import com.example.myexperiments.WorkoutEditer;
import com.google.android.material.textfield.TextInputEditText;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WorkoutEditerTest {

    private static final String mockCoach = "{\"classType\": \"2\"}";
    private FragmentScenario<WorkoutEditer> workoutEditerScenario;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void addWorkoutButton_toDisplayTextWhenNoInput() {
        //setup
        Bundle bundle = new Bundle();
        bundle.putString("currUser", mockCoach);
        workoutEditerScenario = FragmentScenario.launchInContainer(WorkoutEditer.class, bundle, R.style.TestTheme);

        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.AddWorkout);
        });

        workoutEditerScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        onView(withId(R.id.addWorkoutButton)).perform(click());
        onView(withText("Please enter proper Workout information")).check(matches(isDisplayed()));
    }

    @Test
    public void addWorkoutButton_navigatesToDashboard() {
        //setup
        Bundle bundle = new Bundle();
        bundle.putString("currUser", mockCoach);
        workoutEditerScenario = FragmentScenario.launchInContainer(WorkoutEditer.class, bundle, R.style.TestTheme);

        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.AddWorkout);
        });

        Helpers.waitForView(R.id.sets, 11000);

        onView(allOf(isAssignableFrom(TextInputEditText.class), isDescendantOfA(withId(R.id.sets))))
                .perform(ViewActions.replaceText("3"));
        onView(allOf(isAssignableFrom(TextInputEditText.class), isDescendantOfA(withId(R.id.urlinput))))
                .perform(ViewActions.replaceText("https://www.example.com"));
        onView(allOf(isAssignableFrom(TextInputEditText.class), isDescendantOfA(withId(R.id.reps))))
                .perform(ViewActions.replaceText("10"));
        onView(allOf(isAssignableFrom(TextInputEditText.class), isDescendantOfA(withId(R.id.duration))))
                .perform(ViewActions.replaceText("60"));
        onView(allOf(isAssignableFrom(TextInputEditText.class), isDescendantOfA(withId(R.id.rest))))
                .perform(ViewActions.replaceText("120"));

        // Select an item from the MaterialSpinner
        onView(withId(R.id.exerciseSpinner)).perform(click());
        onData(anything()).atPosition(1).perform(click());

        workoutEditerScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        onView(withId(R.id.addWorkoutButton)).perform(click());
        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.Dashboard);
    }

    @Test
    public void addExerciseButton_ExerciseAlreadyExists() {
        //setup
        Bundle bundle = new Bundle();
        bundle.putString("currUser", mockCoach);
        workoutEditerScenario = FragmentScenario.launchInContainer(WorkoutEditer.class, bundle, R.style.TestTheme);

        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.AddWorkout);
        });

        // Select an item from the MaterialSpinner
        onView(withId(R.id.addExercise)).perform(click());



        onView(allOf(isAssignableFrom(TextInputEditText.class), isDescendantOfA(withId(R.id.exercise))))
                .perform(ViewActions.replaceText("test"));

        workoutEditerScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        onView(withId(R.id.addWorkoutButton)).perform(click());
        onView(withText("Exercise Created Successfully!")).check(matches(isDisplayed()));
    }

    @Test
    public void removeExerciseButton_navigatesToDashboard() {
        //setup
        Bundle bundle = new Bundle();
        bundle.putString("currUser", mockCoach);
        workoutEditerScenario = FragmentScenario.launchInContainer(WorkoutEditer.class, bundle, R.style.TestTheme);

        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.AddWorkout);
        });

        Helpers.waitForView(R.id.removeExercise, 11000);

        // Select an item from the MaterialSpinner
        onView(withId(R.id.removeExercise)).perform(click());
        onView(withId(R.id.exerciseSpinner)).perform(click());
        onData(hasToString(startsWith("test")))
                .inAdapterView(withId(R.id.exerciseSpinner))
                .perform(click());

        workoutEditerScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        onView(withId(R.id.addWorkoutButton)).perform(click());
        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.Dashboard);
    }
}
