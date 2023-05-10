package com.example.myexperiments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myexperiments.databinding.DashboardBinding;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class represents the Dashboard fragment, the dashboard allows
 * users to navigate to their profile, workouts, or
 * manage other users based on their role.
 * @author Jayden Luse and Zane Eason
 */
public class Dashboard extends Fragment {

    private DashboardBinding binding;
    private JSONObject currUser;

    /**
     * This method inflates the layout and initializes the class variables.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DashboardBinding.inflate(inflater, container, false);
        retrieveCurrentUser();
        setupButtons();
        return binding.getRoot();
    }

    /**
     * Retrieves the current user from the arguments.
     */
    private void retrieveCurrentUser() {
        if (getArguments() != null) {
            try {
                currUser = new JSONObject(getArguments().getString("currUser"));
                Log.d("Dashboard", "Current User: " + currUser.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets up the visibility and click listeners for the buttons.
     */
    private void setupButtons() {
        try {
            String classType = currUser.getString("classType");
            if (classType.equals("2")) {
                setupCoachButton();
            } else {
                setupWorkoutButton();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        setupChatButton();

        binding.editProfilePanel.setOnClickListener(view -> navigateToUserProfile());
    }

    /**
     * Sets up the chat button
     */
    private void setupChatButton(){
        binding.chatButton.setOnClickListener(view -> navigateToChat());
    }


    /**
     * Sets up the coach button.
     */
    private void setupCoachButton() {
        binding.coachButton.setVisibility(View.VISIBLE);
        binding.workoutButton.setVisibility(View.INVISIBLE);
        binding.coachButton.setOnClickListener(view -> navigateToCoach());
    }

    /**
     * Sets up the workout button.
     */
    private void setupWorkoutButton() {
        binding.coachButton.setVisibility(View.INVISIBLE);
        binding.workoutButton.setVisibility(View.VISIBLE);
        binding.workoutButton.setOnClickListener(view -> {
            try {
                navigateBasedOnClassType();
            } catch (JSONException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    /**
     * Navigates to a destination based on the classType value.
     *
     * @throws JSONException if there is an error parsing JSON data
     */
    private void navigateBasedOnClassType() throws JSONException {
        Bundle bundle = createBundleWithCurrentUser();
        int classType = currUser.getInt("classType");

        if (classType == 1) {
            NavHostFragment.findNavController(this).navigate(R.id.action_Dashboard_to_Workouts, bundle);
        } else if (classType == 3) {
            NavHostFragment.findNavController(this).navigate(R.id.action_Dashboard_to_Manager, bundle);
        }
    }

    /**
     * Navigates to the coach destination.
     */
    private void navigateToCoach() {
        Bundle bundle = createBundleWithCurrentUser();
        NavHostFragment.findNavController(this).navigate(R.id.action_Dashboard_to_Coach, bundle);
    }

    /**
     * Navigates to the user profile destination.
     */
    private void navigateToUserProfile() {
        Bundle bundle = createBundleWithCurrentUser();
        NavHostFragment.findNavController(this).navigate(R.id.action_Dashboard_to_UserProfile, bundle);
    }

    /**
     * Navigates to the chat screen.
     */
    private void navigateToChat(){
        Bundle bundle = createBundleWithCurrentUser();
        NavHostFragment.findNavController(this).navigate(R.id.action_Dashboard_to_Chat, bundle);
    }
    /**
     * Creates a bundle with the current user.
     *
     * @return A bundle containing the current user.
     */
    private Bundle createBundleWithCurrentUser() {
        Bundle bundle = new Bundle();
        bundle.putString("currUser", currUser.toString());
        return bundle;
    }

    /**
     * This method handles menu item selections.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_profile) {
            navigateToUserProfile();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the view is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
