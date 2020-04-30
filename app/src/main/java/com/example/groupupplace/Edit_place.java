package com.example.groupupplace;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Edit_place extends AppCompatActivity {
    String pId = "", Name = "", Dest = "", Faci = "", Rating = "", UserId = "", Price = "", Phone = "", Seat = "", Deposite = "", Day = "", StartTime = "", EndTime = "";
    Edit_place.ResponseStr responseStr = new Edit_place.ResponseStr();
    ArrayList<HashMap<String, String>> placeImage, placeTheme;
    EditText edtName, edtDetail, edtPhone;
    Spinner sp_price, sp_seat;
    Button btn_calendar, btn_theme, btn_time;
    ImageView img1, img2, img3, img4, img5;
    CheckBox cb_park, cb_wifi, cb_Credit, cb_kid, cb_air, cb_priRoom, cb_bts, cb_mrt;
    TextView showTime;
    Switch sw_depo;
    String[] some_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_place);
        edtName = findViewById(R.id.edt_editname);
        edtDetail = findViewById(R.id.edt_editdetails);
        edtPhone = findViewById(R.id.edt_editphone);
        btn_calendar = findViewById(R.id.btn_editCalen);
        btn_theme = findViewById(R.id.btn_editTheme);
        sp_price = findViewById(R.id.spin_EditpriceRange);
        btn_time = findViewById(R.id.btn_editselect_time);
        showTime = findViewById(R.id.editshow_time);
        img1 = findViewById(R.id.edit_image1);
        img2 = findViewById(R.id.edit_image2);
        img3 = findViewById(R.id.edit_image3);
        img4 = findViewById(R.id.edit_image4);
        img5 = findViewById(R.id.edit_image5);
        cb_park = findViewById(R.id.cb_editparking);
        cb_wifi = findViewById(R.id.cb_editwifi);
        cb_Credit = findViewById(R.id.cb_editcreditCard);
        cb_kid = findViewById(R.id.cb_editgoodForKids);
        cb_air = findViewById(R.id.cb_editairConditioning);
        cb_priRoom = findViewById(R.id.cb_editprivateRoom);
        cb_bts = findViewById(R.id.cb_editbts);
        cb_mrt = findViewById(R.id.cb_editmrt);
        sp_seat = findViewById(R.id.spin_EditnumberOfSeats);
        sw_depo = findViewById(R.id.sw_editdeposit);
        some_array = getResources().getStringArray(R.array.day);
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
        placeImage = new ArrayList<>();
        placeTheme = new ArrayList<>();
        Log.d("editplce", pId + " : " + Name + " : " + Dest + " : " + Faci + " : " + Rating + " : " + UserId + " : " + Price + " : " + Phone + " : " + Seat + " : " + Deposite + " : " + Day + " : " + StartTime + " : " + EndTime);
        getPlacePhotoPid();
        getPlaceTheme();
        edtName.setText(Name);
        edtDetail.setText(Dest);
        edtPhone.setText(Phone);
        String day = "";
        StringTokenizer stD = new StringTokenizer(Day, ":");
        while (stD.hasMoreTokens()) {
            day += checkDay(stD.nextToken()) + " ";
        }
        day += "เวลา " + StartTime + "-" + EndTime;
        showTime.setText(day);
        showFacilityDay(Faci);

        ArrayAdapter<CharSequence> adp = ArrayAdapter.createFromResource(this, R.array.priceRange, android.R.layout.simple_spinner_item);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_price.setAdapter(adp);
        sp_price.setSelection(Integer.parseInt(Price));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numberOfSeats, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_seat.setAdapter(adapter);
        sp_seat.setSelection(Integer.parseInt(Seat));
        if (Integer.parseInt(Deposite) == 1) {
            Log.d("placedeposit", Deposite);//on
            sw_depo.setChecked(true);
        } else if (Integer.parseInt(Deposite) == 0) {
            Log.d("placedeposit", Deposite);//off
            sw_depo.setChecked(false);
        }


    }

    public void backHome(View v) {
        finish();
    }

    public void getPlacePhotoPid() {
        responseStr = new Edit_place.ResponseStr();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getplacephotobypid.php";
        url += "?sId=" + pId;
        Log.d("position", "stringRequest  " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> map;
                            JSONArray data = new JSONArray(response.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("photoplace_id", c.getString("photoplace_id"));
                                map.put("place_id", c.getString("place_id"));
                                map.put("photoplace_path", c.getString("photoplace_path"));
                                map.put("place_upid", c.getString("place_upid"));
                                MyArrList.add(map);
                                placeImage.add(map);

                            }
                            Log.d("editplce", placeImage.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
        new CountDownTimer(300, 300) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                new Extend_MyHelper.SendHttpRequestTask(placeImage.get(0).get("photoplace_path"), img1, 350).execute();
                new Extend_MyHelper.SendHttpRequestTask(placeImage.get(1).get("photoplace_path"), img2, 350).execute();
                new Extend_MyHelper.SendHttpRequestTask(placeImage.get(2).get("photoplace_path"), img3, 350).execute();
                new Extend_MyHelper.SendHttpRequestTask(placeImage.get(3).get("photoplace_path"), img4, 350).execute();
                new Extend_MyHelper.SendHttpRequestTask(placeImage.get(4).get("photoplace_path"), img5, 350).execute();
            }
        }.start();
    }

    public void getPlaceTheme() {
        responseStr = new Edit_place.ResponseStr();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getplaceThemebypid.php";
        url += "?sId=" + pId;
        Log.d("position", "stringRequest  " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> map;
                            JSONArray data = new JSONArray(response.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("placetheme_id", c.getString("placetheme_id"));
                                map.put("theme_id", c.getString("theme_id"));
                                map.put("place_id", c.getString("place_id"));
                                map.put("place_upid", c.getString("place_upid"));
                                MyArrList.add(map);
                                placeTheme.add(map);
                            }
//                            Log.d("position", MyArrList.size() + "");
                            Log.d("placeHome", "get placeTheme " + placeTheme.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    public String checkDay(String d) {
        String day = "";
        day = some_array[Integer.parseInt(d)];
        return day;
    }
    public void showFacilityDay(String d) {
        StringTokenizer st = new StringTokenizer(d,":");
        while (st.hasMoreTokens()){
            String s="";
            s=st.nextToken();
            if (Integer.parseInt(s)==1){
                cb_park.setChecked(true);
            }else if (Integer.parseInt(s)==2){
                cb_wifi.setChecked(true);
            }else if (Integer.parseInt(s)==3){
                cb_Credit.setChecked(true);
            }else if (Integer.parseInt(s)==4){
                cb_kid.setChecked(true);
            }else if (Integer.parseInt(s)==5){
                cb_air.setChecked(true);
            }else if (Integer.parseInt(s)==6){
                cb_priRoom.setChecked(true);
            }else if (Integer.parseInt(s)==7){
                cb_bts.setChecked(true);
            }else if (Integer.parseInt(s)==8){
                cb_mrt.setChecked(true);
            }

        }
    }

    public class ResponseStr {
        private String str;
        JSONArray jsonArray;

        public void setValue(JSONArray jsonArr) {
            this.jsonArray = jsonArr;
        }

    }
}
