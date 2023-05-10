package com.example.myexperiments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myexperiments.databinding.CoachBinding;
import com.example.myexperiments.utils.Const;
import com.example.myexperiments.utils.IntRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractCollection;

/**
 * This class is for handling coach actions.
 * @author Jayden Luse and Zane Eason
 */
public class Coach extends Fragment {

    public CoachBinding binding;

    int coachId;

    private RequestQueue requestQueue;
    private final String TAG = Coach.class.getSimpleName();

    String tag_json_coach_array;

    private JSONObject currUser;

    private JSONObject coach;


    /**
     * Inflate the layout for this fragment, retrieve the current user, and set up the table for displaying athletes.
     *
     * @param inflater           The LayoutInflater used to inflate the layout
     * @param container          The ViewGroup that contains this fragment
     * @param savedInstanceState The saved instance state of the fragment
     * @return The inflated layout's root view
     */
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        requestQueue = Volley.newRequestQueue(requireContext());

        binding = CoachBinding.inflate(inflater, container, false);

        // Retrieve currUser from the arguments
        if (getArguments() != null) {
            try {
                currUser = new JSONObject(getArguments().getString("currUser"));
                //if currUser is a manager, e.g. navigated from the manager screen
                if (currUser.getInt("classType") == 3){
                    coach = new JSONObject(getArguments().getString("coach"));
                    coachId = getArguments().getInt("coachId");
                }
                else {
                    coach = currUser;
                }
                Log.d("Workout", "Current User: " + currUser);
                makeJsonArrayReq(new Coach.VolleyCallbackArray() {
                    @Override
                    public void onSuccess(JSONArray result) throws JSONException {
                        if (result.length() == 0){
                            binding.setHasAthletes(false);
                        }
                        else {
                            binding.setHasAthletes(true);
                            TableLayout tableLayout = binding.tableLayout;
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject athlete = result.getJSONObject(i);
                                TableRow row = createAthleteRow(athlete, i);
                                tableLayout.addView(row);
                            }
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String firstName = coach.optString("firstName");
        String lastName = coach.optString("lastName");
        binding.setFirstName(firstName);
        binding.setLastName(lastName);

        binding.addWorkoutCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                try {
                    if(currUser.getInt("classType") == 3){
                        bundle.putString("currUser", currUser.toString());
                        bundle.putString("coach", coach.toString());
                    }
                    else{
                        bundle.putString("currUser", currUser.toString());
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                NavHostFragment.findNavController(Coach.this)
                        .navigate(R.id.action_Coach_to_AddWorkout, bundle);
            }
        });

        return binding.getRoot();

    }

    /**
     * Create a TableRow for an athlete.
     *
     * @param athlete The JSONObject representing an athlete
     * @param index   The index of the athlete in the list
     * @return A TableRow containing the athlete's name and action buttons
     * @throws JSONException If there is a problem retrieving data from the JSONObject
     */
    private TableRow createAthleteRow(JSONObject athlete, int index) throws JSONException {
        String athleteFirstName = athlete.getJSONObject("user").getString("firstName");
        String athleteLastName = athlete.getJSONObject("user").getString("lastName");
        String name = athleteFirstName + " " + athleteLastName;

        // Create a new TableRow for this athlete
        TableRow row = new TableRow(getContext());

        // Add TextViews to display the athlete's first and last name
        TextView nameView = new TextView(getContext());
        if (name.length() > 15){
            name = name.substring(0, 13) + "...";
            nameView.setText(name);
        }
        else{
            nameView.setText(name);
        }
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.latobold);
        nameView.setTypeface(typeface);
        nameView.setGravity(Gravity.CENTER);
        row.addView(nameView);

        // Add a Button to allow the coach to view the athlete's workout
        Button viewButton = createButton(R.string.View, R.color.gray, index);
        int viewButtonBaseId = 100000; // Use a predefined base ID for view buttons
        int viewButtonId = viewButtonBaseId + (index * 2);
        viewButton.setId(viewButtonId);
        row.addView(viewButton);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFragment(R.id.action_Coach_to_Workouts, athlete, index);
            }
        });

