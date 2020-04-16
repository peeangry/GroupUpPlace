package com.example.groupupplace;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

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

import static android.content.Context.CONNECTIVITY_SERVICE;

public class Extend_MyHelper {
    public static void checkInternetLost(Context context){
        //////////////////////check status internet///////////////////////
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            //เมื่อมีการเชื่อมต่ออินเตอร์เน็ต
//            Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
            //ไม่มีการเชื่อมต่อ
        }
        //////////////////////check status internet///////////////////////
    }

}
