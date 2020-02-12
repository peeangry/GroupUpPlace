package com.example.groupupplace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class createPlace extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_place);
    }

    public void backHome(View v){
        Intent in = new Intent(this,placeHome.class);
        startActivity(in);
    }

    public void selectThemePlace(View v){
        Intent in = new Intent(this,theme.class);
        startActivity(in);
    }
}
