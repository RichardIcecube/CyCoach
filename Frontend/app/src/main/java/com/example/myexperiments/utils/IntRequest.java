package com.example.myexperiments.utils;

import androidx.annotation.NonNull;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.nio.charset.StandardCharsets;

public class IntRequest extends Request<Integer> {
    private final Response.Listener<Integer> listener;

    public IntRequest(int method, String url, Response.Listener<Integer> listener,
                      Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
    }

    @Override
    protected Response<Integer> parseNetworkResponse(NetworkResponse response) {
        try {
            String responseBody = new String(response.data, StandardCharsets.UTF_8);
            int parsedInt = Integer.parseInt(responseBody);
            return Response.success(parsedInt, HttpHeaderParser.parseCacheHeaders(response));
        } catch (NumberFormatException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(@NonNull Integer response) {
        listener.onResponse(response);
    }
}
