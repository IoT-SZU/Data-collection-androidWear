package com.example.tommy.androidwear.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tommy.androidwear.R;
import com.example.tommy.androidwear.StringAdapter;

import java.util.List;

/**
 * Created by Tommy on 2018/3/24.
 */

public class SerialFragment extends Fragment{

    private static final String TAG = "SerialFragment";
    private static final String typeKey = "type";

    String[] order1 = {"3.3/gentle","3.3/hard",
            "3.5/fist",
            "3.6/angleIn45","3.6/angleOut45","3.6/walk",
            "3.7/handwash",
            "4.1/random","4.4/intimate"};
    String[] order2 = {"1.2/trainningSize",
            "3.1/tap/over","3.1/tap/below","3.1/tap/left","3.1/tap/right",
            "3.2/sensor/P2","3.2/sensor/P3","3.2/sensor/P4",
            "3.9/time/day1","3.9/time/day2","3.9/time/day7","3.9/time/day14","3.9/time/day30"};

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public static SerialFragment newInstance(String type) {
        SerialFragment fragment = new SerialFragment();
        Bundle args = new Bundle();
        args.putString(typeKey,type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_serial_wear,container,false);
       return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final WatchViewStub stub = (WatchViewStub)getActivity().findViewById(R.id.fragment_serial_wear);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub watchViewStub) {
                layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                recyclerView = (RecyclerView)getActivity().findViewById(R.id.recyclerview);
                recyclerView.setLayoutManager(layoutManager);
                switch (getArguments().getString(typeKey)){
                    case "order1":
                        adapter = new StringAdapter(order1,getFragmentManager());
                        break;
                    case "order2":
                        adapter = new StringAdapter(order2,getFragmentManager());
                        break;
                }
                recyclerView.setAdapter(adapter);

                getActivity().findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getFragmentManager().popBackStack();
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

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
}
