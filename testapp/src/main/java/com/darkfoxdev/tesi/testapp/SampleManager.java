package com.darkfoxdev.tesi.testapp;

import android.app.Activity;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class SampleManager{

    private static final SampleManager sharedInstance = new SampleManager();

    public static SampleManager getInstance() {
        return sharedInstance;
    }

    public void addListener (Activity activity) {

    }
}
