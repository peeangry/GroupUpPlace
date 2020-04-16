package com.example.groupupplace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeAlert extends AppCompatActivity {
    String id="",email="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_alert);
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
    }
    public void backHome(View v) {
        Intent in = new Intent(HomeAlert.this, HomePlace .class);
        in.putExtra("id", id + "");
        in.putExtra("email", email + "");
        startActivity(in);
    }
}
