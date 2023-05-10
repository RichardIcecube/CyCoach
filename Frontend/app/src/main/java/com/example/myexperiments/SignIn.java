package com.example.myexperiments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.myexperiments.databinding.SignInBinding;
import com.example.myexperiments.utils.Const;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is for handling user sign-in. Allowing for the three different
 * types of users to login via their email and password.
 * @author Jayden Luse and Zane Eason
 */
public class SignIn extends Fragment {

    private SignInBinding binding;

    public JSONObject currUser = null;

    private String email;

    private String password;

    private final String TAG = SignIn.class.getSimpleName();

    private final String tag_json_obj = "jUser";

    private boolean userFound = false;

    /**
     * Initializes the view for this fragment.
     */
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = SignInBinding.inflate(inflater, container, false);

        setupSignInButtonOnClickListener();
        setupRegisterButtonOnClickListener();

        return binding.getRoot();
    }

    /**
     * Sets up the on-click listener for the sign-in button.
     */
    private void setupSignInButtonOnClickListener() {
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText usernameEditText = (TextInputEditText) binding.usernameText.getEditText();
                TextInputEditText passwordEditText = (TextInputEditText) binding.passwordText.getEditText();
                if (usernameEditText != null && passwordEditText != null) {
                    email = usernameEditText.getText().toString();
                    password = passwordEditText.getText().toString();
                    if (!email.isEmpty() && !password.isEmpty()) {
                        validateUserCredentials(new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                if (userFound) {
                                    Bundle bundle = new Bundle();
                                    MainActivity.currUser = currUser;
                                    bundle.putString("currUser", currUser.toString());
                                    NavHostFragment.findNavController(SignIn.this).navigate(R.id.action_SignIn_to_Dashboard, bundle);
                                } else {
                                    Snackbar.make(view, "User Not Found!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            }
                        });
                    } else {
                        Snackbar.make(view, "Please enter proper Username and Password", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            }
        });
    }

    /**
     * Sets up the on-click listener for the register button.
     */
    private void setupRegisterButtonOnClickListener() {
        binding.registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("title", "Register");
                bundle.putString("currUser", null);
                NavHostFragment.findNavController(SignIn.this)
                        .navigate(R.id.action_SignIn_to_Register, bundle);
            }
        });
    }

    public interface VolleyCallback {
        void onSuccess(JSONObject result);
    }

    /**
     * Validates user credentials by making a JSON array request.
     *
     * @param callback A callback to be invoked when the request completes successfully.
     */
    private void validateUserCredentials(VolleyCallback callback) {
        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(Const.URL_JSON_USERS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        JSONObject user = null;
                        String emailWithoutDomain = email.split("@")[0];
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                user = response.getJSONObject(i);
                                String userEmailAddressWithoutDomain = user.getString("emailAddress").split("@")[0];
                                if ((user.get("emailAddress").equals(email) || userEmailAddressWithoutDomain.equals(emailWithoutDomain))
                                        && user.get("password").equals(password)) {
                                    currUser = response.getJSONObject(i);
                                    userFound = true;
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

        String tag_json_array = "jarray_req";
        MainActivity.getInstance().addToRequestQueue(jsonArrayReq, tag_json_array);
    }

    /**
     * Handles the fragment's view destruction.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}