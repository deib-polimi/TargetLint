package com.darkfoxdev.tesi.testapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class SampleFragment extends Fragment {

    String string;

    public SampleFragment(){
        super();
        doSomething();
    }

    public SampleFragment(String string){
        super();
        this.string = string;
    }

    private void doSomething(){

        ArrayList<String> p = new ArrayList<>();

            for (String pollo : p) {
                int j = 5;
            }

            do {
                int k = 0;

            } while (string != "d");


            while (string != "d") {
                int k = 0;
            }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Button button = (Button) inflater.inflate(R.layout.custom_button, mLinearLayout, true);
        Button button = (Button) inflater.inflate(R.layout.custom_button, mLinearLayout, false);
        Button button = (Button) inflater.inflate(R.layout.custom_button, mLinearLayout);


        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
