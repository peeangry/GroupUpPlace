package com.example.groupupplace;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.net.ssl.HttpsURLConnection;

public class Edit_place extends AppCompatActivity {
    String pId = "", Name = "", Dest = "", Faci = "", Rating = "", UserId = "", Price = "", Phone = "", Seat = "", Deposite = "", Day = "", StartTime = "", EndTime = "", email = "";
    Edit_place.ResponseStr responseStr = new Edit_place.ResponseStr();
    ArrayList<HashMap<String, String>> placeImage, placeTheme;
    EditText edtName, edtDetail, edtPhone;
    Spinner sp_price, sp_seat;
    Button btn_calendar, btn_theme, btn_time, btn_con;
    ImageView img1, img2, img3, img4, img5;
    CheckBox cb_park, cb_wifi, cb_Credit, cb_kid, cb_air, cb_priRoom, cb_bts, cb_mrt;
    TextView showTime;
    Switch sw_depo;
    boolean check = true;
    Bitmap bitmap, bitmap2, bitmap3, bitmap4, bitmap5;
    ArrayList<String> facility;
    ArrayList<String> theme;
    String[] some_array;
    ArrayList<String> date;
    String dt_timeOpen, dt_timeEnd;
    final int READ_EXTERNAL_PERMISSION_CODE = 1;
    ProgressDialog progressDialog;
    String ServerUploadPath = "http://www.groupupdb.com/android/editplace.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_place);
        Extend_MyHelper.checkInternetLost(this);
        Extend_MyHelper.deleteCache(this);
        edtName = findViewById(R.id.edt_editname);
        edtDetail = findViewById(R.id.edt_editdetails);
        edtPhone = findViewById(R.id.edt_editphone);
        btn_calendar = findViewById(R.id.btn_editCalen);
        btn_theme = findViewById(R.id.btn_editTheme);
        sp_price = findViewById(R.id.spin_EditpriceRange);
        btn_time = findViewById(R.id.btn_editselect_time);
        btn_con = findViewById(R.id.editPlace_confirm);
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
        placeImage = new ArrayList<>();
        placeTheme = new ArrayList<>();
        date = new ArrayList<>();
        facility = new ArrayList<>();
        theme = new ArrayList<>();
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
        theme = (ArrayList<String>) getIntent().getSerializableExtra("theme");
        if (theme != null) {
            Log.d("theme", "back select theme " + theme.toString()); // theme use for db
        } else {
            getPlaceTheme();
        }

        dt_timeOpen = "";
        dt_timeEnd = "";
        getPlacePhotoPid();
        Log.d("editplce", pId + " : " + Name + " : " + Dest + " : " + Faci + " : " + Rating + " : " + UserId + " : " + Price + " : " + Phone + " : " + Seat + " : " + Deposite + " : " + Day + " : " + StartTime + " : " + EndTime);


