package com.example.agriventure;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if(destination.getId()==R.id.navigation_select_profile||destination.getId()==R.id.navigation_farmer_login||destination.getId()==R.id.navigation_buyer_login) {
                    Objects.requireNonNull(getSupportActionBar()).hide();
                    navView.setVisibility(View.GONE);
                }
        });
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