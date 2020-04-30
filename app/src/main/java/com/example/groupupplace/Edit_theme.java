package com.example.groupupplace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Edit_theme extends AppCompatActivity {
    String pId = "", Name = "", Dest = "", Faci = "", Rating = "", UserId = "", Price = "", Phone = "", Seat = "", Deposite = "", Day = "", StartTime = "", EndTime = "", email = "";
    ArrayList<String> theme;
    private CheckBox cafe, coffee, river, karaoke, clubs, pub, wine, night, vegeterian, hotelBuf, rooftop, izakaya, dessert, alacarte, seafood, steak, iceCream, bakery,
            bbq, shabu, buffet, cleanFood, thaiBbq, pizza, sushi, burger, ramen, dimsum, vegan, veget, original, bar, outdoor, cozy, family, minimal, warm, child;
    Button btn_con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_theme);
        theme = new ArrayList<>();
        btn_con = findViewById(R.id.btn_confirmEditCustom);
        pId = getIntent().getStringExtra("ItemId");
        Name = getIntent().getStringExtra("ItemName");
        Dest = getIntent().getStringExtra("ItemDest");
        Faci = getIntent().getStringExtra("ItemFaci");//
        Rating = getIntent().getStringExtra("Rating");
        UserId = getIntent().getStringExtra("ItemUserId");
        Price = getIntent().getStringExtra("ItemPrice");
        Phone = getIntent().getStringExtra("ItemPhone");
        Seat = getIntent().getStringExtra("ItemSeat");
        Deposite = getIntent().getStringExtra("ItemDeposite");
        Day = getIntent().getStringExtra("ItemDay");//
        StartTime = getIntent().getStringExtra("ItemStartTime");
        EndTime = getIntent().getStringExtra("ItemEndTime");
        email = getIntent().getStringExtra("ItemUserEmail");
        Log.d("editplce", pId + " : " + Name + " : " + Dest + " : " + Faci + " : " + Rating + " : " + UserId + " : " + Price + " : " + Phone + " : " + Seat + " : " + Deposite + " : " + Day + " : " + StartTime + " : " + EndTime);

        theme = (ArrayList<String>) getIntent().getSerializableExtra("theme");
        Log.d("theme", "edit Start " + theme.toString());
        check();
        ShowIdelTheme(theme);

        btn_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmTheme();
            }
        });
    }

    public void ShowIdelTheme(ArrayList<String> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).equals("2131820591")){
                cafe.setChecked(true);
            }else if (arrayList.get(i).equals("2131820600")){
                coffee.setChecked(true);
            }else if (arrayList.get(i).equals("2131820742")){
                river.setChecked(true);
            }else if (arrayList.get(i).equals("2131820666")){
                karaoke.setChecked(true);
            }else if (arrayList.get(i).equals("2131820734")){
                clubs.setChecked(true);
            }else if (arrayList.get(i).equals("2131820599")){
                pub.setChecked(true);
            }else if (arrayList.get(i).equals("2131820784")){
                wine.setChecked(true);
            }else if (arrayList.get(i).equals("2131820668")){
                night.setChecked(true);
            }else if (arrayList.get(i).equals("2131820778")){
                vegeterian.setChecked(true);
            }else if (arrayList.get(i).equals("2131820657")){
                hotelBuf.setChecked(true);
            }else if (arrayList.get(i).equals("2131820743")){
                rooftop.setChecked(true);
            }else if (arrayList.get(i).equals("2131820664")){
                izakaya.setChecked(true);
            }else if (arrayList.get(i).equals("2131820630")){
                dessert.setChecked(true);
            }else if (arrayList.get(i).equals("2131820576")){
                alacarte.setChecked(true);
            }else if (arrayList.get(i).equals("2131820745")){
                seafood.setChecked(true);
            }else if (arrayList.get(i).equals("2131820764")){
                steak.setChecked(true);
            }else if (arrayList.get(i).equals("2131820658")){
                iceCream.setChecked(true);
            }else if (arrayList.get(i).equals("2131820582")){
                bakery.setChecked(true);
            }else if (arrayList.get(i).equals("2131820585")){
                bbq.setChecked(true);
            }else if (arrayList.get(i).equals("2131820757")){
                shabu.setChecked(true);
            }else if (arrayList.get(i).equals("2131820589")){
                buffet.setChecked(true);
            }else if (arrayList.get(i).equals("2131820597")){
                cleanFood.setChecked(true);
            }else if (arrayList.get(i).equals("2131820770")){
                thaiBbq.setChecked(true);
            }else if (arrayList.get(i).equals("2131820730")){
                pizza.setChecked(true);
            }else if (arrayList.get(i).equals("2131820767")){
                sushi.setChecked(true);
            }else if (arrayList.get(i).equals("2131820590")){
                burger.setChecked(true);
            }else if (arrayList.get(i).equals("2131820737")){
                ramen.setChecked(true);
            }else if (arrayList.get(i).equals("2131820633")){
                dimsum.setChecked(true);
            }else if (arrayList.get(i).equals("2131820777")){
                vegan.setChecked(true);
            }else if (arrayList.get(i).equals("2131820778")){
                veget.setChecked(true);
            }else if (arrayList.get(i).equals("2131820721")){
                original.setChecked(true);
            }else if (arrayList.get(i).equals("2131820583")){
                bar.setChecked(true);
            }else if (arrayList.get(i).equals("2131820722")){
                outdoor.setChecked(true);
            }else if (arrayList.get(i).equals("2131820620")){
                cozy.setChecked(true);
            }else if (arrayList.get(i).equals("2131820647")){
                family.setChecked(true);
            }else if (arrayList.get(i).equals("2131820673")){
                minimal.setChecked(true);
            }else if (arrayList.get(i).equals("2131820782")){
                warm.setChecked(true);
            }else if (arrayList.get(i).equals("2131820595")){
                child.setChecked(true);
            }

        }
    }

    public void backEditPlace(View v) {
        finish();
    }

    public void confirmTheme() {
        Extend_MyHelper.removeDuplicateValue(theme);
        Log.d("theme", theme.toString());
        Intent in = new Intent(Edit_theme.this, Edit_place.class);
        in.putExtra("ItemId", pId + "");
        in.putExtra("ItemName", Name + "");
        in.putExtra("ItemDest", Dest + "");
        in.putExtra("ItemFaci", Faci + "");
        in.putExtra("Rating", Rating + "");
        in.putExtra("ItemUserId", UserId + "");
        in.putExtra("ItemPrice", Price + "");
        in.putExtra("ItemPhone", Phone + "");
        in.putExtra("ItemSeat", Seat + "");
        in.putExtra("ItemDay", Day + "");
        in.putExtra("ItemDeposite", Deposite + "");
        in.putExtra("ItemStartTime", StartTime + "");
        in.putExtra("ItemEndTime", EndTime + "");
        in.putExtra("ItemUserEmail", email + "");
        in.putExtra("theme", theme);
        startActivity(in);
    }

    public void check() {
        cafe = findViewById(R.id.cafe);
        cafe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (cafe.isChecked()) {
                    cafe.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820591");
//                    Log.d("theme",
//                            cafe.getText()+"--"+R.string.cafe+"");

                } else {
                    cafe.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820591");
                }

            }
        });


        coffee = findViewById(R.id.coffee_tea);
        coffee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (coffee.isChecked()) {
                    coffee.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820600");
                } else {
                    coffee.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820600");
                }

            }
        });

        river = findViewById(R.id.riverside);
        river.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (river.isChecked()) {
                    river.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820742");
                } else {
                    river.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820742");
                }

            }
        });

        karaoke = findViewById(R.id.karaoke);
        karaoke.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (karaoke.isChecked()) {
                    karaoke.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820666");
                } else {
                    karaoke.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820666");
                }

            }
        });

        clubs = findViewById(R.id.clubs);
        clubs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (clubs.isChecked()) {
                    clubs.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820734");
                } else {
                    clubs.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820734");
                }

            }
        });

        pub = findViewById(R.id.pub);
        pub.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (pub.isChecked()) {
                    pub.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820599");
                } else {
                    pub.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820599");
                }

            }
        });

        wine = findViewById(R.id.wine_bar);
        wine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (wine.isChecked()) {
                    wine.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820784");
                } else {
                    wine.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820784");
                }

            }
        });

        night = findViewById(R.id.late_night_rice);
        night.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (night.isChecked()) {
                    night.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820668");
                } else {
                    night.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820668");
                }

            }
        });

        vegeterian = findViewById(R.id.vegeterian);
        vegeterian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (vegeterian.isChecked()) {
                    vegeterian.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820778");
                } else {
                    vegeterian.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820778");
                }

            }
        });

        hotelBuf = findViewById(R.id.hotel_buffet);
        hotelBuf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (hotelBuf.isChecked()) {
                    hotelBuf.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820657");
                } else {
                    hotelBuf.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820657");
                }

            }
        });

        rooftop = findViewById(R.id.rooftop);
        rooftop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (rooftop.isChecked()) {
                    rooftop.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820743");
                } else {
                    rooftop.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820743");
                }

            }
        });

        izakaya = findViewById(R.id.izakaya);
        izakaya.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (izakaya.isChecked()) {
                    izakaya.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820664");
                } else {
                    izakaya.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820664");
                }

            }
        });

        dessert = findViewById(R.id.dessert);
        dessert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (dessert.isChecked()) {
                    dessert.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820630");
                } else {
                    dessert.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820630");
                }

            }
        });

        alacarte = findViewById(R.id.alacarte);
        alacarte.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (alacarte.isChecked()) {
                    alacarte.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820576");
                } else {
                    alacarte.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820576");
                }

            }
        });

        seafood = findViewById(R.id.seafood);
        seafood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (seafood.isChecked()) {
                    seafood.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820745");
                } else {
                    seafood.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820745");
                }

            }
        });

        steak = findViewById(R.id.steak);
        steak.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (steak.isChecked()) {
                    steak.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820764");
                } else {
                    steak.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820764");
                }

            }
        });

        iceCream = findViewById(R.id.iceCream);
        iceCream.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (iceCream.isChecked()) {
                    iceCream.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820658");
                } else {
                    iceCream.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820658");
                }

            }
        });

        bakery = findViewById(R.id.bakery_cake);
        bakery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (bakery.isChecked()) {
                    bakery.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820582");
                } else {
                    bakery.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820582");
                }

            }
        });

        bbq = findViewById(R.id.bbq);
        bbq.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (bbq.isChecked()) {
                    bbq.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820585");
                } else {
                    bbq.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820585");
                }

            }
        });

        shabu = findViewById(R.id.shabu);
        shabu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (shabu.isChecked()) {
                    shabu.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820757");
                } else {
                    shabu.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820757");
                }

            }
        });

        buffet = findViewById(R.id.buffet);
        buffet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (buffet.isChecked()) {
                    buffet.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820589");
                } else {
                    buffet.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820589");
                }

            }
        });

        cleanFood = findViewById(R.id.cleanFood);
        cleanFood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (cleanFood.isChecked()) {
                    cleanFood.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820597");
                } else {
                    cleanFood.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820597");
                }

            }
        });

        thaiBbq = findViewById(R.id.thai_bbq);
        thaiBbq.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (thaiBbq.isChecked()) {
                    thaiBbq.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820770");
                } else {
                    thaiBbq.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820770");
                }

            }
        });

        pizza = findViewById(R.id.pizza);
        pizza.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (pizza.isChecked()) {
                    pizza.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820730");
                } else {
                    pizza.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820730");
                }

            }
        });

        sushi = findViewById(R.id.sushi);
        sushi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (sushi.isChecked()) {
                    sushi.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820767");
                } else {
                    sushi.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820767");
                }

            }
        });

        burger = findViewById(R.id.burger);
        burger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (burger.isChecked()) {
                    burger.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820590");
                } else {
                    burger.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820590");
                }

            }
        });

        ramen = findViewById(R.id.ramen);
        ramen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (ramen.isChecked()) {
                    ramen.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820737");
                } else {
                    ramen.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820737");
                }

            }
        });

        dimsum = findViewById(R.id.dimsum);
        dimsum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (dimsum.isChecked()) {
                    dimsum.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820633");
                } else {
                    dimsum.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820633");
                }

            }
        });

        vegan = findViewById(R.id.vegan);
        vegan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (vegan.isChecked()) {
                    vegan.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820777");
                } else {
                    vegan.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820777");
                }

            }
        });

        veget = findViewById(R.id.vegeterian2);
        veget.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (veget.isChecked()) {
                    veget.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820778");
                } else {
                    veget.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820778");
                }

            }
        });

        original = findViewById(R.id.original);
        original.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (original.isChecked()) {
                    original.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820721");
                } else {
                    original.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820721");
                }

            }
        });

        bar = findViewById(R.id.bar);
        bar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (bar.isChecked()) {
                    bar.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820583");
                } else {
                    bar.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820583");
                }

            }
        });

        outdoor = findViewById(R.id.outdoor);
        outdoor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (outdoor.isChecked()) {
                    outdoor.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820722");
                } else {
                    outdoor.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820722");
                }

            }
        });

        cozy = findViewById(R.id.cozy);
        cozy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (cozy.isChecked()) {
                    cozy.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820620");
                } else {
                    cozy.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820620");
                }

            }
        });

        family = findViewById(R.id.family);
        family.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (family.isChecked()) {
                    family.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820647");
                } else {
                    family.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820647");
                }

            }
        });

        minimal = findViewById(R.id.minimal);
        minimal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (minimal.isChecked()) {
                    minimal.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820673");
                } else {
                    minimal.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820673");
                }

            }
        });

        warm = findViewById(R.id.warm);
        warm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (warm.isChecked()) {
                    warm.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820782");
//                    Log.d("box",warm+"");
                } else {
                    warm.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820782");
                }

            }
        });

        child = findViewById(R.id.child);
        child.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (child.isChecked()) {
                    child.setBackgroundResource(R.color.blueWhite);
                    theme.add("2131820595");
//                    Log.d("box",child+"");
                } else {
                    child.setBackgroundResource(R.drawable.frame);
                    removeTheme("2131820595");
                }

            }
        });
//        Log.d("theme", theme.toString());

    }

    public void removeTheme(String id) {
        String number = "";
        for (int i = 0; i < theme.size(); i++) {
            if (id.equals(theme.get(i))) {
                number = i + "";
            }
        }
        theme.remove(Integer.parseInt(number));
        Log.d("theme", "Remove : " + theme.toString());
    }
}
