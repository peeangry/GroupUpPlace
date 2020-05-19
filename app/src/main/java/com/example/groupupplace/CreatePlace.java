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
import android.widget.ImageButton;
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

import javax.net.ssl.HttpsURLConnection;

public class CreatePlace extends AppCompatActivity {
    String id = "", email = "", name = "", detail = "", price = "", time = "", phone = "", deposit = "", people = "", peopleId = "", priceID = "";
    String placename, placeDetail, placeprice, placephone, placenumofseat, placedeposit = "0";
    ArrayList<String> factlity;
    EditText edt_name, edt_detail, edt_phone;
    Spinner sp_price, spin_numseat;
    Button btn_con, btn_time, btn_selectTheme;
    Switch sw_depo;
    Bitmap bitmap, bitmap2, bitmap3, bitmap4, bitmap5;
    boolean check = true;
    CheckBox parking, wifi, creditCard, kid, air, privateRoom, bts, mrt;
    ImageButton btn_img1, btn_img2, btn_img3, btn_img4, btn_img5;
    TextView showTime;
    final int READ_EXTERNAL_PERMISSION_CODE = 1;
    ProgressDialog progressDialog;
    String ServerUploadPath = "http://www.groupupdb.com/android/createplace.php";
    ArrayList<String> theme;
    ArrayList<String> date;
    String dt_timeOpen, dt_timeEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_place);
        showTime = findViewById(R.id.show_time);
        spin_numseat = findViewById(R.id.spin_numberOfSeats);
        sp_price = findViewById(R.id.spin_priceRange);
        edt_name = findViewById(R.id.name);
        edt_detail = findViewById(R.id.details);
        edt_phone = findViewById(R.id.phone);
        btn_con = findViewById(R.id.newPlace_confirm);
        btn_time = findViewById(R.id.select_time);
        btn_selectTheme = findViewById(R.id.newGroup_selectTheme);
        sw_depo = findViewById(R.id.sw_deposit);
        parking = findViewById(R.id.parking);
        wifi = findViewById(R.id.wifi);
        creditCard = findViewById(R.id.creditCard);
        kid = findViewById(R.id.goodForKids);
        air = findViewById(R.id.airConditioning);
        privateRoom = findViewById(R.id.privateRoom);
        bts = findViewById(R.id.bts);
        mrt = findViewById(R.id.mrt);
        btn_img1 = findViewById(R.id.image1);
        btn_img2 = findViewById(R.id.image2);
        btn_img3 = findViewById(R.id.image3);
        btn_img4 = findViewById(R.id.image4);
        btn_img5 = findViewById(R.id.image5);
        showTime.setVisibility(View.GONE);
        theme = new ArrayList<>();
        date = new ArrayList<>();
        dt_timeOpen = "";
        dt_timeEnd = "";
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        detail = getIntent().getStringExtra("detail");
        priceID = getIntent().getStringExtra("price");
        phone = getIntent().getStringExtra("phone");
        peopleId = getIntent().getStringExtra("people");
        String create = getIntent().getStringExtra("create");


        edt_name.setText(name);
        edt_detail.setText(detail);
        edt_phone.setText(phone);

        if (create.equalsIgnoreCase("create")) {

        } else {
            theme = (ArrayList<String>) getIntent().getSerializableExtra("themeSelect");
            factlity = (ArrayList<String>) getIntent().getSerializableExtra("facility");
            new CountDownTimer(100, 100) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    btn_selectTheme.setText("เลือกรูปแบบสถานที่สำเร็จ");
                    spin_numseat.setSelection(Integer.parseInt(peopleId));
                    sp_price.setSelection(Integer.parseInt(priceID));
                }
            }.start();

            Log.d("checkTheme", "theme : " + theme.toString());
        }
        Log.d("checkTheme", email + " : " + id + " : " + name + " : " + detail + " : " + priceID + " : " + phone + " : " + deposit + " : " + time + " : " + peopleId);

//        theme = (ArrayList<String>)getIntent().getSerializableExtra("themeSelect");
//        theme.add("Last");

        factlity = new ArrayList<>();
        Extend_MyHelper.checkInternetLost(this);
        ArrayAdapter<CharSequence> adp = ArrayAdapter.createFromResource(this, R.array.priceRange, android.R.layout.simple_spinner_item);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_price.setAdapter(adp);
        sp_price.setSelection(0);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numberOfSeats, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_numseat.setAdapter(adapter);
        spin_numseat.setSelection(0);

        sw_depo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    placedeposit = "1";
                    Log.d("placedeposit", placedeposit);//on
                } else {
                    placedeposit = "0";
                    Log.d("placedeposit", placedeposit);//off
                }
            }
        });
        checkFacility();
        btn_con.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ImageUploadToServerFunction();
