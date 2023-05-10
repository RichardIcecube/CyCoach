package com.example.myexperiments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myexperiments.databinding.ChatBinding;
import com.google.android.material.snackbar.Snackbar;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * This class represents the Chat fragment, this class allows
 * users to communicate through websockets
 * @author Jayden Luse and Zane Eason
 */
public class Chat extends Fragment {

    public ChatBinding binding;

    private WebSocketServerHandler server;
    private WebSocketClient cc;
    private EditText chatInput;
    private Draft[] drafts = {new Draft_6455()};

    private JSONObject currUser;

    private final String TAG = Chat.class.getSimpleName();

    /**
     * Creates the fragment's view.
     */
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = ChatBinding.inflate(inflater, container, false);

        if (getArguments() != null){
            try{
                currUser = new JSONObject(getArguments().getString("currUser"));
            }
            catch (JSONException e){
                Log.d(TAG, e.getMessage());
            }
        }

        chatInput = binding.chatInput;
        ScrollView scroller = binding.scrollView2;
        scroller.setSmoothScrollingEnabled(true);
        Button connectButton = binding.connectButton;
        TextView chatContent = binding.chatContent;
        Button sendButton = binding.sendButton;

        sendButton.setOnClickListener(v -> {
            try {
                if (chatInput.getText().toString().isEmpty()){
                    Snackbar.make(v, "Type something first!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else{
                    cc.send(chatInput.getText().toString());
                    chatInput.setText("");
                }
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage());
                if (chatInput.getText().toString().isEmpty()){
                    Snackbar.make(v, "Type something first!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                if (cc == null){
                    Snackbar.make(v, "Not connected!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        connectButton.setOnClickListener(view -> {
            if (cc != null){
                cc.close();
            }
            server = new WebSocketServerHandler(8080);
            server.start();

            new Handler().postDelayed(() -> {
                String w = "";
                try{
                    w = "ws://coms-309-043.class.las.iastate.edu:8080/websocket/" + currUser.getInt("id");
                    //chatInput.getText().toString();
                }
                catch (JSONException e){
                    Log.d(TAG, e.getMessage());
                    Snackbar.make(view, "You must log in before you can chat!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                try {
                    Log.d("Socket:", "Trying socket");
                    cc = new WebSocketClient(new URI(w), drafts[0]) {
                        @Override
                        public void onMessage(String message) {
                            Log.d("", "run() returned: " + message);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String s = chatContent.getText().toString();
                                    String response = s + "\nServer:" + message;
                                    chatContent.setText(response);
                                }
                            });
                        }

                        @Override
                        public void onOpen(ServerHandshake handshake) {
                            Log.d("OPEN", "run() returned: " + "is connecting");
                        }

                        @Override
                        public void onClose(int code, String reason, boolean remote) {
                            Log.d("CLOSE", "onClose() returned: " + reason);
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("Exception:", e.toString());
                        }
                    };
                } catch (URISyntaxException e) {
                    Log.d("Exception:", e.getMessage());
                    e.printStackTrace();
                }
                cc.connect();
            }, 1000);
        });

        return binding.getRoot();
    }

    /**
     * Closes the connection, destroys the view, and clears the binding
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (cc != null){
            cc.close();
        }
        binding = null;
    }
}
