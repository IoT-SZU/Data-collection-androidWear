package com.example.tommy.androidwear;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.tommy.androidwear.Fragment.OptionFragment;

public class AppStart extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_start);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, OptionFragment.newInstance())
                .commit();
    }

    public void enterOrder1(View view) {
    }
}
