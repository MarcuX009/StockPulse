    package com.example.stockpulse;

    import android.content.Context;
    import android.content.SharedPreferences;
    import android.os.Bundle;

    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Toast;

    import com.example.stockpulse.network.StockAPIHelper;
    import com.example.stockpulse.network.StockObject;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.firestore.FieldValue;
    import com.google.firebase.firestore.FirebaseFirestore;

    import java.util.ArrayList;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Set;

    public class FavouritesFragment extends Fragment implements RecyclerViewInterface {
        private static final String PREFS_NAME = "StockPulse_Prefs";
        private List<StockObject> stockItemList;
        private RecyclerView favRecyclerView;
        private stockAdapter stockAdapter;
        private Set<String> favouritesSet;

        public FavouritesFragment() {
            // Required empty public constructor
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.d("DEBUG_LOG", "favr onCreate: ");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View rootView = inflater.inflate(R.layout.fragment_favourites, container, false);
            stockItemList = generateStockItem();
            favRecyclerView = rootView.findViewById(R.id.favListLayout);
            favRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            stockAdapter = new stockAdapter(stockItemList, this);
            favRecyclerView.setAdapter(stockAdapter);
            Log.d("DEBUG_LOG", "favr onCreateView: ");
            return rootView;
        }

        public List<StockObject> generateStockItem() {
            List<StockObject> stockItemList = new ArrayList<>();
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

            favouritesSet = sharedPreferences.getStringSet("FavouritesList", new HashSet<>());
            Log.d("DEBUG_LOG", "FavouritesList: " + favouritesSet);
            for (String favourite : favouritesSet) {
                Log.d("DEBUG_LOG", "Favourite: " + favourite);
                // Log.d("DEBUG_LOG", "Info:" + sharedPreferences.getString(favourite, ""));
                // split the sharedPreferences.getString(favourite,"") : {c:192.685,d:-0.495}
                // cut the { and }
                // String[] info = sharedPreferences.getString(favourite, "").substring(1, sharedPreferences.getString(favourite, "").length() - 1).split(",");
                // String c = info[0].split(":")[1];
                // String d = info[1].split(":")[1];

                    StockAPIHelper.YFAPICall(favourite, new StockAPIHelper.ResponseListener() {
                        @Override
                        public void onYFResponse(StockObject responseData) {
                            if (getActivity() != null && isAdded()) {
                                stockItemList.add(new StockObject(responseData.getStockSymbol(), responseData.getC(), responseData.getD()));
                                stockAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFHResponse(StockObject fhResponse) {
                           // do nothing here since we are not using FH API for now
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Log.d("DEBUG_LOG", "Error updating stock: " + t.getMessage());
                        }
                    });
                }
                // StockObject stockObject = new StockObject(favourite, Double.parseDouble(c), Double.parseDouble(d));
    //            stockItemList.add(stockObject);

            return stockItemList;
        }

        @Override
        public void onItemClick(int position) {
            Log.d("favList", "List number:" + position);
            if (!stockItemList.isEmpty()) {
                StockObject element = stockItemList.get(position);
                String symbol = element.getStockSymbol();
                StockAPIHelper.YFAPICall(symbol, new StockAPIHelper.ResponseListener() {
                    @Override
                    public void onYFResponse(StockObject response) {
                        if (getActivity() != null) {
                            StockFragment fragment = StockFragment.newInstance(response, null);
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }

                    @Override
                    public void onFHResponse(StockObject fhResponse) {
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(getContext(), "Error code: YFAPIFAIL", Toast.LENGTH_SHORT).show();
                        Log.d("DEBUG_LOG", "Error: " + t.getMessage());
                    }
                });
            }

        }

        @Override
        public void onItemLongClick(int position) {
            String stockName = stockItemList.get(position).getStockSymbol(); // get the stock name from the stockItemList before removing it
            stockItemList.remove(position);
            stockAdapter.notifyItemRemoved(position);
            Toast.makeText(getActivity(), "Long Click detected stock removed", Toast.LENGTH_SHORT).show();
            // delete the stock from the sharedPreferences
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Set<String> favouritesSet = new HashSet<>(sharedPreferences.getStringSet("FavouritesList", new HashSet<>()));
            favouritesSet.remove(stockName); // remove from favouritesSet
            editor.putStringSet("FavouritesList", favouritesSet);
            editor.apply();
            Log.d("DEBUG_LOG", "New favourite:\n");
            for (String favourite : favouritesSet) {
                Log.d("DEBUG_LOG", "  " + favourite);
            }
            removeStockFromFirestore(stockName);
        }
        private void removeStockFromFirestore(String stockName) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            db.collection("users").document(userId).update("favourites", FieldValue.arrayRemove(stockName))
                    .addOnSuccessListener(aVoid -> Log.d("DEBUG_LOG", stockName+ " removed from Firestore"))
                    .addOnFailureListener(e -> Log.d("DEBUG_LOG", "Error removing stock from Firestore", e));
        }

    }