package com.example.stockpulse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //    private Toolbar toolBar;
    private static final String PREFS_NAME = "StockPulse_Prefs";
    private DrawerLayout drawerLayout;
    private String[] stockList = new String[]{
            "AAPL", "MSFT", "GOOGL", "TSLA", "TSM", "AMZN", "SPY",
            "NFLX", "V", "BA", "JNJ", "PG", "UNH", "MA", "NVDA", "HD",
            "JPM", "VZ", "DIS", "ADBE", "PFE", "KO", "NKE", "MRK", "XOM",
            "CMCSA", "T", "PEP", "CSCO", "ABT", "AVGO", "ORCL", "CRM", "ABBV",
            "WMT", "ACN", "TXN", "MCD", "COST", "DHR", "MDLZ", "LLY", "BMY",
            "AMT", "HON", "QCOM", "LIN", "SBUX", "UPS", "UNP", "LOW", "C",
            "CVS", "BAC", "INTC", "WFC", "TMO", "CHTR", "INTU", "PYPL",
            "IBM", "BLK", "AMGN", "FISV", "GS", "MMM", "CAT", "GE",
            "NOW", "LMT", "DE", "BKNG", "ISRG", "AMD", "GILD", "RTX",
            "MO", "TGT", "EL", "MU", "MS", "D", "SYK", "FIS", "SO",
            "MDY", "GD", "ECL", "ATVI", "ADP", "MMC", "BK", "REGN",
            "CB", "EMR", "CI", "PNC", "USB", "CME", "ADI"
    };

    // savedInstanceState used to restore the state of the activity, it cant hold large data
    // the bundle object has the data limited, TransactionTooLargeException will be thrown if the data is too large
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_menu_hint,
                R.string.close_menu_hint);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.menu_home);
        }

        // need to optimize this part later, for testing use only
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> allStockList = new HashSet<>(sharedPreferences.getStringSet("AllStockList", new HashSet<>()));
        for (String stock : stockList) {
            allStockList.add(stock);
        }
        editor.putStringSet("AllStockList", allStockList);
        editor.apply();

        // 读取以验证
        Set<String> savedStockList = sharedPreferences.getStringSet("AllStockList", new HashSet<>());
        Log.d("DEBUG_LOG", "AllStockList: " + savedStockList);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_home) {
            // close all the fragments and open the home fragment
            getSupportFragmentManager().popBackStack(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        } else if (itemId == R.id.menu_favourites) {
            getSupportFragmentManager().popBackStack(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavouritesFragment()).commit();
        } else if (itemId == R.id.menu_simulator) {
            getSupportFragmentManager().popBackStack(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SimulatorFragment()).commit();
        } else if (itemId == R.id.menu_about) {
            getSupportFragmentManager().popBackStack(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
        } else if (itemId == R.id.menu_logout) {
            Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    protected void onPuase() {
//        super.onPause();
//        Log.d("DEBUG_LOG", "enter onPause!");
//    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}