package com.example.groupupplace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CreatePlace extends AppCompatActivity {

    //ImageButton SelectImageGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_place);

        Spinner sp  = findViewById(R.id.spin_priceRange);
        ArrayAdapter<CharSequence> adp = ArrayAdapter.createFromResource(this,R.array.priceRange,android.R.layout.simple_spinner_item);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adp);

        Spinner spin = findViewById(R.id.spin_numberOfSeats);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.numberOfSeats,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);


    }

    public void backHome(View v){
        Intent in = new Intent(this, PlaceHome.class);
        startActivity(in);
    }

    public void selectThemePlace(View v){
        Intent in = new Intent(this, Theme.class);
        startActivity(in);
    }
}