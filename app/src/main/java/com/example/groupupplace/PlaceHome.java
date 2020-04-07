package com.example.groupupplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

public class PlaceHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "place";
    DrawerLayout drawerLayout;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_home);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.na_view);
        navigationView.setNavigationItemSelectedListener(PlaceHome.this);
        navigationView.bringToFront();
    }
    public void addplace(View v){
        Intent in = new Intent(this, CreatePlace.class);
        startActivity(in);
    }

    public void menuHambergerPlace(View v){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()){
            case R.id.menu_home:
                Log.d(TAG, "onNavigationItemSelected home: " + item.getTitle());
                break;
            case R.id.menu_account:
                goToManageAccount();
                Log.d(TAG, "onNavigationItemSelected account: " + item.getTitle());
                break;
            case R.id.menu_calendar:
                goToManageCalendar();
                Log.d(TAG,"onNavigationItemSelected calendar: " + item.getTitle());
                break;
            case R.id.menu_signout:
                Log.d(TAG, "onNavigationItemSelected signout: " + item.getTitle());
                break;
        }
        drawerLayout.closeDrawers();
        return true;
    }

    public void goToManageAccount() {
        Intent intent = new Intent(PlaceHome.this, Register.class);
        startActivity(intent);
    }

    public void goToManageCalendar() {
        Intent intent = new Intent(PlaceHome.this, Register.class);
        startActivity(intent);
    }

}
