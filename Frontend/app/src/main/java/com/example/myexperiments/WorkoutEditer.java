package com.example.myexperiments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myexperiments.databinding.AddRemoveWorkoutBinding;
import com.example.myexperiments.utils.Const;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * This class that allows users to edit their workout routine by
 * adding, editing or removing exercises and workouts.
 * @author Jayden Luse and Zane Eason
 */
public class WorkoutEditer extends Fragment {
    private AddRemoveWorkoutBinding binding;
    private String exercise;
    private int selectedAthleteId = -1;
    int coachId;
    private boolean exerciseExists;
    private JSONObject currUser;
    private JSONObject coach;
    private JSONObject athlete;
    private int selectedExerciseID;

    private int selectedWorkoutId = -1;
    JSONObject selectedExerciseObj = null;
    String tag_json_workout_array;
    private final String TAG = WorkoutEditer.class.getSimpleName();

    private int sets;
    private int reps;
    private String duration;
    private String rest;
    private String url;

    /**
     * A nested class representing an Exercise.
     */
    private static class Exercise {
        String exerciseName;

        /**
         * Constructor for an Exercise.
         *
         * @param exerciseName The name of the exercise.
         */
        public Exercise(String exerciseName) {
            this.exerciseName = exerciseName;
        }

        /**
         * Convert Exercise object to a JSONObject.
         *
         * @return A JSONObject representing the Exercise object.
         */
        public JSONObject toJson() {
            JSONObject json = new JSONObject();
            try {
                json.put("exerciseName", exerciseName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json;
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = AddRemoveWorkoutBinding.inflate(inflater, container, false);
        getCurrentUserFromArguments();

        binding.addWorkoutButton.setOnClickListener(v -> {
            if (binding.addExercise.isChecked()) {
                handleAddExercise(v);
            } else if (binding.addWorkout.isChecked()) {
                if (athlete != null && athlete.has("user")) {
                    try {
                        handleAssignWorkoutToAthlete(v);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                else{
                    handleAddWorkout(v);
                }
            } else if (binding.removeExercise.isChecked()) {
                if (athlete != null && athlete.has("user")) {
                    try {
                        handleRemoveWorkoutFromAthlete(v);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                else{
                    handleRemoveExercise(v);
                }
            }
        });

        return binding.getRoot();
    }

    /**
     * Retrieves the current user's data from the passed arguments.
     */
    private void getCurrentUserFromArguments() {
        if (getArguments() != null) {
            try {
                // Retrieve currUser from the arguments
                currUser = new JSONObject(getArguments().getString("currUser"));
                coachId = getArguments().getInt("coachId");
                coach = currUser;
                String athleteString = getArguments().getString("athlete");
                if (athleteString != null && !athleteString.isEmpty()) {
                    athlete = new JSONObject(athleteString);
                }
                //if currUser is a manager
                if (currUser.getInt("classType") == 3){
                    coach = new JSONObject(getArguments().getString("coach"));
                }
                Log.d("Dashboard", "Current User: " + currUser.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleAssignWorkoutToAthlete(View v) throws JSONException {
        if (selectedWorkoutId != -1) {
            selectedAthleteId = athlete.getInt("id"); // Extract the athlete id
            updateAthleteWorkout(coachId, selectedAthleteId, selectedWorkoutId);
            showSnackbarAndNavigate(v, "Workout Added to Athlete!");
        } else {
            Snackbar.make(v, "Please select a workout to add", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private void handleRemoveWorkoutFromAthlete(View v) throws JSONException {
        if (selectedWorkoutId != -1) {
            selectedAthleteId = athlete.getInt("id"); // Extract the athlete id
            deleteAthleteWorkout(coachId, selectedAthleteId, selectedWorkoutId);
            showSnackbarAndNavigate(v, "Workout Deleted from Athlete!");
        } else {
            Snackbar.make(v, "Please select a workout to delete", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    /**
     * Handles adding an exercise to the list.
     *
     * @param v The view on which the action is performed.
     */
    private void handleAddExercise(View v) {
        TextInputEditText exerciseEditText = (TextInputEditText) binding.exercise.getEditText();
        if (exerciseEditText != null) {
            exercise = exerciseEditText.getText().toString();
            if (v.getId() == R.id.addWorkoutButton && !exercise.isEmpty()) {
                makeJsonObjectReq(result -> {
                    if (!exerciseExists) {
                        postJsonObjReq(exercise);
                        showSnackbarAndNavigate(v, "Exercise Created Successfully!");
                    } else {
                        Snackbar.make(v, "Exercise Already Exists!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        exerciseExists = false;
                    }
                });

            } else {
                Snackbar.make(v, "Please enter a proper Exercise", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

    /**
     * Handles adding a workout to the list.
     *
     * @param v The view on which the action is performed.
     */
    private void handleAddWorkout(View v) {
        TextInputEditText setsEditText = (TextInputEditText) binding.sets.getEditText();
        TextInputEditText repsEditText = (TextInputEditText) binding.reps.getEditText();
        TextInputEditText durationEditText = (TextInputEditText) binding.duration.getEditText();
        TextInputEditText restEditText = (TextInputEditText) binding.rest.getEditText();
        TextInputEditText urlEditText = (TextInputEditText) binding.urlinput.getEditText();
        if (setsEditText != null && repsEditText != null && durationEditText != null && restEditText != null) {
            duration = durationEditText.getText().toString();
            try {
                sets = Integer.parseInt(setsEditText.getText().toString());
                reps = Integer.parseInt(repsEditText.getText().toString());
            } catch (NumberFormatException e) {
                Snackbar.make(v, "Please enter proper Workout information", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            rest = restEditText.getText().toString();
            url = urlEditText.getText().toString();

            if (v.getId() == R.id.addWorkoutButton && !duration.isEmpty() && exercise != null && !rest.isEmpty()
                    && sets > 0 && reps > 0) {
                makeJsonObjectReq(result -> {
                    Exercise currentExercise = new Exercise(exercise);
                    if (url.length() > 0) {
                        postJsonObjReq(sets, reps, duration, rest, currentExercise.toJson(), url);
                    }
                    else{
                        postJsonObjReq(sets, reps, duration, rest, currentExercise.toJson(), null);
                    }
                    showSnackbarAndNavigate(v, "Workout Created Successfully!");
                });
            } else {
                Snackbar.make(v, "Please enter proper Workout information", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

    /**
     * Handles removing an exercise from the list.
     *
     * @param v The view on which the action is performed.
     */
    private void handleRemoveExercise(View v) {
        if (exercise != null) {
            try {
                deleteJsonObjReq(selectedExerciseID, result -> {
                    try {
                        Snackbar.make(v, "Successfully removed " + selectedExerciseObj.get("exerciseName"), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    navigateToDashboard(v);
                });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            Snackbar.make(v, "Please enter a proper Exercise", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    /**
     * Displays a Snackbar with a given message and navigates to the dashboard.
     *
     * @param v       The view on which the action is performed.
     * @param message The message to be displayed in the Snackbar.
     */
    private void showSnackbarAndNavigate(View v, String message) {
        Snackbar.make(v, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        navigateToDashboard(v);
    }

    /**
     * Navigates to the dashboard fragment.
     *
     * @param v The view on which the action is performed.
     */
    private void navigateToDashboard(View v) {
        Bundle bundle = new Bundle();
        if (athlete != null && athlete.has("user"))  {
            bundle.putString("currUser", coach.toString());
        }
        else{
            bundle.putString("currUser", currUser.toString());
        }
        NavHostFragment.findNavController(WorkoutEditer.this).navigate(R.id.action_Addworkout_to_Dashboard, bundle);
    }

    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned,
     * but before any saved state has been restored in to the view.
     *
     * @param view               The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String URL;

        binding.addWorkout.setChecked(true);

        if (athlete != null && athlete.has("user")) {
            toggleLayoutVisibility(View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE);
            binding.removeExercise.setText(R.string.RemoveWorkout);
            URL = Const.URL_JSON_GET_WORKOUTS;
        }
        else{
            URL = Const.URL_JSON_GET_EXERCISES;
        }
        handleClassType();

        makeJsonArrayReq(URL, this::populateExerciseSpinner);

        final CheckBox[] checkBoxes = {binding.addExercise, binding.addWorkout, binding.removeExercise};
        CompoundButton.OnCheckedChangeListener listener = this::handleCheckedChangeListener;
        for (CheckBox cb : checkBoxes) {
            cb.setOnCheckedChangeListener(listener);
        }
    }

    /**
     * Handles changes in the user's class type and updates the UI accordingly.
     */
    private void handleClassType() {
        try {
            if ((currUser.getInt("classType") == 2 || currUser.getInt("classType") == 3) && athlete != null) {
                JSONObject athleteObject = athlete.getJSONObject("user");
                binding.setFirstName(athleteObject.getString("firstName"));
                binding.setLastName(athleteObject.getString("lastName"));
            } else {
                binding.addWorkoutName.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Populates the exercise spinner with exercise names from a JSON array.
     *
     * @param result The JSON array containing exercise data.
     */
    private void populateExerciseSpinner(JSONArray result) {
        try {
            MaterialSpinner spinner = binding.exerciseSpinner;
            List<String> spinnerItems = new ArrayList<>();

            if ((currUser.getInt("classType") == 2 || currUser.getInt("classType") == 3) && athlete != null){
                spinnerItems.add("Select a workout");
            }
            else{
                spinnerItems.add("Select an exercise");
            }

            for (int i = 0; i < result.length(); i++) {
                JSONObject jsonObject = result.getJSONObject(i);
                if ((currUser.getInt("classType") == 2 || currUser.getInt("classType") == 3) && athlete != null){
                    spinnerItems.add(String.format("%s (%dx%d) %s, %s rest",
                            jsonObject.getJSONObject("exercise").getString("exerciseName"),
                            jsonObject.getInt("sets"),
                            jsonObject.getInt("reps"),
                            jsonObject.getString("duration"),
                            jsonObject.getString("rest")));
                }
                else{
                    spinnerItems.add(jsonObject.getString("exerciseName"));
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerItems);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new com.jaredrummler.materialspinner.MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                    handleSpinnerItemSelected(result, position);
                }
            });
            spinner.setSelectedIndex(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles changes in the spinner selection and updates the selected exercise accordingly.
     *
     * @param result   The JSONArray containing the exercises.
     * @param position The index of the selected item in the spinner.
     */
    private void handleSpinnerItemSelected(JSONArray result, int position) {
        if (athlete != null && athlete.has("user"))  {
            if (position > 0) { // Skip the first item, which is "Select a workout" or "Select an exercise"
                try {
                    JSONObject jsonObject = result.getJSONObject(position - 1);
                    selectedWorkoutId = jsonObject.getInt("id"); // Assuming "id" is the workout id in the JSON object
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                selectedWorkoutId = -1;
            }
        } else {
            if (position == 0) {
                exercise = null;
            } else {
                try {
                    exercise = result.getJSONObject(position - 1).getString("exerciseName");
                    selectedExerciseObj = result.getJSONObject(position - 1);
                    selectedExerciseID = selectedExerciseObj.getInt("id");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Handles changes in checked state for the CheckBox buttons and updates the layout accordingly.
     *
     * @param buttonView The CheckBox whose checked state has changed.
     * @param isChecked  The new checked state of the CheckBox.
     */
    private void handleCheckedChangeListener(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            for (CheckBox cb : new CheckBox[]{binding.addExercise, binding.addWorkout, binding.removeExercise}) {
                if (cb != buttonView) {
                    cb.setChecked(false);
                }
            }

            if (buttonView == binding.addExercise) {
                handleAddExerciseLayout();
            } else if (buttonView == binding.addWorkout) {
                handleAddWorkoutLayout();
            } else if (buttonView == binding.removeExercise) {
                handleRemoveExerciseLayout();
            }
        }
    }

    /**
     * Updates the layout for adding an exercise.
     */
    private void handleAddExerciseLayout() {
        binding.setIsChecked(true);
        binding.addWorkoutTitle.setText(R.string.AddExercise);
        binding.addWorkoutButton.setText(R.string.AddExercise);
        // Show only the exercise TextInputLayout
        toggleLayoutVisibility(View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE);
    }

    /**
     * Updates the layout for adding a workout.
     */
    private void handleAddWorkoutLayout() {
        binding.setIsChecked(false);
        binding.addWorkoutTitle.setText(R.string.AddWorkout);
        binding.addWorkoutButton.setText(R.string.AddWorkout);
        if (athlete != null && athlete.has("user")) {
            toggleLayoutVisibility(View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE);
        }
        else{
            toggleLayoutVisibility(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE, View.VISIBLE, View.GONE);
        }
    }

    /**
     * Updates the layout for removing an exercise.
     */
    private void handleRemoveExerciseLayout() {
        binding.setIsChecked(false);
        if (athlete != null && athlete.has("user")) {
            binding.addWorkoutTitle.setText(R.string.RemoveWorkout);
            binding.addWorkoutButton.setText(R.string.RemoveWorkout);
            binding.removeExercise.setText(R.string.RemoveWorkout);
            toggleLayoutVisibility(View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE);
        }
        else{
            binding.addWorkoutTitle.setText(R.string.RemoveExercise);
            binding.addWorkoutButton.setText(R.string.RemoveExercise);
            // Show only the exercise TextInputLayout
            toggleLayoutVisibility(View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE);
        }
    }

    /**
     * Toggles the visibility of the TextInputLayouts.
     *
     * @param setsVisibility        Sets the visibility for the sets TextInputLayout.
     * @param repsVisibility        Sets the visibility for the reps TextInputLayout.
     * @param durationVisibility    Sets the visibility for the duration TextInputLayout.
     * @param restVisibility        Sets the visibility for the rest TextInputLayout.
     * @param workoutNameVisibility Sets the visibility for the workout name TextInputLayout.
     * @param urlVisibility         Sets the visibility for the url TextInputLayout
     */
    private void toggleLayoutVisibility(int setsVisibility, int repsVisibility, int durationVisibility, int restVisibility, int workoutNameVisibility, int urlVisibility, int checkboxVisibility, int addWorkoutVisibility) {
        binding.sets.setVisibility(setsVisibility);
        binding.reps.setVisibility(repsVisibility);
        binding.duration.setVisibility(durationVisibility);
        binding.rest.setVisibility(restVisibility);
        binding.addWorkoutName.setVisibility(workoutNameVisibility);
        binding.urlinput.setVisibility(urlVisibility);
        binding.addExercise.setVisibility(checkboxVisibility);
        binding.addWorkoutName.setVisibility(addWorkoutVisibility);
    }


    /**
     * Sends a POST request to create a new workout.
     *
     * @param sets      the number of sets for the workout
     * @param reps      the number of reps for the workout
     * @param duration  the duration of the workout
     * @param rest      the rest time between sets
     * @param exercise  the JSONObject containing exercise information
     * @param url       the URL to send the POST request to
     */
    private void postJsonObjReq(int sets, int reps, String duration, String rest, JSONObject exercise, String url) {
        JSONObject newWorkout = new JSONObject();

        try {
            newWorkout.put("sets", sets);
            newWorkout.put("reps", reps);
            newWorkout.put("duration", duration);
            newWorkout.put("rest", rest);
            newWorkout.put("exercise", exercise);
            newWorkout.put("video", url);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Const.URL_JSON_POST_WORKOUT, newWorkout,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });

        // Adding request to request queue
        String tag_json_obj = "jobj_req";
        MainActivity.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void updateAthleteWorkout(int coachId, int athleteId, int workoutId) {
        String url = String.format("http://coms-309-043.class.las.iastate.edu:8080/coaches/%d/athletes/%d/workouts/%d", coachId, athleteId, workoutId);
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.PUT, url, response -> {
            Log.d("UpdateAthleteWorkout", "Workout added successfully");
        }, error -> {
            Log.e("UpdateAthleteWorkout", "Error: " + error.toString());
        });

        queue.add(request);
    }

    private void deleteAthleteWorkout(int coachId, int athleteId, int workoutId) {
        String url = String.format("http://coms-309-043.class.las.iastate.edu:8080/coaches/%d/athletes/%d/workouts/%d", coachId, athleteId, workoutId);
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.DELETE, url, response -> {
            Log.d("DeleteAthleteWorkout", "Workout deleted successfully");
        }, error -> {
            Log.e("DeleteAthleteWorkout", "Error: " + error.toString());
        });

        queue.add(request);
    }


    /**
     * Sends a POST request to create a new exercise.
     *
     * @param exerciseName the name of the new exercise
     */
    private void postJsonObjReq(String exerciseName) {
        JSONObject newExercise = new JSONObject();

        try {
            newExercise.put("exerciseName", exerciseName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Const.URL_JSON_POST_EXERCISE, newExercise,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });

        // Adding request to request queue
        String tag_json_obj = "jobj_req";
        MainActivity.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    /**
     * Sends a DELETE request to delete an exercise.
     *
     * @param exerciseId the ID of the exercise to delete
     * @param callback   a callback that will be executed upon successful deletion
     * @throws JSONException if the response cannot be parsed
     */
    private void deleteJsonObjReq(int exerciseId, WorkoutEditer.VolleyCallback callback) throws JSONException {

        String url = Const.URL_JSON_DELETE_EXERCISE.replace("{id}", String.valueOf(exerciseId));

        JsonPayloadRequest jsonObjReq = new JsonPayloadRequest(Request.Method.DELETE,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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

        // Adding request to request queue
        String tag_json_obj = "jobj_req";
        MainActivity.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    /**
     * A custom JsonObjectRequest to handle JSON payloads in DELETE requests.
     */
    private class JsonPayloadRequest extends JsonObjectRequest {
        private final JSONObject requestBody;

        /**
         * Constructor for JsonPayloadRequest.
         *
         * @param method        the HTTP method to use
         * @param url           the URL to send the request to
         * @param requestBody   the JSON payload to send with the request
         * @param listener      a listener for handling the response
         * @param errorListener a listener for handling errors
         */
        public JsonPayloadRequest(int method, String url, JSONObject requestBody, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
            super(method, url, null, listener, errorListener);
            this.requestBody = requestBody;
        }

        @Override
        public byte[] getBody() {
            return requestBody.toString().getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public String getBodyContentType() {
            return "application/json; charset=utf-8";
        }
    }

    /**
     * Callback interface for handling JSONArray responses.
     */
    public interface VolleyCallbackArr {
        void onSuccess(JSONArray result);
    }

    /**
     * Callback interface for handling JSONObject responses.
     */
    public interface VolleyCallback {
        void onSuccess(JSONObject result) throws JSONException;
    }

    /**
     * Sends a GET request to retrieve a JSONArray from the specified URL.
     *
     * @param url      the URL to send the request to
     * @param callback a callback that will be executed upon successful retrieval of the JSONArray
     */
    private void makeJsonArrayReq(String url, WorkoutEditer.VolleyCallbackArr callback) {
        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        MainActivity.getInstance().addToRequestQueue(jsonArrayReq, tag_json_workout_array);
    }

    /**
     * Sends a GET request to retrieve a JSONObject based on the user's selected exercise.
     *
     * @param callback a callback that will be executed upon successful retrieval of the JSONObject
     */
    private void makeJsonObjectReq(WorkoutEditer.VolleyCallback callback) {
        String URL;
        if (binding.addExercise.isChecked()){
            URL = Const.URL_JSON_GET_EXERCISES;
        }
        else{ URL = Const.URL_JSON_WORKOUT_ARRAY;}
        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        JSONObject user = null;
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                user = response.getJSONObject(i);
                                if (binding.addExercise.isChecked()){
                                    if(user.get("exerciseName").equals(exercise)){
                                        exerciseExists = true;
                                    }
                                }
                                else {
                                    if (user.getJSONObject("exercise").get("exerciseName").equals(exercise)) {
                                        exerciseExists = true;
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            VolleyLog.d(TAG, "Error while searching exercises");
                        }
                        try {
                            callback.onSuccess(user);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        MainActivity.getInstance().addToRequestQueue(jsonArrayReq, tag_json_workout_array);
    }

    /**
     * Called when the view is destroyed. Cleans up the binding.
     */
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}