//                placename = edt_name.getText().toString();
//                placeDetail = edt_detail.getText().toString();
//                placeprice = sp_price.getSelectedItem().toString();
//                placephone = edt_phone.getText().toString();
//                placenumofseat = spin_numseat.getSelectedItem().toString();
//                if (placename == null || placeDetail == null || placeprice == null || placephone == null || spin_numseat.getSelectedItemPosition() != 0 || sp_price.getSelectedItemPosition() != 0) {
//                    Toast.makeText(CreatePlace.this, "Some field is empty", Toast.LENGTH_SHORT).show();
//                } else {
//                    addPlaceToDB();
//                }
            }
        });
        btn_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage(1);
            }
        });
        btn_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage(2);
            }
        });
        btn_img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage(3);
            }
        });
        btn_img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage(4);
            }
        });
        btn_img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage(5);
            }
        });
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker();
            }
        });
        btn_selectTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectThemePlace();
            }
        });
    }

    public void backHome(View v) {
        Intent in = new Intent(CreatePlace.this, HomePlace.class);
        in.putExtra("id", id + "");
        in.putExtra("email", email + "");
        startActivity(in);
    }

    public void selectThemePlace() {
        Intent in = new Intent(CreatePlace.this, Theme.class);
        name = edt_name.getText().toString();
        detail = edt_detail.getText().toString();
        priceID = sp_price.getSelectedItemPosition() + "";
        price = sp_price.getSelectedItem() + "";
        people = spin_numseat.getSelectedItem() + "";
        phone = edt_phone.getText().toString();
        peopleId = spin_numseat.getSelectedItemPosition() + "";

        in.putExtra("id", id + "");
        in.putExtra("email", email + "");
        in.putExtra("name", name + "");
        in.putExtra("detail", detail + "");
        in.putExtra("price", priceID + "");
        in.putExtra("phone", phone + "");
        in.putExtra("people", peopleId + "");
        in.putExtra("factlity", factlity);

        Log.d("checkTheme", email + " : " + id + " : " + name + " : " + detail + " : " + priceID + " : " + phone + " : " + deposit + " : " + time + " : " + peopleId);
        startActivity(in);
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

                btn_img1.setImageBitmap(lbp);


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

                btn_img2.setImageBitmap(lbp);


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

                btn_img3.setImageBitmap(lbp);


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

                btn_img4.setImageBitmap(lbp);


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

                btn_img5.setImageBitmap(lbp);


            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public void checkFacility() {
        parking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (parking.isChecked()) {
                    parking.setBackgroundResource(R.color.blueWhite);
                    factlity.add("1");
                } else {
                    parking.setBackgroundResource(R.drawable.my_style);
                    removeFacility("1");
                }

            }
        });
        wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (wifi.isChecked()) {
                    wifi.setBackgroundResource(R.color.blueWhite);
                    factlity.add("2");
                } else {
                    wifi.setBackgroundResource(R.drawable.my_style);
                    removeFacility("2");
                }

            }
        });
        creditCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (creditCard.isChecked()) {
                    creditCard.setBackgroundResource(R.color.blueWhite);
                    factlity.add("3");
                } else {
                    creditCard.setBackgroundResource(R.drawable.my_style);
                    removeFacility("3");
                }

            }
        });
        kid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (kid.isChecked()) {
                    kid.setBackgroundResource(R.color.blueWhite);
                    factlity.add("4");
                } else {
                    kid.setBackgroundResource(R.drawable.my_style);
                    removeFacility("4");
                }

            }
        });
        air.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (air.isChecked()) {
                    air.setBackgroundResource(R.color.blueWhite);
                    factlity.add("5");
                } else {
                    air.setBackgroundResource(R.drawable.my_style);
                    removeFacility("5");
                }

            }
        });
        privateRoom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (privateRoom.isChecked()) {
                    privateRoom.setBackgroundResource(R.color.blueWhite);
                    factlity.add("6");
                } else {
                    privateRoom.setBackgroundResource(R.drawable.my_style);
                    removeFacility("6");
                }

            }
        });
        bts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (bts.isChecked()) {
                    bts.setBackgroundResource(R.color.blueWhite);
                    factlity.add("7");
                } else {
                    bts.setBackgroundResource(R.drawable.my_style);
                    removeFacility("7");
                }
            }
        });
        mrt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (mrt.isChecked()) {
                    mrt.setBackgroundResource(R.color.blueWhite);
                    factlity.add("8");
                } else {
                    mrt.setBackgroundResource(R.drawable.my_style);
                    removeFacility("8");
                }
            }
        });

    }

    public void removeFacility(String id) {
        String number = "";
        for (int i = 0; i < factlity.size(); i++) {
            if (id.equals(factlity.get(i))) {
                number = i + "";
            }
        }
        factlity.remove(Integer.parseInt(number));
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

    public void requestImagePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed for access the gallery")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(CreatePlace.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_PERMISSION_CODE);
                        }
                    }).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_PERMISSION_CODE);
        }
    }

    public void getImage(int reqCode) {
        if (ContextCompat.checkSelfPermission(CreatePlace.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(CreatePlace.this, "You have already permission access gallery", Toast.LENGTH_SHORT).show();
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
        if (bitmap == null) {
            createNoImage();
        } else {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
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
            final String name = edt_name.getText().toString();
            final String detail = edt_detail.getText().toString();
            final String price = sp_price.getSelectedItemPosition()+"";
            final String phone = edt_phone.getText().toString();
            final String people = spin_numseat.getSelectedItemPosition()+"";
            final String depo = placedeposit;
            final String dt_timeS = dt_timeOpen;
            final String dt_timeE = dt_timeEnd;
            String dateopen = "";
            String facility = "";
            String themeString = "";
            Extend_MyHelper.removeDuplicateValue(date);
            for (int i = 0; i < date.size(); i++) {
                if (i == date.size() - 1) {
//                    dateopen += date.get(i);
                } else {
                    dateopen += date.get(i) + ":";
                }
            }
            for (int i = 0; i < factlity.size(); i++) {
                if (i == factlity.size() - 1) {
                    facility += factlity.get(i);
                } else {
                    facility += factlity.get(i) + ":";
                }
            }
            for (int i = 0; i < theme.size(); i++) {
                if (i == theme.size() - 1) {
                    themeString += theme.get(i);
                } else {
                    themeString += theme.get(i) + ":";
                }
            }

            final String finalFacility = facility;
            final String finalDateopen = dateopen;
            final String finalThemeString = themeString;
            Log.d("datatodb", name + " : " + detail + " : " + price + " : " + phone + " : " + people + " : " + depo + " : " + dt_timeS + " : " + dt_timeE + " : " + finalDateopen + " : " + finalFacility + " : " + finalThemeString);
            Log.d("datatodb", theme.toString());

            class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    progressDialog = ProgressDialog.show(CreatePlace.this, "กำลังสร้างสถานที่", "กรุณารอซักครู่", false, false);
                }

                @Override
                protected void onPostExecute(String string1) {

                    super.onPostExecute(string1);

                    // Dismiss the progress dialog after done uploading.
                    progressDialog.dismiss();

                    // Printing uploading success message coming from server on android app.
                    Toast.makeText(CreatePlace.this, string1, Toast.LENGTH_LONG).show();

                    // Setting image as transparent after done uploading.
                    btn_img1.setImageResource(android.R.color.transparent);
                    btn_img2.setImageResource(android.R.color.transparent);
                    btn_img3.setImageResource(android.R.color.transparent);
                    btn_img4.setImageResource(android.R.color.transparent);
                    btn_img5.setImageResource(android.R.color.transparent);
                    Intent in = new Intent(CreatePlace.this, HomePlace.class);
                    in.putExtra("id", id + "");
                    in.putExtra("email", email + "");
                    startActivity(in);

                }

                @Override
                protected String doInBackground(Void... params) {
                    Log.d("datatodb", name + " : " + detail + " : " + price + " : " + phone + " : " + people + " : " + depo + " : " + dt_timeS + " : " + dt_timeE + " : " + finalDateopen + " : " + finalFacility + " : " + finalThemeString);
                    Log.d("datatodb", theme.toString());
                    ImageProcessClass imageProcessClass = new ImageProcessClass();
                    HashMap<String, String> HashMapParams = new HashMap<String, String>();
                    HashMapParams.put("name", name.toString());
                    HashMapParams.put("id", id.toString());
                    HashMapParams.put("email", email.toString());
                    HashMapParams.put("detail", detail.toString());
                    HashMapParams.put("price", price.toString());
                    HashMapParams.put("phone", phone.toString());
                    HashMapParams.put("people", people.toString());
                    HashMapParams.put("depo", depo.toString());
                    HashMapParams.put("date", finalDateopen.toString());
                    HashMapParams.put("timeS", dt_timeS.toString());
                    HashMapParams.put("timeE", dt_timeE.toString());
                    HashMapParams.put("facility", finalFacility.toString());
                    HashMapParams.put("theme", finalThemeString.toString());
                    HashMapParams.put("photo", ConvertImage.toString());
                    HashMapParams.put("photo2", ConvertImage2.toString());
                    HashMapParams.put("photo3", ConvertImage3.toString());
                    HashMapParams.put("photo4", ConvertImage4.toString());
                    HashMapParams.put("photo5", ConvertImage5.toString());

                    Log.d("hashmap", HashMapParams.size() + "");
                    Log.d("hashmap", HashMapParams.toString());
                    String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);
                    return FinalData;
                }
            }
            AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
            AsyncTaskUploadClassOBJ.execute();
        }

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


    public void createNoImage() {
        final String name = edt_name.getText().toString();
        final String detail = edt_detail.getText().toString();
        final String price = sp_price.getSelectedItemPosition()+"";
        final String phone = edt_phone.getText().toString();
        final String people = spin_numseat.getSelectedItemPosition()+"";
        final String depo = placedeposit;
        final String dt_timeS = dt_timeOpen;
        final String dt_timeE = dt_timeEnd;
        String dateopen = "";
        String facility = "";
        String themeString = "";
        for (int i = 0; i < date.size(); i++) {
            if (i == date.size() - 1) {
                dateopen += date.get(i);
            } else {
                dateopen += date.get(i) + ":";
            }
        }
        for (int i = 0; i < factlity.size(); i++) {
            if (i == factlity.size() - 1) {
                facility += factlity.get(i);
            } else {
                facility += factlity.get(i) + ":";
            }
        }
        for (int i = 0; i < theme.size(); i++) {
            if (i == theme.size() - 1) {
                themeString += theme.get(i);
            } else {
                themeString += theme.get(i) + ":";
            }
        }

        final String finalFacility = facility;
        final String finalDateopen = dateopen;
        final String finalThemeString = themeString;
        Log.d("datatodb", name + " : " + detail + " : " + price + " : " + phone + " : " + people + " : " + depo + " : " + dt_timeS + " : " + dt_timeE + " : " + finalDateopen + " : " + finalFacility + " : " + finalThemeString);
        Log.d("datatodb", theme.toString());
        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(CreatePlace.this, "กำลังสร้างสถานที่", "กรุณารอซักครู่", false, false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                Toast.makeText(CreatePlace.this, string1, Toast.LENGTH_LONG).show();

                // Setting image as transparent after done uploading.
                Intent in = new Intent(CreatePlace.this, HomePlace.class);
                in.putExtra("id", id + "");
                in.putExtra("email", email + "");
                startActivity(in);

            }

            @Override
            protected String doInBackground(Void... params) {
                Log.d("datatodb", name + " : " + detail + " : " + price + " : " + phone + " : " + people + " : " + depo + " : " + dt_timeS + " : " + dt_timeE + " : " + finalDateopen + " : " + finalFacility + " : " + finalThemeString);
                Log.d("datatodb", theme.toString());
                ImageProcessClass imageProcessClass = new ImageProcessClass();
                HashMap<String, String> HashMapParams = new HashMap<String, String>();
                HashMapParams.put("name", name);
                HashMapParams.put("id", id);
                HashMapParams.put("email", email);
                HashMapParams.put("detail", detail);
                HashMapParams.put("price", price);
                HashMapParams.put("phone", phone);
                HashMapParams.put("people", people);
                HashMapParams.put("depo", depo);
                HashMapParams.put("date", finalDateopen);
                HashMapParams.put("timeS", dt_timeS);
                HashMapParams.put("timeE", dt_timeE);
                HashMapParams.put("facility", finalFacility);
                HashMapParams.put("theme", finalThemeString);

                Log.d("hashmap", HashMapParams.size() + "");
                Log.d("hashmap", HashMapParams.toString());
                String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);
                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

    public void timePicker() {
        final AlertDialog viewTime = new AlertDialog.Builder(CreatePlace.this).create();
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
                mTimePicker = new TimePickerDialog(CreatePlace.this, new TimePickerDialog.OnTimeSetListener() {
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
                mTimePicker = new TimePickerDialog(CreatePlace.this, new TimePickerDialog.OnTimeSetListener() {
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
                showTime.setVisibility(View.VISIBLE);
                dt_timeOpen = edt_timeOne.getText().toString();
                dt_timeEnd = edt_timeTwo.getText().toString();
                if (dt_timeOpen.isEmpty()||dt_timeEnd.isEmpty()){
                    final AlertDialog viewDetail = new AlertDialog.Builder(CreatePlace.this).create();
                    viewDetail.setTitle("กรุณากำหนดเวลาเปิด-ปิด");
                    viewDetail.setButton(viewDetail.BUTTON_POSITIVE,"เสร็จสิ้น", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewDetail.dismiss();
                        }
                    });
                    viewDetail.show();
                    Button btnPositive = viewDetail.getButton(AlertDialog.BUTTON_POSITIVE);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                    layoutParams.weight=15;
                    btnPositive.setLayoutParams(layoutParams);
                }else {
                    showTime.setText(showStringDay(date) + "เวลา " + dt_timeOpen + " - " + dt_timeEnd);
                    btn_time.setVisibility(View.GONE);
                    viewTime.dismiss();
                }
            }
        });

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

}
