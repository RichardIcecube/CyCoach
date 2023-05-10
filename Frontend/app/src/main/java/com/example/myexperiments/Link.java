package com.example.myexperiments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.myexperiments.databinding.LinkBinding;
import com.example.myexperiments.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is for a screen for a manager user to link and unlink coaches and athletes together
 * @author Zane Eason
 */
public class Link extends Fragment {


    private LinkBinding binding;

    private JSONObject currUser;

    private JSONArray athletes;

    private JSONArray coaches;

    private JSONObject currCoach;

    private JSONObject currAthlete;

    private final String TAG = Link.class.getSimpleName();

    private Spinner coachDropdown;

    private Spinner athleteDropdown;

    private int managerId;

    private int coachId;

    private int athleteId;

    private boolean linked = false;

    /**
     * Inflate the layout for this fragment, retrieve the current user and arrays of coaches and athletes, populates
     * the dropdown spinners, and sets a listener for the link/unlink button
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

        binding = LinkBinding.inflate(inflater, container, false);

        // Retrieve currUser from the arguments
        if (getArguments() != null) {
            try {
                currUser = new JSONObject(getArguments().getString("currUser"));
                coaches = new JSONArray(getArguments().getString("coaches"));
                athletes = new JSONArray(getArguments().getString("athletes"));
                Log.d("Link", "Current User: " + currUser.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        populateDropdowns();

        binding.linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                try{
                    currCoach = new JSONObject(coachDropdown.getSelectedItem().toString());
                    currAthlete = new JSONObject(athleteDropdown.getSelectedItem().toString());
                }
                catch (JSONException e){
                    Log.d(TAG, e.getMessage());
                }

                findIds();

                Bundle bundle = new Bundle();
                bundle.putString("currUser", currUser.toString());
                NavHostFragment.findNavController(Link.this).navigate(R.id.action_Link_to_Manager, bundle);
            }
        });

        return binding.getRoot();
    }

    /**
     * Calls the server with a PUT mapping to link a coach and athlete together
     */
    private void linkUsers(){
        String url = Const.URL_COACH_ATHLETE_LINK.replace("{managerId}", String.valueOf(managerId)).replace("{athleteId}", String.valueOf(athleteId)).replace("{coachId}", String.valueOf(coachId));
        JsonObjectRequest linkUsers = new JsonObjectRequest(Method.PUT, url, null,
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

        String update_tag = "jObj_put";
        MainActivity.getInstance().addToRequestQueue(linkUsers, update_tag);
    }

    /**
     * Calls the server with a PUT mapping to unlink a coach and athlete
     */
    private void unlinkUsers(){
        String url = Const.URL_COACH_ATHLETE_UNLINK.replace("{managerId}", String.valueOf(managerId)).replace("{athleteId}", String.valueOf(athleteId));
        JsonObjectRequest linkUsers = new JsonObjectRequest(Method.PUT, url, null,
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

        String update_tag = "jObj_put";
        MainActivity.getInstance().addToRequestQueue(linkUsers, update_tag);
    }

    /**
     * Gets the ids of relevant users, then links the coach and athlete if they are unlinked or unlinks them if they are linked
     */
    private void findIds() {
        getManagers(new VolleyCallback() { //Waits for the response to come back, due to the call being async
            @Override
            public void onSuccess(Integer result) {
                getCoaches(new VolleyCallback() { //Waits for the response to come back, due to the call being async
                    @Override
                    public void onSuccess(Integer result) {
                        getAthletes(new VolleyCallback() { //Waits for the response to come back, due to the call being async
                            @Override
                            public void onSuccess(Integer result) {
                                checkIfLinked(new VolleyCallback() { //Waits for the response to come back, due to the call being async
                                    @Override
                                    public void onSuccess(Integer result) {
                                        if (linked){
                                            unlinkUsers();
                                        }
                                        else{
                                            linkUsers();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * This method searches the list of linked athletes of a coach for a certain athlete and assigns the boolean variable
     * 'linked' to either true if the athlete was found or false if they weren't
     *
     * @param callback Callback interface for handling responses
     */
    private void checkIfLinked(VolleyCallback callback){
        String url = Const.URL_GET_COACH_ATHLETES_BASE.replace("{id}", String.valueOf(coachId));
        JsonArrayRequest checkIfLinkedUsers = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        for (int i = 0; i < response.length(); i++){
                            try{
                                JSONObject linkedAthlete = new JSONObject(response.getJSONObject(i).getJSONObject("user").toString());
                                if (linkedAthlete.toString().equals(currAthlete.toString())){
                                    linked = true;
                                }
                            }
                            catch (JSONException e){
                                Log.d(TAG, e.getMessage());
                            }
                        }
                        callback.onSuccess(null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });

        String update_tag = "jObj_put";
        MainActivity.getInstance().addToRequestQueue(checkIfLinkedUsers, update_tag);
    }

    /**
     * This method takes the current manager user and gets their managerId in order to make future server calls
     *
     * @param callback Callback interface for handling responses
     */
    private void getManagers(VolleyCallback callback){
        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(Const.URL_GET_MANAGERS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        int id;
                        try{
                            for (int i = 0; i < response.length(); i++){
                                JSONObject temp = new JSONObject(response.getJSONObject(i).getJSONObject("user").toString());
                                if (temp.get("firstName").equals(currUser.get("firstName")) && temp.get("lastName").equals(currUser.get("lastName"))){
                                    id = response.getJSONObject(i).getInt("id");
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
    private void getCoaches(VolleyCallback callback){
        String url = Const.URL_GET_MANAGERS_COACHES.replace("{managerId}", String.valueOf(managerId));
        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        int id;
                        try{
                            for (int i = 0; i < response.length(); i++){
                                JSONObject temp = new JSONObject(response.getJSONObject(i).getJSONObject("user").toString());
                                if (temp.get("firstName").equals(currCoach.get("firstName")) && temp.get("lastName").equals(currCoach.get("lastName"))){
                                    id = response.getJSONObject(i).getInt("id");
                                    coachId = id;
                                }
                            }
                        }
                        catch (JSONException e){
                            Log.d(TAG, e.getMessage());
                        }
                        callback.onSuccess((Integer)coachId);
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
                        int id;
                        try{
                            for (int i = 0; i < response.length(); i++){
                                JSONObject temp = new JSONObject(response.getJSONObject(i).getJSONObject("user").toString());
                                if (temp.get("firstName").equals(currAthlete.get("firstName")) && temp.get("lastName").equals(currAthlete.get("lastName"))){
                                    id = response.getJSONObject(i).getInt("id");
                                    athleteId = id;
                                }
                            }
                        }
                        catch (JSONException e){
                            Log.d(TAG, e.getMessage());
                        }
                        callback.onSuccess((Integer)athleteId);
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
        void onSuccess(Integer result);
    }

    /**
     * This method fills the dropdown menus with the list of coaches and athletes
     */
    private void populateDropdowns(){
        String[] coachNames = new String[coaches.length()];
        String[] athleteNames = new String[athletes.length()];

        for (int i = 0; i < coaches.length(); i++)
        {
            try {
                JSONObject coach = new JSONObject(coaches.getJSONObject(i).getJSONObject("user").toString());
                coachNames[i] = coach.toString();
//                coachNames[i] = coach.getString("firstName") + " " + coach.getString("lastName");
            }
            catch (JSONException e){
                System.out.println(e.getMessage());
            }
        }

        for (int i = 0; i < athletes.length(); i++)
        {
            try {
                JSONObject athlete = new JSONObject(athletes.getJSONObject(i).getJSONObject("user").toString());
                athleteNames[i] = athlete.toString();
//                athleteNames[i] = athlete.getString("firstName") + " " + athlete.getString("lastName");
            }
            catch (JSONException e){
                System.out.println(e.getMessage());
            }
        }

        coachDropdown = binding.coachDropdown;
        athleteDropdown = binding.athleteDropdown;

        ArrayAdapter<String> coachAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, coachNames);
        ArrayAdapter<String> athleteAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, athleteNames);

        coachDropdown.setAdapter(coachAdapter);
        athleteDropdown.setAdapter(athleteAdapter);
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
