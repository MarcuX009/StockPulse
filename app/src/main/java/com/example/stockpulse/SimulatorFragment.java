package com.example.stockpulse;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stockpulse.network.StockAPIHelper;
import com.example.stockpulse.network.StockObject;

public class SimulatorFragment extends Fragment {

    //UI variable
    private View view;
    private TextView stockName;
    private TextView stockValue;
    private TextView positiveProfit;
    private TextView negativeProfit;
    private Button searchButton;
    private Button clearButton;
    private Button calculateButton;
    private EditText searchUserInput;
    private EditText buyPriceInput;
    private EditText buyAmountInput;
    private EditText sellPriceInput;
    private EditText sellAmountInput;


    //Calculation Variable
    private int buyAmount;
    private int sellAmount;
    private double buyPrice;
    private double sellPrice;
    private double result;
    private String formattedValue;

    public SimulatorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DEBUG_LOG", "SimulatorFragment onCreated...");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_simulator, container, false);

        //initializing
        stockName = view.findViewById(R.id.simulatorStockPriceLayout);
        stockValue = view.findViewById(R.id.simulatorStockPriceValueLayout);
        positiveProfit = view.findViewById(R.id.resultPositiveLayout);
        negativeProfit = view.findViewById(R.id.resultNegativeLayout);
        searchButton = view.findViewById(R.id.simulatorSearchButton);
        clearButton = view.findViewById(R.id.allClearButton);
        calculateButton = view.findViewById(R.id.calculateButtonLayout);
        searchUserInput = view.findViewById(R.id.simulatorUserInputLayout);
        buyPriceInput = view.findViewById(R.id.buyInputPriceLayout);
        buyAmountInput = view.findViewById(R.id.buyInputAmountLayout);
        sellPriceInput = view.findViewById(R.id.sellInputPriceLayout);
        sellAmountInput = view.findViewById(R.id.sellInputAmountLayout);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = searchUserInput.getText().toString().toUpperCase();
                if (!userInput.isEmpty()) {
                    StockAPIHelper.YFAPICall(userInput, new StockAPIHelper.ResponseListener() {
                        @Override
                        public void onYFResponse(StockObject response) {
                            if (getActivity() != null) {
                                stockName.setText(response.getStockSymbol());
                                stockValue.setText(String.valueOf(response.getC()));
                            }
                        }

                        @Override
                        public void onFHResponse(StockObject fhResponse) {
                            // this should never be called, but it has to be implemented here to satisfy the interface
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Toast.makeText(getContext(), "Error code: YFAPIFAIL", Toast.LENGTH_SHORT).show();
                            Log.d("DEBUG_LOG", "Error: " + t.getMessage());
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Please enter something", Toast.LENGTH_SHORT).show();
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyPriceInput.setText("");
                buyAmountInput.setText("");
                sellPriceInput.setText("");
                sellAmountInput.setText("");
                positiveProfit.setVisibility(View.INVISIBLE);
                negativeProfit.setVisibility(View.INVISIBLE);
            }
        });
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //setting up the input Values for calculator
                    buyAmount = Integer.parseInt(buyAmountInput.getText().toString());
                    sellAmount = Integer.parseInt(sellAmountInput.getText().toString());
                    buyPrice = Double.parseDouble(buyPriceInput.getText().toString());
                    sellPrice = Double.parseDouble(sellPriceInput.getText().toString());
                } catch (Exception e) {
                    Log.d("DEBUG_LOG", "error message: " + e);
                    Toast.makeText(getContext(), "Please enter all the required field", Toast.LENGTH_SHORT).show();
                }
                if (buyAmount == 0 | sellAmount == 0 | buyPrice == 0.0 | sellPrice == 0.0) {
                    Toast.makeText(getContext(), "Please enter all the required field", Toast.LENGTH_SHORT).show();
                } else if (sellAmount > buyAmount) {
                    Toast.makeText(getContext(), "You can't sell more then you Buy", Toast.LENGTH_SHORT).show();
                } else {
                    result = ((sellPrice * sellAmount) - (buyPrice * buyAmount));
                    if (result >= 0) {
                        formattedValue = String.format("%.2f", result);
                        positiveProfit.setText(formattedValue);
                        positiveProfit.setVisibility(View.VISIBLE);
                    } else {
                        formattedValue = String.format("%.2f", result);
                        negativeProfit.setText(formattedValue);
                        negativeProfit.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        return view;
    }
}