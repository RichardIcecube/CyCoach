package com.example.myexperiments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.myexperiments.databinding.WorkoutBinding;
import com.example.myexperiments.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class represents the Workout fragment, this class allows
 * a user to view their workouts or their athletes workouts.
 * @author Jayden Luse and Zane Eason
 */
public class Workout extends Fragment {

    public WorkoutBinding binding;

    private final String TAG = Workout.class.getSimpleName();

    String tag_json_workout_array = "workoutArr";

    private JSONObject currUser;

    private JSONObject athlete;

    private JSONObject coach;

    private final JSONArray[] workoutList = {null};

    /**
     * Creates the fragment's view.
     */
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = WorkoutBinding.inflate(inflater, container, false);

        initializeWorkoutFragment();


        binding.videoNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("currUser", currUser.toString());
                bundle.putString("workouts", workoutList[0].toString());
                NavHostFragment.findNavController(Workout.this).navigate(R.id.action_Workout_to_Videos, bundle);
            }
        });

        return binding.getRoot();

    }

    /**
     * Initializes the workout fragment.
     */
    private void initializeWorkoutFragment() {
        if (getArguments() != null) {
            try {
                // Retrieve currUser from the arguments
                currUser = new JSONObject(getArguments().getString("currUser"));
                if (currUser.getInt("classType") == 2){ //currUser is coach
                    coach = currUser;
                    athlete = new JSONObject(getArguments().getString("athlete"));
                }
                else{
                    if (currUser.getInt("classType") == 3){ //currUser is manager
                        coach = new JSONObject(getArguments().getString("coach"));
                        athlete = new JSONObject(getArguments().getString("athlete"));
                    }
                    else{ //currUser is athlete
                        athlete = currUser;
                    }
                }
                Log.d("Workout", "Current User: " + currUser);

                String firstName;
                String lastName;

                // Set the athlete's name

                if (athlete.has("firstName") && athlete.has("lastName")) {
                    firstName = athlete.getString("firstName");
                    lastName = athlete.getString("lastName");
                    fetchWorkouts(athlete, new Workout.VolleyCallbackArray() {
                        @Override
                        public void onSuccess(JSONArray result) throws JSONException {
                            workoutList[0] = result;
                            updateWorkoutListUI(workoutList[0]);
                        }
                    });
                } else {
                    firstName = athlete.getJSONObject("user").getString("firstName");
                    lastName = athlete.getJSONObject("user").getString("lastName");
                    workoutList[0] = athlete.getJSONArray("workoutList");
                    updateWorkoutListUI(workoutList[0]);
                }
                binding.setFirstName(firstName);
                binding.setLastName(lastName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the UI with the workout list data.
     *
     * @param workoutList JSONArray containing the workout data.
     * @throws JSONException if there is an error parsing the workout list data.
     */
    private void updateWorkoutListUI(JSONArray workoutList) throws JSONException {
        if (workoutList.length() > 0) {
            binding.setHasWorkouts(true);
            displayWorkoutList(workoutList);
        } else {
            binding.setHasWorkouts(false);
        }
    }

    /**
     * Displays the workout list in the table layout.
     *
     * @param workoutList JSONArray containing the workout data.
     * @throws JSONException if there is an error parsing the workout list data.
     */
    private void displayWorkoutList(JSONArray workoutList) throws JSONException {
        TableLayout tableLayout = binding.tableLayout;
        for (int i = 0; i < workoutList.length(); i++) {
            JSONObject workout = workoutList.getJSONObject(i);

            TableRow row = createTableRowForWorkout(workout);
            tableLayout.addView(row);
        }
    }

    /**
     * Creates a table row for the given workout.
     *
     * @param workout JSONObject containing the workout data.
     * @return TableRow containing the workout data.
     * @throws JSONException if there is an error parsing the workout data.
     */
    private TableRow createTableRowForWorkout(JSONObject workout) throws JSONException {
        TableRow row = new TableRow(getContext());

        TextView workoutNameView = createWorkoutNameView(workout);
        TextView setsView = createSetsView(workout);
        TextView repsView = createRepsView(workout);
        TextView durationView = createDurationView(workout);
        TextView restView = createRestView(workout);

        row.addView(workoutNameView);
        row.addView(setsView);
        row.addView(repsView);
        row.addView(durationView);
        row.addView(restView);

        return row;
    }

    public interface VolleyCallbackArray {
        void onSuccess(JSONArray result) throws JSONException;
    }

    /**
     * Fetches the workouts for the given user.
     *
     * @param passedCurrUser JSONObject containing the user data.
     * @param callback Callback for handling the fetched workout data.
     * @throws JSONException if there is an error parsing the user data.
     */
    private void fetchWorkouts(JSONObject passedCurrUser, Workout.VolleyCallbackArray callback) throws JSONException {
        String url = Const.URL_JSON_GET_ATHLETES_WORKOUTS.replace("{id}", passedCurrUser.get("id").toString());
        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });
        MainActivity.getInstance().addToRequestQueue(jsonArrayReq, tag_json_workout_array);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Helper methods to create workout property TextViews

    /**
     * Creates a TextView for displaying the workout name.
     *
     * @param workout JSONObject containing the workout data.
     * @return TextView displaying the workout name.
     * @throws JSONException if there is an error parsing the workout data.
     */
    private TextView createWorkoutNameView(JSONObject workout) throws JSONException {
        TextView workoutNameView = new TextView(getContext());
        workoutNameView.setText(workout.getJSONObject("exercise").getString("exerciseName"));
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.latobold);
        workoutNameView.setTypeface(typeface);
        workoutNameView.setPadding(0, 25, 0, 0);
        workoutNameView.setGravity(Gravity.RIGHT);
        workoutNameView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        return workoutNameView;
    }

    /**
     * Creates a TextView for displaying the workout sets.
     *
     * @param workout JSONObject containing the workout data.
     * @return TextView displaying the workout sets.
     * @throws JSONException if there is an error parsing the workout data.
     */
    private TextView createSetsView(JSONObject workout) throws JSONException {
        TextView setsView = new TextView(getContext());
        setsView.setText(String.valueOf(workout.getInt("sets")));
        setsView.setGravity(Gravity.RIGHT);
        setsView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        return setsView;
    }

    /**
     * Creates a TextView for displaying the workout reps.
     *
     * @param workout JSONObject containing the workout data.
     * @return TextView displaying the workout reps.
     * @throws JSONException if there is an error parsing the workout data.
     */
    private TextView createRepsView(JSONObject workout) throws JSONException {
        TextView repsView = new TextView(getContext());
        repsView.setText(String.valueOf(workout.getInt("reps")));
        repsView.setPadding(0, 0, 40, 0);
        repsView.setGravity(Gravity.RIGHT);
        repsView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        return repsView;
    }

    /**
     * Creates a TextView for displaying the workout duration.
     *
     * @param workout JSONObject containing the workout data.
     * @return TextView displaying the workout duration.
     * @throws JSONException if there is an error parsing the workout data.
     */
    private TextView createDurationView(JSONObject workout) throws JSONException {
        TextView durationView = new TextView(getContext());
        durationView.setText(workout.getString("duration"));
        durationView.setGravity(Gravity.RIGHT);
        durationView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        return durationView;
    }

    /**
     * Creates a TextView for displaying the workout rest time.
     *
     * @param workout JSONObject containing the workout data.
     * @return TextView displaying the workout rest time.
     * @throws JSONException if there is an error parsing the workout data.
     */
    private TextView createRestView(JSONObject workout) throws JSONException {
        TextView restView = new TextView(getContext());
        restView.setText(workout.getString("rest"));
        restView.setPadding(0, 0, 10, 0);
        restView.setGravity(Gravity.CENTER);
        restView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        return restView;
    }
}


