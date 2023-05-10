package com.example.coms309.services;

public interface RequestListener {
    public void onSuccess(Object response);
    public void onFailure(String errorMessage);
}
