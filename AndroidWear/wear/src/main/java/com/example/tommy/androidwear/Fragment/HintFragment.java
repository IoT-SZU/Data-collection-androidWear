package com.example.tommy.androidwear.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.WatchViewStub;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tommy.androidwear.MainActivity;
import com.example.tommy.androidwear.R;
import com.example.tommy.androidwear.StaticConfig;
import com.example.tommy.androidwear.StringAdapter;

import java.util.HashMap;

/**
 * Created by Tommy on 2018/3/25.
 */

public class HintFragment extends Fragment {

    private static final String HashKey = "key";
    HashMap<String,String> hashMap = new HashMap<>();

    public static HintFragment newInstance(String param1) {
        HintFragment fragment = new HintFragment();
        Bundle args = new Bundle();
        args.putString(HashKey, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hashMap.put("gentle","小力敲击35下");
        hashMap.put("hard","大力敲击35");
        hashMap.put("fist","握拳敲击35下");
        hashMap.put("angleIn45","手背向外45°敲击35下");
        hashMap.put("angleOut45","手背向内45°敲击35下");
        hashMap.put("walk","跑步机或原地踏步敲击35下");
        hashMap.put("handwash","洗手后敲击35下");
        hashMap.put("random","随机位置敲击35下");
        hashMap.put("intimate","监督者随机位置敲击10下");
        hashMap.put("trainningSize","敲击100下");
        hashMap.put("over","上移0.5敲击35下");
        hashMap.put("below","下移0.5敲击35下");
        hashMap.put("left","左移0.5敲击35下");
        hashMap.put("right","右移0.5敲击35下");
        hashMap.put("P2","手表在P2敲击35下");
        hashMap.put("P3","手表在P3敲击35下");
        hashMap.put("P4","手表在P4敲击35下");
        hashMap.put("day","敲击35下");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hint_wear,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final WatchViewStub stub = (WatchViewStub)getActivity().findViewById(R.id.fragment_hint_wear);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub watchViewStub) {
                TextView hint = (TextView) getActivity().findViewById(R.id.hint);
                String hintStrKey= getArguments().getString(HashKey);
                hintStrKey = hintStrKey.substring(hintStrKey.lastIndexOf("/") + 1);
                String hintStr = hashMap.get(hintStrKey);
                hint.setText(hintStr);

                getActivity().findViewById(R.id.goback).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getFragmentManager().popBackStack();
                    }
                });

                getActivity().findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("fileName", StaticConfig.FINAL_PATH);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}