        edtName.setText(Name);
        edtDetail.setText(Dest);
        edtPhone.setText(Phone);
        Log.d("facility124", Faci);
        String day = "";
        checkFacility();
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
        sw_depo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Deposite = "1";
                    Log.d("placedeposit", Deposite);//on
                } else {
                    Deposite = "0";
                    Log.d("placedeposit", Deposite);//off
                }
            }
        });
        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Edit_place.this, ManageCalendar.class);
                in.putExtra("id", UserId + "");
                in.putExtra("email", email + "");
                in.putExtra("pid", pId + "");
                startActivity(in);
            }
        });
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker();
            }
        });
        btn_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectThemePlace();
            }
        });
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage(1);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage(2);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage(3);
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage(4);
            }
        });
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage(5);
            }
        });
        btn_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUploadToServerFunction();
            }
        });
    }

    public void selectThemePlace() {
        Log.d("theme", theme.toString());//off
        Intent in = new Intent(Edit_place.this, Edit_theme.class);
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
        Log.d("editplce", pId + " : " + Name + " : " + Dest + " : " + Faci + " : " + Rating + " : " + UserId + " : " + Price + " : " + Phone + " : " + Seat + " : " + Deposite + " : " + Day + " : " + StartTime + " : " + EndTime);

        startActivity(in);
        //ตอนส่งกลับใช้ finish
    }

    public void backHome(View v) {
        finish();
    }

    public void timePicker() {
        final AlertDialog viewTime = new AlertDialog.Builder(Edit_place.this).create();
        View mView = getLayoutInflater().inflate(R.layout.layout_showtimeopen_dialog, null);
        final EditText edt_timeOne = mView.findViewById(R.id.edt_timeOne);
        final EditText edt_timeTwo = mView.findViewById(R.id.edt_timeTwo);
        final Button btn_condate = mView.findViewById(R.id.btn_confirmdate);
        final CheckBox everyday = mView.findViewById(R.id.everyday);
        final CheckBox monfri = mView.findViewById(R.id.monFri);
        final CheckBox satsun = mView.findViewById(R.id.satSun);
        final CheckBox mon = mView.findViewById(R.id.mon);
        final CheckBox tue = mView.findViewById(R.id.tue);
        final CheckBox wed = mView.findViewById(R.id.wed);
        final CheckBox thu = mView.findViewById(R.id.thu);
        final CheckBox fri = mView.findViewById(R.id.fri);
        final CheckBox sat = mView.findViewById(R.id.sat);
        final CheckBox sun = mView.findViewById(R.id.sun);
        edt_timeOne.setFocusable(false);
        edt_timeTwo.setFocusable(false);
        edt_timeOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Edit_place.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edt_timeOne.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        edt_timeTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Edit_place.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edt_timeTwo.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        viewTime.setView(mView);
        viewTime.show();
        everyday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (everyday.isChecked()) {
                    everyday.setBackgroundResource(R.color.blueWhite);
                    date.add("10");
                    enableCheckBox(mon, tue, wed, thu, fri, sat, sun, monfri, satsun, false);
                } else {
                    everyday.setBackgroundResource(R.drawable.my_style);
                    removeDate("10");
                    enableCheckBox(mon, tue, wed, thu, fri, sat, sun, monfri, satsun, true);
                }

            }
        });
        monfri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (monfri.isChecked()) {
                    monfri.setBackgroundResource(R.color.blueWhite);
                    date.add("9");
                    enableCheckBox(mon, tue, wed, thu, fri, sat, sun, everyday, satsun, false);
                } else {
                    monfri.setBackgroundResource(R.drawable.my_style);
                    removeDate("9");
                    enableCheckBox(mon, tue, wed, thu, fri, sat, sun, everyday, satsun, true);
                }

            }
        });
        satsun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (satsun.isChecked()) {
                    satsun.setBackgroundResource(R.color.blueWhite);
                    date.add("8");
                    enableCheckBox(mon, tue, wed, thu, fri, sat, sun, everyday, monfri, false);
                } else {
                    satsun.setBackgroundResource(R.drawable.my_style);
                    removeDate("8");
                    enableCheckBox(mon, tue, wed, thu, fri, sat, sun, everyday, monfri, true);
                }

            }
        });
        mon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (mon.isChecked()) {
                    mon.setBackgroundResource(R.color.blueWhite);
                    date.add("1");
                    enableCheckBox(everyday, monfri, satsun, false);
                } else {
                    mon.setBackgroundResource(R.drawable.my_style);
                    removeDate("1");
                    if (date.isEmpty()) {
                        enableCheckBox(everyday, monfri, satsun, true);
                    } else {

                    }
                }

            }
        });
        tue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (tue.isChecked()) {
                    tue.setBackgroundResource(R.color.blueWhite);
                    date.add("2");
                    enableCheckBox(everyday, monfri, satsun, false);
                } else {
                    tue.setBackgroundResource(R.drawable.my_style);
                    removeDate("2");
                    if (date.isEmpty()) {
                        enableCheckBox(everyday, monfri, satsun, true);
                    } else {

                    }
                }

            }
        });
        wed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (wed.isChecked()) {
                    wed.setBackgroundResource(R.color.blueWhite);
                    date.add("3");
                    enableCheckBox(everyday, monfri, satsun, false);
                } else {
                    wed.setBackgroundResource(R.drawable.my_style);
                    removeDate("3");
                    if (date.isEmpty()) {
                        enableCheckBox(everyday, monfri, satsun, true);
                    } else {

                    }
                }

            }
        });
        thu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (thu.isChecked()) {
                    thu.setBackgroundResource(R.color.blueWhite);
                    date.add("4");
                    enableCheckBox(everyday, monfri, satsun, false);
                } else {
                    thu.setBackgroundResource(R.drawable.my_style);
                    removeDate("4");
                    if (date.isEmpty()) {
                        enableCheckBox(everyday, monfri, satsun, true);
                    } else {

                    }
                }

            }
        });
        fri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (fri.isChecked()) {
                    fri.setBackgroundResource(R.color.blueWhite);
                    date.add("5");
                    enableCheckBox(everyday, monfri, satsun, false);
                } else {
                    fri.setBackgroundResource(R.drawable.my_style);
                    removeDate("5");
                    if (date.isEmpty()) {
                        enableCheckBox(everyday, monfri, satsun, true);
                    } else {

                    }
                }

            }
        });
        sat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (sat.isChecked()) {
                    sat.setBackgroundResource(R.color.blueWhite);
                    date.add("6");
                    enableCheckBox(everyday, monfri, satsun, false);
                } else {
                    sat.setBackgroundResource(R.drawable.my_style);
                    removeDate("6");
                    if (date.isEmpty()) {
                        enableCheckBox(everyday, monfri, satsun, true);
                    } else {

                    }
                }

            }
        });
        sun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (sun.isChecked()) {
                    sun.setBackgroundResource(R.color.blueWhite);
                    date.add("7");
                    enableCheckBox(everyday, monfri, satsun, false);
                } else {
                    sun.setBackgroundResource(R.drawable.my_style);
                    removeDate("7");
                    if (date.isEmpty()) {
                        enableCheckBox(everyday, monfri, satsun, true);
                    } else {

                    }

                }

            }
        });
        btn_condate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Extend_MyHelper.removeDuplicateValue(date);
                showTime.setVisibility(View.VISIBLE);
                dt_timeOpen = edt_timeOne.getText().toString();
                dt_timeEnd = edt_timeTwo.getText().toString();
                if (dt_timeOpen.isEmpty() || dt_timeEnd.isEmpty()) {
                    final AlertDialog viewDetail = new AlertDialog.Builder(Edit_place.this).create();
                    viewDetail.setTitle("กรุณากำหนดเวลาเปิด-ปิด");
                    viewDetail.setButton(viewDetail.BUTTON_POSITIVE, "เสร็จสิ้น", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewDetail.dismiss();
                        }
                    });
                    viewDetail.show();
                    Button btnPositive = viewDetail.getButton(AlertDialog.BUTTON_POSITIVE);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                    layoutParams.weight = 15;
                    btnPositive.setLayoutParams(layoutParams);
                } else {
                    showTime.setText(showStringDay(date) + "เวลา " + dt_timeOpen + " - " + dt_timeEnd);
                    btn_time.setVisibility(View.GONE);
                    viewTime.dismiss();
                }

            }
        });

    }

    public void removeDate(String id) {
        String number = "";
        for (int i = 0; i < date.size(); i++) {
            if (id.equals(date.get(i))) {
                number = i + "";
            }
        }
        date.remove(Integer.parseInt(number));
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
                if (placeImage.size() == 0) {

                } else {
                    new Extend_MyHelper.SendHttpRequestTask(placeImage.get(0).get("photoplace_path"), img1, 350).execute();
                    new Extend_MyHelper.SendHttpRequestTask(placeImage.get(1).get("photoplace_path"), img2, 350).execute();
                    new Extend_MyHelper.SendHttpRequestTask(placeImage.get(2).get("photoplace_path"), img3, 350).execute();
                    new Extend_MyHelper.SendHttpRequestTask(placeImage.get(3).get("photoplace_path"), img4, 350).execute();
                    new Extend_MyHelper.SendHttpRequestTask(placeImage.get(4).get("photoplace_path"), img5, 350).execute();
                }
            }
        }.start();
    }

    public void getPlaceTheme() {
        theme = new ArrayList<>();
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
        new CountDownTimer(300, 300) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                for (int i = 0; i < placeTheme.size(); i++) {
                    theme.add(placeTheme.get(i).get("theme_id").toString());
                }
                Log.d("placeHome", "theme " + theme.toString());
            }
        }.start();
    }

    public String checkDay(String d) {
        String day = "";
        day = some_array[Integer.parseInt(d)];
        return day;
    }

    public void showFacilityDay(String d) {
        StringTokenizer st = new StringTokenizer(d, ":");
        while (st.hasMoreTokens()) {
            String s = st.nextToken();
//            facility.add(s);
            if (Integer.parseInt(s) == 1) {
                cb_park.setChecked(true);
            } else if (Integer.parseInt(s) == 2) {
                cb_wifi.setChecked(true);
            } else if (Integer.parseInt(s) == 3) {
                cb_Credit.setChecked(true);
            } else if (Integer.parseInt(s) == 4) {
                cb_kid.setChecked(true);
            } else if (Integer.parseInt(s) == 5) {
                cb_air.setChecked(true);
            } else if (Integer.parseInt(s) == 6) {
                cb_priRoom.setChecked(true);
            } else if (Integer.parseInt(s) == 7) {
                cb_bts.setChecked(true);
            } else if (Integer.parseInt(s) == 8) {
                cb_mrt.setChecked(true);
            }
        }
        while (st.hasMoreTokens()) {
            facility.add(st.nextToken());
        }
        Log.d("facility124", "start : " + facility.toString());
    }

    public class ResponseStr {
        private String str;
        JSONArray jsonArray;

        public void setValue(JSONArray jsonArr) {
            this.jsonArray = jsonArr;
        }

    }

    public void checkFacility() {
        cb_park.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (cb_park.isChecked()) {
                    cb_park.setBackgroundResource(R.color.blueWhite);
                    facility.add("1");
                } else {
                    cb_park.setBackgroundResource(R.drawable.my_style);
                    removeFacility("1");
                }

            }
        });
        cb_wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (cb_wifi.isChecked()) {
                    cb_wifi.setBackgroundResource(R.color.blueWhite);
                    facility.add("2");
                } else {
                    cb_wifi.setBackgroundResource(R.drawable.my_style);
                    removeFacility("2");
                }

            }
        });
        cb_Credit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (cb_Credit.isChecked()) {
                    cb_Credit.setBackgroundResource(R.color.blueWhite);
                    facility.add("3");
                } else {
                    cb_Credit.setBackgroundResource(R.drawable.my_style);
                    removeFacility("3");
                }

            }
        });
        cb_kid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (cb_kid.isChecked()) {
                    cb_kid.setBackgroundResource(R.color.blueWhite);
                    facility.add("4");
                } else {
                    cb_kid.setBackgroundResource(R.drawable.my_style);
                    removeFacility("4");
                }

            }
        });
        cb_air.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (cb_air.isChecked()) {
                    cb_air.setBackgroundResource(R.color.blueWhite);
                    facility.add("5");
                } else {
                    cb_air.setBackgroundResource(R.drawable.my_style);
                    removeFacility("5");
                }

            }
        });
        cb_priRoom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (cb_priRoom.isChecked()) {
                    cb_priRoom.setBackgroundResource(R.color.blueWhite);
                    facility.add("6");
                } else {
                    cb_priRoom.setBackgroundResource(R.drawable.my_style);
                    removeFacility("6");
                }

            }
        });
        cb_bts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (cb_bts.isChecked()) {
                    cb_bts.setBackgroundResource(R.color.blueWhite);
                    facility.add("7");
                } else {
                    cb_bts.setBackgroundResource(R.drawable.my_style);
                    removeFacility("7");
                }
            }
        });
        cb_mrt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (cb_mrt.isChecked()) {
                    cb_mrt.setBackgroundResource(R.color.blueWhite);
                    facility.add("8");
                } else {
                    cb_mrt.setBackgroundResource(R.drawable.my_style);
                    removeFacility("8");
                }
            }
        });

    }

    public void removeFacility(String id) {
        String number = "";
        for (int i = 0; i < facility.size(); i++) {
            if (id.equals(facility.get(i))) {
                number = i + "";
            }
        }
        facility.remove(Integer.parseInt(number));
        Log.d("facility124", facility.toString());
    }

    public void enableCheckBox(CheckBox cb, CheckBox cb1, CheckBox cb2, CheckBox cb3, CheckBox cb4, CheckBox cb5, CheckBox cb6, CheckBox cb7, CheckBox cb8, boolean b) {
        cb.setEnabled(b);
        cb1.setEnabled(b);
        cb2.setEnabled(b);
        cb3.setEnabled(b);
        cb4.setEnabled(b);
        cb5.setEnabled(b);
        cb6.setEnabled(b);
        cb7.setEnabled(b);
        cb8.setEnabled(b);

    }

    public void enableCheckBox(CheckBox cb, CheckBox cb1, CheckBox cb2, boolean b) {
        cb.setEnabled(b);
        cb1.setEnabled(b);
        cb2.setEnabled(b);
    }

    public String showStringDay(ArrayList<String> date) {
        String day = "";
        for (int i = 0; i < date.size(); i++) {
            if (Integer.parseInt(date.get(i)) == 1) {
                day += "จันทร์ ";

            } else if (Integer.parseInt(date.get(i)) == 2) {
                day += "อังคาร ";
            } else if (Integer.parseInt(date.get(i)) == 3) {
                day += "พุธ ";
            } else if (Integer.parseInt(date.get(i)) == 4) {
                day += "พฤหัสบดี ";
            } else if (Integer.parseInt(date.get(i)) == 5) {
                day += "ศุกร์ ";
            } else if (Integer.parseInt(date.get(i)) == 6) {
                day += "เสาร์ ";
            } else if (Integer.parseInt(date.get(i)) == 7) {
                day += "อาทิตย์ ";
            } else if (Integer.parseInt(date.get(i)) == 8) {
                day += "วันเสาร์อาทิตย์ ";
            } else if (Integer.parseInt(date.get(i)) == 9) {
                day += "จันทร์ - ศุกร์ ";
            } else if (Integer.parseInt(date.get(i)) == 10) {
                day += "ทุกวัน ";
            }
        }
        return day;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                Bitmap dstBmp;
                if (bitmap.getWidth() >= bitmap.getHeight()) {

                    dstBmp = Bitmap.createBitmap(
                            bitmap,
                            bitmap.getWidth() / 2 - bitmap.getHeight() / 2,
                            0,
                            bitmap.getHeight(),
                            bitmap.getHeight()
                    );

                } else {

                    dstBmp = Bitmap.createBitmap(
                            bitmap,
                            0,
                            bitmap.getHeight() / 2 - bitmap.getWidth() / 2,
                            bitmap.getWidth(),
                            bitmap.getWidth()
                    );
                }
                Bitmap lbp = scaleDown(dstBmp, 275, false);

                img1.setImageBitmap(lbp);


            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        if (RC == 2 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                Bitmap dstBmp;
                if (bitmap2.getWidth() >= bitmap2.getHeight()) {

                    dstBmp = Bitmap.createBitmap(
                            bitmap2,
                            bitmap2.getWidth() / 2 - bitmap2.getHeight() / 2,
                            0,
                            bitmap2.getHeight(),
                            bitmap2.getHeight()
                    );

                } else {

                    dstBmp = Bitmap.createBitmap(
                            bitmap2,
                            0,
                            bitmap2.getHeight() / 2 - bitmap2.getWidth() / 2,
                            bitmap2.getWidth(),
                            bitmap2.getWidth()
                    );
                }
                Bitmap lbp = scaleDown(dstBmp, 275, false);

                img2.setImageBitmap(lbp);


            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        if (RC == 3 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                Bitmap dstBmp;
                if (bitmap3.getWidth() >= bitmap3.getHeight()) {

                    dstBmp = Bitmap.createBitmap(
                            bitmap3,
                            bitmap3.getWidth() / 2 - bitmap3.getHeight() / 2,
                            0,
                            bitmap3.getHeight(),
                            bitmap3.getHeight()
                    );

                } else {

                    dstBmp = Bitmap.createBitmap(
                            bitmap3,
                            0,
                            bitmap3.getHeight() / 2 - bitmap3.getWidth() / 2,
                            bitmap3.getWidth(),
                            bitmap3.getWidth()
                    );
                }
                Bitmap lbp = scaleDown(dstBmp, 275, false);

                img3.setImageBitmap(lbp);


            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        if (RC == 4 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                bitmap4 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                Bitmap dstBmp;
                if (bitmap4.getWidth() >= bitmap4.getHeight()) {

                    dstBmp = Bitmap.createBitmap(
                            bitmap4,
                            bitmap4.getWidth() / 2 - bitmap4.getHeight() / 2,
                            0,
                            bitmap4.getHeight(),
                            bitmap4.getHeight()
                    );

                } else {

                    dstBmp = Bitmap.createBitmap(
                            bitmap4,
                            0,
                            bitmap4.getHeight() / 2 - bitmap4.getWidth() / 2,
                            bitmap4.getWidth(),
                            bitmap4.getWidth()
                    );
                }
                Bitmap lbp = scaleDown(dstBmp, 275, false);

                img4.setImageBitmap(lbp);


            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        if (RC == 5 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                bitmap5 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                Bitmap dstBmp;
                if (bitmap5.getWidth() >= bitmap5.getHeight()) {

                    dstBmp = Bitmap.createBitmap(
                            bitmap5,
                            bitmap5.getWidth() / 2 - bitmap5.getHeight() / 2,
                            0,
                            bitmap5.getHeight(),
                            bitmap5.getHeight()
                    );

                } else {

                    dstBmp = Bitmap.createBitmap(
                            bitmap5,
                            0,
                            bitmap5.getHeight() / 2 - bitmap5.getWidth() / 2,
                            bitmap5.getWidth(),
                            bitmap5.getWidth()
                    );
                }
                Bitmap lbp = scaleDown(dstBmp, 275, false);

                img5.setImageBitmap(lbp);


            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public void requestImagePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed for access the gallery")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Edit_place.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_PERMISSION_CODE);
                        }
                    }).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_PERMISSION_CODE);
        }
    }

    public void getImage(int reqCode) {
        if (ContextCompat.checkSelfPermission(Edit_place.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(Edit_place.this, "You have already permission access gallery", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("email", email + "");
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), reqCode);
        } else {
            requestImagePermission();
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), reqCode);
        }
    }

    public void ImageUploadToServerFunction() {

        ByteArrayOutputStream byteArrayOutputStreamObject, byteArrayOutputStreamObject2, byteArrayOutputStreamObject3, byteArrayOutputStreamObject4, byteArrayOutputStreamObject5;
        byteArrayOutputStreamObject = new ByteArrayOutputStream();
        byteArrayOutputStreamObject2 = new ByteArrayOutputStream();
        byteArrayOutputStreamObject3 = new ByteArrayOutputStream();
        byteArrayOutputStreamObject4 = new ByteArrayOutputStream();
        byteArrayOutputStreamObject5 = new ByteArrayOutputStream();
//        if (bitmap == null&&bitmap2 == null&&bitmap3 ==null&&bitmap4==null&&bitmap5==null) {
////            createNoImage();
//            Log.d("ConvertImage","noimage");
//        } else {
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
        }
        if (bitmap2 != null) {
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject2);
        }
        if (bitmap3 != null) {
            bitmap3.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject3);
        }
        if (bitmap4 != null) {
            bitmap4.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject4);
        }
        if (bitmap5 != null) {
            bitmap5.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject5);
        }
        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
        byte[] byteArrayVar2 = byteArrayOutputStreamObject2.toByteArray();
        byte[] byteArrayVar3 = byteArrayOutputStreamObject3.toByteArray();
        byte[] byteArrayVar4 = byteArrayOutputStreamObject4.toByteArray();
        byte[] byteArrayVar5 = byteArrayOutputStreamObject5.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);
        final String ConvertImage2 = Base64.encodeToString(byteArrayVar2, Base64.DEFAULT);
        final String ConvertImage3 = Base64.encodeToString(byteArrayVar3, Base64.DEFAULT);
        final String ConvertImage4 = Base64.encodeToString(byteArrayVar4, Base64.DEFAULT);
        final String ConvertImage5 = Base64.encodeToString(byteArrayVar5, Base64.DEFAULT);
        Log.d("ConvertImage", "1 " + ConvertImage);
        Log.d("ConvertImage", "2 " + ConvertImage2.isEmpty());//true ไม่มีรูป false มีรูป
        Log.d("ConvertImage", "3 " + ConvertImage3);
        Log.d("ConvertImage", "4 " + ConvertImage4);
        Log.d("ConvertImage", "5 " + ConvertImage5);
        final String name = edtName.getText().toString();
        final String detail = edtDetail.getText().toString();
        final String price = sp_price.getSelectedItemPosition() + "";
        final String phone = edtPhone.getText().toString();
        final String people = sp_seat.getSelectedItemPosition() + "";
        final String depo = Deposite;
        final String dt_timeS = dt_timeOpen;
        final String dt_timeE = dt_timeEnd;
        String dateopen = "";
        String facilityString = "";
        String themeString = "";
        for (int i = 0; i < date.size(); i++) {
            if (i == date.size() - 1) {
                dateopen += date.get(i);
            } else {
                dateopen += date.get(i) + ":";
            }
        }
        for (int i = 0; i < facility.size(); i++) {
            if (i == facility.size() - 1) {
                facilityString += facility.get(i);
            } else {
                facilityString += facility.get(i) + ":";
            }
        }
        for (int i = 0; i < theme.size(); i++) {
            if (i == theme.size() - 1) {
                themeString += theme.get(i);
            } else {
                themeString += theme.get(i) + ":";
            }
        }

        final String finalFacility = facilityString;
        final String finalDateopen = dateopen;
        final String finalThemeString = themeString;
        Log.d("datatodb", name + " : " + detail + " : " + price + " : " + phone + " : " + people + " : " + depo + " : " + dt_timeS.isEmpty() + " : " + dt_timeE.isEmpty() + " : " + finalDateopen.isEmpty() + " : " + finalFacility + " : " + finalThemeString);
        Log.d("datatodb", theme.toString());

        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(Edit_place.this, "place is creating", "Please Wait", false, false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                Toast.makeText(Edit_place.this, string1, Toast.LENGTH_LONG).show();

                // Setting image as transparent after done uploading.
                img1.setImageResource(android.R.color.transparent);
                img2.setImageResource(android.R.color.transparent);
                img3.setImageResource(android.R.color.transparent);
                img4.setImageResource(android.R.color.transparent);
                img5.setImageResource(android.R.color.transparent);
                Intent in = new Intent(Edit_place.this, HomePlace.class);
                in.putExtra("id", UserId + "");
                in.putExtra("email", email + "");
                startActivity(in);

            }

            @Override
            protected String doInBackground(Void... params) {
                Log.d("datatodb", name + " : " + detail + " : " + price + " : " + phone + " : " + people + " : " + depo + " : " + dt_timeS + " : " + dt_timeE + " : " + finalDateopen + " : " + finalFacility + " : " + finalThemeString);
                Log.d("datatodb", theme.toString());
                Edit_place.ImageProcessClass imageProcessClass = new Edit_place.ImageProcessClass();
                HashMap<String, String> HashMapParams = new HashMap<String, String>();
                HashMapParams.put("pid", pId.toString());
                HashMapParams.put("name", name.toString());
                HashMapParams.put("id", UserId.toString());
                HashMapParams.put("email", email.toString());
                HashMapParams.put("detail", detail.toString());
                HashMapParams.put("price", price.toString());
                HashMapParams.put("phone", phone.toString());
                HashMapParams.put("people", people.toString());
                HashMapParams.put("depo", depo.toString());
                if (!finalDateopen.isEmpty()){
                    HashMapParams.put("date", finalDateopen.toString());
                }
                if (!dt_timeS.isEmpty()){
                    HashMapParams.put("timeS", dt_timeS.toString());
                }
                if (!dt_timeE.isEmpty()){
                    HashMapParams.put("timeE", dt_timeE.toString());
                }
                HashMapParams.put("facility", finalFacility.toString());
                if (!finalThemeString.isEmpty()){
                    HashMapParams.put("theme", finalThemeString.toString());
                }
                if (!ConvertImage.isEmpty()){
                    HashMapParams.put("photo", ConvertImage.toString());
                }
                if (!ConvertImage2.isEmpty()){
                    HashMapParams.put("photo", ConvertImage2.toString());
                }
                if (!ConvertImage3.isEmpty()){
                    HashMapParams.put("photo", ConvertImage3.toString());
                }
                if (!ConvertImage4.isEmpty()){
                    HashMapParams.put("photo", ConvertImage4.toString());
                }
                if (!ConvertImage5.isEmpty()){
                    HashMapParams.put("photo", ConvertImage5.toString());
                }
                Log.d("hashmap", HashMapParams.size() + "");
                Log.d("hashmap", HashMapParams.toString());
                String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);
                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass {

        public String ImageHttpRequest(String requestURL, HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject;
                BufferedReader bufferedReaderObject;
                int RC;

                url = new URL(requestURL);
                Log.d("hashmap","url : "+ url.toString());
                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(240000);

                httpURLConnectionObject.setConnectTimeout(240000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();
                Log.d("hashmap","url : "+ RC);
                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null) {

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                if (check)

                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }

    }

}
