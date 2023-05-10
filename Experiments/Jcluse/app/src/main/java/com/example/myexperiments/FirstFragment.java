package com.example.myexperiments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.myexperiments.databinding.FragmentFirstBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText usernameEditText = (TextInputEditText) binding.usernameText.getEditText();
                TextInputEditText passwordEditText = (TextInputEditText) binding.passwordText.getEditText();
                if (usernameEditText != null && passwordEditText != null) {
                    String username = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    if (!username.isEmpty() && !password.isEmpty()) {
                        NavHostFragment.findNavController(FirstFragment.this)
                                .navigate(R.id.action_FirstFragment_to_ThirdFragment);
                    } else {
                    Snackbar.make(view, "Please enter proper Username and Password", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
        }}});

        binding.registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        return binding.getRoot();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}