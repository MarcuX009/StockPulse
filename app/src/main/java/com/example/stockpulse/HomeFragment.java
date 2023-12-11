package com.example.stockpulse;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import com.example.stockpulse.network.FinnhubAPIResponse;
import com.example.stockpulse.network.YahooFinanceAPIResponse;

public class HomeFragment extends Fragment {

    private View view;
    private ListView stockListViewUI;
    private EditText userInputUI;
    private Button searchButtonUI;
    String sampleData[] = {"data", "data", "data", "data", "data", "data", "data", "data", "data", "data"};
    ArrayAdapter<String> arrayAdapter;
    private ToggleButton toggleButtonUI;
    private boolean isYahoo = false;

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
        toggleButtonUI = view.findViewById(R.id.toggleButtonLayout);

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

        toggleButtonUI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isYahoo = isChecked;
                Log.d("DEBUG_LOG", "isYahoo: " + isYahoo);
            }
        });

        searchButtonUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = userInputUI.getText().toString().toUpperCase();
                if (userInput.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a stock symbol", Toast.LENGTH_SHORT).show();
                } else {
                    if (isYahoo) {
                        StockAPIHelper.YFAPICall(userInput, new StockAPIHelper.ResponseListener() {
                            @Override
                            public void onYFResponse(YahooFinanceAPIResponse responseData) {
                                if (getActivity() != null) {
                                    StockFragment fragment = StockFragment.newInstance(responseData, null);
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.fragment_container, fragment)
                                            .addToBackStack(null)
                                            .commit();
                                }
                            }
                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(getContext(), "Error code: YFAPIFAIL", Toast.LENGTH_SHORT).show();
                                Log.d("DEBUG_LOG", "Error: " + t.getMessage());
                            }
                            @Override
                            public void onFHResponse(FinnhubAPIResponse fhResponse) {
                                // this should never be called, but it has to be implemented here to satisfy the interface
                            }
                        });
                    } else {
                        StockAPIHelper.FHAPICall(userInput, new StockAPIHelper.ResponseListener() {
                            @Override
                            public void onFHResponse(FinnhubAPIResponse responseData) {
                                if (getActivity() != null) {
                                    StockFragment fragment = StockFragment.newInstance(null, responseData);
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.fragment_container, fragment)
                                            .addToBackStack(null)
                                            .commit();
                                }
                            }
                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(getContext(), "Error code: FHAPIFAIL", Toast.LENGTH_SHORT).show();
                                Log.d("DEBUG_LOG", "Error: " + t.getMessage());
                            }
                            @Override
                            public void onYFResponse(YahooFinanceAPIResponse yfResponse) {
                                // this should never be called, but it has to be implemented here to satisfy the interface
                            }
                        });
                    }
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }



}