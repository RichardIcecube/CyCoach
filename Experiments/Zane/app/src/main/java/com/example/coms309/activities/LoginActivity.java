package com.example.coms309.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.example.coms309.R;
import com.example.coms309.services.RequestListener;
import com.example.coms309.services.VolleyListener;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText username   = (EditText)findViewById(R.id.username);
            EditText password   = (EditText)findViewById(R.id.password);
            try {
                sendLoginInfo(username.getText().toString(), password.getText().toString());
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
    };

    private View.OnClickListener signUpListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Not doing this, too lazy
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button login  = (Button)findViewById(R.id.loginButton);
        final Button signUp = (Button)findViewById(R.id.signUpButton);

        /* Set up the listeners for the buttons */
        login.setOnClickListener(loginListener);
        signUp.setOnClickListener(signUpListener);
    }

    private void sendLoginInfo(String username, String password) throws JSONException {
        RequestListener loginListener = new RequestListener(){

            @Override
            public void onSuccess(Object jsonObject){
                JSONObject object = (JSONObject)jsonObject;
                Intent homeIntent = new Intent(getBaseContext(), HomeActivity.class);
                try {
                    homeIntent.putExtra("message", object.get("message").toString());
                    startActivity(homeIntent);
                }
                catch(JSONException jsonException){
                    System.out.println("Major Problems");
                }
            }
            @Override
            public void onFailure(String error){
                System.out.println(error);
            }
        };

        JSONObject data = new JSONObject();
        data.put("username", username);
        data.put("password", password);

        VolleyListener.makeRequest(getApplicationContext(), "077e2f47", loginListener, data, Request.Method.POST);
    }
}