        // Add a Button to allow the coach to modify the athlete's workout
        Button modifyButton = createButton(R.string.AddRemove, R.color.gray, index);
        int modifyButtonBaseId = 200000; // Use a predefined base ID for modify buttons
        int modifyButtonId = modifyButtonBaseId + (index * 2) + 1;
        modifyButton.setId(modifyButtonId);
        row.addView(modifyButton);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFragment(R.id.action_Coach_to_AddWorkout, athlete, index);
            }
        });

        return row;
    }

    /**
     * Create a Button with specified properties.
     *
     * @param textId      The resource ID for the button's text
     * @param textColorId The resource ID for the button's text color
     * @param tag         The tag to be set for the button
     * @return The created Button
     */
    private Button createButton(int textId, int textColorId, Object tag) {
        Button button = new Button(getContext());
        button.setText(textId);
        button.setTextColor(ContextCompat.getColor(getContext(), textColorId));
        button.setTypeface(ResourcesCompat.getFont(getContext(), R.font.latobold));
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, 32);
        button.setGravity(Gravity.CENTER);
        button.setTag(tag); // set the index of the athlete as the tag
        return button;
    }

    /**
     * Navigate to the specified fragment with the athlete's data in a bundle.
     *
     * @param actionId The ID of the action used to navigate to the target fragment
     * @param athlete  The JSONObject representing an athlete
     * @param index    The index of the athlete in the list
     */
    private void navigateToFragment(int actionId, JSONObject athlete, int index) {
        Bundle bundle = new Bundle();
        try {
            String athleteName = athlete.getJSONObject("user").getString("firstName") + " " + athlete.getJSONObject("user").getString("lastName");
            JSONArray workouts = athlete.getJSONArray("workoutList");
            athlete.put("workouts", workouts.toString());
            bundle.putString("currUser", currUser.toString());
            bundle.putString("athlete", athlete.toString());
            bundle.putString("athleteName", athleteName);
            bundle.putString("coach", coach.toString());
            int userId = athlete.getInt("id"); // Replace with the desired user ID
            if (currUser.getInt("classType") == 3){
                bundle.putInt("coachId", coachId);
                NavHostFragment.findNavController(Coach.this)
                        .navigate(actionId, bundle);
            }
            else {
                getClassIDByUserId(userId, new VolleyCallback() {
                    @Override
                    public void onSuccess(int classID) {
                        int coachId = classID;
                        bundle.putInt("coachId", coachId);
                        NavHostFragment.findNavController(Coach.this)
                                .navigate(actionId, bundle);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

        public interface VolleyCallbackArray {
        void onSuccess(JSONArray result) throws JSONException;
    }

    private void makeJsonArrayReq(Coach.VolleyCallbackArray callback) throws JSONException {

        String url = Const.URL_JSON_GET_COACHES_ATHLETES.replace("{id}", coach.get("id").toString());

        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        JSONArray athletesArray = response;
                        try {
                            callback.onSuccess(athletesArray);
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

        MainActivity.getInstance().addToRequestQueue(jsonArrayReq, tag_json_coach_array);
    }
    
    public interface VolleyCallback {
        void onSuccess(int classID);
    }

    private void getClassIDByUserId(int userId, final VolleyCallback callback) {
        String url = "http://coms-309-043.class.las.iastate.edu:8080/users/" + userId + "/classID";

        IntRequest intRequest = new IntRequest(Request.Method.GET, url,
                new Response.Listener<Integer>() {
                    @Override
                    public void onResponse(Integer response) {
                        Log.d("MainActivity", "Class ID: " + response);
                        callback.onSuccess(response); // Call the onSuccess method of the callback
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MainActivity", "Error fetching class ID", error);
                    }
                });

        requestQueue.add(intRequest);
    }

    /**
     * Called when the view is destroyed. Cleans up the binding.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

