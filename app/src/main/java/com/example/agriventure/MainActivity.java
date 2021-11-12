package com.example.agriventure;

import android.content.Context;
import android.net.Network;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.agriventure.util.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        Set<Integer> topLevelDestinations = new HashSet<>();
        topLevelDestinations.add(R.id.navigation_farmer_login);
        topLevelDestinations.add(R.id.navigation_buyer_login);
        topLevelDestinations.add(R.id.navigation_select_profile);
        topLevelDestinations.add(R.id.navigation_farmer_market);
        topLevelDestinations.add(R.id.navigation_credit);
        topLevelDestinations.add(R.id.navigation_learn);
        topLevelDestinations.add(R.id.navigation_buyer_market);
        topLevelDestinations.add(R.id.navigation_offers);
        topLevelDestinations.add(R.id.navigation_payments);
        topLevelDestinations.add(R.id.navigation_farmer_register);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if(destination.getId()==R.id.navigation_select_profile||
                    destination.getId()==R.id.navigation_farmer_login||
                    destination.getId()==R.id.navigation_buyer_login) {
                    Objects.requireNonNull(getSupportActionBar()).hide();
                    navView.setVisibility(View.GONE);
                }else{
                Objects.requireNonNull(getSupportActionBar()).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showSnack(String message){
        Snackbar snack =
                Snackbar.make(this.findViewById(android.R.id.content),
                        message,
                        BaseTransientBottomBar.LENGTH_LONG);
        snack.setBackgroundTint(this.getResources().getColor(R.color.orange,
                this.getTheme()));
        snack.setTextColor(this.getResources().getColor(R.color.white,
                this.getTheme()));
        snack.show();
    }


    public void setUpBottomNavigation(String profile, int menuID, int checkedItemID){
        if (profile.equals("Farmer")){
            setUpNav(menuID,checkedItemID);
        }else if(profile.equals("Buyer")){
            setUpNav(menuID,checkedItemID);
        }
    }

    private void setUpNav(int menuID, int checkedItemID){
        navView.getMenu().clear();
        navView.inflateMenu(menuID);
        NavigationUI.setupWithNavController(navView, navController);
        navView.setVisibility(View.VISIBLE);
        MenuItem item = navView.getMenu().findItem(checkedItemID);
        item.setChecked(true);
    }
    
}