package com.example.groupupplace;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
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

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    public static ArrayList getEventStatusPriorty(String uid, String eid, String pri, Context context) {
        final ArrayList allId = new ArrayList(); //format = eid:statusid:priority->0:1:2
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getidintran.php";
        url += "?sId=" + uid;//ร  อเอาIdหรือ email จากfirebase
        url += "&eId=" + eid;
        url += "&pri=" + pri;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> map = null;
                            JSONArray data = new JSONArray(response.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("trans_id", c.getString("trans_id"));
                                map.put("user_id", c.getString("user_id"));
                                map.put("events_id", c.getString("events_id"));
                                map.put("states_id", c.getString("states_id"));
                                map.put("pri_id", c.getString("pri_id"));
                                map.put("states_name", c.getString("states_name"));
                                MyArrList.add(map);
                            }
                            allId.add(MyArrList.get(0).get("events_id"));
                            allId.add(MyArrList.get(0).get("states_id"));
                            allId.add(MyArrList.get(0).get("pri_id"));
                            allId.add(MyArrList.get(0).get("states_name"));

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
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
        return allId;
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
    public static class SendHttpRequestTask extends AsyncTask<String, Void, Bitmap> {
        String urlImg ;
        ImageView imb;
        float imgSize;
        public SendHttpRequestTask(String url ,ImageView image,float size){
            urlImg=url;
            imb=image;
            imgSize =size;
        }
        @Override
        protected Bitmap doInBackground(String... params) {

            try {
                URL url = new URL(urlImg);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                Bitmap lbp = scaleDown(myBitmap, imgSize, false);
                Log.d("http123", connection.toString());
                return lbp;

            } catch (Exception e) {
                Log.d("http123", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imb.setImageBitmap(result);
        }
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

}
