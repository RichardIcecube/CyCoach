package com.example.myexperiments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myexperiments.databinding.UserProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class displays a screen that will show the currently logged in user's information, and give them an opportunity to change it
 * with a button.
 * @author Zane Eason
 */
public class UserProfile extends Fragment{

    private UserProfileBinding binding;

    private JSONObject currUser;

    /**
     * Inflate the layout for this fragment, fills the text boxes with relevant information, and sets a listener to navigate away.
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

        binding = UserProfileBinding.inflate(inflater, container, false);

        // Retrieve currUser from the arguments
        if (getArguments() != null) {
            try {
                currUser = new JSONObject(getArguments().getString("currUser"));
                Log.d("User Profile:", "Current User: " + currUser);
                String firstName = currUser.getString("firstName");
                String lastName = currUser.getString("lastName");
                String email = currUser.getString("emailAddress");
                String password = currUser.getString("password");
                binding.firstNameText.setText(firstName);
                binding.lastNameText.setText(lastName);
                binding.emailAddressText.setText(email);
                binding.userPasswordText.setText(password);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        binding.editProfileButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("currUser", currUser.toString());
            bundle.putString("title", "Edit Profile");
            NavHostFragment.findNavController(UserProfile.this)
                    .navigate(R.id.action_UserProfile_to_Register, bundle);
        });


        return binding.getRoot();
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
