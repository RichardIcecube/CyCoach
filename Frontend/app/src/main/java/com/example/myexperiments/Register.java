package com.example.myexperiments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.myexperiments.databinding.RegisterBinding;
import com.example.myexperiments.utils.Const;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is for handling user registration.
 * @author Jayden Luse and Zane Eason
 */
public class Register extends Fragment {

    private RegisterBinding binding;
    private final String TAG = Register.class.getSimpleName();

    private boolean userExists = false;

    private String email;

    private JSONObject newUser = new JSONObject();

    private JSONObject currUser;

    private final String tag_json_array = "jarray_req";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = RegisterBinding.inflate(inflater, container, false);

        setupArguments();
        setupRegisterButtonOnClickListener();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupCheckBoxes();
    }

    /**
     * Sets up the arguments for the fragment. Allowing for a different display on the UserProfile fragment.
     */
    private void setupArguments() {
        if (!getArguments().isEmpty()) {
            try {
                String title = getArguments().getString("title");
                binding.registerTitleText.setText(title);
                if (title.equals("Edit Profile")) {
                    currUser = new JSONObject(getArguments().getString("currUser", null));
                    binding.registerbutton.setText("UPDATE");
                    binding.checkbox.setVisibility(View.INVISIBLE);
                    binding.checkbox1.setVisibility(View.INVISIBLE);
                    binding.checkbox2.setVisibility(View.INVISIBLE);
                    Log.d("User Profile:", "Current User: " + currUser.toString());
                } else {
                    binding.registerbutton.setText("REGISTER");
                }
            } catch (JSONException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Sets up the on-click listener for the register button.
     */
    private void setupRegisterButtonOnClickListener() {
        binding.registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText firstnameEditText = (TextInputEditText) binding.firstname.getEditText();
                TextInputEditText lastnameEditText = (TextInputEditText) binding.lastname.getEditText();
                TextInputEditText emailEditText = (TextInputEditText) binding.email.getEditText();
                TextInputEditText passwordEditText = (TextInputEditText) binding.registerPassword.getEditText();
                if (firstnameEditText != null && lastnameEditText != null && emailEditText != null && passwordEditText != null) {
                    String email = emailEditText.getText().toString();
                    String firstName = firstnameEditText.getText().toString();
                    String lastName = lastnameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    if (v.getId() == R.id.registerbutton && !email.isEmpty()
                            && !firstName.isEmpty()
                            && !lastName.isEmpty()
                            && !password.isEmpty()) {

                        if (binding.registerTitleText.getText().equals("Edit Profile")) {
                            updateUser(currUser, firstName, lastName, email, password);
                            Bundle bundle = new Bundle();
                            bundle.putString("currUser", currUser.toString());
                            NavHostFragment.findNavController(Register.this).navigate(R.id.action_Register_to_UserProfile, bundle);
                        } else {
                            makeJsonArrayReq(new VolleyCallback() {
                                @Override
                                public void onSuccess(JSONObject result) {
                                    if (!userExists) {
                                        postJsonObjReq(firstName, lastName, email, password);
                                        Snackbar.make(v, "User Created Successfully!", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                        NavHostFragment.findNavController(Register.this).navigate(R.id.action_Register_to_SignIn);
                                    } else {
                                        Snackbar.make(v, "User Already Exists!", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                        userExists = false;
                                    }
                                }
                            });
                        }
                    } else {
                        Snackbar.make(v, "Please enter proper User information", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            }
        });
    }

    /**
     * Sets up the checkboxes with an onCheckedChangeListener.
     */
    private void setupCheckBoxes() {
        final CheckBox[] checkBoxes = {binding.checkbox, binding.checkbox1, binding.checkbox2};
        CompoundButton.OnCheckedChangeListener listener = (buttonView, isChecked) -> {
            if (isChecked) {
                for (CheckBox cb : checkBoxes) {
                    if (cb != buttonView) {
                        cb.setChecked(false);
                    }
                }
            }
        };

        for (CheckBox cb : checkBoxes) {
            cb.setOnCheckedChangeListener(listener);
        }
    }

    /**
     Updates the user information. The information to be updated is specified by the
     parameters passed to the method.
     @param currUser The JSON object containing the current user information
     @param firstName The first name of the user to be updated
     @param lastName The last name of the user to be updated
     @param email The email address of the user to be updated
     @param password The password of the user to be updated
     */
    private void updateUser(JSONObject currUser, String firstName, String lastName, String email, String password) {

        int id = 0;

        try {
            id = currUser.getInt("id");
            String date = currUser.getString("dateJoined");
            int classType = currUser.getInt("classType");
            JSONArray workouts = currUser.getJSONArray("workouts");

            currUser.remove("id");
            currUser.remove("firstName");
            currUser.remove("lastName");
            currUser.remove("emailAddress");
            currUser.remove("password");
            currUser.remove("dateJoined");
            currUser.remove("classType");
            currUser.remove("workouts");

            currUser.put("id", id);
            currUser.put("firstName", firstName);
            currUser.put("lastName", lastName);
            currUser.put("emailAddress", email);
            currUser.put("password", password);
            currUser.put("dateJoined", date);
            currUser.put("classType", classType);
            currUser.put("workouts", workouts);
        }
        catch (JSONException e) {
            System.out.println(e.getMessage());
        }

        String url = Const.URL_UPDATE_USER_PUT.replace("{userId}", String.valueOf(id));
        JsonObjectRequest userUpdate = new JsonObjectRequest(Request.Method.PUT, url, currUser,
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
        MainActivity.getInstance().addToRequestQueue(userUpdate, update_tag);
    }



    /**
     * Making json object request
     *
     * @param firstName A String representing the first name of the user.
     * @param lastName  A String representing the last name of the user.
     * @param email     A String representing the email address of the user.
     * @param password  A String representing the password of the user.
     */
    private void postJsonObjReq(String firstName, String lastName, String email, String password) {
        try {
            newUser.put("firstName", firstName);
            newUser.put("lastName", lastName);
            newUser.put("emailAddress", email);
            newUser.put("password", password);
            if (binding.checkbox.isChecked()){
                newUser.put("classType", 1);
            } else if (binding.checkbox1.isChecked()) {
                newUser.put("classType", 2);
            } else{
                newUser.put("classType", 3);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
                Const.URL_JSON_USERS, newUser,
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

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    public interface VolleyCallback {
        void onSuccess(JSONObject result);
    }

    private void makeJsonArrayReq(VolleyCallback callback) {
        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(Const.URL_JSON_USERS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        JSONObject user = null;
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                user = response.getJSONObject(i);
                                if (user.get("emailAddress").equals(email)) {
                                    userExists = true;
                                }
                            }
                        } catch (JSONException e) {
                            VolleyLog.d(TAG, "Error while searching users");
                        }
                        callback.onSuccess(user);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        MainActivity.getInstance().addToRequestQueue(jsonArrayReq, tag_json_array);
    }
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
