package com.example.groupupplace;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class CreatePlace extends AppCompatActivity {
    String id = "", email = "", name = "";
    String placename, placeDetail, placeprice, placephone, placenumofseat, placedeposit;
    ArrayList<String> factlity;
    EditText edt_name, edt_detail, edt_phone;
    Spinner sp_price, spin_numseat;
    Button btn_con;
    Switch sw_depo;
    Bitmap bitmap;
    boolean check = true;
    CheckBox parking, wifi, creditCard, kid, air, privateRoom, bts, mrt;
    ImageButton btn_img1, btn_img2, btn_img3, btn_img4, btn_img5;
    final int READ_EXTERNAL_PERMISSION_CODE = 1;
    ProgressDialog progressDialog;
    String ServerUploadPath = "http://www.groupupdb.com/android/createplace.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_place);
        spin_numseat = findViewById(R.id.spin_numberOfSeats);
        sp_price = findViewById(R.id.spin_priceRange);
        edt_name = findViewById(R.id.name);
        edt_detail = findViewById(R.id.details);
        edt_phone = findViewById(R.id.phone);
        btn_con = findViewById(R.id.newPlace_confirm);
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
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
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
                placename = edt_name.getText().toString();
                placeDetail = edt_detail.getText().toString();
                placeprice = sp_price.getSelectedItem().toString();
                placephone = edt_phone.getText().toString();
                placenumofseat = spin_numseat.getSelectedItem().toString();
                if (placename == null || placeDetail == null || placeprice == null || placephone == null || spin_numseat.getSelectedItemPosition() != 0 || sp_price.getSelectedItemPosition() != 0) {
                    Toast.makeText(CreatePlace.this, "Some field is empty", Toast.LENGTH_SHORT).show();
                } else {
                    addPlaceToDB();
                }
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
    }

    public void backHome(View v) {
        Intent in = new Intent(CreatePlace.this, HomePlace.class);
        in.putExtra("id", id + "");
        in.putExtra("email", email + "");
        startActivity(in);
    }

    public void selectThemePlace(View v) {
        Intent in = new Intent(CreatePlace.this, Theme.class);
        in.putExtra("id", id + "");
        in.putExtra("email", email + "");
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
                Bitmap lbp = scaleDown(dstBmp, 375, false);

                btn_img1.setImageBitmap(lbp);


            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        if (RC == 2 && RQC == RESULT_OK && I != null && I.getData() != null) {

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
                Bitmap lbp = scaleDown(dstBmp, 375, false);

                btn_img2.setImageBitmap(lbp);


            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        if (RC == 3 && RQC == RESULT_OK && I != null && I.getData() != null) {

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
                Bitmap lbp = scaleDown(dstBmp, 375, false);

                btn_img3.setImageBitmap(lbp);


            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        if (RC == 4 && RQC == RESULT_OK && I != null && I.getData() != null) {

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
                Bitmap lbp = scaleDown(dstBmp, 375, false);

                btn_img4.setImageBitmap(lbp);


            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        if (RC == 5 && RQC == RESULT_OK && I != null && I.getData() != null) {

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
                Bitmap lbp = scaleDown(dstBmp, 375, false);

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

        ByteArrayOutputStream byteArrayOutputStreamObject;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();
        if (bitmap == null) {
            class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

                @Override
                protected void onPreExecute() {

                    super.onPreExecute();
                    progressDialog = ProgressDialog.show(CreatePlace.this, "Image is Uploading", "Please Wait", false, false);
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


                }

                @Override
                protected String doInBackground(Void... params) {
                    createNoImage();
                    return "finish update name";
                }
            }
            AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
            AsyncTaskUploadClassOBJ.execute();

        } else {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
            byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
            final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);
            class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

                @Override
                protected void onPreExecute() {

                    super.onPreExecute();

                    progressDialog = ProgressDialog.show(CreatePlace.this, "Image is Uploading", "Please Wait", false, false);
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

                }

                @Override
                protected String doInBackground(Void... params) {

                    ImageProcessClass imageProcessClass = new ImageProcessClass();
                    HashMap<String, String> HashMapParams = new HashMap<String, String>();
//                    HashMapParams.put("name", name);
//                    HashMapParams.put("email", email);
                    HashMapParams.put("photo", ConvertImage);
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

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(19000);

                httpURLConnectionObject.setConnectTimeout(19000);

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

    public void addPlaceToDB() {
        Log.d("cb", factlity.toString());
    }

    public void createNoImage() {
        Log.d("cb", factlity.toString());
    }

}
