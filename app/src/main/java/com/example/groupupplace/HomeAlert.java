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
    }
    public void backHome(View v) {
        Intent in = new Intent(this, HomePlace.class);
        in.putExtra("email", email+"");
        startActivity(in);
//        addNotification();
    }
}
