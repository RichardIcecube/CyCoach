package com.example.myexperiments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.example.myexperiments.databinding.CoachLinkBinding;
import com.example.myexperiments.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class allows a manager to link or unlink a coach to their account.
 * @author Jayden Luse and Zane Eason
 */
public class CoachLink extends Fragment {

    private CoachLinkBinding binding;

    private JSONObject currUser;

    private JSONArray coaches;

    private JSONObject currCoach;

    private final String TAG = CoachLink.class.getSimpleName();

    private Spinner coachDropdown;

    private ArrayAdapter<String> coachAdapter;

    private int managerId;

    private int coachId;

    private boolean linked = false;

    /**
     * onCreateView is called when the view is created. It sets up the view and retrieves the current user and coaches from the arguments.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = CoachLinkBinding.inflate(inflater, container, false);

        // Retrieve currUser from the arguments
        if (getArguments() != null) {
            try {
                currUser = new JSONObject(getArguments().getString("currUser"));
                coaches = new JSONArray(getArguments().getString("coaches"));
                managerId = getArguments().getInt("managerId");
                Log.d("CoachLink", "Current User: " + currUser.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        getCoaches(new VolleyCallback() {
            @Override
            public void onSuccess(Object result) {
                populateDropdowns();
            }
        });


        binding.linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                try{
                    currCoach = new JSONObject(coachDropdown.getSelectedItem().toString());
                }
                catch (JSONException e){
                    Log.d(TAG, e.getMessage());
                }

                findIds();

                Bundle bundle = new Bundle();
                bundle.putString("currUser", currUser.toString());
                NavHostFragment.findNavController(CoachLink.this).navigate(R.id.action_CoachLink_to_Manager, bundle);
            }
        });

        return binding.getRoot();
    };

    /**
     * linkUsers sends a PUT request to the server to link the manager and coach accounts.
     */
    private void linkUsers(){
        String url = Const.URL_MANAGER_COACH_LINK.replace("{managerId}", String.valueOf(managerId)).replace("{coachId}", String.valueOf(coachId));
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
     * unlinkUsers sends a PUT request to the server to unlink the manager and coach accounts.
     */
    private void unlinkUsers(){
        String url = Const.URL_MANAGER_COACH_UNLINK.replace("{managerId}", String.valueOf(managerId)).replace("{coachId}", String.valueOf(coachId));
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
     * Method to find the IDs of the coach and manager users.
     */
    private void findIds() {
        getCoachId(new VolleyCallback() {
            @Override
            public void onSuccess(Object result) {
                checkIfLinked(new VolleyCallback() { //Waits for the response to come back, due to the call being async
                    @Override
                    public void onSuccess(Object result) {
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

    /**
     * Checks if the current coach is linked to the manager and calls the onSuccess method of the given callback.
     *
     * @param callback the callback to call onSuccess method of
     */
    private void checkIfLinked(VolleyCallback callback){
        String url = Const.URL_GET_MANAGERS_COACHES.replace("{managerId}", String.valueOf(managerId));
        JsonArrayRequest checkIfLinkedUsers = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        for (int i = 0; i < response.length(); i++){
                            try{
                                JSONObject linkedCoach = new JSONObject(response.getJSONObject(i).getJSONObject("user").toString());
                                if (linkedCoach.toString().equals(currCoach.toString())){
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
     * Gets the ID of the current coach and calls the onSuccess method of the given callback with the ID as an argument.
     *
     * @param callback the callback to call onSuccess method of with the coach ID as an argument
     */
    private void getCoachId(VolleyCallback callback){
        String url = Const.URL_GET_ALL_COACHES.replace("{managerId}", String.valueOf(managerId));
        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        int id = 0;
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
     * Gets all coaches linked to the current manager and calls the onSuccess method of the given callback with an array of coaches as an argument.
     *
     * @param callback the callback to call onSuccess method of with an array of coaches as an argument
     */
    private void getCoaches(VolleyCallback callback) {
        String url = Const.URL_GET_ALL_COACHES.replace("{managerId}", String.valueOf(managerId));
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
     * Populates the spinner dropdown with coach names fetched from the server.
     */
    private void populateDropdowns(){
        String[] coachNames = new String[coaches.length()];

        for (int i = 0; i < coaches.length(); i++)
        {
            try {
                JSONObject coach = new JSONObject(coaches.getJSONObject(i).getJSONObject("user").toString());
                coachNames[i] = coach.toString();
                //coachNames[i] = coach.getString("firstName") + " " + coach.getString("lastName");
            }
            catch (JSONException e){
                System.out.println(e.getMessage());
            }
        }

        coachDropdown = binding.coachDropdown;

        coachAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, coachNames);

        coachDropdown.setAdapter(coachAdapter);
    }

    /**
     * Handles menu item clicks.
     *
     * @param item the selected menu item
     * @return true if the event was handled, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Interface for handling Volley responses.
     */
    public interface VolleyCallback {
        void onSuccess(Object result);
    }

    /**
     * Called when the fragment's view is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
