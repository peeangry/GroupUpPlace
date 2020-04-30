package com.example.groupupplace;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class Edit_place extends AppCompatActivity {
    String pId = "", Name = "", Dest = "", Faci = "", Rating = "", UserId = "", Price = "", Phone = "", Seat = "", Deposite = "", Day = "", StartTime = "", EndTime = "";
    Edit_place.ResponseStr responseStr = new Edit_place.ResponseStr();
    ArrayList<HashMap<String, String>> placeImage;
    EditText edtName, edtDetail, edtPhone;
    Spinner sp_price, sp_seat;
    Button btn_calendar, btn_theme, btn_time;
    ImageView img1, img2, img3, img4, img5;
    CheckBox cb_park, cb_wifi, cb_Credit, cb_kid, cb_air, cb_priRoom, cb_bts, cb_mrt;
    TextView showTime;
    Switch sw_depo;

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

        pId = getIntent().getStringExtra("ItemId");
        Name = getIntent().getStringExtra("ItemName");
        Dest = getIntent().getStringExtra("ItemDest");
        Faci = getIntent().getStringExtra("ItemFaci");
        Rating = getIntent().getStringExtra("Rating");
        UserId = getIntent().getStringExtra("ItemUserId");
        Price = getIntent().getStringExtra("ItemPrice");
        Phone = getIntent().getStringExtra("ItemPhone");
        Seat = getIntent().getStringExtra("ItemSeat");
        Deposite = getIntent().getStringExtra("ItemDeposite");
        Day = getIntent().getStringExtra("ItemDay");
        StartTime = getIntent().getStringExtra("ItemStartTime");
        EndTime = getIntent().getStringExtra("ItemEndTime");
        placeImage = new ArrayList<>();
        Log.d("editplce", pId + " : " + Name + " : " + Dest + " : " + Faci + " : " + Rating + " : " + UserId + " : " + Price + " : " + Phone + " : " + Seat + " : " + Deposite + " : " + Day + " : " + StartTime + " : " + EndTime);
        edtName.setText(Name);
        edtDetail.setText(Dest);
        getPlacePhotoPid();
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
    }

    public class ResponseStr {
        private String str;
        JSONArray jsonArray;

        public void setValue(JSONArray jsonArr) {
            this.jsonArray = jsonArr;
        }

    }
}
