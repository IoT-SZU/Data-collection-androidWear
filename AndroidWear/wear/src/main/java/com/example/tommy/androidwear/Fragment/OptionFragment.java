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
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_option, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().findViewById(R.id.order1).setOnClickListener(this);
        getActivity().findViewById(R.id.order2).setOnClickListener(this);
        getActivity().findViewById(R.id.setting).setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.order1:
                Log.i(TAG, "onClick: order1");
                break;
            case R.id.order2:
                Log.i(TAG, "onClick: order2");
                break;
            case R.id.setting:
                Log.i(TAG, "onClick: setting");
                break;
        }
    }
}
