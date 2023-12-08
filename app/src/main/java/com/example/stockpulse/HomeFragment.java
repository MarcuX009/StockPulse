package com.example.stockpulse;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private View view;
    private ListView stockListViewUI;
    private EditText userInputUI;
    private Button searchButtonUI;
    String sampleData[] = {"data","data","data","data","data","data","data","data","data","data"};
    ArrayAdapter<String> arrayAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        stockListViewUI = view.findViewById(R.id.stockListLayout);
        userInputUI = view.findViewById(R.id.searchBarLayout);
        searchButtonUI = view.findViewById(R.id.searchButtonLayout);

        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, sampleData);
        stockListViewUI.setAdapter(arrayAdapter);

//        stockListViewUI.setOnClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView temp = ((TextView) view);
//                temp.setText(sampleData[position]);
//                arrayAdapter.notifyDataSetChanged();
//            }
//        });
        // Inflate the layout for this fragment

        return view;
    }


}