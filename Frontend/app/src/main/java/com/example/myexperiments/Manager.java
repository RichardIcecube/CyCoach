package com.example.myexperiments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.myexperiments.databinding.ManagerBinding;
import com.example.myexperiments.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class displays a screen that allows a manager to view their coaches and navigate to managing their links
 * @author Zane Eason
 */
public class Manager extends Fragment {

    private ManagerBinding binding;

    private JSONObject currUser;

    private JSONArray athletes = new JSONArray();

    private JSONArray coaches = new JSONArray();

    private int managerId;

    private final String TAG = Manager.class.getSimpleName();

    /**
     * Inflate the layout for this fragment, gets the arguments from the bundle, displays the table of coaches,
     * and sets up listeners to travel to other screens
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

        binding = ManagerBinding.inflate(inflater, container, false);

        // Retrieve currUser from the arguments
        if (getArguments() != null) {
            try {
                currUser = new JSONObject(getArguments().getString("currUser"));
                Log.d("Manager", "Current User: " + currUser.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        binding.peopleTable.removeAllViews();
        displayRelationships();

        binding.linkScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Bundle bundle = new Bundle();
                bundle.putString("currUser", currUser.toString());
                bundle.putString("coaches", coaches.toString());
                bundle.putString("athletes", athletes.toString());
                NavHostFragment.findNavController(Manager.this).navigate(R.id.action_Manager_to_Link, bundle);
            }
        });

        binding.coachLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Bundle bundle = new Bundle();
                bundle.putString("currUser", currUser.toString());
                bundle.putString("coaches", coaches.toString());
                bundle.putInt("managerId", managerId);
                NavHostFragment.findNavController(Manager.this).navigate(R.id.action_Manager_to_CoachLink, bundle);
            }
        });

        return binding.getRoot();
    };

    /**
     * Displays the table of coaches by first getting the manager id, then the list of coaches associated with that manager,
     * then a list of athletes. The list of athletes is not used in creating the table but is stored to be sent to the link screen.
     */
    private void displayRelationships() {
        getManagerId(new VolleyCallback() {
            @Override
            public void onSuccess(Object result) {
                getCoaches(new VolleyCallback() {
                    @Override
                    public void onSuccess(Object result) {
                        getAthletes(new VolleyCallback() {
                            @Override
                            public void onSuccess(Object result) {
                                TableLayout table = binding.peopleTable;
                                try {
                                    for (int i = 0; i < coaches.length(); i++) {
                                        String coachFirstName = coaches.getJSONObject(i).getJSONObject("user").getString("firstName");
                                        String coachLastName = coaches.getJSONObject(i).getJSONObject("user").getString("lastName");
                                        String name = coachFirstName + " " + coachLastName;
                                        // Create a new TableRow for this coach
                                        TableRow row = new TableRow(getContext());
                                        // Add TextViews to display the coach's first and last name
                                        TextView nameView = new TextView(getContext());
                                        if (name.length() > 15) {
                                            name = name.substring(0, 13) + "...";
                                            nameView.setText(name);
                                        } else {
                                            nameView.setText(name);
                                        }

                                        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.latobold);
                                        nameView.setTypeface(typeface);
                                        nameView.setGravity(Gravity.CENTER);
                                        row.addView(nameView);

                                        // Add a Button to allow the coach to view the athlete's workout
                                        Button viewButton = new Button(getContext());
                                        int viewButtonBaseId = 100000; // Use a predefined base ID for view buttons
                                        int viewButtonId = viewButtonBaseId + i;
                                        viewButton.setId(viewButtonId);
                                        viewButton.setText(R.string.Athletes);
                                        viewButton.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
                                        viewButton.setTypeface(ResourcesCompat.getFont(getContext(), R.font.latobold));
                                        viewButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, 32);
                                        viewButton.setGravity(Gravity.CENTER);
                                        row.addView(viewButton);
                                        viewButton.setTag(i); // set the index of the coach as the tag

                                        viewButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // Retrieve the index of the athlete from the button's tag
                                                int index = (int) v.getTag();

                                                // Create a bundle to pass the athlete's data to the Workout fragment
                                                Bundle bundle = new Bundle();
                                                try {
                                                    JSONObject coach = coaches.getJSONObject(index).getJSONObject("user");
                                                    int coachId = coaches.getJSONObject(index).getInt("id");
                                                    bundle.putString("currUser", currUser.toString());
                                                    bundle.putString("coach", coach.toString());
                                                    bundle.putInt("coachId", coachId);
                                                    NavHostFragment.findNavController(Manager.this)
                                                            .navigate(R.id.action_Manager_to_Coach, bundle);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        // Add the new TableRow to the TableLayout
                                        table.addView(row);
                                    }
                                } catch (JSONException e) {
                                    Log.d(TAG, e.getMessage());
                                }
                            }
                        });
                    }
                });

            }
        });

    }

    /**
     * This method takes the current manager user and gets their managerId in order to make future server calls
     *
     * @param callback Callback interface for handling responses
     */
    private void getManagerId(VolleyCallback callback){
        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(Const.URL_GET_MANAGERS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        int id = 0;
                        try{
                            for (int i = 0; i < response.length(); i++){
                                JSONObject temp = new JSONObject(response.getJSONObject(i).getJSONObject("user").toString());
                                if (temp.get("firstName").equals(currUser.get("firstName")) && temp.get("lastName").equals(currUser.get("lastName"))){
                                    id = response.getJSONObject(i).getInt("id");
                                    currUser = new JSONObject(temp.toString());
                                    managerId = id;
                                }
                            }
                        }
                        catch (JSONException e){
                            Log.d(TAG, e.getMessage());
                        }
                        callback.onSuccess((Integer)managerId);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        String tag_json_array = "jarray_req";
        MainActivity.getInstance().addToRequestQueue(jsonArrayReq, tag_json_array);
    }

    /**
     * This method gets a list of coaches in order to find the id of a certain coach
     *
     * @param callback Callback interface for handling responses
     */
    private void getCoaches(VolleyCallback callback) {
        String url = Const.URL_GET_MANAGERS_COACHES.replace("{managerId}", String.valueOf(managerId));
        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try{
                            coaches = new JSONArray();
                            for (int i = 0; i < response.length(); i++){
                                coaches.put(response.getJSONObject(i));
                            }
                        }
                        catch (JSONException e){
                            Log.d(TAG, e.getMessage());
                        }
                        callback.onSuccess(coaches);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        String tag_json_array = "jarray_req";
        MainActivity.getInstance().addToRequestQueue(jsonArrayReq, tag_json_array);
    }

    /**
     * This method gets a list of athletes in order to find the id of a certain athlete
     *
     * @param callback Callback interface for handling responses
     */
    private void getAthletes(VolleyCallback callback){
        String url = Const.URL_GET_ATHLETES.replace("{managerId}", String.valueOf(managerId));
        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try{
                            athletes = new JSONArray();
                            for (int i = 0; i < response.length(); i++){
                                athletes.put(response.getJSONObject(i));
                            }
                        }
                        catch (JSONException e){
                            Log.d(TAG, e.getMessage());
                        }
                        callback.onSuccess(athletes);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        String tag_json_array = "jarray_req";
        MainActivity.getInstance().addToRequestQueue(jsonArrayReq, tag_json_array);
    }

    /**
     * Callback interface to handle server calls (wait for asynchronous response)
     */
    public interface VolleyCallback {
        void onSuccess(Object result);
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

