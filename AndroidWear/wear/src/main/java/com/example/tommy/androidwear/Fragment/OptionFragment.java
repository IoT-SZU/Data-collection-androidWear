package com.example.tommy.androidwear.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tommy.androidwear.R;

/**
 * Created by Tommy on 2018/3/24.
 */

public class OptionFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "OptionFragment";
    public static OptionFragment newInstance() {
        OptionFragment fragment = new OptionFragment();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_option, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated: ");
        getActivity().findViewById(R.id.order1).setOnClickListener(this);
        getActivity().findViewById(R.id.order2).setOnClickListener(this);
        getActivity().findViewById(R.id.setting).setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: ");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: ");
    }

    
    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.order1:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container,SerialFragment.newInstance("order1"))
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.order2:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container,SerialFragment.newInstance("order2"))
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.setting:
                Log.i(TAG, "onClick: setting");
                break;
        }
    }
}
