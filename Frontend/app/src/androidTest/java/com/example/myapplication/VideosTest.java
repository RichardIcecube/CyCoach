package com.example.myapplication;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.anything;

import android.os.Bundle;
import android.provider.MediaStore;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myexperiments.Link;
import com.example.myexperiments.MainActivity;
import com.example.myexperiments.R;
import com.example.myexperiments.Videos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;

public class VideosTest {

    private FragmentScenario<Videos> videoScenario;

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
    public void rendersVideoScreen_NoDisplay() throws JSONException {
        JSONObject mockAthlete = new JSONObject();
        mockAthlete.put("firstName", "George");
        mockAthlete.put("lastName", "Washington");
        mockAthlete.put("id", 3);
        mockAthlete.put("classType", 1);
        Bundle bundle = new Bundle();
        bundle.putString("currUser", String.valueOf(mockAthlete));
        bundle.putString("workouts", createMockWorkouts().toString());
        videoScenario = FragmentScenario.launchInContainer(Videos.class, bundle, R.style.TestTheme);

        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.Videos);
        });

        videoScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        onView(withId(R.id.workoutDropdown)).perform(click());
        onData(anything()).inRoot(isPlatformPopup()).atPosition(0).perform(click());
        onView(withId(R.id.errorTextView)).check(matches(withText("Sorry, that workout does not have an associated video!")));
    }
}
