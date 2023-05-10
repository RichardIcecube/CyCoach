package com.example.myexperiments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myexperiments.databinding.VideosBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class displays a screen that gives athletes an opportunity to view videos in a WebView associated with the workouts assigned to them
 * by their coaches.
 * @author Zane Eason
 */
public class Videos extends Fragment {

    public VideosBinding binding;

    private final String TAG = Videos.class.getSimpleName();

    private JSONArray workoutList;

    private Spinner workoutDropdown;

    /**
     * Inflate the layout for this fragment, populates the dropdown menu of workouts, and sets a listener for whenever a
     * workout is selected from the menu.
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

        binding = VideosBinding.inflate(inflater, container, false);

        binding.errorTextView.setVisibility(View.GONE);
        // Retrieve currUser from the arguments
        if (getArguments() != null) {
            try{
                workoutList = new JSONArray(getArguments().getString("workouts"));
                JSONObject currUser = new JSONObject(getArguments().getString("currUser"));
                Log.d("Videos", "Current User: " + currUser);
                Log.d("Videos", "Current User Workouts: " + workoutList.toString());
            }
            catch (JSONException e){
                Log.d(TAG, e.getMessage());
            }

        }

        binding.webView.setVisibility(View.GONE);
        populateDropdowns();

        workoutDropdown.setOnItemSelectedListener(new ItemListener());

        return binding.getRoot();

    }

    /**
     * Class to handle items being selected in the dropdown menu: When a workout is selected, either the webview is made visible and
     * the video is opened, or an error text shows there is no associated video.
     */
    public class ItemListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            try {
                if (pos != 0){
                    JSONObject workout = new JSONObject(workoutDropdown.getSelectedItem().toString());
                    String url = workout.getString("video");
                    if (!url.equals("null")){
                        setVideo(url);
                    }
                    else{
                        binding.errorTextView.setVisibility(View.VISIBLE);
                    }
                }
            }
            catch (JSONException e) {
                Log.d(TAG, e.getMessage());
            }
        }
        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    /**
     * This method pulls up a webview with the video associated with the url
     * @param url url of the video to be viewed (typically YouTube)
     */
    private void setVideo(String url){
        WebView webView = binding.webView;
        webView.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            webView.getSettings().setSafeBrowsingEnabled(true);
        }
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    /**
     * This method fills the dropdown menu with a list of the athlete's workouts
     */
    private void populateDropdowns(){
        String[] workouts = new String[workoutList.length()+1];
        workouts[0] = "Please select a workout";

        for (int i = 1; i < workoutList.length()+1; i++)
        {
            try {
                JSONObject workout = new JSONObject(workoutList.getJSONObject(i-1).toString());
                workouts[i] = workout.toString();
            }
            catch (JSONException e){
                System.out.println(e.getMessage());
            }
        }

        workoutDropdown = binding.workoutDropdown;

        ArrayAdapter<String> workoutAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, workouts);

        workoutDropdown.setAdapter(workoutAdapter);
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
